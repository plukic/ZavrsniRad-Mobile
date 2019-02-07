package ba.ito.assistance.ui.cameras;

import java.util.List;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.cameras.CameraViewModel;

public interface CamerasContract {

    interface Presenter extends BasePresenter<View> {
        void loadCameras();

    }

    interface View extends BaseAsyncView<Presenter> {
        void displayCameras(List<CameraViewModel> cameras);
        void displayNoCameras();

        void refreshPictures();
    }
}
