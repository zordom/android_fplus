package fi.harism.fplus;

import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionState;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class LoginActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Session.OpenRequest openRequest = new Session.OpenRequest(this).setPermissions(Arrays.asList("read_stream")).setCallback(new Session.StatusCallback() {			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (state == SessionState.CLOSED_LOGIN_FAILED) {
					finish();
				}
				if (session.isOpened()) {
					Intent intent = new Intent(LoginActivity.this,
							FeedActivity.class);
					startActivity(intent);
					finish();
					overridePendingTransition(android.R.anim.slide_in_left,
							android.R.anim.slide_out_right);
				}
			}
		});
	    Session session = new Session.Builder(this).build();
	    Session.setActiveSession(session);
	    session.openForRead(openRequest);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode,
				resultCode, data);
	}

}
