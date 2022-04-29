package com.fyp.sitwell;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.fyp.sitwell.alarm.MuscleRelaxSetting;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.fyp.sitwell.muscleTraining.MuscleRelaxActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

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
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
    }

    public void startMuscleRelaxSetting(View view){
        startActivity(new Intent(this, MuscleRelaxSetting.class));
    }

    private void isFirstLogin(){
        showCustomDialog();
    }

    private void showCustomDialog(){
        final Dialog dialog = new Dialog(MainActivity.this);
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.75);
        dialog.setContentView(R.layout.dialog_profile);
        dialog.getWindow().setLayout(width, height);
        dialog.show();
        dialog.setCancelable(false);
        Button mConfirmBtn = dialog.findViewById(R.id.button_confirm);
        Button mCancelBtn  = dialog.findViewById(R.id.button_cancel);
        mConfirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // do.....
                dialog.dismiss();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


}