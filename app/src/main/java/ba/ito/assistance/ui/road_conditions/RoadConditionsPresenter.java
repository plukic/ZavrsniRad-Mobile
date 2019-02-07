package ba.ito.assistance.ui.road_conditions;

import android.text.Html;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.data.road_conditions.IRoadConditionsRepo;
import ba.ito.assistance.model.road_condition.RoadConditionType;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import timber.log.Timber;

public class RoadConditionsPresenter implements RoadConditionContract.Presenter {
    private RoadConditionContract.View view;

    private IRoadConditionsRepo roadConditionsRepo;
    private ISchedulersProvider schedulersProvider;

    private Disposable roadConditionLoadSubscribe;
    private Disposable roadConditionFlowableSubscribe;

    @Inject
    public RoadConditionsPresenter(IRoadConditionsRepo roadConditionsRepo, ISchedulersProvider schedulersProvider) {
        this.roadConditionsRepo = roadConditionsRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void takeView(RoadConditionContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        roadConditionFlowableSubscribe
                =
                roadConditionsRepo.GetRoadConditions()
                .subscribeOn(schedulersProvider.network())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.main())
                .subscribe(result -> {
                    Collections.sort(result, (o1, o2) -> o1.RoadConditionType.compareTo(o2.RoadConditionType));
                    if (result.isEmpty())
                        view.displayNoData();
                    else
                        view.displayRoadConditions(result);
                }, throwable -> {
                    view.displayUnexpectedError();
                });

        loadRoadConditionData();
    }

    private void loadData(boolean refresh){
        if(roadConditionLoadSubscribe!=null)
            roadConditionLoadSubscribe.dispose();
        roadConditionLoadSubscribe =
                roadConditionsRepo.LoadRoadConditions(refresh)
                        .subscribeOn(schedulersProvider.network())
                        .observeOn(schedulersProvider.main())
                        .doOnSubscribe(disposable -> view.displayLoading(true))
                        .doOnComplete(() -> view.displayLoading(false))
                        .doOnError(throwable -> view.displayLoading(false))
                        .subscribe(() -> {
                            Timber.d("Sync success");
                        }, t -> {
                            view.displayFailedToRefreshData();
                            Timber.e(t);
                        });
    }


    @Override
    public void loadRoadConditionData() {
        loadData(false);
    }



    @Override
    public void onStop() {
        if (roadConditionLoadSubscribe != null)
            roadConditionLoadSubscribe.dispose();
        if (roadConditionFlowableSubscribe != null)
            roadConditionFlowableSubscribe.dispose();
    }

    @Override
    public void refreshData() {
        if (roadConditionLoadSubscribe != null)
            roadConditionLoadSubscribe.dispose();
        loadData(true);
    }

    @Override
    public int getTabText(RoadConditionType roadConditionType) {
        switch (roadConditionType) {
            case TrafficFlow:
                return R.string.label_services_road_conditions_traffic_flow;
            case BorderCrossings:
                return R.string.label_services_road_conditions_border_crossing;
            case FerryTrafic:
                return R.string.label_services_road_conditions_water_transportation;
            case RailwayTraffic:
                return R.string.label_services_road_conditions_subway_traffic;
            case RoadWorks:
                return R.string.label_services_road_conditions_road_works;
            case TrafficForecast:
                return R.string.label_services_road_conditions_traffic_forecast;
        }
        return 0;
    }

    @Override
    public int getIcon(RoadConditionType roadConditionType) {
        switch (roadConditionType) {
            case TrafficFlow:
                return R.drawable.ic_no_entry;
            case BorderCrossings:
                return R.drawable.ic_custom_officer;
            case FerryTrafic:
                return R.drawable.ic_water_transportation;
            case RailwayTraffic:
                return R.drawable.ic_subway;
            case RoadWorks:
                return R.drawable.ic_under_construction;
            case TrafficForecast:
                return R.drawable.ic_crashed_car;
        }
        return 0;
    }
}
