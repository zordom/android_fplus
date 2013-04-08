package fi.harism.fplus.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FeedItemData {

	private static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
	private static final String SELF_LIKE = "fplus_self_like";
	private static final String SELF_LIKE_VALUE = "fplus_self_like_value";
	private boolean mAnimateIn = true;
	private JSONObject mJSONObject;

	public FeedItemData(JSONObject jsonObject) {
		mJSONObject = jsonObject;
	}

	public boolean getAnimateIn() {
		return mAnimateIn;
	}

	public String getCaption() {
		return mJSONObject.optString("caption");
	}

	public int getCommentsCount() {
		JSONObject comments = mJSONObject.optJSONObject("comments");
		if (comments != null) {
			return comments.optInt("count");
		}
		return 0;
	}

	public Date getCreatedTime() {
		try {
			return mDateFormat.parse(mJSONObject.optString("created_time"));
		} catch (ParseException e) {
		}
		return null;
	}

	public String getDescription() {
		return mJSONObject.optString("description");
	}

	public String getFromId() {
		JSONObject from = mJSONObject.optJSONObject("from");
		return from.optString("id");
	}

	public String getFromName() {
		JSONObject from = mJSONObject.optJSONObject("from");
		return from.optString("name");
	}

	public String getIcon() {
		return mJSONObject.optString("icon");
	}

	public String getId() {
		return mJSONObject.optString("id");
	}

	public int getLikesCount() {
		int count = 0;
		JSONObject likes = mJSONObject.optJSONObject("likes");
		if (likes != null) {
			count = likes.optInt("count");
		}
		return count + getSelfLike();
	}

	public String getLikesId(int index) {
		JSONArray likes = mJSONObject.optJSONObject("likes").optJSONArray(
				"data");
		return likes.optJSONObject(index).optString("id");
	}

	public String getMessage() {
		return mJSONObject.optString("message");
	}

	public String getName() {
		return mJSONObject.optString("name");
	}

	public String getObjectId() {
		return mJSONObject.optString("object_id");
	}

	public String getPicture() {
		return mJSONObject.optString("picture");
	}

	public int getSelfLike() {
		return mJSONObject.optInt(SELF_LIKE);
	}

	public int getSelfLikeValue() {
		return mJSONObject.optInt(SELF_LIKE_VALUE);
	}

	public String getStatusType() {
		return mJSONObject.optString("status_type");
	}

	public String getStory() {
		return mJSONObject.optString("story");
	}

	public String getType() {
		return mJSONObject.optString("type");
	}

	public Date getUpdatedTime() {
		try {
			return mDateFormat.parse(mJSONObject.optString("updated_time"));
		} catch (ParseException e) {
		}
		return null;
	}

	public void setAnimateIn(boolean animateIn) {
		mAnimateIn = animateIn;
	}

	public void setSelfLike(boolean selfLike) {
		try {
			int v = mJSONObject.optInt(SELF_LIKE_VALUE);
			mJSONObject.put(SELF_LIKE, selfLike ? v : v - 1);
		} catch (JSONException e) {
		}
	}

	public void setSelfLikeValue(int selfLikeValue) {
		try {
			mJSONObject.put(SELF_LIKE_VALUE, selfLikeValue);
		} catch (JSONException e) {
		}
	}

}
