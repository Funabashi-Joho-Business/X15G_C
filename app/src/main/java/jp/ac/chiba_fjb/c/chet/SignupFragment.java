package jp.ac.chiba_fjb.c.chet;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import android.database.Cursor;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.ac.chiba_fjb.c.chet.SubModule.BitmapUtil;

import static android.content.Context.WINDOW_SERVICE;

public class SignupFragment extends Fragment {


    public SignupFragment() {
        // Required empty public constructor
    }

    private static final int READ_REQUEST_CODE = 42;
    TextView username;
    TextView usermeil;
    ImageButton ib;
    String base64 = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        username = view.findViewById(R.id.username);
        usermeil = view.findViewById(R.id.meil);

        Button singup = view.findViewById(R.id.signup);
        ib = view.findViewById(R.id.image);

        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(username.getText().equals("")) && !(usermeil.getText().equals(""))) {
                    try {
                        byte[] name = (username.getText().toString() + ",").getBytes();
                        byte[] meil = (usermeil.getText().toString() + ",").getBytes();
                        byte[] icon = base64.getBytes();

                        FileOutputStream outputStream = getActivity().openFileOutput("Chet.txt", getContext().MODE_PRIVATE);
                        outputStream.write(name);
                        outputStream.write(meil);
                        outputStream.write(icon);
                        outputStream.flush();
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    SignupMain sup = new SignupMain();
                    sup.loadData(getActivity());
                    sup.main(getActivity());

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.maindisplay, new MainFragment());
                    ft.commit();
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                    bitmap =BitmapUtil.resize(bitmap, 160, 160);

                    ib.setImageBitmap(bitmap);
                    base64 = BitmapUtil.toBase64(bitmap, 160, 160);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


