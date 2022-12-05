package com.example.photoediting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import example.photoediting.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUrl = intent.getStringExtra(EXTRA_PHOTO)

        Glide.with(this)
            .load(photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(binding.ivSelectedImage)

        binding.btnSelectedImage.setOnClickListener {
//            val intent = Intent(this, EditImageActivity::class.java)
//            startActivity(intent)

            Intent(this, EditImageActivity::class.java).also {
                it.putExtra(EditImageActivity.EXTRA_PHOTO, photoUrl)
                startActivity(it)
            }
        }
    }

    companion object {
        const val EXTRA_PHOTO = "extra_photo"
    }

}