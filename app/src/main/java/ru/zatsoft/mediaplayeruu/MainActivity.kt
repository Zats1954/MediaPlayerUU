package ru.zatsoft.mediaplayeruu

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import ru.zatsoft.mediaplayeruu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null
    private var songList = listOf(
        Song(R.raw.menuet, "Менуэт"),
        Song(R.raw.melody, "Мелоди"),
        Song(R.raw.tokata, "Токката")
    )
    private var numberSong = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        numberSong = 0
        binding.rewind.visibility = View.GONE
        binding.songName.text = songList[0].songName
        playSong(songList[numberSong].songNumber)

        binding.forward.setOnClickListener {
            if (++numberSong >= songList.size) numberSong = 0
            moveSong()
        }

        binding.rewind.setOnClickListener {
            if (--numberSong < 0) numberSong = songList.lastIndex
            moveSong()
        }
    }

    private fun moveSong() {
        stopPlay()
        when (numberSong) {
            0 -> {
                binding.rewind.visibility = View.GONE
                binding.forward.visibility = View.VISIBLE
                binding.songName.text = songList[0].songName
            }

            songList.lastIndex -> {
                binding.forward.visibility = View.GONE
                binding.songName.text = songList[numberSong].songName
            }

            else -> {
                binding.rewind.visibility = View.VISIBLE
                binding.forward.visibility = View.VISIBLE
                binding.songName.text = songList[numberSong].songName
            }

        }
        playSong(songList[numberSong].songNumber)
    }

    private fun playSong(song: Int) {
        binding.play.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, song)
                initializeSeekbar()
            }
            mediaPlayer?.start()
        }

        binding.pause.setOnClickListener {
            if (mediaPlayer !== null) mediaPlayer?.pause()
        }

        binding.stop.setOnClickListener {
            stopPlay()
        }

        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) mediaPlayer?.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar??) {
                }
            })
    }

    private fun stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun initializeSeekbar() {
        binding.seekBar.max = mediaPlayer!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    binding.seekBar.progress = 0
                }
            }
        }, 0)
    }
}