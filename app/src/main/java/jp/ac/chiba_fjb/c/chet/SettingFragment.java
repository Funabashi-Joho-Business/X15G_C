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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import jp.ac.chiba_fjb.c.chet.SubModule.BitmapUtil;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    private static final int READ_REQUEST_CODE = 42;
    private ImageButton ib;
    private String base64 = "";
    TextView meil;
    TextView tv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        SignupMain sup = new SignupMain();
        sup.loadData(getActivity());

        tv = view.findViewById(R.id.username2);
        meil = view.findViewById(R.id.meil2);

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
                ft.replace(R.id.maindisplay, new MainFragment());
                ft.commit();
            }
        });
        view.findViewById(R.id.change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(tv.getText().equals("")) && !(meil.getText().equals(""))) {
                    try {
                        byte[] name;
                        byte[] m;
                        byte[] icon;
                        name = (tv.getText().toString() + ",").getBytes();
                        m = (meil.getText().toString() + ",").getBytes();
                        icon = base64.getBytes();

                        FileOutputStream outputStream = null;
                        outputStream = getActivity().openFileOutput("Chet.txt", getContext().MODE_PRIVATE);
                        outputStream.write(name);
                        outputStream.write(m);
                        outputStream.write(icon);
                        outputStream.flush();
                        outputStream.close();


                        SignupMain sup = new SignupMain();
                        sup.loadData(getActivity());
                        sup.main(getActivity());

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.maindisplay, new MainFragment());
                        ft.commit();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
                    Bitmap bitmap = BitmapUtil.setCircle(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri));

                    base64 = BitmapUtil.toBase64(bitmap, 160, 160);
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
