package com.diavolo.movieznow.common.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import com.diavolo.movieznow.R
import com.google.android.material.appbar.MaterialToolbar

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class CenteredToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.toolbarStyle
) : MaterialToolbar(context, attrs, defStyleAttr) {

    private val titleView = AppCompatTextView(getContext())

    init {
        titleView.maxLines = 1
        titleView.ellipsize = TextUtils.TruncateAt.END

        TextViewCompat.setTextAppearance(titleView, androidx.appcompat.R.style.TextAppearance_AppCompat_Widget_ActionBar_Title)

        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER
        titleView.layoutParams = lp

        addView(titleView)
    }

    override fun setTitle(title: CharSequence) {
        titleView.text = title
    }

    override fun setTitle(@StringRes resId: Int) {
        val s = resources.getString(resId)
        title = s
    }

}