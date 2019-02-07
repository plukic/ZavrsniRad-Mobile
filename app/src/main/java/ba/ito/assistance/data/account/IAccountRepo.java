package ba.ito.assistance.data.account;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ba.ito.assistance.model.login.AuthenticationResponse;
import ba.ito.assistance.model.settings.SettingsVM;
import ba.ito.assistance.model.user.ClientAccountUpdateVM;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import ba.ito.assistance.model.user.MobileClientCreateModel;
import ba.ito.assistance.ui.settings_screen.SettingsFragment;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IAccountRepo {
    Observable<AuthenticationResponse> loginUser(String username, String password);
    Observable<ClientsDetailsModel> loadUserProfileInfo();

    Completable LogoutCurrentUser();

    Completable confirmPasswordReset(String username, String password, String token);

    Completable requestPasswordResetToken(String username);

    Observable<Boolean> isUserLogged();


    Observable<SettingsVM> getUserSettings();

    Completable ToggleSettings(SettingsFragment.SettingsUpdate type);

    boolean IsAutomaticCrashDetectionServiceEnabled();

    boolean areNotificationsEnabled();

    Single<LatLng> getUserLastParkedLocation();

    Completable setUserLastParkedLocation(LatLng latLng);

    Observable<String> GetDeviceUniqueId();

    Completable updateUserAccountInformation(ClientAccountUpdateVM accountUpdateVM);

    Observable<List<EmergencyContactNumbers>> GetEmergencyContacts();

    Completable deleteEmergencyContact(EmergencyContactNumbers emergencyContactNumber);

    Completable updateEmergencyContact(EmergencyContactNumbers emergencyContactNumber);

    Completable createEmergencyContact(EmergencyContactNumbers emergencyContactNumber);

    Completable registerUser(MobileClientCreateModel clientCreateModel);
}
