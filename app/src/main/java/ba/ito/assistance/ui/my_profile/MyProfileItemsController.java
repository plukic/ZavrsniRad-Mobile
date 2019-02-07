package ba.ito.assistance.ui.my_profile;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.content.res.AppCompatResources;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.EpoxyController;

import ba.ito.assistance.R;
import ba.ito.assistance.ui.services.ServicesViewModel_;

public class MyProfileItemsController extends EpoxyController {
    @AutoModel
    ServicesViewModel_ userInformations;
    @AutoModel
    ServicesViewModel_ personalInformations;
    @AutoModel
    ServicesViewModel_ healthInformations;
    @AutoModel
    ServicesViewModel_ emergencyNumbers;


    private Context ctx;
    private IMyProfileItemCallback callback;


    public enum MyProfileItemType {
        USER_INFORMATION, PERSONAL_INFORMATION, HEALTH_INFORMATION, EMERGENCY_NUMBERS
    }

    public MyProfileItemsController(Context ctx, IMyProfileItemCallback callback) {
        this.ctx = ctx;
        this.callback = callback;
    }

    @Override
    protected void buildModels() {
        Resources resources = ctx.getResources();

        userInformations.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_icons8_contacts_blue))
                .title(resources.getString(R.string.label_my_profile_user_informations))
                .clickListener((view) -> {
                    callback.onMyProfileItemSelected(MyProfileItemType.USER_INFORMATION);
                })
                .addTo(this);


        personalInformations.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_icons8_user_folder))
                .title(resources.getString(R.string.label_my_profile_personal_informations))
                .clickListener((view) -> {
                    callback.onMyProfileItemSelected(MyProfileItemType.PERSONAL_INFORMATION);
                })
                .addTo(this);

        healthInformations.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_icons8_heart_with_pulse))
                .title(resources.getString(R.string.label_my_profile_health_informations))
                .clickListener((view) -> {
                    callback.onMyProfileItemSelected(MyProfileItemType.HEALTH_INFORMATION);
                })
                .addTo(this);
        emergencyNumbers.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_icons8_address_book))
                .title(resources.getString(R.string.label_my_profile_emergency_contacts_informations))
                .clickListener((view) -> {
                    callback.onMyProfileItemSelected(MyProfileItemType.EMERGENCY_NUMBERS);
                })
                .addTo(this);


    }
}