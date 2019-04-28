package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class StopperService extends Service {
    private static final String TAG = "StopperService";
    UsageStatsManager usm;
    PackageManager pm;
    Timer blockTimer;
    EntryDao entryDao;

    // First time we create our service
    // Only one time in the life cycle of our service
    @Override
    public void onCreate() {
        Log.v(TAG, "CREATED");
        super.onCreate();
        entryDao = AppDatabase.getDatabase(getApplication()).entryDao();
        usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        pm = getPackageManager();
        blockTimer = new Timer();
    }

    // Pass intent
    // Which will be our input from Edit Text
    // Called every time we call startService on this service
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "On Start Command");
        // We are returning something else
        // return super.onStartCommand(intent, flags, startId);

        // ---- Handles the permanent Big Brother notification channel ---- //
        String input = intent.getStringExtra("inputExtra");
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setContentTitle("NestShield is Active")
                .setContentText(input).setSmallIcon(R.drawable.ic_android).setContentIntent(pendingIntent).setColor(0x7BABAE).build();
        // id must be > 0
        startForeground(1, notification);
        // ---- end of Notification Channel handler ---- //

        // ---- Get the current list of running apps ---- //
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            blockTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    String currentApp = "";
                    long time = System.currentTimeMillis();
                    final List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
                    SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                    if (appList != null && appList.size() > 0) {
                        for (UsageStats usageStats : appList) {
                            mySortedMap.put(usageStats.getLastTimeUsed(),
                                    usageStats);
                        }
                        // ---- currentApp = App in foreground ---- //
                        if (!mySortedMap.isEmpty()) {
                            currentApp = mySortedMap.get(
                                    mySortedMap.lastKey()).getPackageName();
                            Log.v(TAG, "Current: " + currentApp);
                        }
                        Boolean isListed = entryDao.isWhitelisted(currentApp).getValue();
                        Log.v(TAG, "Is Listed: " + isListed);
                        if (isListed != null && !isListed && pm.getLaunchIntentForPackage(currentApp) != null && !currentApp.equals("appstopper.mobile.cs.fsu.edu.appstopper")) {
                            Log.v(TAG, "Blocking: " + currentApp);
                            Intent lockIntent = new Intent(getApplicationContext(), HomeActivity.class);
                            lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(lockIntent);
                        }
                    }
                }
            }, 0, 2000); /* Time 1000 milliseconds = 1 second */
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "Service Destroyed 1");
        blockTimer.cancel();
        blockTimer.purge();
        blockTimer = null;
        super.onDestroy();
        Log.v(TAG, "Service Destroyed 2");
        stopSelf();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


