package ba.ito.assistance.ui.emergency_contacts;

import java.util.List;

import ba.ito.assistance.base.BaseAsyncView;
import ba.ito.assistance.base.BasePresenter;
import ba.ito.assistance.model.user.EmergencyContactNumbers;

public interface EmergencyContactsContract {
    interface View extends BaseAsyncView<Presenter> {

        void displayNoEmergencyContacts();
        void displayEmergencyContacts(List<EmergencyContactNumbers> numbersList);
    }

    interface Presenter extends BasePresenter<View> {

        void refreshData();

        void onDeleteContact(EmergencyContactNumbers emergencyContactNumber);

        void onEmergencyContact(EmergencyContactNumbers emergencyContactNumber, boolean isUpdateDialog);
    }
}
