package me.xnuminousx.spirits.misc;

import me.xnuminousx.spirits.Main;

public class DBConnection {

	public static Database sql;

	public static String host;
	public static int port;
	public static String db;
	public static String user;
	public static String pass;
	public static boolean isOpen = false;

	public static void init() {
		if (Main.plugin.getConfig().getString("Storage.engine").equalsIgnoreCase("mysql")) {
			sql = new MySQL(Main.plugin.getLogger(), "Establishing MySQL Connection...", host, port, user, pass, db);
			if (((MySQL) sql).open() == null) {
				Main.plugin.getLogger().severe("Disabling due to database error");
				return;
			}

			isOpen = true;
			Main.plugin.getLogger().info("Database connection established.");

			if (!sql.tableExists("spirit_data")) {
				Main.plugin.getLogger().info("Creating spirit_data table");
				String query = "CREATE TABLE `spirit_data` (" + "`uuid` varchar(36) NOT NULL," + "`player` varchar(16) NOT NULL," + "`spirit` varchar(1)," + " PRIMARY KEY (uuid));";
				sql.modifyQuery(query);
			}

		} else {
			sql = new SQLite(Main.plugin.getLogger(), "Establishing SQLite Connection.", "spiritdata.db", Main.plugin.getDataFolder().getAbsolutePath());
			if (((SQLite) sql).open() == null) {
				Main.plugin.getLogger().severe("Disabling due to database error");
				return;
			}

			isOpen = true;
			if (!sql.tableExists("spirit_data")) {
				Main.plugin.getLogger().info("Creating spirit_data table.");
				String query = "CREATE TABLE `spirit_data` (" + "`uuid` TEXT(36) PRIMARY KEY," + "`player` TEXT(16)," + "`spirit` TEXT(1));";
				sql.modifyQuery(query);
			}
		}
	}

	public static boolean isOpen() {
		return isOpen;
	}

}