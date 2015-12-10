ALTER TABLE T_WORKITEM
 DROP PRIMARY KEY CASCADE;
DROP TABLE T_WORKITEM CASCADE CONSTRAINTS;
CREATE TABLE T_WORKITEM(
       PK_WORKITEM_ID                     NUMBER            NOT NULL,
       FK_WORKORDER_ID                    NUMBER            NOT NULL,
       STEP                               INT               NOT NULL,
       ATTRIBUTE_NAME                     VARCHAR2(200)     NOT NULL,
       ATTRIBUTE_VALUE                    VARCHAR2(200)     NULL,
       DESCRIPT                           VARCHAR2(200)     NULL,
       CREATED_BY                         VARCHAR2(200)     NOT NULL,
       CREATED_ON                         TIMESTAMP(6)      DEFAULT SYSTIMESTAMP NOT NULL,
       MODIFIED_BY                        VARCHAR2(200)     NULL,
       MODIFIED_ON                        TIMESTAMP(6)      DEFAULT SYSTIMESTAMP
);
ALTER TABLE T_WORKITEM  ADD CONSTRAINT T_WORKITEM_PK PRIMARY KEY
(
PK_WORKITEM_ID
)
 ENABLE
;
