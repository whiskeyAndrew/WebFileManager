create table file
(
    file_id     bigserial not null
        primary key,
    extension   varchar(255),
    file        bytea,
    metadata_id bigint
        constraint uk_dg6ywoxkwetx8vthaqjyj2wkb
            unique
        constraint fkspfgunf0vyspsaig7ckpx37p0
            references metadata
);