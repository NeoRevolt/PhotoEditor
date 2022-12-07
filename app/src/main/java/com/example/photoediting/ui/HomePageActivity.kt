package com.example.photoediting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import example.photoediting.R

class HomePageActivity : AppCompatActivity() {

    private lateinit var btnFromRemote: CardView
    private lateinit var btnFromGallery: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        btnFromRemote = findViewById(R.id.btn_searchFromServer)
        btnFromGallery = findViewById(R.id.btn_searchFromGallery)

        btnFromRemote.setOnClickListener {
            val intent = Intent(this, RemoteImagesActivity::class.java)
            startActivity(intent)
        }

        btnFromGallery.setOnClickListener {
            Intent(this, EditImageActivity::class.java).also {
                it.putExtra(EditImageActivity.EXTRA_REQ, "gallery")
                startActivity(it)
            }
        }
    }

    companion object{
        const val EXTRA_REQ = "extra_req"
    }
}