package appstopper.mobile.cs.fsu.edu.appstopper;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(version = 1, entities = {WhitelistEntry.class})
public abstract class AppDatabase extends RoomDatabase {
    abstract public EntryDao entryDao();

}
