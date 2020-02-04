/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     25-Dec-19 15:33:08                           */
/*==============================================================*/


drop table if exists CITY;

drop table if exists CLINIC;

drop table if exists COUNTRY;

drop table if exists DIAGNOSIS;

drop table if exists DOCTORSPECIALIZATION;

drop table if exists EXAMINATIONMEDICALSTAFF;

drop table if exists EXAMINATION;

drop table if exists MEDICAL_RECORD;

drop table if exists MEDICINE;

drop table if exists PRICELIST;

drop table if exists REPORT;

drop table if exists REPORTDIAGNOSIS;

drop table if exists REPORTMEDICINE;

drop table if exists ROLE;

drop table if exists ROOM;

drop table if exists ROOMTYPE;

drop table if exists SCHEDULE;

drop table if exists TYPEOFEXAMINATION;

drop table if exists USER;

drop table if exists VACATION;

/*==============================================================*/
/* Table: CITY                                                  */
/*==============================================================*/
create table CITY
(
   ID_CITY              int not null AUTO_INCREMENT,
   ID_COUNTRY           int not null,
   NAME                 varchar(1024) not null,
   primary key (ID_CITY)
);

/*==============================================================*/
/* Table: CLINIC                                                */
/*==============================================================*/
create table CLINIC
(
   ID_CLINIC            int not null AUTO_INCREMENT,
   ID_CITY              integer not null,
   NAME                 varchar(1024) not null,
   STREET               varchar(1024) not null,
   WORKTIME_START       time not null,
   WORKTIME_END         time,
   DESCRIPTION          varchar(1024),
   DELETED              bool not null,
   primary key (ID_CLINIC)
);

/*==============================================================*/
/* Table: COUNTRY                                               */
/*==============================================================*/
create table COUNTRY
(
   ID_COUNTRY           int not null AUTO_INCREMENT,
   NAME                 varchar(1024) not null,
   primary key (ID_COUNTRY)
);

/*==============================================================*/
/* Table: DIAGNOSIS                                             */
/*==============================================================*/
create table DIAGNOSIS
(
   ID_DIAGNOSIS         int not null AUTO_INCREMENT,
   NAME                 varchar(1024) not null,
   DESCRIPTION          varchar(1024),
   DELETED              bool not null,
   primary key (ID_DIAGNOSIS)
);

/*==============================================================*/
/* Table: DOCTORSPECIALIZATION                                   */
/*==============================================================*/
create table DOCTORSPECIALIZATION
(
   ID_TYPE_OF_EXAMINATION int not null,
   ID_USER              int not null,
   primary key (ID_TYPE_OF_EXAMINATION, ID_USER)
);

/*==============================================================*/
/* Table: EXAMINATION                                           */
/*==============================================================*/
create table EXAMINATION
(
   ID_EXAMINATION       int not null AUTO_INCREMENT,
   ID_USER              int,
   ID_ROOM              int,
   ID_ROOM_TYPE         int not null,
   ID_DOCTOR            int not null,
   ID_PRICELIST         int not null,
   PREDEFINED           bool not null,
   FINISHED             bool not null,
   DISCOUNT             decimal not null,
   ACCEPTED             bool not null,
   GRADE_CLINIC         decimal,
   GRADE_DOCTOR         decimal,
   DELETED              bool not null,
   CANCELED             bool not null,
   DATE_TIME            datetime not null,
   primary key (ID_EXAMINATION)
);

/*==============================================================*/
/* Table: MEDICAL_RECORD                                        */
/*==============================================================*/
create table MEDICAL_RECORD
(
   ID_MEDICAL_RECORD    int not null AUTO_INCREMENT,
   BLOOD_TYPE_RH        varchar(3) not null,
   HEIGHT               decimal not null,
   WEIGHT               decimal not null,
   RACE                 varchar(1024) not null,
   DELETED              bool not null,
   primary key (ID_MEDICAL_RECORD)
);

/*==============================================================*/
/* Table: MEDICINE                                              */
/*==============================================================*/
create table MEDICINE
(
   ID_MEDICINE          int not null AUTO_INCREMENT,
   NAME                 varchar(1024),
   DESCRIPTION          varchar(1024),
   DELETED              bool not null,
   primary key (ID_MEDICINE)
);

/*==============================================================*/
/* Table: PRICELIST                                             */
/*==============================================================*/
create table PRICELIST
(
   ID_PRICELIST         int not null AUTO_INCREMENT,
   ID_CLINIC            int not null,
   ID_TYPE_OF_EXAMINATION int not null,
   PRICE                decimal(8,2) not null,
   DELETED              bool not null,
   primary key (ID_PRICELIST)
);

/*==============================================================*/
/* Table: REPORT                                                */
/*==============================================================*/
create table REPORT
(
   ID_REPORT            int not null AUTO_INCREMENT,
   ID_USER              int,
   DESCRIPTION          varchar(1024),
   MEDICINE_CERTIFIED   bool not null,
   DELETED              bool not null,
   primary key (ID_REPORT)
);

/*==============================================================*/
/* Table: REPORTDIAGNOSIS                                       */
/*==============================================================*/
create table REPORTDIAGNOSIS
(
   ID_DIAGNOSIS         int not null AUTO_INCREMENT,
   ID_REPORT            int not null,
   primary key (ID_DIAGNOSIS, ID_REPORT)
);

/*==============================================================*/
/* Table: REPORTMEDICINE                                        */
/*==============================================================*/
create table REPORTMEDICINE
(
   ID_REPORT            int not null AUTO_INCREMENT,
   ID_MEDICINE          int not null,
   primary key (ID_REPORT, ID_MEDICINE)
);

/*==============================================================*/
/* Table: ROLE                                                  */
/*==============================================================*/
create table ROLE
(
   ID_ROLE              int not null AUTO_INCREMENT,
   NAME                 varchar(1024) not null,
   primary key (ID_ROLE)
);

/*==============================================================*/
/* Table: ROOM                                                  */
/*==============================================================*/
create table ROOM
(
   ID_ROOM              int not null AUTO_INCREMENT,
   ID_CLINIC            int not null,
   ID_ROOM_TYPE         int not null,
   NUMBER               varchar(1024) not null,
   FLOOR                 int not null,
   DELETED              bool not null,
   primary key (ID_ROOM)
);

/*==============================================================*/
/* Table: ROOMTYPE                                              */
/*==============================================================*/
create table ROOMTYPE
(
   ID_ROOM_TYPE         int not null AUTO_INCREMENT,
   NAME                 varchar(1024),
   primary key (ID_ROOM_TYPE)
);

/*==============================================================*/
/* Table: SCHEDULE                                              */
/*==============================================================*/
create table SCHEDULE
(
   ID_SCHEDULE          int not null AUTO_INCREMENT,
   ID_USER              int,
   START_DATE_SCHEDULE  date not null,
   END_DATE_SCHEDULE    date not null,
   SHIFT_START_TIME     time not null,
   SHIFT_END_TIME       time not null,
   DELETED              bool not null,
   primary key (ID_SCHEDULE)
);

/*==============================================================*/
/* Table: TYPEOFEXAMINATION                                     */
/*==============================================================*/
create table TYPEOFEXAMINATION
(
   ID_TYPE_OF_EXAMINATION int not null AUTO_INCREMENT,
   ID_TYPE              int not null,
   DURATION             time not null,
   NAME                 varchar(1024) not null,
   DESCRIPTION          varchar(1024),
   DELETED              bool not null,
   primary key (ID_TYPE_OF_EXAMINATION)
);

/*==============================================================*/
/* Table: USER                                                  */
/*==============================================================*/
create table USER
(
   ID_USER              int not null AUTO_INCREMENT,
   ID_ROLE              int not null,
   ID_MEDICAL_RECORD    int,
   ID_CLINIC            int,
   ID_CITY              int not null,
   JBO                  varchar(1024),
   NAME                 varchar(1024) not null,
   LASTNAME             varchar(1024) not null,
   EMAIL                varchar(1024) not null,
   PASSWORD             varchar(1024) not null,
   PASSWORD_CHANGED     varchar(1024) not null,
   STREET               varchar(1024),
   PHONE                varchar(1024),
   VERIFIED             bool not null,
   DELETED              bool not null,
   USERNAME             varchar(128) not null,
   DATE_OF_BIRTH        date not null,
   ADMIN_APPROVED       bool not null
   primary key (ID_USER)
);

/*==============================================================*/
/* Table: VACATION                                              */
/*==============================================================*/
create table VACATION
(
   ID_VACATION          int not null AUTO_INCREMENT,
   ID_USER              int not null,
   ACCEPTED             bool not null,
   START_DATE           date not null,
   END_DATE             date not null,
   DESCRIPTION          varchar(1024),
   DELETED              bool not null,
   primary key (ID_VACATION)
);

alter table CITY add constraint FK_RELATIONSHIP_12 foreign key (ID_COUNTRY)
      references COUNTRY (ID_COUNTRY) on delete restrict on update restrict;

alter table CLINIC add constraint FK_RELATIONSHIP_14 foreign key (ID_CITY)
      references CITY (ID_CITY) on delete restrict on update restrict;

alter table DOCTORSPECIALIZATION add constraint FK_DOCTORSPECIALIZATION foreign key (ID_TYPE_OF_EXAMINATION)
      references TYPEOFEXAMINATION (ID_TYPE_OF_EXAMINATION) on delete restrict on update restrict;

alter table DOCTORSPECIALIZATION add constraint FK_DOCTORSPECIALIZATION2 foreign key (ID_USER)
      references USER (ID_USER) on delete restrict on update restrict;

alter table EXAMINATION add constraint FK_EXAMINATIONPATIENT foreign key (ID_USER)
      references USER (ID_USER) on delete restrict on update restrict;

alter table EXAMINATION add constraint FK_EXAMINATIONDOCTOR foreign key (ID_DOCTOR)
references USER (ID_USER) on delete restrict on update restrict;

alter table EXAMINATION add constraint FK_RELATIONSHIP_24 foreign key (ID_PRICELIST)
      references PRICELIST (ID_PRICELIST) on delete restrict on update restrict;

alter table EXAMINATION add constraint FK_RELATIONSHIP_25 foreign key (ID_ROOM_TYPE)
      references ROOMTYPE (ID_ROOM_TYPE) on delete restrict on update restrict;

alter table EXAMINATION add constraint FK_RELATIONSHIP_5 foreign key (ID_ROOM)
      references ROOM (ID_ROOM) on delete restrict on update restrict;

alter table PRICELIST add constraint FK_RELATIONSHIP_20 foreign key (ID_CLINIC)
      references CLINIC (ID_CLINIC) on delete restrict on update restrict;

alter table PRICELIST add constraint FK_RELATIONSHIP_21 foreign key (ID_TYPE_OF_EXAMINATION)
      references TYPEOFEXAMINATION (ID_TYPE_OF_EXAMINATION) on delete restrict on update restrict;

alter table REPORT add constraint FK_AUTHENTICATION foreign key (ID_USER)
      references USER (ID_USER) on delete restrict on update restrict;

alter table REPORTDIAGNOSIS add constraint FK_REPORTDIAGNOSIS foreign key (ID_DIAGNOSIS)
      references DIAGNOSIS (ID_DIAGNOSIS) on delete restrict on update restrict;

alter table REPORTDIAGNOSIS add constraint FK_REPORTDIAGNOSIS2 foreign key (ID_REPORT)
      references REPORT (ID_REPORT) on delete restrict on update restrict;

alter table REPORTMEDICINE add constraint FK_RELATIONSHIP_18 foreign key (ID_REPORT)
      references REPORT (ID_REPORT) on delete restrict on update restrict;

alter table REPORTMEDICINE add constraint FK_RELATIONSHIP_19 foreign key (ID_MEDICINE)
      references MEDICINE (ID_MEDICINE) on delete restrict on update restrict;

alter table ROOM add constraint FK_CLINICROOM foreign key (ID_CLINIC)
      references CLINIC (ID_CLINIC) on delete restrict on update restrict;

alter table ROOM add constraint FK_RELATIONSHIP_26 foreign key (ID_ROOM_TYPE)
      references ROOMTYPE (ID_ROOM_TYPE) on delete restrict on update restrict;

alter table TYPEOFEXAMINATION add constraint FK_TYPEOFEXAMINATIONROOMTYPE foreign key (ID_TYPE)
      references ROOMTYPE (ID_ROOM_TYPE) on delete restrict on update restrict;

alter table SCHEDULE add constraint FK_RELATIONSHIP_22 foreign key (ID_USER)
      references USER (ID_USER) on delete restrict on update restrict;

alter table USER add constraint FK_PATIENTMEDICALRECORD foreign key (ID_MEDICAL_RECORD)
      references MEDICAL_RECORD (ID_MEDICAL_RECORD) on delete restrict on update restrict;

alter table USER add constraint FK_RELATIONSHIP_13 foreign key (ID_CITY)
      references CITY (ID_CITY) on delete restrict on update restrict;

alter table USER add constraint FK_USER_ROLE foreign key (ID_ROLE)
      references ROLE (ID_ROLE) on delete restrict on update restrict;

alter table USER add constraint FK_WORK foreign key (ID_CLINIC)
      references CLINIC (ID_CLINIC) on delete restrict on update restrict;

alter table VACATION add constraint FK_RELATIONSHIP_23 foreign key (ID_USER)
      references USER (ID_USER) on delete restrict on update restrict;

INSERT INTO `clinical_center_db`.`role` (`ID_ROLE`, `NAME`) VALUES ('1', 'ROLE_ADMIN_SYSTEM');
INSERT INTO `clinical_center_db`.`role` (`ID_ROLE`, `NAME`) VALUES ('2', 'ROLE_ADMIN_CLINIC');
INSERT INTO `clinical_center_db`.`role` (`ID_ROLE`, `NAME`) VALUES ('3', 'ROLE_DOCTOR');
INSERT INTO `clinical_center_db`.`role` (`ID_ROLE`, `NAME`) VALUES ('4', 'ROLE_NURSE');
INSERT INTO `clinical_center_db`.`role` (`ID_ROLE`, `NAME`) VALUES ('5', 'ROLE_USER');

INSERT INTO `clinical_center_db`.`roomtype` (`ID_ROOM_TYPE`, `NAME`) VALUES ('1', 'EXAMINATION');
INSERT INTO `clinical_center_db`.`roomtype` (`ID_ROOM_TYPE`, `NAME`) VALUES ('2', 'OPERATION');

INSERT INTO COUNTRY(ID_COUNTRY, NAME)
VALUES (1, 'Serbia');

INSERT INTO CITY(ID_CITY, NAME, ID_COUNTRY) VALUES (1, 'Novi Sad', '1');
INSERT INTO `clinical_center_db`.`city` (`ID_CITY`, `ID_COUNTRY`, `NAME`) VALUES ('2', '1', 'Belgrade');

INSERT INTO `clinical_center_db`.`user` (`ID_USER`, `ID_ROLE`, `ID_CITY`, `NAME`, `LASTNAME`, `EMAIL`, `PASSWORD`, `PASSWORD_CHANGED`, `STREET`, `VERIFIED`, `DELETED`, `USERNAME`, `ADMIN_APPROVED`, `GENDER`) VALUES ('1', '1', '1', 'Dragan', 'Jovic', 'draganjovic96@hotmail.rs', '$2y$10$Wchw26xG1ZR92xiOL5qE6ujILmlpnq7rvh/dV5Rp3Uz.kjULKdcEm', '0', 'Jovana Subiotica 4, 3.4', '1', '0', 'admin', '1', '0');
