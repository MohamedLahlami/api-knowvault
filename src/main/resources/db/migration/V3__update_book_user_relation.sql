ALTER TABLE book DROP COLUMN IF EXISTS utilisateur_id;

ALTER TABLE book
    ADD COLUMN utilisateur_id UUID;

ALTER TABLE book
    ADD CONSTRAINT fk_book_utilisateur
        FOREIGN KEY (utilisateur_id)
            REFERENCES utilisateur(id)
            ON DELETE SET NULL;
