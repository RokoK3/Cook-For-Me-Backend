ALTER TABLE IF EXISTS user_data
    RENAME COLUMN verificationCode TO verification_code;

ALTER TABLE IF EXISTS cook
    RENAME COLUMN verificationCode TO verification_code;