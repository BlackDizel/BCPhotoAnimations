package org.byters.bcphotoanimations.view.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.byters.bcphotoanimations.ApplicationStopMotion;
import org.byters.bcphotoanimations.R;
import org.byters.bcphotoanimations.view.INavigator;

import javax.inject.Inject;

public class ActivityMain extends ActivityBase {

    @Inject
    INavigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApplicationStopMotion.getComponent().inject(this);

        navigator.setData(getSupportFragmentManager(), R.id.flContent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        //todo implement presenter

        navigator.navigateProjectsList();
    }


    @Override
    public void onBackPressed() {
        if (!navigator.onBackPressed())
            super.onBackPressed();
    }
}
