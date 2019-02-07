package ba.ito.assistance.base;

import android.support.annotation.CallSuper;
import android.view.LayoutInflater;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;
import butterknife.ButterKnife;

public abstract class BaseEpoxyHolder extends EpoxyHolder {
    public View itemView;

    @CallSuper
    @Override
    protected void bindView(View itemView) {
        this.itemView = itemView;
        ButterKnife.bind(this, itemView);
    }
}