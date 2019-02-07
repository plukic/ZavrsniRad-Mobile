package ba.ito.assistance.ui.highway_prices;

import javax.inject.Inject;
import javax.inject.Named;

import ba.ito.assistance.data.highway.IHighwayRepo;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class HighwayPricesPresenter implements HighwayPricesContract.Presenter {
    private HighwayPricesContract.View view;

    private HighwayVM selectedHighway;
    private HighwayTollboothVM selectedEntrance;
    private HighwayTollboothVM selectedExit;
    private IHighwayRepo highwayRepo;
    private Disposable refreshPricesSubsribe;
    private Disposable highwayPricesChangeSubscribe;
    private ISchedulersProvider schedulersProvider;

    @Inject
    public HighwayPricesPresenter(HighwayVM selectedHighway, @Named("entrance") HighwayTollboothVM selectedEntrance, @Named("exit") HighwayTollboothVM selectedExit, IHighwayRepo highwayRepo, ISchedulersProvider schedulersProvider) {
        this.selectedHighway = selectedHighway;
        this.selectedEntrance = selectedEntrance;
        this.selectedExit = selectedExit;
        this.highwayRepo = highwayRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void refreshPrices(boolean forceRefresh) {
        if (refreshPricesSubsribe != null) {
            refreshPricesSubsribe.dispose();
            refreshPricesSubsribe = null;
        }
        refreshPricesSubsribe = highwayRepo.LoadHighwayPrices(selectedHighway.Id, selectedEntrance.Id, selectedExit.Id,forceRefresh)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(() -> view.displayLoading(false), throwable -> {
                    view.displayLoading(false);
                    view.displayRefreshingPricesFailed();
                });
    }

    @Override
    public void takeView(HighwayPricesContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        highwayPricesChangeSubscribe = highwayRepo.GetHighwayPrices(selectedHighway.Id, selectedEntrance.Id, selectedExit.Id)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(highwayRoutePriceVMS -> view.displayPrices(selectedHighway, selectedEntrance, selectedExit, highwayRoutePriceVMS),
                        throwable -> view.displayUnexpectedError());

        refreshPrices(false);
    }

    @Override
    public void onStop() {
        if (refreshPricesSubsribe != null) {
            refreshPricesSubsribe.dispose();
            refreshPricesSubsribe = null;
        }
        if (highwayPricesChangeSubscribe != null) {
            highwayPricesChangeSubscribe.dispose();
            highwayPricesChangeSubscribe = null;
        }
    }
}
