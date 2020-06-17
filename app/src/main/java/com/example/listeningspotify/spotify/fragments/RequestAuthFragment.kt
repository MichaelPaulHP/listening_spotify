package com.example.listeningspotify.spotify.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.listeningspotify.R
import com.example.listeningspotify.config.SpotifyCredentials
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [RequestAuthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RequestAuthFragment : Fragment() {
    // TODO: Rename and change types of parameters
    val REQUEST_CODE:Int =2100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.startAuthBtn).setOnClickListener {
            showSpotifyAuthorization()
        }
    }
    private fun showSpotifyAuthorization(){
        val builder= AuthorizationRequest
            .Builder(
                SpotifyCredentials.CLIENT_ID.key,
                AuthorizationResponse.Type.TOKEN,
                SpotifyCredentials.REDIRECT_URL.key
            );
        builder.setScopes(arrayOf("app-remote-control"))
        val request = builder.build();
        AuthorizationClient.openLoginActivity(this.activity, REQUEST_CODE, request);
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RequestAuthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(message:String) =
            RequestAuthFragment().apply {

            }
    }
}