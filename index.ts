import { NativeEventEmitter, NativeModules } from 'react-native';

const { C72RfidScanner } = NativeModules;

const eventEmitter = new NativeEventEmitter(C72RfidScanner);

type initializeReader = () => void

type deInitializeReader = () => void

type readSingleTag = () => Promise<any>

type readPower = () => Promise<any>

type changePower = (powerValue: any) => Promise<any>

type AddListener = (cb: (args: any[]) => void) => void

export const initializeReader: initializeReader = () => C72RfidScanner.initializeReader();

export const deInitializeReader: deInitializeReader  = () => C72RfidScanner.deInitializeReader();

export const readSingleTag: readSingleTag = () => C72RfidScanner.readSingleTag();

export const startReadingTags = (callback: (args: any[]) => any) => C72RfidScanner.startReadingTags(callback);

export const stopReadingTags = (callback: (args: any[]) => any) => C72RfidScanner.stopReadingTags(callback);

export const readPower = () => C72RfidScanner.readPower();

export const returnReadTags = () => C72RfidScanner.returnReadTags();

export const changePower: changePower = (powerValue: any) => C72RfidScanner.changePower(powerValue);

export const powerListener: AddListener = (listener) =>
    eventEmitter.addListener("UHF_POWER", listener);

export const tagListener: AddListener = (listener) =>
    eventEmitter.addListener("UHF_TAG", listener);

export default {
    powerListener,
    tagListener,
    initializeReader,
    readSingleTag,
    startReadingTags,
    stopReadingTags,
    returnReadTags,
    readPower,
    changePower,
    deInitializeReader
}