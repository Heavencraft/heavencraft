
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
