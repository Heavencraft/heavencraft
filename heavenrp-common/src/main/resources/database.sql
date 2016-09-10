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
    bank_account_id MEDIUMINT UNSIGNED  NOT NULL,

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
    bank_account_id MEDIUMINT UNSIGNED  NOT NULL,

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
    bank_account_id MEDIUMINT UNSIGNED  NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (uuid),
    UNIQUE (name),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id)
);
