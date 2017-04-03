-- Table: public.course

-- DROP TABLE public.course;

CREATE TABLE public.course
(
    id bigint NOT NULL DEFAULT nextval('course_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    description character varying(10000) COLLATE pg_catalog."default" NOT NULL,
    offeredby bigint NOT NULL,
    cid character varying(10000) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT course_pkey PRIMARY KEY (id),
    CONSTRAINT course_offeredby_fkey FOREIGN KEY (offeredby)
        REFERENCES public.university (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.course
    OWNER to postgres;
	
-- ---------------------------------------------------------------------------------------------

-- Table: public.followup

-- DROP TABLE public.followup;

CREATE TABLE public.followup
(
    id bigint NOT NULL DEFAULT nextval('followup_id_seq'::regclass),
    comments character varying(100000) COLLATE pg_catalog."default" NOT NULL,
    partof bigint NOT NULL,
    askedby integer NOT NULL,
    CONSTRAINT followup_pkey PRIMARY KEY (id),
    CONSTRAINT followup_partof_fkey FOREIGN KEY (partof)
        REFERENCES public.forum (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT person_fkey FOREIGN KEY (askedby)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.followup
    OWNER to postgres;
	
-- ---------------------------------------------------------------------------------------------

-- Table: public.forum

-- DROP TABLE public.forum;

CREATE TABLE public.forum
(
    id bigint NOT NULL DEFAULT nextval('forum_id_seq'::regclass),
    post character varying(100000) COLLATE pg_catalog."default" NOT NULL,
    askedby bigint NOT NULL,
    partof bigint NOT NULL,
    CONSTRAINT forum_pkey PRIMARY KEY (id),
    CONSTRAINT forum_askedby_fkey FOREIGN KEY (askedby)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT forum_partof_fkey FOREIGN KEY (partof)
        REFERENCES public.course (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.forum
    OWNER to postgres;
	
-- --------------------------------------------------------------------------------------------

-- Table: public.lecture

-- DROP TABLE public.lecture;

CREATE TABLE public.lecture
(
    id bigint NOT NULL,
    partof bigint NOT NULL,
    topic character varying(100) COLLATE pg_catalog."default" NOT NULL,
    filename character varying(100) COLLATE pg_catalog."default" NOT NULL,
    type filetype NOT NULL,
    lecturetype "lectureType" NOT NULL,
    topicdescription character varying(10000) COLLATE pg_catalog."default",
    CONSTRAINT lecture_pkey PRIMARY KEY (id),
    CONSTRAINT lecture_partof_fkey FOREIGN KEY (partof)
        REFERENCES public.course (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.lecture
    OWNER to postgres;
	
-- -------------------------------------------------------------------------------------------

-- Table: public.login

-- DROP TABLE public.login;

CREATE TABLE public.login
(
    id bigint NOT NULL,
    username character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    type character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT login_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.login
    OWNER to postgres;

-- ---------------------------------------------------------------------------------------------

-- Table: public.person

-- DROP TABLE public.person;

CREATE TABLE public.person
(
    id bigint NOT NULL DEFAULT nextval('person_id_seq'::regclass),
    name name NOT NULL,
    email character varying(100) COLLATE pg_catalog."default" NOT NULL,
    address address NOT NULL,
    CONSTRAINT person_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.person
    OWNER to postgres;
	
-- ----------------------------------------------------------------------------------------------

-- Table: public.professor

-- DROP TABLE public.professor;

CREATE TABLE public.professor
(
    id bigint NOT NULL,
    worksfor bigint NOT NULL,
    designation designation NOT NULL,
    CONSTRAINT professor_pkey PRIMARY KEY (id, worksfor),
    CONSTRAINT professor_id_fkey FOREIGN KEY (id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT professor_worksfor_fkey FOREIGN KEY (worksfor)
        REFERENCES public.university (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.professor
    OWNER to postgres;
	
-- ----------------------------------------------------------------------------------------------

-- Table: public.professorcourse

-- DROP TABLE public.professorcourse;

CREATE TABLE public.professorcourse
(
    teaches bigint NOT NULL,
    taughtby bigint NOT NULL,
    CONSTRAINT professorcourse_pkey PRIMARY KEY (teaches, taughtby),
    CONSTRAINT professorcourse_teaches_fkey FOREIGN KEY (teaches)
        REFERENCES public.course (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.professorcourse
    OWNER to postgres;
	
-- ------------------------------------------------------------------------------------------

-- Table: public.professorforum

-- DROP TABLE public.professorforum;

CREATE TABLE public.professorforum
(
    answers bigint NOT NULL,
    answeredby bigint NOT NULL,
    CONSTRAINT professorforum_pkey PRIMARY KEY (answers, answeredby),
    CONSTRAINT professorforum_answers_fkey FOREIGN KEY (answers)
        REFERENCES public.forum (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.professorforum
    OWNER to postgres;
	
-- -----------------------------------------------------------------------------------------

-- Table: public.student

-- DROP TABLE public.student;

CREATE TABLE public.student
(
    id bigint NOT NULL DEFAULT nextval('student_id_seq'::regclass),
    CONSTRAINT student_pkey PRIMARY KEY (id),
    CONSTRAINT student_id_fkey FOREIGN KEY (id)
        REFERENCES public.person (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.student
    OWNER to postgres;
	
-- -----------------------------------------------------------------------------------------

-- Table: public.studentcourse

-- DROP TABLE public.studentcourse;

CREATE TABLE public.studentcourse
(
    takes bigint NOT NULL,
    takenby bigint NOT NULL,
    status character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT studentcourse_pkey PRIMARY KEY (takes, takenby),
    CONSTRAINT studentcourse_takenby_fkey FOREIGN KEY (takenby)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT studentcourse_takes_fkey FOREIGN KEY (takes)
        REFERENCES public.course (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.studentcourse
    OWNER to postgres;
	
-- ----------------------------------------------------------------------------------------

-- Table: public.studentlecture

-- DROP TABLE public.studentlecture;

CREATE TABLE public.studentlecture
(
    views bigint NOT NULL,
    viewedby bigint NOT NULL,
    CONSTRAINT studentlecture_pkey PRIMARY KEY (views, viewedby),
    CONSTRAINT fk_viewedby FOREIGN KEY (viewedby)
        REFERENCES public.student (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk_views FOREIGN KEY (views)
        REFERENCES public.lecture (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.studentlecture
    OWNER to postgres;
	
-- ----------------------------------------------------------------------------------------

-- Table: public.university

-- DROP TABLE public.university;

CREATE TABLE public.university
(
    id bigint NOT NULL DEFAULT nextval('university_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    adrress address NOT NULL,
    CONSTRAINT university_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.university
    OWNER to postgres;