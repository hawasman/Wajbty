package com.madminds.wajbty;

import android.media.Image;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class RecipeActivity extends Activity {
    ImageView meal,user_image;
    TextView recipe_full,user_name;
    Toolbar toolbar;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        meal = (ImageView)findViewById(R.id.image_recipe );
        user_image = (ImageView)findViewById(R.id.uImage_recipe);
        recipe_full = (TextView)findViewById(R.id.recipe_full);
        user_name = (TextView)findViewById(R.id.uname_recipe);
        toolbar = (Toolbar)findViewById(R.id.toolbar_recipe);
        fab = (FloatingActionButton)findViewById(R.id.recipe_fab);
        Glide.with(this).load(getIntent().getExtras().getString("rimg")).into(meal);
        Glide.with(this).load(getIntent().getExtras().getString("uimg")).into(user_image);
        recipe_full.setText(getIntent().getExtras().getString("recipe"));
        user_name.setText(getIntent().getExtras().getString("uname"));
        toolbar.setTitle(getIntent().getExtras().getString("rname"));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(RecipeActivity.this,"Favorite",Toast.LENGTH_LONG).show();
            }
        });
    }
}
