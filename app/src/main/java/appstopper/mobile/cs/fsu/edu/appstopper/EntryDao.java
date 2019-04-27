package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM Whitelist")
    public List<WhitelistEntry> getAll();

    @Query("SELECT packageName FROM Whitelist")
    public List<String> getPackageNames();

    @Query("SELECT labelName FROM Whitelist")
    public List<String> getLabelNames();

    @Query("SELECT isWhitelisted FROM Whitelist WHERE packageName = :pName")
    public LiveData<Boolean> isWhitelisted(String pName);

    @Query("SELECT labelName FROM Whitelist WHERE packageName = :pName")
    public String getLabel(String pName);

    @Insert
    public void insertEntry(WhitelistEntry... entries);

    @Update
    public void updateEntry(WhitelistEntry... entries);



}
