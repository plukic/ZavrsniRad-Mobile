package ba.ito.assistance.ui.cameras;

        import android.support.annotation.NonNull;
        import android.support.v4.widget.CircularProgressDrawable;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Filter;
        import android.widget.Filterable;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.RequestManager;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;
        import com.bumptech.glide.request.RequestOptions;

        import org.joda.time.DateTime;

        import java.util.ArrayList;
        import java.util.List;

        import ba.ito.assistance.R;
        import ba.ito.assistance.model.cameras.CameraViewModel;
        import butterknife.BindView;
        import butterknife.ButterKnife;

public class CamerasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable{


    private RequestManager glideLoader;
    private String refreshStringAppend="";




    public interface ICamerasInteraction {
        void onCameraSelected(CameraViewModel cameraViewModel);
    }

    private List<CameraViewModel> cameraViewModelList;
    private List<CameraViewModel> filterCameraList;
    private ICamerasInteraction callback;
    private CircularProgressDrawable circularProgressDrawable;
    public CamerasAdapter(RequestManager glideLoader, ICamerasInteraction callback, CircularProgressDrawable circularProgressDrawable) {
        this.glideLoader = glideLoader;
        this.callback = callback;
        this.circularProgressDrawable = circularProgressDrawable;

        this.cameraViewModelList = new ArrayList<>();
        this.filterCameraList=cameraViewModelList;

        DateTime now = DateTime.now();
        refreshStringAppend = now.year().get()+""+now.monthOfYear().get()+""+now.dayOfMonth().get()+""+now.minuteOfDay().get();
    }

    public void updateData(List<CameraViewModel> cameraViewModels,String searchFilter) {
        this.cameraViewModelList.clear();
        this.cameraViewModelList.addAll(cameraViewModels);
        this.filterCameraList=cameraViewModelList;
        getFilter().filter(searchFilter);
    }
    public void refreshPicturesData() {

        DateTime now = DateTime.now();
        refreshStringAppend = now.year().get()+""+now.monthOfYear().get()+""+now.dayOfMonth().get()+""+now.minuteOfDay().get();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_cameras, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(filterCameraList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return filterCameraList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CameraViewModel cameraViewModel;

        @BindView(R.id.iv_camera)
        ImageView ivCamera;
        @BindView(R.id.tv_camera_name)
        TextView tvCameraName;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(CameraViewModel cameraViewModel, int position) {
            this.cameraViewModel = cameraViewModel;
            tvCameraName.setText(cameraViewModel.Name);

            glideLoader.load(cameraViewModel.WebImageLocation+"?time="+refreshStringAppend)
                    .apply(new RequestOptions()
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.ic_error_outline_black_24dp))
                    .into(ivCamera);
        }

        @Override
        public void onClick(View v) {
            glideLoader.load(cameraViewModel.WebImageLocation).apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_error_outline_black_24dp)
            ).into(ivCamera);
            callback.onCameraSelected(cameraViewModel);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filterCameraList=cameraViewModelList;
                } else {
                    List<CameraViewModel> filteredList = new ArrayList<>();
                    for (CameraViewModel row : cameraViewModelList) {
                        if (row.Name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    filterCameraList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterCameraList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }
}