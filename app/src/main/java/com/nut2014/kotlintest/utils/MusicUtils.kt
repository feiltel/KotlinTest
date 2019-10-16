package com.nut2014.kotlintest.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

/**
 *@author feiltel 2019/9/29 0029
 *
 */
object MusicUtils {
    fun getCoverPicture(path: String): Bitmap {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(path)
        val cover = mediaMetadataRetriever.embeddedPicture
        return BitmapFactory.decodeByteArray(cover, 0, cover.size)
    }

    fun getCoverTitle(path: String): String {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(path)
        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
    }

    fun getCoverArtist(path: String): String {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(path)
        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
    }
}