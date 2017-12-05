package jp.ac.chiba_fjb.c.chet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import jp.ac.chiba_fjb.c.chet.SubModule.BitmapUtil;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    private static final int READ_REQUEST_CODE = 42;
    private ImageButton ib;
    private String base64;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SignupMain sup = new SignupMain();
        sup.loadData(getActivity(),getContext());

        TextView tv = view.findViewById(R.id.username);
        TextView meil = view.findViewById(R.id.meil);

        tv.setText(sup.getUsername());
        meil.setText(sup.getMeil());

        ib = view.findViewById(R.id.image2);
        ib.setImageBitmap(BitmapUtil.fromBase64(new SignupMain().getIcon()));
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new MainFragment());
                ft.commit();
            }
        });
        view.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SignupMain sup = new SignupMain();
                sup.loadData(getActivity(),getContext());
                sup.main(getActivity());

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.maindisplay,new MainFragment());
                ft.commit();
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                    base64 = BitmapUtil.toBase64(bitmap,64,64);
                    bitmap.recycle();
                    Bitmap dest = BitmapUtil.fromBase64(base64);
                    ib.setImageBitmap(dest);
                    ib.setScaleType(ImageView.ScaleType.FIT_XY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
