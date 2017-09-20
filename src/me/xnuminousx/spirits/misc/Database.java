package me.xnuminousx.spirits.misc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.bukkit.scheduler.BukkitRunnable;

import com.projectkorra.projectkorra.ProjectKorra;

public abstract class Database {

	protected final Logger log;
	protected final String prefix;
	protected final String dbprefix;
	protected Connection connection = null;

	public Database(Logger log, String prefix, String dbprefix) {
		this.log = log;
		this.prefix = prefix;
		this.dbprefix = dbprefix;
	}
	
	protected void printInfo(String message) {
		log.info(prefix + dbprefix + message);
	}
	
	protected void printErr(String message, boolean severe) {
		if (severe)
			log.severe(prefix + dbprefix + message);
		else
			log.warning(prefix + dbprefix + message);
	}
	
	public Connection getConnection() {
		return connection;
	}

	abstract Connection open();

	public void close() {
		if (connection != null) {
			try {
				connection.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			this.printErr("There was no SQL connection open.", false);
		}
	}

	public void modifyQuery(final String query) {
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					PreparedStatement stmt = connection.prepareStatement(query);
					stmt.execute();
					stmt.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(ProjectKorra.plugin);
	}

	public ResultSet readQuery(String query) {
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();

			return rs;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean tableExists(String table) {
		try {
			DatabaseMetaData dmd = connection.getMetaData();
			ResultSet rs = dmd.getTables(null, null, table, null);

			return rs.next();
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}