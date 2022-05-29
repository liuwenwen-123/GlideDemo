package com.lww.glidedemo.glide.loaddata

import android.graphics.Bitmap
import android.util.Log

class EngineResource {
    var  imgBitmap:Bitmap?=null  //  位图
    var key:String?=null  //  图片 唯一标识
    var count:Int=0  //  图片引用次数



    fun  useAction(){
        imgBitmap?.let {
            if(!it.isRecycled){
                count++
            }
        }

    }

}