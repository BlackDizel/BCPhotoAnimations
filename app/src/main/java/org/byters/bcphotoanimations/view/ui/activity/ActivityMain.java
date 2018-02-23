package org.byters.bcphotoanimations.view.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.controller.Injector;

public class ActivityMain extends ActivityBase {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Injector.getInstance().getNavigator()
                .setData(getSupportFragmentManager(), R.id.flContent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //todo implement presenter

        Injector.getInstance().getNavigator()
                .navigateProjectsList();
    }


    @Override
    public void onBackPressed() {
        if (!Injector.getInstance().getNavigator().onBackPressed())
            super.onBackPressed();
    }
}
