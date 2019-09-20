package com.xhhold.flutter.plugin.musicscanner

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray

/**
 * 音乐搜索类
 */
class MusicScanner {
    companion object {
        private const val TAG = "MusicScanner"

        /**
         * 获取所有音乐
         */
        @JvmStatic
        fun getAllMusic(contentResolver: ContentResolver?): MutableList<MutableMap<String, Any?>>? {
            return getMusics(contentResolver, null, null, null, null)
        }

        /**
         * 获取音乐
         */
        @JvmStatic
        fun getMusics(contentResolver: ContentResolver?, where: String?, whereArgs: Array<String>?, sortBy: String?, sort: String?): MutableList<MutableMap<String, Any?>>? {
            if (contentResolver == null) {
                Log.e(TAG, "contentResolver is null!!!")
                return null
            }
            // 音乐列表
            val musicList: MutableList<MutableMap<String, Any?>> = ArrayList()
            // 专辑列表
            val albumList: MutableList<MutableMap<String, Any?>>? = getAllAlbum(contentResolver)
            // 专辑表
            val albumMap: SparseArray<MutableMap<String, Any?>> = SparseArray()
            albumList?.forEach {
                val id: Any? = it["id"]
                if (id is Int) {
                    albumMap.put(id, it)
                }
            }
            // 需要取出的字段
            val projection: Array<String> = arrayOf(
                    MediaStore.Audio.Media._ID, // int
                    MediaStore.Audio.Media.TITLE, // string
                    MediaStore.Audio.Media.DATA, // string
                    MediaStore.Audio.Media.COMPOSER, // string
                    MediaStore.Audio.Media.DURATION, // int
                    MediaStore.Audio.Media.SIZE, // int
                    MediaStore.Audio.Media.YEAR, // int
                    MediaStore.Audio.Media.ALBUM, // string
                    MediaStore.Audio.Media.ALBUM_ID, // int
                    MediaStore.Audio.Media.ARTIST, // string
                    MediaStore.Audio.Media.ARTIST_ID, // int
                    MediaStore.Audio.Media.DATE_ADDED, // int
                    MediaStore.Audio.Media.DATE_MODIFIED, // int
                    MediaStore.Audio.Media.DISPLAY_NAME //string
            )
            val cursor: Cursor? = contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    where,
                    if (where == null) null else whereArgs,
                    if (sortBy == null) null else "$sortBy $sort"
            )

            cursor?.apply {
                moveToFirst()
                while (moveToNext()) {
                    val music: MutableMap<String, Any?> = HashMap()
                    music["id"] = getInt(0)
                    music["title"] = getString(1)
                    music["path"] = getString(2)
                    music["composer"] = getString(3)
                    music["duration"] = getInt(4)
                    music["size"] = getInt(5)
                    music["year"] = getInt(6)
                    music["album"] = getString(7)
                    music["album_id"] = getInt(8)
                    music["artist"] = getString(9)
                    music["artist_id"] = getInt(10)
                    music["date_added"] = getInt(11)
                    music["date_modified"] = getInt(12)
                    music["file_name"] = getString(13)
                    val album: MutableMap<String, Any?>? = albumMap[getInt(8)]
                    if (album != null) {
                        music["album_path"] = album["path"]
                    }
                    musicList.add(music)
                }
            }
            cursor?.close()
            return musicList
        }

        /**
         * 获取所有专辑
         */
        @JvmStatic
        fun getAllAlbum(contentResolver: ContentResolver?): MutableList<MutableMap<String, Any?>>? {
            return getAlbums(contentResolver, null, null, null, null)
        }

        /**
         * 获取专辑
         */
        @JvmStatic
        fun getAlbums(contentResolver: ContentResolver?, where: String?, whereArgs: Array<String>?, sortBy: String?, sort: String?): MutableList<MutableMap<String, Any?>>? {
            if (contentResolver == null) {
                Log.e(TAG, "contentResolver is null!!!")
                return null
            }
            // 专辑列表
            val albumList: MutableList<MutableMap<String, Any?>> = ArrayList()
            // 需要取出的字段
            val projection: Array<String> = arrayOf(
                    MediaStore.Audio.Albums._ID, // int
                    MediaStore.Audio.Albums.ALBUM, // string
                    MediaStore.Audio.Albums.ALBUM_ART, // string
                    MediaStore.Audio.Albums.ARTIST, // string
                    MediaStore.Audio.Albums.LAST_YEAR, // int
                    MediaStore.Audio.Albums.FIRST_YEAR, // int
                    MediaStore.Audio.Albums.NUMBER_OF_SONGS //int
            )
            val cursor: Cursor? = contentResolver.query(
                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    projection,
                    where,
                    if (where == null) null else whereArgs,
                    if (sortBy == null) null else "$sortBy $sort"
            )

            cursor?.apply {
                moveToFirst()
                while (moveToNext()) {
                    val album: MutableMap<String, Any?> = HashMap()
                    album["id"] = getInt(0)
                    album["name"] = getString(1)
                    album["path"] = getString(2)
                    album["artist"] = getString(3)
                    album["last_year"] = getInt(4)
                    album["first_year"] = getInt(5)
                    album["total"] = getInt(6)
                    albumList.add(album)
                }
            }
            cursor?.close()
            return albumList
        }

    }
}