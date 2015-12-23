package com.hardwork.fg607.jianfengchazhen.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.hardwork.fg607.jianfengchazhen.R;
import com.hardwork.fg607.jianfengchazhen.utils.DensityUtil;

public class FailedActivity extends Activity {

    private ImageView mLostImageView;
    private Button mRetryButton;
    private Button mExitButton;
    private Animation mScaleAimation;
    private TranslateAnimation mTranslateAnimation;
    private View mContentView;
    private int mLostImageSize;
    private int mScreenHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = View.inflate(this,R.layout.activity_failed,null);
        setContentView(mContentView);

        init();

        mLostImageView.startAnimation(mTranslateAnimation);
    }

    private void initTranslateAnimation() {
        mTranslateAnimation = new TranslateAnimation(0f,0f,-mLostImageSize,mScreenHeight/2-mLostImageSize-mLostImageSize/2);
        mTranslateAnimation.setFillAfter(true);
        mTranslateAnimation.setDuration(1300);
        mTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mRetryButton.setVisibility(View.VISIBLE);
                mRetryButton.startAnimation(mScaleAimation);
                mExitButton.setVisibility(View.VISIBLE);
                mExitButton.startAnimation(mScaleAimation);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initScaleAnimation() {

        mScaleAimation = new ScaleAnimation(0,1,0,1, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mScaleAimation.setDuration(500);
        mScaleAimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mRetryButton.setEnabled(false);
                mExitButton.setEnabled(false);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mRetryButton.setEnabled(true);
                mExitButton.setEnabled(true);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void initView() {

        mLostImageSize = DensityUtil.getScreenWidth(this)*2/5;
        mScreenHeight = DensityUtil.getScreenHeight(this);

        mLostImageView = (ImageView) findViewById(R.id.failed_imageview);
        mLostImageView.setMinimumHeight(mLostImageSize);
        mLostImageView.setMinimumWidth(mLostImageSize);
        mRetryButton = (Button) findViewById(R.id.retry_button);
        mExitButton = (Button) findViewById(R.id.exit_button);
        mRetryButton.setVisibility(View.INVISIBLE);
        mExitButton.setVisibility(View.INVISIBLE);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FailedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FailedActivity.this, StartActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void init() {

        initView();

        initScaleAnimation();

        initTranslateAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_failed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
