package com.xhhold.flutter.plugin.musicscanner

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import android.util.SparseArray
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.channels.FileChannel
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * 音乐搜索类
 */
class MusicScanner {
    companion object {
        private const val TAG = "MusicScanner"
        private const val CACHE_ALBUM_IMAGE_FOLDER = "album_images"
        private val threadPoolExecutor = ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())
        /**
         * 判断线程池中的任务是否全部执行完成
         */
        private val threadCount = AtomicInteger()

        /**
         * 模糊查找音乐
         */
        @JvmStatic
        fun searchMusic(context: Context, keyword: String?): MutableList<MutableMap<String, Any?>>? {
            if (keyword == null) {
                return null
            }
            return getMusics(context, "${MediaStore.Audio.Media.TITLE} LIKE ? OR ${MediaStore.Audio.Media.ALBUM} LIKE ? OR ${MediaStore.Audio.Media.ARTIST} LIKE ?", arrayOf("%$keyword%", "%$keyword%", "%$keyword%"), null, null)
        }

        /**
         * 根据专辑id查找音乐
         */
        @JvmStatic
        fun getMusicsByAlbumId(context: Context, albumId: Int?): MutableList<MutableMap<String, Any?>>? {
            if (albumId == null) {
                return null
            }
            return getMusics(context, "${MediaStore.Audio.Media.ALBUM_ID} = ?", arrayOf("$albumId"), null, null)
        }

        /**
         * 获取所有音乐
         */
        @JvmStatic
        fun getAllMusic(context: Context): MutableList<MutableMap<String, Any?>>? {
            return getMusics(context, null, null, null, null)
        }

        /**
         * 获取音乐
         */
        @JvmStatic
        fun getMusics(context: Context, where: String?, whereArgs: Array<String>?, sortBy: String?, sort: String?): MutableList<MutableMap<String, Any?>>? {
            val contentResolver: ContentResolver? = context.contentResolver
            if (contentResolver == null) {
                Log.e(TAG, "contentResolver is null!!!")
                return null
            }
            // 音乐列表
            val musicList: MutableList<MutableMap<String, Any?>> = ArrayList()
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
                while (moveToNext()) {
                    val music: MutableMap<String, Any?> = HashMap()
                    val albumId = getInt(8)
                    val path = getString(2)
                    music["id"] = getInt(0)
                    music["title"] = getString(1)
                    music["path"] = path
                    music["composer"] = getString(3)
                    music["duration"] = getInt(4)
                    music["size"] = getInt(5)
                    music["year"] = getInt(6)
                    music["album"] = getString(7)
                    music["album_id"] = albumId
                    music["artist"] = getString(9)
                    music["artist_id"] = getInt(10)
                    music["date_added"] = getInt(11)
                    music["date_modified"] = getInt(12)
                    music["file_name"] = getString(13)
                    // 专辑封面路径直接设置为缓存的路径
                    music["album_path"] = "${context.cacheDir}/$CACHE_ALBUM_IMAGE_FOLDER/$albumId"
                    musicList.add(music)
                }
                close()
            }
            return musicList
        }

        /**
         * 根据专辑id查找专辑
         */
        @JvmStatic
        fun getAlbumByAlbumId(context: Context, albumId: Int?): MutableMap<String, Any?>? {
            if (albumId == null) {
                return null
            }
            val albums = getAlbums(context, "${MediaStore.Audio.Albums._ID} = ?", arrayOf("$albumId"), null, null)
            if (albums == null || albums.size == 0) {
                return null
            }
            return albums[0]
        }

        /**
         * 获取所有专辑
         */
        @JvmStatic
        fun getAllAlbum(context: Context): MutableList<MutableMap<String, Any?>>? {
            return getAlbums(context, null, null, null, null)
        }

        /**
         * 获取专辑
         */
        @JvmStatic
        fun getAlbums(context: Context, where: String?, whereArgs: Array<String>?, sortBy: String?, sort: String?): MutableList<MutableMap<String, Any?>>? {
            val contentResolver: ContentResolver? = context.contentResolver
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
                while (moveToNext()) {
                    val album: MutableMap<String, Any?> = HashMap()
                    val id = getInt(0)
                    album["id"] = id
                    album["name"] = getString(1)
                    // 专辑封面路径直接设置为缓存的路径
                    album["path"] = "${context.cacheDir}/$CACHE_ALBUM_IMAGE_FOLDER/$id"
                    album["artist"] = getString(3)
                    album["last_year"] = getInt(4)
                    album["first_year"] = getInt(5)
                    album["total"] = getInt(6)
                    albumList.add(album)
                }
                close()
            }
            return albumList
        }

        /**
         * 清除专辑图片缓存
         */
        @Synchronized
        @JvmStatic
        fun clearAlbumImagesCache(context: Context) {
            // 获取缓存文件夹
            val cacheDir = "${context.cacheDir.path}/$CACHE_ALBUM_IMAGE_FOLDER"
            val cacheFile = File(cacheDir)
            // 没有创建缓存文件夹则返回
            if (!cacheFile.exists()) {
                return
            }
            // 遍历删除缓存文件以及缓存文件夹
            try {
                val cacheFiles = cacheFile.listFiles()
                cacheFiles?.forEach {
                    it.delete()
                }
                cacheFile.delete()
            } catch (e: Exception) {

            }
        }

        /**
         * 刷新专辑封面
         */
        @Synchronized
        @JvmStatic
        fun refreshAlbumImagesCache(registrar: PluginRegistry.Registrar, result: MethodChannel.Result) {
            val context = registrar.context()
            val contentResolver: ContentResolver? = context.contentResolver
            if (contentResolver == null) {
                Log.e(TAG, "contentResolver is null!!!")
                return
            }
            // 专辑列表
            val albumMap: SparseArray<String?> = SparseArray()
            contentResolver.query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART), null, null, null)
                    ?.apply {
                        while (moveToNext()) {
                            val id = getInt(0)
                            val path = getString(1)
                            albumMap.put(id, path)
                        }
                        close()
                    }
            // 查询所有音乐
            contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayOf(MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.DATA), null, null, null)
                    ?.apply {
                        while (moveToNext()) {
                            // 进入新的任务自增
                            threadCount.incrementAndGet()
                            val id = getInt(0)
                            val path = getString(1)
                            threadPoolExecutor.execute {
                                // 加载专辑图片并缓存
                                loadAlbumImageToCache(context, id, albumMap[id], path)
                                // 当任务执行完成时自减并判断是否是最后一个
                                if (threadCount.decrementAndGet() != 0) {
                                    return@execute
                                }
                                registrar.apply {
                                    activity()?.apply {
                                        if (isFinishing || isDestroyed) {
                                            return@execute
                                        }
                                        runOnUiThread {
                                            try {
                                                // 执行完成返回结果
                                                result.success(null)
                                            } catch (e: Exception) {
                                            }
                                        }

                                    }
                                }
                            }
                        }
                        close()
                    }
        }

        /**
         * 处理专辑图片
         * @param context
         * @param albumId 专辑id
         * @param albumPath 媒体库的专辑图片路径
         * @param musicPath 音乐文件路径
         */
        @JvmStatic
        private fun loadAlbumImageToCache(context: Context, albumId: Int?, albumPath: String?, musicPath: String?): String? {
            if (albumId == null || musicPath == null) return null
            // 获取缓存文件
            val cacheFile = File("${context.cacheDir.path}/$CACHE_ALBUM_IMAGE_FOLDER/$albumId")
            // 如果已经有文件则直接返回
            if (cacheFile.exists() && cacheFile.isFile && cacheFile.length() > 0) {
                return cacheFile.path
            }

            // 获取媒体库的专辑封面
            if (albumPath != null) {
                val albumFile = File(albumPath)
                // 文件有效就复制到app的缓存下
                if (albumFile.exists() && albumFile.isFile && albumFile.length() > 0) {
                    return copyToCache(context, CACHE_ALBUM_IMAGE_FOLDER, albumFile.path, "$albumId")
                }
            }

            // 创建album_images缓存文件夹
            if (!cacheFile.parentFile.exists()) {
                cacheFile.parentFile.mkdirs()
            }
            // 从音乐文件中读取专辑封面信息
            val mediaMetadataRetriever = MediaMetadataRetriever()
            mediaMetadataRetriever.setDataSource(musicPath)
            // 查找专辑图片
            val byteArray: ByteArray? = mediaMetadataRetriever.embeddedPicture
            // 没有图片数据就返回空
            if (byteArray != null && byteArray.isEmpty()) {
                return null
            }
            // 将获取的封面数据写入缓存文件夹
            var outputStream: FileOutputStream? = null
            try {
                outputStream = FileOutputStream(cacheFile)
                outputStream.write(byteArray)
                return cacheFile.path
            } catch (e: Exception) {
            } finally {
                // 释放
                mediaMetadataRetriever.release()
                outputStream?.close()
            }
            return null
        }

        /**
         * 复制文件到缓存
         * @param context
         * @param folder 缓存的文件夹
         * @param filePath 源文件完整路径
         * @param fileName 保存在缓存中的文件名
         */
        @JvmStatic
        private fun copyToCache(context: Context, folder: String, filePath: String, fileName: String): String? {
            // 获取缓存文件夹
            val cacheDir = "${context.cacheDir.path}/$folder"
            val cacheFile = File(cacheDir)
            // 创建缓存文件夹
            if (!cacheFile.exists()) {
                cacheFile.mkdirs()
            }
            // 缓存目标文件
            val newFile = File("$cacheDir/$fileName")
            // 缓存文件存在则删除
            if (newFile.exists()) {
                if (newFile.isFile) {
                    newFile.delete()
                } else {
                    return newFile.path
                }
            }
            // 源文件
            val srcFile = File(filePath)

            // 使用FileChannel复制文件
            var inputChannel: FileChannel? = null
            var outputChannel: FileChannel? = null

            try {
                inputChannel = FileInputStream(srcFile).channel
                outputChannel = FileOutputStream(newFile).channel
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size())
                return newFile.path
            } catch (e: Exception) {
            } finally {
                inputChannel?.close()
                outputChannel?.close()
            }
            return null
        }
    }
}