package com.himanshu.toxtap.service

import android.content.Context
import android.graphics.PixelFormat
import android.view.*
import android.widget.FrameLayout
import com.himanshu.toxtap.data.PreferenceManager
import com.himanshu.toxtap.model.GestureType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.abs

class GestureOverlayManager(private val context: Context) {

    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlayView: View? = null
    private val prefManager = PreferenceManager(context)
    private val scope = CoroutineScope(Dispatchers.Main)

    fun showOverlay() {
        if (overlayView != null) return

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                @Suppress("DEPRECATION")
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
        )

        overlayView = FrameLayout(context).apply {
            // Set alpha to a very small value so it's practically invisible but can still receive touches
            // Or keep it transparent if background is null
            background = null
            setOnTouchListener(object : View.OnTouchListener {
                private val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onDown(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onDoubleTap(e: MotionEvent): Boolean {
                        handleGesture(GestureType.DOUBLE_TAP)
                        return true
                    }

                    override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                        if (e1 == null) return false
                        val diffY = e2.y - e1.y
                        val diffX = e2.x - e1.x
                        if (abs(diffX) > abs(diffY)) {
                            if (abs(diffX) > 100 && abs(velocityX) > 100) {
                                if (diffX > 0) {
                                    handleGesture(GestureType.SWIPE_RIGHT)
                                } else {
                                    handleGesture(GestureType.SWIPE_LEFT)
                                }
                                return true
                            }
                        } else if (abs(diffY) > 100 && abs(velocityY) > 100) {
                            if (diffY > 0) {
                                    handleGesture(GestureType.SWIPE_DOWN)
                            } else {
                                    handleGesture(GestureType.SWIPE_UP)
                            }
                            return true
                        }
                        return false
                    }
                })

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    return gestureDetector.onTouchEvent(event)
                }
            })
        }

        windowManager.addView(overlayView, params)
    }

    fun hideOverlay() {
        overlayView?.let {
            windowManager.removeView(it)
            overlayView = null
        }
    }

    private fun handleGesture(type: GestureType) {
        scope.launch {
            val action = prefManager.getGestureAction(type).first()
            ActionExecutor.execute(context, action)
        }
    }
}
