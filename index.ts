import { NativeEventEmitter, NativeModules } from 'react-native';

const { C72RfidScanner } = NativeModules;

const eventEmitter = new NativeEventEmitter(C72RfidScanner);

type initializeReader = () => void

type deInitializeReader = () => void

type AddListener = (cb: (args: any[]) => void) => void

export const initializeReader: initializeReader = () => C72RfidScanner.initializeReader();

export const deInitializeReader: deInitializeReader  = () => C72RfidScanner.deInitializeReader();

export const powerListener: AddListener = (listener) =>
    eventEmitter.addListener("UHF_POWER", listener);

export const tagListener: AddListener = (listener) =>
    eventEmitter.addListener("UHF_TAG", listener);

export default {
    initializeReader,
    deInitializeReader
}