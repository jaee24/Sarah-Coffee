package com.senja.core;

/** ID deterministik — WAJIB identik dengan PosIds di senja-api. */
public final class PosIds {
    private PosIds() {}
    public static String detailId(String trxId,int idx){ return Uuid.s("txd:"+trxId+":"+idx); }
    public static String mutasiId(String trxId,int idx,String bahanId){ return Uuid.s("mut:trx:"+trxId+":"+idx+":"+bahanId); }
    public static String mutasiBatalId(String trxId,int idx,String bahanId){ return Uuid.s("mut:batal:"+trxId+":"+idx+":"+bahanId); }
}