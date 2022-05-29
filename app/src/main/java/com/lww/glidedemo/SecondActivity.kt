package com.lww.glidedemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.lww.glidedemo.glide.Glide

class SecondActivity : AppCompatActivity() {
    var url = "http://qzonestyle.gtimg.cn/qzone/app/weishi/client/testimage/64/1.jpg"
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        var img=  findViewById<ImageView>(R.id.image)
        Glide.with(this).load(url).into(img);
    }

    fun loadimg2(view: View) {
        var img2=  findViewById<ImageView>(R.id.image2)
        Glide.with(this).load(url).into(img2);
    }
}