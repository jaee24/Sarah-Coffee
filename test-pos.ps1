# ============================================================
# test-pos.ps1 — Uji tahap POS (transaksi + diskon) senja-api
# Pakai:  .\test-pos.ps1 -ProdukId "<uuid PR-02 dari phpMyAdmin>"
# ID produk: SELECT id FROM produk WHERE kode='PR-02';  (db: senja)
# ============================================================
param(
    [Parameter(Mandatory = $true)]
    [string]$ProdukId
)

 $base = "http://localhost:8080"
 $script:pass = 0
 $script:fail = 0

function Api($Method, $Uri, $Body, $Headers) {
    try {
        $p = @{ Method = $Method; Uri = $Uri; ErrorAction = "Stop" }
        if ($Headers) { $p.Headers = $Headers }
        if ($null -ne $Body) {
            $p.ContentType = "application/json"
            $p.Body = if ($Body -is [System.Collections.IDictionary]) {
                # hashtable: serialisasi biasa, TAPI kunci yang bernilai array harus tetap array.
                # ConvertTo-Json di PS5.1 menangani ini benar SELAMA kita tidak mem-pipe.
                ConvertTo-Json -InputObject $Body -Depth 10
            } else {
                ConvertTo-Json -InputObject @($Body) -Depth 10
            }
        }
        Invoke-RestMethod @p
    } catch {
        $pesan = $_.ErrorDetails.Message
        if (-not $pesan -and $_.Exception.Response) {
            try {
                $stream = $_.Exception.Response.GetResponseStream()
                $reader = New-Object System.IO.StreamReader($stream)
                $pesan = $reader.ReadToEnd()
            } catch { $pesan = $_.Exception.Message }
        }
        Write-Host "    [debug] HTTP $([int]$_.Exception.Response.StatusCode): $pesan" -ForegroundColor DarkGray
        [pscustomobject]@{
            __error = $true
            status  = [int]$_.Exception.Response.StatusCode
            pesan   = $pesan
        }
    }
}
function Cek($label, $hasil) {
    if ($hasil) { Write-Host "  PASS  $label" -ForegroundColor Green; $script:pass++ }
    else        { Write-Host "  GAGAL $label" -ForegroundColor Red;   $script:fail++ }
}

Write-Host "`n== 1. Login kasir ==" -ForegroundColor Cyan
 $lg = Api POST "$base/api/auth/login" @{ kode = "KSR01"; password = "kasir123" }
 $HK = @{ Authorization = "Bearer $($lg.token)" }
Cek "login dapat token" (-not $lg.__error -and $lg.token.Length -gt 30)

Write-Host "`n== 2. Validasi diskon ==" -ForegroundColor Cyan
 $v1 = Api POST "$base/api/discounts/validate" @{ kode = "SENJA10"; subtotal = 66000 } $HK
Cek "SENJA10 valid, potongan 6600" ($v1.valid -eq $true -and $v1.potongan -eq 6600)
 $v2 = Api POST "$base/api/discounts/validate" @{ kode = "HEMAT5K"; subtotal = 20000 } $HK
Cek "HEMAT5K ditolak (min 25rb)" ($v2.valid -eq $false)
 $v3 = Api POST "$base/api/discounts/validate" @{ kode = "PROMO20"; subtotal = 60000 } $HK
Cek "PROMO20 ditolak (kedaluwarsa)" ($v3.valid -eq $false)

Write-Host "`n== 3. Buat transaksi (cash + SENJA10, Cold-Large x2) ==" -ForegroundColor Cyan
 $tid = [guid]::NewGuid().ToString()
 $trx = Api POST "$base/api/transactions" @{
    id = $tid; metode = "cash"; diskon_kode = "SENJA10"; dibayar = 70000
    items = @( @{ produk_id = $ProdukId; qty = 2; opsi = "Cold - Large"; harga = 33000 } )
} $HK
 $tx = $trx.transaksi
Cek "transaksi tersimpan"        (-not $trx.__error -and $trx.idempotent -eq $false)
Cek "subtotal 66000"             ($tx.subtotal -eq 66000)
Cek "diskon 6600 (SENJA10)"      ($tx.diskon -eq 6600 -and $tx.diskon_kode -eq "SENJA10")
Cek "total 59400"                ($tx.total -eq 59400)
Cek "kembalian 10600"            ($tx.kembalian -eq 10600)
Cek "detail 1 baris, HPP terisi" ($trx.detail.Count -eq 1 -and $trx.detail[0].hpp -gt 0)
Cek "mutasi stok tercatat"       ($trx.mutasi.Count -ge 3)
if ($trx.warnings) { Write-Host "  (peringatan stok: $($trx.warnings -join '; '))" -ForegroundColor Yellow }

Write-Host "`n== 4. Idempotensi (POST ulang id sama) ==" -ForegroundColor Cyan
 $rep = Api POST "$base/api/transactions" @{
    id = $tid; metode = "cash"; dibayar = 70000
    items = @( @{ produk_id = $ProdukId; qty = 2; opsi = "Cold - Large"; harga = 33000 } )
} $HK
Cek "replay dikenali idempotent" ($rep.idempotent -eq $true)
Cek "kode transaksi sama"        ($rep.transaksi.id -eq $tid -and $rep.transaksi.kode -eq $tx.kode)

Write-Host "`n== 5. Validasi harga oleh server ==" -ForegroundColor Cyan
 $bad = Api POST "$base/api/transactions" @{
    id = [guid]::NewGuid().ToString(); metode = "cash"; dibayar = 70000
    items = @( @{ produk_id = $ProdukId; qty = 1; opsi = ""; harga = 1000 } )
} $HK
Cek "harga palsu ditolak 400" ($bad.__error -eq $true -and $bad.status -eq 400)

Write-Host "`n== 6. QRIS (non-cash dibayar pas) ==" -ForegroundColor Cyan
 $tid2 = [guid]::NewGuid().ToString()
 $qr = Api POST "$base/api/transactions" @{
    id = $tid2; metode = "qris"
    items = @( @{ produk_id = $ProdukId; qty = 1; opsi = ""; harga = 28000 } )
} $HK
Cek "qris: dibayar = total, kembalian 0" ($qr.transaksi.dibayar -eq 28000 -and $qr.transaksi.kembalian -eq 0)

Write-Host "`n== 7. List, detail, struk ==" -ForegroundColor Cyan
 $ls = Api GET "$base/api/transactions?limit=5" $null $HK
Cek "list transaksi kasir" ($ls.total -ge 2)
 $dt = Api GET "$base/api/transactions/$tid" $null $HK
Cek "detail transaksi by id" ($dt.transaksi.id -eq $tid)
 $rc = Api GET "$base/api/transactions/$tid/receipt" $null $HK
Cek "data struk + info toko" ($null -ne $rc.toko.nama)

Write-Host "`n== 8. Pembatalan ==" -ForegroundColor Cyan
 $c1 = Api POST "$base/api/transactions/$tid/cancel" $null $HK
Cek "kasir dilarang cancel (403)" ($c1.__error -eq $true -and $c1.status -eq 403)
 $lgA = Api POST "$base/api/auth/login" @{ kode = "ADM01"; password = "admin123" }
 $HA  = @{ Authorization = "Bearer $($lgA.token)" }
 $c2  = Api POST "$base/api/transactions/$tid/cancel" $null $HA
Cek "admin cancel sukses"        (-not $c2.__error -and $c2.transaksi.status -eq "dibatalkan")
Cek "mutasi kompensasi (masuk)"  ($c2.mutasi.Count -ge 3)
 $c3  = Api POST "$base/api/transactions/$tid/cancel" $null $HA
Cek "cancel kedua ditolak (400)" ($c3.__error -eq $true -and $c3.status -eq 400)

Write-Host "`n==================================" -ForegroundColor Cyan
Write-Host "HASIL: $script:pass PASS, $script:fail GAGAL" -ForegroundColor $(if ($script:fail -eq 0) { "Green" } else { "Red" })