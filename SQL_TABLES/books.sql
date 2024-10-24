-- Table: public.books

-- DROP TABLE IF EXISTS public.books;

CREATE TABLE IF NOT EXISTS public.books
(
    id bigint NOT NULL,
    author character varying(255) COLLATE pg_catalog."default",
    description character varying(255) COLLATE pg_catalog."default",
    genre character varying(255) COLLATE pg_catalog."default",
    isbn character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT books_pkey PRIMARY KEY (id),
    CONSTRAINT books_isbn_key UNIQUE (isbn),
    CONSTRAINT ukkibbepcitr0a3cpk3rfr7nihn UNIQUE (isbn)
    )

    TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.books
    OWNER to postgres;