package ba.ito.assistance.data.cameras;

import java.util.List;

import javax.inject.Inject;

import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.model.base.PagingResultVM;
import ba.ito.assistance.model.cameras.CameraViewModel;
import io.reactivex.Flowable;

public class CamerasRepo implements ICamerasRepo {
    private IApiService apiService;

    @Inject
    public CamerasRepo(IApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Flowable<List<CameraViewModel>> LoadCameras() {
        return apiService.GetCameras();
    }
}
