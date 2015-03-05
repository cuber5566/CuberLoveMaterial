package com.cuber.cuberlovematerial;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.cuber.cuberlovematerial.views.FlatButton;
import com.cuber.cuberlovematerial.views.FloatingActionButton;
import com.cuber.cuberlovematerial.views.RaisedButton;

/**
 * Created by cuber on 2015/2/26.
 */
public class ButtonActivity extends ActionBarActivity {

    FlatButton flatButton;
RaisedButton raisedButton;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);
        flatButton = (FlatButton)findViewById(R.id.flatButton);
        raisedButton = (RaisedButton)findViewById(R.id.raisedButton);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            boolean disable = true;
            @Override
            public void onClick(View v) {
                disable = !disable;
                flatButton.setEnabled(disable);
                raisedButton.setEnabled(disable);
            }
        });

    }
}
