package com.ismael.teams.ui.utils.media


import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore



fun createImageUri(context: Context): Uri? {
    val contentResolver = context.contentResolver
    val imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    return contentResolver.insert(imageUri, ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    })
}


fun createVideoUri(context: Context): Uri? {
    val contentResolver = context.contentResolver
    val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    return contentResolver.insert(videoUri, ContentValues().apply {
        put(MediaStore.Video.Media.DISPLAY_NAME, "video_${System.currentTimeMillis()}.mp4")
        put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
    })
}