package ba.ito.assistance.di.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import ba.ito.assistance.BuildConfig;
import ba.ito.assistance.R;
import ba.ito.assistance.data.shared_prefs.SharedPreferenceHelper;
import ba.ito.assistance.data.shared_prefs.SharedPrefsRepo;
import ba.ito.assistance.services.cache.CacheService;
import ba.ito.assistance.services.configuration.ConfigurationService;
import ba.ito.assistance.services.crash_detection.CrashDetectionLogicService;
import ba.ito.assistance.services.crash_detection.ICrashDetectionLogicService;
import ba.ito.assistance.services.currency.CurrencyService;
import ba.ito.assistance.services.cache.ICacheService;
import ba.ito.assistance.services.configuration.IConfigurationService;
import ba.ito.assistance.services.currency.ICurrencyService;
import ba.ito.assistance.ui.notifications.NotificationManager;
import ba.ito.assistance.util.BaseErrorFactory;
import ba.ito.assistance.util.DateAndTimeUtil;
import ba.ito.assistance.util.DateTimeTypeAdapter;
import ba.ito.assistance.util.ISchedulersProvider;
import ba.ito.assistance.util.MyAuthenticator;
import ba.ito.assistance.util.MyInterceptor;
import ba.ito.assistance.util.MyRegex;
import ba.ito.assistance.util.SchedulersProvider;
import ba.ito.assistance.util.ServicesUtil;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public abstract class ApplicationModule {


    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);


    @Provides
    @Singleton
    @Named("google_maps_api_key")
    static String providesGoogleMapKey(Context ctx){
        return ctx.getResources().getString(R.string.google_maps_key);
    }
    @Provides
    static SharedPreferences provideSharedPrefs(Application application) {
        return application.getSharedPreferences("PREF_ASSISTO", Context.MODE_PRIVATE);
    }

    @Binds
    @Singleton
    abstract ISchedulersProvider schedulersProvider(SchedulersProvider schedulersProvider);

    @Provides
    @Singleton
    static DateTimeFormatter dateTimeFormatter(){
        return DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    }

    @Provides
    @Singleton
    static Gson gson(DateTimeFormatter dateTimeFormatter) {
        return new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter(dateTimeFormatter))
                .create();
    }

    @Provides
    @Singleton
    static SharedPrefsRepo sharedPrefsRepo(SharedPreferences sharedPreferences, Gson gson) {
        return new SharedPrefsRepo(new SharedPreferenceHelper(sharedPreferences), gson);
    }

    @Binds
    @Singleton
    abstract IConfigurationService configurationService(ConfigurationService configurationService);
    @Binds
    @Singleton
    abstract ICrashDetectionLogicService  crashDetectionLogicService(CrashDetectionLogicService crashDetectionLogicService);

    @Provides
    @Singleton
    static NotificationManager notificationManager(){
        return new NotificationManager();
    }

    @Provides
    @Singleton
    static ServicesUtil servicesUtil(){
        return new ServicesUtil();
    }


    @Binds
    @Singleton
    abstract ICurrencyService currencyService(CurrencyService currencyService);

    @Binds
    @Singleton
    abstract Interceptor authInterceptor(MyInterceptor myInterceptor);


    @Provides
    @Singleton
    static EventBus providesEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    @Singleton
    static Authenticator providesAuthenticator(SharedPrefsRepo sharedPrefsRepo, EventBus eventBus,@Named("client_id") String clientId) {
        return new MyAuthenticator(sharedPrefsRepo, eventBus, clientId);
    }

    @Provides
    @Singleton
    static OkHttpClient providesOkHttp(Interceptor interceptor, Authenticator authenticator) {
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .authenticator(authenticator)
                .connectTimeout(20,TimeUnit.SECONDS)
                .readTimeout(20,TimeUnit.SECONDS)
                .build();
    }


    @Provides
    @Singleton
    @Named("assistance_retrofit")
    static Retrofit providesAssistanceRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    @Provides
    @Singleton
    @Named("distance_retrofit")
    static Retrofit providesRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.DISTANCE_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    @Named("weather_retrofit")
    static Retrofit providesWeatherRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.WEATHER_API)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    @Provides
    @Singleton
    static BaseErrorFactory baseErrorFactory(Gson gson, Context ctx) {
        return new BaseErrorFactory(gson, ctx.getResources());
    }

    @Provides
    @Singleton
    static MyRegex myRegex() {
        return new MyRegex();
    }

    @Provides
    @Singleton
    static DateAndTimeUtil dateAndTimeUtil() {
        return new DateAndTimeUtil();
    }


    @Binds
    @Singleton
    abstract ICacheService cacheService(CacheService cacheService);

}
