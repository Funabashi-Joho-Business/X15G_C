package jp.ac.chiba_fjb.c.chet;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainFragment extends Fragment implements OnMapReadyCallback {


    public MainFragment() {
        // Required empty public constructor
    }

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    ImageButton ib;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        ib = (ImageButton) view.findViewById(R.id.ImageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.menubox,new MenuFragment());
                ft.commit();
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //自分の位置をマップ上に表示
        MyLocationSource ls = new MyLocationSource(getActivity());
        googleMap.setMyLocationEnabled(true); //警告は無視
        googleMap.setLocationSource(ls);

        mMap = googleMap;
        LatLng sydney = new LatLng(ls.my,ls.mx);                //位置設定
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17.0f));   //範囲2.0～21.0(全体～詳細)
    }
}
