package com.example.listeningspotify.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.example.listeningspotify.R

import com.squareup.picasso.Picasso

class ListHistoryAdapter(
    private var videos: List<VideoEntity>?,
    private val listener: ListVideoListener,
    private val color: Int
) : RecyclerView.Adapter<ListHistoryAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_video, parent, false)

        return VideoViewHolder(itemView)
    }
    fun setVideo(videos: List<VideoEntity>){
        this.videos=videos;
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return videos?.count()?:0
    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        videos?.get(position)?.let { holder.bind(it, listener, color) }
    }

    interface ListVideoListener {
        fun onVideoClick(video: VideoEntity)
        fun onVideoDeleteClick(video: VideoEntity)
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(video: VideoEntity, listener: ListVideoListener, color: Int) {
            itemView.findViewById<TextView>(R.id.videoTitle)?.text = video.title
            itemView.findViewById<TextView>(R.id.videoTitle)?.setTextColor(color)

            itemView.findViewById<TextView>(R.id.videoOther)?.text = video.channelTitle
            itemView.findViewById<TextView>(R.id.videoOther)?.setTextColor(color)

            val image = itemView.findViewById<ImageView>(R.id.videoImage)
            image?.setOnClickListener {
                listener.onVideoClick(video)
            }
            setImage(image!!, video);

            val btn=itemView.findViewById<Button>(R.id.deleteVideoBtn);
            btn.visibility=View.VISIBLE
            btn.setOnClickListener { listener.onVideoDeleteClick(video) }

        }

        private fun setImage(imageView: ImageView, video: VideoEntity) {
            //Glide.with(imageView.context).load(video.thumbnail).into(imageView)
            val picasso = Picasso.get()
            picasso.load(video.thumbnail)
                .into(imageView)
            /* Glide
    .with(myFragment)
    .load(url)
    .centerCrop()
    .placeholder(R.drawable.loading_spinner)
    .into(myImageView);*/
        }
    }
}