package ba.ito.assistance.ui.user_information;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.TypedEpoxyController;

import ba.ito.assistance.R;
import ba.ito.assistance.model.user.ClientsDetailsModel;
import ba.ito.assistance.ui.my_profile.MyProfileItemsController;
import ba.ito.assistance.ui.settings_screen.TwoRowsListItemModel_;

public class UserInformationsController extends TypedEpoxyController<ClientsDetailsModel> {


    private MyProfileItemsController.MyProfileItemType myProfileItemSelectedType;

    public enum UserInformationUpdateType {
        FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER, ADDRESS, CITY, CHRONIC_DISEASES, DIAGNOSE, HISTORY_OF_CRITICAL_ILLNESS
    }

    public interface IUserInformationCallback {
        void onUserInformationUpdate(UserInformationUpdateType userInformationUpdateType, String value);
    }

    @AutoModel
    TwoRowsListItemModel_ firstName;
    @AutoModel
    TwoRowsListItemModel_ lastName;
    @AutoModel
    TwoRowsListItemModel_ cardNumber;
    @AutoModel
    TwoRowsListItemModel_ email;
    @AutoModel
    TwoRowsListItemModel_ phoneNumber;
    @AutoModel
    TwoRowsListItemModel_ address;
    @AutoModel
    TwoRowsListItemModel_ city;


    @AutoModel
    TwoRowsListItemModel_ bloodType;
    @AutoModel
    TwoRowsListItemModel_ ChronicDiseases;
    @AutoModel
    TwoRowsListItemModel_ Diagnose;
    @AutoModel
    TwoRowsListItemModel_ HistoryOfCriticalIllness;

    private Context context;
    private IUserInformationCallback callback;

    public UserInformationsController(Context context, IUserInformationCallback callback, MyProfileItemsController.MyProfileItemType selectedType) {
        this.context = context;
        this.callback = callback;
        this.myProfileItemSelectedType =selectedType;
    }

    @Override
    protected void buildModels(ClientsDetailsModel clientsDetailsModel) {
        Resources resources = context.getResources();

        String change = resources.getString(R.string.label_change);
        if (myProfileItemSelectedType == MyProfileItemsController.MyProfileItemType.USER_INFORMATION) {
            setItem(resources, R.string.label_username, clientsDetailsModel.UserName, cardNumber);
            setItem(resources, R.string.label_email, clientsDetailsModel.Email, email, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.EMAIL, clientsDetailsModel.Email);

            }, change);
            setItem(resources, R.string.label_phone_number_contact, clientsDetailsModel.PhoneNumber, phoneNumber, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.PHONE_NUMBER, clientsDetailsModel.PhoneNumber);

            }, change);


        }
        else if(myProfileItemSelectedType==MyProfileItemsController.MyProfileItemType.PERSONAL_INFORMATION){
            setItem(resources, R.string.label_firstname, clientsDetailsModel.FirstName, firstName, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.FIRST_NAME, clientsDetailsModel.FirstName);
            }, change);

            setItem(resources, R.string.label_lastname, clientsDetailsModel.LastName, lastName, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.LAST_NAME, clientsDetailsModel.LastName);

            }, change);
            setItem(resources, R.string.label_user_address, clientsDetailsModel.Address, address, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.ADDRESS, clientsDetailsModel.Address);

            }, change);

            setItem(resources, R.string.label_user_city, clientsDetailsModel.City, city, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.CITY, clientsDetailsModel.City);
            }, change);
        }
        else if(myProfileItemSelectedType==MyProfileItemsController.MyProfileItemType.HEALTH_INFORMATION) {
            setItem(resources, R.string.label_chronic_diseases, clientsDetailsModel.ChronicDiseases, ChronicDiseases, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.CHRONIC_DISEASES, clientsDetailsModel.ChronicDiseases);
            }, change);


            setItem(resources, R.string.label_diagnose, clientsDetailsModel.Diagnose, Diagnose, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.DIAGNOSE, clientsDetailsModel.Diagnose);
            }, change);

            setItem(resources, R.string.label_history_of_critical_illness, clientsDetailsModel.HistoryOfCriticalIllness, HistoryOfCriticalIllness, (view) -> {
                callback.onUserInformationUpdate(UserInformationUpdateType.HISTORY_OF_CRITICAL_ILLNESS, clientsDetailsModel.HistoryOfCriticalIllness);
            }, change);
        }

    }


    private void setItem(Resources resources, int label, String content, TwoRowsListItemModel_ model) {
        setItem(resources, label, content, model, null, null);
    }

    private void setItem(Resources resources, int label, String content, TwoRowsListItemModel_ model, View.OnClickListener clickListener, String editText) {
        if (content == null)
            content = resources.getString(R.string.not_entered);

        model.title(resources.getString(label))
                .clickListener(clickListener)
                .content(content)
                .editText(editText)
                .addTo(this);
    }
}
