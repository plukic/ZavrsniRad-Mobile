package ba.ito.assistance.util;


import android.support.annotation.Nullable;


import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;

import ba.ito.assistance.BuildConfig;
import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.data.shared_prefs.SharedPrefsRepo;
import ba.ito.assistance.model.login.AuthenticationResponse;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MyAuthenticator implements Authenticator {

    public class UnauthorizedException{
        private Throwable throwable;
    }

    private SharedPrefsRepo sharedPrefsRepo;
    private IApiService apiService;
    private EventBus eventBus;
    private String clientId;
    @Inject
    public MyAuthenticator(SharedPrefsRepo sharedPrefsRepo, EventBus eventBus,@Named("client_id") String clientId) {
        this.sharedPrefsRepo = sharedPrefsRepo;
        this.eventBus = eventBus;
        this.clientId = clientId;
        OkHttpClient okHttpClient = new OkHttpClient();
        Retrofit build = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = build.create(IApiService.class);
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        try {
            AuthenticationResponse authenticationResponse = sharedPrefsRepo.getAuthenticationResponse().blockingFirst();
            if (authenticationResponse == null || authenticationResponse.access_token == null || authenticationResponse.refresh_token == null) {
                eventBus.post(new UnauthorizedException());
                return null;
            }

            if (responseCount(response) >= 3) {
                eventBus.post(new UnauthorizedException());
                return null; // If we've failed 3 times, give up.
            }
            AuthenticationResponse ar;
            try {
                ar = apiService.RefreshUserToken("refresh_token", authenticationResponse.refresh_token,clientId).blockingFirst();
            } catch (Exception e) {
                eventBus.post(new UnauthorizedException());
                return null;
            }
            sharedPrefsRepo.setAuthenticationResponse(ar);

            // Add new header to rejected request and retry it
            return response.request().newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", ar.token_type + " " + ar.access_token)
                    .build();
        }catch (Exception e){
            Timber.e(e);
            return null;
        }
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }


}