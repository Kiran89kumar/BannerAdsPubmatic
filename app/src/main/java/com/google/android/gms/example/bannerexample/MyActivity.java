/*
 * Copyright (C) 2013 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.example.bannerexample;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

/**
 * Main Activity. Inflates main activity xml and child fragments.
 */
public class MyActivity extends AppCompatActivity {

    private PublisherAdView mAdView;
    private LinearLayout adsView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        context = this;
        adsView = (LinearLayout) findViewById(R.id.ads);
        loadDFPBannerAd(AD_UNIT);
        ((Button)findViewById(R.id.button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDFPBannerAd(AD_UNIT);
            }
        });
    }


    private void loadDFPBannerAd(String adUnitID) {
        try {

            adsView.removeAllViews(); //Removing already added view

            mAdView = new PublisherAdView(this);
            mAdView.setAdUnitId(adUnitID);
            mAdView.setAdSizes(AdSize.BANNER.BANNER);

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    //todo logic for reload next ad.
                    Toast.makeText(context, "Ad Failed Errocode:"+errorCode, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    Toast.makeText(context, "Ad success:", Toast.LENGTH_LONG).show();
                    adsView.setVisibility(View.VISIBLE);
                }
            });

            //adsView is layout for displaying ad. It can be Linearlayout in XML
            adsView.addView(mAdView);
            //todo Content URL call.
            //String contentUrl = getContentUrl();
            //Log.d("MainActivity", "DFP content URL:" + contentUrl);
            //publisherAdView.loadAd(new PublisherAdRequest.Builder().setContentUrl(contentUrl).build());

            //Loading banner Ad without Content url
            mAdView.loadAd(new PublisherAdRequest.Builder().build());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        ((Button)findViewById(R.id.button)).setOnClickListener(null);
        super.onDestroy();
    }

    private final static String AD_UNIT = "/1024780/cbz_320x50_android_gallery";
}
