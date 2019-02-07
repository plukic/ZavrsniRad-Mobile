package ba.ito.assistance.ui.user_information;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.user.ClientsDetailsModel;

public interface IUserInformationContract {
    interface  View extends BaseAsyncView<Presenter> {

        void displayUserData(ClientsDetailsModel clientsDetailsModel);

        void displayUserDataUpdated(ClientsDetailsModel clientsDetailsModel);
    }
    interface  Presenter extends BasePresenter<View> {


        void refreshUserInformationData();

        void onUpdateValue(String newValue, UserInformationsController.UserInformationUpdateType updateType);
    }
}
