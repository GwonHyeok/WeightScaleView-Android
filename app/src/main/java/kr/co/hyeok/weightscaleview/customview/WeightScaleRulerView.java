package kr.co.hyeok.weightscaleview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

import kr.co.hyeok.weightscaleview.R;

/**
 * 체중계 줄자 커스텀 뷰
 * Created by GwonHyeok on 2016. 4. 18..
 */
public class WeightScaleRulerView extends View {
    private final String TAG = getClass().getSimpleName();

    private Paint mLongDividerPaint, mShortDividerPaint;
    private int mLongDividerHeight = 60;
    private int mShortDividerHeight = 30;

    private Paint mWeightTextPaint;
    private Rect textBounds = new Rect();
    private int mTextMarginTop = 0;

    // 체중 줄자 최대치
    private int mMaxWeight = 200;

    // 현재 체중
    private float mWeight = 0;

    // 5KG 간 간격 Pixel
    private int mDividerSpace = 100;

    // 체중 별 좌표 위치
    private HashMap<Integer, Float> mPositionMap = new HashMap<>();

    public WeightScaleRulerView(Context context) {
        this(context, null);
    }

    public WeightScaleRulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeightScaleRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.WeightScaleRulerView, defStyle, 0);
        int shortColor = a.getColor(R.styleable.WeightScaleRulerView_shortDividerColor, Color.BLACK);
        mShortDividerHeight = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_shortDividerHeight, mShortDividerHeight);
        int shortDividerWidth = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_shortDividerWidth, 5);

        int longColor = a.getColor(R.styleable.WeightScaleRulerView_longDividerColor, Color.BLACK);
        mLongDividerHeight = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_longDividerHeight, mLongDividerHeight);
        int longDividerWidth = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_longDividerWidth, 5);

        mDividerSpace = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_dividerSpace, mDividerSpace);

        int textSize = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_weightTextSize, 24);
        int textColor = a.getColor(R.styleable.WeightScaleRulerView_weightTextColor, Color.BLACK);

        mTextMarginTop = a.getDimensionPixelSize(R.styleable.WeightScaleRulerView_weightTextMarginTop, mTextMarginTop);

        a.recycle();

        // 체중계 구분선
        mLongDividerPaint = new Paint();
        mLongDividerPaint.setColor(longColor);
        mLongDividerPaint.setStrokeWidth(longDividerWidth);

        mShortDividerPaint = new Paint();
        mShortDividerPaint.setColor(shortColor);
        mShortDividerPaint.setStrokeWidth(shortDividerWidth);

        // 체중계 텍스트
        mWeightTextPaint = new Paint();
        mWeightTextPaint.setColor(textColor);
        mWeightTextPaint.setAntiAlias(true);
        mWeightTextPaint.setTextSize(textSize);


        initPositionMap();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Integer integer : mPositionMap.keySet()) {
            float xPosition = mPositionMap.get(integer);
            if (integer % 10 == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mLongDividerHeight, mLongDividerPaint);

                String weight = String.valueOf(integer);
                mWeightTextPaint.getTextBounds(weight, 0, weight.length(), textBounds);
                canvas.drawText(weight, xPosition - textBounds.width() / 2, mTextMarginTop + textBounds.height() + mLongDividerHeight, mWeightTextPaint);
            } else if (integer % 5 == 0) {
                canvas.drawLine(xPosition, 0, xPosition, mShortDividerHeight, mShortDividerPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWeightTextPaint.getTextBounds("0", 0, 1, textBounds);

        setMeasuredDimension(
                getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                mLongDividerHeight + mTextMarginTop + (textBounds.height() > 0 ? textBounds.height() : 140)
        );
        initPositionMap();
    }

    /**
     * 최대 체중 값 설정
     *
     * @param maxWeight 최대 체중 값
     */
    public void setMaxWeight(int maxWeight) {
        this.mMaxWeight = maxWeight;
    }

    /**
     * 최대 체중 값 반환
     *
     * @return 최대 체중 값
     */
    public int getMaxWeight() {
        return this.mMaxWeight;
    }

    /**
     * 현재 체중계에 보여질 체중 설정 후
     * 뷰 새로고침
     *
     * @param weight 현재 체중
     */
    public void setWeight(float weight) {
        this.mWeight = weight;
        writePositionMap();
        invalidate();
    }

    /**
     * 현재 체중계의 체중
     *
     * @return 현재 체중
     */
    public float getWeight() {
        return this.mWeight;
    }

    private void initPositionMap() {
        float startX = (float) getMeasuredWidth() / 2;
        for (int i = 0; i <= mMaxWeight; i += 5) {
            mPositionMap.put(i, startX);
            startX += mDividerSpace;
        }
    }

    private void writePositionMap() {
        float startX = (getMeasuredWidth() / 2 - (mDividerSpace * mWeight / 5));

        for (int i = 0; i <= mMaxWeight; i += 5) {
            mPositionMap.put(i, startX);
            startX += mDividerSpace;
        }
    }
}