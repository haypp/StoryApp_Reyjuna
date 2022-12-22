package com.haypp.storyapp_reyjuna.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.haypp.storyapp_reyjuna.R
import com.haypp.storyapp_reyjuna.data.ListStory
import com.haypp.storyapp_reyjuna.databinding.ActivityDetilStoryBinding

class DetilStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetilStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detil_story)
        binding = ActivityDetilStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val detail = intent.getParcelableExtra<ListStory>(EXTRA_NAME) as ListStory

        binding.apply {
            tvname.text = detail.name
            tvDesc.text = detail.description
            tvDate.text = detail.createdAt?.removeRange(16,detail.createdAt!!.length)
            Glide.with(this@DetilStoryActivity)
                .load(detail.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(ivHero)
        }

        binding.btnShare.setOnClickListener {
            val shareUser = Intent(Intent.ACTION_SEND)
            shareUser.type = "text/plain"
            val textOnShare = "This Story Name = ${detail.name}, Created at = ${detail.createdAt.toString().removeRange(16,detail.createdAt.toString().length)}" +
                    ", Description = ${detail.description}, Image = ${detail.photoUrl}"
            shareUser.putExtra(Intent.EXTRA_TEXT, textOnShare)
            startActivity(Intent.createChooser(shareUser, "Share Via"))
        }
    }
    companion object {
        const val EXTRA_NAME = "extra_name"
    }
}