package com.smartdengg.mylibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class ProgressLayout extends RelativeLayout {

    private static final int defStyleAttr = 0;

    private static final String LOADING_TAG = "ProgressLayout.loading_tag";
    private static final String NONE_TAG = "ProgressLayout.none_tag";
    private static final String ERROR_TAG = "ProgressLayout.error_tag";

    private LayoutInflater layoutInflater;

    /*Some ViewGroup*/
    private View loadingContainer;
    private View noneContainer;
    private View networkErrorContainer;
    private View failedContainer;

    /*Some Id*/
    private int loadingId;
    private int noneId;
    private int failedId;
    private int networkErrorId;

    private List<View> contentViews = new ArrayList<>();

    public static final int LOADING = 1;/*正在加载*/
    public static final int NONE = 2;/*无内容*/
    public static final int CONTENT = 3;/*内容显示*/
    public static final int NETWORK_ERROR = 4;/*网络错误*/
    public static final int FAILED = 5;/*加载失败*/

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(value = { LOADING, NONE, CONTENT, NETWORK_ERROR, FAILED })
    public @interface LAYOUT_TYPE {}

    @LAYOUT_TYPE private int currentState = LOADING;

    public ProgressLayout(Context context) {
        this(context, null);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, defStyleAttr);
    }

    public ProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!this.isInEditMode()) {
            ProgressLayout.this.init(context, attrs, defStyleAttr);
        }
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressLayout, defStyleAttr, R.style.DefaultSmartStyle);

        if (typedArray == null) return;

        try {
            this.loadingId = typedArray.getResourceId(R.styleable.ProgressLayout_loading_layout, -1);
            this.noneId = typedArray.getResourceId(R.styleable.ProgressLayout_none_content, -1);
            this.networkErrorId = typedArray.getResourceId(R.styleable.ProgressLayout_network_content, -1);
            this.failedId = typedArray.getResourceId(R.styleable.ProgressLayout_failed_content, -1);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null ||
                (!child.getTag().equals(LOADING_TAG) && !child.getTag().equals(NONE_TAG) && !child.getTag().equals(ERROR_TAG))) {

            this.contentViews.add(child);
            this.setContentVisibility(false);
        }
    }

    public void showLoading() {

        ProgressLayout.this.showLoadingView();

        ProgressLayout.this.hideNoContentView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.hideFailedView();
        ProgressLayout.this.setContentVisibility(false);
    }

    public void showNone() {
        ProgressLayout.this.showNone(null);
    }

    public void showNone(@Nullable OnClickListener retryListener) {

        ProgressLayout.this.showNoneView(retryListener);

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.hideFailedView();
        ProgressLayout.this.setContentVisibility(false);
    }

    public void showNetError() {
        ProgressLayout.this.showNetError(null);
    }

    public void showNetError(@Nullable OnClickListener retryListener) {

        ProgressLayout.this.showNetErrorView(retryListener);

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNoContentView();
        ProgressLayout.this.hideFailedView();
        ProgressLayout.this.setContentVisibility(false);
    }

    public void showFailed() {
        ProgressLayout.this.showFailed(null);
    }

    public void showFailed(@Nullable OnClickListener retryListener) {

        ProgressLayout.this.showFailedView(retryListener);

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNoContentView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.setContentVisibility(false);
    }

    public void showContent() {

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNoContentView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.hideFailedView();

        ProgressLayout.this.setContentVisibility(true);
    }

    @LAYOUT_TYPE
    public int getCurrentState() {
        return currentState;
    }

    /** 显示正在加载界面 */
    private void showLoadingView() {

        if (this.loadingContainer == null) {

            this.loadingContainer = this.layoutInflater.inflate(loadingId, ProgressLayout.this, false);
            this.loadingContainer.setTag(LOADING_TAG);

            LayoutParams layoutParams = (LayoutParams) loadingContainer.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            ProgressLayout.this.addView(loadingContainer, layoutParams);
        } else {
            this.loadingContainer.setVisibility(VISIBLE);
        }
    }

    /**
     * 显示无内容界面
     *
     * @param retryListener
     *         点击事件回调
     */
    private void showNoneView(OnClickListener retryListener) {

        if (this.noneContainer == null) {

            this.noneContainer = this.layoutInflater.inflate(noneId, ProgressLayout.this, false);
            this.noneContainer.setTag(NONE_TAG);

            LayoutParams layoutParams = (LayoutParams) noneContainer.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            ProgressLayout.this.addView(noneContainer, layoutParams);

            if (retryListener != null) {
                this.noneContainer.setClickable(true);
                this.noneContainer.setOnClickListener(retryListener);
            }
        } else {
            this.noneContainer.setVisibility(VISIBLE);
        }
    }

    /**
     * 显示网络错误界面
     *
     * @param retryListener
     *         点击事件回调
     */
    private void showNetErrorView(OnClickListener retryListener) {

        if (this.networkErrorContainer == null) {

            this.networkErrorContainer = this.layoutInflater.inflate(networkErrorId, ProgressLayout.this, false);
            this.networkErrorContainer.setTag(ERROR_TAG);

            LayoutParams layoutParams = (LayoutParams) networkErrorContainer.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            ProgressLayout.this.addView(networkErrorContainer, layoutParams);

            if (retryListener != null) {
                this.networkErrorContainer.setClickable(true);
                this.networkErrorContainer.setOnClickListener(retryListener);
            }
        } else {
            this.networkErrorContainer.setVisibility(VISIBLE);
        }
    }

    /**
     * 显示加载失败界面
     *
     * @param retryListener
     *         点击事件回调
     */
    private void showFailedView(OnClickListener retryListener) {

        if (this.failedContainer == null) {

            this.failedContainer = this.layoutInflater.inflate(failedId, ProgressLayout.this, false);
            this.failedContainer.setTag(ERROR_TAG);

            LayoutParams layoutParams = (LayoutParams) failedContainer.getLayoutParams();
            layoutParams.addRule(CENTER_IN_PARENT);

            ProgressLayout.this.addView(failedContainer, layoutParams);

            if (retryListener != null) {
                this.failedContainer.setClickable(true);
                this.failedContainer.setOnClickListener(retryListener);
            }
        } else {
            this.failedContainer.setVisibility(VISIBLE);
        }
    }

    /** 隐藏正在加载界面 */
    private void hideLoadingView() {
        if (loadingContainer != null && loadingContainer.getVisibility() != GONE) {
            this.loadingContainer.setVisibility(GONE);
        }
    }

    /** 隐藏无内容界面 */
    private void hideNoContentView() {
        if (noneContainer != null && noneContainer.getVisibility() != GONE) {
            this.noneContainer.setVisibility(GONE);
        }
    }

    /** 隐藏网络错误界面 */
    private void hideNetErrorView() {
        if (networkErrorContainer != null && networkErrorContainer.getVisibility() != GONE) {
            this.networkErrorContainer.setVisibility(GONE);
        }
    }

    /** 隐藏网络错误界面 */
    private void hideFailedView() {
        if (failedContainer != null && failedContainer.getVisibility() != GONE) {
            this.failedContainer.setVisibility(GONE);
        }
    }

    public boolean isLoading() {
        return this.currentState == LOADING;
    }

    public boolean isnOContent() {
        return this.currentState == NONE;
    }

    public boolean isContent() {
        return this.currentState == CONTENT;
    }

    public boolean isFailed() {
        return this.currentState == FAILED;
    }

    public boolean isNetworkError() {
        return this.currentState == NETWORK_ERROR;
    }

    private void setContentVisibility(boolean visible) {
        for (View contentView : contentViews) {
            contentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
}