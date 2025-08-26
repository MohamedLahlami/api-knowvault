ALTER TABLE comment
    DROP CONSTRAINT fk_comment_utilisateur;

ALTER TABLE comment
    ADD created_at TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE comment
    ADD utilisateur_login VARCHAR(255);

ALTER TABLE comment
    DROP COLUMN utilisateur_id;

ALTER TABLE book
    ALTER COLUMN book_title DROP NOT NULL;

ALTER TABLE chapter
    ALTER COLUMN chapter_title DROP NOT NULL;

ALTER TABLE page
    ALTER COLUMN content TYPE VARCHAR(255) USING (content::VARCHAR(255));

ALTER TABLE shelf
    ALTER COLUMN created_at DROP NOT NULL;

ALTER TABLE book
    ALTER COLUMN description TYPE VARCHAR(255) USING (description::VARCHAR(255));

ALTER TABLE shelf
    ALTER COLUMN description TYPE VARCHAR(255) USING (description::VARCHAR(255));

ALTER TABLE utilisateur
    ALTER COLUMN login DROP NOT NULL;

ALTER TABLE page
    ALTER COLUMN markdown_content TYPE VARCHAR(255) USING (markdown_content::VARCHAR(255));

ALTER TABLE history
    ALTER COLUMN modification_date DROP NOT NULL;

ALTER TABLE history
    DROP COLUMN modification_type;

ALTER TABLE history
    ADD modification_type SMALLINT;

ALTER TABLE page
    ALTER COLUMN page_number SET NOT NULL;

ALTER TABLE comment
    ALTER COLUMN text TYPE VARCHAR(255) USING (text::VARCHAR(255));

ALTER TABLE tag
    ALTER COLUMN type TYPE VARCHAR(255) USING (type::VARCHAR(255));

ALTER TABLE shelf
    ALTER COLUMN updated_at DROP NOT NULL;