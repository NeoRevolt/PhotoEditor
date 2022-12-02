package com.example.photoediting.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.photoediting.remote.ListStoryItem
import example.photoediting.databinding.ItemRemoteImagesBinding

class RemoteImagesAdapter : RecyclerView.Adapter<RemoteImagesAdapter.ViewHolder>() {

    private val list = ArrayList<ListStoryItem>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setList(story: ArrayList<ListStoryItem>) {
        list.clear()
        list.addAll(story)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(private val binding: ItemRemoteImagesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem){
            binding.apply {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .fitCenter()
                    .into(imgItemPhoto)
                tvItemName.text = story.name
                tvItemDesk.text = story.description
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemRemoteImagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}