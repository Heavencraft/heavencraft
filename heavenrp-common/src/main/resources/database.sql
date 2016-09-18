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
    name            VARCHAR(28)         NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (name),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id)
);

INSERT INTO companies (id, name, bank_account_id) VALUES ('1', 'Heavencraft', '1');

--
-- Users
--

CREATE TABLE users (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    uuid            CHAR(36)            NOT NULL,
    name            VARCHAR(16)         NOT NULL,
    balance         MEDIUMINT UNSIGNED  NOT NULL DEFAULT 200,
    home_number     TINYINT UNSIGNED    NOT NULL DEFAULT 2,
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
    home_number     TINYINT UNSIGNED    NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    yaw             FLOAT               NOT NULL,
    pitch           FLOAT               NOT NULL,

    PRIMARY KEY (user_id, home_number),
    FOREIGN KEY (user_id) REFERENCES users (id)
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

CREATE TABLE towns_users (
    town_id         MEDIUMINT UNSIGNED  NOT NULL,
    user_id         MEDIUMINT UNSIGNED  NOT NULL,

    PRIMARY KEY (town_id, user_id),
    FOREIGN KEY (town_id) REFERENCES towns (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

--
-- Stocks
--

CREATE TABLE stocks (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    owner_id        MEDIUMINT UNSIGNED  NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES users (id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id)
);

--
-- Stores
--
CREATE TABLE stores (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    stock_id        MEDIUMINT UNSIGNED  NOT NULL,
    qty             TINYINT UNSIGNED    NOT NULL,
    price           MEDIUMINT UNSIGNED  NOT NULL,
    isBuyer         BOOLEAN             NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    x               DOUBLE              NOT NULL,
    y               DOUBLE              NOT NULL,
    z               DOUBLE              NOT NULL,
    
    PRIMARY KEY (id),
    FOREIGN KEY (stock_id) REFERENCES stocks (id)
);

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
    UNIQUE (name),
    FOREIGN KEY (creator) REFERENCES users (id)
);