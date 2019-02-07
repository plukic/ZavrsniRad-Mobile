package ba.ito.assistance.ui.gas_stations.vm;

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


@EpoxyModelClass(layout = R.layout.list_item_gas_company_prices_header)
public abstract class GasCompanyHeaderListItem extends EpoxyModelWithHolder<GasCompanyHeaderListItem.Holder> {


    @EpoxyAttribute
    String gasStationUrl;
    @EpoxyAttribute
    String gasCompanyDescription;

    @EpoxyAttribute(EpoxyAttribute.Option.DoNotHash)
    RequestManager requestManager;


    @Override
    public void bind(@NonNull Holder holder) {
        super.bind(holder);
        requestManager.load(gasStationUrl).apply(new RequestOptions().error(R.drawable.ic_petrol).placeholder(R.drawable.ic_petrol))
                .into(holder.ivGasStationLogo);
        holder.tvGasCompanyDescription.setText(gasCompanyDescription);

    }

    @Override
    public void unbind(@NonNull Holder holder) {
        super.unbind(holder);
    }


    public class Holder extends BaseEpoxyHolder {
        @BindView(R.id.iv_gas_station_logo)
        ImageView ivGasStationLogo;
        @BindView(R.id.tv_gas_company_description)
        TextView tvGasCompanyDescription;


    }
}
