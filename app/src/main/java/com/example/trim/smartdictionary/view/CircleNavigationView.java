package com.example.trim.smartdictionary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.trim.smartdictionary.R;
import com.example.trim.smartdictionary.utils.LogUtiles;

/**
 * 自定义一个圆形导航控件，共有上下左右中五个区域，每个区域由一个icon和一个text组成。
 * Created by cclin on 2016/10/6.
 */

public class CircleNavigationView extends View {

    private final static int CENTER = 0;
    private final static int UP = 1;
    private final static int DOWN = 2;
    private final static int LEFT = 3;
    private final static int RIGHT = 4;
    private final static int UNSPECIFIED = -1;
    // 整个盘块的范围
    private RectF mRectF;
    // 整个盘块的直径 单位为px
    private float mRadius = 0;
    // 绘制盘块的画笔
    private Paint mArcPaint;
    // 绘制直线的画笔
    private Paint mLinePaint;
    // 绘制文本的画笔
    private Paint mTextPaint;
    // 文本高度
    private float textHeight;
    // 绘制中间圆形的画笔
    private Paint mCenterPaint;

    private Bitmap mCenterBitmap;
    // 定义一个画布
    private Canvas mCanvas;

    private int mWidthOnWindow;
    private int mHeightOnWindow;

    private int mWidth = 900; // px
    private int mHeight = 900; // px
    // 圆形导航监听接口
    private CircleNavigationOnClickListener mOnClickListener;

    private int textSize = 15; // 文本字体大小
    private float lineWidth = 1.0f; // 线宽
    private String upText; // 区域文本
    private String downText;
    private String leftText;
    private String rightText;

    public float getmRadius() {
        return mRadius;
    }

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public String getUpText() {
        return upText;
    }

    public void setUpText(String upText) {
        this.upText = upText;
    }

    public String getDownText() {
        return downText;
    }

    public void setDownText(String downText) {
        this.downText = downText;
    }

    public String getLeftText() {
        return leftText;
    }

    public void setLeftText(String leftText) {
        this.leftText = leftText;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public void setOnClickListener(CircleNavigationOnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    public CircleNavigationView(Context context) {
        this(context, null);
    }

    public CircleNavigationView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleNavigationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        int count = attrs.getAttributeCount(); // 获取属性个数
        LogUtiles.d("count = "+count);
        for (int i=0; i<count; i++){
            String name = attrs.getAttributeName(i);
            String value = attrs.getAttributeValue(i);
            LogUtiles.d("name: "+name+";value: "+value+";");
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleNavigationView);
        int taCount = typedArray.getIndexCount(); // 数组实际可用长度
        for (int i=0; i<taCount; i++){
            int itemId = typedArray.getIndex(i);
            LogUtiles.d("itemId = "+itemId);

            switch (itemId){
                case R.styleable.CircleNavigationView_radius:
                    float radio = typedArray.getDimension(itemId, 0); // 从图纸中获取零件半径的大小
//                    mRadius = radio; // px
                    LogUtiles.d("dp radio = "+mRadius);
                    break;

                case R.styleable.CircleNavigationView_items:
                    int items = typedArray.getInt(itemId, 0);
                    LogUtiles.d("items = "+items);
                    break;

                case R.styleable.CircleNavigationView_angle: // 获取角度
                    float angle = typedArray.getFloat(itemId, 0);
                    LogUtiles.d("angle = "+angle);
                    break;

                case R.styleable.CircleNavigationView_text_size: // 获取文本大小
                    textSize = typedArray.getDimensionPixelSize(itemId, textSize);
                    LogUtiles.d("textSize = "+textSize);
                    break;

                case R.styleable.CircleNavigationView_line_width: // 获取线宽
                    lineWidth = typedArray.getFloat(itemId, lineWidth);
                    LogUtiles.d("lineWidth = "+lineWidth);
                    break;
            }
        }
        upText = typedArray.getString(R.styleable.CircleNavigationView_up_text);
        downText = typedArray.getString(R.styleable.CircleNavigationView_down_text);
        leftText = typedArray.getString(R.styleable.CircleNavigationView_left_text);
        rightText = typedArray.getString(R.styleable.CircleNavigationView_right_text);
        LogUtiles.d(upText+";"+downText+";"+leftText+";"+rightText+";");
        typedArray.recycle(); // 回收资源

        init();// 初始化资源
    }

    /**
     * 定义一个初始化方法
     */
    private void init(){
        setBackgroundColor(Color.TRANSPARENT); // 设置背景色为红色

        // 1、设置绘制圆盘的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mArcPaint.setDither(true);
//        mArcPaint.setColor(Color.argb(0xdf, 0x3f, 0x51, 0xb5)); //102b6a 2a5caa 3F51B5 33a3dc
        mArcPaint.setColor(Color.argb(0xff, 0x4e, 0x72, 0xb8));

        // 2、设置绘制白线的画笔
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mLinePaint.setColor(Color.WHITE); // 设置画笔颜色为白色
        mLinePaint.setStrokeWidth(lineWidth); //设置线宽

        // 2、设置绘制文本的画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mTextPaint.setColor(Color.WHITE); // 设置画笔颜色为白色
        mTextPaint.setTextSize(textSize); //设置线宽
        textHeight = mTextPaint.descent() + mTextPaint.ascent(); // 文本高度 = 文本上下坡高度之和

        mCenterPaint = new Paint();
        mCenterPaint.setAntiAlias(true);// 设置画笔的锯齿效果
        mCenterPaint.setDither(true);
        mCenterPaint.setColor(getResources().getColor(R.color.colorPrimary)); //102b6a 2a5caa 3F51B5
        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_close); // 设置中心的图片
        // 可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        LogUtiles.d("real widthSpecSize = "+widthSpecSize);
        LogUtiles.d("real heightSpecSize = "+heightSpecSize);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeight);
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, mHeight);
        }

//        if (mRadius > 0)
//            setMeasuredDimension((int)mRadius, (int)mRadius);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mWidthOnWindow = right-left;
        mHeightOnWindow = bottom-top;
        LogUtiles.d("leftOnWindow = "+left);
        LogUtiles.d("topOnWindow = "+top);
        LogUtiles.d("rightOnWindow = "+right);
        LogUtiles.d("bottomOnWindow = "+bottom);
        LogUtiles.d("mWidthOnWindow = "+mWidthOnWindow);
        LogUtiles.d("mHeightOnWindow = "+mHeightOnWindow);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtiles.d("onDraw");

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth()-paddingRight-paddingLeft; // 获取布局阶段控件宽度
        int height = getHeight()-paddingBottom-paddingTop; // 高度
        int radius = Math.min(width, height)/2; // 半径为高度、宽度的最小值的一半

        mRadius = radius;
        if (radius > 0) {
            float cx = paddingLeft + radius; // 获取圆心位置，水平坐标为控件宽度的一半
            float cy = paddingTop + radius; // 垂直坐标为控件高度一半
            float r = radius;

            LogUtiles.d("cx = " + cx);
            LogUtiles.d("cy = " + cy);
            LogUtiles.d("r = " + r);

            canvas.drawCircle(cx, cy, r, mArcPaint);
            drawLines(canvas, paddingLeft, paddingRight, paddingTop, paddingBottom);

            drawText(0, 1, upText, canvas, paddingLeft, paddingRight, paddingTop, paddingBottom);
            drawText(0, -1, downText, canvas, paddingLeft, paddingRight, paddingTop, paddingBottom);
            drawText(-1, 0, leftText, canvas, paddingLeft, paddingRight, paddingTop, paddingBottom);
            drawText(1, 0, rightText, canvas, paddingLeft, paddingRight, paddingTop, paddingBottom);

            drawCenterBitmap(canvas, paddingLeft, paddingRight, paddingTop, paddingBottom);
        }
    }

    /**
     * 绘制交叉直线
     * @param canvas
     */
    private void drawLines(Canvas canvas, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom) {
        float startX = (float) ((1-Math.cos(Math.toRadians(45.0))) * mRadius);
        float startY = (float) ((1-Math.sin(Math.toRadians(45.0))) * mRadius);
        float stopX = 2*mRadius-startX;
        float stopY = 2*mRadius-startY;

        canvas.drawLine(startX+paddingLeft, startY+paddingTop, stopX+paddingLeft, stopY+paddingTop, mLinePaint);
        canvas.drawLine(startX+paddingLeft, stopY+paddingTop, stopX+paddingLeft, startY+paddingTop, mLinePaint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getTop();
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
    }

    /**
     * 在指定位置绘制文本
     * @param x
     * @param y
     * @param text
     * @param canvas
     * @param paddingLeft
     * @param paddingRight
     * @param paddingTop
     * @param paddingBottom
     */
    private void drawText(int x, int y, String text, Canvas canvas, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        // (0, 1) (0,-1) (1,0) (-1,0)
        float textWidth = mTextPaint.measureText(text); // 测量文本宽度
        if (x ==0 && y==1) {
            canvas.drawText(text, paddingLeft+mRadius - textWidth / 2, paddingTop+mRadius*3/8 - textHeight / 2, mTextPaint);
        }else if(x ==0 && y==-1) {
            canvas.drawText(text, paddingLeft+mRadius - textWidth / 2, (paddingTop+mRadius*13/8) - textHeight / 2, mTextPaint);
        }else if(x ==1 && y==0) {
            canvas.drawText(text, (paddingTop+mRadius*13/8) - textWidth / 2, paddingLeft+mRadius - textHeight / 2, mTextPaint);
        }else if(x ==-1 && y==0) {
            canvas.drawText(text, paddingTop+mRadius*3/8 - textWidth / 2, paddingLeft+mRadius - textHeight / 2, mTextPaint);
        }
    }

    /**
     * 绘制中间的退出按键
     * @param canvas
     * @param paddingLeft
     * @param paddingRight
     * @param paddingTop
     * @param paddingBottom
     */
    private void drawCenterBitmap(Canvas canvas, int paddingLeft, int paddingRight, int paddingTop, int paddingBottom){
        int imageWidth = (int) (mRadius/4);// 设置图片宽度为直径 1/8
        int x = (int) (paddingLeft+mRadius);
        int y = (int) (paddingTop+mRadius);
        Rect rect = new Rect(x-imageWidth/2, y-imageWidth/2, x+imageWidth/2, y+imageWidth/2);

        canvas.drawCircle(x, y, imageWidth, mCenterPaint);
        canvas.drawBitmap(mCenterBitmap, null, rect, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogUtiles.d("onTouchEvent..."+MotionEvent.actionToString(event.getAction()));
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_UP:
                int eventType = getEventByPosition(x, y); // 识别点击区域
                handleOnClickListener(eventType); // 处理点击事件
                break;

            case MotionEvent.ACTION_CANCEL:
                break;
        }

        return true;
    }

    /**
     * 根据区域划分调用点击事件接口
     * @param eventType
     */
    private void handleOnClickListener(int eventType) {
        if (mOnClickListener != null){
            switch (eventType){
                case CENTER:
                    mOnClickListener.center();
                    break;
                case UP:
                    mOnClickListener.up();
                    break;
                case DOWN:
                    mOnClickListener.down();
                    break;
                case LEFT:
                    mOnClickListener.left();
                    break;
                case RIGHT:
                    mOnClickListener.right();
                    break;
                default:break;
            }
        }
    }

    /**
     * 根据坐标返回区域事件
     * @param x
     * @param y
     */
    private int getEventByPosition(int x, int y){
        int result = UNSPECIFIED; // 返回值
        int zeroX = (int) (getPaddingLeft()+mRadius);
        int zeroY = (int) (getPaddingTop()+mRadius);
        x = x-zeroX;
        y = zeroY-y;
        LogUtiles.d("x = "+x+";y = "+y+";");
        int absX = Math.abs(x);
        int absY = Math.abs(y);

        int absZ = (int) (Math.pow(absX, 2)+Math.pow(absY, 2)); // 利用勾股定理求出点到圆心距离
        absZ = (int) Math.sqrt(absZ);
        LogUtiles.d("absZ = "+absZ);

        if (absZ<mRadius/4){
            // 中心圆区域
            LogUtiles.d("中心圆区域");
            return CENTER;
        }else if (absZ>mRadius){
            // 超出圆形区域
            LogUtiles.d("超出圆形区域");
            return UNSPECIFIED;
        }else {
            LogUtiles.d("正常识别区域");
        }

        if (x>0 && y>0){
            // 第一象项
            if (absX > absY){
                // 搜索区域
                LogUtiles.d("第一象项 搜索区域");
                result = RIGHT;
            }else if (absX < absY){
                // 页头区域
                LogUtiles.d("第一象项 页头区域");
                result = UP;
            }
        }else if (x>0 && y<0){
            // 第四象项
            if (absX > absY){
                // 搜索区域
                LogUtiles.d("第四象项 搜索区域");
                result = RIGHT;
            }else if (absX < absY){
                // 页尾区域
                LogUtiles.d("第四象项 页尾区域");
                result = DOWN;
            }
        }else if (x<0 && y>0){
            // 第二象项
            if (absX > absY){
                // 足迹区域
                LogUtiles.d("第二象项 足迹区域");
                result = LEFT;
            }else if (absX < absY){
                // 页头区域
                LogUtiles.d("第二象项 页头区域");
                result = UP;
            }
        }else if (x<0 && y<0){
            // 第三象项
            if (absX > absY){
                // 足迹区域
                LogUtiles.d("第三象项 足迹区域");
                result = LEFT;
            }else if (absX < absY){
                // 页尾区域
                LogUtiles.d("第三象项 页尾区域");
                result = DOWN;
            }
        }

        return result;
    }

    public interface CircleNavigationOnClickListener{
        void up();
        void down();
        void left();
        void right();
        void center();
    }
}
