
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

@interface RNReactNativeLocalBarcodeRecognizer : NSObject <RCTBridgeModule>
- (dispatch_queue_t)methodQueue;
+ (NSDictionary *)validCodeTypes;
- (UIImage *)decodeBase64ToImage:(NSString *)strEncodeData;
@end
  
