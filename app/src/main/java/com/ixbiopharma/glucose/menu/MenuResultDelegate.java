package com.ixbiopharma.glucose.menu;

import android.app.Activity;
import android.widget.Toast;

import com.ixbiopharma.glucose.ChromeTab;
import com.ixbiopharma.glucose.journal.JournalActivity;
import com.ixbiopharma.glucose.news.NewsListActivity;
import com.ixbiopharma.glucose.progress.ProgressActivity;

/**
 * MenuResultDelegate
 * <p>
 * Created by ivan on 28.04.17.
 */

public class MenuResultDelegate {

    public static void handleMenuResult(Activity activity, int resultCode) {
        switch (resultCode) {
            case MenuFragment.PROGRESS:
                if (!(activity instanceof ProgressActivity)) {
                    ProgressActivity.start(activity);
                    activity.finish();
                }
                break;
            case MenuFragment.JOURNAL:
                if (!(activity instanceof JournalActivity)) {
                    JournalActivity.start(activity);
                    activity.finish();
                }
                break;

            case MenuFragment.NEWS:
                if (!(activity instanceof NewsListActivity)) {
                    NewsListActivity.start(activity);
                    activity.finish();
                }
                break;

            default:
//                if (!(activity instanceof ChromeTab)) {
//                    ChromeTab.start(activity, "https://tackthis.co/shop/36835/");
//                    activity.finish();
//                }
                Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show();
        }
    }

}
