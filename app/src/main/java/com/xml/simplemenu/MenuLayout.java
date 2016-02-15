package com.xml.simplemenu;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

/**
 * Created by xulinggang on 16/2/2.
 */
public class MenuLayout extends RelativeLayout {
    private Context mContext;
    private View mRoot;
    private View mIconMain;
    private View mIconGu;
    private View mIconShare;
    private View mIconDownload;
    private View mIconSearch;


    public MenuLayout(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initView();
    }


    private void initView() {
        mRoot = inflate(mContext, R.layout.menu_layout, this);
        mIconMain = findViewById(R.id.icon_main);
        mIconMain.setOnClickListener(mClickListener);
        mIconGu = findViewById(R.id.icon_gu);
        mIconShare = findViewById(R.id.icon_share);
        mIconDownload = findViewById(R.id.icon_download);
        mIconSearch = findViewById(R.id.icon_search);
        initAnim();
    }

    private View.OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.icon_main:
                    if (isAnimating) {
                        return;
                    }
                    if (isReadToShow) {
                        mUpAnim.start();
                    } else {
                        mHideAnim.start();
                    }
                    mRotateAnim.start();
                    break;
            }
        }
    };

    private int mGap;
    private int mIconHeight;
    private int STEP_GU;
    private int STEP_SHARE;
    private int STEP_DOWN;
    private int STEP_SEARCH;

    private final int DURATION = 800;

    private ValueAnimator mUpAnim;
    private ValueAnimator mRotateAnim;
    private ValueAnimator mHideAnim;
    private boolean isAnimating;
    private boolean isReadToShow = true;

    private void initAnim() {
        mGap = dp2px(10);
        mIconHeight = dp2px(50);
        int height = mIconHeight * 4 + mGap * 4;
        STEP_GU = mIconHeight  + 1* mGap;
        STEP_SHARE = mIconHeight * 2 + 2 * mGap;
        STEP_DOWN = mIconHeight * 3 + 3 *mGap;
        STEP_SEARCH = mIconHeight* 4 +4*mGap;


        mUpAnim = ValueAnimator.ofFloat(0, - height);
        mUpAnim.setDuration(DURATION);
        mUpAnim.setInterpolator(new DecelerateInterpolator());
        mUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float valueOrigin = (float) animation.getAnimatedValue();
                float value = Math.abs(valueOrigin);

                if (value < STEP_GU) {
                    mIconGu.setTranslationY(valueOrigin);
                    mIconShare.setTranslationY(valueOrigin);
                    mIconDownload.setTranslationY(valueOrigin);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value >= STEP_GU && value < STEP_SHARE) {
                    mIconGu.setTranslationY(-STEP_GU);
                    mIconShare.setTranslationY(valueOrigin);
                    mIconDownload.setTranslationY(valueOrigin);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value >= STEP_SHARE && value < STEP_DOWN) {
                    mIconShare.setTranslationY(-STEP_SHARE);
                    mIconDownload.setTranslationY(valueOrigin);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value >= STEP_DOWN && value < STEP_SEARCH) {
                    mIconDownload.setTranslationY(-STEP_DOWN);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value >= STEP_SEARCH) {
                    mIconSearch.setTranslationY(-STEP_SEARCH);
                }
            }
        });

        mUpAnim.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isReadToShow = false;
            }
        });

        mRotateAnim = ObjectAnimator.ofFloat(mIconMain,"rotation",0,360);
        mRotateAnim.setDuration(DURATION);
        mRotateAnim.setInterpolator(new AnticipateOvershootInterpolator());

        mHideAnim = ValueAnimator.ofFloat(- height,0);
        mHideAnim.setDuration(DURATION);
        mHideAnim.setInterpolator(new DecelerateInterpolator());
        mHideAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float valueOrigin = (float) animation.getAnimatedValue();
                float value = Math.abs(valueOrigin);

                if (value <STEP_SEARCH &&value >= STEP_DOWN  ) {
                    mIconGu.setTranslationY(-STEP_GU);
                    mIconShare.setTranslationY(-STEP_SHARE);
                    mIconDownload.setTranslationY(-STEP_DOWN);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value <STEP_DOWN &&value >= STEP_SHARE) {
                    mIconGu.setTranslationY(-STEP_GU);
                    mIconShare.setTranslationY(-STEP_SHARE);
                    mIconDownload.setTranslationY(valueOrigin);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value <STEP_SHARE &&value >= STEP_GU) {
                    mIconGu.setTranslationY(-STEP_GU);
                    mIconShare.setTranslationY(valueOrigin);
                    mIconDownload.setTranslationY(valueOrigin);
                    mIconSearch.setTranslationY(valueOrigin);
                } else if (value < STEP_GU) {
                    mIconGu.setTranslationY(valueOrigin);
                    mIconShare.setTranslationY(valueOrigin);
                    mIconDownload.setTranslationY(valueOrigin);
                    mIconSearch.setTranslationY(valueOrigin);
                }
            }
        });

        mHideAnim.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
                isReadToShow = true;
            }
        });

    }


    private int dp2px(int dipValue) {
        return (int) (mContext.getResources().getDisplayMetrics().density * dipValue);
    }



}
