package com.senja.core;

import java.util.List;
import java.util.Map;

/** Konstanta bisnis yang dipakai Admin & Kasir. */
public final class Const {
private Const() {}

    public static final long HARGA_LARGE = 5000;

    /** P = Pagi, S = Sore */
    public static final Map<String, String[]> SHIFT_HOURS = Map.of(
            "P", new String[]{"10:00", "16:30"},
            "S", new String[]{"16:30", "23:00"});
    public static final Map<String, String> SHIFT_LABEL = Map.of("P", "Pagi", "S", "Sore");

    public static final List<String> PG_KATS =
            List.of("Operasional", "Peralatan", "Perawatan", "Transportasi", "Lainnya");
}