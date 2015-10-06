package com.geosearch.dempsey.geosearch;

import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;

public class MapActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {


    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private double targetLat;
    private double targetLon;
    private double latRandom;
    private double lonRandom;
    private int radius;
    private int circleState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle b = getIntent().getExtras();
        targetLat = b.getDouble("lat");
        targetLon = b.getDouble("lon");


        radius = getIntent().getExtras().getInt("rad");
        setUpMapIfNeeded();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


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
        mMap.setMapType(MAP_TYPE_HYBRID); //set map type

        double shiftFactor = .0001;
        int color = getIntent().getExtras().getInt("color");

        while(findDistance(targetLat,targetLon,targetLat+shiftFactor,targetLon+shiftFactor)<radius){
            shiftFactor+=.0001;
        }


        latRandom = (Math.random()*shiftFactor);
        lonRandom = (Math.random()*shiftFactor);
        int rand2 = (int)(Math.random()*2)+1;
        if(rand2==1){
            latRandom = 0 - latRandom;
        }
        rand2 = (int)(Math.random()*2)+1;
        if(rand2==1){
            lonRandom = 0 - lonRandom;
        }


        LatLng loc = new LatLng(targetLat+latRandom,targetLon + lonRandom); //lat and long values for target(shift)
        CircleOptions circOpt = new CircleOptions(); //circle appearance
        circOpt.center(loc).radius(radius);
        circOpt.strokeWidth(0);
        circOpt.fillColor(color);
        mMap.addCircle(circOpt);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 19)); //move screen to target(shift)
        mMap.setMyLocationEnabled(true);
        circleState=3;

    }
    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop(){
        super.onStart();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"Connection Suspended", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        double difference = findDistance(lat,lon,targetLat,targetLon);
        if(difference<radius && difference > 4&& circleState==3){
            mMap.clear();
            LatLng loc = new LatLng(targetLat+latRandom,targetLon + lonRandom); //lat and long values for target(shift)
            CircleOptions circOpt = new CircleOptions(); //circle appearance
            circOpt.center(loc).radius(radius);
            circOpt.strokeWidth(0);
            circOpt.fillColor(Color.argb(120, 0, 0, 255));
            mMap.addCircle(circOpt);
            circleState = 2;
        }
        else if(difference<4&&difference > 2 && circleState==2){
            mMap.clear();
            LatLng loc = new LatLng(targetLat,targetLon); //lat and long values for target(not shifted)
            CircleOptions circOpt = new CircleOptions(); //circle appearance
            circOpt.center(loc).radius(4); //Make smaller search circle
            circOpt.strokeWidth(0);
            circOpt.fillColor(Color.argb(120, 0, 255, 0));
            mMap.addCircle(circOpt);
            circleState = 1;
        }
        else if(difference<3&&circleState==1){
            mMap.addCircle(new CircleOptions().center(new LatLng(targetLat,targetLon)).radius(1).strokeWidth(0).fillColor(Color.RED));
            circleState=0;
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"Connection Failed",Toast.LENGTH_SHORT).show();
    }

    //Precondition: two pairs of real coordinates
    public double findDistance(double lat1,double lon1, double lat2, double lon2){
        final int EARTH_RADIUS = 6371000;
        double a = Math.sin(Math.toRadians(lat2 - lat1)/2)*Math.sin(Math.toRadians(lat2 - lat1)/2)+
                Math.cos(Math.toRadians(lat1))*Math.cos(Math.toRadians(lat2))*
                        Math.sin(Math.toRadians(lon2 - lon1)/2)*Math.sin(Math.toRadians(lon2 - lon1)/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c ; //Return the distance
    }
}
