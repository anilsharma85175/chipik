package com.chipik.util;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;

/**
 * Utility for fetching details about the device.
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/6/14.
 */
public class DeviceInfo {

    /**
     * Get the device's manufacturer name string.
     * @return
     */
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    /**
     * Get the device's UUID
     * @return
     */
    public static String getDeviceUUID(){
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        String uuid = adapter.getAddress();
        return uuid;
    }

    /**
     * Internal helper to capitalize the string properly.
     * @param s
     * @return
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * Is this application an emulator?
     * @return
     */
    public static boolean isEmulator(){
        String brand = Build.BRAND;
        if (brand.equalsIgnoreCase("generic")) {
            return true;
        } else {
            return false;
        }
    }
}
