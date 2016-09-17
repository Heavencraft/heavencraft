--
-- Bank accounts
--

CREATE TABLE bank_accounts (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    balance         MEDIUMINT UNSIGNED  NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

--
-- Companies
--

CREATE TABLE companies (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(28)         NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (name),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id)
);

--
-- Towns
--

CREATE TABLE towns (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(28)         NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (name),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id)
);

--
-- Users
--

CREATE TABLE users (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    uuid            CHAR(36)            NOT NULL,
    name            VARCHAR(16)         NOT NULL,
    balance         MEDIUMINT UNSIGNED  NOT NULL DEFAULT 0,
    home_number     TINYINT UNSIGNED    NOT NULL DEFAULT 1,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (uuid),
    UNIQUE (name),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id)
);

--
-- Homes
--

CREATE TABLE homes (
    user_id         MEDIUMINT UNSIGNED  NOT NULL,
    home_nb         TINYINT UNSIGNED    NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    pitch           FLOAT               NOT NULL,
    yaw             FLOAT               NOT NULL,

    PRIMARY KEY (user_id, home_nb),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

--
-- Warps
--
CREATE TABLE warps (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    creator         MEDIUMINT UNSIGNED  NOT NULL,
    price           INTEGER             NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    yaw             FLOAT               NOT NULL,
    pitch           FLOAT               NOT NULL,
    
    PRIMARY KEY (id),
    UNIQUE (name),
    FOREIGN KEY (creator) REFERENCES users (id)
);