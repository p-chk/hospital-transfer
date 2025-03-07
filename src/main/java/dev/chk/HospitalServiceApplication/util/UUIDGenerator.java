package dev.chk.HospitalServiceApplication.util;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UUIDGenerator {
    public static String getNewUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
