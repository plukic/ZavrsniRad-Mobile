package ba.ito.assistance.model.room.RoadCondition;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import com.google.android.gms.maps.internal.IStreetViewPanoramaDelegate;

import org.joda.time.DateTime;

import java.util.List;

import ba.ito.assistance.model.road_condition.RoadConditionVM;

@Entity(tableName = "RoadConditions")
public class RoadConditionEntity {
    @PrimaryKey
    public int Id;
    public DateTime ValidUntil;
    public ba.ito.assistance.model.road_condition.RoadConditionType RoadConditionType;
    public String Description;
    public boolean IsActive;
    public DateTime UpdatedAt;

    public RoadConditionEntity() {
    }

    public RoadConditionEntity(RoadConditionVM roadConditionVM) {
        Id = roadConditionVM.Id;
        ValidUntil = roadConditionVM.ValidUntil;
        RoadConditionType = roadConditionVM.RoadConditionType;
        Description = roadConditionVM.Description;
        IsActive=roadConditionVM.IsActive;
    }
}
