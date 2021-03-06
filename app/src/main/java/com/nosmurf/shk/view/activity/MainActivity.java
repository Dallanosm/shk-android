package com.nosmurf.shk.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.nosmurf.domain.model.Access;
import com.nosmurf.shk.R;
import com.nosmurf.shk.internal.di.component.DaggerMainComponent;
import com.nosmurf.shk.internal.di.component.MainComponent;
import com.nosmurf.shk.presenter.MainPresenter;
import com.nosmurf.shk.presenter.Presenter;
import com.nosmurf.shk.utils.AnimationUtils;
import com.nosmurf.shk.view.adapter.AccessAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class MainActivity extends RootActivity implements MainPresenter.View {

    public static final String TAG = "MainActivity";

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    MainComponent mainComponent;

    @Inject
    MainPresenter mainPresenter;

    @Bind(R.id.container)
    RelativeLayout container;

    @Bind(R.id.progress)
    ContentLoadingProgressBar progress;

    @Bind(R.id.camera)
    FloatingActionButton camera;

    @Bind(R.id.fingerprint)
    RelativeLayout fingerPrint;

    @Bind(R.id.access)
    RecyclerView access;

    private AccessAdapter accessAdapter;

    public static Intent getCallingIntent(RootActivity rootActivity) {
        return new Intent(rootActivity, MainActivity.class);
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    public Presenter getPresenter() {
        return mainPresenter;
    }

    @Override
    protected void initializeInjector() {
        mainComponent = DaggerMainComponent.builder()
                .appComponent(getAppComponent())
                .build();

        mainComponent.inject(this);
    }

    @Override
    protected void initializeUI() {
        this.accessAdapter = new AccessAdapter();
        access.setAdapter(accessAdapter);
        access.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initializePresenter() {
        mainPresenter.start(this);
    }

    @Override
    protected void registerListeners() {
        camera.setOnClickListener(view -> mainPresenter.takeAPhoto(REQUEST_IMAGE_CAPTURE));
    }

    @Override
    public void showProgress(String message) {
        progress.setVisibility(View.VISIBLE);
        if (message != null) {
            showSnackbar(message);
        }
    }

    @Override
    public void showProgress(int messageId) {
        progress.setVisibility(View.VISIBLE);
        if (messageId != 0) {
            showSnackbar(messageId);
        }
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        showSnackbar(message);
    }

    @Override
    public void showError(int messageId) {
        showSnackbar(messageId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mainPresenter.showAndUploadPhoto();
        }
    }

    @Override
    public void showImage(Bitmap bitmap) {
        // TODO: 01/12/2016 Show Photo
    }

    @Override
    public void showNormalUI() {
        fingerPrint.setVisibility(View.INVISIBLE);
        camera.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFingerPrintSuccess() {
        AnimationUtils.exitReveal(fingerPrint);
        camera.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFingerPrintError() {
        fingerPrint.setBackgroundColor(ContextCompat.getColor(this, R.color.red_500));
    }

    @Override
    public void showAccess(List<Access> accesses) {
        accessAdapter.addAll(accesses);
    }

    public Context getContext() {
        return this;
    }

    private void showSnackbar(int messageId) {
        Snackbar.make(container, messageId, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

}
