CREATE TABLE tt_user
(
    id           UUID    NOT NULL,
    first_name   VARCHAR NOT NULL,
    last_name    VARCHAR NOT NULL,
    gender       VARCHAR NOT NULL,
    birthday     DATE    NOT NULL,
    email        VARCHAR NOT NULL,
    phone_number VARCHAR NOT NULL,
    PRIMARY KEY (id)
);
