ALTER TABLE wish
    ADD COLUMN created_at datetime(6) not null DEFAULT CURRENT_TIMESTAMP(6),
    ADD COLUMN updated_at datetime(6);