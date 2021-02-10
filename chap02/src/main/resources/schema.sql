-- 删除所有表
drop schema public cascade;
create schema public;

GRANT
ALL
ON SCHEMA public TO postgres;
GRANT ALL
ON SCHEMA public TO public;

--
drop table if exists t_job;
create table t_job
(
    id    serial primary key,
    name      varchar(64) not null,
    lastUsed timestamp with time zone default current_timestamp
);

ALTER SEQUENCE t_job_id_seq RESTART WITH 100000;

insert into t_job(name)
values ('attempt to clean your room'),
       ('please clean your room');