package biz.agbo.baccus.controller.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import biz.agbo.baccus.R;
import biz.agbo.baccus.controller.fragment.WebFragment;
import biz.agbo.baccus.model.Wine;

public class WebActivity extends FragmentContainerActivity {

    public static final String EXTRA_WINE = "biz.agbo.baccus.controller.activity.WebActivity.extra_wine";


    @Override
    protected Fragment createFragment() {
        Bundle arguments = new Bundle();
        arguments.putSerializable(WebFragment.ARG_WINE, getIntent().getSerializableExtra(EXTRA_WINE));

        WebFragment fragment = new WebFragment();
        fragment.setArguments(arguments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
