package com.hungdt.periodtracked.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.text.TextUtils;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;
import com.hungdt.periodtracked.R;


public class CustomWeekView extends WeekView {


    private int mRadius;

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();


    /**
     * 24节气画笔
     *24 thuật ngữ năng lượng mặt trời
     */
    //private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     * Dấu chấm nền
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     * Màu nền hôm nay
     */
    private Paint mCurrentDayPaint = new Paint();


    private Paint mNextDayPaint = new Paint();

    /**
     * 圆点半径
     * Bán kính chấm
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     * Tùy chỉnh nền tròn của logo Meizu
     */
    private Paint mSchemeBasicPaint = new Paint();

    private float mSchemeBaseLine;

    public CustomWeekView(Context context) {
        super(context);
        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        //mSolarTermTextPaint.setColor(0xff489dff);
        //mSolarTermTextPaint.setAntiAlias(true);
        //mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);


        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);

////////////
        mNextDayPaint.setAntiAlias(true);
        mNextDayPaint.setStyle(Paint.Style.STROKE);
        mNextDayPaint.setStrokeWidth(5);
        mNextDayPaint.setColor(Color.RED);
        float[] intervals = new float[]{20.0f, 20.0f};
        float phase = 0;
        DashPathEffect dashPathEffect =
                new DashPathEffect(intervals, phase);
        mNextDayPaint.setPathEffect(dashPathEffect);
////////////
        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

    }


    @Override
    protected void onPreviewHook() {
       // mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        int cx = x + mItemWidth / 2;
        int cy = mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }


    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {

        boolean isSelected = isSelected(calendar);
        if (isSelected) {
            mPointPaint.setColor(Color.WHITE);
        } else {
            mPointPaint.setColor(Color.GRAY);
        }

        canvas.drawCircle(x + mItemWidth / 2, mItemHeight - 3 * mPadding, mPointRadius, mPointPaint);
    }

    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;

        int cy = mItemHeight / 2;
        //int top = -mItemHeight / 6;
        int top = 0;

        if (calendar.isCurrentDay() && !isSelected) {
            canvas.drawCircle(cx, cy, mRadius, mCurrentDayPaint);
        }


        /*if (calendar.isCurrentDay()) {
            Calendar nextDay = new Calendar();
            nextDay.setDay(calendar.getDay() + 1);
            nextDay.setMonth(calendar.getMonth());
            nextDay.setYear(calendar.getYear());

            canvas.drawCircle(cx + mItemWidth, cy, mRadius, mNextDayPaint);
            canvas.drawCircle(cx + mItemWidth, cy, mRadius- 5, mCurrentDayPaint);
        }*/

        if(hasScheme){
            canvas.drawCircle(x + mItemWidth - mPadding - mCircleRadius / 2, mPadding + mCircleRadius, mCircleRadius, mSchemeBasicPaint);

            mTextPaint.setColor(calendar.getSchemeColor());

            canvas.drawText(calendar.getScheme(), x + mItemWidth - mPadding - mCircleRadius, mPadding + mSchemeBaseLine, mTextPaint);
        }

        if (calendar.isWeekend() && calendar.isCurrentMonth()) {
            mCurMonthTextPaint.setColor(0xFFffffff); //weekend
            //mCurMonthLunarTextPaint.setColor(0xFF489dff);
            //mSchemeTextPaint.setColor(0xFF489dff);
            //mSchemeLunarTextPaint.setColor(0xFF489dff);
            //mOtherMonthLunarTextPaint.setColor(0xFF489dff);
            //mOtherMonthTextPaint.setColor(0xFF489dff);
        } else {
            mCurMonthTextPaint.setColor(0xffffffff); //day normal
            //mCurMonthLunarTextPaint.setColor(0xffCFCFCF);
           // mSchemeTextPaint.setColor(0xff333333);
            //mSchemeLunarTextPaint.setColor(0xffCFCFCF);

            //mOtherMonthTextPaint.setColor(0xFFe1e1e1);
            //mOtherMonthLunarTextPaint.setColor(0xFFe1e1e1);
        }

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            /*canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10, mSelectedLunarTextPaint);*/
        } else if (hasScheme) {

            /*canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);*/

            /*canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                    !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint : mSchemeLunarTextPaint);*/
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);

            /*canvas.drawText(calendar.getLunar(), cx, mTextBaseLine + mItemHeight / 10,
                    calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                            !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint :
                                    calendar.isCurrentMonth() ?
                                            mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);*/
        }
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
