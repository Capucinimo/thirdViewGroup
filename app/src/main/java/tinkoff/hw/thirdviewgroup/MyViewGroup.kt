package tinkoff.hw.thirdviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.Gravity
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

class MyViewGroup : ViewGroup {

    var lastViews: ArrayList<Int> = ArrayList()
    var paddingWidth = 0
    var paddingHeight = 0
    var childHeight = 50
    var gravity = Gravity.LEFT

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            setAttributes(context, attrs)
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            setAttributes(context, attrs)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val count = childCount
        var xPosition = paddingLeft
        var yPosition = paddingTop
        val childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width - paddingLeft - paddingRight, MeasureSpec.AT_MOST)
        var stringHeight = childHeight + paddingHeight
        lastViews.clear()
        for (i in 0 until count) {
            val child = getChildAt(i)
            val leftMargin = child.marginLeft
            val rightMargin = child.marginRight
            val topMargin = child.marginTop
            val bottomMargin = child.marginBottom
            if (child.visibility != GONE) {
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
                val childWidth = child.measuredWidth
                if (xPosition + leftMargin + childWidth + rightMargin > width - paddingRight) {
                    xPosition = paddingLeft
                    yPosition += stringHeight
                    stringHeight = childHeight + paddingHeight
                    lastViews.add(i - 1)
                }
                stringHeight = Math.max(stringHeight, childHeight + paddingHeight + topMargin + bottomMargin)
                xPosition += leftMargin + childWidth + rightMargin + paddingWidth
            }
        }
        var resultHeight = 0
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> resultHeight = yPosition + stringHeight - paddingHeight + paddingBottom
            MeasureSpec.AT_MOST -> resultHeight =
                if (yPosition + stringHeight - paddingHeight + paddingBottom < height) yPosition + stringHeight + paddingBottom else height
            MeasureSpec.EXACTLY -> resultHeight = height
        }
        setMeasuredDimension(width, resultHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l - paddingRight
        var xPosition = paddingLeft
        var yPosition = paddingTop
        var stringHeight = childHeight + paddingHeight
        if (gravity != Gravity.RIGHT) for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == GONE) {
                child.layout(0, 0, 0, 0)
            }
            else {
                val leftMargin = child.marginLeft
                val rightMargin = child.marginRight
                val topMargin = child.marginTop
                val bottomMargin = child.marginBottom
                val childWidth = child.measuredWidth
                if (xPosition + leftMargin + childWidth + rightMargin > width) {
                    xPosition = paddingLeft
                    yPosition += stringHeight
                    stringHeight = childHeight + paddingHeight
                }
                stringHeight = Math.max(stringHeight, childHeight + paddingHeight + topMargin + bottomMargin)
                child.layout(
                    xPosition + leftMargin,
                    yPosition + topMargin,
                    xPosition + leftMargin + childWidth,
                    yPosition + topMargin + childHeight
                )
                xPosition += leftMargin + childWidth + rightMargin + paddingWidth
            }
        }
        else {
            for (i in 0 until lastViews.size) {
                xPosition = width
                for (j in lastViews[i] downTo if (i == 0) 0 else lastViews[i - 1] + 1) {
                    val child = getChildAt(j)
                    if (child.visibility == GONE) {
                        child.layout(0, 0, 0, 0)
                    }
                    else {
                        val leftMargin = child.marginLeft
                        val rightMargin = child.marginRight
                        val topMargin = child.marginTop
                        val bottomMargin = child.marginBottom
                        val childWidth = child.measuredWidth
                        child.layout(
                            xPosition - rightMargin - childWidth,
                            yPosition + topMargin,
                            xPosition - rightMargin,
                            yPosition + topMargin + childHeight
                        )
                        xPosition -= leftMargin + childWidth + rightMargin + paddingWidth
                        stringHeight = Math.max(stringHeight, childHeight + paddingHeight + topMargin + bottomMargin)
                    }
                }
                yPosition += stringHeight
                stringHeight = childHeight + paddingHeight
            }
            xPosition = width
            val firstElement = if (lastViews.isEmpty()) 0 else lastViews.last() + 1
            for (i in childCount - 1 downTo firstElement) {
                val child = getChildAt(i)
                if (child.visibility == GONE) {
                    child.layout(0, 0, 0, 0)
                }
                else {
                    val leftMargin = child.marginLeft
                    val rightMargin = child.marginRight
                    val topMargin = child.marginTop
                    val childWidth = child.measuredWidth
                    child.layout(
                        xPosition - rightMargin - childWidth,
                        yPosition + topMargin,
                        xPosition - rightMargin,
                        yPosition + topMargin + childHeight
                    )
                    xPosition -= leftMargin + childWidth + rightMargin + paddingWidth
                }
            }
        }
    }

    private fun setAttributes(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyViewGroup)
        paddingWidth = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_interval_width, 0)
        paddingHeight = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_interval_height, 0)
        childHeight = typedArray.getDimensionPixelSize(R.styleable.MyViewGroup_child_height, 50)
        gravity = typedArray.getInt(R.styleable.MyViewGroup_android_gravity, Gravity.LEFT)
        typedArray.recycle()
    }
}