package com.example.kiiroz.lab9googlemap;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener,LocationListener{

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    LocationManager LM;
    int PLACE = 0;
    LatLng oldpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        //set map to accept long click from user
        mMap.setOnMapLongClickListener(this);

        LM = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //minTime, minDistance, Listerner
        LM.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        LatLng BKK = new LatLng(13.7563,100.5018);
        LatLng HK = new LatLng(22.2783, 114.1747);

        //markers
        mMap.addMarker(new MarkerOptions().position(BKK).title("Bangkok"));
        mMap.addMarker(new MarkerOptions().position(HK).title("Hong Kong"));

        //set initial camera
        CameraUpdate start_map = CameraUpdateFactory.newLatLng(BKK);
        mMap.moveCamera(start_map);

        //set initial zoom
        CameraUpdate start_zoom = CameraUpdateFactory.zoomTo(5);
        mMap.animateCamera(start_zoom);

        //Draw a line
        mMap.addPolyline(new PolylineOptions().add(BKK).add(HK));
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //add marker while hold click
        mMap.addMarker(new MarkerOptions().position(latLng).title("Place" + ++PLACE));

        //set new center from getting point
        CameraUpdate new_center = CameraUpdateFactory.newLatLng(latLng);
        mMap.moveCamera(new_center);

        //check 1st point, its should not lined with other point except its pair point
        if (PLACE != 1)
            mMap.addPolyline(new PolylineOptions().add(latLng).add(oldpoint));

        //set new zoom for getting point
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
        mMap.animateCamera(zoom);

        //update oldpoint to pair it with line
        oldpoint = latLng;
    }

    @Override
    public void onLocationChanged(Location location) {
        //use geo fix <long> <lat> to change the location from terminal.
        //Get Lat,Long from User
        LatLng someplace = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(someplace).title("Place" + ++PLACE));

        //set new center from getting point
        CameraUpdate new_center = CameraUpdateFactory.newLatLng(someplace);
        mMap.moveCamera(new_center);

        //check 1st point, its should not lined with other point except its pair point
        if (PLACE != 1)
            mMap.addPolyline(new PolylineOptions().add(someplace).add(oldpoint));

        //set new zoom for getting point
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(5);
        mMap.animateCamera(zoom);

        //update oldpoint to pair it with line
        oldpoint = someplace;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
