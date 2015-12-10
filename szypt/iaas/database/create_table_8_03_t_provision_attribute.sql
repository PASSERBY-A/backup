ALTER TABLE T_PROVISION_ATTRIBUTE  DROP PRIMARY KEY CASCADE;
DROP TABLE T_PROVISION_ATTRIBUTE CASCADE CONSTRAINTS;
CREATE TABLE T_PROVISION_ATTRIBUTE(
       PK_PROVISION_ATTRIBUTE_ID                  NUMBER                     NOT NULL,
       WORKORDER_TYPE                             INT                        NOT NULL,
       ATTRIBUTE_NAME                             VARCHAR2(200)              NOT NULL,
       TYPE                                       VARCHAR2(200)              NOT NULL,
       ALIGNTO                                    VARCHAR2(200)              NULL,
	   REQUIRED									  NUMBER(1)					 DEFAULT 1 NOT NULL,
       VISIBLE                                    NUMBER(1)                  NOT NULL,
       EXTERNALVALUE                              NUMBER(1)                  NOT NULL,
       EDITABLE                                   NUMBER(1)                  NOT NULL,
       SEQUENCE                                   NUMBER(2)                  NOT NULL,
       DESCRIPT1                                  VARCHAR2(200)              NULL,
       DESCRIPT2                                  VARCHAR2(500)              NULL,
       CREATED_BY                                 VARCHAR2(200)              NOT NULL,
       CREATED_ON                                 TIMESTAMP(6)               DEFAULT SYSTIMESTAMP NOT NULL,
       MODIFIED_BY                                VARCHAR2(200)              NULL,
       MODIFIED_ON                                TIMESTAMP(6)               DEFAULT SYSTIMESTAMP
);                                                 
ALTER TABLE T_PROVISION_ATTRIBUTE  ADD CONSTRAINT T_PROVISION_ATTR_PK PRIMARY KEY
(
PK_PROVISION_ATTRIBUTE_ID
)
 ENABLE
;
