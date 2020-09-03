package com.example.nasaglancer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MotionEventCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class searchActivity extends AppCompatActivity {

    EditText searchBar;
    ListView listView;
    ArrayList<String> main=new ArrayList<>();
    ImageView select,imageView;
   WebView videoView;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    Float mScaleFactor=1.f;
    private ScaleGestureDetector mScaleDetector;
    ProgressBar bar;
    private static final String TAG = "searchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        bar=findViewById(R.id.progressBar2);
        bar.setVisibility(View.GONE);
        toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.drawer1);
        toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Open,R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());

        navigationView=findViewById(R.id.nav1);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.libMedia:

                        drawerLayout.closeDrawer(GravityCompat.START);
                    case R.id.dbMedia:
                        listView.setVisibility(View.GONE);
                        videoView.stopLoading();
                        Intent intent=new Intent(searchActivity.this,MainActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });

        searchBar=findViewById(R.id.searchbar);
        listView=findViewById(R.id.searchList);

        select=findViewById(R.id.selectImage);
        listView.setVisibility(View.VISIBLE);
        videoView=findViewById(R.id.videoView);
        videoView.setVisibility(View.GONE);



        searchBar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    searchBar.setHint("");
                    Log.d(TAG, "onFocusChange: "+searchBar.getText().toString());
                    checkSearch(searchBar.getText().toString());
                }else{
                    searchBar.setHint("Search");
                }
            }
        });

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                main.clear();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkSearch(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void checkSearch(CharSequence charSequence){
        main.clear();
        listView.setVisibility(View.VISIBLE);
        select.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        videoView.onPause();
        if (!String.valueOf(charSequence).isEmpty()) {
            SearcherNetworkClass networkClass = RetrofitClientInstance2.getRetrofit().create(SearcherNetworkClass.class);
            Call<collection> outers = networkClass.getSearchResult(charSequence);
            outers.enqueue(new Callback<collection>() {
                @Override
                public void onResponse(Call<collection> call, final Response<collection> response) {
                    main.clear();
                    Log.d(TAG, "onResponse: called");
                    if (response.body().getMajor().getCoverObject().size() > 0) {
                        for (int i = 0; i < 10; i++) {
                            Log.d(TAG, "onResponse: " + response.body().getMajor());
                            if (i<response.body().getMajor().getCoverObject().size()) {
                                main.add(String.valueOf(response.body().getMajor().getCoverObject().get(i).getDataList().get(0).desc));
                            }
                        }
                    }else{
                        main.add("No items....Put more details");
                    }

                    ListAdapter adapter = new ArrayAdapter<String>(searchActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, main) {
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);

                            TextView tv = v.findViewById(android.R.id.text1);
                            tv.setText(main.get(position));
                           tv.setSelected(true);
                            tv.setHorizontallyScrolling(true);
                            tv.setLines(1);

                            return v;
                        }
                    };

                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            listView.setVisibility(View.GONE);
                            bar.setVisibility(View.VISIBLE);
                            AssetImageNetworkClass networkClass1 = RetrofitClientInstance2.getRetrofit().create(AssetImageNetworkClass.class);
                            final Call<ImageCollection> imageSet = networkClass1.getReqImg(response.body().getMajor().getCoverObject().get(i).getDataList().get(0).getId());
                            imageSet.enqueue(new Callback<ImageCollection>() {
                                @Override
                                public void onResponse(Call<ImageCollection> call, Response<ImageCollection> response) {

                                    final String urL = response.body().getImageOuterCover().getImageResources().get(1).getJpegUrl().replace("http", "https");
                                    Log.d(TAG, "onResponse: " + urL);
                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);

                                   if (urL.endsWith(".mp4")) {
                                       bar.setVisibility(View.GONE);
                                        select.setVisibility(View.GONE);
                                        videoView.setVisibility(View.VISIBLE);
                                        videoView.getSettings().setJavaScriptEnabled(true);
                                        videoView.loadUrl(urL);
                                        videoView.onResume();

                                        videoView.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View view) {
                                                videoView.onPause();
                                                videoView.setVisibility(View.GONE);
                                                LayoutInflater li = LayoutInflater.from(searchActivity.this);
                                                final View promptsView = li.inflate(R.layout.web_alert, null);


                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        searchActivity.this);

                                                alertDialogBuilder.setView(promptsView);
                                                ImageButton button=promptsView.findViewById(R.id.cancel);
                                                alertDialogBuilder.setCancelable(false);
                                                WebView webView1=promptsView.findViewById(R.id.web_enlarger);
                                                webView1.loadUrl(urL);
                                                webView1.getSettings().setJavaScriptEnabled(true);
                                                webView1.getSettings().setLoadWithOverviewMode(true);

                                                alertDialogBuilder.setCancelable(false);

                                                final AlertDialog dialog=alertDialogBuilder.create();
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        videoView.setVisibility(View.VISIBLE);
                                                        videoView.onResume();
                                                        dialog.dismiss();
                                                    }
                                                });
                                                dialog.show();
                                                return true;
                                            }
                                        });


                                    } else {
                                        videoView.setVisibility(View.GONE);
                                        select.setVisibility(View.VISIBLE);
                                        new Picasso.Builder(searchActivity.this)
                                                .downloader(new OkHttp3Downloader(searchActivity.this))
                                                .build()
                                                .load(urL)
                                                .resize(300, 300)
                                                .error(R.drawable.ic_launcher_foreground)
                                                .into(select);

                                        bar.setVisibility(View.GONE);


                                        select.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(searchActivity.this, "Long Press to enlarge", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        select.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View view) {
                                                select.setVisibility(View.GONE);
                                                LayoutInflater li = LayoutInflater.from(searchActivity.this);
                                                final View promptsView = li.inflate(R.layout.image_alert, null);


                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        searchActivity.this);

                                                alertDialogBuilder.setView(promptsView);

                                                imageView=promptsView.findViewById(R.id.enlarger);
                                                new Picasso.Builder(searchActivity.this).downloader(new OkHttp3Downloader(searchActivity.this))
                                                        .build().load(urL).resize(300,300).into(imageView);
                                                imageView.setOnTouchListener(new View.OnTouchListener() {

                                                    @Override
                                                    public boolean onTouch(View view, MotionEvent motionEvent) {
                                                        mScaleDetector.onTouchEvent(motionEvent);
                                                        return true;
                                                    }
                                                });



                                                ImageButton button=promptsView.findViewById(R.id.imageButton);
                                                alertDialogBuilder.setCancelable(false);

                                                final AlertDialog dialog=alertDialogBuilder.create();
                                                button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                        select.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                                dialog.show();
                                                return true;
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFailure(Call<ImageCollection> call, Throwable t) {

                                }
                            });
                            Toast.makeText(searchActivity.this, response.body().getMajor().getCoverObject().get(i).getDataList().get(0).getId(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }

                @Override
                public void onFailure(Call<collection> call, Throwable t) {

                }
            });
        }
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