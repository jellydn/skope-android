package com.productsway.skope;

/**
 * Skope
 *
 * Created by Vo Hoang San - hoangsan.762@gmai.com
 * Copyright (c) 2015 San Vo. All right reserved.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.productsway.skope.custom.CustomActivity;

public class LoginActivity extends CustomActivity {
    Button btnLoginFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initControls();
        initListeners();
    }


    @Override
    public void initData() {

    }

    @Override
    public void initControls() {
        btnLoginFacebook = (Button) this.findViewById(R.id.btn_login_facebook);
    }

    @Override
    public void initListeners() {
        btnLoginFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
            }
        });
    }
}
