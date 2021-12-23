package com.getmega.trendingrepos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
//    Toolbar toolbar;
    SwipeRefreshLayout refreshLayout;
    int cacheSize = 10 * 1024 * 1024; //10MiB
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = findViewById(R.id.MyToolBar);
        refreshLayout = findViewById(R.id.refreshLayout);
//        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.repoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        getData();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

//    @
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String s){
//        getData();
//    }

    private void getData() {

        Cache cache = new Cache(getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        if (!isNetworkAvailable()) {
                            int maxStale = 60 * 60 * 2; //2hrs
                            request = request
                                    .newBuilder()
                                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                    .build();
                        }
                        return chain.proceed(request);
                    }
                })
                .build();
        Retrofit.Builder builder =new Retrofit.Builder()
                .baseUrl("https://private-anon-893f88548f-githubtrendingapi.apiary-mock.com")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create());


        refreshLayout.setRefreshing(true);
        Call<List<RepoList>> repoList = RepoApi.getService().getRepo();
        repoList.enqueue(new Callback<List<RepoList>>() {
            @Override
            public void onResponse(Call<List<RepoList>> call, Response<List<RepoList>> response) {
                ShimmerFrameLayout container =
                        (ShimmerFrameLayout) findViewById(R.id.shimmerFrameLayout);
                container.stopShimmer();
                container.setVisibility(View.GONE);

//                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();



                List<RepoList> list = response.body();
                recyclerView.setAdapter(new RepoAdapter(MainActivity.this, list));
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RepoList>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);

            }
        });
    }
}