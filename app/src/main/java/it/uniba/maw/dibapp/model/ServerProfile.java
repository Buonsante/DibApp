package it.uniba.maw.dibapp.model;

/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.bluetooth.BluetoothGattService;

import java.util.Calendar;
import java.util.UUID;

/**
 * Implementation of the Bluetooth GATT Time Profile.
 * https://www.bluetooth.com/specifications/adopted-specifications
 */
public class ServerProfile {

    /* Current Time Service UUID */
    public static UUID SERVER_SERVICE = UUID.fromString("00001805-0000-1000-8000-00805f9b34fb");


    /**
     * Return a configured {@link BluetoothGattService} instance for the
     * Current Time Service.
     */
    public static BluetoothGattService createServerService() {
        BluetoothGattService service = new BluetoothGattService(SERVER_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        return service;
    }

}
