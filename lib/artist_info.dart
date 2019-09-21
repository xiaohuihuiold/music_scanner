import 'package:json_annotation/json_annotation.dart';

part 'artist_info.g.dart';

@JsonSerializable()
class ArtistInfo {
  @JsonKey(name: 'id')
  int id;

  @JsonKey(name: 'name')
  String name;

  @JsonKey(name: 'albums')
  int albums;

  @JsonKey(name: 'tracks')
  int tracks;

  ArtistInfo(
    this.id,
    this.name,
    this.albums,
    this.tracks,
  );

  factory ArtistInfo.fromJson(Map<String, dynamic> srcJson) =>
      _$ArtistInfoFromJson(srcJson);

  Map<String, dynamic> toJson() => _$ArtistInfoToJson(this);
}
