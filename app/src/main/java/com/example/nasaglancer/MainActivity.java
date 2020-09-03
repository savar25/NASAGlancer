package com.example.nasaglancer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    DatePicker datePicker;
    Button button;
    ImageView iview,imageView;
  WebView playerView;
  DatePickerDialog pickerDialog;
  int mYear,mMonth,mDay;
  ProgressBar bar;
  ActionBarDrawerToggle toggle;
  NavigationView navigationView;
  Toolbar toolbar;
    Float mScaleFactor=1.f;
    private ScaleGestureDetector mScaleDetector;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView=findViewById(R.id.nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.libMedia:
                        playerView.stopLoading();
                        playerView.onPause();

                        Intent intent=new Intent(MainActivity.this,searchActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                    case R.id.dbMedia:
                        drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        button=findViewById(R.id.button);
        iview=findViewById(R.id.HubbleImage);
        playerView=findViewById(R.id.web);
        playerView.setVisibility(View.GONE);
        bar=findViewById(R.id.progressBar);
        bar.setVisibility(View.GONE);
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog pickerDialog=new DatePickerDialog(MainActivity.this,R.style.datePicker, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {


                        Calendar calendar = Calendar.getInstance();
                        iview.setImageResource(R.color.rand);

                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                        calendar1.set(Calendar.MONTH, datePicker.getMonth());
                        calendar1.set(Calendar.YEAR, datePicker.getYear());
                        calendar1.set(Calendar.HOUR_OF_DAY, 0);
                        calendar1.set(Calendar.MINUTE, 0);
                        calendar1.set(Calendar.SECOND, 0);
                        if (calendar1.getTimeInMillis() < calendar.getTimeInMillis()) {
                            String date = String.valueOf(datePicker.getYear()) + "-" + String.valueOf(datePicker.getMonth()) + "-" + String.valueOf(datePicker.getDayOfMonth());
                            Log.d(TAG, "onClick: " + date);
                            DataNetworkClass networkClass = RetrofitClientInstance.getRetrofit().create(DataNetworkClass.class);
                            bar.setVisibility(View.VISIBLE);
                            Call<ImViewerItem> img = networkClass.getImg(date);
                            img.enqueue(new Callback<ImViewerItem>() {
                                @Override
                                public void onResponse(Call<ImViewerItem> call, final Response<ImViewerItem> response) {
                                    Log.d(TAG, "onResponse: " + response.body().getType());
                                    switch (response.body().getType()) {
                                        case "image":
                                            Log.d(TAG, "onResponse: " + response.body().getUrl());
                                            playerView.stopLoading();
                                            playerView.onPause();
                                            playerView.setVisibility(View.GONE);
                                            iview.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new Picasso.Builder(MainActivity.this).downloader(new OkHttp3Downloader(MainActivity.this))
                                                            .build().load(response.body().getUrl()).resize(400, 400)
                                                            .placeholder(R.drawable.logo).error(R.drawable.ic_launcher_background).into(iview);

                                                    bar.setVisibility(View.GONE);
                                                }
                                            }, 1500);
                                            iview.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Toast.makeText(MainActivity.this, "Long Press to enlarge", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            iview.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View view) {

                                                    iview.setVisibility(View.GONE);
                                                    LayoutInflater li = LayoutInflater.from(MainActivity.this);
                                                    final View promptsView = li.inflate(R.layout.image_alert, null);


                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                            MainActivity.this);

                                                    alertDialogBuilder.setView(promptsView);

                                                    imageView = promptsView.findViewById(R.id.enlarger);
                                                    new Picasso.Builder(MainActivity.this).downloader(new OkHttp3Downloader(MainActivity.this))
                                                            .build().load(response.body().getUrl()).resize(300, 300).into(imageView);

                                                    imageView.setOnTouchListener(new View.OnTouchListener() {

                                                        @Override
                                                        public boolean onTouch(View view, MotionEvent motionEvent) {
                                                            mScaleDetector.onTouchEvent(motionEvent);
                                                            return true;
                                                        }
                                                    });
                                                    ImageButton button = promptsView.findViewById(R.id.imageButton);
                                                    alertDialogBuilder.setCancelable(false);

                                                    final AlertDialog dialog = alertDialogBuilder.create();
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            dialog.dismiss();
                                                            iview.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                                    dialog.show();
                                                    return true;
                                                }
                                            });

                                            break;
                                        case "video":
                                            bar.setVisibility(View.GONE);
                                            iview.setVisibility(View.GONE);
                                            playerView.stopLoading();
                                            playerView.onResume();
                                            playerView.setVisibility(View.VISIBLE);
                                            String playVideo = "<html><body>Youtube video .. <br> <iframe class=\"youtube-player\" type=\"text/html\" width=\"640\" height=\"385\" src=\"" + response.body().getUrl() + "\" frameborder=\"0\"></body></html>";

                                            Log.d(TAG, "onResponse: " + response.body().getUrl().toString());

                                            playerView.clearCache(true);
                                            playerView.getSettings().setJavaScriptEnabled(true);
                                            playerView.loadUrl(response.body().getUrl());

                                            playerView.setOnLongClickListener(new View.OnLongClickListener() {
                                                @Override
                                                public boolean onLongClick(View view) {
                                                    playerView.onPause();
                                                    playerView.setVisibility(View.GONE);
                                                    LayoutInflater li = LayoutInflater.from(MainActivity.this);
                                                    final View promptsView = li.inflate(R.layout.web_alert, null);


                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                            MainActivity.this);

                                                    alertDialogBuilder.setView(promptsView);
                                                    ImageButton button = promptsView.findViewById(R.id.cancel);
                                                    alertDialogBuilder.setCancelable(false);
                                                    WebView webView1 = promptsView.findViewById(R.id.web_enlarger);
                                                    webView1.loadUrl(response.body().getUrl());
                                                    webView1.getSettings().setJavaScriptEnabled(true);
                                                    webView1.getSettings().setLoadWithOverviewMode(true);

                                                    alertDialogBuilder.setCancelable(false);

                                                    final AlertDialog dialog = alertDialogBuilder.create();
                                                    button.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            playerView.setVisibility(View.VISIBLE);
                                                            playerView.onResume();
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    dialog.show();
                                                    return true;
                                                }
                                            });


                                    }

                                }

                                @Override
                                public void onFailure(Call<ImViewerItem> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Image Load Error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Toast.makeText(MainActivity.this, "Max Date till today", Toast.LENGTH_SHORT).show();
                        }
                    }
                },mYear,mMonth,mDay);
                pickerDialog.show();

            }
        });



    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            imageView.setScaleX(mScaleFactor);
            imageView.setScaleY(mScaleFactor);
            return true;
        }
    }
}