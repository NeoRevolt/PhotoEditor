package com.example.photoediting.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoediting.data.offline.LayoutViewModel
import com.example.photoediting.data.offline.entity.LayoutEntity
import com.example.photoediting.data.offline.room.LayoutDao
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

    private lateinit var mLayoutModel: LayoutViewModel
    private val result =
        MediatorLiveData<com.example.photoediting.data.Result<List<LayoutEntity>>>()

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
        result.value = com.example.photoediting.data.Result.Loading
        showLoading(true)
        mLayoutModel = ViewModelProvider(this).get(LayoutViewModel::class.java)
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
                        listStoryItem.forEach {
//                            mLayoutModel.deleteLayoutFromDB()
                            mLayoutModel.addLayout(
                                LayoutEntity(
                                    it.photoUrl,
                                    it.createdAt,
                                    it.name,
                                    it.description,
                                    it.lon,
                                    it.id,
                                    it.lat
                                )
                            )
                        }

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
                    "No Connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
//        mLayoutModel.readAllLayout.observe(this, Observer { it ->
//            it.forEach {
//                listStoryItem.add(ListStoryItem(it.photoUrl,it.createdAt,it.name,it.description,it.lon,it.id,it.lat))
//            }
//            adapter.setList(listStoryItem)
//        })
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