package jp.ac.chiba_fjb.c.chet;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Pin implements LocationSource, android.location.LocationListener {
    private final static int GPS_TIME = 5 * 1000;    //5秒
    private final static int NET_TIME = 1000;    //10秒
    private Context mContext;
    private Activity mActivity;
    private LocationManager mLocationManager;
    private OnLocationChangedListener mListener;
    private Location mLastLocation;
    private MainFragment mf;
    private Location wifi;
    private Location gps;

    public static double my;
    public static double mx;
    public static String origin;
    public LatLng sydney;


    Pin(Activity activity, Context context, GoogleMap map) {
        mActivity = activity;
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        wifi = mLocationManager.getLastKnownLocation("network");
        if (DecisionWifi()) {
            my = wifi.getLatitude();
            mx = wifi.getLongitude();
            map.setMyLocationEnabled(true); //警告は無視
            map.setLocationSource(this);
            sydney = new LatLng(my, mx);                //位置設定
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));   //範囲2.0～21.0(全体～詳細)
        }
        gps = mLocationManager.getLastKnownLocation("gps");
        if (DecisionGps()) {
            my = gps.getLatitude();
            mx = gps.getLongitude();
            map.setMyLocationEnabled(true); //警告は無視
            map.setLocationSource(this);
            sydney = new LatLng(my, mx);                //位置設定
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.0f));   //範囲2.0～21.0(全体～詳細)
        }
        mf = new MainFragment();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {

        mListener = listener;
        getLocation();
    }

    @Override
    public void deactivate() {
        //警告は無視
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (mListener != null) {
            mListener.onLocationChanged(location);
            mLastLocation = location;
            my = mLastLocation.getLatitude();
            mx = mLastLocation.getLongitude();
            if(new MainFragment().getmFlg()) {
                GasMain gm = new GasMain();
                gm.main(mActivity, mContext, "Main");
            }
            origin = my + "," + mx;
            mf.Route(origin);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public boolean DecisionWifi(){
        if(wifi != null){
            return true;
        }else{
            return false;
        }
    }

    public boolean DecisionGps(){
        if(gps != null){
            return true;
        }else{
            return false;
        }
    }

    public void getLocation(){
        LocationProvider gpsProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        if (gpsProvider != null) {
            //警告は無視
            mLocationManager.requestLocationUpdates(gpsProvider.getName(), GPS_TIME, 50, this);
        }

        LocationProvider networkProvider = mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER);

        if (networkProvider != null) {
            //警告は無視
            mLocationManager.requestLocationUpdates(networkProvider.getName(), NET_TIME, 50, this);
        }
    }
}
