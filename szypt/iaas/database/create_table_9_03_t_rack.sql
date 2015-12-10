
ALTER TABLE T_RACK  DROP PRIMARY KEY CASCADE;

DROP TABLE T_RACK CASCADE CONSTRAINTS;
CREATE TABLE T_RACK 
(
   T_RACK_ID            NUMBER                         NOT NULL PRIMARY KEY ,
   NAME                 VARCHAR2(200)                  NULL,
   SERIALNUMBER         VARCHAR2(200)                  NOT NULL,
   UUID                 VARCHAR2(200)                  NULL,
   RACKTYPE				VARCHAR2(200)				   NULL,
   BAYCOUNT             INT                            NULL,
   STATUS               INT                            NULL,
   SAVESTATUS			NUMBER						   NULL,
   OAIP                 VARCHAR2(200)                  NULL,
   CREATED_BY           VARCHAR(60)                    NOT NULL,
   CREATED_ON           TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
   MODIFIED_BY          VARCHAR(60)                    NULL,
   MODIFIED_ON          TIMESTAMP DEFAULT SYSTIMESTAMP NULL
);

COMMENT ON TABLE T_RACK IS 
'刀箱';

comment on column t_rack.savestatus is 
' 0：未处理
1：己处理';
