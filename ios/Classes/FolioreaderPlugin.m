#import "FolioreaderPlugin.h"
#if __has_include(<folioreader/folioreader-Swift.h>)
#import <folioreader/folioreader-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "folioreader-Swift.h"
#endif

@implementation FolioreaderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFolioreaderPlugin registerWithRegistrar:registrar];
}
@end
