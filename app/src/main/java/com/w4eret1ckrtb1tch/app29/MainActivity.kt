package com.w4eret1ckrtb1tch.app29

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.FlingAnimation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.concurrent.Executors
import kotlin.math.hypot
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {

    var isRevealed: Boolean = false

    lateinit var main: ConstraintLayout
    lateinit var content: FrameLayout
    lateinit var fab: FloatingActionButton
    lateinit var text: TextView
    lateinit var flight: ImageView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main = findViewById(R.id.main_container)
        content = findViewById(R.id.content_container)
        fab = findViewById(R.id.floatingActionButton)
        text = findViewById(R.id.text)
        flight = findViewById(R.id.flight)


        // TODO: 28.07.2021 29.2 29.2. Fling

        val flingX = FlingAnimation(flight, DynamicAnimation.ROTATION_X).apply {
            minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_ROTATION_DEGREES
            friction = 1.5f
        }
        val flingY = FlingAnimation(flight, DynamicAnimation.ROTATION_Y).apply {
            minimumVisibleChange = DynamicAnimation.MIN_VISIBLE_CHANGE_ROTATION_DEGREES
            friction = 1.5f
        }

        val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                flingX.setStartVelocity(velocityX)
                flingY.setStartVelocity(velocityY)
                flingX.start()
                flingY.start()
                return true
            }
        }

        val gestureDetector = GestureDetector(this,gestureListener)

        flight.setOnTouchListener { view, motionEvent ->
            view.performClick()
            gestureDetector.onTouchEvent(motionEvent)
        }

        // TODO: 28.07.2021 29.1. Circular Reveal

        Executors.newSingleThreadExecutor().execute {
            while (true) {
                if (content.isAttachedToWindow) {
                    isRevealed = !isRevealed
                    runOnUiThread {
                        show()
                    }
                    return@execute
                }
            }
        }


        fab.setOnClickListener {
            isRevealed = !isRevealed
            if (isRevealed) show() else hide()
        }

    }

    private fun show() {
        val x: Int = fab.x.roundToInt() + fab.width / 2
        val y: Int = fab.y.roundToInt() + fab.height / 2

        val startRadius = 0f
        val endRadius = hypot(content.height.toFloat(), content.width.toFloat())

        val animation =
            ViewAnimationUtils.createCircularReveal(content, x, y, startRadius, endRadius)
        animation.apply {
            interpolator = BounceInterpolator()
            duration = 1000
            doOnStart {
                content.visibility = View.VISIBLE
                text.visibility = View.INVISIBLE
            }
        }.start()
    }

    private fun hide() {
        val x: Int = fab.x.roundToInt() + fab.width / 2
        val y: Int = fab.y.roundToInt() + fab.height / 2
        val startRadius = hypot(content.height.toFloat(), content.width.toFloat())
        val endRadius = 0f

        val animation =
            ViewAnimationUtils.createCircularReveal(content, x, y, startRadius, endRadius)

        animation.apply {
            interpolator = AnticipateInterpolator()
            duration = 1000
            doOnEnd {
                content.visibility = View.INVISIBLE
                text.visibility = View.VISIBLE
            }
        }.start()
    }

}