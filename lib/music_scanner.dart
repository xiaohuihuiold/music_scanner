import 'dart:async';

import 'package:flutter/services.dart';
import 'album_info.dart';
import 'music_info.dart';

export 'album_info.dart';
export 'music_info.dart';

class MusicScanner {
  static const MethodChannel _channel =
      const MethodChannel('com.xhhold.flutter.plugin.musicscanner');

  /// 模糊查找音乐
  static Future<List<MusicInfo>> searchMusic(String keyword) async {
    List<dynamic> musicList =
        await _channel.invokeMethod('searchMusic', {'keyword': keyword});
    List<MusicInfo> result = List();
    musicList?.forEach((e) {
      if (e == null || e is! Map) return;
      MusicInfo musicInfo = MusicInfo.fromJson(Map.from(e));
      result.add(musicInfo);
    });
    return result;
  }

  /// 获取所有音乐
  static Future<List<MusicInfo>> getAllMusic() async {
    List<dynamic> musicList = await _channel.invokeMethod('getAllMusic');
    List<MusicInfo> result = List();
    musicList?.forEach((e) {
      if (e == null || e is! Map) return;
      MusicInfo musicInfo = MusicInfo.fromJson(Map.from(e));
      result.add(musicInfo);
    });
    return result;
  }

  /// 根据专辑id获取音乐
  static Future<List<MusicInfo>> getMusicsByAlbumId(int albumId) async {
    List<dynamic> musicList =
        await _channel.invokeMethod('getMusicsByAlbumId', {'albumId': albumId});
    List<MusicInfo> result = List();
    musicList?.forEach((e) {
      if (e == null || e is! Map) return;
      MusicInfo musicInfo = MusicInfo.fromJson(Map.from(e));
      result.add(musicInfo);
    });
    return result;
  }

  /// 获取所有专辑
  static Future<List<AlbumInfo>> getAllAlbum() async {
    List<dynamic> albumList = await _channel.invokeMethod('getAllAlbum');
    List<AlbumInfo> result = List();
    albumList?.forEach((e) {
      if (e == null || e is! Map) return;
      AlbumInfo albumInfo = AlbumInfo.fromJson(Map.from(e));
      result.add(albumInfo);
    });
    return result;
  }

  /// 根据专辑id获取音乐
  static Future<AlbumInfo> getAlbumByAlbumId(int albumId) async {
    dynamic album =
        await _channel.invokeMethod('getAlbumByAlbumId', {'albumId': albumId});
    if (album == null) {
      return null;
    }
    return AlbumInfo.fromJson(Map.from(album));
  }

  /// 获取专辑图片
  static Future<Null> refreshAlbumImagesCache() async {
    await _channel.invokeMethod('refreshAlbumImagesCache');
  }

  /// 清除专辑图片缓存
  static Future<Null> clearAlbumImagesCache() async {
    await _channel.invokeMethod('clearAlbumImagesCache');
  }
}
