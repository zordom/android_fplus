package fi.harism.fplus;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
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
	private NetworkObserver mNetworkObserver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_feed);

		mNetworkObserver = new NetworkObserver();
		MainApplication.getNetworkCache().addNetworkObserver(mNetworkObserver);

		findViewById(R.id.button_refresh).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								NetworkCache cache = MainApplication
										.getNetworkCache();
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
				});

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

	@Override
	public void onDestroy() {
		super.onDestroy();
		MainApplication.getNetworkCache().removeNetworkObserver(
				mNetworkObserver);
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

	private class NetworkObserver implements NetworkCache.NetworkObserver {

		private ObjectAnimator mRotationAnim;

		public NetworkObserver() {
			View v = findViewById(R.id.button_refresh);
			PropertyValuesHolder rotation;
			rotation = PropertyValuesHolder.ofFloat("rotation", 0, 360);
			mRotationAnim = ObjectAnimator.ofPropertyValuesHolder(v, rotation);
			mRotationAnim.setDuration(1000);
			mRotationAnim.setRepeatCount(ValueAnimator.INFINITE);
			mRotationAnim.setInterpolator(new LinearInterpolator());
		}

		@Override
		public void onNetworkFinish() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					findViewById(R.id.button_refresh).setClickable(true);
					mRotationAnim.end();
				}
			});
		}

		@Override
		public void onNetworkStart() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					findViewById(R.id.button_refresh).setClickable(false);
					mRotationAnim.start();
				}
			});
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
