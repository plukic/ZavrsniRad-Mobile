package ba.ito.assistance.data.help_request;

import com.google.android.gms.maps.model.LatLng;

import javax.inject.Inject;

import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.model.firebase.FirebaseTokenUpdateVM;
import ba.ito.assistance.model.help_request.HelpRequest;
import ba.ito.assistance.model.help_request.HelpRequestCategory;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class HelpRequestRepo implements IHelpRequestRepo {
    private IApiService apiService;

    @Inject
    public HelpRequestRepo(IApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public Completable makeNewRequest(LatLng location, HelpRequestCategory helpRequestCategory) {
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.HelpRequestCategory = helpRequestCategory;
        helpRequest.Latitude = location.latitude;
        helpRequest.Longitude = location.longitude;

        return apiService.CreateHelpRequest(helpRequest);
    }

    @Override
    public Observable<HelpRequestResponseVM> GetHelpRequestResponseForClient() {
        return apiService.GetHelpRequestResponse();
    }

    @Override
    public Completable registerUserFirebaseToken(String firebaseToken, String uniqueId) {
        if(firebaseToken==null)
            return  Completable.complete();

        FirebaseTokenUpdateVM vm = new FirebaseTokenUpdateVM();
        vm.Token=firebaseToken;
        vm.UniqueMobileId=uniqueId;
        return apiService.RegisterUserFirebaseToken(vm);
    }
}
