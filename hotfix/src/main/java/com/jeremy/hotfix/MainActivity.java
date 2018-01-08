package com.jeremy.hotfix;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Cat mCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCat = new Cat();
    }


    public void click(View view) {
        Toast.makeText(this, mCat.say(), Toast.LENGTH_SHORT).show();
    }

}
