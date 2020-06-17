package com.example.listeningspotify.spotify.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listeningspotify.R
import com.example.listeningspotify.spotify.PlayerIsPlaying
import com.example.listeningspotify.spotify.PlayerTrackSelected
import com.example.listeningspotify.spotify.PlayerViewModel
import com.example.listeningspotify.spotify.PlayerViewModelFactory
import com.spotify.protocol.types.Track


class PlayerFragment : Fragment(), View.OnClickListener {



    var track: Track? = null
    //var vm: PlayerViewModel? = null
    private val vm:PlayerViewModel by activityViewModels()

    private lateinit var trackName: TextView
    private lateinit var trackArtists: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view);

        //vm = ViewModelProvider(this, PlayerViewModelFactory(this.activity!!.application)).get(PlayerViewModel::class.java)
        initListeners()
    }
    private fun initUI(view: View){
        view.findViewById<Button>(R.id.youTubeBtn).setOnClickListener(this)

        trackName = view.findViewById(R.id.trackName)
        trackArtists = view.findViewById(R.id.trackArtists)
    }

    private fun initListeners(){
        this.vm.getState().observe(viewLifecycleOwner, Observer {
            when(it){
                is PlayerIsPlaying->{
                    if(!isEqualTrack(this.track,it.track)){
                        showTrack(it.track)
                        this.track=it.track;
                    }
                }
            }
        })
    }
    private fun isEqualTrack(myTrack:Track?,track: Track):Boolean{
        return myTrack == track;
    }
    private fun showTrack(track: Track) {
        this.trackName.text=track.name
        var artist="";
        if(track.artists!=null){
            track.artists.forEach {
                artist=artist+it.name.toString()+", "
            }
        }
        this.trackArtists.text=artist
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.youTubeBtn -> {

                track?.let {
                    this.vm.addEvent(PlayerTrackSelected(it))
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PlayerFragment().apply {

            }
    }

}


