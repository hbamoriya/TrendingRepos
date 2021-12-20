package com.getmega.trendingrepos;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepoApi {
    private static final String url = "https://private-anon-893f88548f-githubtrendingapi.apiary-mock.com";

        public static RepoListApi RepoList = null;
        public static RepoListApi getService(){
            if(RepoList == null)
            {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                RepoList = retrofit.create(RepoListApi.class);
            }
            return RepoList;
        }
}
