package com.example.listeningspotify.spotify

import com.spotify.protocol.types.Track

abstract class PlayerState {

}
class PlayerIsPlaying(var track:Track): PlayerState() {

}
class PlayerNotFound : PlayerState(){

}
class PlayerIsPaused: PlayerState(){

}
class PlayerLoadFailure(var message: String) :PlayerState(){

}
class PlayerIsSearching(var track: Track):PlayerState(){

}
class PlayerWaitTrack:PlayerState(){}
class PlayerNotAuthorize:PlayerState(){}
class PlayerIsDisconnected:PlayerState(){}
