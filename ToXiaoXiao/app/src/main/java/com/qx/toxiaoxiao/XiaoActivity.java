package com.qx.toxiaoxiao;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class XiaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiao);
        getSupportActionBar().hide();

        //phone
        Button phoneButton = (Button)findViewById(R.id.call);
        phoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10000"));
                startActivity(intent);
            }
        });
        phoneButton.getBackground().setAlpha(200);

        //internet
        Button internetButton = (Button)findViewById(R.id.internet);
        internetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(XiaoActivity.this, internetActivity.class);
                startActivity(intent);
            }
        });
        internetButton.getBackground().setAlpha(200);
    }
}
