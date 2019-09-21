package com.xhhold.flutter.plugin.musicscanner

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.concurrent.*

class MusicScannerPlugin(private val registrar: Registrar) : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "com.xhhold.flutter.plugin.musicscanner")
            channel.setMethodCallHandler(MusicScannerPlugin(registrar))
        }
    }

    private val threadPoolExecutor = ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "searchMusic" -> {
                // 模糊查找音乐
                searchMusic(call, result)
            }
            "getAllMusic" -> {
                // 获取所有音乐
                getAllMusic(result)
            }
            "getAllAlbum" -> {
                // 获取所有专辑
                getAllAlbum(result)
            }
            "getAllArtist" -> {
                // 获取所有艺术家
                getAllArtist(result)
            }
            "getMusicsByAlbumId" -> {
                // 根据专辑id查找音乐
                getMusicsByAlbumId(call, result)
            }
            "getMusicsByArtistId" -> {
                // 根据艺术家id查找音乐
                getMusicsByArtistId(call, result)
            }
            "getAlbumByAlbumId" -> {
                // 根据专辑id查找专辑
                getAlbumByAlbumId(call, result)
            }
            "getArtistByArtistId" -> {
                // 根据艺术家id查找艺术家
                getArtistByArtistId(call, result)
            }
            "refreshAlbumImagesCache" -> {
                // 刷新专辑图片
                refreshAlbumImagesCache(result)
            }
            "clearAlbumImagesCache" -> {
                // 清除专辑图片缓存
                clearAlbumImagesCache(result)
            }
            else -> {
                result.notImplemented()
            }
        }
    }

    /**
     * 模糊查找音乐
     */
    private fun searchMusic(call: MethodCall, result: Result) {
        val keyword = call.argument<String>("keyword")
        if (keyword == null) {
            result.success(null)
            return
        }
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 获得所有音乐
                val musicList: MutableList<MutableMap<String, Any?>>? = MusicScanner.searchMusic(context(), keyword)
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(musicList)
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取所有音乐
     */
    private fun getAllMusic(result: Result) {
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 获得所有音乐
                val musicList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getAllMusic(context())
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(musicList)
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取所有专辑
     */
    private fun getAllAlbum(result: Result) {
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 获得所有专辑
                val albumList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getAllAlbum(context())
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(albumList)
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取所有艺术家
     */
    private fun getAllArtist(result: Result) {
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 获得所有专辑
                val artistList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getAllArtist(context())
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(artistList)
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据专辑id查找音乐
     */
    private fun getMusicsByAlbumId(call: MethodCall, result: Result) {
        val albumId = call.argument<Int>("albumId")
        if (albumId == null) {
            result.success(null)
            return
        }
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 根据专辑id查找音乐
                val musicList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getMusicsByAlbumId(context(), albumId)
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(musicList)
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据艺术家id查找音乐
     */
    private fun getMusicsByArtistId(call: MethodCall, result: Result) {
        val artistId = call.argument<Int>("artistId")
        if (artistId == null) {
            result.success(null)
            return
        }
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 根据艺术家id查找音乐
                val musicList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getMusicsByArtistId(context(), artistId)
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(musicList)
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据专辑id查找专辑
     */
    private fun getAlbumByAlbumId(call: MethodCall, result: Result) {
        val albumId = call.argument<Int>("albumId")
        if (albumId == null) {
            result.success(null)
            return
        }
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 根据专辑id查找专辑
                val album: MutableMap<String, Any?>? = MusicScanner.getAlbumByAlbumId(context(), albumId)
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(album)
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据艺术家id查找艺术家
     */
    private fun getArtistByArtistId(call: MethodCall, result: Result) {
        val artistId = call.argument<Int>("artistId")
        if (artistId == null) {
            result.success(null)
            return
        }
        // 线程池里面查找
        threadPoolExecutor.execute {
            registrar.apply {
                // 根据艺术家id查找艺术家
                val artist: MutableMap<String, Any?>? = MusicScanner.getArtistByArtistId(context(), artistId)
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(artist)
                        }
                    }
                }
            }
        }
    }

    /**
     * 刷新专辑封面
     */
    private fun refreshAlbumImagesCache(result: Result) {
        threadPoolExecutor.execute {
            MusicScanner.refreshAlbumImagesCache(registrar, result)
        }
    }

    /**
     * 清除专辑封面缓存
     */
    private fun clearAlbumImagesCache(result: Result) {
        threadPoolExecutor.execute {
            MusicScanner.clearAlbumImagesCache(registrar.context())
            registrar.apply {
                // 传回数据
                activity().apply {
                    if (!(isFinishing || isDestroyed)) {
                        runOnUiThread {
                            result.success(null)
                        }
                    }
                }
            }
        }
    }
}
