CREATE SEQUENCE metadata_metadata_id_seq START 1;

create table metadata
(
    metadata_id bigserial not null
        primary key,
    created_at  timestamp(6) with time zone,
    name        varchar(255),
    user_id     bigint
        references person,
    folder_id   bigint
);