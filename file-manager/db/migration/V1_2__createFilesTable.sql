create table if not exists efile
(
    id           bigserial
        primary key,
    file_name    varchar(255),
    file_path    varchar(255),
    file_size    bigint,
    file_type    varchar(255),
    directory_id bigint
        constraint fk4xfy1y07xihfq2b2r3kme6so4
            references directory,
    -- пока без связи
    owner_id     bigint
);

