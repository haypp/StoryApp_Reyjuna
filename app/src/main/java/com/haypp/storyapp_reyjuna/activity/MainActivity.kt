package com.haypp.storyapp_reyjuna.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.adaptor.LoadingStoryAdapter
import com.haypp.storyapp_reyjuna.adaptor.StoryAdaptor
import com.haypp.storyapp_reyjuna.data.UserPref
import com.haypp.storyapp_reyjuna.data.ViewModelFactory
import com.haypp.storyapp_reyjuna.databinding.ActivityMainBinding
import com.haypp.storyapp_reyjuna.viewmodels.AddStoryViewModel
import com.haypp.storyapp_reyjuna.viewmodels.LoginViewModel
import com.haypp.storyapp_reyjuna.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mviewmodel: MainViewModel
    private lateinit var lviewmodel: LoginViewModel
    private lateinit var adaptor: StoryAdaptor
    private lateinit var factory: ViewModelFactory
    private lateinit var adViewmodel : AddStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "StoryApp"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        mviewmodel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        lviewmodel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
        adViewmodel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
        adaptor = StoryAdaptor()
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.setHasFixedSize(true)
        cekusertoken()
    }

    private fun setbutton() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
            finish()
        }
    }

    private fun perAdaptoran() {
        binding.rvMain.adapter = adaptor.withLoadStateFooter(
            footer = LoadingStoryAdapter { adaptor.retry() }
        )
        mviewmodel.getStory().observe(this@MainActivity) {
            adaptor.submitData(lifecycle, it)
        }
    }

    private fun cekusertoken() {
        adViewmodel.getUser().observe(this) { user ->
            if (user.isLogin) {
                perAdaptoran()
                setbutton()
            } else {
                startActivity(Intent(this, Login::class.java))
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                val loginSession = LoginSession(this)
                loginSession.logoutSession()
                lviewmodel.logout()
            }
            R.id.btnmaps -> {
                startActivity(Intent(this, StoryMaps::class.java))
            }
        }
        return true
    }
}