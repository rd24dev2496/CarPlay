package com.example.mycarplay


import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarplay.databinding.FragmentMusicBinding


/**
 * A simple [Fragment] subclass.
 * Use the [MusicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MusicFragment : Fragment(), MusicAdapter.onMediaClickListener {

    private lateinit var musicAdapter: MusicAdapter
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: FragmentMusicBinding
    private var currentIndex = 0
    lateinit var music: List<Music>

    private var isPlaying = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_music, container, false
        ) // used to define layout to dataBinding

        initializeView()
        return binding.root

    }

    private fun initializeView() {

        music = listOf(
            Music(R.raw.kahanisongs, "kahani Songs"),
            Music(R.raw.kesariya, "Pyaar Hota Kayi Baar"),
            Music(R.raw.maanmerejaan, "Maan mere Jaan"),
            Music(R.raw.harshivmahadeva, "Har Har Shambhu Shiv Mahadeva"),
            Music(R.raw.punjabi, "Punjabi"),
            Music(R.raw.maanmerejaan, "Maan mere Jaan"),
        )
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        musicAdapter = MusicAdapter(requireContext(), music, this)
        binding.rvList.adapter = musicAdapter
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.kahanisongs)
        // Set up the play/pause button
        binding.btnPlayPause.setOnClickListener {
            if (isPlaying) {
                pause()
            } else {
                play()
            }
        }
        binding.seekBar.max = mediaPlayer.duration
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                binding.seekBar.progress = mediaPlayer.currentPosition
                handler.postDelayed(this, 1000)
            }
        }, 0)

        binding.btnPrevious.setOnClickListener { previous() }
        binding.btnNext.setOnClickListener { next() }
    }

    private fun play() {
        mediaPlayer.start()
        isPlaying = true
        binding.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_24)
    }

    private fun pause() {
        mediaPlayer.pause()
        isPlaying = false
        binding.btnPlayPause.setImageResource(R.drawable.baseline_play_circle)
    }

    private fun next() {
        if (currentIndex + 1 < music.size) {
            currentIndex = currentIndex + 1
        } else {
            currentIndex = 0
        }
        onMediaClick(music.get(currentIndex), currentIndex)
    }

    private fun previous() {
        if (currentIndex - 1 >= 0) {
            currentIndex = currentIndex - 1
        } else {
            currentIndex = music.size - 1
        }
        onMediaClick(music.get(currentIndex), currentIndex)
    }

    override fun onMediaClick(music: Music, position: Int) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(
            requireContext(),
            Uri.parse("android.resource://" + context?.packageName + "/" + music.resourceId)
        )
        binding.tvSong.text = music.title
        mediaPlayer.prepare()
        mediaPlayer.start()
        currentIndex = position
        isPlaying = true
        binding.btnPlayPause.setImageResource(R.drawable.baseline_pause_circle_24)
        ///
        if (getMp3Thumbnail(music.resourceId) != null)
            binding.imgAlbumArt.setImageBitmap(getMp3Thumbnail(music.resourceId))
        else
            binding.imgAlbumArt.setImageResource(R.drawable.music)

    }

    private fun getMp3Thumbnail(mp3ResourceId: Int): Bitmap? {
        val retriever = MediaMetadataRetriever()
        val fileDescriptor: AssetFileDescriptor = resources.openRawResourceFd(mp3ResourceId)
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