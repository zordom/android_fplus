package fi.harism.fplus.view;

import java.net.URL;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;
import fi.harism.fplus.MainApplication;

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

	public void setPhotoId(String photoId) {
		setImageBitmap(null);

		if (mLoadPictureTask != null) {
			mLoadPictureTask.cancel(true);
		}
		mLoadPictureTask = new LoadPictureTask(false);
		mLoadPictureTask.execute(photoId);
	}

	public void setPhotoURL(String photoURL) {
		setImageBitmap(null);

		if (mLoadPictureTask != null) {
			mLoadPictureTask.cancel(true);
		}
		mLoadPictureTask = new LoadPictureTask(true);
		mLoadPictureTask.execute(photoURL);
	}

	private class LoadPictureTask extends AsyncTask<String, Integer, Bitmap> {

		private boolean mPhotoIsURL;

		public LoadPictureTask(boolean photoIsURL) {
			mPhotoIsURL = photoIsURL;
		}

		@Override
		protected Bitmap doInBackground(String... params) {

			while (!isCancelled()) {
				try {
					if (mPhotoIsURL) {
						URL url = new URL(params[0]);
						return BitmapFactory.decodeStream(url.openStream());
					} else {
						return MainApplication.getNetworkCache()
								.getStreamPhoto(params[0], true);
					}
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
