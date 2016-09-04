# --- !Ups

-- [ Diff summary ]
-- Dropped objects: 1
-- Created objects: 1
-- Changed objects: 0
-- Truncated tables: 0

SET search_path=public,pg_catalog;
-- ddl-end --


-- [ Dropped objects ] --
ALTER TABLE public.room DROP CONSTRAINT IF EXISTS user_fk CASCADE;
-- ddl-end --
ALTER TABLE public.room DROP COLUMN IF EXISTS id_user CASCADE;
-- ddl-end --


-- [ Created objects ] --
-- object: public.many_user_has_many_room | type: TABLE --
-- DROP TABLE IF EXISTS public.many_user_has_many_room CASCADE;
CREATE TABLE public.many_user_has_many_room(
	id_user integer NOT NULL,
	id_room integer NOT NULL,
	id serial,
	creator bool NOT NULL DEFAULT false,
	CONSTRAINT many_user_has_many_room_pk PRIMARY KEY (id)

);
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.many_user_has_many_room DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.many_user_has_many_room ADD CONSTRAINT user_fk FOREIGN KEY (id_user)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: room_fk | type: CONSTRAINT --
-- ALTER TABLE public.many_user_has_many_room DROP CONSTRAINT IF EXISTS room_fk CASCADE;
ALTER TABLE public.many_user_has_many_room ADD CONSTRAINT room_fk FOREIGN KEY (id_room)
REFERENCES public.room (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

# --- !Downs
