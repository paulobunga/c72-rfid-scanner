package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.RFIDWithUHF.BankEnum;

public class C72RfidScannerModule extends ReactContextBaseJavaModule Implements LifecycleEventListener {

    private static final String UHF_READER_POWER_ON_ERROR = "UHF_READER_POWER_ON_ERROR";
    private static final String UHF_READER_INIT_ERROR = "UHF_READER_INIT_ERROR";
    private static final String UHF_READER_READ_ERROR = "UHF_READER_READ_ERROR";
    private static final String UHF_READER_RELEASE_ERROR = "UHF_READER_RELEASE_ERROR";
    private static final String UHF_READER_WRITE_ERROR = "UHF_READER_WRITE_ERROR";
    private static final String UHF_READER_OTHER_ERROR = "UHF_READER_OTHER_ERROR";
    private RFIDWithUHF mReader = null;
    private Boolean mReaderStatus = false;
    private List<String> scannedTags = new ArrayList<String>();
    private Boolean uhfInventoryStatus = false;
    private String deviceName = "";
    
    private final ReactApplicationContext reactContext;

    public C72RfidScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        this.reactContext.addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return "C72RfidScanner";
    }

    @Override
    public void onHostDestroy() {
        new UhfReaderPower(false).start();
    }

    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    private int count = 0;

    private void sendEvent(String eventName, @Nullable WritableArray array) {
        getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, array);
    }

    private void sendEvent(String eventName, @Nullable String status) {
        getReactApplicationContext()
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
            .emit(eventName, status);
    }

    @ReactMethod
    private void initializeReader() {
        new UhfReaderPower().start();
    }

    @ReactMethod
    public void releaseReader() {
        new UhfReaderPower(false).start();
    }

    @ReactMethod
    public void readSingleTag(final Promise promise) {
        try {
        String[] tag = mReader.inventorySingleTagEPC_TID_USER();
        if(tag!= null && tag.length > 1) {
            promise.resolve(convertArrayToWritableArray(tag));
        }
        else {
            promise.reject(UHF_READER_READ_ERROR, "READ FAILED!");
        }
        } catch (Exception e) {
        promise.reject(UHF_READER_READ_ERROR, e);
        }
    }

    @ReactMethod
    public void startReadingTags(final Callback callback) {
        uhfInventoryStatus = mReader.startInventoryTag(0,0);
        new TagThread().start();
        callback.invoke(uhfInventoryStatus);
    }

    @ReactMethod
    public void findTag(final String findEpc, final Callback callback) {
        uhfInventoryStatus = mReader.startInventoryTag(0,0);
        new TagThread(findEpc).start();
        callback.invoke(uhfInventoryStatus);
    }

    @ReactMethod
    public void stopReadingTags(final Callback callback) {
        uhfInventoryStatus = !(mReader.stopInventory());
        callback.invoke(scannedTags.size());
    }

    @ReactMethod
    public void readPower(final Promise promise) {
        int uhfPower = mReader.getPower();
        if(uhfPower>=0)
        promise.resolve(uhfPower);
        else
        promise.reject(UHF_READER_OTHER_ERROR, "Can't Read Power");
    }

    @ReactMethod
    public void changePower(int powerValue, final Promise promise) {
        Boolean uhfPowerState = mReader.setPower(powerValue);
        if(uhfPowerState)
        promise.resolve(uhfPowerState);
        else
        promise.reject(UHF_READER_OTHER_ERROR, "Can't Change Power");
    }

    @ReactMethod
    public void writeDataIntoEpc(String epc, final Promise promise) {
        if(epc.length() == (6*4) ) {
        epc += "00000000";
        // Access Password, Bank Enum (EPC(1), TID(2),...), Pointer, Count, Data
        Boolean uhfWriteState = mReader.writeData_Ex("00000000", BankEnum.valueOf("UII"), 2, 6, epc);
        if(uhfWriteState)
            promise.resolve(uhfWriteState);
        else
            promise.reject(UHF_READER_WRITE_ERROR, "Can't Write Data");
        }
        else {
        promise.reject(UHF_READER_WRITE_ERROR, "Invalid Data"); 
        }
    } 

    @ReactMethod
    public void clearAllTags() {
        scannedTags.clear();
    }

    public void addIfNotExists(String[] tid) {
        if (!scannedTags.contains(tid[0])) {
            scannedTags.add(tid[0]);
            tid[1] = mReader.convertUiiToEPC(tid[1]);

        }
    }
}
