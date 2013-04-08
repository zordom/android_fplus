package fi.harism.fplus.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import fi.harism.fplus.MainApplication;

public class PostImageView extends ImageView {

	private LoadPictureTask mLoadPictureTask;
	private int mWidth, mHeight;

	public PostImageView(Context context) {
		super(context);
	}

	public PostImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PostImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int w, int h) {
		int width = View.MeasureSpec.getSize(w);
		if (mHeight != 0 && mWidth != 0) {
			setMeasuredDimension(width,
					(int) (width * ((float) mHeight / mWidth)));
		} else {
			setMeasuredDimension(width, 0);
		}
	}

	public void setPhotoId(String photoId) {
		setImageBitmap(null);
		mWidth = mHeight = 0;

		if (mLoadPictureTask != null) {
			mLoadPictureTask.cancel(true);
		}
		mLoadPictureTask = new LoadPictureTask(false);
		mLoadPictureTask.execute(photoId);
	}

	public void setPhotoURL(String photoURL) {
		setImageBitmap(null);
		mWidth = mHeight = 0;

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
						byte[] data = MainApplication.getNetworkCache()
								.getByteArray(params[0]);
						return BitmapFactory.decodeByteArray(data, 0,
								data.length);
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
				mWidth = bitmap.getWidth();
				mHeight = bitmap.getHeight();
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
