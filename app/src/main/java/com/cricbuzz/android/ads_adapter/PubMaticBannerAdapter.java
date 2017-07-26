package com.cricbuzz.android.ads_adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;
import com.pubmatic.sdk.banner.PMBannerAdView;
import com.pubmatic.sdk.banner.pubmatic.PubMaticBannerAdRequest;
import com.pubmatic.sdk.common.pubmatic.PUBAdSize;

import java.util.Map;


/**
 * PubMaticBannerAdapter is a sample adapter class which can be referred to integrate the PubMatic
 * as a mediation partner with DFP as a primary SDK. This class can be customise as per the required
 * functionality. For example, additional InternalBrowserListener, RichMediaListener or FeatureSupportHandler
 * from PubMatic SDK can be used to take additional event control. More parameters can be
 * send with basic ad tag details for better targeting.
 */
public class PubMaticBannerAdapter implements CustomEventBanner, PMBannerAdView.BannerAdViewDelegate.RequestListener, PMBannerAdView.BannerAdViewDelegate.ActivityListener{

	private static final String TAG = PubMaticBannerAdapter.class.getSimpleName();

	private PMBannerAdView 				mPMBanner;
	private CustomEventBannerListener 	mDFPBannerListener;

	@Override
	public void onDestroy() {
		if (mPMBanner != null) {
			mPMBanner.setRequestListener(null); // Remove listener
			mPMBanner.destroy();
			mPMBanner = null;
		}
	}

	@Override
	public void onPause() {

	}

	@Override
	public void onResume() {

	}

	@Override
	public boolean onCloseButtonClick(PMBannerAdView arg0) {
		if (mDFPBannerListener != null)
			mDFPBannerListener.onAdClosed();
		return false;
	}

	@Override
	public void onLeavingApplication(PMBannerAdView arg0) {
		if (mDFPBannerListener != null)
			mDFPBannerListener.onAdLeftApplication();
	}

	@Override
	public boolean onOpenUrl(PMBannerAdView arg0, String arg1) {
		if (mDFPBannerListener != null)
			mDFPBannerListener.onAdClicked();
		return false;
	}

	@Override
	public void requestBannerAd(Context context,
								CustomEventBannerListener listener, String serverParameter,
								AdSize adSize, MediationAdRequest mediationAdRequest,
								Bundle customEventExtras) {
		Log.d(TAG,"PubMatic :requestBannerAd:" + customEventExtras);
		mDFPBannerListener = listener;

		// PubMatic ad tag details are configured at DFP portal in the form of
		// <Pub_ID>_<Site_ID>_<Ad_ID>
		// Split the serverParameter to get the same parameters
		String []serverParam = serverParameter.split("_");

		// Initialize PubMatic's Banner AdView
		mPMBanner = new PMBannerAdView(context);
		mPMBanner.setRequestListener(this);
		mPMBanner.setActivityListener(this);

		// Create the mPMBanner ad request for PubMatic SSP channel in PubMatic SDK
		PubMaticBannerAdRequest adRequest = PubMaticBannerAdRequest.createPubMaticBannerAdRequest(
				context, serverParam[0], serverParam[1], serverParam[2]);
		adRequest.setAdSize(PUBAdSize.PUBBANNER_SIZE_320x50);//Can be customised
		mPMBanner.setUseInternalBrowser(true);
		mPMBanner.execute(adRequest);

	}

	@Override
	public void onFailedToReceiveAd(PMBannerAdView pmBannerAdView, int i, String s) {
		Log.d(TAG,"PubMatic :onFailedToReceiveAd:" + s);

		if(this.mPMBanner !=null){
			this.mPMBanner.setVisibility(View.GONE);
		}

		if(this.mDFPBannerListener != null){
			this.mDFPBannerListener.onAdFailedToLoad(222); // 222 is random code, can be customized
		}
	}

	@Override
	public void onReceivedAd(PMBannerAdView adView) {
		Log.d(TAG,"PubMatic :onAdReceived ");
		this.mPMBanner.setVisibility(View.VISIBLE);

		if(this.mDFPBannerListener != null){
			this.mDFPBannerListener.onAdLoaded(adView);
		}
	}

	@Override
	public void onReceivedThirdPartyRequest(PMBannerAdView adView, Map<String, String> properties, Map<String, String> parameters) {
		Log.d(TAG, "PubMatic third-party mPMBanner ad loaded.");
		if (mDFPBannerListener != null && adView != null) {
			this.mDFPBannerListener.onAdLoaded(adView);
		}
	}
}
