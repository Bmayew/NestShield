package appstopper.mobile.cs.fsu.edu.appstopper;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM Whitelist")
    public LiveData<List<WhitelistEntry>> getAll();

    @Query("SELECT isWhitelisted FROM Whitelist WHERE packageName = :pName")
    public Boolean isWhitelisted(String pName);

    @Query("UPDATE Whitelist SET isWhitelisted = :isList WHERE packageName = :pName")
    public void setWhitelisted(Boolean isList, String pName);

    @Query("SELECT labelName FROM Whitelist WHERE packageName = :pName")
    public String getLabel(String pName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEntry(WhitelistEntry... entries);

    @Update
    public void updateEntry(WhitelistEntry... entries);



}
