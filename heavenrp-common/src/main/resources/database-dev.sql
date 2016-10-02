
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
