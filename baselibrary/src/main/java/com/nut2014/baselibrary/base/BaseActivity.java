package com.nut2014.baselibrary.base;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author feiltel 2019/9/29 0029
 */
public class BaseActivity extends AppCompatActivity {
    public static boolean isActive = false;

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
    }
}
