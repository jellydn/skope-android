package vn.dev.Skope;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by tientun on 3/5/15.
 */
@EActivity
public abstract class BaseActionBarActivity extends ActionBarActivity {
    protected String tag = this.getClass().getSimpleName();

    @AfterViews
    protected void initView(){
        this.afterView();
    }

    abstract void afterView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addCustomActionbar();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        setTitle(title.toString());
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        setTitle(getResources().getString(titleId));
    }

    public void setTitle(String title) {
        if (getSupportActionBar().getCustomView() != null) {
            TextView tvTitle = (TextView) getSupportActionBar().getCustomView().findViewById(android.R.id.title);
            tvTitle.setText(title);
        } else {
            super.setTitle(title);
        }
    }

    protected void addCustomActionbar() {
        View customActionBarView = null;

        //Set title to center
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int size = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) {
            return;
        }

        params.setMargins(0, 0, size, 0);

        final LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext()
                .getSystemService(this.LAYOUT_INFLATER_SERVICE);
        customActionBarView = inflater.inflate(
                R.layout.custom_actionbar, null);
        TextView tvTitle = (TextView) customActionBarView.findViewById(android.R.id.title);
        tvTitle.setText(getTitle());
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(customActionBarView, params);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
}
