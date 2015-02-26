package com.cuber.cuberlovematerial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button text = (Button)findViewById(R.id.main_text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.setClass(MainActivity.this, TextActivity.class);
                startActivity(intent);
            }
        });
        Button list1 = (Button)findViewById(R.id.main_list);
        list1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.setClass(MainActivity.this, List1Activity.class);
                startActivity(intent);
            }
        });
        Button list2 = (Button)findViewById(R.id.main_list2);
        list2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.setClass(MainActivity.this, List2Activity.class);
                startActivity(intent);
            }
        });
        Button list3 = (Button)findViewById(R.id.main_list3);
        list3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.setClass(MainActivity.this, List3Activity.class);
                startActivity(intent);
            }
        });
        Button button = (Button)findViewById(R.id.main_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.setClass(MainActivity.this, ButtonActivity.class);
                startActivity(intent);
            }
        });
    }

}
