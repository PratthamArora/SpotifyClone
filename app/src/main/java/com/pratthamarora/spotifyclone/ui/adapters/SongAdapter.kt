package com.pratthamarora.spotifyclone.ui.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import com.pratthamarora.spotifyclone.R
import kotlinx.android.synthetic.main.list_item.view.*
import javax.inject.Inject

class SongAdapter @Inject constructor(
    private val glide: RequestManager
) : BaseSongAdapter(R.layout.list_item) {

    override val differ = AsyncListDiffer(this, differCallBack)

    override fun onBindViewHolder(holder: SongsViewHolder, position: Int) {
        val song = songs[position]

        holder.itemView.apply {
            tvPrimary.text = song.title
            tvSecondary.text = song.subtitle
            glide.load(song.imageUrl).into(ivItemImage)

            setOnClickListener {
                onItemClickListener?.let { listener ->
                    listener(song)
                }
            }
        }
    }

}