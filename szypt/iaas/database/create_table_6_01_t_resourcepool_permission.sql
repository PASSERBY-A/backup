DROP TABLE T_resourcepool_permission;
CREATE TABLE T_resourcepool_permission(
   pk_resoucepool_permission_id NUMBER NOT NULL PRIMARY KEY ,
   owner_type INT NOT NULL,
   owner_id VARCHAR2(200) NOT NULL,
   owner_name VARCHAR2(200) NOT NULL,
   productionpool INT NOT NULL,
   testingpool INT NOT NULL,
   created_by VARCHAR2(200) NOT NULL,
   created_on TIMESTAMP  DEFAULT SYSTIMESTAMP NOT NULL,
   modified_by VARCHAR2(200),
   modified_on TIMESTAMP DEFAULT SYSTIMESTAMP
);
