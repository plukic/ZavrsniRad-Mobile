package ba.ito.assistance.ui.common;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;

@EpoxyModelClass(layout = R.layout.list_item_divider)
public abstract class ListItemDivider extends EpoxyModelWithHolder<ListItemDivider.Holder>{


    public class Holder extends BaseEpoxyHolder{

    }
}
