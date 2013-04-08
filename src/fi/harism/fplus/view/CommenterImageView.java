package fi.harism.fplus.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;
import fi.harism.fplus.MainApplication;

public class CommenterImageView extends ImageView {

	private LoadPictureTask mLoadPictureTask;

	public CommenterImageView(Context context) {
		super(context);
	}

	public CommenterImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CommenterImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setUserId(String userId) {
		setImageBitmap(null);

		if (mLoadPictureTask != null) {
			mLoadPictureTask.cancel(true);
		}
		mLoadPictureTask = new LoadPictureTask();
		mLoadPictureTask.execute(userId);
	}

	private class LoadPictureTask extends AsyncTask<String, Integer, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			while (!isCancelled()) {
				try {
					return MainApplication.getNetworkCache().getProfilePicture(
							params[0], true);
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
						CommenterImageView.this, holderAlpha).setDuration(500);
				anim.start();
			}
		}

	}

}
