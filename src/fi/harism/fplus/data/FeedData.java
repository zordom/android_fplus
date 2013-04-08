package fi.harism.fplus.data;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class FeedData {

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
		FeedItemData itemData = new FeedItemData(json);

		itemData.setSelfLikeValue(1);
		if (itemData.getLikesCount() > 0) {
			if (mUserId.equals(itemData.getLikesId(0))) {
				itemData.setSelfLikeValue(0);
				itemData.setSelfLike(true);
			}
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
