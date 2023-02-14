CREATE TABLE payment_tab
(
    id      BIGINT NOT NULL,
    amount  REAL NOT NULL,
    date    DATE NOT NULL,
    ord_number VARCHAR(40),
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_payment_tab PRIMARY KEY (id)
);
--
-- ALTER TABLE payment_tab
--     ADD CONSTRAINT FK_PAYMENT_TAB_ON_USER FOREIGN KEY (user_id) REFERENCES user_tab (id);