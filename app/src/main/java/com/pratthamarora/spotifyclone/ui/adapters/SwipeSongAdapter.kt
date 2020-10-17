package com.pratthamarora.spotifyclone.ui.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.pratthamarora.spotifyclone.R
import kotlinx.android.synthetic.main.swipe_item.view.*

class SwipeSongAdapter : BaseSongAdapter(R.layout.swipe_item) {

    override val differ = AsyncListDiffer(this, differCallBack)

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val song = songs[position]

        holder.itemView.apply {
            val text = "${song.title} - ${song.subtitle}"
            tvPrimary.text = text

            setOnClickListener {
                onItemClickListener?.let { listener ->
                    listener(song)
                }
            }
        }
    }
}