package jp.ac.chiba_fjb.c.chet;

import android.content.Context;
import android.support.v4.app.FragmentActivity;



import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.IOException;
import java.util.Collections;


/**
 * Created by oikawa on 2016/09/26.
 */

public class GoogleDrive extends GoogleAccount {

    interface OnConnectListener{
        public void onConnected(boolean flag);
    }
    private OnConnectListener mListener;
    private Drive mDrive;
    private String mRootId;
    Context mActivity;
    public GoogleDrive(Context con){
        super(con,null);

        FragmentActivity activity = null;
        if(con instanceof FragmentActivity)
            activity = (FragmentActivity)con;
        mActivity = con;

        mDrive = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), getCredential()).build();
    }
    void setOnConnectedListener(OnConnectListener listener){
        mListener = listener;
    }
    boolean connect(){
        requestAccount();
        return true;
    }

    @Override
    protected void onExec() throws IOException {
        mRootId = mDrive.files().get("root").setFields("id").execute().getId();
//        FileList fileList = mDrive.files().list().setQ(String.format("'%s' in parents",id)).execute();
//        for(File file : fileList.getFiles()){
//            // IDとファイル名
//            System.out.println(" "+file.getId() + " " + file.getName());
//        }


        if(mListener != null)
            mListener.onConnected(mRootId != null);
    }

    @Override
    protected void onError() {
        super.onError();
        if(mListener != null)
            mListener.onConnected(false);
    }

    String getRootId(){
        return mRootId;
    }
    FileList getFileList(String id){
        try {
            return mDrive.files().list().setQ(String.format("'%s' in parents",mRootId)).execute();
        } catch (IOException e) {
            return null;
        }
    }
    String getFolderId(String parent,String name){
        try {
            FileList list = mDrive.files().list().setQ(String.format("'%s' in parents and name='%s'", parent, name)).execute();
            if(list.size() > 0)
                return list.getFiles().get(0).getId();
        } catch (IOException e) {}
        return null;
    }
    String getFolderId(String name){
        String[] folders = name.split("/", 0);
        String id = getRootId();
        for(String f : folders){
            if(f.length() == 0)
                continue;
            id = getFolderId(id,f);
        }
        return id;
    }
    String createFolder(String id,String name){
        File fileMetadata = new File();
        fileMetadata.setName(name);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
        fileMetadata.setParents(Collections.singletonList(id));
        try {
            File file = mDrive.files().create(fileMetadata).setFields("id, parents").execute();
            if(file != null)
                return file.getId();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
