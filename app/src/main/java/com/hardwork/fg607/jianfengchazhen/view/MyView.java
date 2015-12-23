package com.hardwork.fg607.jianfengchazhen.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hardwork.fg607.jianfengchazhen.R;
import com.hardwork.fg607.jianfengchazhen.utils.BitmapUtil;
import com.hardwork.fg607.jianfengchazhen.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fg607 on 15-12-11.
 */
public class MyView extends View implements Runnable,View.OnTouchListener{

    public static final int MAX_INIT_NAILS = 15;
    public static final int MAX_NAILS = 25;
    public static final int MIN_PASS_NAILS = 3;
    private List<Float> mNailsRotateList;
    private boolean mIsRotate = false;
    private Paint mPaint = new Paint();
    private Context mContext;
    private  Matrix mMatrix = new Matrix();;
    private float mViewSize;
    private boolean mIsConflict = false;
    private int mNailWidth  = 16;//dip
    private int mNailHeight = 100;//dip
    private int mCircleRadius = 30;//dip
    private int mNailNumerTextSize = 19;//dip
    private int mFailedTextSize = 21;//dip
    private int mPassTextSize = 13;
    private int mConflictCount = 0;
    private boolean mIsRuning = false;
    private float mMinAngle = 1.5f;
    private int mSleepTime;
    private boolean mIsAddNewNail = false;
    private boolean mIsDrawing = false;
    private Bitmap mNailBitmap;
    private Bitmap mConflicNailBitmap;
    private boolean mIsRotateClockwise;
    private OnConflictListener mConflictedListener = null;
    private int mInitNailNumber = 0;
    private int mPassNailNumber = MIN_PASS_NAILS;
    private int mPassNailCount = 0;

    public static final int SPEED_NORMAL = 25;
    public static final int SPEED_FAST = 15;
    public static final int SPEED_SLOWLY = 30;



    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();

        //用线程来刷新界面
        Thread thread=new Thread(this);
        thread.start();
    }

    private void init(){

        //dip转化为px
        mNailWidth = DensityUtil.dip2px(mContext, mNailWidth);
        mNailHeight = DensityUtil.dip2px(mContext, mNailHeight);
        mCircleRadius = DensityUtil.dip2px(mContext,mCircleRadius);
        mNailNumerTextSize = DensityUtil.dip2px(mContext,mNailNumerTextSize);
        mFailedTextSize = DensityUtil.dip2px(mContext,mFailedTextSize);
        mPassTextSize = DensityUtil.dip2px(mContext,mPassTextSize);

        mIsRuning = true;
        mIsRotate = false;

        mSleepTime = SPEED_NORMAL;
        mIsRotateClockwise = true;
        mNailsRotateList = new ArrayList<>();
        mViewSize =  mContext.getResources().getDisplayMetrics().widthPixels;
        mPaint.setTextAlign(Paint.Align.CENTER);

        mNailBitmap = BitmapUtil.readBitmapById(mContext, R.drawable.nail);
        mNailBitmap = BitmapUtil.scaleImage(mNailBitmap, mNailWidth, mNailHeight);
        mConflicNailBitmap = BitmapUtil.readBitmapById(mContext, R.drawable.conflict_nail);
        mConflicNailBitmap = BitmapUtil.scaleImage(mConflicNailBitmap, mNailWidth, mNailHeight);


        setOnTouchListener(this);
    }

    private void initNails() {

        if(mInitNailNumber <= 0){
            return;
        }else if(mInitNailNumber > MAX_INIT_NAILS){
            mInitNailNumber = MAX_INIT_NAILS;
        }

        float degree = (float)(360 / mInitNailNumber);

        for(int i = 0;i < mInitNailNumber;i++){

            mNailsRotateList.add(i*degree);
        }

    }

    private void addNail(){

        mNailsRotateList.add(0f);

    }

    private void clickScreen(){

        if(isRotated()){
            mIsAddNewNail = true;
        }


    }

    public interface OnConflictListener {

        public void failed();
        public void successed();
    }
 /* @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

           setMeasuredDimension(mViewSize, mViewSize);
    }*/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        prepareDraw();

        drawing(canvas);

        afterDraw();


    }

    private void drawing(Canvas canvas) {

        mPaint.setColor(Color.rgb(0, 0, 0));

        //画中心圆
        canvas.drawCircle(mViewSize / 2, mViewSize / 2, mCircleRadius, mPaint);

        drawNails(canvas);

        drawNailsNumber(canvas);

        drawPassNails(canvas);

       if(mIsConflict) {
           drawConflictNail(canvas);
        }




    }

    private void drawPassNails(Canvas canvas) {

        if(mIsAddNewNail){
            mPassNailCount++;
        }
        int lastNumber = mPassNailNumber - mPassNailCount;

        if(lastNumber >= 1){
            drawPassNail(canvas,0,lastNumber);
        }

        if(lastNumber - 1 >= 1){
            drawPassNail(canvas,mNailWidth*2,lastNumber -1);
        }

        if(lastNumber - 1 - 1 >= 1){
            drawPassNail(canvas, mNailWidth * 4, lastNumber - 1 - 1);
        }

    }

    private void drawPassNail(Canvas canvas,float diffHeight,int lastNumber){

        mPaint.setColor(Color.rgb(0, 0, 0));
        canvas.drawCircle(mViewSize / 2, mViewSize / 2 + mNailHeight + mCircleRadius / 2 + mNailWidth + diffHeight, mNailWidth / 2, mPaint);

        mPaint.setColor(Color.rgb(255, 255, 255));
        mPaint.setTextSize(mPassTextSize);
        canvas.drawText("" + lastNumber, mViewSize / 2, mViewSize / 2 + mNailHeight + mCircleRadius / 2 + mNailWidth + mPaint.getTextSize() / 3 + diffHeight, mPaint);
    }
    private void drawConflictNail(Canvas canvas) {

        mMatrix.setTranslate(mViewSize / 2 - mConflicNailBitmap.getWidth() / 2, mViewSize / 2);
        mMatrix.postRotate(0, mViewSize / 2, mViewSize / 2);

        canvas.drawBitmap(mConflicNailBitmap, mMatrix, null);
    }

    private void prepareDraw() {

        if(mIsAddNewNail){
            addNail();
        }
    }

    private void afterDraw() {

        if(mIsAddNewNail){
            mIsAddNewNail = false;
        }

        if(mIsConflict){

            stopRotate();
            mIsConflict = false;

        }else if(mPassNailCount == mPassNailNumber){
            stopRotate();
            mConflictedListener.successed();
        }

        mIsDrawing = false;
    }

    private void updateNails(){

        //遍历HashMap改变角度

        int size = mNailsRotateList.size();
        float rotate;

        for(int i = 0;i < size;i++){

            rotate = mNailsRotateList.get(i);

            if(mIsRotateClockwise){


                rotate += mMinAngle;

                if (rotate >= 360) {
                    rotate -= 360;
                }
            }else {

                rotate -= mMinAngle;

                if (rotate <= 0) {
                    rotate += 360;
                }
            }


            mNailsRotateList.set(i,rotate);
        }

    }

    private void drawNailsNumber(Canvas canvas) {
        mPaint.setColor(Color.rgb(255, 255, 255));
        mPaint.setTextSize(mNailNumerTextSize);
        canvas.drawText("" + mNailsRotateList.size(), mViewSize / 2, mViewSize / 2 + mPaint.getTextSize()/2, mPaint);
    }

    private void drawNails(Canvas canvas) {

        //清空碰撞检测标志
        mConflictCount = 0;

        for(Float rotate:mNailsRotateList){

            if(mIsAddNewNail){
                checkConflict(rotate);
            }

            mMatrix.setTranslate(mViewSize / 2 - mNailBitmap.getWidth() / 2, mViewSize / 2);
            mMatrix.postRotate(rotate, mViewSize / 2, mViewSize / 2);

            canvas.drawBitmap(mNailBitmap, mMatrix, null);

        }
    }


    public void startRotate(){

        mNailsRotateList.clear();
        initNails();
        this.mIsRotate=true;

    }

    public void pauseRotate(){
        this.mIsRotate= false;
    }

    public void resumeRotate(){
        this.mIsRotate= true;
    }

    public void stopRotate(){

        this.mIsRotate=false;
        mPassNailCount = 0;

    }

    public boolean isRotated(){

        return mIsRotate;
    }

    @Override
    public void run() {

        while(mIsRuning){

            if(mIsRotate && !mIsDrawing){

                updateNails();
                //刷新View
                this.postInvalidate();
                mIsDrawing = true;
            }

            try {
                Thread.sleep(mSleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void checkConflict(float rotate) {

        //根据小球的宽度和高度计算出角度在10-350之间的nail会与新添加nail产生碰撞（包含新添加nail,角度为0）
        if (rotate <= 10 || rotate >= 350) {

            mConflictCount++;

            if (mConflictCount >= 2 ) {

                mIsConflict = true;

                if(mConflictedListener != null){
                    mConflictedListener.failed();
                }

            }
        }
    }

    public void setRotateSpeed(int speed){

        if(speed <= SPEED_FAST){
            mSleepTime = SPEED_FAST;
        }else if(speed <= SPEED_NORMAL){
            mSleepTime = SPEED_NORMAL;
        }else{
            mSleepTime = SPEED_SLOWLY;
        }

    }

    public void setRotateClockwise(boolean isRotateClockwise){
        mIsRotateClockwise = isRotateClockwise;
    }

    public boolean getRotateClockwise(){
        return mIsRotateClockwise;
    }

    public void setPassNailNumber(int passNumber){

        int maxPassNailNumber = MAX_NAILS - mInitNailNumber;
        if(passNumber > maxPassNailNumber){
            mPassNailNumber = maxPassNailNumber;
        }else if(passNumber < MIN_PASS_NAILS){
            mPassNailNumber = MIN_PASS_NAILS;
        }else {
            mPassNailNumber = passNumber;
        }
    }

    public void setOnConflictedListener(OnConflictListener listener){
        mConflictedListener = listener;
    }

    public void setInitNailNumber(int number){
        mInitNailNumber = number;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){

            clickScreen();
        }
        return false;
    }

    @Override
    public void setVisibility(int visibility) {

        if(visibility == VISIBLE){
            mIsRuning = true;
        }else {

            mIsRuning = false;
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void finalize() throws Throwable {
        mIsRuning = false;
        super.finalize();

    }
}
