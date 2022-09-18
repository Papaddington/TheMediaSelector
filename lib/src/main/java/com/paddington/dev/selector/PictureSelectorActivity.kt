package com.paddington.dev.selector

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.githang.statusbar.StatusBarCompat
import com.paddington.dev.MediaSelector
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.R
import com.paddington.dev.bean.MediaEntity

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PictureSelectorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarCompat.setStatusBarColor(this, Color.BLACK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = Color.BLACK
        }
        setContentView(R.layout.base_ms_activity_picture_selector)

        supportFragmentManager.beginTransaction()
            .add(R.id.llContainer, PictureSelectorFragment().apply {
                setMediaSelectListener { mediaList ->
                    setMediaResult(mediaList)
                }
            })
            .commitNowAllowingStateLoss()
    }

    private fun setMediaResult(mediaList: List<MediaEntity>) {
        val intent = Intent().apply {
            putExtras(bundleOf(MediaSelector.KEY_MEDIA_RESULT to mediaList))
        }
        setResult(Activity.RESULT_OK, intent)
        if (MediaSelectorConfig.autoClose) {
            finish()
        }
    }
}