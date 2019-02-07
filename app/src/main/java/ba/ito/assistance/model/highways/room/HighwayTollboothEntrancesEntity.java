package ba.ito.assistance.model.highways.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import ba.ito.assistance.model.highways.api.HighwayTollboothVM;

@Entity(tableName = "HighwayTollboothEntrancesEntity",primaryKeys = {"Id","HighwayId"})
public class HighwayTollboothEntrancesEntity {
    public int Id;
    public int HighwayId;
    public String Name;
    public boolean IsActive;

    public HighwayTollboothEntrancesEntity() {
    }

    public HighwayTollboothEntrancesEntity(HighwayTollboothVM highwayTollboothVM, int highwayId) {
        this.Id=highwayTollboothVM.Id;
        this.Name=highwayTollboothVM.Name;
        this.HighwayId=highwayId;
        this.IsActive=true;

    }
}
