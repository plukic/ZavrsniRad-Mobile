package ba.ito.assistance.ui.road_conditions.details;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.model.road_condition.RoadConditionType;
import ba.ito.assistance.model.road_condition.RoadConditionVM;
import ba.ito.assistance.ui.road_conditions.RoadConditionContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class RoadConditionsDetailsFragment extends Fragment {
    private static final String ROAD_CONDITIONS_VM = "ROAD_CONDITIONS_VM";


    @BindView(R.id.road_condition_details)
    WebView roadConditionDetails;
    Unbinder unbinder;

    public RoadConditionsDetailsFragment() {
    }


    public static RoadConditionsDetailsFragment newInstance(RoadConditionVM vm) {
        RoadConditionsDetailsFragment fragment = new RoadConditionsDetailsFragment();
        Bundle b = new Bundle();
        b.putParcelable(ROAD_CONDITIONS_VM, vm);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_road_conditions, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (getArguments() != null) {
            RoadConditionVM vm = getArguments().getParcelable(ROAD_CONDITIONS_VM);
            if (vm != null && vm.Description != null) {
                roadConditionDetails.loadDataWithBaseURL("", vm.Description, "text/html", "utf-8", "");
            }
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}