package com.example.misheroes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.misheroes.databinding.ItemHeroeBinding
import com.squareup.picasso.Picasso




class HeroeAdapter(val heroes: List<CharacterResponse>) : RecyclerView.Adapter<HeroeAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = heroes[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_heroe, parent, false))
    }

    override fun getItemCount(): Int {
        return heroes.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemHeroeBinding.bind(view)

        fun bind(hero: CharacterResponse) {
            binding.tvSuperHero.text = hero.name
        }
    }
}