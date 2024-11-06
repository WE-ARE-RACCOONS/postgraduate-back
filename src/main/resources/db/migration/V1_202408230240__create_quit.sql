CREATE TABLE IF NOT EXISTS quit
(
    quit_id bigint auto_increment
    primary key,
    reason  varchar(255)                     null,
    etc     text                             null,
    role    enum ('ADMIN', 'SENIOR', 'USER') not null
    );
