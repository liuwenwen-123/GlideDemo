package com.lww.glidedemo.glide.loaddata

interface ResponseListener {

    fun  success(engineResource: EngineResource)
    fun  fail( )
}