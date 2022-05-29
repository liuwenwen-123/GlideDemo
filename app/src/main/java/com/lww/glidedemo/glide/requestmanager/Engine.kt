package com.lww.glidedemo.glide.requestmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.ImageView
import com.lww.glidedemo.glide.cache.ActiveCache
import com.lww.glidedemo.glide.cache.MemoryCache
import com.lww.glidedemo.glide.cache.disklrucache.DiskLruCache
import com.lww.glidedemo.glide.cache.disklrucache.DiskLruCache.Snapshot
import com.lww.glidedemo.glide.fragment.LifecycleCallback
import com.lww.glidedemo.glide.loaddata.EngineResource
import com.lww.glidedemo.glide.loaddata.LoadManager
import com.lww.glidedemo.glide.loaddata.ResponseListener
import java.io.File
import java.io.OutputStream
import kotlin.math.log


class Engine  : LifecycleCallback {
    lateinit  var mPath: String
     lateinit var mGlideContent: Context
    var mkey: String? = null
    var mImageView: ImageView? = null

    var mActiveCache: ActiveCache? = null // 活动缓存  用户可见的缓存

    //  内存缓存   可以多次引用 例如 a 页面 和b页面 都使用到了 某个图片   a页面推出后不应该销毁内存缓存
    var mMemoryCahae: MemoryCache? = null

    var diskLruCache: DiskLruCache? = null
    init {
        if (mActiveCache == null) {
            mActiveCache = ActiveCache()
        }
        if (mMemoryCahae == null) {
//             内存缓存设置 60m
            mMemoryCahae = MemoryCache(1024 * 1024 * 60)
        }

        if (diskLruCache == null) {
            diskLruCache = DiskLruCache.open(File("data/data/com.lww.glidedemo/imgcache"), 1, 1, 10 * 1024 * 1024)
        }
    }


    fun loadValueInitActivity(path: String, context: Context) {
        mPath = path
        mGlideContent = context
        mkey = Key(path).getKey()

    }


    fun into(img: ImageView) {
        mImageView = img
//        开启加载
        val cacheAciton = cacheAciton()

         if(cacheAciton!=null){
             if(cacheAciton.imgBitmap!=null){
                 mImageView?.let {
                     it.setImageBitmap(cacheAciton.imgBitmap)
                 }
             }
         }


    }


    fun cacheAciton(): EngineResource? {
        //1: 先从活动 缓存找

        var resource = mkey?.let {
            mActiveCache?.get(it)
        }
        if(resource!=null){
            resource.useAction()
            Log.d("lww ","从活动 缓存找 到了")
            return resource
        }


//       2从内存 缓存找
        resource = mMemoryCahae?.get(mkey)
        if(resource!=null){
            Log.d("lww ","从内存 缓存找 到了")
            mMemoryCahae?.shoudonRemove(mkey!!)
            mActiveCache?.put(mkey!!, resource!!)
            resource?.useAction()
            return resource
        }
       /* resource?.let {
//              1：从内存缓存移除  2放到活动缓存
            mkey?.let { key ->
                Log.d("lww ","从内存 缓存找 到了")
                mMemoryCahae?.shoudonRemove(key)
                mActiveCache?.put(key, resource!!)
//           缓存使用 +1
                resource?.useAction()
            }

        }
        if(resource!=null){
            return resource
        }*/

//          3  从磁盘获取

     var   snapshot=  diskLruCache?.get(mkey)
      var inputStream=  snapshot?.getInputStream(0)
       var bitmap=  BitmapFactory.decodeStream(inputStream);
        if (bitmap != null) {
//             添加到 活到缓存
//            不需熬 添加到 内存缓存  因为内存缓存只 缓存看不见的数据
            mkey?.let {
                Log.d("lww ","从磁盘 缓存找 到了")
                resource=EngineResource()
                resource?.imgBitmap=bitmap

                mActiveCache?.put(it, resource!!)
                Log.d("lww ","从  mActiveCache.hashCode()  ${mActiveCache.hashCode()}")
                resource?.useAction()
            }
            return resource
        }

//         从 网络下载

          LoadManager.loadImg(mPath,mGlideContent,object :ResponseListener{
              override fun success(engineResource: EngineResource) {
                  if(engineResource!=null){
                      Log.d("lww ","从网络找 到了")
                    saveCache(engineResource)
                     resource=engineResource
                  }

              }

              override fun fail() {
              }

          })

        return  resource


    }

    private fun saveCache(engineResource: EngineResource) {
        var snapshot: Snapshot? = null
        var os: OutputStream? = null
        engineResource.key=mkey
//         保存到磁盘缓存
            snapshot = diskLruCache?.get(engineResource.key)

        if(snapshot==null){
            val edit = diskLruCache?.edit(engineResource.key)
            os = edit?.newOutputStream(0);
            engineResource.imgBitmap?.compress(Bitmap.CompressFormat.JPEG, 50, os);

            edit?.commit();
        }
        snapshot?.let {
            snapshot.close()
        }
        os?.let {
            it.close()
        }

//         保存到活动缓存

        mkey?.let { mActiveCache?.put(it,engineResource) }
        engineResource.useAction()

    }

    override fun glideInitAction() {
    }

    override fun glideStopAction() {
    }

    override fun glideRecycleAction() {
        mActiveCache?.let {
            var resource = mkey?.let {  key->
                mActiveCache?.get(key)
            }

            if(resource!=null){
                mMemoryCahae?.put(mkey,resource)
                Log.d("lww ","从活动 缓存保存到  内存缓存中  ${resource.imgBitmap?.byteCount}")
            }

            it.closeActive()
        }
    }
}