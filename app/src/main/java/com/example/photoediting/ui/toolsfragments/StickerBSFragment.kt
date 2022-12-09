package com.example.photoediting.ui.toolsfragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.photoediting.data.offline.IconViewModel
import com.example.photoediting.data.offline.entity.IconEntity
import com.example.photoediting.data.offline.room.IconsDao
import com.example.photoediting.data.remote.ApiConfig
import com.example.photoediting.data.remote.GetAllStoryResponse
import com.example.photoediting.data.remote.ListStoryItem
import example.photoediting.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StickerBSFragment : BottomSheetDialogFragment() {

    val listStoryItem = ArrayList<ListStoryItem>()
    private lateinit var mIconViewModel: IconViewModel


    private var mStickerListener: StickerListener? = null
    fun setStickerListener(stickerListener: StickerListener?) {
        mStickerListener = stickerListener
    }

    interface StickerListener {
        fun onStickerClick(bitmap: Bitmap?)
    }

    private val mBottomSheetBehaviorCallback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
//        setStory()
        super.setupDialog(dialog, style)
        val contentView = View.inflate(context, R.layout.fragment_bottom_sticker_emoji_dialog, null)
        dialog.setContentView(contentView)

        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior
        if (behavior != null && behavior is BottomSheetBehavior<*>) {
//            behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback)
            behavior.addBottomSheetCallback(mBottomSheetBehaviorCallback)
        }
        (contentView.parent as View).setBackgroundColor(resources.getColor(android.R.color.transparent))
        val rvEmoji: RecyclerView = contentView.findViewById(R.id.rvEmoji)
        val gridLayoutManager = GridLayoutManager(activity, 3)
        rvEmoji.layoutManager = gridLayoutManager
        val stickerAdapter = StickerAdapter()
        rvEmoji.adapter = stickerAdapter
        rvEmoji.setHasFixedSize(true)
//        rvEmoji.setItemViewCacheSize(stickerPathList.size)
//        rvEmoji.setItemViewCacheSize(listStoryItem.size)

        mIconViewModel = ViewModelProvider(this).get(IconViewModel::class.java)
        mIconViewModel.deleteIconFromDB()
        addIconToDatabase()
        mIconViewModel.readAllIcon.observe(this, Observer { icon->
            stickerAdapter.setData(icon)
        })
    }

    private fun addIconToDatabase(){
        mIconViewModel.addIcon(IconEntity(0,"Mouse","https://cdn-icons-png.flaticon.com/256/4392/4392452.png"))
        mIconViewModel.addIcon(IconEntity(0,"Fly","https://cdn-icons-png.flaticon.com/512/2849/2849909.png"))
        Toast.makeText(requireContext(),"Data have been added to DB",Toast.LENGTH_SHORT).show()
    }


    private fun setStory() {
        val service = ApiConfig.getApiService(this@StickerBSFragment.requireContext()).getAllStory(6, 0)
        service.enqueue(object : Callback<GetAllStoryResponse> {
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        listStoryItem.clear()
                        response.body()?.listStory?.let { listStoryItem.addAll(it) }
//                        listStoryItem.addAll(responseBody.listStory)
//                        adapter.setList(response.body()!!.listStory)
                        Log.d("Sticker Status : ", "Berhasil mendapatkan stiker")

                    } else {
                        Toast.makeText(
                            this@StickerBSFragment.requireContext(),
                            response.message(),
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                Toast.makeText(
                    this@StickerBSFragment.requireContext(),
                    "Gagal mendapatkan Sticker",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    inner class StickerAdapter : RecyclerView.Adapter<StickerAdapter.ViewHolder>() {
       private var iconList = emptyList<IconEntity>()

        fun setData(icon: List<IconEntity>){
            this.iconList = icon
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.row_sticker, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            // Load sticker image from remote url
            Glide.with(requireContext())
                    .asBitmap()
//                    .load(stickerPathList[position])
//                    .load(listStoryItem[position].photoUrl)
                    .load(iconList[position].iconUrl)
                    .into(holder.imgSticker)
        }

        override fun getItemCount(): Int {
//            return stickerPathList.size
            return iconList.size
//            return listStoryItem.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imgSticker: ImageView = itemView.findViewById(R.id.imgSticker)

            init {
                itemView.setOnClickListener {
                    if (mStickerListener != null) {
                        Glide.with(requireContext())
                                .asBitmap()
//                                .load(stickerPathList[layoutPosition])
                                .load(iconList[layoutPosition].iconUrl)
//                            .load(listStoryItem[layoutPosition].photoUrl)
                            .into(object : CustomTarget<Bitmap?>(256, 256) {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap?>?) {
                                        mStickerListener!!.onStickerClick(resource)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {}
                                })
                    }
                    dismiss()
                }
            }
        }

    }

    companion object {
        // Image Urls from flaticon(https://www.flaticon.com/stickers-pack/food-289)
        private val stickerPathList = arrayOf(
                "https://cdn-icons-png.flaticon.com/256/4392/4392452.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392455.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392459.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392462.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392465.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392467.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392469.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392471.png",
                "https://cdn-icons-png.flaticon.com/256/4392/4392522.png",
        )
        private val iconPestList = ArrayList<IconEntity>()
    }
}