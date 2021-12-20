package com.getmega.trendingrepos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RepoListApi {
    @GET("repositories")
    Call<List<RepoList>> getRepo();
}
