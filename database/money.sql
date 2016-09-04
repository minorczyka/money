-- Database generated with pgModeler (PostgreSQL Database Modeler).
-- pgModeler  version: 0.8.1
-- PostgreSQL version: 9.4
-- Project Site: pgmodeler.com.br
-- Model Author: ---


-- Database creation must be done outside an multicommand file.
-- These commands were put in this file only for convenience.
-- -- object: money | type: DATABASE --
-- -- DROP DATABASE IF EXISTS money;
-- CREATE DATABASE money
-- ;
-- -- ddl-end --
-- 

-- object: public."user" | type: TABLE --
-- DROP TABLE IF EXISTS public."user" CASCADE;
CREATE TABLE public."user"(
	id integer NOT NULL,
	username text NOT NULL,
	password text NOT NULL,
	email text NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public."user" OWNER TO postgres;
-- ddl-end --

-- object: public.room | type: TABLE --
-- DROP TABLE IF EXISTS public.room CASCADE;
CREATE TABLE public.room(
	id integer NOT NULL,
	name text NOT NULL,
	created date NOT NULL,
	id_user integer NOT NULL,
	CONSTRAINT room_pk PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.room OWNER TO postgres;
-- ddl-end --

-- object: user_fk | type: CONSTRAINT --
-- ALTER TABLE public.room DROP CONSTRAINT IF EXISTS user_fk CASCADE;
ALTER TABLE public.room ADD CONSTRAINT user_fk FOREIGN KEY (id_user)
REFERENCES public."user" (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.person | type: TABLE --
-- DROP TABLE IF EXISTS public.person CASCADE;
CREATE TABLE public.person(
	id integer NOT NULL,
	name text NOT NULL,
	id_room integer NOT NULL,
	CONSTRAINT person_pk PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.person OWNER TO postgres;
-- ddl-end --

-- object: room_fk | type: CONSTRAINT --
-- ALTER TABLE public.person DROP CONSTRAINT IF EXISTS room_fk CASCADE;
ALTER TABLE public.person ADD CONSTRAINT room_fk FOREIGN KEY (id_room)
REFERENCES public.room (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --

-- object: public.bill | type: TABLE --
-- DROP TABLE IF EXISTS public.bill CASCADE;
CREATE TABLE public.bill(
	id integer NOT NULL,
	amount money NOT NULL,
	date date,
	id_person integer NOT NULL,
	CONSTRAINT bill_pk PRIMARY KEY (id)

);
-- ddl-end --
ALTER TABLE public.bill OWNER TO postgres;
-- ddl-end --

-- object: public.many_person_has_many_bill | type: TABLE --
-- DROP TABLE IF EXISTS public.many_person_has_many_bill CASCADE;
CREATE TABLE public.many_person_has_many_bill(
	id_person integer,
	id_bill integer,
	CONSTRAINT many_person_has_many_bill_pk PRIMARY KEY (id_person,id_bill)

);
-- ddl-end --

-- object: person_fk | type: CONSTRAINT --
-- ALTER TABLE public.many_person_has_many_bill DROP CONSTRAINT IF EXISTS person_fk CASCADE;
ALTER TABLE public.many_person_has_many_bill ADD CONSTRAINT person_fk FOREIGN KEY (id_person)
REFERENCES public.person (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: bill_fk | type: CONSTRAINT --
-- ALTER TABLE public.many_person_has_many_bill DROP CONSTRAINT IF EXISTS bill_fk CASCADE;
ALTER TABLE public.many_person_has_many_bill ADD CONSTRAINT bill_fk FOREIGN KEY (id_bill)
REFERENCES public.bill (id) MATCH FULL
ON DELETE CASCADE ON UPDATE CASCADE;
-- ddl-end --

-- object: person_fk | type: CONSTRAINT --
-- ALTER TABLE public.bill DROP CONSTRAINT IF EXISTS person_fk CASCADE;
ALTER TABLE public.bill ADD CONSTRAINT person_fk FOREIGN KEY (id_person)
REFERENCES public.person (id) MATCH FULL
ON DELETE RESTRICT ON UPDATE CASCADE;
-- ddl-end --


