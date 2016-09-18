CREATE TABLE regions (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    name            VARCHAR(64)         NOT NULL,
    parent_id       INT UNSIGNED        DEFAULT NULL,
    world           VARCHAR(16)         NOT NULL,
    min_x           INT                 NOT NULL,
    min_y           TINYINT UNSIGNED    NOT NULL,
    min_z           INT                 NOT NULL,
    max_x           INT                 NOT NULL,
    max_y           TINYINT UNSIGNED    NOT NULL,
    max_z           INT                 NOT NULL,
    flag_pvp        BOOLEAN             DEFAULT NULL,
    flag_public     BOOLEAN             DEFAULT NULL,
    flag_remove_timestamp TIMESTAMP     NULL DEFAULT NULL,
    flag_state      MEDIUMBLOB          DEFAULT NULL,

    PRIMARY KEY (id),
    UNIQUE (name)
);

ALTER TABLE regions
    ADD FOREIGN KEY (parent_id) REFERENCES regions (id) ON DELETE SET NULL;

CREATE TABLE regions_members (
    region_id       INT UNSIGNED        NOT NULL,
    user_id         MEDIUMINT UNSIGNED  NOT NULL,
    owner           BOOLEAN             NOT NULL DEFAULT 0,

    PRIMARY KEY (region_id, user_id)
);

ALTER TABLE regions_members
    ADD FOREIGN KEY (region_id) REFERENCES regions (id);

ALTER TABLE regions_members
    ADD FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE worlds (
    name            VARCHAR(32)         NOT NULL,
    flag_pvp        BOOLEAN             NOT NULL DEFAULT 0,
    flag_public     BOOLEAN             NOT NULL DEFAULT 0,

    PRIMARY KEY (name)
);