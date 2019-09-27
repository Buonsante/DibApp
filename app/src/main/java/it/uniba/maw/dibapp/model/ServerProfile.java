package it.uniba.maw.dibapp.model;


import android.bluetooth.BluetoothGattService;

import java.util.Calendar;
import java.util.UUID;


public class ServerProfile {

    /* Service UUID */
    public static UUID SERVER_SERVICE = UUID.fromString("00001805-0000-1000-8000-00805f9b34fb");



    public static BluetoothGattService createServerService() {

        BluetoothGattService service = new BluetoothGattService(SERVER_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        return service;
    }

}
