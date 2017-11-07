package com.madminds.wajbty;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;

import static android.content.Context.MODE_PRIVATE;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final String TABLE_NAME = "recipes";
    public final String DATABASE_NAME = "localRecipe";
    SQLiteDatabase myDatabase;
    final String HOST_IP = "https://wajbty.000webhostapp.com";
    final String HTTP_JSON_URL = HOST_IP + "/getalldata.php";

    private List<recipeItem> recipeItemList = new ArrayList<>();

    JsonArrayRequest jsonArrayRequest ;

    RequestQueue requestQueue ;

    FloatingActionButton fab;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_home, container, false);
        //myDatabase = getActivity().openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);

        makeJsonRequest(new JsonObjectListener() {
            @Override
            public void onDone(List<recipeItem> recipeItems) {
                RecyclerView rv = (RecyclerView)view.findViewById(R.id.recycle_home);
                rv.setHasFixedSize(true);
                MyAdapter myAdapter = new MyAdapter(getContext(),recipeItems);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(myAdapter);
            }

            @Override
            public void onError(String error) {
                Log.e("JsonError",error);
            }
        });
        fab = (FloatingActionButton)view.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddRecipe.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }
    public int isTableExistsAndHasRecoreds(String TableName){
        if (myDatabase == null || !myDatabase.isOpen()){

            myDatabase = getActivity().openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
        }
        Cursor cursor = myDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+ TableName+"'",null);
        if(cursor!=null){
            if(cursor.getCount() > 0){
                Cursor tCursor = myDatabase.rawQuery("select count(*) from " + TableName,null);
                tCursor.moveToFirst();
                if(tCursor.getCount() > 0){
                    return 2;
                }
                tCursor.close();
                return 1;
            }
        }
        cursor.close();
        return 0;
    }

    public interface JsonObjectListener {

        void onDone(List<recipeItem> recipeItems);

        void onError(String error);
    }

    public void makeJsonRequest(final JsonObjectListener jsonObjectListener){

        jsonArrayRequest = new JsonArrayRequest(HTTP_JSON_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<recipeItem> recipeItems = new ArrayList<>();
                        for(int i = 0; i<response.length(); i++) {
                            recipeItem recipe = new recipeItem();

                            JSONObject json;
                            try {
                                json = response.getJSONObject(i);

                                recipe.setrName(json.getString("name"));
                                recipe.setuName(json.getString("username"));
                                recipe.setrImg(json.getString("image"));
                                recipe.setRecipe(json.getString("recipe"));
                                recipe.setuImg(json.getString("uimage"));
                                Log.wtf("WTF",json.getString("image") + "," + json.getString("uimage"));

                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                            recipeItems.add(recipe);
                        }
                        if(jsonObjectListener != null)
                            jsonObjectListener.onDone(recipeItems);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(jsonObjectListener != null)
                            jsonObjectListener.onError(error.toString());
                    }
                });

        requestQueue = Volley.newRequestQueue(getActivity());

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array){


    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
