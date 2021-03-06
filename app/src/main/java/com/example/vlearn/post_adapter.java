package com.example.vlearn;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.vlearn.PersonalInfo.BookmarkActivity;
import com.example.vlearn.object.key_Topic;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.toUpperCase;

public class post_adapter extends RecyclerView.Adapter<post_adapter.ProductViewHolder> implements Filterable {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Post_content> productList;
    private List<Post_content> searchList;

    //getting the context and product list with constructor
    public post_adapter(Context mCtx, List<Post_content> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.searchList=productList;
    }

    @Override
    public post_adapter.ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_post_layout, parent, false);
        return new post_adapter.ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(post_adapter.ProductViewHolder holder, int position) {
        //getting the product of the specified position



        Post_content product = searchList.get(position);


        holder.post_user.setAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.fade_transition_animation));

        holder.container.setAnimation(AnimationUtils.loadAnimation(mCtx,R.anim.fade_scale_animation));
        //binding the data with the viewholder views
        holder.post_user.setText(product.getUserName());
        holder.artical.setText(product.getPost_content());
        holder.upvote.setText(Integer.toString(product.getUpvotes()));
        holder.downvote.setText(Integer.toString(product.getDownvotes()));
        holder.post_title.setText(product.getPost_Title());
        holder.post_date.setText(product.getPost_Date());
        //holder.topic.setText(product.getTopic());
        holder.topic.setText(key_Topic.getTopic((product.getTopic()).charAt(0)));

        holder.user_id.setText(product.getUser_Id());
        holder.post_id.setText(product.getPost_Id());
       // String s=
        holder.bookmark.setText((product.getBookmark()).toString());
        Character name=product.getUserName().charAt(0);
        name=toUpperCase(name);
        holder.prof_icon.setText(name.toString());
       // if(position%2==0) {
        //    holder.prof_icon.setBackgroundResource(R.drawable.circle_view_lightgreen);
       // }



    }



    @Override
    public int getItemCount() {
        return searchList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String key=constraint.toString();
                if(key.isEmpty())
                {
                    searchList=productList;
                }else{

                    List<Post_content> filtered=new ArrayList<>();
                    for(Post_content row: productList)
                    {
                        if(row.getPost_content().toLowerCase().contains(key.toLowerCase())){
                            filtered.add(row);
                        }
                    }
                    searchList=filtered;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=searchList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                searchList=(List<Post_content>)results.values;
                notifyDataSetChanged();

            }
        };
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView post_user, artical, upvote, downvote,post_title,post_date,topic,user_id,post_id,bookmark,prof_icon;
        LinearLayout container;

        public ProductViewHolder(View itemView) {
            super(itemView);

            container=itemView.findViewById(R.id.postrel);
            post_user = itemView.findViewById(R.id.post_username);
            artical = itemView.findViewById(R.id.artical);
            upvote = itemView.findViewById(R.id.nup);
            downvote = itemView.findViewById(R.id.ndown);
            post_title=itemView.findViewById(R.id.post_title);
            post_date=itemView.findViewById(R.id.post_date);
            topic=itemView.findViewById(R.id.topic_tag);
            user_id=itemView.findViewById(R.id.user_id);
            post_id=itemView.findViewById(R.id.post_id);
            bookmark=itemView.findViewById(R.id.bookmarkState);
            prof_icon=itemView.findViewById(R.id.prof_icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostDetail.class);
                    intent.putExtra("User_Id",user_id.getText());
                    intent.putExtra("Post_User",post_user.getText());
                    intent.putExtra("Post",artical.getText());
                    intent.putExtra("Topic",topic.getText());
                    intent.putExtra("Post_Date",post_date.getText());
                    intent.putExtra("Post_Id",post_id.getText());
                    intent.putExtra("Post_Title",post_title.getText());
                    intent.putExtra("Upvotes",upvote.getText());
                    intent.putExtra("Downvotes",downvote.getText());
                    intent.putExtra("Bookmark",bookmark.getText());

                    Pair[] pairs=new Pair[2];
                    pairs[0]=new Pair<View,String>(prof_icon,"pImageTransition");
                    pairs[1]=new Pair<View,String>(post_user,"pNameTransition");
                    try
                    {
                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity)mCtx,pairs);

                    v.getContext().startActivity(intent,options.toBundle());}
                    catch(Exception e)
                    {
                        //ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(,pairs);

                        v.getContext().startActivity(intent);
                    }


                }
            });

        }


    }
}
