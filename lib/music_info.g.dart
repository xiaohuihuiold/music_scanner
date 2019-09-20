// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'music_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MusicInfo _$MusicInfoFromJson(Map<String, dynamic> json) {
  return MusicInfo(
    json['id'] as int,
    json['title'] as String,
    json['artist'] as String,
    json['composer'] as String,
    json['album'] as String,
    json['album_path'] as String,
    json['file_name'] as String,
    json['path'] as String,
    json['artist_id'] as int,
    json['album_id'] as int,
    json['size'] as int,
    json['duration'] as int,
    json['year'] as int,
    json['date_added'] as int,
    json['date_modified'] as int,
  );
}

Map<String, dynamic> _$MusicInfoToJson(MusicInfo instance) => <String, dynamic>{
      'id': instance.id,
      'title': instance.title,
      'artist': instance.artist,
      'composer': instance.composer,
      'album': instance.album,
      'album_path': instance.albumPath,
      'file_name': instance.fileName,
      'path': instance.path,
      'artist_id': instance.artistId,
      'album_id': instance.albumId,
      'size': instance.size,
      'duration': instance.duration,
      'year': instance.year,
      'date_added': instance.dateAdded,
      'date_modified': instance.dateModified,
    };
