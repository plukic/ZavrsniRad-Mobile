package ba.ito.assistance.data.room.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import ba.ito.assistance.model.cache.CacheEntity;
import io.reactivex.Single;

@Dao
public abstract class ICacheDao {

    @Query("SELECT * FROM CacheEntity WHERE Id=:id")
    public abstract Single<CacheEntity> GetCacheEntity(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void AddCacheEntity(CacheEntity entity);

}
