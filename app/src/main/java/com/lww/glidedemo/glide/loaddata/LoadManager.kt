package com.lww.glidedemo.glide.loaddata

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object LoadManager : Runnable {

    var mPath: String? = null
    var mListener: ResponseListener? = null
    var mContext: Context? = null
    fun loadImg(path: String, context: Context, listener: ResponseListener):EngineResource?{
        mPath=path
        mListener=listener
        mContext=context
        ThreadPoolExecutor(0,Int.MAX_VALUE,60,TimeUnit.SECONDS,
            SynchronousQueue<Runnable>()
        ).execute(this)

        return null
    }

    @SuppressLint("StaticFieldLeak")
    override fun run() {
        val fos: FileOutputStream? = null
        var inputStream: InputStream? = null
        var bitmap: Bitmap? = null
//        创建一个url 对象
        try {
            val url = URL(mPath)
            //            使用HttpURLConnection 通过 创建一个url读取数据
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            inputStream = urlConnection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
             var engineResource=EngineResource();
            engineResource.imgBitmap=bitmap
            Handler(Looper.getMainLooper()).post(Runnable{
                mListener?.let {
                    it.success(engineResource)
                }
            })


        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post(Runnable{
                mListener?.let {
                    it.fail( )
                }
            })

            e.printStackTrace()
        } finally {
            if (inputStream!= null) {
                try {
                    inputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (fos != null) {
                    try {
                        fos.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

   /* private fun downLoad(uri: String): Bitmap? {

        return bitmap
    }*/
}