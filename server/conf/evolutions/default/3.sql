# --- !Ups

-- [ Diff summary ]
-- Dropped objects: 3
-- Created objects: 1
-- Changed objects: 0
-- Truncated tables: 0

SET search_path=public,pg_catalog;
-- ddl-end --


-- [ Dropped objects ] --
ALTER TABLE public.many_user_has_many_room DROP CONSTRAINT IF EXISTS many_user_has_many_room_pk CASCADE;
-- ddl-end --
DROP SEQUENCE IF EXISTS public.many_user_has_many_room_id_seq CASCADE;
-- ddl-end --
ALTER TABLE public.many_user_has_many_room DROP COLUMN IF EXISTS id CASCADE;
-- ddl-end --


-- [ Created objects ] --
-- object: many_user_has_many_room_pk | type: CONSTRAINT --
-- ALTER TABLE public.many_user_has_many_room DROP CONSTRAINT IF EXISTS many_user_has_many_room_pk CASCADE;
ALTER TABLE public.many_user_has_many_room ADD CONSTRAINT many_user_has_many_room_pk PRIMARY KEY (id_user,id_room);
-- ddl-end --

# --- !Downs
