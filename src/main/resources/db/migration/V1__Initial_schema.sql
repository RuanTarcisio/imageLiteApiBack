-- Create users table
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255),
                       email VARCHAR(255) UNIQUE,
                       password VARCHAR(255),
                       cpf VARCHAR(255) UNIQUE,
                       birthdate DATE,
                       cod_token VARCHAR(255),
                       created_at TIMESTAMP(6),
                       enabled BOOLEAN NOT NULL,
                       fully_registered BOOLEAN NOT NULL,
                       profile_image_url VARCHAR(255)
);

-- Create user_connected_account table
CREATE TABLE user_connected_account (
                                        id BIGSERIAL PRIMARY KEY,
                                        user_id BIGINT,
                                        provider VARCHAR(255),
                                        provider_id VARCHAR(255),
                                        connected_at TIMESTAMP(6),
                                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create images table (generic images)
CREATE TABLE images (
                        id VARCHAR(255) PRIMARY KEY,
                        name VARCHAR(255),
                        extension VARCHAR(255),
                        size BIGINT,
                        tags VARCHAR(255),
                        upload_date TIMESTAMP(6),
                        file OID,
                        CONSTRAINT images_extension_check CHECK (extension IN ('PNG', 'GIF', 'JPEG'))
);

-- Create user_images table (user-specific images)
CREATE TABLE user_images (
                             id VARCHAR(255) PRIMARY KEY,
                             user_id BIGINT NOT NULL UNIQUE,
                             name VARCHAR(255),
                             extension VARCHAR(255),
                             size BIGINT,
                             tags VARCHAR(255),
                             upload_date TIMESTAMP(6),
                             file OID,
                             CONSTRAINT user_images_extension_check CHECK (extension IN ('PNG', 'GIF', 'JPEG')),
                             FOREIGN KEY (user_id) REFERENCES users(id)
);