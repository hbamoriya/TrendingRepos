package com.getmega.trendingrepos;

import java.util.List;

public class RepoList {
    public RepoList(List<Repo> repoList) {
        this.repoList = repoList;
    }

    public RepoList() {
    }

    public List<Repo> getRepoList() {
        return repoList;
    }

    private List<Repo> repoList;
}
