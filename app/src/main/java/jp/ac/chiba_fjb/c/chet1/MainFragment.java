package jp.ac.chiba_fjb.c.chet1;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements OnMapReadyCallback ,GoogleMap.OnMapClickListener ,RouteReader.RouteListener {


    public MainFragment() {
        // Required empty public constructor
    }

    private static GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ImageButton ib;
    private Button b;
    private Pin p;
    private static TextView minute;
    private static RouteData.Routes r;
    private static String destination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        minute = view.findViewById(R.id.minute);
        b = view.findViewById(R.id.Transmission);
        ib = (ImageButton) view.findViewById(R.id.ImageButton);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GasMain.class);
                startActivity(intent);
            }
        });

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
        destination = lat.latitude+","+lat.longitude;
        Route(origin);
    }
    public void Route(String origin){
        if(destination != null) {
            RouteReader.recvRoute(origin, destination, this);
        }
    }

    @Override
    public void onRoute(RouteData routeData) {
        //ルート受け取り処理
        if (routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0) {
            r = routeData.routes[0];
//            RouteData.Location start = r.legs[0].start_location;
            RouteData.Location end = r.legs[0].end_location;
            mMap.clear();
//            mMap.addMarker(new MarkerOptions().position(new LatLng(start.lat, start.lng)).title(r.legs[0].start_address));
            mMap.addMarker(new MarkerOptions().position(new LatLng(end.lat, end.lng)).title(r.legs[0].end_address));
            minute.setText(r.legs[0].duration.text+"  "+r.legs[0].distance.text);
            RouteSearch();
        }
    }
    public void RouteSearch(){
        List<LatLng> route = new ArrayList<LatLng>();
        for (RouteData.Steps i : r.legs[0].steps) {
            route.add(new LatLng(i.start_location.lat,i.start_location.lng));
            route.add(new LatLng(i.end_location.lat,i.end_location.lng));
        }
        PolylineOptions options = new PolylineOptions();
        for (LatLng latLng : route) {
            options.add(latLng);
        }
        options.geodesic(true);
        options.color(Color.BLUE);
        options.width(15);
        mMap.addPolyline(options);
    }
}
