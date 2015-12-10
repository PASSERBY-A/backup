package com.hp.idc.report.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.hp.idc.report.util.Page;

public class ReportDaoImp extends JdbcDaoSupport implements ReportDao {
	private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 基本业务信息统计报告
	 * 
	 * @author Fancy
	 */
	@Override
	public JSONObject getBasicBussnessInformationReport(Page page) {
		// 显示起始记录数
		int start;
		// 显示结束记录数
		int end;

		StringBuffer countSql = new StringBuffer();
		StringBuffer mainSql = new StringBuffer();
		StringBuffer pageSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" SELECT n_category.BUSINESS_TYPE_NAME FLD_CATEGORY,CASE WHEN  n_results.TOTAL_NUM IS NULL THEN 0 ELSE n_results.TOTAL_NUM END TOTAL_NUM ");
		mainSql.append(" FROM (SELECT results.FLD_CATEGORY,COUNT(1) TOTAL_NUM ");
		mainSql.append(" FROM (SELECT T.FLD_CATEGORY ");
		mainSql.append(" FROM (SELECT A1.FLD_CATEGORY ");
		mainSql.append(" FROM ITSM_TASK_1013 A1 ");
		mainSql.append(" WHERE 1 = 1  ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND A1.TASK_CREATE_TIME >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND A1.TASK_CREATE_TIME <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}

		mainSql.append(" UNION ALL ");

		mainSql.append(" SELECT A2.FLD_CATEGORY ");
		mainSql.append(" FROM ITSM_TASK_1014 A2 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND A2.TASK_CREATE_TIME >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND A2.TASK_CREATE_TIME <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}

		mainSql.append(" UNION ALL ");
		mainSql.append(" SELECT A3.FLD_CATEGORY ");
		mainSql.append(" FROM ITSM_TASK_1015 A3 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND A3.TASK_CREATE_TIME >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND A3.TASK_CREATE_TIME <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}

		mainSql.append(" ) T ");
		mainSql.append(" WHERE EXISTS (SELECT 1 ");
		mainSql.append(" FROM RPT_BUSINESS_TYPE R ");
		mainSql.append(" WHERE T.FLD_CATEGORY = R.BUSINESS_TYPE_NAME ");
		mainSql.append(" AND R.TYPE_FLAG = " + page.getReportType()
				+ ")) results ");
		mainSql.append(" GROUP BY results.FLD_CATEGORY ) n_results, (SELECT BUSINESS_TYPE_NAME FROM RPT_BUSINESS_TYPE WHERE TYPE_FLAG ="
				+ page.getReportType() + ") n_category ");
		mainSql.append(" WHERE n_category.BUSINESS_TYPE_NAME = n_results.FLD_CATEGORY(+) ");
		/** end */

		// 组装计数 SQL
		countSql.append("SELECT COUNT(1) FROM (");
		countSql.append(mainSql);
		countSql.append(")");

		int count = this.getJdbcTemplate().queryForInt(countSql.toString());

		if (page.getLast() > count) {
			start = page.getFirst();
			end = count;
		} else {
			start = page.getFirst();
			end = page.getLast();
		}

		// 组装查询list分页SQL
		pageSql.append(" SELECT * FROM (");
		pageSql.append(" SELECT ROWNUM ROWN,result_list.FLD_CATEGORY,result_list.TOTAL_NUM FROM ( ");

		pageSql.append(mainSql);

		pageSql.append(" ) result_list");
		pageSql.append(" WHERE ROWNUM <= " + end);
		pageSql.append(" ) newR");
		pageSql.append(" WHERE newR.ROWN >=" + start);

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(pageSql.toString());

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);
		return jo;
	}

	/**
	 * 业务变动统计报告
	 * 
	 * @author Fancy
	 */
	@Override
	public JSONObject getBussnessChangeCountReport(Page page) {
		// 显示起始记录数
		int start;
		// 显示结束记录数
		int end;

		StringBuffer countSql = new StringBuffer();
		StringBuffer mainSql = new StringBuffer();
		StringBuffer pageSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" SELECT results.CREATE_MONTH,COUNT(1) AS TOTAL_NUM,COUNT(ADD_FLAG) AS ADD_NUM,COUNT(UPDATE_FLAG) AS UPDATE_NUM,COUNT(END_FLAG) AS END_NUM ");
		mainSql.append(" FROM (SELECT to_char(T.TASK_CREATE_TIME,'YYYY-MM') AS CREATE_MONTH,T.ADD_FLAG,T.UPDATE_FLAG,T.END_FLAG ");
		mainSql.append(" FROM (SELECT A1.TASK_CREATE_TIME,1 AS ADD_FLAG,NULL AS UPDATE_FLAG,NULL AS END_FLAG ");
		mainSql.append(" FROM ITSM_TASK_1013 A1 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND to_char(A1.TASK_CREATE_TIME,'YYYY') = '"
					+ page.getStartTime()+"' ");
		}
		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND to_char(A1.TASK_CREATE_TIME, 'YYYY-MM') <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }

		mainSql.append(" UNION ALL ");

		mainSql.append(" SELECT A2.TASK_CREATE_TIME, NULL AS ADD_FLAG,1 AS UPDATE_FLAG,NULL AS END_FLAG ");
		mainSql.append(" FROM ITSM_TASK_1014 A2 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND to_char(A2.TASK_CREATE_TIME,'YYYY') = '"
					+ page.getStartTime() + "' ");
		}

		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND to_char(A2.TASK_CREATE_TIME, 'YYYY-MM') <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }

		mainSql.append(" UNION ALL ");

		mainSql.append(" SELECT A3.TASK_CREATE_TIME, NULL AS ADD_FLAG,NULL AS UPDATE_FLAG,1 AS END_FLAG ");
		mainSql.append(" FROM ITSM_TASK_1015 A3 ");
		mainSql.append("  WHERE 1 = 1 ");

		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND to_char(A3.TASK_CREATE_TIME,'YYYY') = '"
					+ page.getStartTime() + "' ");
		}

		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND to_char(A3.TASK_CREATE_TIME, 'YYYY-MM') <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }

		mainSql.append(" ) T) results ");
		mainSql.append(" GROUP BY results.CREATE_MONTH ");
		mainSql.append(" ORDER BY results.CREATE_MONTH DESC ");

		/** end */

		// 组装计数 SQL
		countSql.append("SELECT COUNT(1) FROM (");
		countSql.append(mainSql);
		countSql.append(")");

		int count = this.getJdbcTemplate().queryForInt(countSql.toString());

		if (page.getLast() > count) {
			start = page.getFirst();
			end = count;
		} else {
			start = page.getFirst();
			end = page.getLast();
		}

		// 组装查询list分页SQL
		pageSql.append(" SELECT * FROM (");
		pageSql.append(" SELECT ROWNUM ROWN,result_list.* FROM ( ");

		pageSql.append(mainSql);

		pageSql.append(" ) result_list");
		pageSql.append(" WHERE ROWNUM <= " + end);
		pageSql.append(" ) newR");
		pageSql.append(" WHERE newR.ROWN >=" + start);

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(pageSql.toString());

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);
		return jo;

	}

	/**
	 * 业务工单统计报告
	 * 
	 * @author Fancy
	 */
	@Override
	public JSONObject getBussnessOrderCountReport(Page page) {
		StringBuffer sb = new StringBuffer();
		StringBuffer countSql = new StringBuffer();
		int start;
		int end;
		String start_time;
		String end_time;
		if (page.getStartTime() == null) {
			start_time = df.format(new Date(System.currentTimeMillis()));
		} else {
			start_time = page.getStartTime();
		}
		if (page.getEndTime() == null) {
			end_time = df.format(new Date(System.currentTimeMillis()));
		} else {
			end_time = page.getEndTime();
		}

		// 组装计数 SQL
		countSql.append("SELECT COUNT(1) ");
		countSql.append("FROM (SELECT T.FLD_CATEGORY ");
		countSql.append("FROM (SELECT A1.FLD_CATEGORY ");
		countSql.append("FROM ITSM_TASK_1013 A1 ");
		countSql.append("WHERE A1.TASK_CREATE_TIME BETWEEN to_date('"
				+ start_time + "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		countSql.append("UNION ALL ");

		countSql.append("SELECT A2.FLD_CATEGORY ");
		countSql.append("FROM ITSM_TASK_1014 A2 ");
		countSql.append("WHERE A2.TASK_CREATE_TIME BETWEEN to_date('"
				+ start_time + "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		countSql.append("UNION ALL ");
		countSql.append("SELECT A3.FLD_CATEGORY ");
		countSql.append(" FROM ITSM_TASK_1015 A3  ");
		countSql.append("WHERE A3.TASK_CREATE_TIME BETWEEN to_date('"
				+ start_time + "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		countSql.append(" ) T GROUP BY T.FLD_CATEGORY) newR ");
		System.out.print(countSql.toString());
		int count = this.getJdbcTemplate().queryForInt(countSql.toString());

		if (page.getLast() > count) {
			start = page.getFirst();
			end = count;
		} else {
			start = page.getFirst();
			end = page.getLast();
		}

		// 组装list SQL
		sb.append("SELECT CASE WHEN newR.FLD_CATEGORY IS NULL THEN '-' ELSE newR.FLD_CATEGORY END FLD_CATEGORY,newR.TOTAL_NUM,newR.FINISH_NUM,newR.OVER_TIME_NUM,newR.rown ");
		sb.append("FROM (SELECT result_list.FLD_CATEGORY,result_list.TOTAL_NUM,result_list.FINISH_NUM,result_list.OVER_TIME_NUM,rownum ROWN ");
		sb.append("FROM (SELECT T.FLD_CATEGORY, ");
		sb.append("SUM(T.TOTAL_FLAG) TOTAL_NUM, ");
		sb.append("SUM(T.FINISH_FLAG) FINISH_NUM, ");
		sb.append("SUM(T.OVER_TIME_FLAG) OVER_TIME_NUM ");
		sb.append("FROM (SELECT A1.FLD_CATEGORY, ");
		sb.append("1 AS TOTAL_FLAG, ");
		sb.append("A1.TASK_STATUS AS FINISH_FLAG, ");
		sb.append("CASE WHEN A1.TASK_UPDATE_TIME > ");
		sb.append("to_date(A1.FLD_CARRY_DEADLINE, 'YYYY-MM-DD hh24:mi:ss') THEN ");
		sb.append("1 ELSE 0 END OVER_TIME_FLAG ");
		sb.append("FROM ITSM_TASK_1013 A1 ");
		sb.append("WHERE A1.TASK_CREATE_TIME BETWEEN to_date('" + start_time
				+ "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		sb.append("UNION ALL ");

		sb.append("SELECT A2.FLD_CATEGORY, ");
		sb.append("1 AS TOTAL_FLAG, ");
		sb.append("A2.TASK_STATUS AS FINISH_FLAG, ");
		sb.append("CASE WHEN A2.TASK_UPDATE_TIME > ");
		sb.append("to_date(A2.FLD_CARRY_DEADLINE, 'YYYY-MM-DD hh24:mi:ss') THEN ");
		sb.append("1  ELSE 0 END OVER_TIME_FLAG ");
		sb.append("FROM ITSM_TASK_1014 A2 ");
		sb.append("WHERE A2.TASK_CREATE_TIME BETWEEN to_date('" + start_time
				+ "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		sb.append("UNION ALL ");
		sb.append("SELECT A3.FLD_CATEGORY, ");
		sb.append("1 AS TOTAL_FLAG, ");
		sb.append("A3.TASK_STATUS AS FINISH_FLAG, ");
		sb.append("CASE WHEN A3.TASK_UPDATE_TIME > ");
		sb.append("to_date(A3.FLD_CARRY_DEADLINE, 'YYYY-MM-DD hh24:mi:ss') THEN ");
		sb.append("1 ELSE 0 END OVER_TIME_FLAG ");
		sb.append(" FROM ITSM_TASK_1015 A3  ");
		sb.append("WHERE A3.TASK_CREATE_TIME BETWEEN to_date('" + start_time
				+ "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		sb.append(" ) T GROUP BY T.FLD_CATEGORY) result_list WHERE rownum <="
				+ end + ") newR where newR.rown>=" + start);

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(sb.toString());

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);
		return jo;

	}

	// 客户变动明细报告
	@Override
	public JSONObject getCustomerBiandongmingxiCountReport(Page page) {

		// 显示起始记录数
		int start;
		// 显示结束记录数
		int end;

		StringBuffer countSql = new StringBuffer();
		StringBuffer mainSql = new StringBuffer();
		StringBuffer pageSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" select t.customer_id cid, ");
		mainSql.append(" t.customer_name cname, ");
		mainSql.append(" case t.customer_status ");
		mainSql.append("  when 0 then '历史客户' ");
		mainSql.append("  when 1 then '现有客户' ");
		mainSql.append(" when 2 then '新增客户' ");
		mainSql.append(" else '注销客户' ");
		mainSql.append("  end status ");
		// mainSql.append(" rownum rownum ");
		mainSql.append(" from customer_info t ");
		mainSql.append(" where 1=1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND t.dt_opentime >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND t.dt_opentime <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}
		mainSql.append(" order by t.customer_status ");
		/** end */

		// 组装计数 SQL
		countSql.append("SELECT COUNT(1) FROM (");
		countSql.append(mainSql);
		countSql.append(")");

		int count = this.getJdbcTemplate().queryForInt(countSql.toString());

		if (page.getLast() > count) {
			start = page.getFirst();
			end = count;
		} else {
			start = page.getFirst();
			end = page.getLast();
		}

		// 组装查询list分页SQL
		pageSql.append(" SELECT * FROM (");
		pageSql.append(" SELECT ROWNUM ROWN,result_list.cid,result_list.cname,result_list.status FROM ( ");

		pageSql.append(mainSql);

		pageSql.append(" ) result_list");
		pageSql.append(" WHERE ROWNUM <= " + end);
		pageSql.append(" ) newR");
		pageSql.append(" WHERE newR.ROWN >=" + start);

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(pageSql.toString());

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);

		return jo;

	}

	// 客户变动统计报告
	@Override
	public JSONObject getCustomerBiandongtongjiCountReport(Page page) {
		// 显示起始记录数
		int start;
		// 显示结束记录数
		int end;

		StringBuffer countSql = new StringBuffer();
		StringBuffer mainSql = new StringBuffer();
		StringBuffer pageSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" select openTime, ");
		mainSql.append("  count(td.customer_status) customer_count, ");
		mainSql.append(" count(td.lishi_status) llshi_count, ");
		mainSql.append(" count(td.zhengchang_status) zhengchang_count, ");
		mainSql.append(" count(td.qianzai_status) qianzai_count, ");
		mainSql.append(" count(td.zhuxiao_status) zhuxiao_count ");
		mainSql.append(" from (select customer_status customer_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append(" when 0 then ");
		mainSql.append(" 1 ");
		mainSql.append(" else ");
		mainSql.append(" null ");
		mainSql.append(" end lishi_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append("  when 1 then ");
		mainSql.append(" 1 ");
		mainSql.append(" else ");
		mainSql.append(" null ");
		mainSql.append(" end zhengchang_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append(" when 2 then ");
		mainSql.append("  1 ");
		mainSql.append("  else ");
		mainSql.append(" null ");
		mainSql.append(" end qianzai_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append(" when 3 then ");
		mainSql.append("  1 ");
		mainSql.append(" else ");
		mainSql.append(" null ");
		mainSql.append(" end zhuxiao_status, ");
		mainSql.append(" to_char(dt_opentime, 'yyyy-mm') openTime ");
		mainSql.append(" from customer_info t) td where 1=1 ");

		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND td.openTime LIKE '" + page.getStartTime()
					+ "%'");
		}
		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND td.openTime <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }
		mainSql.append(" group by openTime order by openTime  ");

		/** end */

		// 组装计数 SQL
		countSql.append("SELECT COUNT(1) FROM (");
		countSql.append(mainSql);
		countSql.append(")");

		int count = this.getJdbcTemplate().queryForInt(countSql.toString());

		if (page.getLast() > count) {
			start = page.getFirst();
			end = count;
		} else {
			start = page.getFirst();
			end = page.getLast();
		}

		// 组装查询list分页SQL
		pageSql.append(" SELECT * FROM (");
		pageSql.append(" SELECT ROWNUM ROWN,result_list.openTime,result_list.customer_count,result_list.llshi_count ,result_list.zhengchang_count ,result_list.qianzai_count,result_list.zhuxiao_count  FROM ( ");

		pageSql.append(mainSql);

		pageSql.append(" ) result_list");
		pageSql.append(" WHERE ROWNUM <= " + end);
		pageSql.append(" ) newR");
		pageSql.append(" WHERE newR.ROWN >=" + start);

		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(pageSql.toString());

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);
		return jo;
	}

	@Override
	public void getCustomerBussnessTypeCountReport() {
		// TODO Auto-generated method stub

	}

	// 客户服务统计报告
	@Override
	public JSONObject getCustomerFuwuCountReport(Page page) {
		String sqlPart1 = "select d.serviceType serviceType,d.shoulicount shoulicount,d.wanchengcount wanchengcount from"
				+ " (select t.serviceType serviceType,t.shoulicount shoulicount,t.wanchengcount wanchengcount,rownum trownum from"
				+ " (select results.serviceType serviceType,"
				+ " count(results.shoulicount) shoulicount,"
				+ " count(results.wanchengcount) wanchengcount"
				+ " from (select fld_servicetype serviceType,"
				+ " case task_status"
				+ " when 0 then 1"
				+ "  else null"
				+ " end shoulicount,"
				+ " case task_status"
				+ " when 1 then 1"
				+ " else null" + "  end wanchengcount from itsm_task_1017 ";

		String sqlPart2 = "  where task_create_time between to_date('2011-05-04','yyyy-mm-dd') and to_date('2011-08-01','yyyy-mm-dd')";
		String sqlPart3 = "  ) results group by results.servicetype) t where rownum<=3) d where d.trownum>=2";

		String countSql = "select count(distinct fld_servicetype) from itsm_task_1017 t";
		if (page.getStartTime() != null && page.getEndTime() != null) {
			countSql += " where task_create_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
		}

		int count = this.getJdbcTemplate().queryForInt(countSql);
		if (page.getLast() > count) {
			sqlPart3 = "  ) results group by results.servicetype) t where rownum<="
					+ count + ") d where d.trownum>=" + page.getFirst();
		} else {
			sqlPart3 = "  ) results group by results.servicetype) t where rownum<="
					+ page.getLast()
					+ ") d where d.trownum>="
					+ page.getFirst();
		}

		System.out.println(page.getLast());
		System.out.println(count);

		String sql = null;
		if (page.getStartTime() != null && page.getEndTime() != null) {
			sqlPart2 = "  where task_create_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
			sql = new StringBuffer(sqlPart1).append(sqlPart2).append(sqlPart3)
					.toString();
		} else {
			sql = new StringBuffer(sqlPart1).append(sqlPart3).toString();
		}
		System.out.println(sql);
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(sql);

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);
		return jo;
	}

	// 客户服务清单报告
	@Override
	public JSONObject getCustomerQingdanCountReport(Page page) {
		String sqlPart1 = "select tid,to_char(updateTime,'yyyy-mm-dd') updateTime,taskUser,serviceType,processresult,excuteUser from (select task_oid tid, task_update_time updateTime,task_user taskUser, fld_servicetype serviceType,fld_processresult processresult,fld_execute_user excuteUser,rownum rrownum from itsm_task_1017 where 1=1 ";
		String sqlPart2 = " and task_update_time between to_date('2011-06-01','yyyy-mm-dd') and to_date('2011-08-09','yyyy-mm-dd') ";
		String sqlPart3 = " and rownum<=5) results where rrownum>=1 ";

		String countSql = "select count(task_oid) from itsm_task_1017 t";
		if (page.getStartTime() != null && page.getEndTime() != null) {
			countSql += " where task_update_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
		}

		int count = this.getJdbcTemplate().queryForInt(countSql);
		if (page.getLast() > count) {
			sqlPart3 = " and rownum<= " + count + ") results where rrownum>="
					+ page.getFirst();
		} else {
			sqlPart3 = " and rownum<= " + page.getLast()
					+ ") results where rrownum>=" + page.getFirst();
		}

		System.out.println(page.getLast());
		System.out.println(count);

		String sql = null;
		if (page.getStartTime() != null && page.getEndTime() != null) {
			sqlPart2 = " and task_update_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
			sql = new StringBuffer(sqlPart1).append(sqlPart2).append(sqlPart3)
					.toString();
		} else {
			sql = new StringBuffer(sqlPart1).append(sqlPart3).toString();
		}
		System.out.println(sql);
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(sql);

		JSONObject jo = new JSONObject();
		jo.put("count", count);
		jo.put("results", results);
		return jo;
	}

	@Override
	public JSONObject getCustomerWupinjinruCountReport(Page page) {
		return null;
	}

	@Override
	public List<Map<String, Object>> getPriceBussnessInformationReport() {
		String sql = "select customer_id,count(service_id) count from customer_servant_info group by customer_id";
		return this.getJdbcTemplate().queryForList(sql);
	}

	// 导出基本业务信息统计报告
	@Override
	public List<Map<String, Object>> exportBasicBussnessInformationReport(
			Page page) {

		StringBuffer mainSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" SELECT n_category.BUSINESS_TYPE_NAME FLD_CATEGORY,CASE WHEN  n_results.TOTAL_NUM IS NULL THEN 0 ELSE n_results.TOTAL_NUM END TOTAL_NUM ");
		mainSql.append(" FROM (SELECT results.FLD_CATEGORY,COUNT(1) TOTAL_NUM ");
		mainSql.append(" FROM (SELECT T.FLD_CATEGORY ");
		mainSql.append(" FROM (SELECT A1.FLD_CATEGORY ");
		mainSql.append(" FROM ITSM_TASK_1013 A1 ");
		mainSql.append(" WHERE 1 = 1  ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND A1.TASK_CREATE_TIME >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND A1.TASK_CREATE_TIME <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}

		mainSql.append(" UNION ALL ");

		mainSql.append(" SELECT A2.FLD_CATEGORY ");
		mainSql.append(" FROM ITSM_TASK_1014 A2 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND A2.TASK_CREATE_TIME >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND A2.TASK_CREATE_TIME <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}

		mainSql.append(" UNION ALL ");
		mainSql.append(" SELECT A3.FLD_CATEGORY ");
		mainSql.append(" FROM ITSM_TASK_1015 A3 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND A3.TASK_CREATE_TIME >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND A3.TASK_CREATE_TIME <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}

		mainSql.append(" ) T ");
		mainSql.append(" WHERE EXISTS (SELECT 1 ");
		mainSql.append(" FROM RPT_BUSINESS_TYPE R ");
		mainSql.append(" WHERE T.FLD_CATEGORY = R.BUSINESS_TYPE_NAME ");
		mainSql.append(" AND R.TYPE_FLAG = " + page.getReportType()
				+ ")) results ");
		mainSql.append(" GROUP BY results.FLD_CATEGORY ) n_results, (SELECT BUSINESS_TYPE_NAME FROM RPT_BUSINESS_TYPE WHERE TYPE_FLAG ="
				+ page.getReportType() + ") n_category ");
		mainSql.append(" WHERE n_category.BUSINESS_TYPE_NAME = n_results.FLD_CATEGORY(+) ");
		/** end */

		// 组装查询list分页SQL

		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(mainSql.toString());

		return results;
	}

	// 导出业务变动统计Dao
	@Override
	public List<Map<String, Object>> exportBussnessChangeCountReport(Page page) {

		StringBuffer countSql = new StringBuffer();
		StringBuffer mainSql = new StringBuffer();
		StringBuffer pageSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" SELECT results.CREATE_MONTH,COUNT(1) AS TOTAL_NUM,COUNT(ADD_FLAG) AS ADD_NUM,COUNT(UPDATE_FLAG) AS UPDATE_NUM,COUNT(END_FLAG) AS END_NUM ");
		mainSql.append(" FROM (SELECT to_char(T.TASK_CREATE_TIME,'YYYY-MM') AS CREATE_MONTH,T.ADD_FLAG,T.UPDATE_FLAG,T.END_FLAG ");
		mainSql.append(" FROM (SELECT A1.TASK_CREATE_TIME,1 AS ADD_FLAG,NULL AS UPDATE_FLAG,NULL AS END_FLAG ");
		mainSql.append(" FROM ITSM_TASK_1013 A1 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND to_char(A1.TASK_CREATE_TIME,'YYYY') = '"
					+ page.getStartTime() + "' ");
		}
		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND to_char(A1.TASK_CREATE_TIME, 'YYYY-MM') <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }

		mainSql.append(" UNION ALL ");

		mainSql.append(" SELECT A2.TASK_CREATE_TIME, NULL AS ADD_FLAG,1 AS UPDATE_FLAG,NULL AS END_FLAG ");
		mainSql.append(" FROM ITSM_TASK_1014 A2 ");
		mainSql.append(" WHERE 1 = 1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND to_char(A2.TASK_CREATE_TIME,'YYYY') = '"
					+ page.getStartTime() + "' ");
		}

		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND to_char(A2.TASK_CREATE_TIME, 'YYYY-MM') <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }

		mainSql.append(" UNION ALL ");

		mainSql.append(" SELECT A3.TASK_CREATE_TIME, NULL AS ADD_FLAG,NULL AS UPDATE_FLAG,1 AS END_FLAG ");
		mainSql.append(" FROM ITSM_TASK_1015 A3 ");
		mainSql.append("  WHERE 1 = 1 ");

		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND to_char(A3.TASK_CREATE_TIME,'YYYY') = '"
					+ page.getStartTime() + "' ");
		}

		// if (page.getEndTime() != null && !page.getEndTime().equals("")) {
		// mainSql.append(" AND to_char(A3.TASK_CREATE_TIME, 'YYYY-MM') <= to_char(to_date('"
		// + page.getEndTime() + "', 'YYYY-MM'),'YYYY-MM') ");
		// }

		mainSql.append(" ) T) results ");
		mainSql.append(" GROUP BY results.CREATE_MONTH ");
		mainSql.append(" ORDER BY results.CREATE_MONTH DESC ");

		/** end */

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(mainSql.toString());
		return results;

	}

	// 导出业务工单统计报告
	@Override
	public List<Map<String, Object>> exportBussnessOrderCountReport(Page page) {
		StringBuffer sb = new StringBuffer();

		String start_time;
		String end_time;
		if (page.getStartTime() == null) {
			start_time = df.format(new Date(System.currentTimeMillis()));
		} else {
			start_time = page.getStartTime();
		}
		if (page.getEndTime() == null) {
			end_time = df.format(new Date(System.currentTimeMillis()));
		} else {
			end_time = page.getEndTime();
		}
		// 组装list SQL
		sb.append("SELECT newR.FLD_CATEGORY,newR.TOTAL_NUM,newR.FINISH_NUM,newR.OVER_TIME_NUM,newR.rown ");
		sb.append("FROM (SELECT result_list.FLD_CATEGORY,result_list.TOTAL_NUM,result_list.FINISH_NUM,result_list.OVER_TIME_NUM,rownum ROWN ");
		sb.append("FROM (SELECT T.FLD_CATEGORY, ");
		sb.append("SUM(T.TOTAL_FLAG) TOTAL_NUM, ");
		sb.append("SUM(T.FINISH_FLAG) FINISH_NUM, ");
		sb.append("SUM(T.OVER_TIME_FLAG) OVER_TIME_NUM ");
		sb.append("FROM (SELECT A1.FLD_CATEGORY, ");
		sb.append("1 AS TOTAL_FLAG, ");
		sb.append("A1.TASK_STATUS AS FINISH_FLAG, ");
		sb.append("CASE WHEN A1.TASK_UPDATE_TIME > ");
		sb.append("to_date(A1.FLD_CARRY_DEADLINE, 'YYYY-MM-DD hh24:mi:ss') THEN ");
		sb.append("1 ELSE 0 END OVER_TIME_FLAG ");
		sb.append("FROM ITSM_TASK_1013 A1 ");
		sb.append("WHERE A1.TASK_CREATE_TIME BETWEEN to_date('" + start_time
				+ "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		sb.append("UNION ALL ");

		sb.append("SELECT A2.FLD_CATEGORY, ");
		sb.append("1 AS TOTAL_FLAG, ");
		sb.append("A2.TASK_STATUS AS FINISH_FLAG, ");
		sb.append("CASE WHEN A2.TASK_UPDATE_TIME > ");
		sb.append("to_date(A2.FLD_CARRY_DEADLINE, 'YYYY-MM-DD hh24:mi:ss') THEN ");
		sb.append("1  ELSE 0 END OVER_TIME_FLAG ");
		sb.append("FROM ITSM_TASK_1014 A2 ");
		sb.append("WHERE A2.TASK_CREATE_TIME BETWEEN to_date('" + start_time
				+ "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		sb.append("UNION ALL ");
		sb.append("SELECT A3.FLD_CATEGORY, ");
		sb.append("1 AS TOTAL_FLAG, ");
		sb.append("A3.TASK_STATUS AS FINISH_FLAG, ");
		sb.append("CASE WHEN A3.TASK_UPDATE_TIME > ");
		sb.append("to_date(A3.FLD_CARRY_DEADLINE, 'YYYY-MM-DD hh24:mi:ss') THEN ");
		sb.append("1 ELSE 0 END OVER_TIME_FLAG ");
		sb.append(" FROM ITSM_TASK_1015 A3  ");
		sb.append("WHERE A3.TASK_CREATE_TIME BETWEEN to_date('" + start_time
				+ "','YYYY-MM-DD') AND to_date('" + end_time
				+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");

		sb.append(" ) T GROUP BY T.FLD_CATEGORY) result_list ) newR");

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(sb.toString());

		return results;
	}

	// 导出客户变动明细报告
	@Override
	public List<Map<String, Object>> exportCustomerBiandongmingxiCountReport(
			Page page) {
		StringBuffer mainSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" select t.customer_id cid, ");
		mainSql.append(" t.customer_name cname, ");
		mainSql.append(" case t.customer_status ");
		mainSql.append("  when 0 then '历史客户' ");
		mainSql.append("  when 1 then '现有客户' ");
		mainSql.append(" when 2 then '新增客户' ");
		mainSql.append(" else '注销客户' ");
		mainSql.append("  end status ");
		mainSql.append(" from customer_info t ");
		mainSql.append(" where 1=1 ");
		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append(" AND t.dt_opentime >= to_date('"
					+ page.getStartTime() + "','YYYY-MM-DD') ");
		}
		if (page.getEndTime() != null && !page.getEndTime().equals("")) {
			mainSql.append(" AND t.dt_opentime <= to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ");
		}
		mainSql.append(" order by t.customer_status ");
		/** end */

		// Run SQL
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(mainSql.toString());
		return results;
	}

	// 导出客户变动统计报告
	@Override
	public List<Map<String, Object>> exportCustomerBiandongtongjiCountReport(
			Page page) {
		StringBuffer countSql = new StringBuffer();
		StringBuffer mainSql = new StringBuffer();
		StringBuffer pageSql = new StringBuffer();

		/** sql主体 start */
		mainSql.append(" select openTime, ");
		mainSql.append("  count(td.customer_status) customer_count, ");
		mainSql.append(" count(td.lishi_status) llshi_count, ");
		mainSql.append(" count(td.zhengchang_status) zhengchang_count, ");
		mainSql.append(" count(td.qianzai_status) qianzai_count, ");
		mainSql.append(" count(td.zhuxiao_status) zhuxiao_count ");
		mainSql.append(" from (select customer_status customer_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append(" when 0 then ");
		mainSql.append(" 1 ");
		mainSql.append(" else ");
		mainSql.append(" null ");
		mainSql.append(" end lishi_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append("  when 1 then ");
		mainSql.append(" 1 ");
		mainSql.append(" else ");
		mainSql.append(" null ");
		mainSql.append(" end zhengchang_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append(" when 2 then ");
		mainSql.append("  1 ");
		mainSql.append("  else ");
		mainSql.append(" null ");
		mainSql.append(" end qianzai_status, ");
		mainSql.append(" case customer_status ");
		mainSql.append(" when 3 then ");
		mainSql.append("  1 ");
		mainSql.append(" else ");
		mainSql.append(" null ");
		mainSql.append(" end zhuxiao_status, ");
		mainSql.append(" to_char(dt_opentime, 'yyyy-mm') openTime ");
		mainSql.append(" from customer_info t) td where 1=1 ");

		if (page.getStartTime() != null && !page.getStartTime().equals("")) {
			mainSql.append("  AND td.openTime LIKE '" + page.getStartTime()
					+ "%'");
		}
		
		mainSql.append(" group by openTime order by openTime  ");

		/** end */

		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(mainSql.toString());
		return results;
	}

	@Override
	public List<Map<String, Object>> exportCustomerBussnessTypeCountReport(
			Page page) {
		return null;
	}

	// 导出客户服务统计报告
	@Override
	public List<Map<String, Object>> exportCustomerFuwuCountReport(Page page) {
		String sqlPart1 = "select d.serviceType serviceType,d.shoulicount shoulicount,d.wanchengcount wanchengcount from"
				+ " (select t.serviceType serviceType,t.shoulicount shoulicount,t.wanchengcount wanchengcount,rownum trownum from"
				+ " (select results.serviceType serviceType,"
				+ " count(results.shoulicount) shoulicount,"
				+ " count(results.wanchengcount) wanchengcount"
				+ " from (select fld_servicetype serviceType,"
				+ " case task_status"
				+ " when 0 then 1"
				+ "  else null"
				+ " end shoulicount,"
				+ " case task_status"
				+ " when 1 then 1"
				+ " else null" + "  end wanchengcount from itsm_task_1017 ";

		String sqlPart2 = "  where task_create_time between to_date('2011-05-04','yyyy-mm-dd') and to_date('2011-08-01','yyyy-mm-dd')";
		String sqlPart3 = "  ) results group by results.servicetype) t ) d ";

		System.out.println(page.getLast());

		String sql = null;
		if (page.getStartTime() != null && page.getEndTime() != null) {
			sqlPart2 = "  where task_create_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
			sql = new StringBuffer(sqlPart1).append(sqlPart2).append(sqlPart3)
					.toString();
		} else {
			sql = new StringBuffer(sqlPart1).append(sqlPart3).toString();
		}
		System.out.println(sql);
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(sql);
		return results;
	}

	// 导出客户服务清单报告
	@Override
	public List<Map<String, Object>> exportCustomerQingdanCountReport(Page page) {
		String sqlPart1 = "select tid,to_char(updateTime,'yyyy-mm-dd') updateTime,taskUser,serviceType,processresult,excuteUser from (select task_oid tid, task_update_time updateTime,task_user taskUser, fld_servicetype serviceType,fld_processresult processresult,fld_execute_user excuteUser,rownum rrownum from itsm_task_1017 ";
		String sqlPart2 = "task_update_time between to_date('2011-06-01','yyyy-mm-dd') and to_date('2011-08-09','yyyy-mm-dd') ";
		String sqlPart3 = " ) results ";

		String countSql = "select count(task_oid) from itsm_task_1017 t";
		if (page.getStartTime() != null && page.getEndTime() != null) {
			countSql += " where task_update_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
		}

		String sql = null;
		if (page.getStartTime() != null && page.getEndTime() != null) {
			sqlPart2 = " where task_update_time between to_date('"
					+ page.getStartTime() + "','yyyy-mm-dd') and to_date('"
					+ page.getEndTime()
					+ " 23:59:59','YYYY-MM-DD HH24:MI:SS') ";
			sql = new StringBuffer(sqlPart1).append(sqlPart2).append(sqlPart3)
					.toString();
		} else {
			sql = new StringBuffer(sqlPart1).append(sqlPart3).toString();
		}
		System.out.println(sql);
		List<Map<String, Object>> results = this.getJdbcTemplate()
				.queryForList(sql);
		return results;
	}

	@Override
	public List<Map<String, Object>> exportCustomerWupinjinruCountReport(
			Page page) {
		return null;
	}

	@Override
	public List<Map<String, Object>> exportPriceBussnessInformationReport(
			Page page) {
		return null;
	}

}
