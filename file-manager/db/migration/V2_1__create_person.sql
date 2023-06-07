CREATE SEQUENCE person_user_id_seq START 1;

create table person
(
    user_id  bigserial not null
        primary key,
    username varchar(255)
);

