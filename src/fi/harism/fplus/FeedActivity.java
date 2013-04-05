package fi.harism.fplus;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import fi.harism.fplus.data.FeedData;
import fi.harism.fplus.data.FeedItemData;
import fi.harism.fplus.data.NetworkCache;
import fi.harism.fplus.view.FeedList;
import fi.harism.fplus.view.FeedListItem;

public class FeedActivity extends Activity {

	private FeedAdapter mFeedAdapter;
	private FeedData mFeedData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_feed);

		new Thread(new Runnable() {
			@Override
			public void run() {
				NetworkCache cache = MainApplication.getNetworkCache();
				String userId = cache.getUserId();
				mFeedData = new FeedData(userId);
				mFeedData.setFeedItems(cache.getHomeFeed(true));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mFeedAdapter = new FeedAdapter(FeedActivity.this);
						FeedList feedList = (FeedList) findViewById(R.id.feedlist);
						feedList.setAdapter(mFeedAdapter);
						feedList.setScrollObserver(new ScrollObserver());
					}
				});
				mFeedData.setFeedItems(cache.getHomeFeed(false));
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mFeedAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}

	private class FeedAdapter extends BaseAdapter {

		private Activity mActivity;

		public FeedAdapter(Activity activity) {
			mActivity = activity;
		}

		@Override
		public int getCount() {
			return mFeedData.getFeedItemCount();
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

			FeedItemData feedItem = mFeedData.getFeedItem(position);
			feedListItem.setData(feedItem);

			if (feedItem.getAnimateIn()) {
				feedItem.setAnimateIn(false);
				ListView listView = (ListView) parent;
				if (listView.getFirstVisiblePosition() > position) {
					feedListItem.setRotationX(-30f, 0f, 300);
					feedListItem.setTranslationY(-100, 0, 300);
				}
				if (listView.getLastVisiblePosition() < position) {
					feedListItem.setRotationX(30f, 0f, 300);
					feedListItem.setTranslationY(100, 0, 300);
				}
			}

			return feedListItem;
		}

	}

	private class ScrollObserver implements FeedList.ScrollObserver {
		private boolean mFooterVisible = true;

		@Override
		public void onScrollDown() {
			if (!mFooterVisible) {
				mFooterVisible = true;
				View v = findViewById(R.id.textview_footer);
				PropertyValuesHolder translationYHolder;
				translationYHolder = PropertyValuesHolder.ofFloat(
						"translationY", v.getTranslationY(), 0);
				ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v,
						translationYHolder);
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
				ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(v,
						translationYHolder);
				anim.setDuration(300);
				anim.start();
			}
		}
	}

}
