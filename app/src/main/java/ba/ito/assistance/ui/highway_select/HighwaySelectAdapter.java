package ba.ito.assistance.ui.highway_select;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ba.ito.assistance.R;
import ba.ito.assistance.model.highways.api.HighwayTollboothVM;
import ba.ito.assistance.model.highways.api.HighwayVM;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HighwaySelectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public interface HighwaySelectRouter {
        void onHighwaySelected(HighwayVM highwayVM);

        void onEntranceSelected(HighwayTollboothVM entrance);

        void onExitSelected(HighwayTollboothVM exit);
    }

    private List<HighwayVM> highwayVMS;
    private List<HighwayTollboothVM> entrances;
    private List<HighwayTollboothVM> exits;

    private HighwaySelectRouter callback;

    public HighwaySelectAdapter(HighwaySelectRouter callback) {
        this.highwayVMS = new ArrayList<>();
        this.entrances = new ArrayList<>();
        this.exits = new ArrayList<>();
        this.callback = callback;
    }

    public void updateHighways(List<HighwayVM> items) {
        entrances.clear();
        exits.clear();
        highwayVMS.clear();
        highwayVMS.addAll(items);
        notifyDataSetChanged();
    }

    public void updateEntrances(List<HighwayTollboothVM> items) {
        entrances.clear();
        exits.clear();
        highwayVMS.clear();
        entrances.addAll(items);
        notifyDataSetChanged();
    }

    public void updateExits(List<HighwayTollboothVM> items) {
        entrances.clear();
        exits.clear();
        highwayVMS.clear();

        exits.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_highway_select, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String content = null;
        if (highwayVMS.size()-1 >= position) {
            content = highwayVMS.get(position).Name;
        } else if (entrances.size()-1 >= position) {
            content = entrances.get(position).Name;
        } else if (exits.size()-1 >= position)
            content = exits.get(position).Name;

        ((ViewHolder) holder).bind(position, content);
    }

    @Override
    public int getItemCount() {
        return highwayVMS.size() + entrances.size() + exits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        @BindView(R.id.tv_content)
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position, String content) {
            tvContent.setText(content);
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (highwayVMS.size()-1 >= position) {
                callback.onHighwaySelected(highwayVMS.get(position));
            } else if (entrances.size()-1 >= position) {
                callback.onEntranceSelected(entrances.get(position));
            } else if (exits.size()-1 >= position) {
                callback.onExitSelected(exits.get(position));
            }
        }
    }
}
