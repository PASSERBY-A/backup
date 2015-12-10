REM Script to create iaas schema owner
REM Note that the actual schema owner is passed in as a parameter
REM to allow for multiple instances on a shared sever

REM $Id: createSchema.sql,v 1.0 2013/12/28 17:15:00 Zhefang Chen $

spool _log_create_schema.log


DROP tablespace TBS_IAAS INCLUDING CONTENTS;


Create tablespace TBS_IAAS 
datafile 'D:\program\oracle\product\oradata\IAAS_data_01.dbf' size 1024M
/* datafile '/oradata/cloudora/IAAS_data_01.dbf' size 1024M */
REUSE
autoextend on  
next 3m maxsize unlimited  
extent management local;  


DROP USER &&1 CASCADE;

CREATE USER &&1 IDENTIFIED BY &&2
 DEFAULT TABLESPACE TBS_IAAS
 TEMPORARY TABLESPACE temp
 QUOTA UNLIMITED ON users;
/*
GRANT create session
    , create table
    , create procedure
    , create sequence
    , create trigger
    , create view
    , create synonym
    , alter session
    , create type
    , create materialized view
    , query rewrite
    , create dimension
    , create any directory
    , alter user
    , resumable
    , ALTER ANY TABLE  -- These
    , DROP ANY TABLE   -- five are
    , LOCK ANY TABLE   -- needed
    , CREATE ANY TABLE -- to use
    , SELECT ANY TABLE -- DBMS_REDEFINITION
TO &&1;

GRANT select_catalog_role
    , execute_catalog_role
*/
GRANT CONNECT, RESOURCE
TO &&1;

spool off
