package com.fyp.sitwell;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fyp.sitwell.alarm.MuscleRelaxSetting;
import com.fyp.sitwell.habitCorrection.HabitCorrectionSetting;
import com.fyp.sitwell.report.DBHandler;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "PrefsFile";


    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar  mToolBar;
    private FirebaseAuth mFirebaseAuth;
    private static DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigation_view = (NavigationView) findViewById(R.id.nav_view);
        mToolBar      = findViewById(R.id.toolbar);
        mFirebaseAuth = FirebaseAuth.getInstance();
        dbHandler= new DBHandler(this);
        dbHandler.getUserSittingRec().setUserID(mFirebaseAuth.getCurrentUser().getUid());
        dbHandler.userID = mFirebaseAuth.getCurrentUser().getUid();

        Log.e("MainActivity", ""+mFirebaseAuth.getCurrentUser().getUid());
        navigation_view.setCheckedItem(R.id.nav_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                new MainFragment()).commit();
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                            new MainFragment()).commit();
                    return true;
                }
                else if (id == R.id.nav_stats) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                            new StatsFragment()).commit();
                    return true;
                }
                else if (id == R.id.nav_profile) {
                    showCustomDialog();
                    return false;
                }
                else if(id == R.id.nav_setting){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                            new SettingPageFragment()).commit();
                }
                else if (id == R.id.nav_logout){
                    mFirebaseAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
                return false;
            }
        });
        if (mFirebaseAuth != null){
            String name = mFirebaseAuth.getCurrentUser().getDisplayName();
            mToolBar.setTitle("Hello," + name);
        }
        setSupportActionBar(mToolBar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, mToolBar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String FirstTime = pref.getString("FirstTime", "true");

        if(FirstTime.equals("true")){
            showCustomDialog();
            pref.edit().putString("FirstTime", String.valueOf(false)).commit();
        }else if(FirstTime.equals("false")) {

        }

    }

    public void startMuscleRelaxSetting(View view){
        startActivity(new Intent(this, MuscleRelaxSetting.class));
    }


    public void startHabitCorrectionSetting(View view){
        startActivity(new Intent(this, HabitCorrectionSetting.class));
    }

    private void isFirstLogin(){
        showCustomDialog();
    }

    private void showCustomDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.65);
        dialog.setContentView(R.layout.dialog_profile);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
        dialog.setCancelable(false);
        Button mConfirmBtn = dialog.findViewById(R.id.button_confirm);
        //Button mCancelBtn  = dialog.findViewById(R.id.button_cancel);

        TextInputLayout mLayoutHeight = (TextInputLayout) dialog.findViewById(R.id.layout_height);
        TextInputLayout mLayoutWeight =(TextInputLayout) dialog.findViewById(R.id.layout_weight);
        TextInputLayout mLayoutAge = (TextInputLayout) dialog.findViewById(R.id.layout_age);
        EditText mEditHeight = mLayoutHeight.getEditText();
        EditText mEditWeight = mLayoutWeight.getEditText();
        EditText mEditAge = mLayoutAge.getEditText();

        // do init..
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String pref_height = pref.getString("height","");
        String pref_weight = pref.getString("weight","");
        String pref_age = pref.getString("age","");
        mEditAge.setText(pref_age);
        mEditWeight.setText(pref_weight);
        mEditHeight.setText(pref_height);

        mConfirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mLayoutAge.setErrorEnabled(true);
                mLayoutWeight.setErrorEnabled(true);
                mLayoutHeight.setErrorEnabled(true);
                String age = mEditAge.getText().toString();
                String weight = mEditWeight.getText().toString();
                String height = mEditHeight.getText().toString();
                if(validHeight(mLayoutHeight,height) &&
                        validWeight(mLayoutWeight,weight) &&
                        validAge(mLayoutAge,age)){

                    pref.edit().putString("height",height)
                            .putString("weight",weight)
                            .putString("age",age).commit();


                    dialog.dismiss();
                }
            }
        });
        /*
        mCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 111) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                    new MainFragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_home);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 112) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                    new MainFragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_home);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 113) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container ,
                    new MainFragment()).commit();
            navigation_view.setCheckedItem(R.id.nav_home);
        }       

    }

    private boolean validAge(TextInputLayout layoutAge,String age) {
        if (!age.equals("")){

            int value = Integer.parseInt(age);
            if (value >= 18 && value <= 45) {

                layoutAge.setError(null);
                layoutAge.setErrorEnabled(false);
                return true;
            }else {
                showError(layoutAge,"You don't meet the age requirement. Age:18-45");
                return false;
            }
        }else{
            showError(layoutAge,"Please enter your age");
            return false;
        }
    }

    private boolean validWeight(TextInputLayout layoutWeight,String weight) {
        if(!weight.equals("")) {
            int value = Integer.parseInt(weight);
            if (value >= 45 && value <= 90) {
                layoutWeight.setError(null);
                layoutWeight.setErrorEnabled(false);
                return true;
            }else{
                showError(layoutWeight,"You don't meet the weight requirement. Weight:45-90kg");
                return false;
            }
        }else{
            showError(layoutWeight,"Please enter your weight");
            return false;
        }
    }

    private boolean validHeight(TextInputLayout layoutHeight,String height) {
        if(!height.equals("")) {
            int value = Integer.parseInt(height);
            if (value >= 150 && value <= 200) {
                layoutHeight.setError(null);
                layoutHeight.setErrorEnabled(false);
                return true;
            }else{
                showError(layoutHeight,"You don't meet the height requirement. Height:150-200cm");
                return false;
            }
        }else{
            showError(layoutHeight,"Please enter your height");
            return false;
        }
    }

    private void showError(TextInputLayout textInputLayout,String error){
        textInputLayout.setError(error);
        textInputLayout.getEditText().setFocusable(true);
        textInputLayout.getEditText().setFocusableInTouchMode(true);
        textInputLayout.getEditText().requestFocus();
    }
}