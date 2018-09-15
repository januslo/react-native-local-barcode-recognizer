###react-native-local-barcode-recognizer
## *** Still under development.

##Install:
```bash
#install:
npm install react-native-local-barcode-recognizer

#and link:
react-native link react-native-local-barcode-recognizer

```

or you may install manually.

##Usage:
Here is the demo (may check the examples folder of source code as well)

```typescript 
import ...

import LocalBarcodeRecognizer from 'react-native-local-barcode-recognizer';


const imageBase64 = "data:image/jpeg;base64,/9j/4AA.......";

type Props = {};
export default class App extends Component<Props> {
  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>React Native Local Barcode Recoginzer Demo</Text>
        <Text>Follow images to test:</Text>
        <Image source={{uri:imageBase64}} style={{width:width,height:width}}></Image>
          <Button onPress={()=>{
              this.recoginze();
          }} title={"Recognize"} />
      </View>
    );
  }

  recoginze = async ()=>{
	// Here is the demoe
     let result = await LocalBarcodeRecognizer.decode(imageBase64.replace("data:image/jpeg;base64,",""),{codeTypes:['ean13','qr']});
     alert(result);
  }

}
...
});


```

## API
```javascript

 let result = await LocalBarcodeRecognizer.decode(base64EncodeStringWithSchema,options);


```

## Options
Only codeTypes supports currently
Options:

| name | desc |
|:----:|:----:|
| codeTypes | the codeFormat array, no default values,at last one of follow values needed: aztec ean13 ean8 qr pdf417 upc_e datamatrix code39 code93 interleaved2of5 codabar code128 maxicode rss14 rssexpanded upc_a upc_ean   |
