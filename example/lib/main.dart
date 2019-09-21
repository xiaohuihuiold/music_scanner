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
    _controller = TabController(length: 2, vsync: this);
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
          ],
        ),
        appBar: AppBar(
          title: const Text('Music Scanner Example'),
          bottom: TabBar(
            controller: _controller,
            tabs: [
              Tab(text: "音乐"),
              Tab(text: "专辑"),
            ],
          ),
        ),
        body: TabBarView(
          controller: _controller,
          children: [
            MusicPage(),
            AlbumPage(),
          ],
        ),
      ),
    );
  }
}

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
            print(musicInfo.albumPath);
          },
          leading: AspectRatio(
            aspectRatio: 1.0,
            child: Image.file(
              File(musicInfo.albumPath),
              fit: BoxFit.cover,
            ),
          ),
          title: Text(musicInfo.title),
          subtitle: Text(musicInfo.album),
        );
      },
    );
  }
}

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
            child: albumInfo.path != null
                ? Image.file(
                    File(albumInfo.path),
                    fit: BoxFit.cover,
                  )
                : CircleAvatar(
                    child: Icon(Icons.music_note),
                    backgroundColor: Colors.grey,
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
