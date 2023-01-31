ALTER TABLE records
    ADD FOREIGN KEY (user_id) REFERENCES users (id);
ALTER TABLE records
    ADD FOREIGN KEY (category_id) REFERENCES categories (id);
ALTER TABLE categories
    ADD FOREIGN KEY (user_id) REFERENCES users (id);