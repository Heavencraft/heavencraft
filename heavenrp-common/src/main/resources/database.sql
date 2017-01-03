--
-- Bank accounts
--

CREATE TABLE bank_accounts (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    balance         MEDIUMINT UNSIGNED  NOT NULL DEFAULT 0,
    last_update     TIMESTAMP           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO bank_accounts (id, balance) VALUES ('1', '1000');


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
    ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id) ON DELETE SET NULL;


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
    ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;


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
    ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id) ON DELETE SET NULL;

CREATE TABLE towns_users (
    town_id         MEDIUMINT UNSIGNED  NOT NULL,
    user_id         MEDIUMINT UNSIGNED  NOT NULL,

    PRIMARY KEY (town_id, user_id)
);

ALTER TABLE towns_users
    ADD FOREIGN KEY (town_id) REFERENCES towns (id) ON DELETE CASCADE;

ALTER TABLE towns_users
    ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;


--
-- Companies
--

CREATE TABLE companies (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    tag             VARCHAR(14)         NOT NULL,
    bank_account_id MEDIUMINT UNSIGNED  NULL,

    PRIMARY KEY (id),
    UNIQUE (name),
    UNIQUE (tag)
);

ALTER TABLE companies
    ADD FOREIGN KEY (bank_account_id) REFERENCES bank_accounts (id) ON DELETE SET NULL;

CREATE TABLE companies_users (
    company_id      MEDIUMINT UNSIGNED  NOT NULL,
    user_id         MEDIUMINT UNSIGNED  NOT NULL,
    employer        BOOLEAN             NOT NULL,

    PRIMARY KEY (company_id, user_id)
);

ALTER TABLE companies_users
    ADD FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE;

ALTER TABLE companies_users
    ADD FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;


INSERT INTO companies (name, tag, bank_account_id) VALUES ('Heavencraft', 'Heavencraft', 1);



--
-- Warps
--
CREATE TABLE warps (
    id              MEDIUMINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    creator         MEDIUMINT UNSIGNED  NULL,
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
    ADD FOREIGN KEY (creator) REFERENCES users (id) ON DELETE SET NULL;

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
    name            VARCHAR(14)         NOT NULL,
    world           VARCHAR(16)         NOT NULL,
    sign_x          INT                 NOT NULL,
    sign_y          TINYINT UNSIGNED    NOT NULL,
    sign_z          INT                 NOT NULL,
    chest_x         INT                 NOT NULL,
    chest_y         TINYINT UNSIGNED    NOT NULL,
    chest_z         INT                 NOT NULL,

    PRIMARY KEY (id),
    UNIQUE (name, company_id),
    UNIQUE (world, sign_x, sign_y, sign_z),
    UNIQUE (world, chest_x, chest_y, chest_z)
);

ALTER TABLE stocks
    ADD FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE;

--
-- Stores
--

CREATE TABLE stores (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,

    stock_id        INT UNSIGNED        NULL,
    company_id      MEDIUMINT UNSIGNED  NOT NULL,
    stock_name      VARCHAR(14)         NOT NULL,

    quantity        SMALLINT UNSIGNED   NOT NULL,
    price           MEDIUMINT UNSIGNED  NOT NULL,
    is_buyer        BOOLEAN             NOT NULL,

    world           VARCHAR(16)         NOT NULL,
    x               INT                 NOT NULL,
    y               TINYINT             NOT NULL,
    z               INT                 NOT NULL,
    
    PRIMARY KEY (id),
    UNIQUE (world, x, y, z)
);

ALTER TABLE stores
    ADD FOREIGN KEY (company_id) REFERENCES companies (id) ON DELETE CASCADE;
    
ALTER TABLE stores
    ADD FOREIGN KEY (stock_id) REFERENCES stocks (id) ON DELETE SET NULL;

--
-- NPC messages
--

CREATE TABLE npc_actions (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    npc_tag         VARCHAR(16)         NOT NULL,
    conditions      TEXT                NOT NULL,
    messages        TEXT                NOT NULL,
    commands        TEXT                NOT NULL,

    PRIMARY KEY (id)
);

--
-- server quests
--

CREATE TABLE server_quests (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    name            VARCHAR(32)         NOT NULL,
    first_step      INT UNSIGNED        NOT NULL,
    current_step    INT UNSIGNED        NOT NULL,
    completed       BOOLEAN             DEFAULT FALSE,
    completed_goals TEXT                DEFAULT NULL,

    PRIMARY KEY (id)
);

ALTER TABLE server_quests
    ADD FOREIGN KEY (current_step) REFERENCES server_quest_steps (id);

CREATE TABLE server_quest_steps (
    id              INT UNSIGNED        NOT NULL AUTO_INCREMENT,
    next_step       INT UNSIGNED        DEFAULT NULL,
    schematic       MEDIUMBLOB          DEFAULT NULL,
    goals           TEXT                NOT NULL,

    PRIMARY KEY (id)
);

ALTER TABLE server_quest_steps
    ADD FOREIGN KEY (next_step) REFERENCES server_quest_steps (id);

INSERT INTO server_quest_steps (id, next_step, goals) VALUES (2, NULL, "");
INSERT INTO server_quest_steps (id, next_step, goals) VALUES (1, 2, "GIVE_ITEM 4 COBBLESTONE");
INSERT INTO server_quests (id, name, first_step, current_step) VALUES (1, "Quête de test", 1, 1);