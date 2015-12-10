SPOOL _log_createSchemaObjects.log

PROMPT ####################################################################################
PROMPT Application Framework 
PROMPT ####################################################################################
PROMPT create table app_config
@create_table_1_01_app_config

PROMPT create table create_table_2_01_t_template_account
@create_table_2_01_t_template_account

PROMPT create table create_table_3_01_t_log
@create_table_3_01_t_log

PROMPT create table create_table_4_01_property
@create_table_4_01_property

PROMPT create table create_table_5_01_T_ResourcePool
@create_table_5_01_T_ResourcePool

PROMPT create table create_table_5_02_T_ResourcePool_Zone_Relations
@create_table_5_02_T_ResourcePool_Zone_Relations

PROMPT create table create_table_6_01_t_resourcepool_permission
@create_table_6_01_t_resourcepool_permission

PROMPT create table create_table_7_01_t_dim_resource
@create_table_7_01_t_dim_resource

PROMPT create table create_table_8_01_t_workorder
@create_table_8_01_t_workorder

PROMPT create table create_table_8_02_t_workitem
@create_table_8_02_t_workitem

PROMPT create table create_table_8_03_t_provision_attribute
@create_table_8_03_t_provision_attribute

PROMPT create table create_table_8_04_t_approverule_config
@create_table_8_04_t_approverule_config

PROMPT create table create_table_9_01_t_host
@create_table_9_01_t_host

PROMPT create table create_table_9_02_t_hba
@create_table_9_02_t_hba

PROMPT create table create_table_9_03_t_rack
@create_table_9_03_t_rack


PROMPT create table create_table_9_04_t_bayinfo
@create_table_9_04_t_bayinfo


PROMPT create table create_table_10_01_t_equipmentenroll
@create_table_10_01_t_equipmentenroll


PROMPT create table create_table_10_02_t_equipmentEvent
@create_table_10_02_t_equipmentEvent


PROMPT create table create_table_11_01_t_hpvm
@create_table_11_01_t_hpvm


PROMPT create table create_table_11_02_t_resource
@create_table_11_02_t_resource


PROMPT create table create_table_12_01_t_paas_destory_obj
@create_table_12_01_t_paas_destory_obj


PROMPT ####################################################################################
PROMPT Module   
PROMPT ####################################################################################


PROMPT ####################################################################################
PROMPT Common Function
PROMPT ####################################################################################

PROMPT ####################################################################################
PROMPT Common Procedure
PROMPT ####################################################################################


PROMPT ####################################################################################
PROMPT Common Sequence
PROMPT ####################################################################################
@create_sequence_1_01_app_config_id_seq
@create_sequence_2_01_t_template_account_id_seq
@create_sequence_3_01_t_log_id_seq
@create_sequence_4_01_property
@create_sequence_5_02_T_ResourcePool_Zone_Relations_seq
@create_sequence_6_01_t_resource_perm_id_seq
@create_sequence_7_01_t_dim_resource_id_seq
@create_sequence_8_01_t_workorder_id_seq
@create_sequence_8_02_t_workitem_id_seq
@create_sequence_8_03_t_provision_attribute_id_seq
@create_sequence_8_04_t_approverule_config_id_seq
@create_sequence_9_01_t_host_id_seq
@create_sequence_9_02_t_hba_id_seq
@create_sequence_9_03_t_rack_id_seq
@create_sequence_9_04_t_bainfo_id_seq
@create_sequence_10_01_t_equipmentenroll_id
@create_sequence_10_02_t_equipmentevent_id
@create_sequence_11_01_t_hpvm_id_seq
@create_sequence_11_02_t_resource_id_seq
@create_sequence_12_01_t_paas_destory_id_seq
SPOOL OFF
