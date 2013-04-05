package fi.harism.fplus.data;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class NetworkCache {

	private static final String TYPE_HOMEFEED = "homefeed";
	private static final String TYPE_PROFILEPICTURE = "profilepicture_";
	private static final String TYPE_STREAMPHOTO = "streamphoto_";

	private byte[] mBuffer = new byte[1024];
	private Context mContext;
	private String mUserId;

	public NetworkCache(Context context) {
		mContext = context;
	}

	public String getHomeFeed(boolean useCache) {
		if (useCache) {
			try {
				InputStream is = mContext.openFileInput(TYPE_HOMEFEED);
				Scanner s = new Scanner(is).useDelimiter("\\A");
				return s.hasNext() ? s.next() : "";
			} catch (Exception ex) {
			}
		}
		try {
			int read = 0;
			String accessToken = Session.getActiveSession().getAccessToken();
			URL url = new URL(
					"https://graph.facebook.com/me/home?access_token="
							+ accessToken);
			InputStream is = url.openStream();
			OutputStream os = mContext.openFileOutput(TYPE_HOMEFEED,
					Context.MODE_PRIVATE);
			while ((read = is.read(mBuffer)) != -1) {
				os.write(mBuffer, 0, read);
			}
			is.close();
			is = mContext.openFileInput(TYPE_HOMEFEED);
			Scanner s = new Scanner(is).useDelimiter("\\A");
			return s.hasNext() ? s.next() : "";
		} catch (Exception ex) {
			mContext.deleteFile(TYPE_HOMEFEED);
		}
		return null;
	}

	public Bitmap getProfilePicture(String id, boolean useCache) {
		if (useCache) {
			try {
				InputStream is = mContext.openFileInput(TYPE_PROFILEPICTURE
						+ id);
				return BitmapFactory.decodeStream(is);
			} catch (Exception ex) {
			}
		}
		try {
			int read = 0;
			URL url = new URL("http://graph.facebook.com/" + id + "/picture");
			InputStream is = url.openStream();
			OutputStream os = mContext.openFileOutput(TYPE_PROFILEPICTURE + id,
					Context.MODE_PRIVATE);
			while ((read = is.read(mBuffer)) != -1) {
				os.write(mBuffer, 0, read);
			}
			is.close();
			is = mContext.openFileInput(TYPE_PROFILEPICTURE + id);
			return BitmapFactory.decodeStream(is);
		} catch (Exception ex) {
			mContext.deleteFile(TYPE_PROFILEPICTURE + id);
		}
		return null;
	}

	public Bitmap getStreamPhoto(String id, boolean useCache) {
		if (useCache) {
			try {
				InputStream is = mContext.openFileInput(TYPE_STREAMPHOTO + id);
				return BitmapFactory.decodeStream(is);
			} catch (Exception ex) {
			}
		}
		try {
			int read = 0;
			String accessToken = Session.getActiveSession().getAccessToken();
			URL url = new URL("https://graph.facebook.com/" + id
					+ "/picture?access_token=" + accessToken);
			InputStream is = url.openStream();
			OutputStream os = mContext.openFileOutput(TYPE_STREAMPHOTO + id,
					Context.MODE_PRIVATE);
			while ((read = is.read(mBuffer)) != -1) {
				os.write(mBuffer, 0, read);
			}
			is.close();
			is = mContext.openFileInput(TYPE_STREAMPHOTO + id);
			return BitmapFactory.decodeStream(is);
		} catch (Exception ex) {
			mContext.deleteFile(TYPE_STREAMPHOTO + id);
		}
		return null;
	}

	public String getUserId() {
		if (mUserId == null) {
			Request request = Request.newGraphPathRequest(
					Session.getActiveSession(), "me", null);
			Bundle parameters = new Bundle();
			parameters.putString("fields", "id");
			request.setParameters(parameters);
			Response response = request.executeAndWait();
			GraphObject graphObject = response.getGraphObject();
			if (graphObject != null) {
				JSONObject json = graphObject.getInnerJSONObject();
				mUserId = json.optString("id");
			}
		}
		return mUserId;
	}
}
