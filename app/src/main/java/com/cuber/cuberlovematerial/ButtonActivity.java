package com.cuber.cuberlovematerial;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.cuber.cuberlovematerial.views.FlatButton;

/**
 * Created by cuber on 2015/2/26.
 */
public class ButtonActivity extends ActionBarActivity {

    FlatButton disabledFlatButton, hoverFlatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        disabledFlatButton = (FlatButton) findViewById(R.id.disabled_flatButton);
        disabledFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        hoverFlatButton = (FlatButton) findViewById(R.id.hover_flatButton);
        hoverFlatButton.setOnClickListener(new View.OnClickListener() {
            boolean disable = true;

            @Override
            public void onClick(View v) {
                disable = !disable;
                disabledFlatButton.setEnabled(disable);

                if (disable)
                    disabledFlatButton.setText("Normal");
                else
                    disabledFlatButton.setText("Disabled");
            }
        });

    }
}
