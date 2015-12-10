REM Main create script£¬database schema, object and init data 
REM To be run from SQL*Plus conected as a DBA

REM Schema Owner iaas is parameterized to allow for simpler workshop / 
REM classroom setup where only one shared server is available
REM $Id: build.sql,v 1.0 2013/12/28 17:15:00 Zhefang Chen $

SET FEEDBACK 1
SET NUMWIDTH 10
SET LINESIZE 132
SET TRIMSPOOL ON
SET TAB OFF
SET PAGESIZE 999
SET ECHO OFF

REM Define the changable data
DEFINE DB_SCHEMA = iaas2dba
DEFINE DB_SCHEMA_PASSWORD = iaas2dba
DEFINE DB_LINSTER = orcl

REM create Schema owner

@createSchema &&DB_SCHEMA &&DB_SCHEMA_PASSWORD

REM  use user contect and invoke create schema object script¡£

CONNECT &&DB_SCHEMA/&&DB_SCHEMA_PASSWORD@&&DB_LINSTER

@createSchemaObjects

CONNECT &&DB_SCHEMA/&&DB_SCHEMA_PASSWORD@&&DB_LINSTER

@populateSchemaTables

commit;


Prompt What OBJECTS were created?
column object_name format a30
column object_type format a30

select 	object_type,object_name 
from 	user_objects 
order by 1,2;

Prompt Are there any INVALID OBJECTS?

select 	object_type, object_name 
from 	user_objects 
where 	status='INVALID';
