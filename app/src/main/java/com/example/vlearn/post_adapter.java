package com.example.vlearn;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class post_adapter extends RecyclerView.Adapter<post_adapter.ProductViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;

    //we are storing all the products in a list
    private List<Post_content> productList;

    //getting the context and product list with constructor
    public post_adapter(Context mCtx, List<Post_content> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
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
        Post_content product = productList.get(position);

        //binding the data with the viewholder views
        holder.post_user.setText(product.getUserName());
        holder.artical.setText(product.getPost_content());
        holder.upvote.setText(Integer.toString(product.getUpvotes()));
        holder.downvote.setText(Integer.toString(product.getDownvotes()));
        holder.post_title.setText(product.getPost_Title());
        holder.post_date.setText(product.getPost_Date());
        holder.topic.setText(product.getTopic());
        holder.user_id.setText(product.getUser_Id());
        holder.post_id.setText(product.getPost_Id());



    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView post_user, artical, upvote, downvote,post_title,post_date,topic,user_id,post_id;

        public ProductViewHolder(View itemView) {
            super(itemView);

            post_user = itemView.findViewById(R.id.post_username);
            artical = itemView.findViewById(R.id.artical);
            upvote = itemView.findViewById(R.id.nup);
            downvote = itemView.findViewById(R.id.ndown);
            post_title=itemView.findViewById(R.id.post_title);
            post_date=itemView.findViewById(R.id.post_date);
            topic=itemView.findViewById(R.id.topic);
            user_id=itemView.findViewById(R.id.user_id);
            post_id=itemView.findViewById(R.id.post_id);

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
                    v.getContext().startActivity(intent);


                }
            });

        }
    }
}
