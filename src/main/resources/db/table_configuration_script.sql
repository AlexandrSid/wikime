-- drop database if exists wikime_test;
-- create database wikime_test;

--
create table articles
(
    id    integer     not null
        constraint articles_pk
            primary key,
    title varchar(64) not null,
    text  text
);

alter table articles
    owner to postgres;

create unique index articles_id_uindex
    on articles (id);

--
create table tags
(
    id       serial not null
        constraint tags_pkey
            primary key,
    tag_body varchar(64)
);

alter table tags
    owner to postgres;

--
create table tags_connector
(
    tag_id     integer
        constraint tags_connector_tags_id_fk
            references tags
            on update cascade on delete cascade,
    article_id integer
        constraint tags_connector_articles_id_fk
            references articles
            on update cascade on delete cascade
);

alter table tags_connector
    owner to postgres;

