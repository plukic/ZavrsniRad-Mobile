package ba.ito.assistance.data.cameras;

import java.util.List;

import ba.ito.assistance.model.base.PagingResultVM;
import ba.ito.assistance.model.cameras.CameraViewModel;
import io.reactivex.Flowable;

public interface ICamerasRepo {
    Flowable<List<CameraViewModel>> LoadCameras();
}
