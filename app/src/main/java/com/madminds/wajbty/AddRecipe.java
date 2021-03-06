package com.madminds.wajbty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddRecipe extends AppCompatActivity {
    EditText name,recipe;
    TextView textView;
    ImageView image;
    Button btn;
    Bitmap photo;
    String URL = "http://wajbty.atwebpages.com/upload.php";
    Cloudinary cloudinary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        name = (EditText)findViewById(R.id.meal_name);
        recipe = (EditText)findViewById(R.id.recipe_text);
        image = (ImageView)findViewById(R.id.meal_image);
        textView = (TextView) findViewById(R.id.textView);
        btn = (Button)findViewById(R.id.btn_add_recipe);
        Map configmap = new HashMap();
        configmap.put("cloud_name", "hawasman");
        configmap.put("api_key", "638951816456322");
        configmap.put("api_secret","XE7rCn4F8SMdbbrrsPI0w8Ub2P4");
        cloudinary = new Cloudinary(configmap);

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
                Upload upload = new Upload(cloudinary);
                upload.execute();
            }
        });
    }

    class Upload extends AsyncTask<String, Void, String> {
        private Cloudinary mCloudinary;
        Map cloudinaryResult;
        public Upload(Cloudinary cloudinary) {
            super();
            mCloudinary = cloudinary;
        }
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                cloudinaryResult = mCloudinary.uploader().upload("data:image/jpeg;base64," + getStringFromBitmap(photo),ObjectUtils.emptyMap());
                Log.v("Bitmap","data:image/jpeg;base64," + getStringFromBitmap(photo));
                String cloudURL = cloudinaryResult.get("public_id").toString();
                uploadImage(cloudURL);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }


    public String getStringFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream byteIO = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,0,byteIO);
        byte[] imgByte = byteIO.toByteArray();
        String output = Base64.encodeToString(imgByte,Base64.DEFAULT);
        return output;
    }
    private void uploadImage(final String Url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.wtf("WTF",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("WTF",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String mName = name.getText().toString();
                String mRecipe = recipe.getText().toString();
                String mImage = Url;
                Map<String,String> prams = new HashMap<String,String>();
                prams.put("name",mName);
                prams.put("recipe",mRecipe);
                prams.put("image",mImage);

                return prams;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
