package com.hardwork.fg607.jianfengchazhen.activitys;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hardwork.fg607.jianfengchazhen.R;
import com.hardwork.fg607.jianfengchazhen.view.MyView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private MyView myView;
    private TextView mLevelTextView;
    private ImageView mWonImageView;
    private int mLevel = 1;
    private Handler mHandler = null;
    private SharedPreferences mSharedPreferences;
    private Runnable mFailedRunnable;
    private AlphaAnimation mAnimation;
    private AnimationDrawable mSuccessAnimation;
    private FrameLayout mFrameLayout;
    private boolean mIsRandomInverseRotate = false;
    private boolean mIsRuning = true;
    private Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        int level = mSharedPreferences.getInt("level",0);

        if(level != 0){
            mLevel = level;
        }

        startInverseRotateThread();
        playLevel(mLevel);

    }

    private void startInverseRotateThread() {

        final Random random = new Random(System.currentTimeMillis());

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mIsRuning){

                    if(mIsRandomInverseRotate){

                        boolean b = myView.getRotateClockwise();
                        myView.setRotateClockwise(!b);

                    }

                    int sleepTime = (random.nextInt(70) + 30)*100;

                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();
    }

    private void init() {

        mSharedPreferences = getSharedPreferences("save",MODE_PRIVATE);
        mVibrator = (Vibrator)getSystemService(Service.VIBRATOR_SERVICE);

        initView();

        initAnimation();

        initHandler();

        initRunnable();

    }

    private void initView() {
        myView = (MyView) findViewById(R.id.myview);
        mLevelTextView = (TextView) findViewById(R.id.level_textview);
        mWonImageView = (ImageView) findViewById(R.id.won_imageview);
        mWonImageView.setVisibility(View.INVISIBLE);
        mFrameLayout = (FrameLayout) findViewById(R.id.framelayout);

        myView.setOnConflictedListener(new MyView.OnConflictListener() {
            @Override
            public void failed() {
                onFailed();
            }

            @Override
            public void successed() {

                onSuccess();
            }
        });
    }

    private void onFailed() {
        vibrate();
        new Thread(mFailedRunnable).start();
    }

    private void vibrate() {

        if(mVibrator != null){
            mVibrator.vibrate(300);
        }

    }

    private void initRunnable() {
        mFailedRunnable =new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this, FailedActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }

    private void initHandler() {


        mHandler = new Handler(){

            public void handleMessage(Message msg){

                    boolean b = myView.getRotateClockwise();
                    myView.setRotateClockwise(!b);
            }
        };
    }

    private void initAnimation() {
        mAnimation = new AlphaAnimation(1,1);
        mAnimation.setDuration(1000);
        mAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mFrameLayout.setBackgroundColor(Color.WHITE);
                mWonImageView.setVisibility(View.INVISIBLE);
                playLevel(mLevel);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mWonImageView.setBackgroundResource(R.drawable.won_animation);
        mSuccessAnimation = (AnimationDrawable) mWonImageView.getBackground();
        mSuccessAnimation.start();
    }

    private void onSuccess() {

        int achievement = mSharedPreferences.getInt("achievement",0);
        if(mLevel > achievement){
            mSharedPreferences.edit().putInt("achievement",mLevel).commit();
        }

        mLevel++;
        mSharedPreferences.edit().putInt("level",mLevel).commit();
        mFrameLayout.setBackgroundColor(getResources().getColor(R.color.green));
        mWonImageView.setVisibility(View.VISIBLE);
        mWonImageView.startAnimation(mAnimation);




    }

    private void playLevel(int level){

        mLevelTextView.setText("Level " + level);

        switch (level) {
            case 1:
                runLevel1();
                break;
            case 2:
                runLevel2();
                break;
            case 3:
                runLevel3();
                break;
            case 4:
                runLevel4();
                break;
            case 5:
                runLevel5();
                break;
            case 6:
                runLevel6();
                break;
            case 7:
                runLevel7();
                break;
            case 8:
                runLevel8();
                break;
            case 9:
                runLevel9();
                break;
            case 10:
                runLevel10();
                break;
            case 11:
                runLevel11();
                break;
            case 12:
                runLevel12();
                break;
            case 13:
                runLevel13();
                break;
            case 14:
                runLevel14();
                break;
            case 15:
                runLevel15();
                break;
            case 16:
                runLevel16();
                break;
            case 17:
                runLevel17();
                break;
            case 18:
                runLevel18();
                break;
            case 19:
                runLevel19();
                break;
            case 20:
                runLevel20();
                break;
            default:
                break;
        }

    }

    private void runLevel1() {
        myView.setInitNailNumber(2);
        myView.setPassNailNumber(7);
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel2() {
        myView.setInitNailNumber(4);
        myView.setPassNailNumber(8);
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel3() {
        myView.setInitNailNumber(6);
        myView.setPassNailNumber(10);
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel4() {
        myView.setInitNailNumber(3);
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.setPassNailNumber(10);
        mIsRandomInverseRotate = true;
        myView.startRotate();
    }

    private void runLevel5() {
        myView.setInitNailNumber(8);
        myView.setPassNailNumber(10);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel6() {
        myView.setInitNailNumber(10);
        myView.setPassNailNumber(12);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel7() {
        myView.setInitNailNumber(6);
        myView.setPassNailNumber(18);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel8() {
        myView.setInitNailNumber(10);
        myView.setPassNailNumber(13);
        mIsRandomInverseRotate = false;
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel9() {
        myView.setInitNailNumber(4);
        myView.setPassNailNumber(10);
        mIsRandomInverseRotate = false;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel10() {
        myView.setInitNailNumber(8);
        myView.setPassNailNumber(13);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_NORMAL);
        myView.startRotate();
    }

    private void runLevel11() {
        myView.setInitNailNumber(10);
        myView.setPassNailNumber(13);
        myView.setRotateSpeed(MyView.SPEED_FAST);
        mIsRandomInverseRotate = true;
        myView.startRotate();
    }

    private void runLevel12() {
        myView.setInitNailNumber(7);
        myView.setPassNailNumber(15);
        mIsRandomInverseRotate = false;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel13() {
        myView.setInitNailNumber(6);
        myView.setPassNailNumber(18);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel14() {
        myView.setInitNailNumber(11);
        myView.setPassNailNumber(10);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel15() {
        myView.setInitNailNumber(13);
        myView.setPassNailNumber(10);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel16() {
        myView.setInitNailNumber(8);
        myView.setPassNailNumber(16);
        mIsRandomInverseRotate = false;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel17() {
        myView.setInitNailNumber(10);
        myView.setPassNailNumber(15);
        mIsRandomInverseRotate = false;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel18() {
        myView.setInitNailNumber(9);
        myView.setPassNailNumber(14);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel19() {
        myView.setInitNailNumber(11);
        myView.setPassNailNumber(13);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }

    private void runLevel20() {
        myView.setInitNailNumber(13);
        myView.setPassNailNumber(12);
        mIsRandomInverseRotate = true;
        myView.setRotateSpeed(MyView.SPEED_FAST);
        myView.startRotate();
    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
