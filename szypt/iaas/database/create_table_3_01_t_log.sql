DROP TABLE T_LOG;
create table T_LOG
(
  PK_T_LOG_ID         number(10) NOT NULL PRIMARY KEY,
  CONTENT    VARCHAR2(255),
  INFO       VARCHAR2(4000),
  IP         VARCHAR2(255),
  LOGINNAME  VARCHAR2(255),
  MODULE     VARCHAR2(255),
  OPERATION  VARCHAR2(255),
  RESULT     VARCHAR2(255),
  TARGETID   VARCHAR2(255),  
  CREATED_BY       VARCHAR2(50)            NOT NULL,
  CREATED_ON       TIMESTAMP                    NOT NULL,
  MODIFIED_BY      VARCHAR2(50)            ,
  MODIFIED_ON      TIMESTAMP                    
);
