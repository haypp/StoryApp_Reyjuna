package com.haypp.storyapp_reyjuna.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.adaptor.StoryAdaptor
import com.haypp.storyapp_reyjuna.data.UserPref
import com.haypp.storyapp_reyjuna.data.ViewModelFactory
import com.haypp.storyapp_reyjuna.databinding.ActivityMainBinding
import com.haypp.storyapp_reyjuna.viewmodels.MainViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mviewmodel: MainViewModel
    private lateinit var adaptor: StoryAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "StoryApp"
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mviewmodel = ViewModelProvider(this, ViewModelFactory(UserPref.getInstance(dataStore))
        )[MainViewModel::class.java]
        cekusertoken()
    }

    private fun setbutton() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
            finish()
        }
    }

    private fun perAdaptoran() {
        adaptor = StoryAdaptor()
        adaptor.notifyDataSetChanged()
        binding.apply {
            recyclerViewMain.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewMain.setHasFixedSize(true)
            recyclerViewMain.adapter = adaptor
        }
        filladaptor()
    }

    private fun filladaptor() {
        val loginSession = LoginSession(this)
        val token = loginSession.passToken().toString()
        mviewmodel.getAllStories("Bearer $token")
        mviewmodel.allStories.observe(this) {
            if (it != null) {
                adaptor.setListStory(it.ListStory)
            }
            if (it == null) {
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cekusertoken() {
        mviewmodel.getUser().observe(this) { user ->
            if (user.isLogin) {
                Log.d("cek", "user sudah login")
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
                mviewmodel.logout()
            }
        }
        return true
    }
}