package com.example.salvationarmyconnect

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object SongRepository {
    fun getSongs(context: Context): List<Song> {
        val jsonString: String
        try {
            // Reads the file you created in assets
            jsonString = context.assets.open("marathi_songs.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return emptyList()
        }

        val listType = object : TypeToken<List<Song>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}