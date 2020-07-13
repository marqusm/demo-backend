CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE account
(
    id            UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    username      TEXT             NOT NULL UNIQUE,
    password_hash TEXT             NOT NULL,
    role          TEXT             NOT NULL,
    name          TEXT             NOT NULL
);

CREATE TABLE file
(
    id      UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name    TEXT             NOT NULL,
    size    BIGINT           NOT NULL,
    content BYTEA            NOT NULL
);

CREATE TABLE picture
(
    id                 UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    account_id         UUID             NOT NULL REFERENCES account (id),
    file_id            UUID             NOT NULL UNIQUE REFERENCES file (id),
    name               TEXT             NOT NULL,
    displayable_status TEXT             NOT NULL,
    purchasable_status TEXT             NOT NULL
);

CREATE TABLE hashtag
(
    id   UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    name TEXT             NOT NULL UNIQUE
);

CREATE TABLE picture_x_hashtag
(
    picture_id UUID NOT NULL REFERENCES picture (id),
    hashtag_id UUID NOT NULL REFERENCES hashtag (id),
    PRIMARY KEY (picture_id, hashtag_id)
);

CREATE TABLE soc_like
(
    account_id UUID NOT NULL REFERENCES account (id),
    picture_id UUID NOT NULL REFERENCES picture (id),
    PRIMARY KEY (account_id, picture_id)
);

CREATE TABLE transaction
(
    id                          UUID PRIMARY KEY NOT NULL DEFAULT uuid_generate_v4(),
    seller_account_id           UUID             NOT NULL REFERENCES account (id),
    buyer_account_id            UUID             NOT NULL REFERENCES account (id),
    external_transaction_id     TEXT,
    amount                      NUMERIC(15, 2)   NOT NULL,
    currency                    TEXT,
    external_transaction_status TEXT,
    buyer_full_name             TEXT             NOT NULL,
    buyer_address               TEXT             NOT NULL,
    buyer_email                 TEXT             NOT NULL,
    created_at                  TIMESTAMP,
    status                      TEXT             NOT NULL
);

CREATE VIEW hashtag_popularity AS
SELECT h.name AS name, count(*) AS count
FROM picture_x_hashtag pxh
         INNER JOIN hashtag h ON pxh.hashtag_id = h.id
GROUP BY h.name, pxh.hashtag_id;

CREATE VIEW soc_like_count AS
SELECT picture_id, count(*)
FROM soc_like
GROUP BY picture_id;

INSERT INTO account(username, password_hash, role, name)
VALUES ('admin', 'pass', 'ADMIN', 'Admin');