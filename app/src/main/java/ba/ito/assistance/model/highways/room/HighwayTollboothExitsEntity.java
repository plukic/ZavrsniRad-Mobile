package ba.ito.assistance.model.highways.room;

import android.arch.persistence.room.Entity;

import ba.ito.assistance.model.highways.api.HighwayTollboothVM;

@Entity(tableName = "HighwayTollboothExitsEntity",primaryKeys = {"Id","HighwayId","EntranceId"})
public class HighwayTollboothExitsEntity {
    public int Id;
    public int HighwayId;
    public int EntranceId;
    public String Name;
    public boolean IsActive;


    public HighwayTollboothExitsEntity() {
    }

    public HighwayTollboothExitsEntity(HighwayTollboothVM highwayTollboothVM, int highwayId, int entranceId) {
        this.Id=highwayTollboothVM.Id;
        this.Name=highwayTollboothVM.Name;
        this.HighwayId=highwayId;
        this.IsActive=true;
        this.EntranceId=entranceId;

    }
}
