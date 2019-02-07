package ba.ito.assistance.data.highway;

import java.util.List;

import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface IHighwayRepo {

    Flowable<List<HighwayVM>> GetHighways();
    Flowable<List<HighwayTollboothVM>> GetHighwaysEntrances(int highwayId);
    Flowable<List<HighwayTollboothVM>> GetHighwaysExits(int highwayId, int entranceId);
    Flowable<List<HighwayRoutePriceVM>> GetHighwayPrices(int highwayId, int entranceId, int exitId);

    Completable LoadHighways(boolean forceRefresh);
    Completable LoadExits(int highwayId, int entranceId, boolean forceRefresh);
    Completable LoadEntrances(int highwayId, boolean forceRefresh);

    Completable LoadHighwayPrices(int highwayId, int entranceId, int exitId, boolean forceRefresh);
}
