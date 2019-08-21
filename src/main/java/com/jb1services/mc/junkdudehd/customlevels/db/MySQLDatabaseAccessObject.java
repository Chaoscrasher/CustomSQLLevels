package com.jb1services.mc.junkdudehd.customlevels.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import com.jb1services.chaosutil.util.collections.ChaosArrayList;
import com.jb1services.chaosutil.util.collections.ChaosList;
import com.jb1services.chaosutil.util.formating.FormatUtil;

public class MySQLDatabaseAccessObject
{
	private Connection con;

	private String user;
	private String pw;
	private String timezone;
	private String host;
	private String db;

	public MySQLDatabaseAccessObject(Properties prop)
	{
		this(prop.getProperty("user"), prop.getProperty("pw"), prop.getProperty("timezone"), prop.getProperty("host"), prop.getProperty("db"));
	}
	
	public MySQLDatabaseAccessObject()
	{
		this("root", "password", "CET", "localhost:3306", "chores");
	}

	public MySQLDatabaseAccessObject(String user, String pw, String timezone, String host, String db)
	{
		this.user = user;
		this.pw = pw;
		this.timezone = timezone;
		this.host = host;
		this.db = db;
	}

	public void connect()
	{
		try
		{
			Properties p = new Properties();
			p.setProperty("user", user);
			p.setProperty("password", pw);
			p.setProperty("serverTimezone", timezone);
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(
					"jdbc:mysql://"+host+"/"+db, p);
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//here sonoo is database name, root is username and password
	}
	
	public Optional<String> testConnect()
	{
		try
		{
			Properties p = new Properties();
			p.setProperty("user", user);
			p.setProperty("password", pw);
			p.setProperty("serverTimezone", timezone);
			Class.forName("com.mysql.cj.jdbc.Driver");
			con=DriverManager.getConnection(
					"jdbc:mysql://"+host+"/"+db, p);
			close();
			return Optional.empty();
		} catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Optional.of(e.getCause().getClass().getName());
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Optional.of(e.getCause().getClass().getName());
		}
	}

	public void close()
	{
		try
		{
			con.close();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean execute(String query)
	{
		connect();
		Statement stmt;
		try
		{
			stmt = con.createStatement();
			boolean res = stmt.execute(query);
			close();
			return res;
		} catch (SQLException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
			close();
			return false;
		}
	}

	public Optional<ResultSet> query(String query)
	{
		connect();
		Statement stmt;
		try
		{
			if (con != null)
			{
				stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				return Optional.of(rs);
			}
			else
			{
				System.err.println("SQL Connection missing! Can not execute query!");
				return Optional.empty();
			}
		} catch (SQLException e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return Optional.empty();
		}
	}


	public Optional<List<User>> getAllUsers()
	{
		Optional<ResultSet> rso = query("select * from users");
		if (rso.isPresent())
		{
			ResultSet rs = rso.get();
			List<User> users = new ArrayList<>();
			try
			{
				while (rs.next())
				{
					users.add(new User(UUID.fromString(rs.getString(1)), rs.getInt(2), FormatUtil.sQLDateToLocalDate(rs.getDate(3))));
				}
			} catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			close();
			return Optional.of(users);
		}
		else
		{
			System.err.println("Couldn't retrieve users from SQL Server!");
			return Optional.empty();
		}
	}

	public Optional<User> getUser(UUID uuid)
	{
		Optional<ResultSet> rso = query("SELECT * FROM users WHERE uuid = '" + uuid + "';");
		if (rso.isPresent())
		{
			ResultSet rs = rso.get();
			close();
			try 
			{
				return Optional.of(new User(UUID.fromString(rs.getString(1)), rs.getInt(2), FormatUtil.sQLDateToLocalDate(rs.getDate(3))));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return Optional.empty();
			}
		}
		else
		{
			System.err.println("Couldn't retrieve sigular user from SQL Server!");
			return Optional.empty();
		}
	}
	
	public boolean addXP(UUID user, int xpToAdd) throws SQLException
	{
		Optional<Integer> cxpo = getXP(user);
		if (cxpo.isPresent())
		{
			int cxp = cxpo.get();
			return execute("UPDATE TABLE users SET xp = "+(cxp+xpToAdd)+" WHERE uuid = '"+user+"';");
		}
		else
		{
			System.err.println("Couldn't add XP due to retrieving current XP failing!");
			return false;
		}
	}

	public Optional<Integer> getXP(UUID user) throws SQLException
	{
		Optional<ResultSet> rso = query("SELECT xp FROM users WHERE uuid = '"+user+"';");
		if (rso.isPresent())
		{
			ResultSet rs = rso.get();
			int xp = rs.getInt(1);
			close();
			return Optional.of(xp);
		}
		else
		{
			System.err.println("Couldn't retrieve XP for user "+user+" from SQL Server!");
			return Optional.empty();
		}
	}
	
	public Optional<DateTime> getDateTimeFromSQL(String sql)
	{
		return Optional.empty();
	}
	
	public Optional<LocalDate> getLastRewardDate(UUID user) throws SQLException
	{
		if (userExists(user))
		{
			Optional<ResultSet> rso = query("SELECT last_login_reward FROM users WHERE uuid = '"+user+"';");
			close();
			if (rso.isPresent())
			{
				return Optional.of(FormatUtil.sQLDateToLocalDate(rso.get().getDate(1)));
			}
		}
		return Optional.empty();
	}
	
	public boolean userExists(UUID user)
	{
		return execute("SELECT * FROM USERS WHERE uuid = '"+user+"';");
	}
	
	public void setUpUser(UUID user)
	{
		execute("INSERT INTO USERS(uuid, xp) VALUES('"+user+"', 0);");
	}

	public void test ()
	{

	}
}
