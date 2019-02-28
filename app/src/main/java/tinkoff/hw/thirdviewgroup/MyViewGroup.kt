package tinkoff.hw.thirdviewgroup

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.ViewGroup
import android.os.Parcel
import android.view.View


class MyViewGroup : ViewGroup {

    var lastViews:ArrayList<Int> = ArrayList()
    var paddingWidth = 0
    var paddingHeight = 0
    var childHeight = 0
    var gravity:String? = "left"
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

    /*public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.childrenStates = SparseArray<Parcelable>()
        for (i in 0 until childCount) {
            getChildAt(i).saveHierarchyState(ss.childrenStates as SparseArray<Parcelable>)
        }
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        for (i in 0 until childCount) {
            getChildAt(i).restoreHierarchyState(ss.childrenStates as SparseArray<Parcelable>)
        }
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    internal class SavedState : View.BaseSavedState {
        var childrenStates: SparseArray<Parcelable>? = null

        constructor(superState: Parcelable) : super(superState) {}

        private constructor(`in`: Parcel, classLoader: ClassLoader) : super(`in`) {
            childrenStates = `in`.readSparseArray(classLoader) as SparseArray<Parcelable>
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeSparseArray(childrenStates as SparseArray<Any>)
        }

        companion object {
@JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> =
                object : Parcelable.ClassLoaderCreator<SavedState> {
                    override fun createFromParcel(source: Parcel, loader: ClassLoader?): SavedState {
                        return SavedState(source, loader!!)
                    }

                    override fun createFromParcel(source: Parcel): SavedState {
                        return createFromParcel(source, null)
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }*/
    /*override fun onSaveInstanceState(): Parcelable {
        val saveInstanceState = super.onSaveInstanceState()
        val savedState = SavedState(saveInstanceState)
        savedState.childrenStates = SparseArray()
        for (i in 0 until childCount) {
            getChildAt(i).saveHierarchyState(savedState.childrenStates)
        }
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.getSuperState())
        for (i in 0 until childCount) {
            getChildAt(i).restoreHierarchyState(savedState.childrenStates)
        }
    }

    private class SavedState : View.BaseSavedState {
        var childrenStates: SparseArray<Any>? = null

        internal constructor(superState: Parcelable) : super(superState)

        private constructor(`in`: Parcel, classLoader: ClassLoader) : super(`in`) {
            childrenStates = `in`.readSparseArray(classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeSparseArray(childrenStates)
        }

        companion object {

            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> =
                object : Parcelable.ClassLoaderCreator<SavedState> {
                    override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                        return SavedState(source, loader)
                    }

                    override fun createFromParcel(source: Parcel?): SavedState {
                        return createFromParcel(null)
                    }

                    override fun newArray(size: Int): Array<SavedState> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }*/

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val height = MeasureSpec.getSize(heightMeasureSpec) - paddingTop - paddingBottom
        val count = childCount
        var xpos = paddingLeft
        var ypos = paddingTop
        val childHeightMeasureSpec= MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY)
        val childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,MeasureSpec.AT_MOST)
        val stringHeight = childHeight + paddingHeight
        lastViews.clear()
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec)
                val childw = child.measuredWidth
                if (xpos + childw > width) {
                    xpos = paddingLeft
                    ypos += stringHeight
                    lastViews.add(i-1)
                }
                xpos += childw + paddingWidth
            }
        }
        var resultHeight = 0
        when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> resultHeight = ypos + stringHeight
            MeasureSpec.AT_MOST -> resultHeight = if (ypos + stringHeight < height) ypos + stringHeight else height
            MeasureSpec.EXACTLY -> resultHeight = height
        }
        setMeasuredDimension(width, resultHeight)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l
        var xpos = paddingLeft
        var ypos = paddingTop
        if (gravity == "left") for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != GONE) {
                val childw = child.measuredWidth
                val childh = child.measuredHeight
                if (xpos + childw > width) {
                    xpos = paddingLeft
                    ypos += childHeight + paddingHeight
                }
                child.layout(xpos, ypos, xpos + childw, ypos + childh)
                xpos += childw + paddingWidth
            }
        }
        else {


            for (i in 0 until lastViews.size) {
                xpos = width - paddingRight
                for (j in lastViews[i] downTo if (i == 0) 0 else lastViews[i - 1] + 1) {
                    val child = getChildAt(j)
                    if (child.visibility != GONE) {
                        val childw = child.measuredWidth
                        child.layout(xpos- childw, ypos, xpos, ypos + childHeight)
                        xpos -= childw + paddingWidth
                    }
                }
                ypos += childHeight + paddingHeight


            }
            xpos = width - paddingRight
            val firstElement= if (lastViews.isEmpty()) 0
            else lastViews.last()+1
            for (i in childCount-1 downTo firstElement) {
                val child = getChildAt(i)
                if (child.visibility != GONE) {
                    val childw = child.measuredWidth
                    child.layout(xpos- childw, ypos, xpos, ypos + childHeight)
                    xpos -= childw + paddingWidth
                }
            }
        }

    }
    private fun setAttributes(context:Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyViewGroup)
        paddingWidth = typedArray.getInt(R.styleable.MyViewGroup_padding_width, 0)
        paddingHeight = typedArray.getInt(R.styleable.MyViewGroup_padding_height, 0)
        childHeight = typedArray.getInt(R.styleable.MyViewGroup_child_height, 50)
        gravity = if (typedArray.getString(R.styleable.MyViewGroup_gravity) == null) "left" else typedArray.getString(R.styleable.MyViewGroup_gravity)
        typedArray.recycle()
    }
}