create table if not exists directory
(
    id               serial not null
        primary key,
    directory_name   varchar(255),
    full_path        varchar(255),
    parent_directory bigint
        constraint fk6ytqp4lmjubx6ohdtjf0e9bf4
            references directory
);

INSERT INTO directory (id,directory_name,full_path,parent_directory) VALUES (0,null,null,null);