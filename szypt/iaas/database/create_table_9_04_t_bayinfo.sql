
ALTER TABLE T_BAYINFO  DROP PRIMARY KEY CASCADE;

DROP TABLE T_BAYINFO CASCADE CONSTRAINTS;

CREATE TABLE T_BAYINFO 
(
   PK_BAYINFO_ID        NUMBER                         NOT NULL PRIMARY KEY,
   FK_RACK_ID           NUMBER						   NOT NULL,
   BAYINDEX             INT                            NULL,
   ILONAME              VARCHAR2(200)                  NULL,
   ILOIP                VARCHAR2(200)                  NULL,
   SERIALNUM            VARCHAR2(200)                  NULL,
   SERVERNAME           VARCHAR2(200)                  NULL,
   HOST_STATUS          VARCHAR2(200)                  NULL,
   STATUS               VARCHAR2(200)                  NULL,
   POWERFLAG            VARCHAR2(200)                  NULL,
   CREATED_BY           VARCHAR(60)                    NOT NULL,
   CREATED_ON           TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
   MODIFIED_BY          VARCHAR(60)                    NULL,
   MODIFIED_ON          TIMESTAMP DEFAULT SYSTIMESTAMP NULL
);

COMMENT ON TABLE T_BAYINFO IS 
'²ÛÐÅÏ¢';

COMMENT ON COLUMN T_BAYINFO.HOST_STATUS IS 
'µ¶Æ¬×´Ì¬';

COMMENT ON COLUMN T_BAYINFO.STATUS IS 
'¿¨²Û×´Ì¬';
