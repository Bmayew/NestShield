package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT packageName FROM Whitelist")
    public List<String> getPackageNames();

    @Query("SELECT labelName FROM Whitelist")
    public List<String> getLabelNames();

    @Query("SELECT isWhitelisted FROM Whitelist WHERE packageName = :pName")
    public boolean isWhitelisted(String pName);

    @Query("SELECT labelName FROM Whitelist WHERE packageName = :pName")
    public String getLabel(String pName);

    @Query("UPDATE Whitelist SET isWhitelisted =:isWhite WHERE packageName = :pName")
    public void setWhitelisted(String pName, boolean isWhite);

    @Insert
    public void insertEntry(WhitelistEntry entry);


}
