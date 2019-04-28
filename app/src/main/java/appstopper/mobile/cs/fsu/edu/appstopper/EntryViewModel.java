package appstopper.mobile.cs.fsu.edu.appstopper;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

public class EntryViewModel extends AndroidViewModel {
    private EntryRepository mRepository;
    private LiveData<List<WhitelistEntry>> mAllEntries;

    public EntryViewModel(Application application) {
        super(application);
        mRepository = new EntryRepository(application);
        mAllEntries = mRepository.getAllEntries();
    }

    LiveData<List<WhitelistEntry>> getAllEntries() {return mAllEntries;}

    public void insert(WhitelistEntry entry) {
        mRepository.insert(entry);
        Log.v("Database", "ViewModel inserted" + entry.labelName);
    }

    public void update(WhitelistEntry entry) {mRepository.update(entry);}

    public void setWhitelisted(String isWhite, String pName) {mRepository.setIsListed(isWhite, pName);}
}
