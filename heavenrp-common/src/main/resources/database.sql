--
-- Bank accounts
--

CREATE TABLE bank_accounts (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    balance         MEDIUMINT UNSIGNED  NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);

INSERT INTO bank_accounts (id, balance) VALUES ('1', '1000');

--
-- Companies
--

CREATE TABLE companies (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (name)
);

ALTER TABLE companies
    ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id);

INSERT INTO companies (id, name, bank_account_id) VALUES ('1', 'Heavencraft', '1');

--
-- Users
--

CREATE TABLE users (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    uuid            CHAR(36)            NOT NULL,
    name            VARCHAR(16)         NOT NULL,
    last_login      TIMESTAMP			NOT NULL DEFAULT "0000-00-00 00:00:00",
    balance         MEDIUMINT UNSIGNED  NOT NULL DEFAULT 200,
    home_number     TINYINT UNSIGNED    NOT NULL DEFAULT 2,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (uuid),
    UNIQUE (name)
);

ALTER TABLE users
    ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id);
--
-- Homes
--

CREATE TABLE homes (
    user_id         MEDIUMINT UNSIGNED  NOT NULL,
    home_number     TINYINT UNSIGNED    NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    yaw             FLOAT               NOT NULL,
    pitch           FLOAT               NOT NULL,

    PRIMARY KEY (user_id, home_number)
);

ALTER TABLE homes
    ADD FOREIGN KEY (user_id) REFERENCES users (id);

--
-- Towns
--

CREATE TABLE towns (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (name)
);

ALTER TABLE towns
    ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id);

CREATE TABLE towns_users (
    town_id         MEDIUMINT UNSIGNED  NOT NULL,
    user_id         MEDIUMINT UNSIGNED  NOT NULL,

    PRIMARY KEY (town_id, user_id)
);

ALTER TABLE towns_users
    ADD FOREIGN KEY (town_id) REFERENCES towns (id);

ALTER TABLE towns_users
    ADD FOREIGN KEY (user_id) REFERENCES users (id);
    

--
-- Warps
--
CREATE TABLE warps (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    creator         MEDIUMINT UNSIGNED  NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    yaw             FLOAT               NOT NULL,
    pitch           FLOAT               NOT NULL,
    
    PRIMARY KEY (id),
    UNIQUE (name)
);

ALTER TABLE warps
    ADD FOREIGN KEY (creator) REFERENCES users (id);

--
-- Insert global regions on HeavenGuard
--

INSERT INTO worlds (name, flag_pvp, flag_public) VALUES
("world", 0, 0),
("world_resources", 0, 1),
("world_nether", 1, 1),
("world_the_end", 1, 1);


--
-- Stocks
--

CREATE TABLE stocks (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    company_id      MEDIUMINT UNSIGNED  NOT NULL,
    name            VARCHAR(16)         NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               INT                 NOT NULL,
    y               TINYINT UNSIGNED    NOT NULL,
    z               INT                 NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (name, company_id)
);

ALTER TABLE stocks
    ADD FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE;
