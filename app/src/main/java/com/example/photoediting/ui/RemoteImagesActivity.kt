package com.example.photoediting.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoediting.EditImageActivity
import com.example.photoediting.remote.ApiConfig
import com.example.photoediting.remote.ApiService
import com.example.photoediting.remote.ListStoryItem
import com.example.photoediting.ui.adapters.RemoteImagesAdapter
import example.photoediting.databinding.ActivityRemoteImagesBinding

class RemoteImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemoteImagesBinding
    private lateinit var adapter: RemoteImagesAdapter

    private lateinit var sharedPreferences: SharedPreferences
    val listStoryItem = ArrayList<ListStoryItem>()

    private var SHARED_PREF_NAME = "mypref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoteImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE)

//        setStory()
        initRv()
    }

//    private fun setStory() {
//        showLoading(true)
//        val service = ApiConfig.getApiService(this).getAllStory()
//    }

    private fun initRv() {
        adapter = RemoteImagesAdapter()
        binding.apply {
            rvImages.layoutManager = LinearLayoutManager(this@RemoteImagesActivity)
            rvImages.setHasFixedSize(true)
            rvImages.adapter = adapter

            adapter.setOnItemClickCallback(object : RemoteImagesAdapter.OnItemClickCallback{
                override fun onItemClicked(data: ListStoryItem) {
                    Toast.makeText(
                        this@RemoteImagesActivity,
                        data.description,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }



    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



}