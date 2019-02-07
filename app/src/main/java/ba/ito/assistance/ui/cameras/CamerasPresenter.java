package ba.ito.assistance.ui.cameras;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.data.cameras.ICamerasRepo;
import ba.ito.assistance.model.base.PagingResultVM;
import ba.ito.assistance.model.cameras.CameraViewModel;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import timber.log.Timber;

public class CamerasPresenter implements CamerasContract.Presenter {


    private CamerasContract.View view;
    private ICamerasRepo camerasRepo;
    private ISchedulersProvider schedulersProvider;
    private Disposable camerasLoadSubscribe;
    private Disposable refreshPicturesSubscribe;

    @Inject
    public CamerasPresenter(ICamerasRepo camerasRepo, ISchedulersProvider schedulersProvider) {
        this.camerasRepo = camerasRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void loadCameras() {
        camerasLoadSubscribe = camerasRepo.LoadCameras()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(subscription -> view.displayLoading(true))
                .subscribe(cameraViewModels -> {
                    view.displayLoading(false);
                    if (cameraViewModels.isEmpty()) {
                        stopRefreshPicturesInterval();
                        view.displayNoCameras();
                    } else {
                        startRefreshPicturesInterval();
                        view.displayCameras(cameraViewModels);
                    }
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });

    }

    private void startRefreshPicturesInterval() {
        stopRefreshPicturesInterval();
        refreshPicturesSubscribe = Observable.interval(5, TimeUnit.MINUTES)
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .subscribe(aLong -> {
                    view.refreshPictures();
                }, Timber::e);
    }

    private void stopRefreshPicturesInterval() {
        if(refreshPicturesSubscribe!=null)
            refreshPicturesSubscribe.dispose();
    }

    @Override
    public void takeView(CamerasContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {
        if(camerasLoadSubscribe!=null)
            camerasLoadSubscribe.dispose();
        stopRefreshPicturesInterval();
    }
}
