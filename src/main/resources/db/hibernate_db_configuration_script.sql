drop database if exists wikime_hiber;
create database wikime_hiber;

create table articles
(
    id     integer not null
        constraint articles_pkey
            primary key,
    header varchar(255),
    text   message_text
);

alter table articles
    owner to postgres;

create table tags
(
    id  integer not null
        constraint tags_pkey
            primary key,
    tag varchar(255)
);

alter table tags
    owner to postgres;


create table article_tag
(
    article_id integer not null
        constraint fk7p3k31j9h2wy52hf0cklkjl3k
            references articles,
    tag_id     integer not null
        constraint fk4e5j1bt5ixg4h3ak1pfq20ve9
            references tags,
    constraint article_tag_pkey
        primary key (article_id, tag_id)
);

alter table article_tag
    owner to postgres;

