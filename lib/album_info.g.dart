// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'album_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

AlbumInfo _$AlbumInfoFromJson(Map<String, dynamic> json) {
  return AlbumInfo(
    json['id'] as int,
    json['name'] as String,
    json['path'] as String,
    json['artist'] as String,
    json['last_year'] as int,
    json['first_year'] as int,
    json['total'] as int,
  );
}

Map<String, dynamic> _$AlbumInfoToJson(AlbumInfo instance) => <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'path': instance.path,
      'artist': instance.artist,
      'last_year': instance.lastYear,
      'first_year': instance.firstYear,
      'total': instance.total,
    };
