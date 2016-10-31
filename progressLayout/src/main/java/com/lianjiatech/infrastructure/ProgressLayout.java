package com.lianjiatech.infrastructure;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.List;

public class ProgressLayout extends RelativeLayout {

    private static final int defStyleAttr = R.attr.progressLayoutDefStyle;
    private static final int NOT_SET = -1;

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

    public enum LAYOUT_TYPE {

        /** 正在加载 */
        LOADING,
        /** 无内容 */
        NONE,
        /** 内容显示 */
        CONTENT,
        /** 网络错误 */
        NETWORK_ERROR,
        /** 加载失败 */
        FAILED
    }

    private LAYOUT_TYPE currentState = LAYOUT_TYPE.LOADING;

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

        this.layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.ProgressLayout, defStyleAttr,
                        R.style.DefaultSmartStyle);

        if (typedArray == null) {
            return;
        }

        try {
            this.loadingId =
                    typedArray.getResourceId(R.styleable.ProgressLayout_loading_layout, NOT_SET);
            this.noneId =
                    typedArray.getResourceId(R.styleable.ProgressLayout_none_content, NOT_SET);
            this.networkErrorId =
                    typedArray.getResourceId(R.styleable.ProgressLayout_network_content, NOT_SET);
            this.failedId =
                    typedArray.getResourceId(R.styleable.ProgressLayout_failed_content, NOT_SET);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        if (child.getTag() == null ||
                (!child.getTag().equals(LOADING_TAG) && !child.getTag().equals(NONE_TAG) &&
                        !child.getTag().equals(ERROR_TAG))) {

            this.contentViews.add(child);

            if (!this.isInEditMode()) {
                this.setContentVisibility(false);
            }
        }
    }

    public void showLoading() {

        ProgressLayout.this.showLoadingView();

        ProgressLayout.this.hideNoneView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.hideFailedView();
        ProgressLayout.this.setContentVisibility(false);

        this.currentState = LAYOUT_TYPE.LOADING;
    }

    public void showNone() {
        ProgressLayout.this.showNone(null);
    }

    public void showNone(OnClickListener retryListener) {

        ProgressLayout.this.showNoneView(retryListener);

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.hideFailedView();
        ProgressLayout.this.setContentVisibility(false);

        this.currentState = LAYOUT_TYPE.NONE;
    }

    public void showNetError() {
        ProgressLayout.this.showNetError(null);
    }

    public void showNetError(OnClickListener retryListener) {

        ProgressLayout.this.showNetErrorView(retryListener);

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNoneView();
        ProgressLayout.this.hideFailedView();
        ProgressLayout.this.setContentVisibility(false);

        this.currentState = LAYOUT_TYPE.NETWORK_ERROR;
    }

    public void showFailed() {
        ProgressLayout.this.showFailed(null);
    }

    public void showFailed(OnClickListener retryListener) {

        ProgressLayout.this.showFailedView(retryListener);

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNoneView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.setContentVisibility(false);

        this.currentState = LAYOUT_TYPE.FAILED;
    }

    public void showContent() {

        ProgressLayout.this.hideLoadingView();
        ProgressLayout.this.hideNoneView();
        ProgressLayout.this.hideNetErrorView();
        ProgressLayout.this.hideFailedView();

        ProgressLayout.this.setContentVisibility(true);

        this.currentState = LAYOUT_TYPE.CONTENT;
    }

    public LAYOUT_TYPE getCurrentState() {
        return currentState;
    }

    /** 显示正在加载界面 */
    private void showLoadingView() {

        if (this.loadingContainer == null) {

            if (loadingId == NOT_SET) {
                throw new IllegalStateException(
                        "cannot call showLoadingView() when loadingId was NO_SET which value is -1");
            }

            this.loadingContainer =
                    this.layoutInflater.inflate(loadingId, ProgressLayout.this, false);
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

            if (noneId == NOT_SET) {
                throw new IllegalStateException(
                        "cannot call showNoneView() when noneId was NO_SET which value is -1");
            }

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

            if (networkErrorId == NOT_SET) {
                throw new IllegalStateException(
                        "cannot call showNetErrorView() when networkErrorId was NO_SET which value is -1");
            }

            this.networkErrorContainer =
                    this.layoutInflater.inflate(networkErrorId, ProgressLayout.this, false);
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

            if (failedId == NOT_SET) {
                throw new IllegalStateException(
                        "cannot call showFailedView() when failedId was NO_SET which value is -1");
            }

            this.failedContainer =
                    this.layoutInflater.inflate(failedId, ProgressLayout.this, false);
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
    private void hideNoneView() {
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

    /** 隐藏数据错误界面 */
    private void hideFailedView() {
        if (failedContainer != null && failedContainer.getVisibility() != GONE) {
            this.failedContainer.setVisibility(GONE);
        }
    }

    public boolean isLoading() {
        return this.currentState == LAYOUT_TYPE.LOADING;
    }

    public boolean isContent() {
        return this.currentState == LAYOUT_TYPE.CONTENT;
    }

    public boolean isNone() {
        return this.currentState == LAYOUT_TYPE.NONE;
    }

    public boolean isNetworkError() {
        return this.currentState == LAYOUT_TYPE.NETWORK_ERROR;
    }

    public boolean isFailed() {
        return this.currentState == LAYOUT_TYPE.FAILED;
    }

    private void setContentVisibility(boolean visible) {
        for (View contentView : contentViews) {
            contentView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }
    
    public View getFailedContainer() {
        return failedContainer;
    }

    public View getLoadingContainer() {
        return loadingContainer;
    }

    public View getNetworkErrorContainer() {
        return networkErrorContainer;
    }

    public View getNoneContainer() {
        return noneContainer;
    }
}
