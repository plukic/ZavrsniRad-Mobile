package ba.ito.assistance.ui.highway_select;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.data.highway.IHighwayRepo;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.services.cache.ICacheService;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class HighwaySelectPresenter implements HighwaySelectContract.Presenter {

    private IHighwayRepo highwayRepo;
    private ISchedulersProvider schedulersProvider;
    private ICacheService cacheService;
    private HighwaySelectContract.View view;

    private HighwaySelectActivity.HighwaySelectType type;
    private HighwayVM selectedHighway;
    private HighwayTollboothVM selectedEntrance;

    private Disposable exitsSubscribe;
    private Disposable entranceSubscribe;
    private Disposable highwaySubscribe;
    private Disposable highwayRefreshSubscribe;
    private Disposable exitsRefreshSubscribe;
    private Disposable entrancesRefreshSubscribe;
    private Disposable lastDateUpdate;

    @Inject
    public HighwaySelectPresenter(IHighwayRepo highwayRepo,
                                  HighwaySelectActivity.HighwaySelectType type, @Nullable HighwayVM selectedHighway, @Nullable HighwayTollboothVM selectedEntrance, ISchedulersProvider schedulersProvider, ICacheService cacheService) {
        this.highwayRepo = highwayRepo;
        this.type = type;
        this.selectedHighway = selectedHighway;
        this.selectedEntrance = selectedEntrance;
        this.schedulersProvider = schedulersProvider;
        this.cacheService = cacheService;
    }


    @Override
    public void refreshData(boolean forceRefresh) {
        switch (type) {
            case HIGHWAY:
                refreshHighways(forceRefresh);
                break;
            case ENTRANCE:
                refreshEntrances(forceRefresh);
                break;
            case EXIT:
                refreshExits(forceRefresh);
                break;
        }
    }
    @NonNull
    private Consumer<DateTime> onDataRefreshSuccess() {
        return (dateTime) -> {
            view.displayLoading(false);
            view.displayLastUpdateTime(dateTime);
        };
    }
    @Override
    public void loadData() {
        switch (type) {
            case HIGHWAY:
                subscribeForHighwayChanges();
                refreshHighways(false);
                break;
            case ENTRANCE:
                subscribeForEntranceChanges();
                refreshEntrances(false);
                break;
            case EXIT:
                subscribeForExitChanges();
                refreshExits(false);
                break;
        }
    }

    private void processTitle() {
        Single<DateTime> lastUpdateSingle = null;
        switch (type) {
            case HIGHWAY:
                view.displayTitle(R.string.title_select_highways);
                lastUpdateSingle = cacheService.highwayListLastUpdate();
                break;
            case ENTRANCE:
                view.displayTitle(R.string.title_select_entrance);
                lastUpdateSingle = cacheService.highwayEntranceListLastUpdate(selectedHighway.Id);
                break;
            case EXIT:
                view.displayTitle(R.string.title_select_exit);
                lastUpdateSingle = cacheService.highwayExitListLastUpdate(selectedHighway.Id,selectedEntrance.Id);
                break;
        }

        if (lastUpdateSingle == null)
            return;
        lastDateUpdate = lastUpdateSingle
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(dateTime -> view.displayLastUpdateTime(dateTime), Timber::e);
    }


    //region ActivityLifecycle
    @Override
    public void onStart() {
        loadData();
        processTitle();

    }
    @Override
    public void onStop() {
        if (highwaySubscribe != null)
            highwaySubscribe.dispose();
        if (entranceSubscribe != null)
            entranceSubscribe.dispose();
        if (exitsSubscribe != null)
            exitsSubscribe.dispose();
        if (highwayRefreshSubscribe != null)
            highwayRefreshSubscribe.dispose();
        if (entrancesRefreshSubscribe != null)
            entrancesRefreshSubscribe.dispose();
        if (exitsRefreshSubscribe != null)
            exitsRefreshSubscribe.dispose();
        if (lastDateUpdate != null)
            lastDateUpdate.dispose();

    }
    @Override
    public void takeView(HighwaySelectContract.View view) {
        this.view = view;
    }
    @Override
    public void dropView() {
        this.view = null;
    }
    //endregion

    //region Highways
    private void refreshHighways(boolean forceRefresh) {
        highwayRefreshSubscribe = highwayRepo.LoadHighways(forceRefresh)
                .subscribeOn(schedulersProvider.network())
                .andThen(cacheService.highwayListLastUpdate())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(onDataRefreshSuccess(), throwable -> {
                    view.displayLoading(false);
                    view.displayRefreshHighwaysFailed();
                });
    }

    private void subscribeForHighwayChanges() {
        if (highwaySubscribe != null)
            highwaySubscribe.dispose();
        highwaySubscribe = highwayRepo.GetHighways()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(subscription -> view.displayLoading(true))
                .subscribe(highwayVMS -> {
                    view.displayLoading(false);
                    if (highwayVMS.isEmpty())
                        view.displayNoData();
                    else
                        view.displayHighways(highwayVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }

    //endregion
    //region Exits
    private void refreshExits(boolean forceRefresh) {
        exitsRefreshSubscribe = highwayRepo.LoadExits(selectedHighway.Id, selectedEntrance.Id, forceRefresh)
                .subscribeOn(schedulersProvider.network())
                .andThen(cacheService.highwayExitListLastUpdate(selectedHighway.Id,selectedEntrance.Id))
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(onDataRefreshSuccess(), throwable -> {
                    view.displayLoading(false);
                    view.displayRefreshExitsFailed();
                });
    }

    private void subscribeForExitChanges() {
        if (exitsSubscribe != null)
            exitsSubscribe.dispose();
        exitsSubscribe = highwayRepo.GetHighwaysExits(selectedHighway.Id, selectedEntrance.Id)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(subscription -> view.displayLoading(true))
                .subscribe(highwayTollsboothVMS -> {
                    view.displayLoading(false);
                    if (highwayTollsboothVMS.isEmpty())
                        view.displayNoData();
                    else
                        view.displayExits(highwayTollsboothVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }

    //endregion
    //region Entrances
    private void refreshEntrances(boolean forceRefresh) {
        entrancesRefreshSubscribe = highwayRepo.LoadEntrances(selectedHighway.Id, forceRefresh)
                .subscribeOn(schedulersProvider.network())
                .andThen(cacheService.highwayEntranceListLastUpdate(selectedHighway.Id))
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(onDataRefreshSuccess(), throwable -> {
                    view.displayLoading(false);
                    view.displayRefreshEntranceFailed();
                });
    }
    private void subscribeForEntranceChanges() {

        if (entranceSubscribe != null)
            entranceSubscribe.dispose();
        entranceSubscribe = highwayRepo.GetHighwaysEntrances(selectedHighway.Id)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(subscription -> view.displayLoading(true))
                .subscribe(highwayTollsboothVMS -> {
                    view.displayLoading(false);
                    if (highwayTollsboothVMS.isEmpty())
                        view.displayNoData();
                    else
                        view.displayEntrances(highwayTollsboothVMS);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }
    //endregion
}
