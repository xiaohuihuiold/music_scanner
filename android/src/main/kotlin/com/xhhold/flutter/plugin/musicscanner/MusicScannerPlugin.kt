package com.xhhold.flutter.plugin.musicscanner

import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MusicScannerPlugin(private val registrar: Registrar) : MethodCallHandler {
    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "com.xhhold.flutter.plugin.musicscanner")
            channel.setMethodCallHandler(MusicScannerPlugin(registrar))
        }
    }

    private val threadPoolExecutor = ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, ArrayBlockingQueue<Runnable>(5))

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "getAllMusic" -> {
                getAllMusic(result)
            }
            "getAllAlbum" -> {
                getAllAlbum(result)
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
            registrar.activity()?.apply {
                // 获得所有音乐
                val musicList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getAllMusic(contentResolver)
                // 传回数据
                if (!(isFinishing || isDestroyed)) {
                    runOnUiThread {
                        result.success(musicList)
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
            registrar.activity()?.apply {
                // 获得所有专辑
                val albumList: MutableList<MutableMap<String, Any?>>? = MusicScanner.getAllAlbum(contentResolver)
                // 传回数据
                if (!(isFinishing || isDestroyed)) {
                    runOnUiThread {
                        result.success(albumList)
                    }
                }
            }
        }
    }
}
