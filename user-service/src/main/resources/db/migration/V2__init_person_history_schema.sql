CREATE TABLE person_history.revinfo
(
    rev       BIGSERIAL PRIMARY KEY,
    revtmstmp BIGINT
);

CREATE TABLE person_history.users_history
(
    id            UUID NOT NULL,
    active        boolean NOT NULL DEFAULT TRUE,
    created       TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated       TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    username      VARCHAR(64) NOT NULL,
    password      VARCHAR(128) NOT NULL,
    email         VARCHAR(64) NOT NULL,
    first_name    VARCHAR(64) NOT NULL,
    last_name     VARCHAR(64) NOT NULL,
    revision      BIGINT NOT NULL,
    revision_type SMALLINT NOT NULL,

    CONSTRAINT pk_users_history PRIMARY KEY (id, revision),
    CONSTRAINT fk_users_history_rev FOREIGN KEY (revision) REFERENCES person_history.revinfo (rev)
);

CREATE INDEX IF NOT EXISTS idx_users_history_revision ON person_history.users_history (revision);