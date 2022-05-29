package com.lww.glidedemo.glide.cache

import android.os.Build
import android.util.LruCache
import com.lww.glidedemo.glide.loaddata.EngineResource

class MemoryCache  : LruCache<String,EngineResource>{

    var shoudonRemoved:Boolean = false

    constructor(maxSize:Int):super(maxSize)

//      手动移除
     fun shoudonRemove(key:String): EngineResource? {
         shoudonRemoved=true
         val resource = remove(key)
         shoudonRemoved=false
         return resource
     }

    /**
     * 表示被移除了
     * 1：重复的key'
     * 2:最近最少使用的元素移除 （ 当元素 大于了 maxsize）
     */
    override fun entryRemoved(
        evicted: Boolean,
        key: String?,
        oldValue: EngineResource?,
        newValue: EngineResource?
    ) {
        super.entryRemoved(evicted, key, oldValue, newValue)
    }
    override fun sizeOf(key: String?, value: EngineResource?): Int {
        val imgBitmap = value?.imgBitmap
        val sdkInt = Build.VERSION.SDK_INT
        if(sdkInt>=Build.VERSION_CODES.KITKAT){
            if (imgBitmap != null) {
                return imgBitmap.allocationByteCount//被复用 Bitmap真实占用的内存大小。
            }
        }

        val byteCount = imgBitmap?.byteCount// 图片大小


        return byteCount?.let { it }?:0
    }




}