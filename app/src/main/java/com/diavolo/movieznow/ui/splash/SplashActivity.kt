package com.diavolo.movieznow.ui.splash

import android.os.Bundle
import com.diavolo.movieznow.base.BaseActivity
import com.diavolo.movieznow.common.navigation.navigateToHome

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navigateToHome()
    }
}