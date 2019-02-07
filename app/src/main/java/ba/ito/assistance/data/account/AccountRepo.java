package ba.ito.assistance.data.account;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.data.shared_prefs.SharedPrefsRepo;
import ba.ito.assistance.model.login.AuthenticationResponse;
import ba.ito.assistance.model.settings.SettingsVM;
import ba.ito.assistance.model.user.ClientAccountUpdateVM;
import ba.ito.assistance.model.user.ClientConfirmResetPasswordVM;
import ba.ito.assistance.model.user.ClientResetPasswordVM;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import ba.ito.assistance.model.user.MobileClientCreateModel;
import ba.ito.assistance.ui.settings_screen.SettingsFragment;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class AccountRepo implements IAccountRepo {
    private IApiService apiService;
    private SharedPrefsRepo sharedPrefsRepo;
    private String clientId;


    @Inject
    public AccountRepo(IApiService apiService, SharedPrefsRepo sharedPrefsRepo, @Named("client_id") String clientId) {
        this.apiService = apiService;
        this.sharedPrefsRepo = sharedPrefsRepo;
        this.clientId = clientId;
    }


    @Override
    public Observable<AuthenticationResponse> loginUser(String username, String password) {
        return apiService
                .LoginUser("password", username, password, this.clientId)
                .map(authenticationResponse -> {
                    sharedPrefsRepo.setAuthenticationResponse(authenticationResponse);
                    return authenticationResponse;
                });
    }

    @Override
    public Observable<ClientsDetailsModel> loadUserProfileInfo() {
        return apiService.GetClientProfileInfo()
                .map(clientInfo -> {
                    sharedPrefsRepo.setClientInfo(clientInfo);
                    return clientInfo;
                })
                .onErrorResumeNext((throwable) -> {
                    return Observable.just(sharedPrefsRepo.getClientInfo());
                });
    }

    @Override
    public Completable LogoutCurrentUser() {
        return sharedPrefsRepo.LogoutCurrentUser();
    }

    @Override
    public Completable confirmPasswordReset(String username, String password, String token) {
        ClientConfirmResetPasswordVM clientConfirmResetPasswordVM = new ClientConfirmResetPasswordVM();
        clientConfirmResetPasswordVM.setToken(token);
        clientConfirmResetPasswordVM.setNewPassword(password);
        clientConfirmResetPasswordVM.setUsername(username);
        return apiService.ConfirmPasswordReset(clientConfirmResetPasswordVM);
    }

    @Override
    public Completable requestPasswordResetToken(String username) {
        ClientResetPasswordVM clientResetPasswordVM = new ClientResetPasswordVM();
        clientResetPasswordVM.setUsername(username);
        return apiService.ResetPassword(clientResetPasswordVM);
    }

    @Override
    public Observable<Boolean> isUserLogged() {
        return sharedPrefsRepo.getAuthenticationResponse()
                .flatMap(authenticationResponse -> Observable.just(sharedPrefsRepo.getClientInfo()))
                .flatMap(clientAccountInfo -> Observable.just(true))
                .onErrorResumeNext(throwable -> {
                    return Observable.just(false);
                });
    }


    @Override
    public Observable<SettingsVM> getUserSettings() {
        return
                Observable.fromCallable(() -> sharedPrefsRepo.getClientInfo())
                        .flatMap(clientAccountInfo -> {
                            SettingsVM result = new SettingsVM();
                            result.Username = clientAccountInfo.UserName;
                            result.CrashDetectionEnabled = sharedPrefsRepo.IsAutomaticCrashDetectionEnabled();
                            result.NotificationsEnabled = sharedPrefsRepo.AreNotificationsEnabled();
                            result.NightModeEnabled = sharedPrefsRepo.IsNightModeEnabled();
                            return Observable.just(result);
                        });
    }

    @Override
    public Completable ToggleSettings(SettingsFragment.SettingsUpdate type) {
        return Completable.fromAction(() -> {
            switch (type) {
                case CRASH_DETECTION:
                    sharedPrefsRepo.SetCrashDetection(!sharedPrefsRepo.IsAutomaticCrashDetectionEnabled());
                    break;
                case NOTIFICATION:
                    sharedPrefsRepo.SetNotifications(!sharedPrefsRepo.AreNotificationsEnabled());
                    break;
                case NIGHT_MODE:
                    sharedPrefsRepo.SetNightMode(!sharedPrefsRepo.IsNightModeEnabled());
            }
        });
    }

    @Override
    public boolean IsAutomaticCrashDetectionServiceEnabled() {
        return sharedPrefsRepo.IsAutomaticCrashDetectionEnabled();
    }

    @Override
    public boolean areNotificationsEnabled() {
        return sharedPrefsRepo.AreNotificationsEnabled();
    }

    @Override
    public Single<LatLng> getUserLastParkedLocation() {
        return Single.fromCallable(() -> sharedPrefsRepo.GetLastParkedLocation());
    }

    @Override
    public Completable setUserLastParkedLocation(LatLng latLng) {
        return Completable.fromAction(() -> sharedPrefsRepo.SetLastParkedLocation(latLng));
    }
    @Override
    public Observable<String> GetDeviceUniqueId() {
        String s = sharedPrefsRepo.GetUniqueId();
        return Observable.just(s);
    }

    @Override
    public Completable updateUserAccountInformation(ClientAccountUpdateVM accountUpdateVM) {
        return apiService.UpdateUserAccountInformation(accountUpdateVM);
    }

    @Override
    public Observable<List<EmergencyContactNumbers>> GetEmergencyContacts() {
        return apiService.GetEmergencyContacts();
    }

    @Override
    public Completable deleteEmergencyContact(EmergencyContactNumbers emergencyContactNumber) {
        return apiService.deleteEmergencyContact(emergencyContactNumber.Id);
    }

    @Override
    public Completable updateEmergencyContact(EmergencyContactNumbers emergencyContactNumber) {
        return apiService.updateEmergencyContact(emergencyContactNumber);
    }

    @Override
    public Completable createEmergencyContact(EmergencyContactNumbers emergencyContactNumber) {
        return apiService.createEmergencyContact(emergencyContactNumber);
    }

    @Override
    public Completable registerUser(MobileClientCreateModel clientCreateModel) {

        return apiService.registerUser(clientCreateModel);
    }
}
