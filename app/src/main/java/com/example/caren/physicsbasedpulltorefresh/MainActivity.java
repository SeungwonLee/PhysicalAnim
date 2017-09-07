package com.example.caren.physicsbasedpulltorefresh;

import android.os.Bundle;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import static android.support.animation.SpringForce.DAMPING_RATIO_HIGH_BOUNCY;
import static android.support.animation.SpringForce.STIFFNESS_HIGH;

public class MainActivity extends AppCompatActivity {

    private View minionsImg;
    private SeekBar stiffnessSeekBar;
    private SeekBar bounceSeekbar;

    private SpringAnimation xAnimation;
    private SpringAnimation yAnimation;

    private SpringForce xSpringForce;
    private SpringForce ySpringForce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minionsImg = findViewById(R.id.image);

        xAnimation = new SpringAnimation(minionsImg, SpringAnimation.X);
        yAnimation = new SpringAnimation(minionsImg, SpringAnimation.Y);

        minionsImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float dX = 0;
                float dY = 0;
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();

                        xAnimation.cancel();
                        yAnimation.cancel();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        minionsImg.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    case MotionEvent.ACTION_UP:
                        reset();

                        xAnimation.start();
                        yAnimation.start();
                        break;
                }
                return true;
            }

            private void reset() {
                float stiff = (STIFFNESS_HIGH * (stiffnessSeekBar.getProgress() / 2));
                float damping = (DAMPING_RATIO_HIGH_BOUNCY - (bounceSeekbar.getProgress() / 200f));

                xSpringForce = new SpringForce(0);
                xSpringForce.setStiffness(stiff == 0 ? SpringForce.STIFFNESS_VERY_LOW : stiff);
                xSpringForce.setDampingRatio(damping == 0 ? SpringForce.DAMPING_RATIO_LOW_BOUNCY : damping);
                xAnimation.setSpring(xSpringForce);

                ySpringForce = new SpringForce(0);
                ySpringForce.setStiffness(stiff == 0 ? SpringForce.STIFFNESS_VERY_LOW : stiff);
                ySpringForce.setDampingRatio(damping == 0 ? SpringForce.DAMPING_RATIO_LOW_BOUNCY : damping);
                yAnimation.setSpring(ySpringForce);
            }
        });

        stiffnessSeekBar = (SeekBar) findViewById(R.id.stiffnessValueAdjustment);
        bounceSeekbar = (SeekBar) findViewById(R.id.bounceValueAdjustment);
    }
}
