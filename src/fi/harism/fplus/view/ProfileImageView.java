package fi.harism.fplus.view;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ProfileImageView extends ImageView {

	private LoadPictureTask mLoadPictureTask;

	public ProfileImageView(Context context) {
		super(context);
	}

	public ProfileImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ProfileImageView(Context context, AttributeSet attrs, int defStyle) {
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

					Bitmap bitmapIn = BitmapFactory.decodeByteArray(
							baos.toByteArray(), 0, baos.size());
					Bitmap bitmapOut = Bitmap.createBitmap(bitmapIn.getWidth(),
							bitmapIn.getHeight(), Bitmap.Config.ARGB_8888);

					float dx = 2.0f / bitmapIn.getWidth();
					float dy = 2.0f / bitmapIn.getHeight();
					for (int x = 0; x < bitmapIn.getWidth(); ++x) {
						for (int y = 0; y < bitmapIn.getHeight(); ++y) {
							if (isCancelled()) {
								return null;
							}
							float tx = x * dx - 1.0f;
							float ty = y * dy - 1.0f;
							float len = tx * tx + ty * ty;
							if (len > 1.0f) {
								bitmapOut.setPixel(x, y, Color.TRANSPARENT);
							} else if (len > 0.90f) {
								int c = bitmapIn.getPixel(x, y);
								int a = (c >> 24) & 0xFF;
								float t = (1.0f - len) * 10.0f;
								a *= t * t * (3 - 2 * t);
								bitmapOut.setPixel(x, y, (a << 24)
										| (c & 0xFFFFFF));
							} else {
								bitmapOut.setPixel(x, y,
										bitmapIn.getPixel(x, y));
							}
						}
					}

					return bitmapOut;
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
						ProfileImageView.this, holderAlpha).setDuration(500);
				anim.start();
			}
		}

	}

}
