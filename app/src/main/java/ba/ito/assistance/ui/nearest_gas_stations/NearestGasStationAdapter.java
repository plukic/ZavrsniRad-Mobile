package ba.ito.assistance.ui.nearest_gas_stations;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ba.ito.assistance.R;
import ba.ito.assistance.model.gas_stations.GasStationListItemVM;
import ba.ito.assistance.services.currency.ICurrencyService;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NearestGasStationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface INearestGasStations{
        void onGasStationSelected(GasStationListItemVM gasStationVM);
    }

    private List<GasStationListItemVM> items;
    private RequestManager requestManager;
    private ICurrencyService currencyService;
    private INearestGasStations callback;
    public NearestGasStationAdapter(List<GasStationListItemVM> items, RequestManager requestManager, ICurrencyService currencyService, INearestGasStations callback) {
        this.items = items;
        this.requestManager = requestManager;
        this.currencyService = currencyService;
        this.callback = callback;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_gas_station_item, parent, false);
        return new GasStationViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((GasStationViewHolder) holder).bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void updateData(List<GasStationListItemVM> newItems) {
        //TODO ADD DIFF CALLBACK
        this.items = newItems;
        notifyDataSetChanged();
    }

    public class GasStationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
        @BindView(R.id.pb_calculating_distance)
        ProgressBar pbCalculatingDistance;
        private GasStationListItemVM item;

        public GasStationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(GasStationListItemVM vm) {
            item = vm;
            requestManager.load(vm.IconUrl).apply(new RequestOptions()
                    .placeholder(R.drawable.ic_petrol)
                    .error(R.drawable.ic_petrol)
            ).into(ivGasStationIcon);
            tvGasStationName.setText(vm.GasCompanyName);
            tvGasStationLocation.setText(String.format("%s %s", vm.GasCompanyCity, vm.GasCompanyAddress));
            tvGasStationPrice.setText(currencyService.format(vm.Currency,vm.Price));
            tvGasStationDistance.setVisibility(View.VISIBLE);
            tvGasStationDistance.setText(vm.Distance);
            pbCalculatingDistance.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            callback.onGasStationSelected(item);

        }
    }
}
