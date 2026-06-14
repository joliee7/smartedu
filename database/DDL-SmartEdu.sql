create database if not exists smartEdu;
use smartEdu;

drop table if exists mapel;
create table mapel(
	id_mapel	varchar(8) primary key,
    id_jenjang	varchar(3) not null,
    nama_mapel	varchar(25) not null,
    status_del	tinyint(1) not null
);

drop table if exists materi;
create table materi(
	id_materi	varchar(9) primary key,
    id_mapel	varchar(8) not null,
    nama_materi	varchar(80) not null,
    status_del	tinyint(1) not null
);

drop table if exists jenjang;
create table jenjang(
	id_jenjang	varchar(3) primary key,
    jenjang 	varchar(3) not null,
    level		varchar(3) not null,
    status_del	tinyint(1) not null
);

drop table if exists tutor;
create table tutor(
	id_tutor		varchar(3) primary key,
    nama			varchar(60) not null,
    email			varchar(100) not null,
    kota			varchar(40) not null,
    telepon			varchar(13) not null,
    rating			varchar(1) not null,
    password		varchar(17) not null,
    status_del		tinyint(1) not null
);

drop table if exists biaya_tutor;
create table biaya_tutor(
	id_mapel	varchar(8) not null,
    id_jenjang	varchar(3) not null,
    id_tutor	varchar(3) not null,
	biaya		int unsigned not null,
    status_del	tinyint(1) not null
);

drop table if exists pembayaran_tutor;
create table pembayaran_tutor(
	id_tutor		varchar(3) not null,
    nominal			int unsigned not null,
    bulan_digaji	int unsigned not null,
    tanggal_bayar	date,
    status_bayar	tinyint(1) not null,
    status_del		tinyint(1) not null
);

drop table if exists siswa;
create table siswa(
	id_siswa		varchar(4) primary key,
    id_ortu			varchar(5) not null,
    id_jenjang		varchar(3) not null,
    nama			varchar(60) not null,
    email			varchar(100) not null,
    kota			varchar(40) not null,
    telepon			varchar(13) not null,
    password		varchar(17) not null,
    status_del		tinyint(1) not null
);

drop table if exists ruangan;
create table ruangan(
	id_ruangan		varchar(3) primary key,
    nama_ruangan	varchar(25) not null,
    kapasitas		int unsigned not null,
    status_del		tinyint(1) not null
);

drop table if exists detail_les;
create table detail_les(
	id_detail_les	varchar(8) primary key,
    id_tutor		varchar(3) not null,
    id_mapel		varchar(8) not null,
    id_ruangan		varchar(3) not null,
    jam_mulai		time not null,
    jam_berakhir	time not null,
    tanggal			date not null,
    keterangan 		varchar(500),
    status_les		varchar(1) not null,
    status_del		tinyint(1) not null
);

drop table if exists siswa_detail_les;
create table siswa_detail_les(
	id_siswa		varchar(4) not null,
    id_detail_les	varchar(8) not null,
	status_hadir	varchar(1) not null,
    catatan			varchar(255),
    status_del		tinyint(1) not null
);

drop table if exists orangtua;
create table orangtua(
	id_ortu		varchar(5) primary key,
	nama		varchar(60) not null,
    telepon		varchar(13) not null,
    status_del	tinyint(1) not null
);

drop table if exists tugas;
create table tugas(
	id_tugas		varchar(6) primary key,
    id_materi		varchar(9) not null,
    nama_tugas		varchar(100) not null,
    detail_tugas	text not null,
    status_del		tinyint(1) not null
);

drop table if exists tugas_detail_les;
create table tugas_detail_les(
	id_tugas		varchar(6) not null,
    id_detail_les	varchar(8) not null,
	pengumpulan 	datetime not null,
    status_del		tinyint(1) not null
);

drop table if exists pengumpulan_tugas;
create table pengumpulan_tugas(
	id_tugas		varchar(6) not null,
    id_siswa		varchar(4) not null,
    waktu_kumpul	datetime not null,
    status_kumpul	tinyint(1) not null,
    isi_tugas		varchar(1000) not null,
    file_path		varchar(80) not null,
    status_del		tinyint(1) not null
);

drop table if exists nilai;
create table nilai(
	id_siswa	varchar(4) not null,
    id_tugas	varchar(6) not null,
	nilai		int not null
);

drop table if exists transaksi;
create table transaksi(
	id_transaksi	varchar(8) primary key,
    id_siswa		varchar(4) not null,
    tanggal			date not null,
    nominal			int unsigned not null,
    metode_bayar	varchar(15) not null,
    periode_mulai	date not null,
    periode_akhir	date not null,
    status_del		tinyint(1) not null
);

drop table if exists detail_transaksi;
create table detail_transaksi(
	id_detailtrans	varchar(8) primary key,
    id_transaksi	varchar(8) not null,
    id_mapel		varchar(8) not null,
    id_tutor		varchar(3) not null,
    durasi_sesi		int unsigned not null,
    harga_per_sesi	int unsigned not null,
    jumlah_sesi		int unsigned not null,
    subtotal		int unsigned not null,
    status_del		tinyint(1) not null 
);

drop table if exists admin;
create table admin(
	id_admin	varchar(5) not null,
    nama		varchar(60) not null,
    email		varchar(100) not null,
    password	varchar(17) not null,
    status_del	tinyint(1) not null 
);

alter table mapel add constraint fk_id_jenjang foreign key(id_jenjang) 
	references jenjang (id_jenjang) on delete restrict on update restrict;

alter table materi add constraint fk_materi_id_mapel foreign key(id_mapel)	
	references mapel (id_mapel) on delete restrict on update restrict;

alter table biaya_tutor add constraint fk_biayaTutor_id_mapel foreign key(id_mapel)
	references mapel (id_mapel) on delete restrict on update restrict;
    
alter table biaya_tutor add constraint fk_biayaTutor_id_jenjang foreign key(id_jenjang)
	references jenjang (id_jenjang) on delete restrict on update restrict;

alter table biaya_tutor add constraint fk_biayaTutor_id_tutor foreign key(id_tutor)
	references tutor (id_tutor) on delete restrict on update restrict;
    
alter table pembayaran_tutor add constraint fk_bayarTutor_id_tutor foreign key(id_tutor)
	references tutor (id_tutor) on delete restrict on update restrict;

alter table detail_les add constraint fk_detailLes_id_tutor foreign key(id_tutor)
	references tutor (id_tutor) on delete restrict on update restrict;

alter table detail_les add constraint fk_detailLes_id_mapel foreign key(id_mapel)
	references mapel (id_mapel) on delete restrict on update restrict;

alter table detail_les add constraint fk_detailLes_id_ruangan foreign key(id_ruangan)
	references ruangan (id_ruangan) on delete restrict on update restrict;
    
alter table siswa_detail_les add constraint fk_siswaDL_id_siswa foreign key(id_siswa)
	references siswa (id_siswa) on delete restrict on update restrict;

alter table siswa_detail_les add constraint fk_siswaDL_id_detail_les foreign key(id_detail_les)
	references detail_les (id_detail_les) on delete restrict on update restrict;

alter table siswa add constraint fk_siswa_id_ortu foreign key(id_ortu)
	references orangtua (id_ortu) on delete restrict on update restrict;
    
alter table siswa add constraint fk_siswa_id_jenjang foreign key(id_jenjang)
	references jenjang (id_jenjang) on delete restrict on update restrict;
    
alter table tugas add constraint fk_tugas_materi foreign key(id_materi)
	references materi (id_materi) on delete restrict on update restrict;
    
alter table pengumpulan_tugas add constraint fk_PT_tugas foreign key(id_tugas)
	references tugas (id_tugas) on delete restrict on update restrict;

alter table pengumpulan_tugas add constraint fk_PT_id_siswa foreign key(id_siswa)
	references siswa (id_siswa) on delete restrict on update restrict;
    
alter table tugas_detail_les add constraint fk_TDL_tugas foreign key(id_tugas)
	references tugas (id_tugas) on delete restrict on update restrict;
    
alter table tugas_detail_les add constraint fk_TDL_id_detail_les foreign key(id_detail_les)
	references detail_les (id_detail_les) on delete restrict on update restrict;
    
alter table nilai add constraint fk_nilai_id_siswa foreign key(id_siswa)
	references siswa (id_siswa) on delete restrict on update restrict;
    
alter table nilai add constraint fk_nilai_tugas foreign key(id_tugas)
	references tugas (id_tugas) on delete restrict on update restrict;
    
alter table transaksi add constraint fk_trans_id_siswa foreign key(id_siswa)
	references siswa (id_siswa) on delete restrict on update restrict;

alter table detail_transaksi add constraint fk_DT_id_transaksi foreign key(id_transaksi)
	references transaksi (id_transaksi) on delete restrict on update restrict;

alter table detail_transaksi add constraint fk_DT_id_mapel foreign key(id_mapel)
	references mapel (id_mapel) on delete restrict on update restrict;

alter table detail_transaksi add constraint fk_DT_id_tutor foreign key(id_tutor)
	references tutor (id_tutor) on delete restrict on update restrict;