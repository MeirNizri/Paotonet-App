package com.example.paotonet.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.paotonet.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class LiveStream extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);

//        webview = (WebView) findViewById(R.id.webview);
//        webview.setWebViewClient(new WebViewClient());
//        webview.loadUrl("https://zoo.sandiegozoo.org/cams/baboon-cam");
//        webview.setVerticalScrollBarEnabled(true);
//        webview.setHorizontalScrollBarEnabled(true);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        youTubePlayerView.initialize("AIzaSyDFBe2cUu4ym3pzAQrg5x8cXmRJBqR_fPY",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo("UOgqukLcmoU");
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}