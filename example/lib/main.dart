import 'dart:io';

import 'package:flutter/material.dart';

import 'package:music_scanner/music_scanner.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> with SingleTickerProviderStateMixin {
  TabController _controller;

  @override
  void initState() {
    super.initState();
    _controller = TabController(length: 3, vsync: this);
    MusicScanner.refreshAlbumImagesCache().then((_) {
      print('success');
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        floatingActionButton: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            FloatingActionButton(
              child: Icon(Icons.refresh),
              onPressed: () {
                MusicScanner.refreshAlbumImagesCache().then((_) {
                  print('success');
                });
              },
            ),
            SizedBox(height: 8.0),
            FloatingActionButton(
              child: Icon(Icons.delete),
              onPressed: () {
                MusicScanner.clearAlbumImagesCache().then((_) {
                  print('success');
                });
              },
            ),
            SizedBox(height: 8.0),
            FloatingActionButton(
              child: Icon(Icons.search),
              onPressed: () {
                MusicScanner.searchMusic('vocaloid').then((value) {
                  value?.forEach((f) {
                    print(f.toJson());
                  });
                });
              },
            ),
          ],
        ),
        appBar: AppBar(
          title: const Text('Music Scanner Example'),
          bottom: TabBar(
            controller: _controller,
            tabs: [
              Tab(text: "音乐"),
              Tab(text: "专辑"),
              Tab(text: "艺术家"),
            ],
          ),
        ),
        body: TabBarView(
          controller: _controller,
          children: [
            MusicPage(),
            AlbumPage(),
            ArtistPage(),
          ],
        ),
      ),
    );
  }
}

/// 音乐页面
class MusicPage extends StatefulWidget {
  @override
  _MusicPageState createState() => _MusicPageState();
}

class _MusicPageState extends State<MusicPage> {
  List<MusicInfo> _musicList;

  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) async {
      List<MusicInfo> musicList = await MusicScanner.getAllMusic();
      if (!mounted) return;
      setState(() {
        _musicList = musicList;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: _musicList?.length ?? 0,
      itemBuilder: (context, index) {
        MusicInfo musicInfo = _musicList[index];
        return ListTile(
          onTap: () {
            MusicScanner.getAlbumByAlbumId(musicInfo.albumId).then((value) {
              print(value.toJson());
            });
            MusicScanner.getArtistByArtistId(musicInfo.artistId).then((value) {
              print(value.toJson());
            });
          },
          leading: AspectRatio(
            aspectRatio: 1.0,
            child: ClipRRect(
              borderRadius: BorderRadius.circular(80.0),
              child: Stack(
                fit: StackFit.expand,
                children: <Widget>[
                  Image.file(
                    File(musicInfo.albumPath),
                    fit: BoxFit.cover,
                  ),
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(80.0),
                      border: Border.all(
                          color: Colors.grey.withOpacity(0.6), width: 2.0),
                    ),
                  ),
                ],
              ),
            ),
          ),
          title: Text(musicInfo.title),
          subtitle: Text(musicInfo.album),
        );
      },
    );
  }
}

/// 专辑列表页面
class AlbumPage extends StatefulWidget {
  @override
  _AlbumPageState createState() => _AlbumPageState();
}

class _AlbumPageState extends State<AlbumPage> {
  List<AlbumInfo> _albumList;

  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) async {
      List<AlbumInfo> albumList = await MusicScanner.getAllAlbum();
      if (!mounted) return;
      setState(() {
        _albumList = albumList;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: _albumList?.length ?? 0,
      itemBuilder: (context, index) {
        AlbumInfo albumInfo = _albumList[index];
        return ListTile(
          onTap: () {
            MusicScanner.getMusicsByAlbumId(albumInfo.id).then((value) {
              value.forEach((f) {
                print(f.toJson());
              });
            });
          },
          leading: AspectRatio(
            aspectRatio: 1.0,
            child: ClipRRect(
              borderRadius: BorderRadius.circular(80.0),
              child: Stack(
                fit: StackFit.expand,
                children: <Widget>[
                  Image.file(
                    File(albumInfo.path),
                    fit: BoxFit.cover,
                  ),
                  Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(80.0),
                      border: Border.all(
                          color: Colors.grey.withOpacity(0.6), width: 2.0),
                    ),
                  ),
                ],
              ),
            ),
          ),
          title: Text(albumInfo.name),
          subtitle: Text(albumInfo.artist),
          trailing: Text(
            '${albumInfo.total}首',
            style: TextStyle(color: Colors.black54),
          ),
        );
      },
    );
  }
}

/// 艺术家页面
class ArtistPage extends StatefulWidget {
  @override
  _ArtistPageState createState() => _ArtistPageState();
}

class _ArtistPageState extends State<ArtistPage> {
  List<ArtistInfo> _artistList;

  @override
  void initState() {
    super.initState();

    WidgetsBinding.instance.addPostFrameCallback((_) async {
      List<ArtistInfo> artistList = await MusicScanner.getAllArtist();
      if (!mounted) return;
      setState(() {
        _artistList = artistList;
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    return ListView.builder(
      itemCount: _artistList?.length ?? 0,
      itemBuilder: (context, index) {
        ArtistInfo artistInfo = _artistList[index];
        return ListTile(
          onTap: () {
            MusicScanner.getMusicsByArtistId(artistInfo.id).then((value) {
              value.forEach((f) {
                print(f.toJson());
              });
            });
          },
          leading: AspectRatio(
            aspectRatio: 1.0,
            child: Container(
              alignment: Alignment.center,
              margin: EdgeInsets.all(4.0),
              decoration: BoxDecoration(
                color: Theme.of(context).primaryColor,
                borderRadius: BorderRadius.circular(8.0),
              ),
              child: Text(
                artistInfo.name[0],
                style: TextStyle(
                  fontSize: 28.0,
                  color: Colors.white,
                ),
              ),
            ),
          ),
          title: Text(artistInfo.name),
          trailing: Text(
            '${artistInfo.tracks}首',
            style: TextStyle(color: Colors.black54),
          ),
        );
      },
    );
  }
}
