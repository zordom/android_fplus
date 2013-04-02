package fi.harism.fplus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import fi.harism.fplus.data.FeedItemData;
import fi.harism.fplus.view.FeedList;
import fi.harism.fplus.view.FeedListItem;

public class FeedActivity extends Activity {

	private SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
	private FeedAdapter mFeedAdapter;
	private Vector<FeedItemData> mFeedItems = new Vector<FeedItemData>();
	private boolean mFooterVisible = true;
	private String mUserId;

	private void addFeedItem(JSONObject json) {
		FeedItemData itemData = new FeedItemData();

		itemData.setId(json.optString("id"));
		itemData.setType(json.optString("type"));
		itemData.setMessage(json.optString("message"));
		itemData.setStory(json.optString("story"));
		itemData.setPicture(json.optString("picture"));
		itemData.setObjectId(json.optString("object_id"));

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

		mFeedItems.add(itemData);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_feed);

		FeedList feedList = (FeedList) findViewById(R.id.feedlist);
		feedList.setAdapter(mFeedAdapter = new FeedAdapter(this));
		feedList.setScrollObserver(new FeedList.ScrollObserver() {
			@Override
			public void onScrollDown() {
				if (!mFooterVisible) {
					mFooterVisible = true;
					View v = findViewById(R.id.textview_footer);
					PropertyValuesHolder translationYHolder;
					translationYHolder = PropertyValuesHolder.ofFloat(
							"translationY", v.getTranslationY(), 0);
					ObjectAnimator anim = ObjectAnimator
							.ofPropertyValuesHolder(v, translationYHolder);
					anim.setDuration(300);
					anim.start();
				}
			}

			@Override
			public void onScrollUp() {
				if (mFooterVisible) {
					mFooterVisible = false;
					View v = findViewById(R.id.textview_footer);
					PropertyValuesHolder translationYHolder;
					translationYHolder = PropertyValuesHolder.ofFloat(
							"translationY", v.getTranslationY(), v.getHeight());
					ObjectAnimator anim = ObjectAnimator
							.ofPropertyValuesHolder(v, translationYHolder);
					anim.setDuration(300);
					anim.start();
				}
			}
		});

		Request request = Request.newGraphPathRequest(
				Session.getActiveSession(), "me", new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						GraphObject graphObject = response.getGraphObject();
						if (graphObject != null) {
							JSONObject json = graphObject.getInnerJSONObject();
							TextView tv = (TextView) findViewById(R.id.textview_title);
							tv.setText(json.optString("name", "ERROR"));
							mUserId = json.optString("id");
						}
					}
				});
		Bundle parameters = new Bundle();
		parameters.putString("fields", "name");
		request.setParameters(parameters);
		request.executeAsync();

		request = Request.newGraphPathRequest(Session.getActiveSession(),
				"me/home", new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						GraphObject graphObject = response.getGraphObject();
						if (graphObject != null) {
							JSONObject json = graphObject.getInnerJSONObject();
							JSONArray data = json.optJSONArray("data");
							if (data != null) {
								for (int i = 0; i < data.length(); ++i) {
									JSONObject item = data.optJSONObject(i);
									if (item != null) {
										addFeedItem(item);
									}
								}
								mFeedAdapter.notifyDataSetChanged();
							}
						}
					}
				});
		request.executeAsync();
	}

	private class FeedAdapter extends BaseAdapter {

		private Activity mActivity;

		public FeedAdapter(Activity activity) {
			mActivity = activity;
		}

		@Override
		public int getCount() {
			return mFeedItems.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FeedListItem feedListItem = (FeedListItem) convertView;
			if (feedListItem == null) {
				feedListItem = (FeedListItem) mActivity.getLayoutInflater()
						.inflate(R.layout.layout_feed_item, null);
			}
			feedListItem.setData(mFeedItems.get(position));

			ListView listView = (ListView) parent;
			if (listView.getFirstVisiblePosition() > position) {
				feedListItem.setRotationX(-30f, 0f, 300);
				feedListItem.setTranslationY(-100, 0, 300);
			}
			if (listView.getLastVisiblePosition() < position) {
				feedListItem.setRotationX(30f, 0f, 300);
				feedListItem.setTranslationY(100, 0, 300);
			}

			return feedListItem;
		}

	}

}
