CREATE TABLE IF NOT EXISTS categories
(
    id              INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(100),
    custom          BOOL,
    user_id         INT(11)
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS users
(
    id              INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username        VARCHAR(100),
    first_name      VARCHAR(100),
    last_name       VARCHAR(100),
    email           VARCHAR(100),
    password        VARCHAR(100),
    phone           VARCHAR(20),
    daily_limit     DOUBLE,
    weekly_limit    DOUBLE,
    monthly_limit   DOUBLE,
    yearly_limit    DOUBLE
)ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS records
(
    id              INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(100),
    income_or_outcome  BOOL,
    user_id         INT(11),
    category_id     INT(11),
    amount          DOUBLE,
    description     VARCHAR(255),
    record_date     DATE
)ENGINE = InnoDB;