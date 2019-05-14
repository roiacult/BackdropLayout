package com.roacult.kero.team7.backdrop

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar

class BackdropLayout @JvmOverloads constructor(context: Context, attribute : AttributeSet? = null, defStyleAttr: Int = 0)
    : FrameLayout(context,attribute,defStyleAttr){

    var peeckHeight = 0F
    var frontLayoutId = 0
    var backLayoutId = 0
    var toolbarId = 0
    var menuIcon : Int = R.drawable.menu
    var closeIcon : Int = R.drawable.close
    var duration   = DEFAULT_DURATION

    private var state : State  =State.CLOSE

    private val animator =  ValueAnimator()

    companion object {
        const val OLD_PARCE ="oldParce"
        const val STATE =  "BackDropState"
        const val TRANSITION = "BackDropTransation"
        const val DEFAULT_DURATION = 400
    }

    enum class State {
        OPEN,CLOSE
    }

    override fun onSaveInstanceState(): Parcelable? {
        return  Bundle().apply {
            val p = super.onSaveInstanceState()
            putParcelable(OLD_PARCE,p)
//            putParcelable(TRANSITION,) TODO
            putSerializable(STATE,state)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        bundle.apply {
            super.onRestoreInstanceState(bundle.getParcelable(OLD_PARCE))
            this@BackdropLayout.state = getSerializable(STATE) as State
        }
        changeState()
    }

    init {
        val typedArray = context.obtainStyledAttributes(attribute, R.styleable.BackdropLayout)
        frontLayoutId = typedArray.getResourceId(R.styleable.BackdropLayout_front_layout,0)
        backLayoutId = typedArray.getResourceId(R.styleable.BackdropLayout_back_layout,0)

        if(frontLayoutId == 0 || backLayoutId ==0 )
            throw Exception("you should provide both front and back layout in xml file")

        peeckHeight = typedArray.getDimension(R.styleable.BackdropLayout_peekHeight,0F)
        toolbarId = typedArray.getResourceId(R.styleable.BackdropLayout_toolbarId,0)
        menuIcon = typedArray.getResourceId(R.styleable.BackdropLayout_menuDrawable,R.drawable.menu)
        closeIcon = typedArray.getResourceId(R.styleable.BackdropLayout_closeDrawable,R.drawable.close)
        duration = typedArray.getInteger(R.styleable.BackdropLayout_animationDuration,DEFAULT_DURATION)

        typedArray.recycle()
    }


    private fun changeState() {

        val toolbar = rootView.findViewById<Toolbar>(toolbarId)

        when(state) {
            State.OPEN -> {
                toolbar?.setNavigationIcon(closeIcon)
                startTranslateAnimation(height-peeckHeight)
            }

            State.CLOSE -> {
                toolbar?.setNavigationIcon(menuIcon)
                startTranslateAnimation(0F)
            }
        }
    }

    private fun startTranslateAnimation (to: Float) {
        val frontLayout = findViewById<View>(frontLayoutId) ?: throw Exception("please provide a valid id for front layout")
        animator.pause()
        animator.apply {
            setFloatValues(frontLayout.translationY,to)
            duration = this@BackdropLayout.duration.toLong()
            addUpdateListener {
                frontLayout.translationY = it.animatedValue as Float
            }
            start()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val backLayout : View = findViewById(backLayoutId) ?: throw Exception("please provide a valid id for back layout")
        if( toolbarId != 0 ){
            rootView.findViewById<Toolbar>(toolbarId)?.let{
                it.setNavigationOnClickListener {
//                    switch() TODO
                }
            }
        }
        changeState()

    }

    private fun getBackLayoutHeight(): Int {
        val backLayout : View = findViewById(backLayoutId) ?: throw Exception("please provide a valid id for back layout")
        return Math.min(backLayout.height,height - peeckHeight.toInt())
    }


}