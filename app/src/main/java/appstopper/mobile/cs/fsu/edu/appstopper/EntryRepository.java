package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

public class EntryRepository {
    private EntryDao entryDao;
    private LiveData<List<WhitelistEntry>> allEntries;

    EntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        entryDao = db.entryDao();
        allEntries = entryDao.getAll();
    }

    LiveData<List<WhitelistEntry>> getAllEntries() {
        return allEntries;
    }

    public void insert(WhitelistEntry entry) {
        new insertAsyncTask(entryDao).execute(entry);
    }

    public void update(WhitelistEntry entry) {
        new updateAsyncTask(entryDao).execute(entry);
    }

    public void setIsListed(String isList, String pList) { new setAsyncTask(entryDao).execute(isList, pList);}

    private static class insertAsyncTask extends AsyncTask<WhitelistEntry, Void, Void> {

        private EntryDao mAsyncTaskDao;

        insertAsyncTask(EntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WhitelistEntry... params) {
            mAsyncTaskDao.insertEntry(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<WhitelistEntry, Void, Void> {

        private EntryDao mAsyncTaskDao;

        updateAsyncTask(EntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WhitelistEntry... params) {
            mAsyncTaskDao.updateEntry(params[0]);
            Log.v("Database", "Repo updating: " + params[0].labelName + " to " + params[0].isWhitelisted);
            return null;
        }
    }

    private static class setAsyncTask extends AsyncTask<String, Void, Void> {

        private EntryDao mAsyncTaskDao;

        setAsyncTask(EntryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            Boolean isTrue;
            isTrue = params[0].equals("true");
            mAsyncTaskDao.setWhitelisted(isTrue, params[1]);
            Log.v("Database", "Repo, update " + params[1] + " " + params[0]);

            LiveData<Boolean> is = mAsyncTaskDao.isWhitelisted(params[1]);
            Log.v("Database", "Repo worked: " + is.getValue());
            return null;
        }

    }
}

