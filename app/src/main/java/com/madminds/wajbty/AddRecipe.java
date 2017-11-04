package com.madminds.wajbty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class AddRecipe extends AppCompatActivity {
    EditText name,recipe;
    ImageView image;
    Button btn;
    Bitmap photo;
    String URL = "http://192.168.1.100/android/upload.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        name = (EditText)findViewById(R.id.meal_name);
        recipe = (EditText)findViewById(R.id.recipe_text);
        image = (ImageView)findViewById(R.id.meal_image);
        btn = (Button)findViewById(R.id.btn_add_recipe);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 999);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    public String getStringFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteIO = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteIO);
        byte[] imgByte = byteIO.toByteArray();
        String output = Base64.encodeToString(imgByte,Base64.DEFAULT);
        return output;
    }
    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(AddRecipe.this);
        progressDialog.setMessage("جاري الاضافة...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                Log.wtf("WTF",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
                Log.wtf("WTF",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String mName = name.getText().toString();
                String mRecipe = recipe.getText().toString();
                String mImage = getStringFromBitmap(photo);
                Map<String,String> prams = new HashMap<String,String>();
                prams.put("name",mName);
                prams.put("recipe",mRecipe);
                prams.put("image",mImage);

                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        progressDialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 & resultCode == RESULT_OK && data != null){
            try{
                photo = (Bitmap) data.getExtras().get("data");
                image.setImageBitmap(photo);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
