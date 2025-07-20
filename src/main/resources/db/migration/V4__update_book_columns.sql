ALTER TABLE book DROP COLUMN utilisateur_id;
ALTER TABLE book ADD COLUMN utilisateur_login VARCHAR(255);
