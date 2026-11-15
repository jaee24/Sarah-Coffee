package com.senja.core;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** UUID deterministik (untuk seed) & UUID acak (untuk data baru). */
public final class Uuid {
    private Uuid() {}

    /** Input sama → UUID sama di semua device (kunci seed idempoten). */
    public static String s(String key) {
        return UUID.nameUUIDFromBytes(("senja:" + key).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public static String rand() { return UUID.randomUUID().toString(); }
}