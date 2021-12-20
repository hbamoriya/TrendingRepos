package com.getmega.trendingrepos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
    }

    private void getData() {
        Call<List<RepoList>> repoList = RepoApi.getService().getRepo();
        repoList.enqueue(new Callback<List<RepoList>>() {
            @Override
            public void onResponse(Call<List<RepoList>> call, Response<List<RepoList>> response) {
                List<RepoList> list = response.body();
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<RepoList>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });
    }
}