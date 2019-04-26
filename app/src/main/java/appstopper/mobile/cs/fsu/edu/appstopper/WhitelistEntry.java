package appstopper.mobile.cs.fsu.edu.appstopper;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Whitelist")
public class WhitelistEntry {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String packageName;
    public String labelName;
    public boolean isWhitelisted;
}
