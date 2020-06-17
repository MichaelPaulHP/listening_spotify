package com.example.listeningspotify.youtube.fragments

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listeningspotify.R
import com.example.listeningspotify.spotify.PlayerViewModel
import com.example.listeningspotify.youtube.Video
import com.example.listeningspotify.youtube.YouTubeFailure
import com.example.listeningspotify.youtube.YouTubeSearching
import com.example.listeningspotify.youtube.YouTubeVideosFound


class VideosFragment : Fragment(),ListVideosAdapter.ListVideoListener {


    private val vm: PlayerViewModel by activityViewModels()
    private lateinit var status: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: ListVideosAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_videos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.status = view.findViewById(R.id.statusTextView)
        this.recyclerView = view.findViewById(R.id.videosRecyclerView)
        setUpRecyclerView()
        startListenersFromYouTube()
    }

    private fun setUpRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = ListVideosAdapter(this.vm.videos, this,asyncColor())
        recyclerView.adapter = mAdapter
    }

    private fun showRecyclerView() {
        this.recyclerView.visibility = View.VISIBLE
        this.status.visibility = View.GONE
    }

    private fun hideRecyclerView() {
        this.status.visibility = View.VISIBLE
        this.recyclerView.visibility = View.GONE
    }

    private fun startListenersFromYouTube() {
        this.vm.getYouTubeState().observe(this.viewLifecycleOwner, Observer {
            when (it) {
                is YouTubeVideosFound -> {
                    showRecyclerView()
                    this.mAdapter.notifyDataSetChanged()
                }
                is YouTubeSearching -> {
                    hideRecyclerView()
                    this.status.text = "Searching in YouTube"
                }
                is YouTubeFailure -> {
                    hideRecyclerView()
                    this.status.text = it.message
                }
            }
        })
    }

    private fun watchYoutubeVideo(context: Context, id: String) {
        val appIntent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
        val webIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://www.youtube.com/watch?v=$id")
        )
        try {
            context.startActivity(appIntent)
        } catch (ex: ActivityNotFoundException) {
            context.startActivity(webIntent)
        }
    }

    override fun onVideoClick(video: Video) {
        if (this.context != null) {
            watchYoutubeVideo(this.requireContext(), video.id);
            this.vm.addVideoToHistory(Video.toVideoEntityDB(video))
        }
        Log.e("PLAYER", "onVideoClick")
        Log.e("PLAYER", video.title)
    }


    private fun asyncColor():Int{
        return Color.parseColor("#232830");
        //return 500018;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           return requireActivity().resources.getColor(R.color.colorPrimary, requireActivity().getTheme());
        }else {
            return requireActivity().resources.getColor(R.color.colorPrimary)
        }*/
    }

    companion object {
        fun newInstance() =
            VideosFragment().apply {
            }
    }

}