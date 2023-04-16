package com.gitlab.nastyaka.hw8_movie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.gitlab.nastyaka.hw8_movie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var img: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        img = binding.imageAnimation
        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_anim)
        img.startAnimation(animation)
    }
}