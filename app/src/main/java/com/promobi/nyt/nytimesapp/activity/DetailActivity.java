package com.promobi.nyt.nytimesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.promobi.nyt.nytimesapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    String mUrl;

    @BindView (R.id.progressBar)ProgressBar progressBar;
    @BindView (R.id.webView)WebView webView;
    @BindView (R.id.toolbar)Toolbar toolbar;
    @BindView (R.id.toolbar_text)TextView toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_text.setText(R.string.title_detail_activity);


        mUrl = getIntent().getStringExtra(DashboardActivity.EXTRA_ARTICLE_URL);



        // Display ProgressBar until page is loaded
        progressBar.setIndeterminate(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                super.onPageFinished(view, url);
            }
        });
        webView.loadUrl(mUrl);
    }

    // Make navigation button in app bar behave like the device back button, i.e. return to previous
    // activity without calling onCreate, in this way, search results in MainActivity are maintained
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Change the device back button to a back button of the embedded web browser
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mUrl);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider p = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        p.setShareIntent(intent);

        return super.onCreateOptionsMenu(menu);
    }
}
