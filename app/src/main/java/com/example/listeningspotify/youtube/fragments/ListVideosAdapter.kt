package com.example.listeningspotify.youtube.fragments


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.listeningspotify.R
import com.example.listeningspotify.youtube.Video

import com.squareup.picasso.Picasso


class ListVideosAdapter(
    private val videos: MutableList<Video>,
    private val listener: ListVideoListener,
    private val color: Int
) : RecyclerView.Adapter<ListVideosAdapter.VideoViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_item_video, parent, false)

        return VideoViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return videos.count()
    }


    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(videos[position], listener, color)
    }

    interface ListVideoListener {
        fun onVideoClick(video: Video)
    }

    class VideoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(video: Video, listener: ListVideoListener, color: Int) {
            itemView.findViewById<TextView>(R.id.videoTitle)?.text = video.title
            itemView.findViewById<TextView>(R.id.videoTitle)?.setTextColor(color)

            itemView.findViewById<TextView>(R.id.videoOther)?.text = video.channelTitle
            itemView.findViewById<TextView>(R.id.videoOther)?.setTextColor(color)

            val image = itemView.findViewById<ImageView>(R.id.videoImage)
            image?.setOnClickListener {
                listener.onVideoClick(video)
            }
            setImage(image!!, video);

        }

        private fun setImage(imageView: ImageView, video: Video) {
            val picasso = Picasso.get()
            picasso.load(video.thumbnail)
                .into(imageView)

        }
    }
}