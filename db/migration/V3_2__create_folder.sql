CREATE SEQUENCE folder_folder_id_seq START 1;

create table folder
(
    folder_id   bigserial not null
        primary key,
    metadata_id bigint
        constraint uk_7u1uylw2taseed5ee9e2anmlr
            unique
        constraint fkfdq7k2vevcgplg5koah818x1f
            references metadata
);