DROP TABLE   T_ResourcePool;
CREATE TABLE T_ResourcePool(
       PK_T_ResourcePool_ID INT NOT NULL PRIMARY KEY,
       resourcepool_id VARCHAR2(200) NOT NULL,
       NAME VARCHAR2(200) NOT NULL,
       descript VARCHAR2(200) ,
       created_by VARCHAR2(200),
       created_on TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
       modified_by VARCHAR2(200),
       modified_on TIMESTAMP DEFAULT SYSTIMESTAMP
);