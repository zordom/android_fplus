package fi.harism.fplus.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import fi.harism.fplus.R;
import fi.harism.fplus.data.FeedItemData;

public class FeedListItem extends LinearLayout {

	private FeedItemData mItemData;
	private String mLikeId;

	public FeedListItem(Context context) {
		super(context);
	}

	public FeedListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FeedListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private void bounceButton(CompoundButton button) {
		PropertyValuesHolder scaleXHolder, scaleYHolder, translationYHolder;
		scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 2f);
		scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, 2f);
		translationYHolder = PropertyValuesHolder.ofFloat("translationY", 0f,
				-50f);
		scaleXHolder.setEvaluator(new HalfSinFloatEvaluator());
		scaleYHolder.setEvaluator(new HalfSinFloatEvaluator());
		translationYHolder.setEvaluator(new HalfSinFloatEvaluator());
		Animator anim = ObjectAnimator.ofPropertyValuesHolder(button,
				scaleXHolder, scaleYHolder, translationYHolder);
		anim.setDuration(400);
		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				PropertyValuesHolder scaleXHolder, scaleYHolder;
				scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, 0.9f);
				scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, 0.9f);
				scaleXHolder.setEvaluator(new DoubleSinFloatEvaluator());
				scaleYHolder.setEvaluator(new DoubleSinFloatEvaluator());
				Animator anim = ObjectAnimator.ofPropertyValuesHolder(
						FeedListItem.this, scaleXHolder, scaleYHolder);
				anim.setDuration(400);
				anim.start();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		});
		anim.start();
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();

		CompoundButton b = (CompoundButton) findViewById(R.id.button_comment);
		b.setButtonDrawable(R.drawable.bg_transparent);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CompoundButton b = (CompoundButton) v;
				b.setChecked(false);
			}
		});

		b = (CompoundButton) findViewById(R.id.button_plus);
		b.setButtonDrawable(R.drawable.bg_transparent);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CompoundButton b = (CompoundButton) v;
				if (!b.isChecked()) {
					Request request = Request.newGraphPathRequest(
							Session.getActiveSession(), mLikeId + "/likes",
							new Request.Callback() {
								@Override
								public void onCompleted(Response response) {
									mItemData.setLikesCount(mItemData
											.getLikesCount() - 1);
									CheckBox checkBox = (CheckBox) findViewById(R.id.button_plus);
									checkBox.setText("+"
											+ mItemData.getLikesCount());
								}
							});
					Bundle parameters = new Bundle();
					parameters.putString("method", "delete");
					request.setParameters(parameters);
					request.executeAsync();
				} else {
					Request request = Request.newPostRequest(
							Session.getActiveSession(), mLikeId + "/likes",
							null, new Request.Callback() {
								@Override
								public void onCompleted(Response response) {
									mItemData.setLikesCount(mItemData
											.getLikesCount() + 1);
									CheckBox checkBox = (CheckBox) findViewById(R.id.button_plus);
									checkBox.setText("+"
											+ mItemData.getLikesCount());
								}
							});
					request.executeAsync();
					bounceButton(b);
				}
			}
		});
	}

	public void setData(FeedItemData itemData) {
		mItemData = itemData;

		if (itemData.getObjectId() != null
				&& itemData.getObjectId().length() > 0) {
			mLikeId = itemData.getObjectId();
		} else {
			String id = itemData.getId();
			mLikeId = id.substring(id.lastIndexOf('_') + 1);
		}

		ProfileImageView profileImageView = (ProfileImageView) findViewById(R.id.imageview_profile_picture);
		profileImageView.setUserId(itemData.getFromId());

		PostImageView postImageView = (PostImageView) findViewById(R.id.imageview_post_picture);
		if (itemData.getObjectId() != null
				&& itemData.getObjectId().trim().length() > 0) {
			postImageView.setVisibility(View.VISIBLE);
			postImageView.setPhotoId(itemData.getObjectId());
		} else if (itemData.getPicture() != null
				&& itemData.getPicture().trim().length() > 0) {
			postImageView.setVisibility(View.VISIBLE);
			postImageView.setPhotoURL(itemData.getPicture());
		} else {
			postImageView.setVisibility(View.GONE);
		}

		TextView textView = (TextView) findViewById(R.id.textview_name);
		textView.setText(itemData.getFromName());

		textView = (TextView) findViewById(R.id.textview_message);
		if (itemData.getMessage() != null
				&& itemData.getMessage().trim().length() > 0) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(itemData.getMessage());
		} else {
			textView.setVisibility(View.GONE);
		}

		textView = (TextView) findViewById(R.id.textview_story);
		if (itemData.getStory() != null
				&& itemData.getStory().trim().length() > 0) {
			textView.setVisibility(View.VISIBLE);
			textView.setText(itemData.getStory());
		} else {
			textView.setVisibility(View.GONE);
		}

		CheckBox checkBox = (CheckBox) findViewById(R.id.button_plus);
		checkBox.setText("+" + itemData.getLikesCount());
		checkBox.setChecked(itemData.getLiked());

		checkBox = (CheckBox) findViewById(R.id.button_comment);
		checkBox.setText("" + itemData.getCommentsCount());

		long timeDiff = System.currentTimeMillis()
				- itemData.getCreatedTime().getTime();
		textView = (TextView) findViewById(R.id.textview_time);
		if (timeDiff < 60 * 1000) {
			textView.setText("Just now");
		} else if (timeDiff < 60 * 60 * 1000) {
			textView.setText(timeDiff / (60 * 1000) + " minutes ago");
		} else if (timeDiff < 24 * 60 * 60 * 1000) {
			textView.setText(timeDiff / (60 * 60 * 1000) + " hours ago");
		} else if (timeDiff < 365 * 24 * 60 * 60 * 1000) {
			textView.setText(timeDiff / (24 * 60 * 60 * 1000) + " days ago");
		} else {
			textView.setText(timeDiff / (365 * 24 * 60 * 60 * 1000)
					+ " years ago");
		}
	}

	public void setRotationX(float startAngle, float endAngle, long duration) {
		PropertyValuesHolder rotationXHolder;
		rotationXHolder = PropertyValuesHolder.ofFloat("rotationX", startAngle,
				endAngle);
		ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this,
				rotationXHolder);
		anim.setDuration(duration);
		anim.start();
	}

	public void setTranslationY(float startY, float endY, long duration) {
		PropertyValuesHolder translationYHolder;
		translationYHolder = PropertyValuesHolder.ofFloat("translationY",
				startY, endY);
		ObjectAnimator anim = ObjectAnimator.ofPropertyValuesHolder(this,
				translationYHolder);
		anim.setDuration(duration);
		anim.start();
	}

	private class DoubleSinFloatEvaluator implements TypeEvaluator<Float> {
		@Override
		public Float evaluate(float fraction, Float startValue, Float endValue) {
			double t = (1.0 - fraction) * Math.sin(fraction * Math.PI * 4.0);
			return (float) (startValue + (endValue - startValue) * t);
		}
	}

	private class HalfSinFloatEvaluator implements TypeEvaluator<Float> {
		@Override
		public Float evaluate(float fraction, Float startValue, Float endValue) {
			double t = Math.sin(fraction * Math.PI);
			return (float) (startValue + (endValue - startValue) * t);
		}
	}

}
