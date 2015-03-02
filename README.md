# Material Button

![Screenshot](https://github.com/cuber5566/CuberLoveMaterial/blob/master/app/src/main/res/drawable/button_readme.jpg)
# attrs
``` java
    <!-- Floating Action Button -->
    <declare-styleable name="FloatingActionButton">
        <attr name="button_float_action_background_color" format="color"/>

    </declare-styleable>

    <!-- Raised Button -->
    <declare-styleable name="RaisedButton">
        <attr name="button_raised_background_color" format="color"/>
        <attr name="button_raised_ripple_color" format="color"/>
    </declare-styleable>

    <!-- Flat Button -->
    <declare-styleable name="FlatButton">
        <attr name="button_flat_press_color" format="color"/>
        <attr name="button_flat_ripple_color" format="color"/>
    </declare-styleable>
```
# xml
``` java 
    <com.cuber.cuberlovematerial.views.FlatButton
        android:id="@+id/hover_flatButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="FlatButton"
        android:layout_marginBottom="@dimen/activity_vertical_margin"
        app:button_flat_press_color="@color/accent"
        app:button_flat_ripple_color="@color/white"
        />

    <com.cuber.cuberlovematerial.views.RaisedButton
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="RaisedButton"
        android:layout_marginBottom="@dimen/activity_vertical_margin"
        app:button_raised_background_color="@color/white"
        app:button_raised_ripple_color="@color/white"
        />

    <com.cuber.cuberlovematerial.views.FloatingActionButton
        android:id="@+id/fab"
        android:layout_width="@dimen/fab_size"
        android:layout_height="@dimen/fab_size"
        android:padding="@dimen/fab_padding"
        app:button_float_action_background_color="@color/accent"
        android:src="@drawable/ic_add_white_24dp" />
```
