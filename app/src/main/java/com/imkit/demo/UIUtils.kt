package com.imkit.demo

import android.graphics.Color
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat

class UIUtils {

    companion object {
        fun setFullScreen(window: Window, color: Int = Color.TRANSPARENT) {
            window.apply {
                WindowInsetsControllerCompat(this, decorView).apply {
                    isAppearanceLightStatusBars = true
                }
                statusBarColor = color
            }
        }
    }
}