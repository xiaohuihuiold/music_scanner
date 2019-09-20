import 'package:json_annotation/json_annotation.dart';

part 'music_info.g.dart';

@JsonSerializable()
class MusicInfo {
  @JsonKey(name: 'id')
  int id;

  @JsonKey(name: 'title')
  String title;

  @JsonKey(name: 'artist')
  String artist;

  @JsonKey(name: 'composer')
  String composer;

  @JsonKey(name: 'album')
  String album;

  @JsonKey(name: 'album_path')
  String albumPath;

  @JsonKey(name: 'file_name')
  String fileName;

  @JsonKey(name: 'path')
  String path;

  @JsonKey(name: 'artist_id')
  int artistId;

  @JsonKey(name: 'album_id')
  int albumId;

  @JsonKey(name: 'size')
  int size;

  @JsonKey(name: 'duration')
  int duration;

  @JsonKey(name: 'year')
  int year;

  @JsonKey(name: 'date_added')
  int dateAdded;

  @JsonKey(name: 'date_modified')
  int dateModified;

  MusicInfo(
    this.id,
    this.title,
    this.artist,
    this.composer,
    this.album,
    this.albumPath,
    this.fileName,
    this.path,
    this.artistId,
    this.albumId,
    this.size,
    this.duration,
    this.year,
    this.dateAdded,
    this.dateModified,
  );

  factory MusicInfo.fromJson(Map<String, dynamic> srcJson) =>
      _$MusicInfoFromJson(srcJson);

  Map<String, dynamic> toJson() => _$MusicInfoToJson(this);
}
