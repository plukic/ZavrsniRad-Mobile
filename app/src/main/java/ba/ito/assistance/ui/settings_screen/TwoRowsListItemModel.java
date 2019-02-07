package ba.ito.assistance.ui.settings_screen;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;

@EpoxyModelClass(layout = R.layout.list_item_settings_item)
public abstract class TwoRowsListItemModel extends EpoxyModelWithHolder<TwoRowsListItemModel.Holder> {

    @EpoxyAttribute
    String content;
    @EpoxyAttribute
    String title;
    @EpoxyAttribute(DoNotHash)
    View.OnClickListener clickListener;

    @EpoxyAttribute
    String editText;



    @Override
    public void unbind(@NonNull Holder holder) {
        holder.itemView.setOnClickListener(null);
    }


    @Override
    public void bind(@NonNull Holder holder) {
        holder.itemView.setOnClickListener(clickListener);
        holder.tvContent.setText(content);
        holder.tvTitle.setText(title);
        holder.tvEdit.setText(editText);
    }

    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_edit)
        TextView tvEdit;

    }

}
