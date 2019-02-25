package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    EditText editTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // editTextInput = findViewById(R.id.edit_text_input);
    }

    public void startService(View v) {
        String input = "Your device is being monitored.";

        Intent serviceIntent = new Intent(this, StopperService.class);
        serviceIntent.putExtra("inputExtra", input);

        startService(serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, StopperService.class);

        stopService(serviceIntent);
    }
}




