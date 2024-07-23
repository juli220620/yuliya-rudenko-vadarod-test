--liquibase formatted sql
--changeset Julia_Rudenko:data-structure failOnError:true

create table currency_dict (
    id bigint not null primary key,
    code int not null,
    abbreviation varchar(256) not null,
    scale int not null,
    name varchar(256) not null,
    name_multi varchar(256) not null
);

create table currency_rate (
    curr_id bigint not null,
    date date not null,
    curr_rate double not null,
    primary key (curr_id, date),
    foreign key (curr_id) references currency_dict (id)
);