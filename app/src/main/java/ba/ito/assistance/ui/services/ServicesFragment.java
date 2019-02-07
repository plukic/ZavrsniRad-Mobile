package ba.ito.assistance.ui.services;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ba.ito.assistance.R;
import ba.ito.assistance.ui.cameras.CamerasActivity;
import ba.ito.assistance.ui.gas_stations.GasStationActivity;
import ba.ito.assistance.ui.highways.HighwaysActivity;
import ba.ito.assistance.ui.nearest_gas_stations.NearestGasStationsActivity;
import ba.ito.assistance.ui.road_conditions.RoadConditionsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ServicesFragment extends Fragment implements ServicesController.OnServicesSelect {


    @BindView(R.id.rv_services)
    RecyclerView rvServices;
    Unbinder unbinder;



    public ServicesFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_services, container, false);
        unbinder = ButterKnife.bind(this, view);


        ServicesController controller = new ServicesController(getContext(), this);
        rvServices.setLayoutManager(new GridLayoutManager(getContext(),3));
        rvServices.setAdapter(controller.getAdapter());
        controller.requestModelBuild();

        return view;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRoadConditionsSelect() {
        startActivity(RoadConditionsActivity.getInstance(getContext()));
    }

    @Override
    public void onNearestGasStationsSelect() {
        startActivity(NearestGasStationsActivity.getInstance(getActivity()));
    }

    @Override
    public void onGasStationsSelect() {
        startActivity(GasStationActivity.getInstance(getActivity()));
    }

    @Override
    public void onTollboothSelected() {
        startActivity(HighwaysActivity.getInstance(getContext()));
    }

    @Override
    public void onCamerasSelected() {
        startActivity(CamerasActivity.GetInstance(getContext()));
    }

}
