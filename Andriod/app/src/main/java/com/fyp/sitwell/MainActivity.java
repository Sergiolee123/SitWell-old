package com.fyp.sitwell;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.fyp.sitwell.muscleTraining.MuscleStrengthenActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private CardView mSittingView, mRelaxView, mExerciseView;
    private Toolbar  mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        checkFirstLogin();
        mSittingView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Sitting view clicked.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), HabitCorrectionActivity.class));
            }
        });

        mRelaxView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Relax view clicked.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), VideoSteamActivity.class));
            }
        });
        mExerciseView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "Exercise view clicked.",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MuscleStrengthenActivity.class));
            }
        });
        setSupportActionBar(mToolBar);
        mToolBar.setTitle("");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Menu clicked.",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void init(){
        mSittingView  = findViewById(R.id.card_sitting);
        mRelaxView    = findViewById(R.id.card_relax);
        mExerciseView = findViewById(R.id.card_exercise);
        mToolBar      = findViewById(R.id.toolbar_main);

    }
    private void checkFirstLogin(){
        if(1==2){

        }else{
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Toast.makeText(MainActivity.this,"Click info image",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}