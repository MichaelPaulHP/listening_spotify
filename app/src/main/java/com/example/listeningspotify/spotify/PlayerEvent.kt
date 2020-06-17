package com.example.listeningspotify.spotify

import com.spotify.protocol.types.Track

abstract class PlayerEvent {

}

class PlayerTrackSelected(var track: Track) : PlayerEvent() {

}

class PlayerStarted : PlayerEvent() {

}
class PlayerUserDeniedAuthorization:PlayerEvent(){

}
class PlayerIsAuthorized:PlayerEvent(){}

class PlayerIsFailed:PlayerEvent(){}