package com.gitlab.nastyaka.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gitlab.nastyaka.chat.databinding.OpenedImageBinding

class OpenedImage : AppCompatActivity() {
    private lateinit var binding: OpenedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OpenedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val img = binding.imgHighres
        val backButton = binding.imgBack

        val link = intent.getStringExtra("link")
        Glide.with(this).load("http://213.189.221.170:8008/img/$link").into(img)

        backButton.setOnClickListener {
            finish()
        }
    }
}