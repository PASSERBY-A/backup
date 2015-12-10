DROP TABLE T_ResourcePool_Zone_Relations;

CREATE TABLE T_ResourcePool_Zone_Relations(
       ID INT NOT NULL PRIMARY KEY,
       resourcepool_id VARCHAR2(255) NOT NULL,
       zone_id VARCHAR2(255) NOT NULL,
       created_by VARCHAR2(200),
       created_on TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
       modified_by VARCHAR2(200),
       modified_on TIMESTAMP
);
