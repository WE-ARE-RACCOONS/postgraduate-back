start transaction;

ALTER TABLE wish
    ADD COLUMN postgradu VARCHAR(255),
ADD COLUMN professor VARCHAR(255),
ADD COLUMN lab VARCHAR(255),
ADD COLUMN phone_number VARCHAR(20) NOT NULL;

ALTER TABLE wish
DROP COLUMN major;

ALTER TABLE wish
DROP COLUMN matching_receive;

ALTER TABLE wish ALTER status SET DEFAULT 'WAITING';

commit;
