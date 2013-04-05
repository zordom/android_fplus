package fi.harism.fplus;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.facebook.Session;
import com.facebook.SessionState;

public class LoginActivity extends Activity {

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		Session.OpenRequest openRequest = new Session.OpenRequest(this);
		openRequest.setPermissions(Arrays.asList("read_stream",
				"friends_photos", "friends_status"));
		openRequest.setCallback(new Session.StatusCallback() {

			private boolean mRequestPublishPermission = true;

			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (state == SessionState.CLOSED_LOGIN_FAILED) {
					finish();
				}
				if (session.isOpened()) {
					if (mRequestPublishPermission) {
						Session.NewPermissionsRequest publishRequest = new Session.NewPermissionsRequest(
								LoginActivity.this, Arrays
										.asList("publish_stream"));
						session.requestNewPublishPermissions(publishRequest);
						mRequestPublishPermission = false;
					} else {
						Intent intent = new Intent(LoginActivity.this,
								FeedActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(android.R.anim.slide_in_left,
								android.R.anim.slide_out_right);
					}
				}
			}
		});

		Session session = new Session.Builder(this).build();
		session.openForRead(openRequest);
		Session.setActiveSession(session);
	}

}
