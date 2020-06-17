package com.example.listeningspotify.spotify

import android.content.Context
import com.example.listeningspotify.config.SpotifyCredentials
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

class SpotifyRemote()  {
    private var spotifyAppRemote :SpotifyAppRemote?=null
    fun getParams():ConnectionParams {

            return  ConnectionParams
                .Builder(SpotifyCredentials.CLIENT_ID.key)
                .setRedirectUri(SpotifyCredentials.REDIRECT_URL.key)
                //.setAuthMethod(ConnectionParams.AuthMethod.APP_ID)
                .build()

    }
    fun connect(context: Context,listener:Connector.ConnectionListener ){
        SpotifyAppRemote.connect(context,getParams(),listener)
    }

     /*fun onFailure(p0: Throwable?) {

    }

     fun onConnected(p0: SpotifyAppRemote?) {
        this.spotifyAppRemote=p0
        this.getListeningTrack()
    }
    private fun getListeningTrack(){
        if(this.spotifyAppRemote!=null){

            this.spotifyAppRemote!!.playerApi.subscribeToPlayerState().setEventCallback {
                val track:Track =it.track
            }

        }
    }*/
}