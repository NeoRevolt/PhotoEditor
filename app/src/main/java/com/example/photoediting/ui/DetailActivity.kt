package com.example.photoediting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import example.photoediting.databinding.ActivityDetailBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val photoUrl = intent.getStringExtra(EXTRA_PHOTO)
        val author = intent.getStringExtra(EXTRA_AUTHOR)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val date = intent.getStringExtra(EXTRA_DATE)


        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date2 = date?.let { dateFormat.parse(it) }
        val formatter2 = SimpleDateFormat("dd/MM/yy'  'HH:mm:ss")
        val dateStr = date2?.let { formatter2.format(it) }

        Glide.with(this)
            .load(photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .fitCenter()
            .into(binding.ivSelectedImage)
        binding.apply {
            tvAuthor.text = "Author : @$author"
            tvDesc.text = "Description : $desc"
            tvDate.text = "Created at : $dateStr"
        }

        binding.btnTextSelectTv.setOnClickListener {
//            val intent = Intent(this, EditImageActivity::class.java)
//            startActivity(intent)

            Intent(this, EditImageActivity::class.java).also {
                it.putExtra(EditImageActivity.EXTRA_PHOTO, photoUrl)
                it.putExtra(EditImageActivity.EXTRA_REQ, "remote")
                startActivity(it)
            }
        }
    }

    companion object {
        const val EXTRA_PHOTO = "extra_photo"
        const val EXTRA_AUTHOR = "extra_author"
        const val EXTRA_DESC = "extra_desc"
        const val EXTRA_DATE = "extra_date"
    }

}