package ba.ito.assistance.data.help_request;

import com.google.android.gms.maps.model.LatLng;

import ba.ito.assistance.model.help_request.HelpRequestCategory;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;
import io.reactivex.Completable;
import io.reactivex.Observable;

public interface IHelpRequestRepo {

    Completable makeNewRequest(LatLng location, HelpRequestCategory helpRequestCategory);

    Observable<HelpRequestResponseVM> GetHelpRequestResponseForClient();

    Completable registerUserFirebaseToken(String firebaseToken, String uniqueId);
}

