package fi.harism.fplus.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedData {

	private SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
	private Vector<FeedItemData> mFeedItems = new Vector<FeedItemData>();
	private String mUserId;

	public FeedData(String userId) {
		mUserId = userId;
	}

	public synchronized FeedItemData getFeedItem(int index) {
		return mFeedItems.get(index);
	}

	public synchronized int getFeedItemCount() {
		return mFeedItems.size();
	}

	private void insertFeedItem(JSONObject json, int index) {
		FeedItemData itemData = new FeedItemData();

		itemData.setId(json.optString("id"));
		itemData.setType(json.optString("type"));
		itemData.setMessage(json.optString("message"));
		itemData.setStory(json.optString("story"));
		itemData.setPicture(json.optString("picture"));
		itemData.setObjectId(json.optString("object_id"));
		itemData.setStatusType(json.optString("status_type"));
		itemData.setCaption(json.optString("caption"));
		itemData.setIcon(json.optString("icon"));

		String createdTime = json.optString("created_time");
		if (createdTime != null) {
			try {
				itemData.setCreatedTime(mDateFormat.parse(createdTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		String updatedTime = json.optString("updated_time");
		if (updatedTime != null) {
			try {
				itemData.setUpdatedTime(mDateFormat.parse(updatedTime));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		JSONObject from = json.optJSONObject("from");
		if (from != null) {
			itemData.setFromId(from.optString("id"));
			itemData.setFromName(from.optString("name"));
		}
		JSONObject likes = json.optJSONObject("likes");
		if (likes != null) {
			itemData.setLikesCount(likes.optInt("count"));
			JSONArray data = likes.optJSONArray("data");
			if (data != null) {
				for (int i = 0; i < data.length(); ++i) {
					JSONObject like = data.optJSONObject(i);
					if (mUserId.equals(like.optString("id"))) {
						itemData.setLiked(true);
						break;
					}
				}
			}
		}
		JSONObject comments = json.optJSONObject("comments");
		if (comments != null) {
			itemData.setCommentsCount(comments.optInt("count"));
		}

		mFeedItems.insertElementAt(itemData, index);
	}

	public synchronized void setFeedItems(String jsonString) {
		try {
			mFeedItems.clear();
			JSONObject main = new JSONObject(jsonString);
			JSONArray data = main.getJSONArray("data");
			for (int i = 0; i < data.length(); ++i) {
				insertFeedItem(data.getJSONObject(i), mFeedItems.size());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
