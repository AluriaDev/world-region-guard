create table if not exists region(
    world_name varchar(20) not null,
    name varchar(25) not null,
    location_start varchar(100) not null,
    location_end varchar(100) not null,
    priority int not null default 1,
    primary key(world_name, name)
);