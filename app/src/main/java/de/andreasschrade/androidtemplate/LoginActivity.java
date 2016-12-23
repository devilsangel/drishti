package de.andreasschrade.androidtemplate;

import android.content.Intent;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.andreasschrade.androidtemplate.ui.base.BaseActivity;
import de.andreasschrade.androidtemplate.ui.quote.ListActivity;

public class LoginActivity extends BaseActivity{

    @Bind(R.id.guest)
    Button guest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_SHORT).show();
        setupToolbar();
    }

    private void setupToolbar() {
        final ActionBar ab = getActionBarToolbar();
        ab.hide();
    }
    @OnClick(R.id.guest)
    public void guest()
    {
        Intent intent=new Intent(LoginActivity.this, ListActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean providesActivityToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                openDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return R.id.nav_settings;
    }

    public static class SettingsFragment extends PreferenceFragment {
        public SettingsFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_prefs);
        }
    }
}
