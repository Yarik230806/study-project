create sequence estimated_parts_estimated_parts_id_seq;

alter sequence estimated_parts_estimated_parts_id_seq owner to postgres;

create table role
(
    role_id      integer generated by default as identity
        primary key,
    name_of_role varchar(200) not null
        unique
);

alter table role
    owner to postgres;

create table users
(
    user_id               integer generated by default as identity
        primary key,
    role_id               integer
        references role,
    login                 varchar(150) not null
        unique,
    password              varchar(150) not null,
    first_name            varchar(200) not null,
    last_name             varchar(200) not null,
    insurance_company     varchar(100),
    insurance_expiry_date date
);

alter table users
    owner to postgres;

create table status
(
    status_id      integer generated by default as identity
        primary key,
    name_of_status varchar(100) not null
);

alter table status
    owner to postgres;

create table vehicle_information
(
    vehicle_information_id integer generated by default as identity
        primary key,
    vin                    varchar(30)  not null,
    year                   integer      not null
        constraint vehicle_information_year_check
            check ((year > 1800) AND (year < 3000)),
    make_in                varchar(100),
    license_part           varchar(50)  not null,
    license_state          varchar(100) not null,
    license_expiration     date         not null,
    odometer_value         integer
        constraint vehicle_information_odometer_value_check
            check (odometer_value >= 0),
    model                  varchar(100)
);

alter table vehicle_information
    owner to postgres;

create table vehicle_condition
(
    vehicle_condition_id integer generated by default as identity
        primary key,
    photos               varchar(200)
);

alter table vehicle_condition
    owner to postgres;

create table assignment
(
    assignment_id          bigint generated by default as identity (maxvalue 2147483647)
        primary key,
    user_id                integer
        references users
            on delete cascade,
    status_id              integer
                                references status
                                    on delete set null,
    price                  numeric(9, 2)
        constraint assignment_price_check
            check (price >= (0)::numeric),
    note                   varchar(500),
    date_of_incident       date not null,
    vehicle_information_id integer
                                references vehicle_information
                                    on delete set null,
    supplement_id          integer,
    vehicle_condition_id   integer
                                references vehicle_condition
                                    on delete set null
);

alter table assignment
    owner to postgres;

create table type_of_parts
(
    type_of_parts_id integer generated by default as identity
        constraint type_of_parts_pk
            primary key,
    name_of_type     varchar(100)
);

alter table type_of_parts
    owner to postgres;

create unique index type_of_parts_type_of_parts_id_uindex
    on type_of_parts (type_of_parts_id);

create table other_charges
(
    other_charges_id integer generated by default as identity
        primary key,
    towing           numeric(8, 2),
    storage          numeric(8, 2),
    supplement_id    integer
);

alter table other_charges
    owner to postgres;

create table supplement
(
    supplement_id    integer generated by default as identity
        primary key,
    assignment_id    integer
        references assignment
            on delete set null,
    other_charges_id integer
        references other_charges
            on delete set null
);

alter table supplement
    owner to postgres;

alter table assignment
    add foreign key (supplement_id) references supplement
        on delete set null;

create table estimated_parts
(
    estimated_part_id integer generated by default as identity
        constraint estimated_parts_pk
            primary key,
    type_of_parts_id  integer
        references type_of_parts
            on delete set null,
    description       varchar(500),
    labor_hours       numeric(4, 2),
    price             numeric(9, 2),
    labor_rate        numeric(9, 2),
    supplement_id     integer
        references supplement
            on delete cascade
);

alter table estimated_parts
    owner to postgres;

alter sequence estimated_parts_estimated_parts_id_seq owned by estimated_parts.estimated_part_id;

alter table other_charges
    add foreign key (supplement_id) references supplement
        on delete cascade;

create table type_of_contact
(
    type_of_contact_id integer generated by default as identity
        primary key,
    name_of_type       varchar(50)
);

alter table type_of_contact
    owner to postgres;

create table contacts
(
    contact_id         integer generated by default as identity
        primary key,
    assignment_id      integer
        references assignment
            on delete cascade,
    type_of_contact_id integer
        references type_of_contact
            on delete set null,
    email              varchar(40),
    firstname          varchar(50),
    lastname           varchar(50)
);

alter table contacts
    owner to postgres;

create table address
(
    address_id      integer generated by default as identity
        primary key,
    contact_id      integer
        constraint address_contacts_id_fkey
            references contacts
            on delete cascade,
    city            varchar(100),
    state           varchar(50),
    zip             varchar(40),
    address_line    varchar(100),
    type_of_address varchar(20)
);

alter table address
    owner to postgres;

create table phone
(
    phone_id      integer generated by default as identity
        primary key,
    contact_id    integer
        references contacts
            on delete cascade,
    number        varchar(20),
    type_of_phone varchar(20)
);

alter table phone
    owner to postgres;

create table impact_direction
(
    impact_direction_id  integer generated by default as identity
        constraint impact_direction_pk
            primary key,
    name                 varchar(50),
    vehicle_condition_id integer
        references vehicle_condition
);

alter table impact_direction
    owner to postgres;


SELECT setval(assignment_assignment_id_seq, 10000000);

insert into role (role_id, name_of_role)
values (1, 'ROLE_Client'),
       (2, 'ROLE_Insurance agency'),
       (3, 'ROLE_Estimator');

insert into type_of_parts (type_of_parts_id, name_of_type)
values (1, 'Replacement'),
       (2, 'Repair'),
       (3, 'Paint');

insert into type_of_contact (type_of_contact_id, name_of_type)
values (1, 'Vehicle_owner'),
       (2, 'Police'),
       (3, 'Vehicle'),
       (4, 'Driver'),
       (5, 'Attorney'),
       (6, 'Rental');

insert into status (status_id, name_of_status)
values (1, 'New'),
       (2, 'In progress'),
       (3, 'Assigned'),
       (4,'In review'),
       (5,'Total loss'),
       (6,'Repair'),
       (7,'Repaired');

