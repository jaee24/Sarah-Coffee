package com.senja.pos;

import com.senja.core.SyncMeta;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/** Klien REST senja-api: login token, GET/POST dengan Authorization. */
public final class ApiClient {

    public static class ApiException extends RuntimeException {
        public final int status;
        public ApiException(int status,String msg){ super(msg); this.status=status; }
    }

    private static final HttpClient HTTP=HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(4)).build();
    private static final ObjectMapper M=new ObjectMapper();

    private ApiClient() {}

    public static String server(){ return SyncMeta.get("server_url","http://localhost:8080"); }
    public static boolean hasToken(){ return token()!=null&&!token().isBlank(); }
    static String token(){ return SyncMeta.get("pos_token"); }
    static void setToken(String t){ SyncMeta.set("pos_token",t); }
    public static void clearToken(){ SyncMeta.set("pos_token",""); }

    public static boolean online(){
        try{
            HttpRequest r=HttpRequest.newBuilder(URI.create(server()+"/api/health"))
                    .timeout(Duration.ofSeconds(3)).GET().build();
            return HTTP.send(r,HttpResponse.BodyHandlers.ofString()).statusCode()==200;
        }catch(Exception e){ return false; }
    }

    /** Login via REST. Simpan token. Melempar ApiException bila kredensial salah. */
    public static Map<String,Object> login(String kode,String password){
        Map<String,Object> resp=send("POST","/api/auth/login",
                Map.of("kode",kode,"password",password),false);
        setToken(String.valueOf(resp.get("token")));
        @SuppressWarnings("unchecked")
        Map<String,Object> user=(Map<String,Object>)resp.get("user");
        return user;
    }

    public static Map<String,Object> get(String path){ return send("GET",path,null,true); }
    public static Map<String,Object> post(String path,Object body){ return send("POST",path,body,true); }

    private static Map<String,Object> send(String method,String path,Object body,boolean withAuth){
        try{
            HttpRequest.Builder b=HttpRequest.newBuilder(URI.create(server()+path))
                    .timeout(Duration.ofSeconds(15))
                    .header("Content-Type","application/json");
            if(withAuth&&hasToken()) b.header("Authorization","Bearer "+token());
            if("GET".equals(method)) b.GET();
            else b.POST(HttpRequest.BodyPublishers.ofString(M.writeValueAsString(body==null?Map.of():body)));
            HttpResponse<String> resp=HTTP.send(b.build(),HttpResponse.BodyHandlers.ofString());
            Map<String,Object> parsed=M.readValue(resp.body(),
                    new TypeReference<Map<String,Object>>(){}); // ApiErrorHandler selalu balas JSON
            if(resp.statusCode()/100!=2){
                if(resp.statusCode()==401&&withAuth) clearToken();
                throw new ApiException(resp.statusCode(),
                        String.valueOf(parsed.getOrDefault("pesan",parsed.getOrDefault("error","HTTP "+resp.statusCode()))));
            }
            return parsed;
        }
        catch(ApiException e){ throw e; }
        catch(Exception e){ // koneksi gagal = offline
            throw new ApiException(0,"OFFLINE: "+e.getMessage());
        }
    }
}