package com.pubmatic.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.moceanmobile.mast.MASTAdView;
import com.moceanmobile.mast.MASTAdViewDelegate;

import java.util.Map;


public class MoceanBannerAdapter implements CustomEventBanner,MASTAdViewDelegate.RequestListener, MASTAdViewDelegate.ActivityListener{

	// For Internal Adapter use only
	private static final String MOCEAN_CUSTOM_PARAM_AD_WIDHT = "size_x";
	private static final String MOCEAN_CUSTOM_PARAM_AD_HEIGHT = "size_y";

	private static final String TAG = MoceanBannerAdapter.class.getSimpleName();
	private MASTAdView mMastAdView;
	private CustomEventBannerListener mBannerListener;

	@Override
	public void onDestroy() {
        if (mMastAdView != null) {
            mMastAdView.setRequestListener(null); // Remove listener
            mMastAdView.reset();
            mMastAdView = null;
        }
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCloseButtonClick(MASTAdView arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLeavingApplication(MASTAdView arg0) {
		if (mBannerListener != null) {
			mBannerListener = null;
		}
	}

	@Override
	public boolean onOpenUrl(MASTAdView arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onFailedToReceiveAd(MASTAdView arg0, Exception arg1) {
		Log.d(TAG,"Mocean :onFailedToReceiveAd:" + arg1);

        if(this.mMastAdView!=null){
            this.mMastAdView.setVisibility(View.GONE);
        }

        if(this.mBannerListener != null){
            this.mBannerListener.onAdFailedToLoad(222); // 222 is random code, can be customized
        }
	}

	@Override
	public void onReceivedAd(MASTAdView mastAdView) {
		// TODO Auto-generated method stub
		Log.d(TAG,"Mocean :onAdReceived ");
		this.mMastAdView.setVisibility(View.VISIBLE);

		if(this.mBannerListener != null){
			//this.customEventListener.onReceivedAd(adView);
			this.mBannerListener.onAdLoaded(mastAdView);
		}
	}

	@Override
	public void onReceivedThirdPartyRequest(MASTAdView mastAdView, Map<String, String> arg1, Map<String, String> arg2) {
		Log.d(TAG, "Mocean third-party banner ad loaded.");
		if (mBannerListener != null && mMastAdView != null) {
			this.mBannerListener.onAdLoaded(mastAdView);
		}
	}

	@Override
	public void requestBannerAd(Context context,
								CustomEventBannerListener listener, String serverParameter,
								AdSize adSize, MediationAdRequest mediationAdRequest,
								Bundle customEventExtras) {
		Log.d(TAG,"custom event trigger, appId: "+serverParameter);

		mBannerListener = listener;

		final int zoneId = Integer.parseInt(serverParameter);
		final int adWidth = adSize.getWidth();
		final int adHeight = adSize.getHeight();

		// Initialize Mocean AdView
		mMastAdView = new MASTAdView(context);

		mMastAdView.setZone(zoneId);
		mMastAdView.setRequestListener(this);

		// Get Custom Param Map
		Map<String, String> customParamMap = mMastAdView
				.getAdRequestParameters();
		customParamMap.put(MOCEAN_CUSTOM_PARAM_AD_WIDHT, "" + adWidth);
		customParamMap.put(MOCEAN_CUSTOM_PARAM_AD_HEIGHT, "" + adHeight);

		// Using internal browser by default
		mMastAdView.setUseInternalBrowser(true);
		// Load Ad
		mMastAdView.update();
	}
}
