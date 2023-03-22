package com.awesomeproject;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CallUpdateWidget extends ReactContextBaseJavaModule {

    public CallUpdateWidget(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CallUpdateWidget";
    }

    @ReactMethod
    public void changeFont(String fontName) {
        WidgetClockDigital.setFont(fontName);
    }

}

