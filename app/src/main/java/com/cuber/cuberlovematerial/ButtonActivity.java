package com.cuber.cuberlovematerial;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

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

    }
}
