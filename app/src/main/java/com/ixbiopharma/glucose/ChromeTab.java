package com.ixbiopharma.glucose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ixbiopharma.glucose.component.TabDelegate;
import com.ixbiopharma.glucose.component.ToolbarView;
import com.ixbiopharma.glucose.menu.MenuResultDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * ChromeTab
 * <p>
 * Created by ivan on 14.05.17.
 */

public class ChromeTab extends BaseActivity {

    @BindView(R.id.tab)
    TabLayout tabLayout;

    @BindView(R.id.toolbar)
    ToolbarView toolbarView;

    @BindView(R.id.webView)
    WebView webView;

    private static String URL = "URL";

    public static void start(Activity activity, String url) {
        Intent intent = new Intent(activity, ChromeTab.class);
        intent.putExtra(URL, url);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrome);
        ButterKnife.bind(this);

        toolbarView.showHomeArrow(true);
        toolbarView.setHomeArrowIcon(R.drawable.ic_menu);
        toolbarView.showAction(false);
        toolbarView.setHomeClickListener(v -> showMenu(3));
        toolbarView.setTitle("Store");

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(getIntent().getStringExtra(URL));

        new TabDelegate().init(tabLayout, position ->
                MenuResultDelegate.handleMenuResult(ChromeTab.this, position), 3);

    }
}
