package ba.ito.assistance.ui.help_request_response;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseEpoxyHolder;
import butterknife.BindView;

@EpoxyModelClass(layout = R.layout.list_item_help_request_response)
public abstract class HelpRequestResponseListItem extends EpoxyModelWithHolder<HelpRequestResponseListItem.Holder> {

    @EpoxyAttribute
    String title;

    @EpoxyAttribute
    String text;

    @EpoxyAttribute(value = EpoxyAttribute.Option.DoNotHash)
    View.OnClickListener clickListener;

    @EpoxyAttribute(value = EpoxyAttribute.Option.DoNotHash)
    Drawable drawableRight;



    @Override
    protected Holder createNewHolder() {
        return new Holder();
    }


    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvTitle.setText(title);
        holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(null,null,drawableRight,null);
        holder.tvText.setText(text);
        holder.itemView.setOnClickListener(clickListener);
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_text)
        TextView tvText;

    }

}

