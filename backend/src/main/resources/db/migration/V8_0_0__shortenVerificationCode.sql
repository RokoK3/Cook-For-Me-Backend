ALTER TABLE IF EXISTS user_data
    ALTER COLUMN verificationCode TYPE VARCHAR(36),
    ALTER COLUMN verificationCode DROP DEFAULT;

ALTER TABLE IF EXISTS cook
    ALTER COLUMN verificationCode TYPE VARCHAR(36),
    ALTER COLUMN verificationCode DROP DEFAULT;