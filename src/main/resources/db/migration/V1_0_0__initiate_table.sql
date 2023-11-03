drop table if exists crypto_order CASCADE;
drop table if exists crypto_user CASCADE ;
drop table if exists crypto_trading_pair_price CASCADE;
drop table if exists crypto_wallet CASCADE;
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
create table crypto_order (id bigint not null, created_date timestamp, updated_date timestamp, buy varchar(255), crypto_pair varchar(255), sell varchar(255), type integer, user_id bigint, primary key (id));
create table crypto_user (id bigint not null, created_date timestamp, updated_date timestamp, user_name varchar(255), primary key (id));
create table crypto_trading_pair_price (id bigint not null, created_date timestamp, updated_date timestamp, ask_price double, bid_price double, symbol varchar(255), primary key (id));
create table crypto_wallet (id bigint not null, created_date timestamp, updated_date timestamp, balance double, crypto_unit integer, user_id bigint, primary key (id));
alter table crypto_trading_pair_price add constraint UK_tom45agcxvqp4lum50ef2mtyc unique (symbol);
alter table crypto_order add constraint FK8ln77rlrynqk7iw8rqvi2el7n foreign key (user_id) references crypto_user;
alter table crypto_wallet add constraint FKsjr12h28imk77c4re68qv4h6p foreign key (user_id) references crypto_user;

