package com.ixbiopharma.glucose;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ixbiopharma.glucose.menu.MenuFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * BaseActivity
 *
 * Created by ivan on 02.04.17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    protected void showMenu(int position){
        getFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, MenuFragment.newInstance(position))
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
