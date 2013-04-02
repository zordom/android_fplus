package fi.harism.fplus.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PostImageView extends ImageView {

	private LoadPictureTask mLoadPictureTask;

	public PostImageView(Context context) {
		super(context);
	}

	public PostImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PostImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setImageURL(String url) {
		setImageBitmap(null);

		if (mLoadPictureTask != null) {
			mLoadPictureTask.cancel(true);
		}
		mLoadPictureTask = new LoadPictureTask();
		mLoadPictureTask.execute(url);
	}

	private class LoadPictureTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {

			while (!isCancelled()) {
				try {
					URL url = new URL(params[0]);
					InputStream is = url.openStream();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int read;
					while ((read = is.read(buffer)) != -1) {
						if (isCancelled()) {
							is.close();
							return null;
						}
						baos.write(buffer, 0, read);
					}
					is.close();

					return BitmapFactory.decodeByteArray(baos.toByteArray(), 0,
							baos.size());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			mLoadPictureTask = null;
			if (bitmap != null) {
				setImageBitmap(bitmap);

				PropertyValuesHolder holderAlpha = PropertyValuesHolder
						.ofFloat("alpha", 0, 1);
				ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(
						PostImageView.this, holderAlpha).setDuration(500);
				anim.start();
			}
		}

	}

}
