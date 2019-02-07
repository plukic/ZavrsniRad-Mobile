package ba.ito.assistance.ui.nearest_gas_stations;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;


@EpoxyModelClass(layout = R.layout.list_item_gas_station_item)
public abstract class NearestGasStationViewModel extends EpoxyModelWithHolder<NearestGasStationViewModel.GasStationViewHolder> {

    @EpoxyAttribute
    String urlImage;

    @EpoxyAttribute
    String gasStationName;
    @EpoxyAttribute
    String location;

    @EpoxyAttribute
    String price;
    @EpoxyAttribute
    String distance;


    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    RequestManager requestManager;


    @Override
    public void unbind(@NonNull GasStationViewHolder holder) {

    }


    @Override
    public void bind(@NonNull GasStationViewHolder holder) {
        requestManager.load(urlImage).apply(new RequestOptions()
                .placeholder(R.drawable.ic_petrol)
                .error(R.drawable.ic_petrol)
        ).into(holder.ivGasStationIcon);
        holder.tvGasStationName.setText(gasStationName);
        holder.tvGasStationLocation.setText(location);
        holder.tvGasStationPrice.setText(price);
        holder.tvGasStationDistance.setText(distance);
    }

    @Override
    protected GasStationViewHolder createNewHolder() {
        return new GasStationViewHolder();
    }

    class GasStationViewHolder extends BaseEpoxyHolder {
        @BindView(R.id.iv_gas_station_icon)
        ImageView ivGasStationIcon;
        @BindView(R.id.tv_gas_station_name)
        TextView tvGasStationName;
        @BindView(R.id.tv_gas_station_location)
        TextView tvGasStationLocation;
        @BindView(R.id.tv_gas_station_price)
        TextView tvGasStationPrice;
        @BindView(R.id.tv_gas_station_distance)
        TextView tvGasStationDistance;

    }
}
