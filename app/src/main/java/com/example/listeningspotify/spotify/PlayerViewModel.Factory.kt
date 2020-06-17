package com.example.listeningspotify.spotify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import android.app.Application

class PlayerViewModelFactory(private val app:Application) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PlayerViewModel(app) as T
    }
}