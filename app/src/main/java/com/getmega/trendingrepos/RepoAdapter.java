package com.getmega.trendingrepos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private Context context;

    public RepoAdapter(Context context, List<RepoList> items) {
        this.context = context;
        this.items = items;
    }

    private List<RepoList>items;
    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.repo_list,parent,false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
    RepoList item = items.get(position);
    holder.repoTitle.setText(item.getName());
    holder.repoDescrpition.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder{
        ImageView repoImg;
        TextView repoTitle;
        TextView repoDescrpition;
        public RepoViewHolder(View itemView){
            super(itemView);
            repoImg = (ImageView) itemView.findViewById(R.id.repoImg);
            repoTitle = (TextView) itemView.findViewById(R.id.repoTitle);
            repoDescrpition =(TextView) itemView.findViewById(R.id.repoDescription);

        }
    }
}
