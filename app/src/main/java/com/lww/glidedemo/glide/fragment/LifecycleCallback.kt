package com.lww.glidedemo.glide.fragment

interface LifecycleCallback {
     fun glideInitAction()
    fun glideStopAction()
    fun glideRecycleAction()
}