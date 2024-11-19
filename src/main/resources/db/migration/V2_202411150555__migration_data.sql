start transaction;

INSERT INTO member_role (role, user_id)
SELECT role, user_id
FROM user
WHERE role IN ('SENIOR', 'ADMIN');

INSERT INTO member_role (role, user_id)
SELECT 'USER', user_user_id
FROM wish;

ALTER TABLE wish DROP FOREIGN KEY FKpr2vr0ubj2t1ghyx6cckcamul;
ALTER TABLE wish DROP COLUMN user_user_id;

commit;