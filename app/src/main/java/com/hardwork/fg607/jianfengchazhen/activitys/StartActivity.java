package com.hardwork.fg607.jianfengchazhen.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;

import com.hardwork.fg607.jianfengchazhen.R;
import com.hardwork.fg607.jianfengchazhen.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity implements View.OnClickListener {

    private Button mStartButton;
    private Button mSelectButton;
    private Button mAchieveButton;
    private Button mClearButton;
    private PopupWindow mPopupWindow;
    private SharedPreferences mSharedPreferences;
    private ViewGroup mRootView;
    private View mContentView;
    private LinearLayout mRootLinearLayout;
    private LinearLayout mLineLinearLayout;
    private int mCount;
    private int mMaxLevel = 40;
    private int mScreenWidth;
    private int mContentHeight;
    private List<Button> mButtonList;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContentView = View.inflate(this, R.layout.activity_start, null);
        setContentView(mContentView);

        init();

        mHandler = new Handler(){

            public void handleMessage(Message message){

                if(mPopupWindow.isShowing()){
                    mPopupWindow.dismiss();
                }
            }
        };
    }

    private void init() {

        initView();
        mSharedPreferences = getSharedPreferences("save", MODE_PRIVATE);

        mButtonList = new ArrayList<>();
        mScreenWidth = DensityUtil.getScreenWidth(this);

    }

    private void initView() {

        mStartButton = (Button) findViewById(R.id.start_button);
        mSelectButton = (Button) findViewById(R.id.select_button);
        mAchieveButton = (Button) findViewById(R.id.achieve_button);
        mClearButton = (Button) findViewById(R.id.clear_button);

        mStartButton.setOnClickListener(this);
        mSelectButton.setOnClickListener(this);
        mAchieveButton.setOnClickListener(this);
        mClearButton.setOnClickListener(this);

        mRootLinearLayout = new LinearLayout(this);
        mRootLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootLinearLayout.setOrientation(LinearLayout.VERTICAL);

        mRootView = new ScrollView(this);
        mRootView.setBackgroundColor(getResources().getColor(R.color.blue));
        mRootView.addView(mRootLinearLayout);
    }

    public void start(){
        Intent intent = new Intent(StartActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void selectLevel(){

        if(mButtonList.size() == 0){
            createLevelButtons();
            layoutLevelButtons();
        }

        updateLevelButtons();

        if(mPopupWindow != null){
            mPopupWindow.showAtLocation(mContentView,ViewGroup.TEXT_ALIGNMENT_CENTER,0, 0);
        }else {
            mContentHeight = mContentView.getHeight();
            mPopupWindow = new PopupWindow(mRootView, mScreenWidth, mContentHeight);
            mPopupWindow.showAtLocation(mContentView, ViewGroup.TEXT_ALIGNMENT_CENTER, 0, 0);
            mPopupWindow.update();
        }

    }

    private void updateLevelButtons() {

        int achievement = mSharedPreferences.getInt("achievement", 0);
        Button button;

        for(int i = 0;i < mButtonList.size();i++){

            button = mButtonList.get(i);

            if((i+1) <= achievement + 1){
                button.setEnabled(true);
                button.setTextColor(Color.WHITE);
                button.setTextSize(23);
                if((i+1)<=9){
                    button.setText("level " + (i + 1));
                }else {
                    button.setText("level" + (i + 1));
                }
                button.setBackgroundResource(R.drawable.unlock);
            }else {

                button.setBackgroundResource(R.drawable.lock);
            }
        }
    }

    private void layoutLevelButtons(){
        for (Button button:mButtonList){

            addButtonToView(button);

        }
    }

    private void createLevelButtons() {

        for(int i = 0;i < mMaxLevel;i++){

            final int level = i + 1;

            Button button = new Button(this);

            button.setEnabled(false);
            button.setScaleX(0.8f);
            button.setScaleY(0.8f);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mSharedPreferences.edit().putInt("level",level).commit();
                    Intent intent = new Intent(StartActivity.this,MainActivity.class);
                    startActivity(intent);

                    //dismiss popupwindow
                    mHandler.sendEmptyMessageDelayed(0,1000);

                }
            });

            mButtonList.add(button);
        }
    }

    private void addButtonToView(Button button) {

        if(mCount % 3 == 0){
            mLineLinearLayout = new LinearLayout(this);
            mLineLinearLayout.setGravity(ViewGroup.TEXT_ALIGNMENT_CENTER);
            mLineLinearLayout.setBackgroundColor(getResources().getColor(R.color.blue));
            mLineLinearLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        mLineLinearLayout.addView(button);
        mCount++;

        if(mCount % 3 == 0 || mCount == mMaxLevel){
            mRootLinearLayout.addView(mLineLinearLayout);
        }
    }

    public void showAchievement(){

        int level = mSharedPreferences.getInt("achievement",0);
        String achievement = "level " + level;

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("最高成就");
        dialog.setIcon(R.drawable.achievement);
        if(level == 0){
            dialog.setMessage("还没有最高成就");
        }else {
            dialog.setMessage(achievement);
        }

        dialog.show();

    }
    public void clearRecord(){

        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("退出应用");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "不玩了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "按错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onBackPressed() {
        if(mPopupWindow != null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }else {
            super.onBackPressed();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.start_button:
                start();
                break;
            case R.id.select_button:
                selectLevel();
                break;
            case R.id.achieve_button:
                showAchievement();
                break;
            case R.id.clear_button:
                clearRecord();
                break;
        }

    }
}
