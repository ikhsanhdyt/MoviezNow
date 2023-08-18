package com.diavolo.movieznow.common.navigation

import android.app.Activity
import android.content.Intent
import com.diavolo.movieznow.ui.home.HomeActivity

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
fun Activity.navigateToHome() {
    this.startActivity(Intent(this, HomeActivity::class.java))
    this.finish()
}