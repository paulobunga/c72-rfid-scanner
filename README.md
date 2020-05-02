# c72-rfid-scanner

## Getting started

`$ npm install c72-rfid-scanner --save`

### Mostly automatic installation

`$ react-native link c72-rfid-scanner`

## Usage

```javascript
import C72RfidScanner from "c72-rfid-scanner";
```

```JSX
const App = () => {

    const powerListener = (data: any) => {
        console.log(data);
    }

    const tagListener = (data: any) => {
        console.log(data);
    }

    React.useEffect(() => {
        const scanner = C72RfidScanner;
        scanner.initializeReader();
        scanner.powerListener(powerListener);
        scanner.tagListener(tagListener);
        return () => scanner.deInitializeReader();
    }, []);

    const readPower = async () => {
        C72RfidScanner.readPower().then((result: any) => console.log(result))
    }

    const scanSingleTag = async () => {
    C72RfidScanner.readSingleTag().then((result: any) => console.log(result))
  }

  const startReading = () => {
    C72RfidScanner.startReadingTags(function success(message) {
      //Returns True 
      console.log(message);
    })
  }

  const stopReading = () => {
    C72RfidScanner.stopReadingTags(function success(message) {
      //Returns the number of scanned tags (size of scannertags array)
      console.log(message);
    })
  }
    
}

export default App;
```
