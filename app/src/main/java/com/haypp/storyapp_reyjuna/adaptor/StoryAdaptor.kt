package com.haypp.storyapp_reyjuna.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.haypp.storyapp_reyjuna.activity.DetilStoryActivity
import com.haypp.storyapp_reyjuna.data.ListStory
import com.haypp.storyapp_reyjuna.databinding.ListStoryBinding

class StoryAdaptor : RecyclerView.Adapter<StoryAdaptor.ListStoryHolder>() {

    private val allStories = ArrayList<ListStory>()
    fun setListStory(stories: ArrayList<ListStory>) {
        allStories.clear()
        allStories.addAll(stories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStoryHolder {
        val binding = ListStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStoryHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStoryHolder, position: Int) {
        val (name, _, photoUrl, date) = allStories[position]
        holder.binding.tvName.text = name
        holder.binding.tvCreated.text = date.toString().removeRange(16,date.toString().length)
        Glide.with(holder.itemView.context)
            .load(photoUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.binding.imgHero)
        holder.itemView.setOnClickListener {
            val mcontext = holder.itemView.context
            val pindah = Intent(mcontext, DetilStoryActivity::class.java)
            pindah.putExtra(DetilStoryActivity.EXTRA_NAME, allStories[position])
            mcontext.startActivity(pindah)
        }
    }

    override fun getItemCount() = allStories.size

    class ListStoryHolder(var binding: ListStoryBinding) : RecyclerView.ViewHolder(binding.root)

}