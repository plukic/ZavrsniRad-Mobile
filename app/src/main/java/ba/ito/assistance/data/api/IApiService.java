package ba.ito.assistance.data.api;



import java.util.List;
import java.util.Map;

import ba.ito.assistance.model.cameras.CameraViewModel;
import ba.ito.assistance.model.firebase.FirebaseTokenUpdateVM;
import ba.ito.assistance.model.gas_stations.FuelTypeEnum;
import ba.ito.assistance.model.gas_stations.GasCompanyFuelPrices;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.model.gas_stations.GasStationLocationVM;
import ba.ito.assistance.model.help_request.HelpRequest;
import ba.ito.assistance.model.help_request.HelpRequestResponseVM;
import ba.ito.assistance.model.highways.api.HighwayRoutePriceVM;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import ba.ito.assistance.model.login.AuthenticationResponse;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import ba.ito.assistance.model.user.ClientAccountUpdateVM;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.model.user.ClientConfirmResetPasswordVM;
import ba.ito.assistance.model.user.ClientResetPasswordVM;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import ba.ito.assistance.model.user.MobileClientCreateModel;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IApiService {
    @POST("token")
    @FormUrlEncoded
    Observable<AuthenticationResponse> LoginUser(@Field("grant_type") String grant_type,
                                                 @Field("username") String username,
                                                 @Field("password") String password,
                                                 @Field("client_id") String clientId);

    @POST("token")
    @FormUrlEncoded
    Observable<AuthenticationResponse> RefreshUserToken(@Field("grant_type") String grantType,
                                                        @Field("refresh_token") String refreshToken,
                                                        @Field("client_id")  String clientId);

    @GET("api/client/profile")
    Observable<ClientsDetailsModel> GetClientProfileInfo();

    @POST("api/user/maintenance/resetpassword/confirm")
    Completable ConfirmPasswordReset(@Body ClientConfirmResetPasswordVM clientConfirmResetPasswordVM);
    @POST("api/user/maintenance/resetpassword")
    Completable ResetPassword(@Body ClientResetPasswordVM clientConfirmResetPasswordVM);


    @POST("api/helprequests")
    Completable CreateHelpRequest(@Body HelpRequest helpRequest);

    @GET("api/roadconditions/latest")
    Observable<List<RoadConditionVM>> GetRoadConditions();
    @GET("api/gasstations")
    Observable<List<GasStationListItemVM>> GetGasStationsInfo(@Query("fuelType") FuelTypeEnum fuelType);

    @GET("api/gascompany/prices")
    Observable<Map<FuelTypeEnum,List<GasCompanyFuelPrices>>> GetGasCompanyFuelPrices(@Query("latitude") double latitude,@Query("longitude") double longitude);
    @GET("api/gasstations/location")
    Observable<List<GasStationLocationVM>> GetGasStationWithLocations();

    @GET("api/highways")
    Observable<List<HighwayVM>> GetHighways();

    @GET("api/highways/{id}/entrances")
    Observable<List<HighwayTollboothVM>> GetHighwayEntries(@Query("id") int highwayId);
    @GET("api/highways/{id}/entrances/{entranceId}/exits")
    Observable<List<HighwayTollboothVM>> GetHighwayExits(@Query("id") int highwayId, @Query("entranceId") int entranceId);
    @GET("api/highways/{id}/entrances/{entranceId}/exits/{exitId}/prices")
    Observable<List<HighwayRoutePriceVM>> GetHighwayPrices(@Query("id") int highwayId,@Query("entranceId") int entranceId,@Query("exitId") int exitId);

    @GET("api/helprequest/response")
    Observable<HelpRequestResponseVM> GetHelpRequestResponse();
    @PUT("api/client/profile/firebase")
    Completable RegisterUserFirebaseToken(@Body FirebaseTokenUpdateVM token);
    @GET("api/cameras/active")
    Flowable<List<CameraViewModel>> GetCameras();

    @PUT("api/client/profile/account")
    Completable UpdateUserAccountInformation(@Body ClientAccountUpdateVM accountUpdateVM);

    @GET("api/emergency/numbers")
    Observable<List<EmergencyContactNumbers>> GetEmergencyContacts();
    @DELETE("api/emergency/numbers/{id}")
    Completable deleteEmergencyContact(@Path("id") int id);
    @PUT("api/emergency/numbers")
    Completable updateEmergencyContact(@Body EmergencyContactNumbers emergencyContactNumber);
    @POST("api/emergency/numbers")
    Completable createEmergencyContact(@Body EmergencyContactNumbers emergencyContactNumber);

    @POST("api/client/profile/register")
    Completable registerUser(@Body MobileClientCreateModel clientCreateModel);
}
