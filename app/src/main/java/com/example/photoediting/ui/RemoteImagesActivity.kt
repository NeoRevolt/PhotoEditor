package com.example.photoediting.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoediting.data.remote.ApiConfig
import com.example.photoediting.data.remote.GetAllStoryResponse
import com.example.photoediting.data.remote.ListStoryItem
import com.example.photoediting.ui.adapters.RemoteImagesAdapter
import example.photoediting.databinding.ActivityRemoteImagesBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        setStory()
        initRv()
    }

    private fun setStory() {
        showLoading(true)
        val service = ApiConfig.getApiService(this).getAllStory(30, 0)
        service.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                if (response.isSuccessful) {
                    showLoading(false)
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        response.body()?.listStory?.let { listStoryItem.addAll(it) }
//                        adapter.setList(response.body()!!.listStory)
                        adapter.setList(listStoryItem)

                        Toast.makeText(
                            this@RemoteImagesActivity,
                            "${listStoryItem.size} images have been loaded",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RemoteImagesActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                showLoading(false)
                Toast.makeText(
                    this@RemoteImagesActivity,
                    "Gagal mendapatkan Story",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun initRv() {
        adapter = RemoteImagesAdapter()
        binding.apply {
            rvImages.layoutManager = LinearLayoutManager(this@RemoteImagesActivity)
            rvImages.setHasFixedSize(true)
            rvImages.adapter = adapter
            adapter.setOnItemClickCallback(object : RemoteImagesAdapter.OnItemClickCallback {
                override fun onItemClicked(data: ListStoryItem) {
//                    Intent(this@RemoteImagesActivity, DetailActivity::class.java).also {
//                        it.putExtra(DetailActivity.EXTRA_PHOTO, data.photoUrl)
//                        it.putExtra(DetailActivity.EXTRA_AUTHOR, data.name)
//                        it.putExtra(DetailActivity.EXTRA_DESC, data.description)
//                        it.putExtra(DetailActivity.EXTRA_DATE, data.createdAt)
//                        startActivity(it)
                    Intent(this@RemoteImagesActivity, EditImageActivity::class.java).also {
                        it.putExtra(EditImageActivity.EXTRA_PHOTO, data.photoUrl)
                        it.putExtra(EditImageActivity.EXTRA_REQ, "remote")
                        startActivity(it)

                    }
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