package com.lww.glidedemo.glide.cache

import android.util.Log
import com.lww.glidedemo.glide.loaddata.EngineResource
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

/**
 *  活动 缓存
 *  就是一个map
 */
class ActiveCache {
    /**
     *  我们在获取EngineResource时 ，有可能EngineResource中的bitmap 已经被回收了
    造成  mMapList获取时  为空
    所以 我们需要使用 ReferenceQueue 监听
    当 EngineResource 被回收时  会把 EngineResource 添加到ReferenceQueue 对列


     */

    var mReferenceQueue: ReferenceQueue<EngineResource>? = null
    var mIsCloseThread: Boolean = false
    var mMapList = hashMapOf<String, WeakReference<EngineResource>>()

    fun get(key: String): EngineResource? {
        var valueWeakReference = mMapList[key]
        if (valueWeakReference == null) {
            Log.d("lww ", "活动缓存没数据  ${mMapList.size}")
            return null
        } else {
            return valueWeakReference?.get()
        }

    }

    fun put(key: String, value: EngineResource) {
        mMapList[key] = WeakReference(value, getQueue())

    }


    fun getQueue(): ReferenceQueue<EngineResource>? {
        if (mReferenceQueue == null) {
            mReferenceQueue = ReferenceQueue()
        }
        Thread(Runnable {
            kotlin.run {
//                当 EngineResource 被回收的时候 我们需要把 滑动缓存中持有的 bitmap 回收
                while (!mIsCloseThread) {
                    mReferenceQueue?.let {
                        Log.d("lww ", "活动缓存湖被回收  ")
                        val remove = it.remove()
                        if ((remove.get() != null) && (remove.get()!!.imgBitmap != null)) {
                            remove.get()?.let { resource ->
                                resource.imgBitmap?.let { bitmap ->
                                    bitmap.recycle()
                                }
                            }
                        }
                        false


                    }


                }
            }
        }).start()

        return mReferenceQueue
    }


    //     清楚 活动缓存
    fun closeActive() {
        Log.d("lww ", "清除  活动缓存湖   ")
       /* mMapList.forEach {
            val value = it.value
            value.get()?.imgBitmap?.recycle()
        }*/
        mMapList.clear()
        mIsCloseThread=true
        System.gc()
    }


}