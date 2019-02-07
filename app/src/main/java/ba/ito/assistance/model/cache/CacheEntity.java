package ba.ito.assistance.model.cache;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import dagger.Provides;

@Entity(tableName ="CacheEntity" )
public class CacheEntity {
    @PrimaryKey
    @NonNull
    public String Id;
    public DateTime LastUpdateTime;

    public CacheEntity() {
    }


    public CacheEntity(String key, DateTime dateTime) {
        this.Id=key;
        this.LastUpdateTime=dateTime;
    }
}
