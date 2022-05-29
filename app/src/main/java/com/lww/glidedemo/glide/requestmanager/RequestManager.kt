package com.lww.glidedemo.glide.requestmanager

import androidx.fragment.app.FragmentActivity
import com.lww.glidedemo.glide.fragment.GlideFragmentManage
import com.lww.glidedemo.glide.fragment.LifecycleCallback

class RequestManager  {
    var mFragmentActivity: FragmentActivity? = null
    val FRAGMENT_NAME = "fragment_activity_name"
     var engine:Engine?=null
     init {
         if(engine==null){
             engine= Engine()
         }

     }

    constructor(activity: FragmentActivity) {
        mFragmentActivity = activity
        val supportFragmentManager = mFragmentActivity!!.supportFragmentManager
//       todo    防止 fragment 重复创建
        val findFragmentByTag = supportFragmentManager.findFragmentByTag(FRAGMENT_NAME)
        if (findFragmentByTag == null) {
            var fragment = GlideFragmentManage()
            engine?.let { fragment.setlifecycle(it) }
            supportFragmentManager.beginTransaction().add(fragment, FRAGMENT_NAME)
             .commitAllowingStateLoss()
        }

    }

    fun  load(path:String):Engine{
        mFragmentActivity?.let {
            engine?.loadValueInitActivity(path, it)
        }
        return engine!!

    }




}