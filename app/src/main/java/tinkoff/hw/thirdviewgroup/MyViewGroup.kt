package tinkoff.hw.thirdviewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.Gravity

class MyViewGroup : ViewGroup {

    var lastViews:ArrayList<Int> = ArrayList()
    var paddingWidth = 0
    var paddingHeight = 0
    var childHeight = 0
    var gravity = Gravity.LEFT
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (attrs != null) {
            setAttributes(context,attrs)
        }
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            setAttributes(context,attrs)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val count = childCount
        var xPosition = paddingLeft
        var yPosition = paddingTop
        val childHeightMeasureSpec= MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST)
        val stringHeight = childHeight + paddingHeight
        lastViews.clear()
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
                val childWidth = child.measuredWidth
                if (xPosition + childWidth > width) {
                    xPosition = paddingLeft
                    yPosition += stringHeight
                    lastViews.add(i-1)
                }
                xPosition += childWidth + paddingWidth
            }
        }
        var resultHeight = 0
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> resultHeight = yPosition + stringHeight + paddingBottom
            MeasureSpec.AT_MOST -> resultHeight = if (yPosition + stringHeight < height) yPosition + stringHeight + paddingBottom else height
            MeasureSpec.EXACTLY -> resultHeight = height
        }
        setMeasuredDimension(width + paddingRight, resultHeight)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l - paddingRight
        var xPosition = paddingLeft
        var yPosition = paddingTop
        if (gravity != Gravity.RIGHT) for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight
                if (xPosition + childWidth > width) {
                    xPosition = paddingLeft
                    yPosition += this.childHeight + paddingHeight
                }
                child.layout(xPosition, yPosition, xPosition + childWidth, yPosition + childHeight)
                xPosition += childWidth + paddingWidth
            }
        }
        else {
            for (i in 0 until lastViews.size) {
                xPosition = width
                for (j in lastViews[i] downTo if (i == 0) 0 else lastViews[i - 1] + 1) {
                    val child = getChildAt(j)
                    if (child.visibility != GONE) {
                        val childWidth = child.measuredWidth
                        child.layout(xPosition - childWidth, yPosition, xPosition, yPosition + childHeight)
                        xPosition -= childWidth + paddingWidth
                    }
                }
                yPosition += childHeight + paddingHeight
            }
            xPosition = width
            val firstElement = if (lastViews.isEmpty()) 0 else lastViews.last() + 1
            for (i in childCount - 1 downTo firstElement) {
                val child = getChildAt(i)
                if (child.visibility != GONE) {
                    val childWidth = child.measuredWidth
                    child.layout(xPosition - childWidth, yPosition, xPosition, yPosition + childHeight)
                    xPosition -= childWidth + paddingWidth
                }
            }
        }
    }
    private fun setAttributes(context:Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyViewGroup)
        paddingWidth = typedArray.getInt(R.styleable.MyViewGroup_padding_width, 0)
        paddingHeight = typedArray.getInt(R.styleable.MyViewGroup_padding_height, 0)
        childHeight = typedArray.getInt(R.styleable.MyViewGroup_child_height, 50)
        gravity = typedArray.getInt(R.styleable.MyViewGroup_android_gravity, Gravity.LEFT)
        typedArray.recycle()
    }
}