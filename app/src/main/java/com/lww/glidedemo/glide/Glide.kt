package com.lww.glidedemo.glide

import androidx.fragment.app.FragmentActivity
import com.lww.glidedemo.glide.requestmanager.RequestManager

class Glide {

     companion object{
         var requestManager: RequestManager? =null
         fun with(activity: FragmentActivity):RequestManager{
            if(requestManager==null){
                requestManager=RequestManager(activity)
            }

             return requestManager as RequestManager
         }
     }



}