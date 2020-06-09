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


  const [isReading, setIsReading] = React.useState();

  const [powerState, setPowerState] = React.useState('');

  const [tags, setTags] = React.useState([]);

  const showAlert = (title, data) => {
    Alert.alert(
      title,
      data,
      [
        { text: 'OK', onPress: () => console.log('OK Pressed') },
      ],
      { cancelable: false },
    );
  }

  const powerListener = (data) => {
    //console.log(data);
    setPowerState(data);
  }

  const tagListener = (data) => {
    //rssi = data[1] epc = data[0] //Iam only interested in the EPC tag
    setTags(tags => tags.concat(data[0]));
  }

  React.useEffect(() => {
    const scanner = C72RFIDScanner;
    scanner.initializeReader();
    scanner.powerListener(powerListener);
    scanner.tagListener(tagListener);
    return () => scanner.deInitializeReader();
  }, []);

  const readPower = async () => {
    try {
      let result = await C72RFIDScanner.readPower();
      showAlert('SUCCESS', `The result is ${result}`);
      console.log(result);
    } catch (error) {
      showAlert('FAILED', error.message);
    }
  }

  const scanSingleTag = async () => {
    try {
      let result = await C72RFIDScanner.readSingleTag();
      showAlert('SUCCESS', `The result is ${result}`);
      console.log(result);
    } catch (error) {
      showAlert('FAILED', error.message);
    }
  }

  const startReading = () => {
    C72RFIDScanner.startReadingTags(function success(message) {
      setIsReading(message);
    })
  }

  const stopReading = () => {
    C72RFIDScanner.stopReadingTags(function success(message) {
      setIsReading(false);
    });

    /**
     * When I stop scanning I immediately return the tags in my state 
     * (You could render a view or do whatever you want with the data)
     */
    console.log(tags);
  }

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>

      <View>
        <Text>{powerState}</Text>
      </View>

      <View style={{ marginVertical: 20 }}>
        <Button style={{ margin: 10 }} onPress={() => readPower()} title='Read Power' />
      </View>

      <View style={{ marginVertical: 20 }}>
        <Button style={{ margin: 10 }} onPress={() => scanSingleTag()} title='Read Single Tag' />
      </View>

      <View style={{ marginVertical: 20 }}>
        <Button disabled={isReading} style={{ margin: 10 }} onPress={() => startReading()} title='Start Bulk Scan' />
      </View>

      <View style={{ marginVertical: 20 }}>
        <Button disabled={!isReading} style={{ margin: 10 }} onPress={() => stopReading()} title='Stop Bulk Scan' />
      </View>

    </View>
  );

}

export default App;
```
