package ba.ito.assistance.ui.main_map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ba.ito.assistance.R;

public class CustomInfoWindowsAdapter implements GoogleMap.InfoWindowAdapter {
    private final View windows;

    public CustomInfoWindowsAdapter( Context context) {
        windows= LayoutInflater.from(context).inflate(R.layout.custom_info_window, null);
    }

    private void renderWindowsText(Marker m, View v){

    }
    @Override
    public View getInfoWindow(Marker marker) {
        renderWindowsText(marker,windows);
        return windows;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindowsText(marker,windows);
        return windows;
    }
}
