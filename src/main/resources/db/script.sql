-- create database wikime_test;
create table tags(
                     id serial not null primary key,
                     tag_body varchar(64)
);

create table paragraphs
(
    id serial not null,
    text TEXT
);

create unique index paragraphs_id_uindex
	on paragraphs (id);

alter table paragraphs
    add constraint paragraphs_pk
        primary key (id);
