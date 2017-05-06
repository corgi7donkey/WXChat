package com.bupt.wxchat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.wxchat.R;

import java.util.List;


/**
 * Created by sjb on 2017/5/6.
 */

public class ViewPagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHight;
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6f;
    private int mInitTranslationX;
    private int mTranslationX;

    private int mTabVisibleNum;
    private static final int COUNT_DEFAULT_TAB = 4;

    private List<String> mTitles;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);//获取自定义属性
        initPaint(); //初始化画笔
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffffff"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        mTabVisibleNum = a.getInt(R.styleable.ViewPagerIndicator_visible_tab_num, COUNT_DEFAULT_TAB);
        if (mTabVisibleNum < 0) {
            mTabVisibleNum = COUNT_DEFAULT_TAB;
        }
        a.recycle();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w / mTabVisibleNum * RADIO_TRIANGLE_WIDTH);
        mInitTranslationX = w / mTabVisibleNum / 2 - mTriangleWidth / 2;
        initTriangle();//初始化三角形Path
    }

    /**
     * 初始化三角形Path
     */
    private void initTriangle() {
        mTriangleHight = mTriangleWidth / 3;
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHight);
        mPath.close();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /**
     * ViewPager滑动的时候调用，重新绘制
     *
     * @param position
     * @param offset
     */
    public void scroll(int position, float offset) {
        int tabWidth = getWidth() / mTabVisibleNum;
        mTranslationX = (int) (tabWidth * offset + position * tabWidth);
        if (position >= mTabVisibleNum - 2 && offset > 0 && getChildCount() > mTabVisibleNum) {
            if (mTabVisibleNum == 1) {
                this.scrollTo(position * tabWidth + (int) offset * tabWidth, 0);
            } else {
                this.scrollTo((int) (offset * tabWidth) + (position - (mTabVisibleNum - 2)) * tabWidth, 0);
            }
        }
        invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int cCount = getChildCount();
        if (cCount == 0) return;
        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getWindowWidth() / mTabVisibleNum;
            view.setLayoutParams(lp);
        }
        setItemClickEvent();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getWindowWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 动态生成tab
     *
     * @param titles tab的标题集合
     */
    public void setTabItemTitles(List<String> titles) {
        if (titles != null) {
            this.removeAllViews();
            for (int i = 0; i < titles.size(); i++) {
                addView(generateTextView(titles.get(i)));
            }
        }
        setItemClickEvent();
    }

    /**
     * 动态生成TextView
     *
     * @param title
     * @return
     */
    private View generateTextView(String title) {
        TextView tv = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        lp.width = getWindowWidth() / mTabVisibleNum;
        tv.setText(title);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setTextColor(0xffffffff);
        tv.setLayoutParams(lp);
        return tv;
    }

    /**
     * 设置显示的tab数目
     *
     * @param num
     */
    public void setVisibleTabNum(int num) {
        mTabVisibleNum = num;
    }


    private ViewPager mViewPager;

    /**
     * 设置ViewPager
     *
     * @param viewPager
     * @param pos
     */
    public void setViewPager(ViewPager viewPager, int pos) {
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if (mPageOnChangedListener != null) {
                    mPageOnChangedListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (mPageOnChangedListener != null) {
                    mPageOnChangedListener.onPageSelected(position);
                }
                highLightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPageOnChangedListener != null) {
                    mPageOnChangedListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }

    /**
     * 页面滑动接口
     */
    public interface PageOnChangedListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);
    }

    private PageOnChangedListener mPageOnChangedListener;

    /**
     * 设置ViewPager页面监听器
     *
     * @param pageOnChangedListener
     */
    public void setPageOnChangedListener(PageOnChangedListener pageOnChangedListener) {
        mPageOnChangedListener = pageOnChangedListener;
    }

    public static final int COLOR_TEXT_NORMAL = 0x77ffffff;
    public static final int COLOR_TEXT_HIGHLIGHT = 0xffffffff;

    /**
     * 重置Text颜色
     */
    private void resetTextColor() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }

    /**
     * 高亮text
     *
     * @param pos
     */
    public void highLightTextView(int pos) {
        resetTextColor();
        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }


    /**
     * 设置textview的点击事件
     */
    private void setItemClickEvent() {
        for (int i = 0; i < getChildCount(); i++) {
            final int j = i;
            View view = getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

}
