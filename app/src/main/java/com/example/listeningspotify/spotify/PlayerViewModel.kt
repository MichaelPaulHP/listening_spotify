package com.example.listeningspotify.spotify

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listeningspotify.history.MyRoomDataBase
import com.example.listeningspotify.history.VideoEntity
import com.example.listeningspotify.youtube.*

import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.error.*
import com.spotify.protocol.types.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class PlayerViewModel(application: Application) : Connector.ConnectionListener,
    AndroidViewModel(application) {

    private val state = MutableLiveData<PlayerState>()
    private val events = MutableLiveData<PlayerEvent>()
    private val youTubeState = MutableLiveData<YouTubeState>()

    private val spotifyRemote = SpotifyRemote()
    private lateinit var spotifyAppRemote:SpotifyAppRemote
    var videos = mutableListOf<Video>()

    private val db= MyRoomDataBase.getDatabase(application);
    val myHistory : LiveData<List<VideoEntity>> =
        this.db.videoDao().getAll()


    fun addVideoToHistory(video: VideoEntity){
        viewModelScope.launch (Dispatchers.IO) {

                db.videoDao().insert(video)
        }
    }
    fun deleteVideoFromHistory(video: VideoEntity){
        viewModelScope.launch (Dispatchers.IO) {

            db.videoDao().delete(video)
        }
    }
    fun initEventsListeners() {
        initListeners()
        this.events.value = PlayerStarted()
    }

    private fun initListeners() {
        Log.e("PLAYER", "initListeners");
        this.events.observeForever {

            when (it) {
                is PlayerStarted -> {
                    this.state.value = PlayerNotFound();
                    this.spotifyRemote.connect(this.getApplication(), this);
                }
                is PlayerTrackSelected -> {
                    this.findInYouTube(it.track)
                }
                is PlayerIsAuthorized->{
                    this.spotifyRemote.connect(this.getApplication(), this);
                    this.state.value=PlayerWaitTrack();
                }
                is PlayerUserDeniedAuthorization->{
                    this.state.value=PlayerNotAuthorize();
                }
                is PlayerIsFailed->{
                    this.state.value=PlayerLoadFailure("Error in Authorization")
                }
            }
        }
    }

    override fun onFailure(e: Throwable?) {
        if (e != null) {

            val message = e.message;
            when (e) {
                is UserNotAuthorizedException -> {
                    this.state.value = PlayerNotAuthorize()
                }
                is CouldNotFindSpotifyApp -> {
                    this.state.value =
                        PlayerLoadFailure("A Spotify app that supports App Remote was not found on the device")
                }
                is OfflineModeException -> {
                    this.state.value = PlayerLoadFailure("Spotify app to be in offline mode")
                }
                is SpotifyDisconnectedException -> {
                    this.state.value = PlayerLoadFailure("The Spotify app was/is disconnected")
                }
                is UnsupportedFeatureVersionException -> {
                    this.state.value =
                        PlayerLoadFailure(" Spotify app and the current version of App Remote is not compatible")
                }
                else -> {
                    val err=e.cause?.message
                    this.state.value = PlayerLoadFailure(err?:"Something went wrong. please restart app")
                }
            }
        }
    }

    override fun onConnected(spotifyAppRemote: SpotifyAppRemote?) {

        if (spotifyAppRemote != null) {
            this.spotifyAppRemote=spotifyAppRemote
            spotifyAppRemote.playerApi.subscribeToPlayerState().setEventCallback {
                val track = it.track;

                if (it.isPaused) {
                    this.state.value = PlayerIsPaused();
                }
                if (track != null) {
                    Log.e("PLAYER", "new Tracks");
                    this.state.value = PlayerIsPlaying(track);
                }
                if (track==null){
                    this.state.value = PlayerLoadFailure("Track not found");
                }
            }
        }
    }

    private fun findInYouTube(track: Track) {
        this.youTubeState.value = YouTubeSearching();
        try {


        val q = this.getTrackData(track)
        val videosFound=ServiceBuilder.buildYouTubeService().searchVideos(q);

        GlobalScope.launch {
            val response=videosFound.awaitResponse()
            if(response.isSuccessful){
                val resBody=response.body()
                if(resBody!=null){

                    val youTubeResponse:YouTubeResponse= resBody

                    youTubeResponse.items.reversed().forEach {
                        videos.add(0,Video.fromVideoEntity(it))
                    }
                    withContext(Dispatchers.Main){
                        youTubeState.postValue(YouTubeVideosFound(videos))
                    }
                }


                //youTubeState.value = YouTubeVideosFound(videos);
            }else{
                val err=response.message().toString()
                youTubeState.postValue(YouTubeFailure(err))
                //youTubeState.value = YouTubeFailure(err);
            }

        }
        }catch (e:Exception){
            this.youTubeState.value = YouTubeFailure(e.message?:"please, restart app");
        }
    }
    private fun getTrackData(track: Track):String{
        try{
            var artist="";
            if(track.artist!=null){
                artist=track.artist.name;
            }
            /*else{if(track.artists!=null){
                artist= track.artists[0].name;
            }}*/
            val q= track.name + " "+artist;
            return q
        }catch (e:Exception){
            throw Exception("this is not a track")
        }

    }

    fun getState(): MutableLiveData<PlayerState> {
        return this.state;
    }
    fun getYouTubeState(): MutableLiveData<YouTubeState> {
        return this.youTubeState;
    }
    fun addEvent(event: PlayerEvent) {
        this.events.value = event
    }

    override fun onCleared() {
        super.onCleared()
        SpotifyAppRemote.disconnect(this.spotifyAppRemote)
    }
}