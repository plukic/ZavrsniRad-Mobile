package ba.ito.assistance.model.highways.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import ba.ito.assistance.model.highways.api.HighwayVM;

@Entity(tableName = "Highways")
public class HighwayEntity {
    @PrimaryKey
    public int Id ;
    public String Name;
    public boolean IsActive;

    public HighwayEntity() {
    }

    public HighwayEntity(HighwayVM item) {
        this.Id=item.Id;
        this.Name=item.Name;
        this.IsActive=true;
    }
}
