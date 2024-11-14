CREATE TABLE member_role (
                            role_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            role ENUM('USER', 'SENIOR', 'ADMIN') NOT NULL,
                            user_id BIGINT NOT NULL,
                            FOREIGN KEY (user_id) REFERENCES user(user_id)
);