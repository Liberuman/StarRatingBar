package com.sxu.starratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/*******************************************************************************
 * Description: 自定义RatingBar
 *
 * Author: Freeman
 *
 * Date: 2018/4/18
 *
 *******************************************************************************/

public class StarRatingBar extends View {

	private Drawable mDefaultStar;
	private Drawable mStar;
	private int mDefaultStarColor;
	private int mStarColor;
	private int mStarNum;
	private float mStarStep;
	private int mStarGap;
	private int mStarSize;
	private float mRating;
	private boolean mIsIndicator;

	private Paint starPaint;

	public StarRatingBar(Context context) {
		this(context, null);
	}

	public StarRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StarRatingBar);
		mDefaultStar = typedArray.getDrawable(R.styleable.StarRatingBar_defaultStar);
		mStar = typedArray.getDrawable(R.styleable.StarRatingBar_star);
		mDefaultStarColor = typedArray.getColor(R.styleable.StarRatingBar_defaultStarColor, Color.parseColor("#eeeeee"));
		mStarColor = typedArray.getColor(R.styleable.StarRatingBar_starColor, Color.parseColor("#ff9100"));
		mStarNum = typedArray.getInteger(R.styleable.StarRatingBar_starNum, 5);
		mStarStep = typedArray.getFloat(R.styleable.StarRatingBar_starStep,  0.5f);
		mStarGap = typedArray.getDimensionPixelOffset(R.styleable.StarRatingBar_starGap, 10);
		mStarSize = typedArray.getDimensionPixelOffset(R.styleable.StarRatingBar_starSize, 80);
		mRating = typedArray.getFloat(R.styleable.StarRatingBar_rating, 2.5f);
		mIsIndicator = typedArray.getBoolean(R.styleable.StarRatingBar_isIndicator, true);
		typedArray.recycle();

		starPaint = new Paint();
		starPaint.setAntiAlias(true);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int radius = mStarSize / 2;
		canvas.translate(radius, radius);
		if (mDefaultStar != null) {
			drawStarDrawable(canvas, mDefaultStar, mStarNum);
		} else {
			starPaint.setColor(mDefaultStarColor);
			int gap = 0;
			for (int i = 0; i < mStarNum; i++) {
				drawStar(canvas, starPaint, i * mStarSize + gap, radius);
				gap += mStarGap;
			}
		}

		// 设置可视区域
		int size = Math.round(mRating);
		float decimal = 0;
		// 根据步长获取小数位
		if (size > mRating) {
			decimal = (mRating - size + 1);
			int rate = (int) (decimal / mStarStep);
			decimal = rate * mStarStep;
		}
		int right = (int) (((int)mRating) * (mStarSize + mStarGap) + decimal * mStarSize - radius);
		canvas.clipRect(-radius, -radius, right, mStarSize - radius);
		if (mStar != null) {
			drawStarDrawable(canvas, mStar, size);
		} else {
			Paint newPaint = new Paint();
			newPaint.setAntiAlias(true);
			newPaint.setColor(mStarColor);
			int gap = 0;
			for (int i = 0; i < size; i++) {
				drawStar(canvas, newPaint, i * mStarSize + gap, radius);
				gap += mStarGap;
			}
		}
	}

	private void drawStarDrawable(Canvas canvas, Drawable starDrawable, int starNum) {
		Bitmap bitmap = ((BitmapDrawable)starDrawable).getBitmap();
		int gap = 0;
		int radius = mStarSize / 2;
		for (int i = 0; i < starNum; i++) {
			Rect desRect = new Rect(i * mStarSize - radius + gap , -radius, (i+1) * mStarSize - radius + gap, mStarSize - radius);
			canvas.drawBitmap(bitmap, null, desRect, starPaint);
			gap += mStarGap;
		}
	}

	private void drawStar(Canvas canvas, Paint paint, int startX, int radius) {
		Point[] points = new Point[5];
		for (int i = 0; i < 5; i++) {
			points[i] = new Point();
			points[i].x = startX + (int) (radius * Math.cos(Math.toRadians(72 * i - 18)));
			points[i].y = (int) (radius * Math.sin(Math.toRadians(72 * i - 18)));
		}
		Path path = new Path();
		path.moveTo(points[0].x, points[0].y);
		int i = 2;
		while (i != 5) {
			if (i >= 5) {
				i %= 5;
			}
			path.lineTo(points[i].x, points[i].y);
			i += 2;
		}
		path.close();
		canvas.drawPath(path, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsIndicator) {
			return super.onTouchEvent(event);
		}

		mRating = event.getX() / (mStarSize + mStarGap);
		invalidate();
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode == MeasureSpec.AT_MOST) {
			widthSize = getPaddingLeft() + getPaddingRight();
			if (mStarNum > 0) {
				widthSize += mStarNum * mStarSize + (mStarNum - 1) * mStarGap;
			}
		} else if (widthMode == MeasureSpec.UNSPECIFIED) {
			widthSize = getSuggestedMinimumWidth();
		}

		if (heightMode == MeasureSpec.AT_MOST) {
			heightSize = getPaddingTop() + getPaddingBottom() + mStarSize;
		} else if (heightMode == MeasureSpec.UNSPECIFIED) {
			heightSize = getSuggestedMinimumHeight();
		}

		setMeasuredDimension(widthSize, heightSize);
	}

	public Drawable getDefaultStar() {
		return mDefaultStar;
	}

	public void setDefaultStar(Drawable defaultStar) {
		this.mDefaultStar = defaultStar;
		invalidate();
	}

	public Drawable getStar() {
		return mStar;
	}

	public void setStar(Drawable star) {
		this.mStar = star;
		invalidate();
	}

	public int getDefaultStarColor() {
		return mDefaultStarColor;
	}

	public void setDefaultStarColor(int defaultStarColor) {
		this.mDefaultStarColor = defaultStarColor;
		invalidate();
	}

	public int getStarColor() {
		return mStarColor;
	}

	public void setStarColor(int starColor) {
		this.mStarColor = starColor;
		invalidate();
	}

	public int getStarNum() {
		return mStarNum;
	}

	public void setStarNum(int starNum) {
		this.mStarNum = starNum;
		invalidate();
	}

	public float getStarStep() {
		return mStarStep;
	}

	public void setStarStep(float starStep) {
		this.mStarStep = starStep;
		invalidate();
	}

	public int getStarGap() {
		return mStarGap;
	}

	public void setStarGap(int starGap) {
		this.mStarGap = starGap;
		invalidate();
	}

	public int getStarSize() {
		return mStarSize;
	}

	public void setStarSize(int starSize) {
		this.mStarSize = starSize;
		invalidate();
	}

	public boolean getIsIndicator() {
		return mIsIndicator;
	}

	public void setIsIndicator(boolean isIndicator) {
		this.mIsIndicator = isIndicator;
	}

	public void setRating(float rating) {
		this.mRating = rating;
		invalidate();
	}

	public float getRating() {
		return mRating;
	}
}
