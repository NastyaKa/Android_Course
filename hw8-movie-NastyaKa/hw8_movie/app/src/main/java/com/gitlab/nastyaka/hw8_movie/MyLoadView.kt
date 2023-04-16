package com.gitlab.nastyaka.hw8_movie

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.min

class MyLoadView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val waveGap = 50f
    private var maxRadius = 0f
    private var center = PointF(0f, 0f)
    private var initialRadius = 0f
    private var clr = Color.LTGRAY
    private var strokeW = 10f
    private var animDuration = 500

    init {
        val arr: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.MyLoadView, defStyleAttr, defStyleRes)
        try {
            clr = arr.getColor(R.styleable.MyLoadView_color, clr)
            strokeW = arr.getFloat(R.styleable.MyLoadView_strokWidth, strokeW)
            animDuration = arr.getInt(R.styleable.MyLoadView_animationDuration, animDuration)
        } finally {
            arr.recycle()
        }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        center.set(w / 2f, h / 2f)
        maxRadius = kotlin.math.hypot(center.x.toDouble(), center.y.toDouble()).toFloat()
        initialRadius = w / waveGap
    }

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = clr
        strokeWidth = strokeW
        style = Paint.Style.STROKE
    }

    private var waveAnimator: ValueAnimator? = null
    private var waveRadiusOffset = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        waveAnimator = ValueAnimator.ofFloat(0f, waveGap).apply {
            addUpdateListener {
                waveRadiusOffset = it.animatedValue as Float
            }
            duration = animDuration.toLong()
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    override fun onDetachedFromWindow() {
        waveAnimator?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var currentRadius = initialRadius + waveRadiusOffset
        while (currentRadius < maxRadius) {
            canvas.drawCircle(center.x, center.y, currentRadius, wavePaint)
            currentRadius += waveGap
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        return SavedState(super.onSaveInstanceState(), center)
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val restore = state as SavedState
        super.onRestoreInstanceState(restore.superState)
        center = restore.cntr
    }

    internal class SavedState : BaseSavedState {

        var cntr: PointF

        constructor(source: Parcel) : super(source) {
            this.cntr = PointF(0f, 0f)
        }

        constructor(superState: Parcelable?, cntr: PointF) : super(superState) {
            this.cntr = cntr
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeFloat(cntr.x)
            out?.writeFloat(cntr.y)
        }

        companion object {
            @JvmField
            val CREATOR = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(source: Parcel): SavedState {
                    return SavedState(source)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}