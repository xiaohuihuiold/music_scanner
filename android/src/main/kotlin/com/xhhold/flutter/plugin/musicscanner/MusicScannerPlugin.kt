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
            "getAllMusic" -> {
                // 获取所有音乐
                getAllMusic(result)
            }
            "getAllAlbum" -> {
                // 获取所有专辑
                getAllAlbum(result)
            }
            "getMusicsByAlbumId" -> {
                // 根据专辑id查找音乐
                getMusicsByAlbumId(call,result)
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
