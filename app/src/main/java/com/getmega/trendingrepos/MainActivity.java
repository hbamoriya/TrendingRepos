package com.getmega.trendingrepos;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private ConstraintLayout nointernetLayout;
    private Button tryAgainButton;
    SwipeRefreshLayout refreshLayout;
//    int cacheSize = 10 * 1024 * 1024; //10MiB
//    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String TEXT = "text";
    SharedPreferences Mypref;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = findViewById(R.id.MyToolBar);
        refreshLayout = findViewById(R.id.refreshLayout);
//        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.repoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nointernetLayout = findViewById(R.id.error_layout);
        tryAgainButton=findViewById(R.id.TryAgainButton);
//        errorLayout = findViewById(R.layout.error_layout);
        Mypref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);

        if(Mypref != null) {
            MycachedDataBase();
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
        drawLayout();
//        tryAgainButton.setOnClickListener(new OnClick);
//        tryAgainButton.setOnClickListener(view -> {
//  drawLayout();
//        });

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawLayout();
            }
        });

//        getData();


    }

    private void MycachedDataBase(){
        String savedCache = Mypref.getString("Cache","");
        ObjectMapper mapper = new ObjectMapper();
        List<RepoList> savedList = null;
        try {
            savedList = (List<RepoList>) mapper.readValue(savedCache.getBytes(StandardCharsets.UTF_8),RepoList.class);
            recyclerView.setAdapter(new RepoAdapter(MainActivity.this,savedList));
        } catch (IOException e) {
            e.printStackTrace();
            getData();
        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void drawLayout(){
        if(isNetworkAvailable()){
            nointernetLayout.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);

            recyclerView.setVisibility(View.VISIBLE);

            getData();

        }
        else{
            recyclerView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            ShimmerFrameLayout container =
                    (ShimmerFrameLayout) findViewById(R.id.shimmerFrameLayout);
            container.stopShimmer();
            container.setVisibility(View.GONE);
//            getSupportActionBar().hide();
            nointernetLayout.setVisibility(View.VISIBLE);
            boolean flag =tryAgainButton.isEnabled();
            tryAgainButton.setEnabled(true);
        }
    }
    private void getData() {


//        refreshLayout.setRefreshing(true);
        Call<List<RepoList>> repoList = RepoApi.getService().getRepo();
        repoList.enqueue(new Callback<List<RepoList>>() {
            @Override
            public void onResponse(Call<List<RepoList>> call, Response<List<RepoList>> response) {
                ShimmerFrameLayout container =
                        (ShimmerFrameLayout) findViewById(R.id.shimmerFrameLayout);
                container.stopShimmer();
                container.setVisibility(View.GONE);

                List<RepoList> list = response.body();
                ObjectMapper mapper = new ObjectMapper();

                String cache = null;
                try {
                    cache = mapper.writeValueAsString(list);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                Gson gson = new Gson();
//                String cache = gson.toJson(list);
                /*storing cache data */
                Mypref.edit().putString("Cache",cache);
                /* applying to the List*/
//                Mypref.edit().apply();
                Mypref.edit().commit();
//                Mypref.getString("Cache","no Data Found");
                recyclerView.setAdapter(new RepoAdapter(MainActivity.this, list));
                recyclerView.setVisibility(View.VISIBLE);
                nointernetLayout.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<RepoList>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                drawLayout();
                Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

                refreshLayout.setRefreshing(false);

            }

        });

    }
}