create table if not exists region_entity
(
    id             char(36)    not null primary key,
    world_name     varchar(20) not null,
    region_name    varchar(25) not null,
    location_start varchar(50) not null,
    location_end   varchar(50) not null,
    display_name   varchar(50)          default null,
    priority       int         not null default 1,
    unique (world_name, region_name)
);