# --- !Ups

-- [ Diff summary ]
-- Dropped objects: 1
-- Created objects: 4
-- Changed objects: 4
-- Truncated tables: 0

SET search_path=public,pg_catalog;
-- ddl-end --

-- [ Created objects ] --
-- object: public.user_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.user_id_seq CASCADE;
CREATE SEQUENCE public.user_id_seq
	INCREMENT BY 1
	MINVALUE -2147483648
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.user_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.room_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.room_id_seq CASCADE;
CREATE SEQUENCE public.room_id_seq
	INCREMENT BY 1
	MINVALUE -2147483648
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.room_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.person_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.person_id_seq CASCADE;
CREATE SEQUENCE public.person_id_seq
	INCREMENT BY 1
	MINVALUE -2147483648
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.person_id_seq OWNER TO postgres;
-- ddl-end --

-- object: public.bill_id_seq | type: SEQUENCE --
-- DROP SEQUENCE IF EXISTS public.bill_id_seq CASCADE;
CREATE SEQUENCE public.bill_id_seq
	INCREMENT BY 1
	MINVALUE -2147483648
	MAXVALUE 2147483647
	START WITH 1
	CACHE 1
	NO CYCLE
	OWNED BY NONE;
-- ddl-end --
ALTER SEQUENCE public.bill_id_seq OWNER TO postgres;
-- ddl-end --



-- [ Changed objects ] --
ALTER TABLE public."user" ALTER COLUMN id TYPE integer;
-- ddl-end --
ALTER TABLE public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);
-- ddl-end --
ALTER TABLE public.room ALTER COLUMN id TYPE integer;
-- ddl-end --
ALTER TABLE public.room ALTER COLUMN id SET DEFAULT nextval('public.room_id_seq'::regclass);
-- ddl-end --
ALTER TABLE public.person ALTER COLUMN id TYPE integer;
-- ddl-end --
ALTER TABLE public.person ALTER COLUMN id SET DEFAULT nextval('public.person_id_seq'::regclass);
-- ddl-end --
ALTER TABLE public.bill ALTER COLUMN id TYPE integer;
-- ddl-end --
ALTER TABLE public.bill ALTER COLUMN id SET DEFAULT nextval('public.bill_id_seq'::regclass);
-- ddl-end --

# --- !Downs