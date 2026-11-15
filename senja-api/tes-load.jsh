try { Class.forName("com.senja.api.sync.SyncTables$T"); System.out.println(">>> NESTED OK"); }
catch (Throwable t) { System.out.println(">>> NESTED GAGAL: " + t); }
try { Class.forName("com.senja.api.sync.SyncTables"); System.out.println(">>> OUTER OK"); }
catch (Throwable t) { System.out.println(">>> OUTER GAGAL: " + t); }
/exit
