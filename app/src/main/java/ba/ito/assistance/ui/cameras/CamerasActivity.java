package ba.ito.assistance.ui.cameras;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import ba.ito.assistance.R;
import ba.ito.assistance.base.BaseDaggerAuthorizedActivity;
import ba.ito.assistance.model.cameras.CameraViewModel;
import ba.ito.assistance.util.ISchedulersProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class CamerasActivity extends BaseDaggerAuthorizedActivity implements CamerasContract.View, CamerasAdapter.ICamerasInteraction {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_cameras)
    RecyclerView rvCameras;
    @BindView(R.id.parent)
    CoordinatorLayout parent;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private CamerasAdapter camerasAdapter;

    @Inject
    CamerasContract.Presenter presenter;
    @Inject
    ISchedulersProvider schedulersProvider;
    private String searchingText;
    private Disposable subscribeSearchViewChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameras);
        ButterKnife.bind(this);
        setupToolbar(toolbar, true, R.string.title_activity_cameras);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();
        camerasAdapter = new CamerasAdapter(Glide.with(this), this,circularProgressDrawable);
        rvCameras.setAdapter(camerasAdapter);
        GridLayoutManager layout = new GridLayoutManager(this, 2);
        rvCameras.setLayoutManager(layout);
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.loadCameras());
        presenter.takeView(this);
        presenter.onStart();
        presenter.loadCameras();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onStop();
        presenter.dropView();
        if(subscribeSearchViewChange!=null)
            subscribeSearchViewChange.dispose();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_cameras, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        subscribeSearchViewChange = RxSearchView.queryTextChanges(searchView)
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(schedulersProvider.main())
                .subscribe(searchViewQueryTextEvent ->{
                    camerasAdapter.getFilter().filter(searchViewQueryTextEvent);
                });

//        searchView.setOnCloseListener(() -> {
//            if (searchingText == null || searchingText.isEmpty()) {
//                camerasAdapter.getFilter().filter(searchingText);
//            }
//            return false;
//        });
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                camerasAdapter.getFilter().filter(searchingText);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                searchingText = newText;
//                return false;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void displayCameras(List<CameraViewModel> cameras) {
        camerasAdapter.updateData(cameras,searchingText);
    }

    @Override
    public void displayNoCameras() {
        Snackbar.make(parent, R.string.no_data, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public void refreshPictures() {
        camerasAdapter.refreshPicturesData();
    }

    @Override
    public void displayUnexpectedError() {
        Snackbar.make(parent, R.string.msg_unexpected_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayLoading(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    @Override
    public void onCameraSelected(CameraViewModel cameraViewModel) {

//        Glide.with(this).load(cameraViewModel.WebImageLocation+"?time="+ DateTime.now().getMillis()).into(ivCamera);
    }

    public static Intent GetInstance(Context context) {
        return new Intent(context, CamerasActivity.class);
    }
}
