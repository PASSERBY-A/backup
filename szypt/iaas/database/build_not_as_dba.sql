REM Main create script£¬object and init data 

REM Schema Owner iaas is parameterized to allow for simpler workshop / 
REM classroom setup where only one shared server is available
REM $Id: build_no_using_dba.sql,v 1.0 2014/01/20 01:25:00 Zhefang Chen $

SET FEEDBACK 1
SET NUMWIDTH 10
SET LINESIZE 132
SET TRIMSPOOL ON
SET TAB OFF
SET PAGESIZE 999
SET ECHO OFF

REM Define the changable data
DEFINE DB_SCHEMA = iaasdba
DEFINE DB_SCHEMA_PASSWORD = iaasdba
DEFINE DB_LINSTER = iaas_dev

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
