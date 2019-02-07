package ba.ito.assistance.ui.user_information;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.model.user.ClientAccountUpdateVM;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.disposables.Disposable;

public class UserInformationPresenter implements IUserInformationContract.Presenter {

    private IAccountRepo accountRepo;
    private ISchedulersProvider schedulersProvider;

    @Inject
    public UserInformationPresenter(IAccountRepo accountRepo, ISchedulersProvider schedulersProvider) {
        this.accountRepo = accountRepo;
        this.schedulersProvider = schedulersProvider;
    }

    private IUserInformationContract.View view;
    private Disposable userDataLoadSubscribe;
    private Disposable clientAccountUpdate;

    @Override
    public void refreshUserInformationData() {
        loadUserData();
    }


    @Override
    public void takeView(IUserInformationContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        loadUserData();
    }

    @Override
    public void onStop() {
        if (userDataLoadSubscribe != null)
            userDataLoadSubscribe.dispose();
        if (clientAccountUpdate != null)
            clientAccountUpdate.dispose();
    }

    @Override
    public void onUpdateValue(String newValue, UserInformationsController.UserInformationUpdateType updateType) {
        ClientAccountUpdateVM accountUpdateVM = new ClientAccountUpdateVM();
        switch (updateType) {
            case FIRST_NAME:
                accountUpdateVM.FirstName = newValue;
                break;
            case LAST_NAME:
                accountUpdateVM.LastName = newValue;
                break;
            case EMAIL:
                accountUpdateVM.Email = newValue;
                break;
            case PHONE_NUMBER:
                accountUpdateVM.PhoneNumber = newValue;
                break;
            case ADDRESS:
                accountUpdateVM.Address = newValue;
                break;
            case CITY:
                accountUpdateVM.City = newValue;
                break;
            case CHRONIC_DISEASES:
                    accountUpdateVM.ChronicDiseases=newValue;
                break;
            case DIAGNOSE:
                accountUpdateVM.Diagnose=newValue;
                break;
            case HISTORY_OF_CRITICAL_ILLNESS:
                accountUpdateVM.HistoryOfCriticalIllness=newValue;
                break;
            default:
                return;
        }


        clientAccountUpdate = accountRepo.updateUserAccountInformation(accountUpdateVM)
                .subscribeOn(schedulersProvider.network())
                .andThen(accountRepo.loadUserProfileInfo())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(clientsDetailsModel -> {
                    view.displayLoading(false);
                    view.displayUserDataUpdated(clientsDetailsModel);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });

    }


    private void loadUserData() {
        userDataLoadSubscribe = accountRepo.loadUserProfileInfo()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(clientsDetailsModel -> {
                    view.displayLoading(false);
                    view.displayUserData(clientsDetailsModel);
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }

}
