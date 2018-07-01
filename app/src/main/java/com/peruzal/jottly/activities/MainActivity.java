package com.peruzal.jottly.activities;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.peruzal.jottly.R;
import com.peruzal.jottly.fragments.AddEditJournalFragment;
import com.peruzal.jottly.fragments.MainFragment;
import com.peruzal.jottly.fragments.ViewJournalEntryFragment;
import com.peruzal.jottly.utils.Constants;
import com.peruzal.jottly.utils.JournalEntryAction;
import com.peruzal.jottly.utils.NavigationListener;
import com.peruzal.jottly.utils.SharedPreferencesUtils;
import com.peruzal.jottly.viewmodels.JournalViewModel;

public class MainActivity extends AppCompatActivity implements NavigationListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private JournalViewModel mJournalViewModel;
    private FragmentManager mFragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mJournalViewModel = ViewModelProviders.of(this).get(JournalViewModel.class);
        mJournalViewModel.setEmailAddress(SharedPreferencesUtils.getEmailAddress(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       mFragmentManager = getSupportFragmentManager();
       mFragmentManager
               .beginTransaction()
               .replace(R.id.content, MainFragment.newInstance(this))
               .commit();
    }

    public void addJournalFragment() {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.content, AddEditJournalFragment.newInstance(this, JournalEntryAction.ADD))
                .commit();
    }

    public void loadMainFragment(){
        mFragmentManager
                .beginTransaction()
                .replace(R.id.content, MainFragment.newInstance(this))
                .addToBackStack(null)
                .commit();
    }


    public void viewJournalFragment(){
        mFragmentManager
                .beginTransaction()
                .replace(R.id.content, ViewJournalEntryFragment.newInstance(this))
                .addToBackStack(null)
                .commit();
    }


    public void updatedateJournalFragment() {
        mFragmentManager
                .beginTransaction()
                .replace(R.id.content, AddEditJournalFragment.newInstance(this, JournalEntryAction.UPDATE))
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void setAction(JournalEntryAction action) {
        switch (action){
            case ADD:
                addJournalFragment();
                break;
            case ACTION_COMPLETED:
                loadMainFragment();
                break;
            case UPDATE:
                updatedateJournalFragment();
                break;
            case VIEW:
                viewJournalFragment();
                break;
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.action_sign_out:
//                signOut();
//                break;
//        }
//        return true;
//    }

    private void signOut() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestId()
                .requestEmail()
                .requestProfile()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(this, t -> {
            sharedPreferences.edit().remove(Constants.EMAIL_ADDRESS).apply();
            finish();
        });
    }
}
