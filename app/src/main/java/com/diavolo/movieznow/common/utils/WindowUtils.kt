package com.diavolo.movieznow.common.utils

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.diavolo.movieznow.R

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
object WindowUtils {
    fun setupLightTheme(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 26) {
            setLightNavigationBar(activity)
        }
    }

    @TargetApi(26)
    private fun setLightNavigationBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        activity.window.decorView.systemUiVisibility = flags
        activity.window.navigationBarColor = Color.WHITE
    }

    /**
     * Some devices need to clear light status and navigation bar by default
     */
    fun setupDarkTheme(activity: Activity) {
        if (Build.VERSION.SDK_INT >= 26) {
            setDarkNavigationBar(activity)
        }
    }

    @TargetApi(26)
    private fun setDarkNavigationBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        activity.window.decorView.systemUiVisibility = flags
        activity.window.navigationBarColor = ContextCompat.getColor(activity, R.color.colorBackground)
    }

    fun setToolbarTopPadding(toolbar: Toolbar) {
        toolbar.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(top = insets.systemWindowInsetTop)
            insets
        }
    }

    fun clearStatusBar(activity: Activity) {
        var flags = activity.window.decorView.systemUiVisibility
        flags = flags and View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv()
        activity.window.decorView.systemUiVisibility = flags
    }

    fun setTransparentStatusBar(activity: Activity) {
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }
}