// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'artist_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ArtistInfo _$ArtistInfoFromJson(Map<String, dynamic> json) {
  return ArtistInfo(
    json['id'] as int,
    json['name'] as String,
    json['albums'] as int,
    json['tracks'] as int,
  );
}

Map<String, dynamic> _$ArtistInfoToJson(ArtistInfo instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'albums': instance.albums,
      'tracks': instance.tracks,
    };
