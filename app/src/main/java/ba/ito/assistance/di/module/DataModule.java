package ba.ito.assistance.di.module;

import android.app.Application;

import javax.inject.Named;
import javax.inject.Singleton;

import ba.ito.assistance.R;
import ba.ito.assistance.data.account.AccountRepo;
import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.data.api.IApiService;
import ba.ito.assistance.data.cameras.CamerasRepo;
import ba.ito.assistance.data.cameras.ICamerasRepo;
import ba.ito.assistance.data.distance_api.IDistanceApi;
import ba.ito.assistance.data.gas_stations.GasStationRepo;
import ba.ito.assistance.data.gas_stations.IGasStationsRepo;
import ba.ito.assistance.data.help_request.HelpRequestRepo;
import ba.ito.assistance.data.help_request.IHelpRequestRepo;
import ba.ito.assistance.data.highway.HighwayRepo;
import ba.ito.assistance.data.highway.IHighwayRepo;
import ba.ito.assistance.data.road_conditions.IRoadConditionsRepo;
import ba.ito.assistance.data.road_conditions.RoadConditionsRepo;
import ba.ito.assistance.data.weather.IWeatherApi;
import ba.ito.assistance.data.weather.IWeatherRepo;
import ba.ito.assistance.data.weather.WeatherRepo;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public abstract class DataModule {

    @Singleton
    @Provides
    static IApiService providesApi(@Named("assistance_retrofit") Retrofit retrofit){
        return retrofit.create(IApiService.class);
    }
    @Singleton
    @Provides
    static IDistanceApi providesDistanceApi(@Named("distance_retrofit") Retrofit retrofit){
        return retrofit.create(IDistanceApi.class);
    }

    @Singleton
    @Provides
    static IWeatherApi providesWeatherApi(@Named("weather_retrofit") Retrofit retrofit){
        return retrofit.create(IWeatherApi.class);
    }

    @Singleton
    @Provides
    @Named("client_id")
    static String providesClientId(Application app){
        return app.getResources().getString(R.string.client_id);
    }

    @Singleton
    @Provides
    @Named("weather_id")
    static String providesWeatherId(Application app){
        return app.getResources().getString(R.string.weather_id);
    }

    @Singleton
    @Binds
    abstract IAccountRepo providesAccountRepo(AccountRepo accountRepo);

    @Singleton
    @Binds
    abstract IHelpRequestRepo providesHelpRequestRepo(HelpRequestRepo helpRequestRepo);

    @Singleton
    @Binds
    abstract IRoadConditionsRepo roadConditionsRepo(RoadConditionsRepo roadConditionsRepo);


    @Singleton
    @Binds
    abstract IGasStationsRepo gasStationsRepo(GasStationRepo gasStationRepo);

    @Singleton
    @Binds
    abstract IHighwayRepo highwayRepo(HighwayRepo highwayRepo);
    @Singleton
    @Binds
    abstract ICamerasRepo camerasRepo(CamerasRepo highwayRepo);
    @Singleton
    @Binds
    abstract IWeatherRepo weatherRepo(WeatherRepo weatherRepo);

}
