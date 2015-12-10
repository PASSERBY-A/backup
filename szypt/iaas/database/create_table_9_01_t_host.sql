ALTER TABLE T_HOST  DROP PRIMARY KEY CASCADE;

DROP TABLE T_HOST CASCADE CONSTRAINTS;

CREATE TABLE T_HOST 
(
   PK_HOST_ID           NUMBER(10)                     NOT NULL PRIMARY KEY,
   MANUFACTURER         VARCHAR2(200)                  NULL,
   PRODUCTNAME          VARCHAR2(200)                  NULL,
   SERIALNUMBER         VARCHAR2(200)                  NOT NULL,
   SERVERNAME           VARCHAR2(200)                  NULL,
   CPUCOUNT             INT                            NULL,
   CPUCORES             INT                            NULL,
   CPUTYPE              VARCHAR2(200)                  NULL,
   MEMORY               INT                            NULL,
   STATUS               INT                            NULL,
   SAVESTATUS			INT							   NULL,
   TYPE                 INT                            NULL,
   DESCRIPT             VARCHAR2(200)                  NULL,
   NIC                  INT                            NULL,
   BAY_INDEX            INT                            NULL,
   CREATED_BY           VARCHAR(60)                    NOT NULL,
   CREATED_ON           TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
   MODIFIED_BY          VARCHAR(60)                    NULL,
   MODIFIED_ON          TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
   HOSTNAME             VARCHAR2(200)                  NULL,
   RESOURCEPOOLID       VARCHAR2(200)                  NULL
);

COMMENT ON TABLE T_HOST IS 
'主机信息';

COMMENT ON COLUMN T_HOST.MANUFACTURER IS 
'厂商';

COMMENT ON COLUMN T_HOST.SERVERNAME IS 
'服务器名称';

COMMENT ON COLUMN T_HOST.CPUCOUNT IS 
'CPU数量';

COMMENT ON COLUMN T_HOST.CPUCORES IS 
'CPU核数';


comment on column t_host.memory is 
'内存，单位MB';

comment on column t_host.saveStatus is 
'0：未处理
1：己处理';

comment on column t_host.status is 
'状态:
1:初始化
2：归档（己保存到资产）
3：废弃
4：删除

';

comment on column t_host.type is '主机类型：x86机架式服务器--1;x86刀片服务器--2;hp小型机--3';

comment on column t_host.nic is 
'网卡个数';

comment on column t_host.hostname is 
'主机名称或IP';

comment on column t_host.resourcepoolid is 
' 一级资源池ID';

