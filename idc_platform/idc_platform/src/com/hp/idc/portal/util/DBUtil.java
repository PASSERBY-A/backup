package com.hp.idc.portal.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

public class DBUtil {
	private static JdbcTemplate jdbcTemplate;

	private static LobHandler lobHandler;

	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		DBUtil.jdbcTemplate = jdbcTemplate;
	}


	public static LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		DBUtil.lobHandler = lobHandler;
	}
}
