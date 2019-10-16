package com.nut2014.baselibrary.uitls;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import androidx.core.content.ContextCompat;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.UUID;

/**
 * @author feiltel 2019/9/29 0029
 */
public class DeviceUuidFactory {
    private static final String PREFS_FILE = "device_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static volatile UUID uuid;

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static UUID getDeviceUuid(Context context) {
        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context
                            .getSharedPreferences(PREFS_FILE, 0);
                    final String id = prefs.getString(PREFS_DEVICE_ID, null);
                    if (id != null) {
                        // Use the ids previously computed and stored in the
                        // prefs file
                        uuid = UUID.fromString(id);
                    } else {
                        final String androidId = Settings.Secure.getString(
                                context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        if (!"9774d56d682e549c".equals(androidId)) {
                            uuid = UUID.nameUUIDFromBytes(androidId
                                    .getBytes(StandardCharsets.UTF_8));
                        } else {
                            String deviceId = null;
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                deviceId = ((TelephonyManager)
                                        Objects.requireNonNull(context.getSystemService(
                                                Context.TELEPHONY_SERVICE)))
                                        .getDeviceId();

                            }
                            uuid = deviceId != null ? UUID
                                    .nameUUIDFromBytes(deviceId
                                            .getBytes(StandardCharsets.UTF_8)) : UUID
                                    .randomUUID();

                        }
                        // Write the value out to the prefs file
                        prefs.edit()
                                .putString(PREFS_DEVICE_ID, uuid.toString())
                                .apply();
                    }
                }
            }
        }
        return uuid;
    }


}
