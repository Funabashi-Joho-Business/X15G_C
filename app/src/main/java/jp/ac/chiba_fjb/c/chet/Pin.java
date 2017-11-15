package jp.ac.chiba_fjb.c.chet;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;

public class Pin implements LocationSource, android.location.LocationListener {
    private final static int GPS_TIME = 3 * 1000;    //5秒
    private final static int NET_TIME = 1000;    //10秒
    private Context mContext;
    private LocationManager mLocationManager;
    private OnLocationChangedListener mListener;
    private Location mLastLocation;
    private MainFragment mf;
    public static double my;
    public static double mx;
    public LatLng sydney;


    Pin(Context context, GoogleMap map) {
        mContext = context;
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location gps = mLocationManager.getLastKnownLocation("gps");
        if(gps != null){
            my = gps.getLatitude();
            mx = gps.getLongitude();
            map.setMyLocationEnabled(true); //警告は無視
            map.setLocationSource(this);
            sydney = new LatLng(my,mx);                //位置設定
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17.0f));   //範囲2.0～21.0(全体～詳細)
        }
        mf = new MainFragment();
    }

    @Override
    public void activate(OnLocationChangedListener listener) {

        mListener = listener;
        LocationProvider gpsProvider = mLocationManager.getProvider(LocationManager.GPS_PROVIDER);
        if (gpsProvider != null) {
            //警告は無視
            mLocationManager.requestLocationUpdates(gpsProvider.getName(), GPS_TIME, 10, this);
        }

        LocationProvider networkProvider = mLocationManager.getProvider(LocationManager.NETWORK_PROVIDER);
        ;
        if (networkProvider != null) {
            //警告は無視
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, NET_TIME, 0, this);
        }
    }

    @Override
    public void deactivate() {
        //警告は無視
        mLocationManager.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        if(mListener != null)
        {
            mListener.onLocationChanged(location);
            mLastLocation = location;
            my = mLastLocation.getLatitude();
            mx = mLastLocation.getLongitude();
            String origin = my +","+ mx;
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
}
