package com.example.listeningspotify

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listeningspotify.history.ListHistoryAdapter
import com.example.listeningspotify.history.VideoEntity
import com.example.listeningspotify.spotify.*
import com.example.listeningspotify.spotify.fragments.ErrorFragment
import com.example.listeningspotify.spotify.fragments.PlayerFragment
import com.example.listeningspotify.spotify.fragments.PlayerNotFoundFragment
import com.example.listeningspotify.spotify.fragments.RequestAuthFragment
import com.example.listeningspotify.youtube.fragments.VideosFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ListHistoryAdapter.ListVideoListener {


    private val REQUEST_CODE: Int = 2100

    //var vm:PlayerViewModel?=null
    val vm: PlayerViewModel by viewModels()
    private lateinit var recyclerViewHistory: RecyclerView
    private lateinit var mAdapterHistory: ListHistoryAdapter
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>
    /*val vm:PlayerViewModel by lazy {
        ViewModelProvider(this,PlayerViewModelFactory()).get(PlayerViewModel::class.java)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBlackStatusBar()
        //vm=ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(PlayerViewModel::class.java)
        //vm=ViewModelProvider(this,PlayerViewModelFactory(this.application)).get(PlayerViewModel::class.java)

        initListeners()
        vm.initEventsListeners()
        initFragmentOfVideos()
        setUpRecyclerView()
        initListenerOfHistory()
        initBottonSheet()
    }

    private fun initListeners() {
        this.vm.getState().observe(this, Observer {

            when (it) {
                is PlayerNotFound -> {
                    showFragmentPlayerNotFound()
                }
                is PlayerIsPlaying -> {
                    showFragmentOfPlayer(it)
                }
                is PlayerLoadFailure -> {
                    showFragmentOnError(it.message)
                }
                is PlayerWaitTrack -> {
                    showFragmentPlayerNotFound()
                }
                is PlayerNotAuthorize -> {
                    showFragmentForAuth("Authorization is Required")
                }
            }
        })
    }

    private fun initBottonSheet() {
       // val constraintLayout = findViewById<ConstraintLayout>(R.id.bottom_sheet)
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        findViewById<LinearLayout>(R.id.bottom_sheet).setOnClickListener {
            toggleBottomSheet();
        }
        findViewById<LinearLayout>(R.id.bottom_sheet).setOnDragListener { v, event -> toggleBottomSheet() }
    }

    private fun toggleBottomSheet(): Boolean {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            //btnBottomSheet.setText("Expand sheet");
        }
        return true;
    }

    override fun onBackPressed() {

    }

    private fun initListenerOfHistory() {
        this.vm.myHistory.observe(this, Observer {
            this.mAdapterHistory.setVideo(it)
        })
    }

    private fun setBlackStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setUpRecyclerView() {

        val textColor: Int = Color.parseColor("#FFFFFF");
        this.recyclerViewHistory = findViewById(R.id.historyRecyclerView)
        recyclerViewHistory.setHasFixedSize(true)
        recyclerViewHistory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapterHistory = ListHistoryAdapter(this.vm.myHistory.value, this, textColor)
        recyclerViewHistory.adapter = mAdapterHistory
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {
                    this.vm!!.addEvent(PlayerIsAuthorized())
                }
                AuthorizationResponse.Type.ERROR -> {
                    this.vm!!.addEvent(PlayerUserDeniedAuthorization())
                }
                else -> {
                    this.vm!!.addEvent(PlayerIsFailed())
                }
            }
        }
    }

    private fun initFragmentOfVideos() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentOfVideos, VideosFragment.newInstance())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }


    private fun showFragmentForAuth(message: String) {
        this.replaceFragment(RequestAuthFragment.newInstance(message));
    }

    private fun showFragmentOnError(message: String) {

        this.replaceFragment(ErrorFragment.newInstance(message, "GG"));
    }

    private fun showFragmentOfPlayer(playerState: PlayerIsPlaying) {

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment !is PlayerFragment) {
            Log.e("PLAYER", "is PlayerFragment");
            this.replaceFragment(PlayerFragment.newInstance())
        }
        Log.e("PLAYER", "showFragmentOfPlayer");
    }

    private fun showFragmentPlayerNotFound() {
        this.replaceFragment(PlayerNotFoundFragment.newInstance())
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onVideoClick(video: VideoEntity) {
        watchYoutubeVideo(this, video.id)
    }

    override fun onVideoDeleteClick(video: VideoEntity) {
        this.vm.deleteVideoFromHistory(video)
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
}
