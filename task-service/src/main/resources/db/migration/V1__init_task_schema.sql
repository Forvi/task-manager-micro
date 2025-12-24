CREATE TYPE task.level_typa AS ENUM ('HIGH', 'MIDDLE', 'LOW', 'NEUTRAL');

CREATE TABLE task.tasks
(
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    active           boolean NOT NULL DEFAULT TRUE,
    created          TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    updated          TIMESTAMP NOT NULL DEFAULT (now() AT TIME ZONE 'utc'),
    name             VARCHAR(64) NOT NULL,
    completed        boolean NOT NULL DEFAULT FALSE,
    importance_level VARCHAR(20) NOT NULL DEFAULT 'NEUTRAL',
    user_id          UUID NOT NULL
);
