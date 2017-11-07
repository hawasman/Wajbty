package com.madminds.wajbty;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import com.roughike.bottombar.*;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        FragmentManager frmManager = getSupportFragmentManager();
        FragmentTransaction transaction = frmManager.beginTransaction();
        transaction.replace(R.id.content,new HomeFragment()).commit();
        BottomBar bnv = (BottomBar)findViewById(R.id.bottomBar);
        bnv.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentManager frmManager = getSupportFragmentManager();
                FragmentTransaction transaction = frmManager.beginTransaction();
                switch (tabId){
                    case R.id.tab_home:
                        transaction.replace(R.id.content,new HomeFragment()).commit();
                        break;

                    case R.id.tab_profile:
                        transaction.replace(R.id.content,new ProfileFragment()).commit();
                        break;

                    case R.id.tab_settings:
                        transaction.replace(R.id.content,new SettingsFragment()).commit();
                        break;
                }
            }
        });
    }
}


