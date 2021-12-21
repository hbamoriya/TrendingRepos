package com.getmega.trendingrepos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
    holder.repoTitle.setText(item.getAuthor());
    holder.reponame.setText(item.getName());
    holder.description.setText(item.getDescription());
    //used glide Library for the image view :
        boolean isExpanded = items.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    Glide.with(context)
            .load(items.get(position).getImage())
            .into(holder.repoImg);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RepoViewHolder extends RecyclerView.ViewHolder{
        ImageView repoImg;
        TextView repoTitle;
        TextView reponame;
        TextView description;
        ConstraintLayout expandableLayout;
        public RepoViewHolder(View itemView){
            super(itemView);
            repoImg = (ImageView) itemView.findViewById(R.id.repoImg);
            repoTitle = (TextView) itemView.findViewById(R.id.repoTitle);
            reponame =(TextView) itemView.findViewById(R.id.reponame);
            description = (TextView) itemView.findViewById(R.id.description);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            repoTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RepoList item = items.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded());
//                    items.remove(item);
                    notifyItemChanged(getAdapterPosition());
                }
            });
            reponame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RepoList item = items.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded());
//                    items.remove(item);
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
