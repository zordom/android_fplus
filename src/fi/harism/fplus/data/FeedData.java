package fi.harism.fplus.data;

import java.util.Vector;

import org.json.JSONObject;

public class FeedData {

	private Vector<FeedItemData> mFeedItems = new Vector<FeedItemData>();
	private String mUserId;

	public FeedData(String userId) {
		mUserId = userId;
	}

	public void addFeedItem(JSONObject json) {
		insertFeedItem(json, mFeedItems.size());
	}

	public synchronized void clearFeedItems() {
		mFeedItems.clear();
	}

	public synchronized FeedItemData getFeedItem(int index) {
		return mFeedItems.get(index);
	}

	public synchronized int getFeedItemCount() {
		return mFeedItems.size();
	}

	public void insertFeedItem(JSONObject json, int index) {
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

}
