package ba.ito.assistance.ui.emergency_contacts;

import javax.inject.Inject;

import ba.ito.assistance.data.account.IAccountRepo;
import ba.ito.assistance.model.user.EmergencyContactNumbers;
import ba.ito.assistance.util.ISchedulersProvider;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

public class EmergencyContactPresenter implements EmergencyContactsContract.Presenter {
    private EmergencyContactsContract.View view;


    private IAccountRepo accountRepo;
    private ISchedulersProvider schedulersProvider;


    private Disposable subscribeDeleteContact;
    private Disposable loadEmergencyContactsSubscribe;
    private Disposable subscribeUpdateCreateContact;

    @Inject
    public EmergencyContactPresenter(IAccountRepo accountRepo, ISchedulersProvider schedulersProvider) {
        this.accountRepo = accountRepo;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    public void refreshData() {
        loadEmergencyContacts();
    }



    @Override
    public void onDeleteContact(EmergencyContactNumbers emergencyContactNumber) {
        subscribeDeleteContact = accountRepo.deleteEmergencyContact(emergencyContactNumber)
                .subscribeOn(schedulersProvider.network())
                .andThen(accountRepo.GetEmergencyContacts())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(emergencyContactNumbers -> {
                    view.displayLoading(false);

                    if (emergencyContactNumbers.isEmpty()) {
                        view.displayNoEmergencyContacts();
                    } else {
                        view.displayEmergencyContacts(emergencyContactNumbers);
                    }
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });

    }

    @Override
    public void onEmergencyContact(EmergencyContactNumbers emergencyContactNumber, boolean isUpdateDialog) {
        Completable completable;
        if(isUpdateDialog) {
            completable= accountRepo.updateEmergencyContact(emergencyContactNumber);
        }else{
            completable=accountRepo.createEmergencyContact(emergencyContactNumber);
        }


        subscribeUpdateCreateContact = completable
                    .subscribeOn(schedulersProvider.network())
                    .andThen(accountRepo.GetEmergencyContacts())
                    .observeOn(schedulersProvider.main())
                    .doOnSubscribe(disposable -> view.displayLoading(true))
                    .subscribe(emergencyContactNumbers -> {
                        view.displayLoading(false);

                        if (emergencyContactNumbers.isEmpty()) {
                            view.displayNoEmergencyContacts();
                        } else {
                            view.displayEmergencyContacts(emergencyContactNumbers);
                        }
                    }, throwable -> {
                        view.displayLoading(false);
                        view.displayUnexpectedError();
                    });
    }


    @Override
    public void takeView(EmergencyContactsContract.View view) {
        this.view = view;
    }

    @Override
    public void dropView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        loadEmergencyContacts();
    }

    @Override
    public void onStop() {
        if (loadEmergencyContactsSubscribe != null)
            loadEmergencyContactsSubscribe.dispose();
    }

    private void loadEmergencyContacts() {
        loadEmergencyContactsSubscribe = accountRepo.GetEmergencyContacts()
                .subscribeOn(schedulersProvider.network())
                .observeOn(schedulersProvider.main())
                .doOnSubscribe(disposable -> view.displayLoading(true))
                .subscribe(emergencyContactNumbers -> {
                    view.displayLoading(false);

                    if (emergencyContactNumbers.isEmpty()) {
                        view.displayNoEmergencyContacts();
                    } else {
                        view.displayEmergencyContacts(emergencyContactNumbers);
                    }
                }, throwable -> {
                    view.displayLoading(false);
                    view.displayUnexpectedError();
                });
    }
}
