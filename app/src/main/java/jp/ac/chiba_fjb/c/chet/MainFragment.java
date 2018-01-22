package jp.ac.chiba_fjb.c.chet;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.chiba_fjb.c.chet.SubModule.BitmapUtil;
import jp.ac.chiba_fjb.c.chet.SubModule.parseJsonpOfDirectionAPI;

import static jp.ac.chiba_fjb.c.chet.GasMain.*;
import static jp.ac.chiba_fjb.c.chet.Pin.*;

public class MainFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, RouteReader.RouteListener {


    public MainFragment() {
    }

    private static GoogleMap mMap;
    private static TextView minute;
    private static  Button b;
    private static RouteData.Routes r;
    private static GasMain gm;
    private static String destination;
    private static String sheetid;
    private static boolean mFlg = true;
    private SupportMapFragment mapFragment;
    private ImageButton ib;
    private Handler handler;
    private Runnable run;
    private PolylineOptions lineOptions = null;
    private String suborigin = null;
    private String subdestination = null;
    private Pin pin;

    public static String info_A;
    public static String info_B;
    public static String posinfo;
    public static double latitude;
    public static double longitude;
    public static LinearLayout chatbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        gm = new GasMain();
        minute = view.findViewById(R.id.minute);
        b = view.findViewById(R.id.Transmission);
        ib = (ImageButton) view.findViewById(R.id.ImageButton);
        chatbox = (LinearLayout) view.findViewById(R.id.chatbox);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.setEnabled(false);
                b.setText("送信中");
                mFlg = false;
                EditText e = (EditText) getView().findViewById(R.id.chettext1);
                new MainActivity().text = e.getText().toString();
                gm.main(getActivity(), getContext(), "Main");
                getAllUser();
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

        handler = new Handler();
        run = new Runnable() {
            @Override
            public void run() {
                if (mFlg) {
                    gm.main(getActivity(), getContext(), "Return");
                    Route(origin);
                }
                if(!pin.DecisionWifi() && !pin.DecisionGps() && mFlg){
                    pin = new Pin(getActivity(), getContext(), mMap);
                }
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(run);
        return view;
    }

    @Override
    public void onStart() {
        this.mFlg = true;
        super.onStart();
    }

    @Override
    public void onStop() {
        this.mFlg = false;
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //自分の位置をマップ上に表示
        pin = new Pin(getActivity(), getContext(), mMap);
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        String origin = my + "," + mx;
        if (getSheetid() == null) {
            LatLng lat = new LatLng(latLng.latitude, latLng.longitude);
            latitude = lat.latitude;
            longitude = lat.longitude;
            destination = lat.latitude + "," + lat.longitude;
        }
        Route(origin);
    }

    public void Route(String origin) {
        mMap.clear();

        if (getSheetid() != null && gm.getArray() != null) {
            ArrayList<ArrayList<Object>> s = gm.getArray();
            String name = s.get(0).get(0).toString();
            for (int index = 0; index < s.size(); index++) {
                if (name.equals(s.get(index).get(0))) {
                    if (!(s.get(index).get(3).toString().equals("0"))) {
                        destination = s.get(index).get(3).toString() + "," + s.get(index).get(4).toString();
                    }
                }
            }
        }
        if(lineOptions != null && subdestination != null && destination.equals(subdestination)){
//        if(lineOptions != null && suborigin != null && subdestination != null && destination.equals(subdestination) && origin.equals(suborigin)){
            String[] s = destination.split(",");
            double lat = Double.parseDouble(s[0]);
            double lon = Double.parseDouble(s[1]);
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(r.legs[0].end_address));
            mMap.addPolyline(lineOptions);
            latitude = lat;
            longitude = lon;
        }else if (destination != null && origin != null) {
            RouteReader.recvRoute(origin, destination, this);
            String[] s = destination.split(",");
            latitude = Double.parseDouble(s[0]);
            longitude = Double.parseDouble(s[1]);
        }
        setUserPin();
        suborigin = origin;
        subdestination = destination;
    }

    @Override
    public void onRoute(RouteData routeData) {
        //ルート受け取り処理
        if (routeData != null && routeData.routes.length > 0 && routeData.routes[0].legs.length > 0) {
            r = routeData.routes[0];
            RouteData.Location end = r.legs[0].end_location;
            mMap.addMarker(new MarkerOptions().position(new LatLng(end.lat, end.lng)).title(r.legs[0].end_address));
            minute.setText(r.legs[0].duration.text + "  " + r.legs[0].distance.text);

            List<List<HashMap<String, String>>> list = new parseJsonpOfDirectionAPI().parse(routeData);
            RouteSearch(list);
        }
    }

    public void RouteSearch(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points = null;
        lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();
        if (result.size() != 0) {
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }

                //ポリライン
                lineOptions.addAll(points);
                lineOptions.width(15);
                lineOptions.color(Color.BLUE);
            }
            mMap.addPolyline(lineOptions);
        }
    }

    public String getCheck() {
        String s = "";
        try {
            if(r != null) {
                s = r.legs[0].end_address;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    public String getSheetid() {
        return this.sheetid;
    }

    public void setSheetid(String sheetid) {
        this.sheetid = sheetid;
    }

    public static void setUserPin() {
        if (getAllUser() != null) {
            ArrayList<String> user = getAllUser();
            ArrayList<ArrayList<Object>> s = getArray();

            HashMap<String, Integer> UserArrayIndex = new HashMap<>();
            for (int SIndex = 0; SIndex < s.size(); SIndex++) {
                if (UserArrayIndex.containsKey(s.get(SIndex).get(0).toString()) || user.contains(s.get(SIndex).get(0).toString())) {
                    UserArrayIndex.put(s.get(SIndex).get(0).toString(), SIndex);
                }
            }
            for (int UserIndex = 0; UserIndex < user.size(); UserIndex++) {
                int i = UserArrayIndex.get(user.get(UserIndex));
                BigDecimal lat = (BigDecimal) s.get(i).get(1);
                BigDecimal lon = (BigDecimal) s.get(i).get(2);
                LatLng latLng = new LatLng(lat.doubleValue(), lon.doubleValue());
                Marker m;
                if (s.get(i).get(5).toString().equals("")) {
                    m = mMap.addMarker(new MarkerOptions().position(latLng).title(s.get(i).get(0).toString()));
                } else {
                    Bitmap icon = BitmapUtil.fromBase64(s.get(i).get(5).toString());
                    icon = BitmapUtil.setCircle(icon);
                    m = mMap.addMarker(new MarkerOptions().position(latLng).title(s.get(i).get(0).toString()).icon(BitmapDescriptorFactory.fromBitmap(icon)));
                }
            }
        }
    }

    public boolean getmFlg() {
        return mFlg;
    }

    public void setmFlg(boolean flg) {
        mFlg = flg;
    }

    public void setButton(){
        b.setEnabled(true);
        b.setText("送信");
    }
}
