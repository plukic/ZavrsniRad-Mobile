package ba.ito.assistance.util;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import ba.ito.assistance.data.shared_prefs.SharedPrefsRepo;
import ba.ito.assistance.model.login.AuthenticationResponse;
import ba.ito.assistance.services.configuration.IConfigurationService;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyInterceptor implements Interceptor {
    private SharedPrefsRepo sharedPrefsRepo;

    @Inject
    public MyInterceptor(SharedPrefsRepo sharedPrefsRepo) {
        this.sharedPrefsRepo = sharedPrefsRepo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        AuthenticationResponse response;
        try {
            response = sharedPrefsRepo.getAuthenticationResponse().blockingFirst();
        } catch (Exception e) {
            response = null;
        }

        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-type", "application/json");

        String language = Locale.getDefault().getLanguage();
        requestBuilder.addHeader("Accept-Language", language);
        if (response != null) {
            requestBuilder.header("Authorization", response.token_type + " " + response.access_token);
        }
        requestBuilder.method(original.method(), original.body());
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
