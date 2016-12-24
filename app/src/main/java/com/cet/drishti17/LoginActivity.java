package com.cet.drishti17;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.cet.drishti17.ui.base.BaseActivity;
import com.cet.drishti17.ui.quote.ListActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends BaseActivity{

    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 0;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupToolbar();
        auth=FirebaseAuth.getInstance();
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.cet.drishti17", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("YourKeyHash :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                //System.out.println("YourKeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    @OnClick(R.id.logout)
    public void logOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                );
    }
    @OnClick(R.id.gplus)
    public void signIn()
    {
        if(auth.getCurrentUser()!=null){
            //user already authenticated
            Toast.makeText(getApplicationContext(),"Logged In Already",Toast.LENGTH_SHORT).show();
        }
        else
        {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .setLogo(R.drawable.p3)
                    .setTheme(R.style.Base_Theme)
                    .build(),RC_SIGN_IN);
        }
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            if(resultCode==RESULT_OK){
                //user logged in
                Log.d("AuthNew",auth.getCurrentUser().getEmail());
                Intent intent=new Intent(LoginActivity.this,ListActivity.class);
                intent.putExtra("Data",auth.toString());
                startActivity(intent);
            }
        }
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
