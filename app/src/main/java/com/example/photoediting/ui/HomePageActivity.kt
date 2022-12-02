package com.example.photoediting.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.photoediting.EditImageActivity
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
            val intent = Intent(this, EditImageActivity::class.java)
            startActivity(intent)
        }

        btnFromGallery.setOnClickListener {
            val intent = Intent(this, EditImageActivity::class.java)
            startActivity(intent)
        }

    }
}