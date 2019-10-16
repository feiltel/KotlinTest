package com.nut2014.kotlintest.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import com.nut2014.kotlintest.R

class CirclePercentView : View {
    private var mPaint: Paint? = null
    private var progressPercent: Float = 0.toFloat()
    private var radius: Int = 0//圆弧宽度
    private var rectF: RectF? = null
    private var bgColor: Int = 0
    private var progressColor: Int = 0
    private var startColor: Int = 0
    private var endColor: Int = 0
    private var gradient: LinearGradient? = null
    private var isGradient: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePercentView)
        bgColor =
            typedArray.getColor(R.styleable.CirclePercentView_circleBgColor, resources.getColor(R.color.gray_cfcfcf))
        progressColor = typedArray.getColor(
            R.styleable.CirclePercentView_circleProgressColor,
            resources.getColor(R.color.orange_ffc032)
        )
        radius = typedArray.getInt(R.styleable.CirclePercentView_radius, WIDTH_RADIUS_RATIO)
        isGradient = typedArray.getBoolean(R.styleable.CirclePercentView_circleIsGradient, false)
        startColor = typedArray.getColor(
            R.styleable.CirclePercentView_circleStartColor,
            resources.getColor(R.color.black_3A3D4E)
        )
        endColor =
            typedArray.getColor(R.styleable.CirclePercentView_circleEndColor, resources.getColor(R.color.black_475B80))
        typedArray.recycle()
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredWidth, measuredWidth)//自定义的View能够使用wrap_content或者是match_parent的属性
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gradient = LinearGradient(
            width.toFloat(),
            0f,
            width.toFloat(),
            height.toFloat(),
            startColor,
            endColor,
            Shader.TileMode.MIRROR
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 1、绘制背景灰色圆环
        val centerX = width / 2
        val strokeWidth = centerX / radius
        mPaint!!.shader = null//必须设置为null，否则背景也会加上渐变色
        mPaint!!.strokeWidth = strokeWidth.toFloat() //设置画笔的大小
        mPaint!!.color = bgColor
        canvas.drawCircle(centerX.toFloat(), centerX.toFloat(), (centerX - strokeWidth / 2).toFloat(), mPaint!!)
        // 2、绘制比例弧
        if (rectF == null) {//外切正方形
            rectF = RectF(
                (strokeWidth / 2).toFloat(),
                (strokeWidth / 2).toFloat(),
                (2 * centerX - strokeWidth / 2).toFloat(),
                (2 * centerX - strokeWidth / 2).toFloat()
            )
        }
        //3、是否绘制渐变色
        if (isGradient) {
            mPaint!!.shader = gradient//设置线性渐变
        } else {
            mPaint!!.color = progressColor
        }
        canvas.drawArc(rectF!!, -90f, 3.6f * progressPercent, false, mPaint!!)   //画比例圆弧
    }

    private fun init() {
        mPaint = Paint()
        //画笔样式
        mPaint!!.style = Paint.Style.STROKE
        //设置笔刷的样式:圆形
        mPaint!!.strokeCap = Paint.Cap.ROUND
        //设置抗锯齿
        mPaint!!.isAntiAlias = true
    }

    @Keep
    fun setPercentage(percentage: Float) {
        this.progressPercent = percentage
        invalidate()
    }

    fun setRadius(radius: Int) {
        this.radius = radius
    }

    fun setBgColor(bgColor: Int) {
        this.bgColor = bgColor
    }

    fun setProgressColor(progressColor: Int) {
        this.progressColor = progressColor
    }

    fun setStartColor(startColor: Int) {
        this.startColor = startColor
    }

    fun setEndColor(endColor: Int) {
        this.endColor = endColor
    }

    fun setGradient(gradient: Boolean) {
        isGradient = gradient
    }

    companion object {

        val WIDTH_RADIUS_RATIO = 8     // 弧线半径 : 弧线线宽 (比例)
        val MAX = 100
    }
}

