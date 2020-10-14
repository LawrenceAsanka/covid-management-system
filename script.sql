create table hospital
(
    hospital_id         varchar(20)                        not null
        primary key,
    hospital_name       varchar(30)                        not null,
    city                varchar(20)                        not null,
    district            varchar(20)                        not null,
    capacity            int                                not null,
    director_name       varchar(20)                        not null,
    director_contact    varchar(11)                        not null,
    hospital_contact_01 varchar(11)                        not null,
    hospital_contact_02 varchar(11)                        not null,
    hospital_fax_number varchar(11)                        not null,
    hospital_email      varchar(30)                        not null,
    status              varchar(20) default 'not reserved' not null
);

create table quarantine_center
(
    quarantine_id           varchar(10)                        not null
        primary key,
    quarantine_name         varchar(30)                        not null,
    quarantine_city         varchar(20)                        not null,
    quarantine_district     varchar(20)                        not null,
    quarantine_head_name    varchar(20)                        not null,
    quarantine_head_contact varchar(11)                        not null,
    quarantine_contact_01   varchar(11)                        not null,
    quarantine_contact_02   varchar(11)                        not null,
    quarantine_capacity     int                                not null,
    status                  varchar(20) default 'not reserved' not null
);

create table user
(
    user_id        varchar(10)               not null
        primary key,
    first_name     varchar(30)               not null,
    contact_number varchar(11)               not null,
    email          varchar(30)               not null,
    role           varchar(20)               not null,
    username       varchar(20)               not null,
    password       varchar(20)               not null,
    location       varchar(30) default 'all' not null,
    constraint username
        unique (username)
);

create table global_update_detail
(
    update_id       int auto_increment
        primary key,
    admin_id        varchar(10) not null,
    updated_date    date        not null,
    confirmed_count decimal     not null,
    recovered_count decimal     not null,
    death_count     decimal     not null,
    constraint global_update_detail_ibfk_1
        foreign key (admin_id) references user (user_id)
);

create index admin_id
    on global_update_detail (admin_id);


