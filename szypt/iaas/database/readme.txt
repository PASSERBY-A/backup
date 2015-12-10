0. 安装oracle client端，包含sqlplus组件
1. 编辑build.sql, 其中DB_LINSTER 修改为你本机配置的linster db 的alias
	DEFINE DB_SCHEMA = iaas2dba
	DEFINE DB_SCHEMA_PASSWORD = iaas2dba
	DEFINE DB_LINSTER = local_iaas
2. 进入cmd，cd 当前目录
3. 命令：sqlplus sys/{password}@{linster_db_alias} as sysdba
4. SQL> @build
5. build过程将产生日志文件_log_create_schema.log，_log_createSchemaObjects.log，第一次build的时候日志会drop对象时会提示找不到对象，这是正常提示