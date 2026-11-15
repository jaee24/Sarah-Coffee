package com.senja.api.pos;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/** ID deterministik — WAJIB identik dengan PosIds di senja-core (kunci anti-duplikasi sync). */
public final class PosIds {
    private PosIds() {}

    static String det(String key){
        return UUID.nameUUIDFromBytes(("senja:"+key).getBytes(StandardCharsets.UTF_8)).toString();
    }
    public static String detailId(String trxId,int idx){ return det("txd:"+trxId+":"+idx); }
    public static String mutasiId(String trxId,int idx,String bahanId){ return det("mut:trx:"+trxId+":"+idx+":"+bahanId); }
    public static String mutasiBatalId(String trxId,int idx,String bahanId){ return det("mut:batal:"+trxId+":"+idx+":"+bahanId); }
}