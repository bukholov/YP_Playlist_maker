package com.example.yp_playlist_maker.media.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.io.File
import java.io.FileOutputStream
import java.sql.Timestamp


class PlaylistCreateViewModel(private val playlistRepository: PlaylistRepository): ViewModel() {
    private val context: Context by KoinJavaComponent.inject(Context::class.java)
    lateinit var savedInstancePlaylist: Playlist
    private var pathToSave = ""


    fun createPlaylist(playlist: Playlist){
        if(playlist.pathImage.isNotBlank()){
            saveImageToPrivateStorage(Uri.parse(playlist.pathImage))
        }
        val playlistToSave = Playlist(
            0,
            playlist.playlistName,
            playlist.playlistDescription,
            pathToSave
        )

        viewModelScope.launch {
            playlistRepository.insertPlaylist(playlistToSave)
        }
    }

    private fun saveImageToPrivateStorage(uri: Uri){
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), uri.toString().split("/").last())
        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, uri.toString().split("/").last()+Timestamp(System.currentTimeMillis()))
        val inputStream = context?.contentResolver?.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        Log.d("saveImageToPrivateStorage", "SaveImage from $uri to ${file.absolutePath}")
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        pathToSave = file.absolutePath
    }

    fun savePlaylistInstanceState(playlist: Playlist){
        savedInstancePlaylist = playlist
    }

    fun getPlaylistFromSavedInstanceState(): Playlist{
        return savedInstancePlaylist
    }
}