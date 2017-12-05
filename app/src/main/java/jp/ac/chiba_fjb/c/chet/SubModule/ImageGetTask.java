package jp.ac.chiba_fjb.c.chet.SubModule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageGetTask extends AsyncTask<String,Void,Bitmap> {
	public interface ImageListener{
		public void OnBitmap(Bitmap bitmap);
	}
	ImageListener mListener;
	public ImageGetTask(String url,ImageListener listener){
		mListener = listener;
		execute(url);
	}
	@Override
	protected Bitmap doInBackground(String... params) {
		Bitmap image;
		try {
			URL imageUrl = new URL(params[0]);
			InputStream imageIs;
			imageIs = imageUrl.openStream();
			image = BitmapFactory.decodeStream(imageIs);
			return image;
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		mListener.OnBitmap(result);

	}
}