package com.paddington.dev.base

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
object PictureSelectEventDispatcher {
    private val listenerList = arrayListOf<OnPictureSelectChangListener>()

    fun register(listener: OnPictureSelectChangListener) {
        listenerList.add(listener)
    }

    fun unRegister(listener: OnPictureSelectChangListener) {
        listenerList.remove(listener)
    }

    fun event(event: MediaSelectChangedEvent) {
        listenerList.forEach {
            it.onChanged(event)
        }
    }

    interface OnPictureSelectChangListener {
        fun onChanged(event: MediaSelectChangedEvent)
    }

    data class MediaSelectChangedEvent(
        val isSelect: Boolean,
        val mediaEntity: MediaEntity
    )
}