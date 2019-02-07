package ba.ito.assistance.util.Mapper;

import javax.inject.Inject;

import ba.ito.assistance.model.room.RoadCondition.RoadConditionEntity;
import ba.ito.assistance.model.road_condition.RoadConditionVM;

public class RoadConditionsMapper extends Mapper<RoadConditionVM, RoadConditionEntity> {


    @Inject
    public RoadConditionsMapper() {
    }

    @Override
    public RoadConditionEntity map(RoadConditionVM value) {
        RoadConditionEntity entity = new RoadConditionEntity();
        entity.Id = value.Id;
        entity.ValidUntil = value.ValidUntil;
        entity.RoadConditionType = value.RoadConditionType;
        entity.Description = value.Description;
        entity.IsActive = value.IsActive;
        return entity;
    }

    @Override
    public RoadConditionVM reverseMap(RoadConditionEntity value) {
        RoadConditionVM result = new RoadConditionVM();
        result.Id = value.Id;
        result.ValidUntil = value.ValidUntil;
        result.RoadConditionType = value.RoadConditionType;
        result.Description = value.Description;
        result.IsActive = value.IsActive;
        result.UpdatedAt=value.UpdatedAt;
        return result;
    }
}
