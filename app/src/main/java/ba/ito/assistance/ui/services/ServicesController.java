package ba.ito.assistance.ui.services;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.content.res.AppCompatResources;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.EpoxyController;

import ba.ito.assistance.R;

public class ServicesController extends EpoxyController {
    @AutoModel
    ServicesViewModel_ roadConditions;
    @AutoModel
    ServicesViewModel_ fuelPrices;
    @AutoModel
    ServicesViewModel_ weather;
    @AutoModel
    ServicesViewModel_ tolls;
    @AutoModel
    ServicesViewModel_ nearMe;
    @AutoModel
    ServicesViewModel_ cameras;


    public interface OnServicesSelect {
        void onRoadConditionsSelect();
        void onNearestGasStationsSelect();

        void onGasStationsSelect();

        void onTollboothSelected();

        void onCamerasSelected();
    }

    private Context ctx;
    private OnServicesSelect callback;

    public ServicesController(Context ctx, OnServicesSelect callback) {
        this.ctx = ctx;
        this.callback = callback;
    }

    @Override
    protected void buildModels() {
        Resources resources = ctx.getResources();

        roadConditions.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_car))
                .title(resources.getString(R.string.label_services_road_conditions))
                .clickListener((view) -> {
                    callback.onRoadConditionsSelect();
                })
                .addTo(this);


        fuelPrices.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_petrol))
                .title(resources.getString(R.string.label_services_fuel_prices))
                .clickListener((view)->{
                    callback.onGasStationsSelect();
                })
                .addTo(this);

        tolls.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_tollbooth))
                .title(resources.getString(R.string.label_services_toolboth))
                .clickListener((view)->{
                    callback.onTollboothSelected();
                })
                .addTo(this);


        nearMe.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_near_me))
                .title(resources.getString(R.string.label_services_near_me))
                .clickListener((view)->{
                    callback.onNearestGasStationsSelect();
                })
                .addTo(this);


        cameras.iconTop(AppCompatResources.getDrawable(ctx, R.drawable.ic_wall_mount_camera))
                .title(resources.getString(R.string.label_services_cameras))
                .clickListener((view)->{
                    callback.onCamerasSelected();
                })
                .addTo(this);

    }
}
