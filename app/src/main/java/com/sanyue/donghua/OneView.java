package com.sanyue.donghua;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * Create by Yip
 * Create Time 2020/6/24
 */
public class OneView extends View {
    //背景色
    private Paint mBackgroundPaint;
    //圆形
    private Path mCirclePath;
    private Paint mCirclePaint;
    private Paint mWhiteCirclePaint;
    //状态完成是的圆
    private Paint mSuccessCircle;
    private Paint mSuccessAlphaCircle;

    //旋转的圆
    private Paint mWhiteAlphaCirclePaint1;
    private Paint mWhiteAlphaCirclePaint2;
    private Paint mWhiteAlphaCirclePaint3;
    private Paint mWhiteAlphaCirclePaint4;
    private Paint mWhiteAlphaCirclePaint5;
    //文字
    private Paint mTextPaint;
    //小圆点
    private Paint mDotPaint;
    private Paint mLinSuccessPaint;
    //打勾需要圆角
    private Paint mDotPaint1;
    //画图标
    private Paint mBitPaint;
    private Bitmap mBitmap;

    //圆半径
    private int radius;
    //随机小圆点的半径
    private int dotRadius;
    //小圆点移动路径
    private int floatXY;
    private boolean flag = false;
    //中心点
    private int centerX;
    private int centerY;
    private int mWidth;
    private int mHeight;
    private Handler handler;
    //默认动画 100毫秒执行一次
    private Runnable runnableDefault;
    //进行中加速动画 1毫秒执行一次
    private Runnable runnableAccelerate;
    //完成后小圆点随机移动的动画 100毫秒执行一次
    private Runnable runnableComplete;
    //旋转角度
    private int rotate;
    //默认小圆点数量
    private int dotCount = 40;
    //默认小圆点移动的范围
    private int moveDistance = 20;
    private List<Dots> dots = new ArrayList<>();
    //用于生成小圆点随机坐标不会在region内
    private Region region;
    //点击范围判断
    private Region circleRegion;
    //随机
    private Random random;
    private int type = 0; //0 默认动画 1进行中 2完成
    //记录完整的点击
    private boolean clickStatus = false;
    private OnClickFillIn onClickFillIn;
    //勾的起始点
    int checkStartX;
    //线的长度
    private int moveLin1Distance = 0;
    private int moveLin2Distance = 0;
    //线1的x轴增量
    private int line1X = 0;
    //线1的y轴增量
    private int line1Y = 0;
    //线2的x轴增量
    private int line2X = 0;
    //线2的y轴增量
    private int line2Y = 0;
    //记录完成时圆 以及线条半径变化
    private int successRadius = 0;
    private boolean secLineInit;
    private boolean isComplete = false;
    //完成时打勾动画
    private ValueAnimator mSuccessCircleAnimator;
    private ValueAnimator mLine1Animator;
    private ValueAnimator mLine2Animator;


    public OneView(Context context) {
        this(context, null);
    }

    public OneView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setOnClickFillIn(OnClickFillIn onClickFillIn) {
        this.onClickFillIn = onClickFillIn;
    }

    private void init() {
        dotRadius = dp2px(2);
        mBackgroundPaint = new Paint();
        mCirclePath = new Path();
        circleRegion = new Region();

        mBackgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mWhiteCirclePaint = new Paint();
        mWhiteCirclePaint.setStyle(Paint.Style.STROKE);
        mWhiteCirclePaint.setStrokeWidth(10);
        mWhiteCirclePaint.setAlpha((int) (255 * 0.88));
        mWhiteCirclePaint.setColor(Color.WHITE);

        mSuccessCircle = new Paint();
        mSuccessCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        mSuccessCircle.setColor(Color.WHITE);
        mSuccessCircle.setAlpha((int) (255 * 0.3));

        mSuccessAlphaCircle = new Paint();
        mSuccessAlphaCircle.setStyle(Paint.Style.FILL_AND_STROKE);
        mSuccessAlphaCircle.setColor(Color.WHITE);
        mSuccessAlphaCircle.setAlpha((int) (255 * 0.2));

        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mDotPaint.setColor(Color.WHITE);

        mDotPaint1 = new Paint();
        mDotPaint1.setAntiAlias(true);
        mDotPaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mDotPaint1.setColor(Color.WHITE);

        mLinSuccessPaint = new Paint();
        mLinSuccessPaint.setAntiAlias(true);
        mLinSuccessPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinSuccessPaint.setColor(Color.WHITE);
        mLinSuccessPaint.setStrokeWidth(20);


        mWhiteAlphaCirclePaint1 = new Paint();
        mWhiteAlphaCirclePaint1.setAntiAlias(true);

        mWhiteAlphaCirclePaint1.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhiteAlphaCirclePaint1.setColor(Color.WHITE);
        mWhiteAlphaCirclePaint1.setAlpha((int) (255 * 0.6));

        mWhiteAlphaCirclePaint2 = new Paint();
        mWhiteAlphaCirclePaint2.setAntiAlias(true);
        mWhiteAlphaCirclePaint2.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhiteAlphaCirclePaint2.setColor(Color.WHITE);
        mWhiteAlphaCirclePaint2.setAlpha((int) (255 * 0.5));

        mWhiteAlphaCirclePaint3 = new Paint();
        mWhiteAlphaCirclePaint3.setAntiAlias(true);
        mWhiteAlphaCirclePaint3.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhiteAlphaCirclePaint3.setColor(Color.WHITE);
        mWhiteAlphaCirclePaint3.setAlpha((int) (255 * 0.4));

        mWhiteAlphaCirclePaint4 = new Paint();
        mWhiteAlphaCirclePaint4.setAntiAlias(true);
        mWhiteAlphaCirclePaint4.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhiteAlphaCirclePaint4.setColor(Color.WHITE);
        mWhiteAlphaCirclePaint4.setAlpha((int) (255 * 0.3));

        mWhiteAlphaCirclePaint5 = new Paint();
        mWhiteAlphaCirclePaint5.setAntiAlias(true);
        mWhiteAlphaCirclePaint5.setStyle(Paint.Style.FILL_AND_STROKE);
        mWhiteAlphaCirclePaint5.setColor(Color.WHITE);
        mWhiteAlphaCirclePaint5.setAlpha((int) (255 * 0.2));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(dp2px(18));

        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setAntiAlias(true);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.shouzhi);

        handler = new Handler();
        region = new Region();

        random = new Random();
        moveDistance = random.nextInt(100);
        startRotate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius = (int) (w / 2.4) / 2;
        centerX = w / 2;
        centerY = h / 2;
        mHeight = h;
        mWidth = w;
        mBackgroundPaint.setShader(new LinearGradient(0f, 0f, w, h, Color.parseColor("#019CE3"), Color.parseColor("#3AD4D0"), Shader.TileMode.MIRROR));
        mCirclePaint.setShader(new LinearGradient(centerX - radius, centerY - radius, centerX + radius, centerY + radius, Color.parseColor("#2FC9D4"), Color.parseColor("#15AFDD"), Shader.TileMode.MIRROR));
        mCirclePath.addCircle(centerX, centerX, radius, Path.Direction.CCW);
        region.set(new Region(centerX - radius - (4 * dotRadius), centerY - radius - (4 * dotRadius), centerX + radius + (4 * dotRadius), centerY + radius + (4 * dotRadius)));
        circleRegion.setPath(mCirclePath, new Region(centerX - radius, centerY - radius, centerX + radius, centerY + radius));
        createDot(dotCount);
        checkStartX = centerX - 100;
        moveLin1Distance = radius / 4;
        moveLin2Distance = (int) (radius / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制背景
        canvas.drawRect(new Rect(0, 0, getWidth(), getHeight()), mBackgroundPaint);
        switch (type) {
            case 0:
            case 2:
                //绘制浮动圆点
                drawDot(canvas);
                break;
            case 1:
                //点击开始中心点移动
                moveDot(canvas);
                break;

        }
        //透明旋转圆
        if (type != 2) {
            drawAlphaCircle(canvas);
        }
        //绘制圆行和文字
        drawCircleText(canvas);


    }

    //透明旋转圆
    private void drawAlphaCircle(Canvas canvas) {
        canvas.save();
        canvas.rotate(rotate, centerX, centerY);
        canvas.drawCircle(centerX + 30, centerY + 30, radius, mWhiteAlphaCirclePaint1);
        canvas.drawCircle(centerX - 30, centerY - 30, radius, mWhiteAlphaCirclePaint2);
        canvas.drawCircle(centerX - 30, centerY + 30, radius, mWhiteAlphaCirclePaint3);
        canvas.drawCircle(centerX + 30, centerY - 30, radius, mWhiteAlphaCirclePaint4);
        canvas.drawCircle(centerX + 30, centerY + 30, radius, mWhiteAlphaCirclePaint5);
        canvas.restore();
    }

    //圆形和文字 打勾
    private void drawCircleText(Canvas canvas) {
        switch (type) {
            case 0:
                canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
                canvas.drawCircle(centerX, centerY, radius, mWhiteCirclePaint);
                canvas.drawText("一键填报", centerX, centerY - radius + (float) ((centerY - radius) / 2.2) + dp2px(30) + mBitmap.getHeight(), mTextPaint);
                canvas.drawBitmap(mBitmap, centerX - mBitmap.getWidth() / 2, centerY - radius + (float) ((centerY - radius) / 2.2), mBitPaint);
                break;
            case 1:
                canvas.drawCircle(centerX, centerY, radius, mCirclePaint);
                canvas.drawCircle(centerX, centerY, radius, mWhiteCirclePaint);
                canvas.drawText("正在生成中...", centerX, centerY + (getFontHeight(mTextPaint) / 2), mTextPaint);
                break;
            case 2:
                canvas.drawCircle(centerX, centerY, successRadius, mSuccessAlphaCircle);
                canvas.drawCircle(centerX, centerY, (float) (successRadius * 0.8), mSuccessCircle);

                if (line1X > 0) {
                    //画第一根线
                    canvas.drawCircle(checkStartX, centerY, 10, mDotPaint1);
                    canvas.drawLine(checkStartX, centerY, line1X, line1Y, mLinSuccessPaint);
                    if (secLineInit) {
                        //画第二根线
                        canvas.drawLine(line1X - 12, line1Y, line2X, line2Y, mLinSuccessPaint);
                        if (isComplete) {
                            canvas.drawCircle(line2X, line2Y, 10, mDotPaint1);
                        }
                    }
                }


        }

    }

    //点击中心圆加速动画
    private void accelerate() {
        type = 1;
        if (handler == null) {
            handler = new Handler();
        }
        if (runnableDefault != null) {
            handler.removeCallbacks(runnableDefault);
        }
        if (runnableComplete != null) {
            handler.removeCallbacks(runnableComplete);
        }
        if (runnableAccelerate == null) {
            runnableAccelerate = new Runnable() {
                @Override
                public void run() {
                    //数字自增
                    if (rotate > 360) {
                        rotate = 0;
                    } else {
                        rotate = rotate + 10;
                    }
                    invalidate();
                    handler.postDelayed(this, 1);
                }
            };
        }
        handler.post(runnableAccelerate);


    }

    //小圆点
    private void drawDot(Canvas canvas) {
        for (int i = 0; i < dots.size(); i++) {
            Dots dot = dots.get(i);
            mDotPaint.setAlpha((int) (255 * dot.getAlpha()));
            if (i % 2 == 0) {
                canvas.drawCircle(dot.getX() - floatXY, dot.getY() + floatXY, dot.getRadius(), mDotPaint);
            } else {
                canvas.drawCircle(dot.getX() + floatXY, dot.getY() - floatXY, dot.getRadius(), mDotPaint);
            }
        }
    }

    //向中心点引动小圆点
    private void moveDot(Canvas canvas) {
        for (int i = 0; i < dots.size(); i++) {
            if (dots.get(i).getX() > centerX) {
                dots.get(i).setX(dots.get(i).getX() - 6);
            }
            if (dots.get(i).getX() < centerX) {
                dots.get(i).setX(dots.get(i).getX() + 6);
            }
            if (dots.get(i).getY() > centerY) {
                dots.get(i).setY(dots.get(i).getY() - 6);
            }
            if (dots.get(i).getY() < centerY) {
                dots.get(i).setY(dots.get(i).getY() + 6);
            }
            Dots dot = dots.get(i);
            canvas.drawCircle(dot.getX(), dot.getY(), dot.getRadius(), mDotPaint);

            if (region.contains(dot.getX(), dot.getY())) {
                dots.remove(i);
                i--;
            }
        }
        if (dots.size() < dotCount) {
            createDot(dotCount - dots.size());
        }

    }

    //默认动画
    private void startRotate() {
        reset();
        if (handler == null) {
            handler = new Handler();
        }
        if (runnableAccelerate != null) {
            handler.removeCallbacks(runnableAccelerate);
        }
        if (runnableComplete != null) {
            handler.removeCallbacks(runnableComplete);
        }
        if (runnableDefault == null) {
            runnableDefault = new Runnable() {
                @Override
                public void run() {
                    //旋转角度增加
                    if (rotate > 360) {
                        rotate = 0;
                    } else {
                        rotate += 3;
                    }
                    //判断小圆点移动完成了一次floatXY
                    if (floatXY > moveDistance) {
                        flag = true;
                    } else if (floatXY < -moveDistance) {
                        //第二次完成时 在随机生成移动距离
                        moveDistance = random.nextInt(100);
                        flag = false;
                    }
                    if (flag) {
                        floatXY--;
                    } else {
                        floatXY++;
                    }
                    invalidate();
                    handler.postDelayed(this, 100);
                }
            };
        }
        handler.post(runnableDefault);
    }

    //完成动画
    public void complete() {
        secLineInit = false;
        type = 2;
        if (handler == null) {
            handler = new Handler();
        }
        if (runnableAccelerate != null) {
            handler.removeCallbacks(runnableAccelerate);
        }
        if (runnableComplete != null) {
            handler.removeCallbacks(runnableComplete);
        }
        if (runnableComplete == null) {
            runnableComplete = new Runnable() {
                @Override
                public void run() {
                    if (floatXY > moveDistance) {
                        flag = true;
                    } else if (floatXY < -moveDistance) {
                        moveDistance = random.nextInt(100);
                        flag = false;
                    }
                    if (flag) {
                        floatXY--;
                    } else {
                        floatXY++;
                    }
                    invalidate();
                    handler.postDelayed(this, 100);
                }
            };
        }
        handler.post(runnableComplete);
        if (mSuccessCircleAnimator == null) {
            mSuccessCircleAnimator = ValueAnimator.ofInt(0, radius);
        }
        if (mLine1Animator == null) {
            mLine1Animator = ValueAnimator.ofInt(0, moveLin1Distance);
        }
        if (mLine2Animator == null) {
            mLine2Animator = ValueAnimator.ofInt(0, moveLin2Distance);
        }

        mSuccessCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                successRadius = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mSuccessCircleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mLine1Animator != null) {
                    mLine1Animator.start();
                }
            }
        });
        mSuccessCircleAnimator.setDuration(500);
        mSuccessCircleAnimator.start();

        mLine1Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                line1X = checkStartX + (int) animation.getAnimatedValue();
                line1Y = centerY + (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        mLine1Animator.setDuration(250);
        mLine1Animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                secLineInit = true;
                if (mLine2Animator != null) {
                    mLine2Animator.start();
                }
            }
        });


        mLine2Animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                line2X = line1X + (int) animation.getAnimatedValue();
                line2Y = line1Y - (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        mLine2Animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isComplete = true;
                invalidate();
                if (onClickFillIn != null) {
                    onClickFillIn.complete();
                }
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                line2X = line1X;
            }
        });
        mLine2Animator.setDuration(250);
    }

    //创建随机小圆点避开中心圆区域
    public void createDot(int size) {
        for (int i = 0; i < size; i++) {
            Dots dots1 = new Dots();
            int radius = (int) (Math.random() * (4 - 1)) + 1;
            double alpha = (double) (Math.random() * (0.9 - 0.1)) + 0.1;
            int x = (int) (Math.random() * ((mWidth - 100) - 100)) + 100;
            int y = (int) (Math.random() * ((mHeight - 100) - 100)) + 100;
            dots1.setX(x);
            dots1.setY(y);
            dots1.setRadius(radius * dotRadius);
            dots1.setAlpha(alpha);
            if (!region.contains(x, y)) {
                dots.add(dots1);
            }
        }
        if (dots.size() < size) {
            createDot(size - dots.size());
        }

    }

    //重制属性状态
    public void reset() {
        type = 0;
        line1X = 0;
        line1Y = 0;
        line2X = 0;
        line2Y = 0;
        secLineInit = false;
        isComplete = false;
        checkStartX = centerX - 100;
        successRadius = 0;
    }

    //页面销毁清理
    public void clean() {
        reset();
        if (handler != null) {
            if (runnableDefault != null) {
                handler.removeCallbacks(runnableDefault);
            }
            if (runnableAccelerate != null) {
                handler.removeCallbacks(runnableAccelerate);
            }
            if (runnableComplete != null) {
                handler.removeCallbacks(runnableComplete);
            }
        }
        handler = null;
        runnableDefault = null;
        runnableAccelerate = null;
        runnableComplete = null;
        if (mSuccessCircleAnimator != null) {
            mSuccessCircleAnimator.cancel();
        }
        if (mLine1Animator != null) {
            mLine1Animator.cancel();
        }
        if (mLine2Animator != null) {
            mLine2Animator.cancel();
        }
        dots.clear();
        dots = null;
        region = null;
        circleRegion = null;
        random = null;
        onClickFillIn = null;
        mBitmap = null;
    }


    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, Resources.getSystem().getDisplayMetrics());
    }

    public static class Dots {
        private int x;
        private int y;
        private int radius;
        private double alpha;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public double getAlpha() {
            return alpha;
        }

        public void setAlpha(double alpha) {
            this.alpha = alpha;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickStatus = true;
                break;
            case MotionEvent.ACTION_UP:
                if (clickStatus && circleRegion.contains((int) event.getX(), (int) event.getY())) {
                    accelerate();
                    if (onClickFillIn != null) {
                        onClickFillIn.click();
                    }
                }
                clickStatus = false;
                break;
        }
        return true;
    }

    /**
     * @return 返回指定的文字高度
     */
    public float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
        return fm.descent - fm.ascent;
    }

    interface OnClickFillIn {
        void click();

        void complete();
    }
}
