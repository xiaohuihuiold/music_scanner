#import "MusicScannerPlugin.h"
#import <music_scanner/music_scanner-Swift.h>

@implementation MusicScannerPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMusicScannerPlugin registerWithRegistrar:registrar];
}
@end
