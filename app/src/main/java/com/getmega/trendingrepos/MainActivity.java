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
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    private Context context ;

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
        context = MainActivity.this;
        refreshLayout = findViewById(R.id.refreshLayout);
//        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.repoList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nointernetLayout = findViewById(R.id.error_layout);
        tryAgainButton=findViewById(R.id.TryAgainButton);
//        errorLayout = findViewById(R.layout.error_layout);

//        Mypref = getApplicationContext().getSharedPreferences("Mypref",MODE_PRIVATE);
        Mypref = context.getSharedPreferences("Mypref",MODE_PRIVATE);

//        Mypref = .getSharedPreferences("Mypref",MODE_PRIVATE);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_title_layout);

//        if(Mypref != null) {
//            MycachedDataBase();
//        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDataFromApi();
            }
        });
        drawLayout();

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawLayout();
            }
        });

//        getData();


    }

    private List<Repo> getData(){
//        SharedPreferences mypref = getPreferences(Activity.MODE_PRIVATE);
        String savedCache = Mypref.getString("Cache","");
        ObjectMapper mapper = new ObjectMapper();
        List<Repo> savedList = null;
        RepoList r = new RepoList();

        try {
            r = mapper.readValue(savedCache,RepoList.class);
            savedList = r.getRepoList();
            return savedList;
//            recyclerView.setAdapter(new RepoAdapter(MainActivity.this,savedList));
//            nointernetLayout.setVisibility(View.GONE);
//            recyclerView.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            fetchDataFromApi();
            e.printStackTrace();
            return null;

        }



    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void drawLayout(){
        if(getData()!=null){
            nointernetLayout.setVisibility(View.GONE);
//            refreshLayout.setVisibility(View.VISIBLE);
            ShimmerFrameLayout container =
                    (ShimmerFrameLayout) findViewById(R.id.shimmerFrameLayout);
            container.stopShimmer();
//            refreshLayout.setRefreshing(false);
            container.setVisibility(View.GONE);
            recyclerView.setAdapter(new RepoAdapter(MainActivity.this,getData()));

            recyclerView.setVisibility(View.VISIBLE);

            getData();

        }
        else{
            recyclerView.setVisibility(View.GONE);
//            refreshLayout.setVisibility(View.GONE);
//            refreshLayout.setRefreshing(false);
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
    private void fetchDataFromApi() {


        refreshLayout.setRefreshing(true);
        Call<List<Repo>> repoList = RepoApi.getService().getRepo();
        repoList.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                ShimmerFrameLayout container =
                        (ShimmerFrameLayout) findViewById(R.id.shimmerFrameLayout);
                container.stopShimmer();
                container.setVisibility(View.GONE);

                List<Repo> list = response.body();
                ObjectMapper mapper = new ObjectMapper();

                RepoList newrepo = new RepoList(list);

                String cache = null;
                try {
                    cache = mapper.writeValueAsString(newrepo);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                Gson gson = new Gson();
//                String cache = gson.toJson(list);
                /*storing cache data */

                SharedPreferences.Editor editor = Mypref.edit();


                editor.putString("Cache",cache);
                /* applying to the List*/
//                Mypref.edit().apply();
                editor.commit();

                String Test = Mypref.getString("Cache","");
//                Mypref.getString("Cache","no Data Found");
            recyclerView.setAdapter(new RepoAdapter(MainActivity.this, list));
                recyclerView.setVisibility(View.VISIBLE);
                nointernetLayout.setVisibility(View.GONE);
//                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                drawLayout();
                Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();

                refreshLayout.setRefreshing(false);

            }

        });

    }
}