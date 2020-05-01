import { DeviceEventEmitter, NativeModules } from 'react-native';

const { C72RfidScanner } = NativeModules;

powerStatusListener = null;
tagDataListener = null;

export const startListeningForPowerStatus = (powerStatusChangeCallback) => {
    powerStatusListener = DeviceEventEmitter.addListener('UHF_POWER', powerStatusChangeCallback);
}

export const stopListeningForPowerStatus = () => {
    powerStatusListener.remove();
}

export const changeListenerCallback = (processTagCallback) => {
    if(tagDataListener)
        tagDataListener.remove()
    tagDataListener = DeviceEventEmitter.addListener('UHF_TAG', processTagCallback);
}

export const startListeningForTags = (processTagCallback) => {
    tagDataListener = DeviceEventEmitter.addListener('UHF_TAG', processTagCallback);
}
export const stopListeningForTags = () => {
    tagDataListener.remove();
}

export default C72RfidScanner;