package com.senja.core;

import java.util.Map;

public record SyncResult(
        boolean online,
        Map<String, Integer> pushed,   // tabel → jumlah baris diterima server
        Map<String, Integer> pulled,   // tabel → jumlah baris diterima dari server
        long serverTime,
        String error) {

    public static SyncResult offline(String msg) {
        return new SyncResult(false, Map.of(), Map.of(), 0, msg);
    }

    public int totalPushed() { return pushed.values().stream().mapToInt(Integer::intValue).sum(); }
    public int totalPulled() { return pulled.values().stream().mapToInt(Integer::intValue).sum(); }
}