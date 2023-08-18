package com.diavolo.movieznow.base

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.diavolo.movieznow.common.utils.WindowUtils.clearStatusBar
import com.diavolo.movieznow.common.utils.WindowUtils.setToolbarTopPadding
import com.diavolo.movieznow.common.utils.WindowUtils.setTransparentStatusBar
import com.diavolo.movieznow.common.utils.WindowUtils.setupDarkTheme
import com.diavolo.movieznow.common.utils.WindowUtils.setupLightTheme

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar(this)
        applyTheme()
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)

        toolbar?.let {
            setToolbarTopPadding(toolbar)
        }
    }

    fun clearStatusBar() {
        clearStatusBar(this)
    }

    private fun applyTheme() {
        when(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                setupDarkTheme(this)
            }
            else -> {
                setupLightTheme(this)
            }
        }
    }
}