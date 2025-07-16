
CREATE TABLE utilisateur (
                             id UUID PRIMARY KEY,
                             login VARCHAR(255) NOT NULL UNIQUE,
                             first_name VARCHAR(255),
                             last_name VARCHAR(255),
                             id_user_type VARCHAR(255),
                             created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMPTZ
);

CREATE TABLE shelf (
                       id BIGSERIAL PRIMARY KEY,
                       label VARCHAR(255),
                       description TEXT,
                       tag VARCHAR(255)
);

CREATE TABLE book (
                      id BIGSERIAL PRIMARY KEY,
                      book_title VARCHAR(255) NOT NULL,
                      utilisateur_id UUID,
                      shelf_id BIGINT,
                      CONSTRAINT fk_book_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE SET NULL,
                      CONSTRAINT fk_book_shelf FOREIGN KEY (shelf_id) REFERENCES shelf(id) ON DELETE SET NULL
);

CREATE TABLE chapter (
                         id BIGSERIAL PRIMARY KEY,
                         chapter_title VARCHAR(255) NOT NULL,
                         book_id BIGINT,
                         CONSTRAINT fk_chapter_book FOREIGN KEY (book_id) REFERENCES book(id) ON DELETE CASCADE
);

CREATE TABLE page (
                      id BIGSERIAL PRIMARY KEY,
                      page_number INT,
                      content TEXT,
                      markdown_content TEXT,
                      status VARCHAR(255),
                      chapter_id BIGINT,
                      CONSTRAINT fk_page_chapter FOREIGN KEY (chapter_id) REFERENCES chapter(id) ON DELETE CASCADE
);

CREATE TABLE favorite (
                          id BIGSERIAL PRIMARY KEY,
                          utilisateur_id UUID,
                          page_id BIGINT,
                          CONSTRAINT fk_favorite_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
                          CONSTRAINT fk_favorite_page FOREIGN KEY (page_id) REFERENCES page(id) ON DELETE CASCADE
);

CREATE TABLE comment (
                         id BIGSERIAL PRIMARY KEY,
                         text TEXT,
                         utilisateur_id UUID,
                         page_id BIGINT,
                         CONSTRAINT fk_comment_utilisateur FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id) ON DELETE CASCADE,
                         CONSTRAINT fk_comment_page FOREIGN KEY (page_id) REFERENCES page(id) ON DELETE CASCADE
);

CREATE TABLE history (
                         id BIGSERIAL PRIMARY KEY,
                         modification_date TIMESTAMPTZ NOT NULL,
                         modification_type VARCHAR(255),
                         page_id BIGINT,
                         CONSTRAINT fk_history_page FOREIGN KEY (page_id) REFERENCES page(id) ON DELETE CASCADE
);
