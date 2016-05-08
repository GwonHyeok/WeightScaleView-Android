package kr.co.hyeok.weightscaleview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import kr.co.hyeok.weightscaleview.customview.WeightScaleRulerView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextView;
    private WeightScaleRulerView mScaleRulerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.weight_textview);
        mScaleRulerView = (WeightScaleRulerView) findViewById(R.id.scaleView);

        SeekBar seekBar = (SeekBar) findViewById(R.id.spinner);
        if (seekBar != null) {
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mScaleRulerView.setWeight(progress);
                    mTextView.setText(String.format("%.2f", (float) progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    public void onCLickAnimate(View view) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 94.32f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextView.setText(String.format("%.2f", animation.getAnimatedValue()));
                mScaleRulerView.setWeight((Float) animation.getAnimatedValue());
            }
        });
        animator.setDuration(2000);
        animator.setInterpolator(new OvershootInterpolator());
        animator.start();
    }
}
