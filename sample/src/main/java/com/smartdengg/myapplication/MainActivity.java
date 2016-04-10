package com.smartdengg.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.smartdengg.mylibrary.ProgressLayout;

public class MainActivity extends AppCompatActivity {

    @NonNull
    @Bind(R.id.progress_container)
    protected ProgressLayout progressLayout;

    private View.OnClickListener retryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.this.progressLayout.showLoading();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);

        this.progressLayout.showLoading();
    }

    @NonNull
    @OnClick(R.id.loading_btn)
    protected void onLoadingClick() {
        this.progressLayout.showLoading();
    }

    @NonNull
    @OnClick(R.id.content_btn)
    protected void onContentClick() {
        this.progressLayout.showContent();
    }

    @NonNull
    @OnClick(R.id.none_btn)
    protected void onNoneClick() {
        this.progressLayout.showNone(retryListener);
    }

    @NonNull
    @OnClick(R.id.net_error_btn)
    protected void onNetErrorClick() {
        this.progressLayout.showNetError(retryListener);
    }

    @NonNull
    @OnClick(R.id.failed_btn)
    protected void onFailedClick() {
        this.progressLayout.showFailed(retryListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(MainActivity.this);
    }
}
