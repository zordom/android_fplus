package fi.harism.fplus.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import fi.harism.fplus.R;

public class FeedListItem extends LinearLayout {

	public FeedListItem(Context context) {
		super(context);
	}

	public FeedListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FeedListItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	private Animator getBounceAnimator(View v, float scale) {
		PropertyValuesHolder scaleXHolder, scaleYHolder;
		scaleXHolder = PropertyValuesHolder.ofFloat("scaleX", 1f, scale);
		scaleYHolder = PropertyValuesHolder.ofFloat("scaleY", 1f, scale);
		scaleXHolder.setEvaluator(new BounceFloatEvaluator());
		scaleYHolder.setEvaluator(new BounceFloatEvaluator());
		return ObjectAnimator.ofPropertyValuesHolder(v, scaleXHolder,
				scaleYHolder);
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();

		findViewById(R.id.button_plus).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Animator anim = getBounceAnimator(v, 1.5f);
						anim.setDuration(300);
						anim.addListener(new Animator.AnimatorListener() {
							@Override
							public void onAnimationCancel(Animator animation) {
							}

							@Override
							public void onAnimationEnd(Animator animation) {
								Animator anim = getBounceAnimator(
										FeedListItem.this, 0.95f);
								anim.setDuration(300);
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
				});
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

	private class BounceFloatEvaluator implements TypeEvaluator<Float> {

		@Override
		public Float evaluate(float fraction, Float startValue, Float endValue) {
			double t = Math.sin(fraction * Math.PI);
			return (float) (startValue + (endValue - startValue) * t);
		}

	}

}
