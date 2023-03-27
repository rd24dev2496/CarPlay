package com.example.mycarplay

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MusicAdapter(private val context: Context, private val musicList: List<Music>, private val mListener : onMediaClickListener) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    private var currentIndex = 0

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(mp3File: Music, listener :  onMediaClickListener, position : Int, bm : Bitmap? ) {
            val titleTextView: TextView = itemView.findViewById(R.id.title_textview)
            val musicImage : ImageView = itemView.findViewById(R.id.musicImage)
            titleTextView.text = mp3File.title
            titleTextView.setOnClickListener {
                listener.onMediaClick(mp3File, position)
            }
            if (bm != null)
                musicImage.setImageBitmap(bm)
            else
                musicImage.setImageResource(R.drawable.music)

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_music, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val music = musicList[position]
        holder.bind(music, mListener, position,  getMp3Thumbnail(music.resourceId))
    }

    override fun getItemCount() = musicList.size

    interface onMediaClickListener{
        fun onMediaClick(music : Music, position: Int)

    }

    private fun getMp3Thumbnail(mp3ResourceId: Int): Bitmap? {
        val retriever = MediaMetadataRetriever()
        val fileDescriptor: AssetFileDescriptor = context.resources.openRawResourceFd(mp3ResourceId)
        retriever.setDataSource(
            fileDescriptor.fileDescriptor,
            fileDescriptor.startOffset,
            fileDescriptor.length
        )

        val embeddedPicture: ByteArray? = retriever.embeddedPicture
        if (embeddedPicture != null) {
            return BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.size)
        }

        retriever.release()
        return null
    }


}
