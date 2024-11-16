-- Table: public.book_status_entity

-- DROP TABLE IF EXISTS public.book_status_entity;

CREATE TABLE IF NOT EXISTS public.book_status_entity
(
    is_available boolean NOT NULL,
    book_id bigint NOT NULL,
    borrowed_at timestamp(6) without time zone,
    returned_at timestamp(6) without time zone,
    CONSTRAINT book_status_entity_pkey PRIMARY KEY (book_id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.book_status_entity
    OWNER to postgres;