CREATE TABLE tag (
                     id BIGSERIAL PRIMARY KEY,
                     label VARCHAR(255),
                     type VARCHAR(50),
                     resource_id BIGINT
);
