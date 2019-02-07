package ba.ito.assistance.data.shared_prefs;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.UUID;

import ba.ito.assistance.model.language.Language;
import ba.ito.assistance.model.login.AuthenticationResponse;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.model.weather.CurrentWeatherVM;
import io.reactivex.Completable;
import io.reactivex.Observable;

public class SharedPrefsRepo {
    private static final String KEY_AUTHENTICATION_RESPONSE = "KEY_AUTHENTICATION_RESPONSE";
    private static final String KEY_CLIENT_PROFILE_INFO = "KEY_CLIENT_PROFILE_INFO";
    private static final String KEY_CRASH_DETECTION = "KEY_CRASH_DETECTION";
    private static final String KEY_NOTIFICATIONS = "KEY_NOTIFICATIONS";
    private static final String KEY_SELECTED_LANGUAGE = "KEY_SELECTED_LANGUAGE";
    private static final String KEY_NIGHT_MODE = "KEY_NIGHT_MODE";
    private static final String KEY_LAST_PARKED_LOCATION = "KEY_LAST_PARKED_LOCATION";
    private static final String KEY_LAST_KNOWN_WEATHER = "KEY_LAST_KNOWN_WEATHER";
    private static final String KEY_UNIQUE_ID = "KEY_UNIQUE_ID";
    private SharedPreferenceHelper helper;
    private Gson gson;

    public SharedPrefsRepo(SharedPreferenceHelper helper, Gson gson) {
        this.helper = helper;
        this.gson = gson;
    }

    public Observable<AuthenticationResponse> getAuthenticationResponse() {
        return Observable.fromCallable(() -> {
            String sharedPreferenceString = helper.getSharedPreferenceString(KEY_AUTHENTICATION_RESPONSE, null);
            AuthenticationResponse response = gson.fromJson(sharedPreferenceString, AuthenticationResponse.class);

            if (response == null)
                response = new AuthenticationResponse();

            return response;
        });
    }

    public void setAuthenticationResponse(AuthenticationResponse authenticationResponse) {
        helper.setSharedPreferenceString(KEY_AUTHENTICATION_RESPONSE, gson.toJson(authenticationResponse));
    }

    public void setClientInfo(ClientsDetailsModel clientInfo) {
        helper.setSharedPreferenceString(KEY_CLIENT_PROFILE_INFO, gson.toJson(clientInfo));
    }

    public ClientsDetailsModel getClientInfo() {
        String sharedPreferenceString = helper.getSharedPreferenceString(KEY_CLIENT_PROFILE_INFO, null);
        return gson.fromJson(sharedPreferenceString, ClientsDetailsModel.class);
    }

    public Completable LogoutCurrentUser() {
        return Completable.create(emitter -> {
            helper.setSharedPreferenceString(KEY_AUTHENTICATION_RESPONSE, null);
            helper.setSharedPreferenceString(KEY_CLIENT_PROFILE_INFO, null);
            emitter.onComplete();
        });
    }

    public boolean IsAutomaticCrashDetectionEnabled() {
        return helper.getSharedPreferenceBoolean(KEY_CRASH_DETECTION, true);
    }

    public boolean AreNotificationsEnabled() {
        return helper.getSharedPreferenceBoolean(KEY_NOTIFICATIONS, true);
    }

    public void SetCrashDetection(boolean isEnabled) {
        helper.setSharedPreferenceBoolean(KEY_CRASH_DETECTION, isEnabled);
    }

    public void SetNotifications(boolean isEnabled) {
        helper.setSharedPreferenceBoolean(KEY_NOTIFICATIONS, isEnabled);
    }

    public Language GetSelectedLanguage() {
        String sharedPreferenceString = helper.getSharedPreferenceString(KEY_SELECTED_LANGUAGE, null);
        return gson.fromJson(sharedPreferenceString, Language.class);
    }

    public void SetSelectedLanguage(Language selectedLanguage) {
        String sharedPreferenceString = gson.toJson(selectedLanguage);
        helper.setSharedPreferenceString(KEY_SELECTED_LANGUAGE, sharedPreferenceString);
    }

    public boolean IsNightModeEnabled() {
        return helper.getSharedPreferenceBoolean(KEY_NIGHT_MODE, false);

    }

    public void SetNightMode(boolean isEnabled) {
        helper.setSharedPreferenceBoolean(KEY_NIGHT_MODE, isEnabled);

    }

    public LatLng GetLastParkedLocation() {
        String sharedPreferenceString = helper.getSharedPreferenceString(KEY_LAST_PARKED_LOCATION, null);
        return gson.fromJson(sharedPreferenceString, LatLng.class);

    }

    public void SetLastParkedLocation(LatLng location) {
        helper.setSharedPreferenceString(KEY_LAST_PARKED_LOCATION, gson.toJson(location));
    }

    public void setWeatherApi(CurrentWeatherVM currentWeatherVM) {
        helper.setSharedPreferenceString(KEY_LAST_KNOWN_WEATHER, gson.toJson(currentWeatherVM));

    }

    public CurrentWeatherVM getCurrentWeather() {
        String sharedPreferenceString = helper.getSharedPreferenceString(KEY_LAST_KNOWN_WEATHER, null);
        return gson.fromJson(sharedPreferenceString, CurrentWeatherVM.class);
    }

    public String GetUniqueId() {
        String uniqueId = helper.getSharedPreferenceString(KEY_UNIQUE_ID, null);
        if (uniqueId == null) {
            uniqueId = UUID.randomUUID().toString();
            helper.setSharedPreferenceString(KEY_UNIQUE_ID, uniqueId);
        }
        return uniqueId;
    }
}
