package com.getmega.trendingrepos;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {
    private Context context;

    public RepoAdapter(Context context, List<Repo> items) {
        this.context = context;
        this.items = items;
    }

    private List<Repo>items;
    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.repo_list,parent,false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
    Repo item = items.get(position);
    holder.repoTitle.setText(item.getAuthor());
    holder.reponame.setText(item.getName());
    Glide.with(context).load(item.getAvatar()).into(holder.repoImg);
    holder.description.setText(item.getDescription());
    holder.fork.setText(item.getForks());
    holder.stars.setText(item.getStars());
    holder.lang.setText(item.getLanguage());
    holder.lanuagecolor.setBackgroundColor(Color.parseColor(item.getLanguageColor()));

    //used glide Library for the image view :
        boolean isExpanded = items.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);



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
        CardView cardview;
        TextView fork;
        TextView stars;
        TextView lang;
        TextView lanuagecolor;
        public RepoViewHolder(View itemView){
            super(itemView);
            lanuagecolor = itemView.findViewById(R.id.lanColorButton);
            repoImg = (ImageView) itemView.findViewById(R.id.repoImg);
            repoTitle = (TextView) itemView.findViewById(R.id.repoTitle);
            reponame =(TextView) itemView.findViewById(R.id.reponame);
            description = (TextView) itemView.findViewById(R.id.description);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            cardview  = itemView.findViewById(R.id.cardview1);
            fork  = itemView.findViewById(R.id.forktext);
            stars  = itemView.findViewById(R.id.startext);
            lang  = itemView.findViewById(R.id.langtext);


            cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Repo item = items.get(getAdapterPosition());
                    item.setExpanded(!item.isExpanded());
                    for(int i=0;i<items.size();i++){
                        items.get(i).setExpanded(false);
                        notifyItemChanged(i);
                    }
                    item.setExpanded(!item.isExpanded());
//                    items.remove(item);
                    notifyItemChanged(getAdapterPosition());
                }
            });

//            repoTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    RepoList item = items.get(getAdapterPosition());
//                    item.setExpanded(!item.isExpanded());
//                    for(int i=0;i<items.size();i++){
//                        items.get(i).setExpanded(false);
//                        notifyItemChanged(i);
//                    }
////                    items.remove(item);
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
//            reponame.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    RepoList item = items.get(getAdapterPosition());
////                    if (!item.isExpanded()) item.setExpanded((item.isExpanded()));
////                    else item.setExpanded(!item.isExpanded());
//                    for(int i=0;i<items.size();i++){
//                        items.get(i).setExpanded(false);
//                        notifyItemChanged(i);
//                    }
//                    item.setExpanded(!item.isExpanded());
//
////                    items.remove(item);
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });

        }
    }
}
