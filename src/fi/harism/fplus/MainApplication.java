package fi.harism.fplus;

import android.app.Application;
import fi.harism.fplus.data.NetworkCache;

public class MainApplication extends Application {

	private static NetworkCache mNetworkCache;

	public static NetworkCache getNetworkCache() {
		return mNetworkCache;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNetworkCache = new NetworkCache(this);
	}

}
