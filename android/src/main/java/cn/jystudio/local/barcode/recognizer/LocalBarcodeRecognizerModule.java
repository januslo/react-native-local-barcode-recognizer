package cn.jystudio.local.barcode.recognizer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import com.facebook.react.bridge.*;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;

import java.util.*;

public class LocalBarcodeRecognizerModule extends ReactContextBaseJavaModule {
    public static final String BARCODE_CODE_TYPE_KEY="codeTypes";

    public static final Map<String, Object> VALID_BARCODE_TYPES =
            Collections.unmodifiableMap(new HashMap<String, Object>() {
                {
                    put("aztec", BarcodeFormat.AZTEC.toString());
                    put("ean13", BarcodeFormat.EAN_13.toString());
                    put("ean8", BarcodeFormat.EAN_8.toString());
                    put("qr", BarcodeFormat.QR_CODE.toString());
                    put("pdf417", BarcodeFormat.PDF_417.toString());
                    put("upc_e", BarcodeFormat.UPC_E.toString());
                    put("datamatrix", BarcodeFormat.DATA_MATRIX.toString());
                    put("code39", BarcodeFormat.CODE_39.toString());
                    put("code93", BarcodeFormat.CODE_93.toString());
                    put("interleaved2of5", BarcodeFormat.ITF.toString());
                    put("codabar", BarcodeFormat.CODABAR.toString());
                    put("code128", BarcodeFormat.CODE_128.toString());
                    put("maxicode", BarcodeFormat.MAXICODE.toString());
                    put("rss14", BarcodeFormat.RSS_14.toString());
                    put("rssexpanded", BarcodeFormat.RSS_EXPANDED.toString());
                    put("upc_a", BarcodeFormat.UPC_A.toString());
                    put("upc_ean", BarcodeFormat.UPC_EAN_EXTENSION.toString());
                }
            });


    public LocalBarcodeRecognizerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    /**
     * @return the name of this module. This will be the name used to {@code require()} this module
     * from javascript.
     */
    @Override
    public String getName() {
        return "LocalBarcodeRecognizer";
    }

    @ReactMethod
    public void decode(String base64Data, ReadableMap options, final Promise p){
        try {
            byte[] decodedString =  Base64.decode(base64Data,Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            Result result = null;
            MultiFormatReader reader = new MultiFormatReader();

            if(options.hasKey(BARCODE_CODE_TYPE_KEY)){
                ReadableArray codeTypes = options.getArray(BARCODE_CODE_TYPE_KEY);
                if(codeTypes.size()>0) {
                    EnumMap<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
                    EnumSet<BarcodeFormat> decodeFormats = EnumSet.noneOf(BarcodeFormat.class);
                    for(int i=0;i<codeTypes.size();i++){
                        String code = codeTypes.getString(i);
                        String formatString = (String) VALID_BARCODE_TYPES.get(code);
                        if(formatString!=null){
                            decodeFormats.add(BarcodeFormat.valueOf(formatString));
                        }
                    }
                    hints.put(DecodeHintType.POSSIBLE_FORMATS,decodeFormats);
                    reader.setHints(hints);
                }
            }

            try {
                BinaryBitmap bitmap = generateBitmapFromImageData(decodedByte);
                result = reader.decode(bitmap);
            } catch (NotFoundException e) {
                BinaryBitmap bitmap = generateBitmapFromImageData(rotateImage(decodedByte,90));
                try {
                    result = reader.decode(bitmap);
                } catch (NotFoundException e1) {
                    //no barcode Found
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }

            p.resolve(result!=null?result.getText():"");
        }catch (Exception e){
            p.reject(e);
        }
    }

    private BinaryBitmap generateBitmapFromImageData(Bitmap bitmap) {
        int[] mImageData = new int[bitmap.getWidth()*bitmap.getHeight()];
        bitmap.getPixels(mImageData, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(),mImageData);
        return new BinaryBitmap(new HybridBinarizer(source));
    }

    private Bitmap rotateImage(Bitmap src, float degree)
    {
        // create new matrix
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        Bitmap bmp = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return bmp;
    }
}