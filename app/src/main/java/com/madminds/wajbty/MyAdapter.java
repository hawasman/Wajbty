package com.madminds.wajbty;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.*;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {
    private List<recipeItem> data = new ArrayList<>();

    private Context context;

    MyAdapter(Context context, List<recipeItem> MyData){
        this.context = context;
        data = MyData;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder myHolder, final int position) {

        recipeItem item = data.get(position);
        Glide.with(context).load(item.getrImg()).into(myHolder.rImage);
        Glide.with(context).load(item.getuImg()).into(myHolder.uImage);
        myHolder.rName.setText(item.getrName());
        myHolder.uName.setText(item.getuName());
        myHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeItem item = data.get(position);
                Intent intent = new Intent(context,RecipeActivity.class);
                intent.putExtra("uname",item.getuName());
                intent.putExtra("rname",item.getrName());
                intent.putExtra("uimg",item.getuImg());
                intent.putExtra("rimg",item.getrImg());
                intent.putExtra("recipe",item.getRecipe());
                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        TextView rName,uName;
        ImageView rImage,uImage;
        CardView card;
        MyHolder(View itemView){
            super(itemView);
            rName = (TextView)itemView.findViewById(R.id.rName);
            uName = (TextView)itemView.findViewById(R.id.uName);
            rImage = (ImageView)itemView.findViewById(R.id.rImage);
            uImage = (ImageView)itemView.findViewById(R.id.uImage);
            card = (CardView) itemView.findViewById(R.id.recipe_card);
        }
    }
}
