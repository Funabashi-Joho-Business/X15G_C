package jp.ac.chiba_fjb.c.chet;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment implements OnMapReadyCallback ,GoogleMap.OnMapClickListener, RouteReader.RouteListener {


    public MainFragment() {
        // Required empty public constructor
    }

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    ImageButton ib;
    Pin p;
    TextView minute;
    RouteData.Routes r;
    List<LatLng> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        minute = view.findViewById(R.id.minute);

        ib = (ImageButton) view.findViewById(R.id.ImageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.menubox, new MenuFragment());
                ft.commit();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //自分の位置をマップ上に表示
        p = new Pin(getActivity(), mMap);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng lat = new LatLng(latLng.latitude, latLng.longitude);
        String origin = p.my+","+p.mx;
        String destination = lat.latitude+","+lat.longitude;
        RouteReader.recvRoute(origin, destination, this);
    }

    @Override
    public void onRoute(RouteData routeData) {
        //ルート受け取り処理
        if (routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0) {
            r = routeData.routes[0];
            RouteData.Location start = r.legs[0].start_location;
            RouteData.Location end = r.legs[0].end_location;
            mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(new LatLng(start.lat, start.lng)).title(r.legs[0].start_address));
            mMap.addMarker(new MarkerOptions().position(new LatLng(end.lat, end.lng)).title(r.legs[0].end_address));
            minute.setText(r.legs[0].duration.text+"  "+r.legs[0].distance.text);
            RouteSearch();
        }
    }
    public void RouteSearch(){
//        LatLng s;
//        LatLng g;
//        RouteData.Location start;
//        RouteData.Location end;
//        for (int i = 0; i < r.legs[0].steps.length; i++) {
//            start = r.legs[0].steps[i].start_location;
//            s = new LatLng(start.lat, start.lng);
//            end = r.legs[0].steps[i].end_location;
//            g = new LatLng(end.lat,end.lng);
//            PolylineOptions straight = new PolylineOptions().add(s, g).color(Color.BLUE).width(15);
//            mMap.addPolyline(straight);
//        }
        List<LatLng> route = new ArrayList<LatLng>();
        for (RouteData.Steps i : r.legs[0].steps) {
            route.add(new LatLng(i.start_location.lat,i.start_location.lng));
            route.add(new LatLng(i.end_location.lat,i.end_location.lng));
        }
        PolylineOptions options = new PolylineOptions();
        for (LatLng latLng : route) {
            options.add(latLng);
        }
        options.color(Color.BLUE);
        options.width(15);
        mMap.addPolyline(options);
    }
}
