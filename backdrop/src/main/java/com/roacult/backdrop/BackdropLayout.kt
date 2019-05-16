package com.roacult.backdrop

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

    /**
     * set peek height
     * this should be the height of front layout
     * header
     * */
    var peeckHeight = 0F
    var frontLayoutId = 0
    var backLayoutId = 0
    var toolbarId = 0
    private var frontLayout : View? = null
    private var backLayout : View? = null
    private var toolbar : Toolbar? = null
    var menuIcon : Int = R.drawable.menu
    var closeIcon : Int = R.drawable.close
    var duration   = DEFAULT_DURATION

    private var state : State = State.CLOSE

    private val animator =  ValueAnimator()

    companion object {
        const val OLD_PARCE ="oldParce"
        const val STATE =  "BackDropState"
        const val DEFAULT_DURATION = 400
    }

    enum class State {
        OPEN,CLOSE
    }

    override fun onSaveInstanceState(): Parcelable? {
        return  Bundle().apply {
            val p = super.onSaveInstanceState()
            putParcelable(OLD_PARCE,p)
            putSerializable(STATE,state)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val bundle = state as Bundle
        bundle.apply {
            super.onRestoreInstanceState(bundle.getParcelable(OLD_PARCE))
            this@BackdropLayout.state = getSerializable(STATE) as State
        }
        post {
            update(false)
        }
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
        duration = typedArray.getInteger(R.styleable.BackdropLayout_animationDuration, DEFAULT_DURATION)

        typedArray.recycle()
    }

    /**
     * open : show back layout and swap front layout
     * */
    fun open() {
        if(state == State.OPEN) return
        state = State.OPEN
        update(true)
    }

    /**
     * close : hide back layout and swap front layout
     * */
    fun close(){
        if(state== State.CLOSE) return
        state= State.CLOSE
        update(true)
    }

    /**
     * change state of backdrop layout
     * */
    fun switch() {
        if(state == State.OPEN) close()
        else open()
    }

    private fun update(withAnimation : Boolean) {
        when(state) {
            State.OPEN -> {
                getToolbar()?.setNavigationIcon(closeIcon)
                val transitionHeight = getTransitionHeight()
                if(withAnimation) startTranslateAnimation(transitionHeight)
                else getFrontLayout().translationY = transitionHeight
            }

            State.CLOSE -> {
                getToolbar()?.setNavigationIcon(menuIcon)
                if(withAnimation) startTranslateAnimation(0F)
                else getFrontLayout().translationY = 0F
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getToolbar()?.apply {
            setNavigationIcon( if(state == State.CLOSE) menuIcon else closeIcon )
            setNavigationOnClickListener {
                switch()
            }
        }
    }

    private fun getFrontLayout() : View {
        if(frontLayout != null ) return frontLayout!!
        frontLayout = findViewById(frontLayoutId) ?: throw Exception("please provide a valid id for front layout")
        return frontLayout!!
    }

    private fun getBackLayout() : View {
        if(backLayout != null) return backLayout!!
        backLayout = findViewById(backLayoutId) ?: throw Exception("please provide a valid id for back layout")
        return backLayout!!
    }

    private fun getToolbar() : Toolbar? {
        if(toolbar != null) return toolbar
        else if(toolbarId == 0) return null
        toolbar = rootView.findViewById(toolbarId)
        return toolbar
    }

    private fun startTranslateAnimation (to: Float) {
        animator.pause()
        animator.apply {
            setFloatValues(getFrontLayout().translationY,to)
            duration = this@BackdropLayout.duration.toLong()
            addUpdateListener {
                getFrontLayout().translationY = it.animatedValue as Float
            }
            start()
        }
    }

    private fun getTransitionHeight(): Float {
        return Math.min(getBackLayout().height.toFloat(),height - peeckHeight)
    }
}