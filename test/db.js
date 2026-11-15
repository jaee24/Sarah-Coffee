/* ============================================================
   db.js — SENJA COFFEE · DATABASE & INTI BERSAMA
   Dipakai oleh dua aplikasi terpisah: admin.html & kasir.html.
   Data disimpan di localStorage (key sama) → kedua aplikasi
   selalu membaca/menulis database yang sama, termasuk antar tab.
   ============================================================ */

/* ---------- konstanta ---------- */
const DBKEY='senja_coffee_db_v1';
const HARGA_LARGE=5000;                              // tambahan harga ukuran Large
const SHIFT_HOURS={P:['10:00','16:30'],S:['16:30','23:00']}; // P=Pagi, S=Sore
const SHIFT_LABEL={P:'Pagi',S:'Sore'};
const BULAN=['Jan','Feb','Mar','Apr','Mei','Jun','Jul','Agu','Sep','Okt','Nov','Des'];

/* ---------- util ---------- */
const $=s=>document.querySelector(s);
const esc=s=>String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const rp=n=>'Rp '+Math.round(n).toLocaleString('id-ID');
const num=n=>Math.round(n).toLocaleString('id-ID');
const pad=n=>String(n).padStart(2,'0');
const dISO=d=>d.getFullYear()+'-'+pad(d.getMonth()+1)+'-'+pad(d.getDate());
const today=()=>dISO(new Date());
const dOff=n=>{const d=new Date();d.setDate(d.getDate()+n);return dISO(d)};
const nowT=()=>{const d=new Date();return pad(d.getHours())+':'+pad(d.getMinutes())};
const nowHMS=()=>{const d=new Date();return pad(d.getHours())+':'+pad(d.getMinutes())+':'+pad(d.getSeconds())};
const fmtD=s=>{if(!s)return'-';const[y,m,d]=s.split('-');return`${+d} ${BULAN[+m-1]} ${y}`};
const fmtDT=s=>fmtD(s.slice(0,10))+', '+s.slice(11,16);
const pnum=v=>{const n=parseFloat(String(v).replace(/[^\d.]/g,''));return isNaN(n)?0:Math.round(n)};
const inRange=(d,f,t)=>(!f||d>=f)&&(!t||d<=t);
function mulberry32(a){return function(){a|=0;a=a+0x6D2B79F5|0;let t=Math.imul(a^a>>>15,1|a);t=t+Math.imul(t^t>>>7,61|t)^t;return((t^t>>>14)>>>0)/4294967296}}
const srch=(val,expr,ph)=>`<div class="search">${ic('search',16)}<input class="inp" placeholder="${ph}" value="${esc(val)}" oninput="${expr}"></div>`;
const emptyBox=(i,l,s='')=>`<div class="empty">${ic(i,34)}<b>${l}</b>${s?`<span>${s}</span>`:''}</div>`;
function dlCSV(name,rows){
  const csv=rows.map(r=>r.map(c=>{c=String(c??'');return /[";\n]/.test(c)?'"'+c.replace(/"/g,'""')+'"':c}).join(';')).join('\r\n');
  const b=new Blob(["\uFEFF"+csv],{type:'text/csv;charset=utf-8'});
  const a=document.createElement('a');a.href=URL.createObjectURL(b);a.download=name;document.body.appendChild(a);a.click();a.remove();
}

/* ---------- ikon SVG ---------- */
const ICONS={
home:'<path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/>',
users:'<path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M22 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/>',
coffee:'<path d="M17 8h1a4 4 0 1 1 0 8h-1"/><path d="M3 8h14v9a4 4 0 0 1-4 4H7a4 4 0 0 1-4-4Z"/><line x1="6" x2="6" y1="2" y2="4"/><line x1="10" x2="10" y1="2" y2="4"/><line x1="14" x2="14" y1="2" y2="4"/>',
package:'<path d="m7.5 4.27 9 5.15"/><path d="M21 8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z"/><path d="m3.3 7 8.7 5 8.7-5"/><path d="M12 22V12"/>',
truck:'<path d="M14 18V6a2 2 0 0 0-2-2H4a2 2 0 0 0-2 2v11a1 1 0 0 0 1 1h2"/><path d="M15 18h-5"/><path d="M19 18h2a1 1 0 0 0 1-1v-3.65a1 1 0 0 0-.22-.62l-3.48-4.35A1 1 0 0 0 17.52 8H14"/><circle cx="17" cy="18" r="2"/><circle cx="7" cy="18" r="2"/>',
cart:'<circle cx="8" cy="21" r="1"/><circle cx="19" cy="21" r="1"/><path d="M2.05 2.05h2l2.66 12.42a2 2 0 0 0 2 1.58h9.78a2 2 0 0 0 1.95-1.57l1.65-7.43H5.12"/>',
receipt:'<path d="M4 2v20l2-1 2 1 2-1 2 1 2-1 2 1 2-1 2 1V2l-2 1-2-1-2 1-2-1-2 1-2-1-2 1Z"/><path d="M8 7h8"/><path d="M8 11h8"/><path d="M8 15h5"/>',
wallet:'<path d="M21 12V7H5a2 2 0 0 1 0-4h14v4"/><path d="M3 5v14a2 2 0 0 0 2 2h16v-5"/><path d="M18 12a2 2 0 0 0 0 4h4v-4Z"/>',
chart:'<path d="M3 3v18h18"/><rect x="7" y="12" width="3" height="6" rx="1"/><rect x="12" y="8" width="3" height="10" rx="1"/><rect x="17" y="4" width="3" height="14" rx="1"/>',
trending:'<polyline points="22 7 13.5 15.5 8.5 10.5 2 17"/><polyline points="16 7 22 7 22 13"/>',
layers:'<path d="m12 2 9 5-9 5-9-5 9-5Z"/><path d="m3 12 9 5 9-5"/><path d="m3 17 9 5 9-5"/>',
clipboard:'<rect x="8" y="2" width="8" height="4" rx="1"/><path d="M16 4h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2V6a2 2 0 0 1 2-2h2"/>',
clock:'<circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/>',
settings:'<line x1="4" y1="21" x2="4" y2="14"/><line x1="4" y1="10" x2="4" y2="3"/><line x1="12" y1="21" x2="12" y2="12"/><line x1="12" y1="8" x2="12" y2="3"/><line x1="20" y1="21" x2="20" y2="16"/><line x1="20" y1="12" x2="20" y2="3"/><line x1="1" y1="14" x2="7" y2="14"/><line x1="9" y1="8" x2="15" y2="8"/><line x1="17" y1="16" x2="23" y2="16"/>',
globe:'<circle cx="12" cy="12" r="10"/><path d="M2 12h20"/><path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"/>',
bug:'<path d="m8 2 1.88 1.88"/><path d="M14.12 3.88 16 2"/><path d="M9 7.13v-1a3 3 0 1 1 6 0v1"/><path d="M12 20c-3.3 0-6-2.7-6-6v-3a4 4 0 0 1 4-4h4a4 4 0 0 1 4 4v3c0 3.3-2.7 6-6 6"/><path d="M12 20v-9"/><path d="M6.53 9C4.6 8.8 3 7.1 3 5"/><path d="M6 13H2"/><path d="M3 21c0-2.1 1.7-3.9 3.8-4"/><path d="M20.97 5c0 2.1-1.6 3.8-3.5 4"/><path d="M22 13h-4"/><path d="M17.2 17c2.1.1 3.8 1.9 3.8 4"/>',
search:'<circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>',
plus:'<line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/>',
x:'<line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>',
edit:'<path d="M17 3a2.85 2.83 0 1 1 4 4L7.5 20.5 2 22l1.5-5.5Z"/>',
trash:'<path d="M3 6h18"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/><path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/><line x1="10" y1="11" x2="10" y2="17"/><line x1="14" y1="11" x2="14" y2="17"/>',
printer:'<polyline points="6 9 6 2 18 2 18 9"/><path d="M6 18H4a2 2 0 0 1-2-2v-5a2 2 0 0 1 2-2h16a2 2 0 0 1 2 2v5a2 2 0 0 1-2 2h-2"/><rect x="6" y="14" width="12" height="8"/>',
logout:'<path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/>',
check:'<polyline points="20 6 9 17 4 12"/>',
alert:'<path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/><line x1="12" y1="9" x2="12" y2="13"/><line x1="12" y1="17" x2="12.01" y2="17"/>',
eye:'<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>',
eyeoff:'<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/><line x1="3" y1="3" x2="21" y2="21"/>',
qr:'<rect x="3" y="3" width="7" height="7" rx="1"/><rect x="14" y="3" width="7" height="7" rx="1"/><rect x="3" y="14" width="7" height="7" rx="1"/><path d="M14 14h3v3h-3z"/><path d="M21 14h.01"/><path d="M17 21h4v-4"/><path d="M14 21h.01"/>',
cash:'<rect x="2" y="6" width="20" height="12" rx="2"/><circle cx="12" cy="12" r="2"/><path d="M6 12h.01M18 12h.01"/>',
card:'<rect x="2" y="5" width="20" height="14" rx="2"/><line x1="2" y1="10" x2="22" y2="10"/>',
calendar:'<rect x="3" y="4" width="18" height="18" rx="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/>',
tag:'<path d="M12 2H2v10l9.29 9.29a1 1 0 0 0 1.42 0l8.58-8.58a1 1 0 0 0 0-1.42Z"/><circle cx="7" cy="7" r="1.5"/>',
image:'<rect x="3" y="3" width="18" height="18" rx="2"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/>',
dl:'<path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><polyline points="7 10 12 15 17 10"/><line x1="12" y1="15" x2="12" y2="3"/>',
bulb:'<path d="M9 18h6"/><path d="M10 22h4"/><path d="M12 2a7 7 0 0 0-4 12.7c.6.5 1 1.4 1 2.3h6c0-.9.4-1.8 1-2.3A7 7 0 0 0 12 2Z"/>'
};
const ic=(n,s=18)=>`<svg width="${s}" height="${s}" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" style="flex:none">${ICONS[n]||''}</svg>`;

/* ---------- i18n ---------- */
let LANG='id';
const EN={
'Home':'Home','Data Karyawan':'Employees','Absensi':'Attendance','Shift':'Shift','Menu':'Menu','Kategori':'Categories',
'Stok Bahan Baku':'Raw Material Stock','Supplier':'Suppliers','Pembelian':'Purchases','Penjualan':'Sales','Pengeluaran':'Expenses',
'Laporan Penjualan':'Sales Report','Laporan Stok':'Stock Report','Laporan Absensi':'Attendance Report','Laporan Keuangan':'Financial Report',
'Pengaturan':'Settings','Transaksi':'Transactions','Pembayaran':'Payment','Riwayat':'History',
'Simpan':'Save','Batal':'Cancel','Hapus':'Delete','Tutup':'Close','Edit':'Edit','Selesai':'Done','Reset':'Reset','Terapkan':'Apply',
'Cetak Struk':'Print Receipt','Ekspor Excel':'Export Excel','Aksi':'Actions','Tanggal':'Date','Nama':'Name','Jumlah':'Qty',
'Harga':'Price','Subtotal':'Subtotal','Total':'Total','Diskon':'Discount','Kembalian':'Change','Dibayar':'Paid','Metode':'Method',
'Metode Pembayaran':'Payment Method','Kasir':'Cashier','Status':'Status','Simpan Perubahan':'Save Changes','Masuk':'Sign in',
'User ID':'User ID','Password':'Password','Nama User':'User Name','Level':'Level','Nomor HP':'Phone No.','Alamat':'Address',
'All Items':'All Items','Semua':'All','Nama Bahan Baku':'Ingredient Name','Satuan':'Unit','Stok':'Stock','Minimum Stok':'Min. Stock',
'Harga Modal':'Cost Price','Harga Jual':'Selling Price','Penyajian':'Serving','Opsi Minuman':'Drink Options','Jenis':'Type',
'Kode Diskon':'Discount Code','Ringkasan Pesanan':'Order Summary','Lanjut Pembayaran':'Continue to Payment',
'Tambah ke Keranjang':'Add to Cart','Ubah Item':'Update Item','Bayar':'Pay','Transaksi Berhasil':'Payment Successful',
'Produk Terjual Hari Ini':'Products Sold Today','Produk Terlaris':'Best Sellers','Semua Kategori':'All Categories',
'Shift Saat Ini':'Current Shift','Jam Mulai':'Start Time','Jam Selesai':'End Time','Absen Masuk':'Clock In','Absen Pulang':'Clock Out',
'Riwayat Absen':'Attendance History','ID Transaksi':'Transaction ID','Nama Pegawai':'Employee Name','ID Pegawai':'Employee ID',
'Jam Masuk':'Clock-in','Jam Pulang':'Clock-out','Ukuran':'Size','Waktu':'Time','Hari Ini':'Today','Uang Pas':'Exact Amount',
'Total Pendapatan':'Total Revenue','HPP Penjualan':'COGS','Laba Kotor':'Gross Profit','Total Pengeluaran':'Total Expenses',
'Laba Bersih':'Net Profit','Rugi':'Loss','Pendapatan Penjualan':'Sales Revenue','HPP Produk Terjual':'COGS of Sold Products'
};
const t=s=>LANG==='en'?(EN[s]??s):s;

/* ---------- DATABASE ---------- */
let DB=null;
function saveDB(){localStorage.setItem(DBKEY,JSON.stringify(DB))}
function loadDB(){
  try{DB=JSON.parse(localStorage.getItem(DBKEY))}catch(e){DB=null}
  if(!DB||DB.v!==1){DB=seedDB();saveDB()}
  if(!DB._pg2 ){ // migrasi: pengeluaran → jenis & kategori baru
    const MAP={'Karyawan':'Lainnya','Bahan Baku':'Operasional'};
    (DB.pengeluaran||[]).forEach(e=>{
      if(!e.jenis)e.jenis=/servis|rusak|perbaik|ganti|bocor/i.test(e.keterangan||'')?'tak_terduga':'rutin';
      if(MAP[e.kategori])e.kategori=MAP[e.kategori];
      if(typeof e.bukti!=='string')e.bukti='';
    });
    DB._pg2=1;saveDB();

    if(!DB._shiftPS){ // migrasi: shift M (malam) lama → S (sore), lalu dedup
    DB.shift.forEach(s=>{if(s.jenis==='M')s.jenis='S'});
    const seen=new Set();
    DB.shift=DB.shift.filter(s=>{const k=s.uid+'|'+s.tanggal;if(seen.has(k))return false;seen.add(k);return true});
    DB._shiftPS=1;saveDB();
  }
  }
}
const kat=id=>DB.kategori.find(k=>String(k.id)===String(id));
const prod=id=>DB.produk.find(p=>p.id===id);
const bahan=id=>DB.bahan.find(b=>b.id===id);
const user=id=>DB.users.find(u=>u.user_id===id);
const hpp=pid=>DB.resep.filter(r=>r.pid===pid).reduce((a,r)=>{const b=bahan(r.bid);return a+(b?b.harga*r.jumlah:0)},0);
function bestSellers({limit=5,katId=null,jenis=null}={}){
  const m={};
  DB.transaksi.forEach(tx=>DB.transaksi_detail.filter(r=>r.tid===tx.id).forEach(r=>{m[r.pid]=(m[r.pid]||0)+r.qty}));
  let arr=Object.entries(m).map(([pid,qty])=>({p:prod(pid),qty})).filter(x=>x.p);
  if(katId!=null)arr=arr.filter(x=>String(x.p.kid)===String(katId));
  if(jenis)arr=arr.filter(x=>kat(x.p.kid)?.jenis===jenis);
  return arr.sort((a,b)=>b.qty-a.qty).slice(0,limit);
}
const soldOn=ds=>DB.transaksi.filter(x=>x.tanggal.startsWith(ds)).reduce((a,x)=>a+DB.transaksi_detail.filter(r=>r.tid===x.id).reduce((b,r)=>b+r.qty,0),0);
const soldToday=()=>soldOn(today());
const nextId=(arr,pref,len=3)=>{let m=0;arr.forEach(x=>{const n=parseInt(String(x.id||'').replace(/\D/g,''));if(n>m)m=n});return pref+String(m+1).padStart(len,'0')};
function nextTxId(){const ds=today().replace(/-/g,'');const n=DB.transaksi.filter(x=>x.id.includes(ds)).length+1;return`TRX-${ds}-${pad(n)}`}
function newUserId(level){const pre=level==='admin'?'ADM':'KSR';let m=0;DB.users.forEach(u=>{if(u.user_id.startsWith(pre)){const n=+u.user_id.slice(3);if(n>m)m=n}});return pre+pad(m+1)}

function seedDB(){
  const T=today(),d=n=>dOff(n);
  const db={v:1,
    settings:{theme:'light',lang:'id',toko:'SENJA COFFEE',alamat:'Jl. Kenanga No. 12, Yogyakarta',telp:'0812-3456-7890',footer:'Terima kasih sudah mampir!'},
    users:[
      {user_id:'ADM01',nama:'Nadia Prameswari',level:'admin',password:'admin123',no_hp:'081234567001',alamat:'Jl. Melati No. 3, Yogyakarta'},
      {user_id:'KSR01',nama:'Rani Safitri',level:'kasir',password:'kasir123',no_hp:'081234567002',alamat:'Jl. Kenanga No. 7, Yogyakarta'},
      {user_id:'KSR02',nama:'Bagas Pratama',level:'kasir',password:'kasir123',no_hp:'081234567003',alamat:'Jl. Anggrek No. 21, Sleman'}
    ],
    absensi:[],shift:[],kategori:[],produk:[],resep:[],bahan:[],supplier:[],pembelian:[],pengeluaran:[],diskon:[],transaksi:[],transaksi_detail:[],reports:[]
  };
  [['BB-01','Kopi Arabica','gram',1800,500,130],['BB-02','Espresso Blend','gram',2200,500,120],
   ['BB-03','Susu Segar','ml',15000,4000,18],['BB-04','Gula Pasir','gram',6000,1500,15],
   ['BB-05','Sirup Caramel','ml',2600,600,90],['BB-06','Bubuk Matcha','gram',900,250,340],
   ['BB-07','Cokelat Bubuk','gram',1100,300,180],['BB-08','Air Mineral','ml',60000,15000,1],
   ['BB-09','Susu Kental Manis','gram',4200,1000,40],['BB-10','Telur','pcs',80,24,2500],
   ['BB-11','Tepung Terigu','gram',9000,2000,12],['BB-12','Mentega','gram',2400,600,45],
   ['BB-13','Keju Cheddar','gram',1800,500,110],['BB-14','Bun Burger','pcs',45,15,3500],
   ['BB-15','Patty Ayam','pcs',55,20,6500],['BB-16','Es Batu','gram',25000,6000,1],
   ['BB-17','Bubuk Teh Hitam','gram',700,180,90],['BB-18','Pisang Raja','pcs',140,40,2000],
   ['BB-19','Gula Aren','gram',2200,500,35],['BB-20','Minyak Goreng','ml',8000,2000,3],
   ['BB-21','Kentang Beku','gram',5000,1500,28]].forEach(b=>db.bahan.push({id:b[0],nama:b[1],satuan:b[2],stok:b[3],min:b[4],harga:b[5]}));
  db.kategori=[
    {id:1,nama:'Coffee',jenis:'minuman',penyajian:['hot','cold']},
    {id:2,nama:'Non-Coffee',jenis:'minuman',penyajian:['hot','cold']},
    {id:3,nama:'Food',jenis:'makanan',penyajian:[]},
    {id:4,nama:'Cemilan',jenis:'makanan',penyajian:[]}
  ];
  const OM=['regular','large','less_ice','normal_ice','less_sugar','normal_sugar'];
  const P=(id,nama,kid,pen,opsi,harga,resep)=>{
    db.produk.push({id,nama,kid,penyajian:pen,opsi,harga,status:'aktif',gambar:`https://picsum.photos/seed/senja-${id.slice(3)}/600/450.jpg`});
    resep.forEach(([bid,j])=>db.resep.push({pid:id,bid,jumlah:j}));
  };
  P('PR-01','Kopi Susu Gula Aren',1,['hot','cold'],OM,22000,[['BB-02',18],['BB-03',150],['BB-19',25]]);
  P('PR-02','Caramel Latte',1,['hot','cold'],OM,28000,[['BB-02',18],['BB-03',180],['BB-05',20],['BB-08',60]]);
  P('PR-03','Americano',1,['hot','cold'],OM,18000,[['BB-02',18],['BB-08',220]]);
  P('PR-04','Cappuccino',1,['hot','cold'],OM,26000,[['BB-02',18],['BB-03',140]]);
  P('PR-05','Kopi Tubruk',1,['hot'],['regular','less_sugar','normal_sugar'],12000,[['BB-01',12],['BB-08',200]]);
  P('PR-06','Matcha Latte',2,['hot','cold'],OM,26000,[['BB-06',12],['BB-03',150],['BB-04',10]]);
  P('PR-07','Cokelat Hangat',2,['hot','cold'],OM,24000,[['BB-07',20],['BB-03',150],['BB-09',20]]);
  P('PR-08','Teh Tarik',2,['hot','cold'],OM,18000,[['BB-17',8],['BB-03',120],['BB-09',15]]);
  P('PR-09','Lemon Tea',2,['cold'],OM,16000,[['BB-17',10],['BB-08',250],['BB-04',15]]);
  P('PR-10','Chicken Burger',3,[],[],32000,[['BB-14',1],['BB-15',1],['BB-11',50],['BB-12',10],['BB-13',15]]);
  P('PR-11','Kentang Goreng',3,[],[],20000,[['BB-21',200],['BB-20',30]]);
  P('PR-12','Roti Bakar Cokelat Keju',4,[],[],24000,[['BB-11',80],['BB-07',15],['BB-13',20],['BB-12',12]]);
  P('PR-13','Pisang Goreng Keju',4,[],[],22000,[['BB-18',2],['BB-11',60],['BB-13',20],['BB-20',50]]);
  P('PR-14','Donat Gula',4,[],[],12000,[['BB-11',100],['BB-04',15],['BB-12',10],['BB-10',0.25]]);
  P('PR-15','Cireng Bumbu Rujak',4,[],[],15000,[['BB-11',80],['BB-04',10],['BB-20',40]]);
  P('PR-16','Croissant Butter',4,[],[],25000,[['BB-11',100],['BB-12',40]]);
  db.diskon=[
    {id:'D-01',kode:'SENJA10',tipe:'persen',nilai:10,min:30000,mulai:d(-30),exp:d(60),status:'aktif'},
    {id:'D-02',kode:'HEMAT5K',tipe:'nominal',nilai:5000,min:25000,mulai:d(-10),exp:d(30),status:'aktif'},
    {id:'D-03',kode:'PROMO20',tipe:'persen',nilai:20,min:50000,mulai:d(-90),exp:d(-5),status:'aktif'}
  ];
  db.supplier=[
    {id:'SUP-01',nama:'Toko Kopi Makmur',no_hp:'081299887766',alamat:'Jl. Pasar Gede No. 12, Yogyakarta'},
    {id:'SUP-02',nama:'Susu Murni Nusantara',no_hp:'081377665544',alamat:'Jl. Kaliurang KM 5, Sleman'},
    {id:'SUP-03',nama:'Segar Jaya Bakery',no_hp:'081555444333',alamat:'Jl. Solo KM 8, Yogyakarta'}
  ];
  const mkPO=(id,tgl,sup,arr)=>{
    const s=db.supplier.find(x=>x.id===sup);
    const rows=arr.map(([bid,jumlah,harga])=>{const b=db.bahan.find(x=>x.id===bid);return{bid,nama:b.nama,satuan:b.satuan,jumlah,harga,st:jumlah*harga}});
    db.pembelian.push({id,tanggal:tgl,sup,supNama:s.nama,rows,total:rows.reduce((a,r)=>a+r.st,0)});
  };
  mkPO('PO-001',d(-9),'SUP-01',[['BB-01',1000,125],['BB-02',1200,118]]);
  mkPO('PO-002',d(-5),'SUP-02',[['BB-03',10000,17],['BB-09',2000,38]]);
    db.pengeluaran=[
    {id:'EXP-001',tanggal:d(-6),keterangan:'Pembayaran token listrik & air',kategori:'Operasional',jenis:'rutin',jumlah:450000,bukti:''},
    {id:'EXP-002',tanggal:d(-4),keterangan:'Isi ulang LPG 2 tabung',kategori:'Operasional',jenis:'rutin',jumlah:340000,bukti:''},
    {id:'EXP-003',tanggal:d(-3),keterangan:'Bonus penjualan tim',kategori:'Lainnya',jenis:'rutin',jumlah:300000,bukti:''},
    {id:'EXP-004',tanggal:d(-1),keterangan:'Servis mesin espresso & ganti seal',kategori:'Perawatan',jenis:'tak_terduga',jumlah:275000,bukti:''},
    {id:'EXP-005',tanggal:T,keterangan:'Sample biji kopi baru',kategori:'Operasional',jenis:'rutin',jumlah:150000,bukti:''}
  ];
  let sid=0;
  for(let i=-6;i<=13;i++){const ds=d(i);db.shift.push({id:++sid,uid:'KSR01',tanggal:ds,jenis:'P'},{id:++sid,uid:'KSR02',tanggal:ds,jenis:'S'})}
  let aid=0;
  for(let i=1;i<=6;i++){const ds=d(-i);
    db.absensi.push({id:'ABS-'+String(++aid).padStart(3,'0'),uid:'KSR01',tanggal:ds,shift:'P',masuk:i===4?'06:12':'05:5'+(i%5),pulang:'14:0'+(i%6),status:i===4?'terlambat':'hadir'});
    db.absensi.push({id:'ABS-'+String(++aid).padStart(3,'0'),uid:'KSR02',tanggal:ds,shift:'S',masuk:i===2?'14:18':'13:5'+(i%5),pulang:'22:0'+(i%6),status:i===2?'terlambat':'hadir'});
  }
  /* ---- transaksi contoh 14 hari (RNG berbibit agar konsisten) ---- */
  const rnd=mulberry32(20240521);
  const seq={};
  const ops=p=>{
    const k=db.kategori.find(x=>x.id===p.kid);const o=[];
    if(k.jenis==='minuman'){
      if(p.penyajian.length>1)o.push(rnd()<0.35?'Hot':'Cold');else if(p.penyajian[0])o.push(p.penyajian[0]==='hot'?'Hot':'Cold');
      if(p.opsi.includes('regular')||p.opsi.includes('large'))o.push(p.opsi.includes('large')&&rnd()<0.3?'Large':'Regular');
      if(o[0]==='Cold'&&(p.opsi.includes('less_ice')||p.opsi.includes('normal_ice')))o.push(rnd()<0.3?'Less Ice':'Normal Ice');
      if(p.opsi.includes('less_sugar')||p.opsi.includes('normal_sugar'))o.push(rnd()<0.35?'Less Sugar':'Normal Sugar');
    }
    return o.join(' · ');
  };
  const mkTx=(ds,h,m,uid,items,useDisc)=>{
    seq[ds]=(seq[ds]||0)+1;
    const id=`TRX-${ds.replace(/-/g,'')}-${pad(seq[ds])}`;
    const det=items.map(it=>({tid:id,pid:it.p.id,nama:it.p.nama,opsi:it.opsi,qty:it.qty,harga:it.harga,st:it.harga*it.qty}));
    const sub=det.reduce((a,r)=>a+r.st,0);
    let diskon=0,kode='';
    if(useDisc&&sub>=30000&&rnd()<0.3){diskon=Math.round(sub*0.1);kode='SENJA10'}
    const total=sub-diskon;
    const met=rnd()<0.55?'cash':(rnd()<0.5?'qris':'debit');
    const dibayar=met==='cash'?Math.ceil(total/5000)*5000:total;
    db.transaksi.push({id,tanggal:`${ds} ${pad(h)}:${pad(m)}:${pad(Math.floor(rnd()*60))}`,uid,subtotal:sub,diskon,kode,total,metode:met,dibayar,kembalian:dibayar-total,status:'selesai'});
    db.transaksi_detail.push(...det);
  };
  const soldSet=new Set(),nowH=new Date().getHours();
  for(let day=13;day>=0;day--){
    const ds=d(-day),n=3+Math.floor(rnd()*5);
    for(let i=0;i<n;i++){
      const h=9+Math.floor(rnd()*12);
      if(day===0&&h>nowH)continue;
      const items=[],used=new Set(),ni=1+Math.floor(rnd()*3);
      for(let j=0;j<ni;j++){const p=db.produk[Math.floor(rnd()*db.produk.length)];if(used.has(p.id))continue;used.add(p.id);
        const o=ops(p);items.push({p,opsi:o,qty:1+Math.floor(rnd()*2),harga:p.harga+(o.includes('Large')?HARGA_LARGE:0)})}
      if(!items.length)continue;
      items.forEach(it=>soldSet.add(it.p.id));
      mkTx(ds,h,Math.floor(rnd()*60),rnd()<0.6?'KSR01':'KSR02',items,true);
    }
  }
  db.produk.forEach(p=>{
    if(soldSet.has(p.id))return;
    const ds=d(-(1+Math.floor(rnd()*12))),o=ops(p);
    mkTx(ds,10+Math.floor(rnd()*9),Math.floor(rnd()*60),rnd()<0.5?'KSR01':'KSR02',[{p,opsi:o,qty:2,harga:p.harga+(o.includes('Large')?HARGA_LARGE:0)}],false);
  });
  db.transaksi.sort((a,b)=>a.tanggal.localeCompare(b.tanggal));
  return db;
}

/* ---------- UI: toast & modal ---------- */
function toast(msg,type='ok'){
  const el=document.createElement('div');el.className='toast '+type;
  el.innerHTML=`${ic(type==='ok'?'check':'alert',16)}<span>${msg}</span>`;
  document.getElementById('toasts').appendChild(el);
  requestAnimationFrame(()=>el.classList.add('on'));
  setTimeout(()=>{el.classList.remove('on');setTimeout(()=>el.remove(),260)},2800);
}
const MO=[];
function openModal(inner,cls=''){
  const ov=document.createElement('div');ov.className='mov';
  ov.innerHTML=`<div class="modal ${cls}" onclick="event.stopPropagation()">${inner}</div>`;
  const born=Date.now();
  ov.addEventListener('mousedown',e=>{if(e.target===ov&&Date.now()-born>300)closeModal()});
  document.getElementById('modals').appendChild(ov);MO.push(ov);
  requestAnimationFrame(()=>ov.classList.add('on'));
}
function setModal(inner){if(MO.length)MO[MO.length-1].firstElementChild.innerHTML=inner}
function closeModal(){const ov=MO.pop();if(!ov)return;ov.classList.remove('on');setTimeout(()=>ov.remove(),180)}
function closeAllModals(){while(MO.length)closeModal()}
function confirmBox(title,msg,fn,yes='Ya, lanjutkan',danger=false){
  openModal(`<div class="cf"><div class="cf-i ${danger?'danger':''}">${ic('alert',24)}</div><h3 class="fr">${title}</h3><p>${msg}</p>
  <div class="cf-b"><button class="btn btn-line" onclick="closeModal()">${t('Batal')}</button>
  <button class="btn ${danger?'btn-danger':'btn-acc'}" id="cfYes">${yes}</button></div></div>`);
  document.getElementById('cfYes').onclick=()=>{closeModal();fn()};
}

/* ---------- komponen bersama: struk, detail transaksi, peringkat ---------- */
function zzPath(w,tooth,h){const p=['0 0'];for(let x=0;x<w;x+=tooth){p.push(`${x+tooth/2} ${h}`,`${x+tooth} 0`)}return`polygon(${p.join(',')})`}
function receiptHTML(tx){
  const det=DB.transaksi_detail.filter(r=>r.tid===tx.id);
  const tot=(l,v,c='')=>`<div class="rc-t ${c}"><span>${l}</span><b>${v}</b></div>`;
  return `<div class="rc">
    <div class="rc-shop">${esc(DB.settings.toko)}</div>
    <div class="rc-line rc-c" style="font-size:10.5px;color:#6b6259">${esc(DB.settings.alamat)}</div>
    <div class="rc-line rc-c" style="font-size:10.5px;color:#6b6259">${esc(DB.settings.telp)}</div>
    <div class="rc-dash"></div>
    <div class="rc-line"><span>No</span><span>${tx.id}</span></div>
    <div class="rc-line"><span>${t('Tanggal')}</span><span>${fmtDT(tx.tanggal)}</span></div>
    <div class="rc-line"><span>Kasir</span><span>${esc(user(tx.uid)?.nama||'-')}</span></div>
    <div class="rc-dash"></div>
    ${det.map(r=>`<div class="rc-item"><div class="rc-n">${esc(r.nama)} <span>×${r.qty}</span></div>
      ${r.opsi?`<div class="rc-o">${esc(r.opsi)}</div>`:''}<div class="rc-p">${num(r.st)}</div></div>`).join('')}
    <div class="rc-dash"></div>
    ${tot('Subtotal',num(tx.subtotal))}
    ${tx.diskon?tot('Diskon'+(tx.kode?` (${tx.kode})`:''),'-'+num(tx.diskon)):''}
    ${tot('TOTAL','Rp '+num(tx.total),'rc-big')}
    <div class="rc-dash"></div>
    <div class="rc-line"><span>${t('Metode')}</span><span>${tx.metode.toUpperCase()}</span></div>
    <div class="rc-line"><span>Dibayar</span><span>Rp ${num(tx.dibayar)}</span></div>
    <div class="rc-line"><span>Kembali</span><span>Rp ${num(tx.kembalian)}</span></div>
    <div class="rc-dash"></div>
    <div class="rc-foot">${esc(DB.settings.footer)}</div>
    <div class="rc-bar"></div>
    <div class="rc-line rc-c" style="font-size:10px">${tx.id}</div>
    <div class="rc-zz" style="clip-path:${zzPath(300,12,7)}"></div>
  </div>`;
}
function printReceipt(id){
  const tx=DB.transaksi.find(x=>x.id===id);if(!tx)return;
  document.getElementById('printArea').innerHTML=receiptHTML(tx);
  setTimeout(()=>window.print(),60);
}
function txModal(id){
  const tx=DB.transaksi.find(x=>x.id===id);if(!tx)return;
  const det=DB.transaksi_detail.filter(r=>r.tid===id);
  openModal(`<div class="txm">
    <div class="txm-h"><div><h3 class="fr">${tx.id}</h3><span>${fmtDT(tx.tanggal)} · Kasir: ${esc(user(tx.uid)?.nama||'-')}</span></div>
    <span class="bdg bdg-mut">${tx.metode.toUpperCase()}</span></div>
    <div class="card card-p0"><table class="tbl">
      <thead><tr><th>Produk</th><th>Opsi</th><th>${t('Jumlah')}</th><th>${t('Harga')}</th><th>${t('Subtotal')}</th></tr></thead>
      <tbody>${det.map(r=>`<tr><td><b>${esc(r.nama)}</b></td><td class="mut">${esc(r.opsi)||'—'}</td><td>${r.qty}</td><td>${rp(r.harga)}</td><td>${rp(r.st)}</td></tr>`).join('')}</tbody>
    </table></div>
    <div class="txm-tot">
      <div><span>Subtotal</span><span>${rp(tx.subtotal)}</span></div>
      ${tx.diskon?`<div><span>Diskon${tx.kode?` (${tx.kode})`:''}</span><span>−${rp(tx.diskon)}</span></div>`:''}
      <div class="g"><span>Total</span><span>${rp(tx.total)}</span></div>
      <div><span>Dibayar</span><span>${rp(tx.dibayar)}</span></div>
      <div><span>Kembali</span><span>${rp(tx.kembalian)}</span></div>
    </div>
    <div class="mbtns" style="border:0;padding:0">
      <button class="btn btn-acc" onclick="printReceipt('${id}')">${ic('printer',16)} ${t('Cetak Struk')}</button>
      <button class="btn btn-line" onclick="closeModal()">${t('Tutup')}</button>
    </div></div>`,'w-lg');
}
/* ---- kartu peringkat produk terlaris ---- */
let _rcCss=false;
function rankCards(items,click=''){
  if(!_rcCss){
    _rcCss=true;
    const st=document.createElement('style');
    st.textContent=`
.rc-stack{display:flex;flex-direction:column;gap:10px;margin-top:10px}
.rc-grid{display:grid!important;grid-template-columns:repeat(auto-fit,minmax(92px,1fr))!important;gap:10px!important;align-items:start}
.rcard{position:relative;background:transparent!important;border:0!important;padding:0!important;margin:0 auto!important;width:100%;max-width:150px;text-align:center;transition:.15s;font:inherit;color:inherit}
.rcard.click{cursor:pointer}
.rcard.click:hover{transform:translateY(-2px)}
.rcard-imgwrap{position:relative;aspect-ratio:1/1!important;border-radius:12px;overflow:hidden;background:var(--acc-soft);border:1.5px solid var(--line)}
.rcard.click:hover .rcard-imgwrap{border-color:var(--acc);box-shadow:var(--shadow)}
.rcard.gold .rcard-imgwrap{border-color:var(--acc)}
.rcard-imgwrap img{width:100%;height:100%;object-fit:cover;display:block}
.rcard-rank{position:absolute;top:5px;left:5px;min-width:18px;height:18px;padding:0 5px;border-radius:7px;background:rgba(20,13,6,.65);color:#F4EBDD;display:flex;align-items:center;justify-content:center;font-family:'Fraunces',serif;font-style:italic;font-weight:600;font-size:10.5px}
.rcard.gold .rcard-rank{background:var(--acc);color:var(--acc-c)}
.rcard-b{padding:5px 1px 0!important;display:flex;flex-direction:column;gap:1px!important}
.rcard-n{font-size:10.5px!important;font-weight:600;line-height:1.25!important;white-space:nowrap!important;overflow:hidden;text-overflow:ellipsis;display:block!important}
.rcard-q{font-size:9.5px!important;color:var(--mut)!important}
.rcard-q b{color:var(--acc);font-size:11.5px;font-family:'IBM Plex Mono',monospace}`;
    document.head.appendChild(st);
  }
  if(!items.length)return emptyBox('trending','Belum ada data penjualan','Data peringkat muncul setelah ada transaksi.');
  const cards=items.map((x,i)=>{
    const c=click?click.replace('%ID%',x.p.id):'';
    const open=click?`<button class="rcard click ${i===0?'gold':''}" onclick="${c}">`:`<div class="rcard ${i===0?'gold':''}">`;
    const close=click?'</button>':'</div>';
    return `${open}
      <div class="rcard-imgwrap"><img src="${esc(x.p.gambar)}" loading="lazy" alt="" onerror="this.style.visibility='hidden'">
        <span class="rcard-rank">${i+1}</span></div>
      <div class="rcard-b"><span class="rcard-n" title="${esc(x.p.nama)}">${esc(x.p.nama)}</span>
        <span class="rcard-q"><b>${x.qty}</b> terjual</span></div>
    ${close}`;
  }).join('');
  return `<div class="rc-grid">${cards}</div>`;
}
/* kompatibilitas: panggilan lama rankList()/rankH() otomatis pakai kartu + klik sesuai aplikasi */
function rankList(items){
  const app=document.body.dataset.app;
  const click=app==='admin'?"prodModal('%ID%')":app==='kasir'?"go('transaksi');omOpen('%ID%')":'';
  return rankCards(items,click);
}
const rankH=rankList;
function sparkArr(){const a=[];for(let i=6;i>=0;i--)a.push(soldOn(dOff(-i)));const mx=Math.max(...a,1);return a.map(v=>Math.max(9,Math.round(v/mx*100)))}

/* ---------- tema & bahasa ---------- */
function applyTheme(){
  document.documentElement.dataset.theme=DB.settings.theme;
  LANG=DB.settings.lang;
}

/* ---------- sesi per-aplikasi ---------- */
const SESSION_KEYS={admin:'senja_admin_sess',pos:'senja_pos_sess'};
const setSession=(app,uid)=>sessionStorage.setItem(SESSION_KEYS[app],uid);
const getSession=app=>sessionStorage.getItem(SESSION_KEYS[app]);
const clearSession=app=>sessionStorage.removeItem(SESSION_KEYS[app]);

/* ---------- halaman Pengaturan (identik di kedua aplikasi) ---------- */
function settingsView(){
  document.getElementById('view').innerHTML=`
  <div class="set">
    <div class="card"><h3 class="fr">🎨 Tampilan</h3><p>Pilih tema aplikasi. Karena database-nya satu, tema berlaku juga di aplikasi satunya.</p>
      <div class="themes">
        <button class="th-tile ${DB.settings.theme==='light'?'on':''}" onclick="setThemeDB('light')"><span class="pv light"><i></i><i></i><i></i></span>Light</button>
        <button class="th-tile ${DB.settings.theme==='dark'?'on':''}" onclick="setThemeDB('dark')"><span class="pv dark"><i></i><i></i><i></i></span>Dark</button>
      </div></div>
    <div class="card"><h3 class="fr">🌐 Bahasa</h3><p>Mengubah bahasa antarmuka aplikasi.</p>
      <div class="seg" style="max-width:260px">
        <button class="${DB.settings.lang==='id'?'on':''}" onclick="setLangDB('id')">Bahasa Indonesia</button>
        <button class="${DB.settings.lang==='en'?'on':''}" onclick="setLangDB('en')">English</button>
      </div></div>
    <div class="card"><h3 class="fr">🐞 Lapor Bug</h3><p>Temukan bug/error? Laporkan di sini — laporan tersimpan di database bersama.</p>
      <div class="mform">
        <div class="field"><label>Judul</label><input id="rpJ" class="inp" placeholder="Singkatnya masalahnya apa…"></div>
        <div class="field"><label>Detail</label><textarea id="rpD" class="inp" placeholder="Langkah memunculkan bug, pesan error, dll."></textarea></div>
        <div><button class="btn btn-acc" onclick="sendReport()">${ic('bug',15)} Kirim Laporan</button></div>
      </div></div>
    <div class="card"><h3 class="fr">👤 Akun — ${esc(CUR.nama)}</h3><p>Perbarui nomor HP &amp; password akunmu.</p>
      <div class="mform">
        <div class="field"><label>${t('Nomor HP')}</label><input id="acH" class="inp" value="${esc(CUR.no_hp||'')}"></div>
        <div class="pw-grid">
          <div class="field"><label>Password Lama</label><input id="acO" type="password" class="inp"></div>
          <div class="field"><label>Password Baru</label><input id="acN" type="password" class="inp"></div>
          <div class="field"><label>Konfirmasi Password</label><input id="acC" type="password" class="inp"></div>
        </div>
        <div><button class="btn btn-acc" onclick="saveAccount()">${ic('check',15)} ${t('Simpan Perubahan')}</button></div>
      </div></div>
  </div>`;
}
function setThemeDB(v){DB.settings.theme=v;saveDB();applyTheme();settingsView();toast('Tema diubah — berlaku juga di aplikasi satunya')}
function setLangDB(v){DB.settings.lang=v;saveDB();if(window.render)render();toast('Bahasa diubah')}
function sendReport(){
  const j=document.getElementById('rpJ').value.trim(),d=document.getElementById('rpD').value.trim();
  if(!j){toast('Judul laporan wajib diisi','bad');return}
  DB.reports.push({id:nextId(DB.reports,'RPT-'),app:document.body.dataset.app||'-',tanggal:`${today()} ${nowHMS()}`,uid:CUR.user_id,nama:CUR.nama,judul:j,desk:d});
  saveDB();settingsView();toast('Laporan bug terkirim. Terima kasih!');
}
function saveAccount(){
  const hp=document.getElementById('acH').value.trim(),
        o=document.getElementById('acO').value,n=document.getElementById('acN').value,c=document.getElementById('acC').value;
  CUR.no_hp=hp;
  if(o||n||c){
    if(o!==CUR.password){toast('Password lama salah','bad');return}
    if(n.length<4){toast('Password baru minimal 4 karakter','bad');return}
    if(n!==c){toast('Konfirmasi password tidak sama','bad');return}
    CUR.password=n;
  }
  saveDB();settingsView();toast('Perubahan akun disimpan');
}

/* ---------- sinkronisasi antar tab/aplikasi ---------- */
window.addEventListener('storage',e=>{
  if(e.key!==DBKEY)return;
  loadDB();applyTheme();
  if(typeof window.onSync==='function')window.onSync();
});

/* ---------- CSS BERSAMA (disuntikkan ke kedua aplikasi) ---------- */
(function(){
  const css=`
*{margin:0;padding:0;box-sizing:border-box}
:root{--bg:#F5EFE4;--surface:#FDF9F1;--card:#FFF;--ink:#241A10;--ink2:#5C4B3A;--mut:#94836D;
--line:#E6DAC6;--line2:#D6C6AB;--acc:#B25B1B;--acc-d:#9A4C13;--acc-soft:#F4E4D1;--acc-c:#fff;
--ok:#3E7C4F;--ok-soft:#E4EFE3;--bad:#B3402E;--bad-soft:#F7E4DE;--warn:#9A6B00;--warn-soft:#F5EAD0;
--side:#241A11;--side-ink:#EFE5D4;--side-mut:#A08E77;
--shadow:0 1px 2px rgba(36,26,16,.06),0 10px 26px -14px rgba(36,26,16,.22)}
[data-theme="dark"]{--bg:#151110;--surface:#1D1813;--card:#241E17;--ink:#F1E8DB;--ink2:#CDBBA3;--mut:#94836E;
--line:#342B21;--line2:#483B2B;--acc:#D6853F;--acc-d:#E29552;--acc-soft:#3A2A19;--acc-c:#20150A;
--ok:#8CC49A;--ok-soft:#26331F;--bad:#E08573;--bad-soft:#40251C;--warn:#D9A94A;--warn-soft:#3B2F17;
--side:#0F0C09;--shadow:0 1px 2px rgba(0,0,0,.4),0 12px 30px -14px rgba(0,0,0,.6)}
html{font-size:15px}
body{font-family:'Space Grotesk',sans-serif;background:var(--bg);color:var(--ink);min-height:100vh;-webkit-font-smoothing:antialiased}
button{font-family:inherit}
.fr{font-family:'Fraunces',serif}.mono{font-family:'IBM Plex Mono',monospace}.mut{color:var(--mut)}
.flex1{flex:1}.hide{display:none!important}
::-webkit-scrollbar{width:10px;height:10px}
::-webkit-scrollbar-thumb{background:var(--line2);border-radius:6px;border:2px solid var(--bg)}
:focus-visible{outline:2px solid var(--acc);outline-offset:2px}
.btn{display:inline-flex;align-items:center;justify-content:center;gap:8px;border:0;border-radius:11px;padding:10px 16px;font-size:13.5px;font-weight:600;cursor:pointer;transition:.15s;background:var(--line);color:var(--ink)}
.btn:hover{transform:translateY(-1px)}.btn:active{transform:none}
.btn:disabled{opacity:.45;cursor:not-allowed;transform:none}
.btn-acc{background:var(--acc);color:var(--acc-c)}.btn-acc:hover{background:var(--acc-d)}
.btn-line{background:transparent;border:1.5px solid var(--line2);color:var(--ink2)}.btn-line:hover{border-color:var(--acc);color:var(--acc)}
.btn-danger{background:var(--bad);color:#fff}
.btn-sm{padding:6px 11px;font-size:12px;border-radius:9px}.btn-block{width:100%}
.icon-btn{width:32px;height:32px;display:inline-flex;align-items:center;justify-content:center;border-radius:9px;border:1px solid var(--line);background:transparent;color:var(--ink2);cursor:pointer;transition:.15s;flex:none}
.icon-btn:hover{border-color:var(--acc);color:var(--acc)}.icon-btn.danger:hover{border-color:var(--bad);color:var(--bad)}
.link{border:0;background:none;color:var(--acc);font-weight:600;font-size:12px;cursor:pointer;padding:0;text-align:left}
.inp{width:100%;padding:10px 13px;border:1.5px solid var(--line);border-radius:11px;background:var(--card);color:var(--ink);font:inherit;font-size:13.5px;transition:.15s}
.inp:focus{outline:none;border-color:var(--acc);box-shadow:0 0 0 3px var(--acc-soft)}
textarea.inp{resize:vertical;min-height:74px}
.sel{appearance:none;background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='12' height='8' viewBox='0 0 12 8'><path d='M1 1l5 5 5-5' fill='none' stroke='%2394836D' stroke-width='2' stroke-linecap='round'/></svg>");background-repeat:no-repeat;background-position:right 12px center;padding-right:34px}
.field{display:flex;flex-direction:column;gap:6px}
.field label{font-size:11.5px;font-weight:600;letter-spacing:.7px;text-transform:uppercase;color:var(--mut)}
.row{display:flex;gap:8px;align-items:center}.row2{display:grid;grid-template-columns:1fr 1fr;gap:12px}
.mform{display:flex;flex-direction:column;gap:14px}.mform h3{font-size:20px;font-weight:600}
.mbtns{display:flex;gap:10px;justify-content:flex-end;padding-top:12px;border-top:1px solid var(--line);margin-top:4px;flex-wrap:wrap}
.toolbar{display:flex;gap:10px;align-items:center;flex-wrap:wrap;margin-bottom:16px}
.search{position:relative;flex:1;min-width:200px;max-width:360px}
.search svg{position:absolute;left:12px;top:50%;transform:translateY(-50%);color:var(--mut);pointer-events:none}
.search .inp{padding-left:38px}
.chips{display:flex;gap:8px;flex-wrap:wrap}
.chip{padding:7px 14px;border-radius:999px;border:1.5px solid var(--line);background:transparent;color:var(--ink2);font-size:12.5px;font-weight:600;cursor:pointer;transition:.15s}
.chip:hover{border-color:var(--acc);color:var(--acc)}.chip.on{background:var(--acc);border-color:var(--acc);color:var(--acc-c)}
.card{background:var(--card);border:1px solid var(--line);border-radius:16px;padding:18px}
.card-p0{padding:0;overflow:hidden}
.c-h{display:flex;align-items:center;justify-content:space-between;margin-bottom:10px;gap:8px}
.c-h h3{font-size:17px;font-weight:600}
.tbl{width:100%;border-collapse:collapse;font-size:13.5px}
.tbl th{text-align:left;font-size:10.5px;letter-spacing:1.4px;text-transform:uppercase;color:var(--mut);padding:11px 12px;border-bottom:1.5px solid var(--line);white-space:nowrap}
.tbl td{padding:11px 12px;border-bottom:1px solid var(--line);vertical-align:middle}
.tbl tbody tr:last-child td{border-bottom:0}
.tbl tbody tr{transition:.12s}.tbl tbody tr:hover{background:var(--acc-soft)}
.tbl tr.click{cursor:pointer}
.card-p0 .tbl th:first-child,.card-p0 .tbl td:first-child{padding-left:18px}
.card-p0 .tbl th:last-child,.card-p0 .tbl td:last-child{padding-right:18px}
.bdg{display:inline-flex;align-items:center;gap:5px;padding:3px 10px;border-radius:999px;font-size:11px;font-weight:700;letter-spacing:.4px;white-space:nowrap}
.bdg-ok{background:var(--ok-soft);color:var(--ok)}.bdg-bad{background:var(--bad-soft);color:var(--bad)}
.bdg-w{background:var(--warn-soft);color:var(--warn)}.bdg-a{background:var(--acc-soft);color:var(--acc)}
.bdg-mut{background:var(--line);color:var(--ink2)}.bdg-d{background:#3E2F22;color:#E9D9C4}
.seg{display:flex;background:var(--bg);border:1.5px solid var(--line);border-radius:11px;padding:3px;gap:3px}
.seg button{flex:1;padding:8px 10px;border:0;background:transparent;border-radius:8px;font:inherit;font-size:12.5px;font-weight:600;color:var(--ink2);cursor:pointer;display:flex;align-items:center;justify-content:center;gap:6px;transition:.14s;white-space:nowrap}
.seg button.on{background:var(--acc);color:var(--acc-c)}
.seg.sm button{padding:6px 8px;font-size:12px}
.ck-row{display:flex;flex-wrap:wrap;gap:8px}
.ck{display:inline-flex;align-items:center;gap:7px;padding:8px 12px;border:1.5px solid var(--line);border-radius:10px;font-size:12.5px;font-weight:600;cursor:pointer;user-select:none}
.ck input{accent-color:var(--acc);width:15px;height:15px}
.ck.dis{opacity:.4;pointer-events:none}
.empty{display:flex;flex-direction:column;align-items:center;gap:8px;padding:44px 20px;color:var(--mut);text-align:center;font-size:13.5px}
.empty svg{opacity:.5}.empty .btn{margin-top:6px}
.ava{width:38px;height:38px;border-radius:12px;background:var(--acc);color:var(--acc-c);display:flex;align-items:center;justify-content:center;font-weight:700;font-size:15px;flex:none}
.ava.sm{width:28px;height:28px;font-size:12px;border-radius:9px}
.ava.lg{width:52px;height:52px;font-size:20px;border-radius:16px}
.mini-stats{display:flex;gap:12px;margin-bottom:14px;flex-wrap:wrap}
.mini{background:var(--card);border:1px solid var(--line);border-radius:12px;padding:12px 18px}
.mini span{display:block;font-size:10.5px;letter-spacing:1.2px;text-transform:uppercase;color:var(--mut);font-weight:700}
.mini b{font-size:19px;font-family:'Fraunces'}
#toasts{position:fixed;right:18px;bottom:18px;display:flex;flex-direction:column;gap:8px;z-index:200}
.toast{display:flex;gap:9px;align-items:center;background:var(--side);color:#F4EBDD;padding:11px 16px;border-radius:11px;box-shadow:var(--shadow);transform:translateY(8px);opacity:0;transition:.22s;font-size:13.5px;max-width:360px}
.toast.on{transform:none;opacity:1}
.toast svg{color:#8FCF9F;flex:none}.toast.bad svg{color:#E8907D}
.mov{position:fixed;inset:0;background:rgba(22,15,8,.45);backdrop-filter:blur(3px);display:flex;align-items:center;justify-content:center;padding:20px;z-index:100;opacity:0;transition:.18s}
.mov.on{opacity:1}
.modal{background:var(--surface);border:1px solid var(--line);border-radius:16px;box-shadow:0 24px 60px -20px rgba(0,0,0,.4);max-width:520px;width:100%;max-height:calc(100vh - 40px);overflow:auto;padding:22px;transform:translateY(10px) scale(.98);transition:.18s}
.mov.on .modal{transform:none}.modal.w-lg{max-width:780px}
.cf{text-align:center;display:flex;flex-direction:column;align-items:center;gap:10px;padding:8px}
.cf-i{width:52px;height:52px;border-radius:16px;background:var(--acc-soft);color:var(--acc);display:flex;align-items:center;justify-content:center}
.cf-i.danger{background:var(--bad-soft);color:var(--bad)}
.cf h3{font-size:20px}.cf p{font-size:13.5px;color:var(--ink2);line-height:1.5}
.cf-b{display:flex;gap:10px;margin-top:8px}
.lg{min-height:100vh;display:flex;align-items:center;justify-content:center;padding:24px;background:radial-gradient(900px 480px at 85% -10%,var(--acc-soft),transparent 60%),var(--bg)}
.lg-card{display:grid;grid-template-columns:330px 400px;width:100%;max-width:740px;background:var(--card);border:1px solid var(--line);border-radius:22px;overflow:hidden;box-shadow:var(--shadow)}
.lg-left{background:var(--side);color:#F2E7D6;padding:40px 34px;display:flex;flex-direction:column;position:relative}
.lg-left::after{content:"";position:absolute;inset:0;background-image:radial-gradient(rgba(242,231,214,.09) 1px,transparent 1px);background-size:20px 20px;pointer-events:none}
.steam{width:64px;height:52px;margin-bottom:2px}
.steam path{opacity:0;animation:stm 3.2s ease-in-out infinite}
.steam path:nth-child(2){animation-delay:1.05s}.steam path:nth-child(3){animation-delay:2.1s}
@keyframes stm{0%{opacity:0;transform:translateY(5px)}35%{opacity:.85}70%{opacity:0;transform:translateY(-7px)}100%{opacity:0}}
.lg-cup{color:#E4D2B5;margin-bottom:14px}
.lg-word{font-family:'Fraunces',serif;font-size:44px;font-weight:700;letter-spacing:2px;line-height:1}
.lg-tag{font-size:12.5px;color:var(--side-mut);margin-top:10px;line-height:1.6;max-width:230px}
.lg-mini{margin-top:auto;padding-top:26px;display:flex;flex-direction:column;gap:5px;font-size:11.5px;color:var(--side-mut);border-top:1px solid rgba(242,231,214,.14)}
.lg-mini b{color:#E4D2B5;font-size:12px;letter-spacing:1.6px;text-transform:uppercase}
.lg-right{padding:40px 36px;display:flex;flex-direction:column;gap:14px}
.lg-right h1{font-size:26px;font-weight:600}.lg-right>p{font-size:13px;color:var(--mut);margin-top:-8px}
.lg-err{color:var(--bad);font-size:12.5px;font-weight:600;min-height:16px}
.lg-demo{margin-top:6px;border-top:1px dashed var(--line);padding-top:12px;display:flex;flex-wrap:wrap;gap:6px;font-size:11.5px;color:var(--mut);align-items:center}
.lg-demo button,.lg-demo a{border:1px solid var(--line);background:none;border-radius:999px;padding:4px 10px;font:inherit;font-size:11px;color:var(--ink2);cursor:pointer;transition:.15s;text-decoration:none}
.lg-demo button:hover,.lg-demo a:hover{border-color:var(--acc);color:var(--acc)}
.inp-wrap{position:relative}
.inp-wrap .eye{position:absolute;right:6px;top:50%;transform:translateY(-50%);border:0;background:none;color:var(--mut);cursor:pointer;padding:6px;display:flex}
@media(max-width:760px){.lg-card{grid-template-columns:1fr}.lg-left{padding:28px}.lg-mini{display:none}}
.txm{display:flex;flex-direction:column;gap:14px}
.txm-h{display:flex;justify-content:space-between;align-items:flex-start;gap:10px;flex-wrap:wrap}
.txm-h h3{font-size:19px}.txm-h>div>span{font-size:12.5px;color:var(--mut)}
.txm-tot{display:flex;flex-direction:column;gap:6px;font-size:13.5px}
.txm-tot>div{display:flex;justify-content:space-between}.txm-tot .g{font-size:16px;font-weight:700}
.rc-wrap{display:flex;flex-direction:column;align-items:center;gap:14px}
.rc-actions{display:flex;gap:10px}
.pay-ok{display:flex;align-items:center;gap:9px;justify-content:center;background:var(--ok-soft);color:var(--ok);font-weight:700;border-radius:11px;padding:11px;font-size:14.5px;width:100%}
.rc{width:300px;background:#fff;color:#191512;font-family:'IBM Plex Mono',monospace;font-size:12px;padding:18px 16px 24px;position:relative;box-shadow:0 12px 32px -12px rgba(0,0,0,.35)}
.rc-zz{position:absolute;top:100%;left:0;width:300px;height:7px;background:#fff}
.rc-shop{font-size:15px;font-weight:600;letter-spacing:1.5px;text-align:center}
.rc-line{display:flex;justify-content:space-between;gap:10px;margin:2px 0}
.rc-line span:first-child{color:#6b6259}.rc-c{justify-content:center}
.rc-dash{border-top:1.5px dashed #c9c2b8;margin:8px 0}
.rc-item{margin:4px 0}.rc-n{font-weight:600}.rc-n span{font-weight:400;color:#6b6259}
.rc-o{font-size:10.5px;color:#6b6259;padding-left:8px}.rc-p{text-align:right}
.rc-t{display:flex;justify-content:space-between;margin:2px 0}.rc-big{font-size:15.5px;font-weight:700}
.rc-foot{text-align:center;margin-top:7px;font-size:11px}
.rc-bar{height:34px;margin:11px auto 5px;width:82%;background:repeating-linear-gradient(90deg,#191512 0 2px,transparent 2px 4px,#191512 4px 5px,transparent 5px 9px)}
#printArea{display:none}
@media print{body{background:#fff}#root,#toasts,#modals{display:none!important}
#printArea{display:block!important}#printArea .rc{margin:0 auto;box-shadow:none}.rc-zz{display:none}}
@page{margin:8mm}
.rk{display:grid;grid-template-columns:36px 1fr auto;gap:12px;align-items:center;padding:10px 2px;border-bottom:1px dashed var(--line)}
.rk:last-child{border:0}
.rk-n{font-size:24px;color:var(--line2);font-weight:600;font-style:italic;text-align:center}
.rk:first-child .rk-n{color:var(--acc)}
.rk-t{display:flex;justify-content:space-between;gap:8px;font-size:13.5px;margin-bottom:6px}
.rk-t b{font-weight:600}.rk-t span{color:var(--mut);font-size:12px;white-space:nowrap}
.rk-bar{height:5px;background:var(--acc-soft);border-radius:4px;overflow:hidden}
.rk-bar i{display:block;height:100%;background:var(--acc);border-radius:4px}
.rk-cat{font-size:11px;color:var(--mut);background:var(--bg);border:1px solid var(--line);padding:3px 9px;border-radius:99px;white-space:nowrap}
.hl{display:flex;gap:14px;align-items:flex-end;min-height:170px;padding-top:6px}
.hl-c{flex:1;display:flex;flex-direction:column;align-items:center;gap:7px;text-align:center;min-width:0}
.hl-q{font-size:12px;font-weight:700;color:var(--acc)}
.hl-bar{width:100%;max-width:72px;height:92px;background:var(--acc-soft);border-radius:10px 10px 4px 4px;display:flex;align-items:flex-end;overflow:hidden}
.hl-bar i{width:100%;background:var(--acc);border-radius:10px 10px 4px 4px;min-height:10px}
.hl-rk{font-size:16px;font-style:italic;color:var(--line2)}
.hl-c:first-child .hl-rk{color:var(--acc)}
.hl-n{font-size:11.5px;line-height:1.3;display:-webkit-box;-webkit-line-clamp:2;-webkit-box-orient:vertical;overflow:hidden}
.stat-f{display:flex;gap:14px;align-items:flex-end;margin-top:10px}
.spark{display:flex;gap:4px;align-items:flex-end;height:40px;flex:1}
.spark i{flex:1;background:var(--acc);opacity:.5;border-radius:3px;min-height:4px}
.spark i:last-child{opacity:1}
.set{display:flex;flex-direction:column;gap:14px;max-width:680px}
.set h3{font-size:17px;margin-bottom:10px}
.set p{font-size:13px;color:var(--mut);margin-bottom:10px;line-height:1.5}
.themes{display:flex;gap:10px}
.th-tile{flex:1;display:flex;flex-direction:column;gap:9px;align-items:center;padding:13px;border:1.5px solid var(--line);border-radius:13px;background:transparent;color:var(--ink2);font-weight:600;font-size:12.5px;cursor:pointer;transition:.15s}
.th-tile:hover{border-color:var(--acc)}.th-tile.on{border-color:var(--acc);background:var(--acc-soft);color:var(--acc)}
.pv{width:100%;height:46px;border-radius:9px;border:1px solid var(--line);display:flex;gap:4px;padding:6px}
.pv i{border-radius:4px;flex:1}
.pv.light{background:#F5EFE4}.pv.light i{background:#fff;border:1px solid #E6DAC6}.pv.light i:first-child{background:#B25B1B;border:0}
.pv.dark{background:#171310}.pv.dark i{background:#241E17;border:1px solid #342B21}.pv.dark i:first-child{background:#D6853F;border:0}
.pw-grid{display:grid;grid-template-columns:1fr 1fr 1fr;gap:10px;margin:10px 0}
@media(max-width:640px){.pw-grid{grid-template-columns:1fr}}
.low{color:var(--bad);font-weight:700};`;
//new card produk terlaris

  const st=document.createElement('style');st.textContent=css;document.head.appendChild(st);
})();