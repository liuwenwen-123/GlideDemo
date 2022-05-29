package com.lww.glidedemo.glide.fragment
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
class GlideFragmentManage   : Fragment()  {
     var  mLogTag="lww-FragmentManager"
     var mLifecycleCallback:LifecycleCallback?=null
     fun setlifecycle(lifecycleCallback: LifecycleCallback){
         mLifecycleCallback=lifecycleCallback;
     }
    override fun onStart() {
        super.onStart()
        Log.d(mLogTag,"onStart")
        mLifecycleCallback?.glideInitAction()
    }
    override fun onStop() {
        super.onStop()
        Log.d(mLogTag,"onStop")
        mLifecycleCallback?.glideStopAction()
    }
    override fun onDestroy() {
        super.onDestroy()
        mLifecycleCallback?.glideRecycleAction()
        Log.d(mLogTag,"onDestroy")
    }





    override fun onPause() {
        super.onPause()
        Log.d(mLogTag,"onPause")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(mLogTag,"onDestroyView")
    }



    override fun onDetach() {
        super.onDetach()
        Log.d(mLogTag,"onDetach")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(mLogTag,"onActivityCreated")
    }
}