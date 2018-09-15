
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <AVFoundation/AVFoundation.h>
#import "RNReactNativeLocalBarcodeRecognizer.h"
#import <ZXingObjC/ZXingObjC.h>
@implementation RNReactNativeLocalBarcodeRecognizer

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}

+ (NSDictionary *)validCodeTypes
{
    return @{
             @"upc_e" : AVMetadataObjectTypeUPCECode,
             @"code39" : AVMetadataObjectTypeCode39Code,
             @"code39mod43" : AVMetadataObjectTypeCode39Mod43Code,
             @"ean13" : AVMetadataObjectTypeEAN13Code,
             @"ean8" : AVMetadataObjectTypeEAN8Code,
             @"code93" : AVMetadataObjectTypeCode93Code,
             @"code128" : AVMetadataObjectTypeCode128Code,
             @"pdf417" : AVMetadataObjectTypePDF417Code,
             @"qr" : AVMetadataObjectTypeQRCode,
             @"aztec" : AVMetadataObjectTypeAztecCode,
             @"interleaved2of5" : AVMetadataObjectTypeInterleaved2of5Code,
             @"itf14" : AVMetadataObjectTypeITF14Code,
             @"datamatrix" : AVMetadataObjectTypeDataMatrixCode
             };
}

RCT_EXPORT_MODULE(LocalBarcodeRecognizer);

RCT_EXPORT_METHOD(decode:(NSString *)base64EncodedImage
                  options:(NSDictionary *)options
                  resolver:(RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
    UIImage* image =[self decodeBase64ToImage:base64EncodedImage];
    ZXLuminanceSource *source = [[ZXCGImageLuminanceSource alloc] initWithCGImage:image.CGImage];
    ZXBinaryBitmap *bitmap = [ZXBinaryBitmap binaryBitmapWithBinarizer:[ZXHybridBinarizer binarizerWithSource:source]];
    // There are a number of hints we can give to the reader, including
    // possible formats, allowed lengths, and the string encoding.
    ZXDecodeHints *hints = [ZXDecodeHints hints];
    NSError *error = nil;
    ZXMultiFormatReader *reader = [ZXMultiFormatReader reader];
    ZXResult *result = [reader decode:bitmap
                                hints:hints
                                error:&error];
    if (result) {
        // The coded result as a string. The raw data can be accessed with
        // result.rawBytes and result.length.
        NSString *contents = result.text;
        
        // The barcode format, such as a QR code or UPC-A
        //ZXBarcodeFormat format = result.barcodeFormat;
        resolve(contents);
    } else {
        // Use error to determine why we didn't get a result, such as a barcode
        // not being found, an invalid checksum, or a format inconsistency.
        resolve(@"");
    }
    
}

- (UIImage *)decodeBase64ToImage:(NSString *)strEncodeData {
    NSData *data = [[NSData alloc]initWithBase64EncodedString:strEncodeData options:NSDataBase64DecodingIgnoreUnknownCharacters];
    return [UIImage imageWithData:data];
}

@end
  
