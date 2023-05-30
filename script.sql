create table role
(
    role_id      integer generated by default as identity
        primary key,
    name_of_role varchar(200) not null
        unique
);

alter table role
    owner to postgres;

create table user
(
    person_id  integer generated by default as identity
        primary key,
    role_id    integer
        references role,
    login      varchar(150) not null
        unique,
    password   varchar(150) not null,
    first_name varchar(200) not null,
    last_name  varchar(200) not null
);

alter table user
    owner to postgres;

insert into role (role_id, name_of_role) values
(1,'Client'),(2,'Insurance agency'),(3,'Estimator');