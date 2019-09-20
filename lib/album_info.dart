import 'package:json_annotation/json_annotation.dart';

part 'album_info.g.dart';

@JsonSerializable()
class AlbumInfo {
  @JsonKey(name: 'id')
  int id;

  @JsonKey(name: 'name')
  String name;

  @JsonKey(name: 'path')
  String path;

  @JsonKey(name: 'artist')
  String artist;

  @JsonKey(name: 'last_year')
  int lastYear;

  @JsonKey(name: 'first_year')
  int firstYear;

  @JsonKey(name: 'total')
  int total;

  AlbumInfo(
    this.id,
    this.name,
    this.path,
    this.artist,
    this.lastYear,
    this.firstYear,
    this.total,
  );

  factory AlbumInfo.fromJson(Map<String, dynamic> srcJson) =>
      _$AlbumInfoFromJson(srcJson);

  Map<String, dynamic> toJson() => _$AlbumInfoToJson(this);
}
