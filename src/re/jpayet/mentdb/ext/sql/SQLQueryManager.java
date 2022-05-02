/**
 * Project: MentDB
 * License: GPL v_3
 * Description: Mentalese Database Engine
 * Website: https://www.mentdb.org
 * Twitter: https://twitter.com/mentalese_db
 * Facebook: https://www.facebook.com/mentdb
 * Author: Jimmitry Payet
 * Mail: contact@mentdb.org
 * Locality: Reunion Island (French)
 */

package re.jpayet.mentdb.ext.sql;

import java.sql.ResultSet;

import re.jpayet.mentdb.core.db.Database;

public class SQLQueryManager {

	public static String show_tables(String subType, String schema) throws Exception {
		
		return Database.execute_admin_mql(null, "execute \"db.table.show.get\" \"[type]\" \""+(subType.replace("\"", "\\\""))+"\" \"[schema]\" \""+(schema.replace("\"", "\\\""))+"\";");
		
	}

	public static String show_data_count(String subType, String schema, String tablename) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "SELECT count(*) FROM "+tablename;
			
		case "SQLServer":
			
			return "SELECT count(*) FROM "+tablename;
			
		case "MySQL":
			
			return "SELECT count(*) FROM `"+tablename+"`";
			
		case "PostgreSQL":
			
			return "SELECT count(*) FROM "+schema+"."+tablename;
			
		case "Oracle":
			
			return "SELECT count(*) FROM "+schema+"."+tablename;
			
		case "Derby":
			
			return "SELECT count(*) FROM "+schema+"."+tablename;
			
		case "H2":
			
			return "SELECT count(*) FROM "+schema+"."+tablename;
			
		case "HSQL":
			
			return "SELECT count(*) FROM "+schema+"."+tablename;
			
		case "DB2":
			
			return "SELECT count(*) FROM "+schema+"."+tablename;
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String show_data(String subType, String schema, String tablename) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "SELECT * FROM "+tablename+" FETCH FIRST 500 ROWS ONLY";
			
		case "SQLServer":
			
			return "SELECT TOP 500 * FROM "+tablename;
			
		case "MySQL":
			
			return "SELECT * FROM `"+tablename+"` LIMIT 0, 500";
			
		case "PostgreSQL":
			
			return "SELECT * FROM "+schema+"."+tablename+" OFFSET 0 LIMIT 500";
			
		case "Oracle":
			
			return "SELECT * FROM "+schema+"."+tablename+" WHERE ROWNUM <= 500";
			
		case "Derby":
			
			return "SELECT * FROM "+schema+"."+tablename+" FETCH FIRST 500 ROWS ONLY";
			
		case "H2":
			
			return "SELECT * FROM "+schema+"."+tablename+" LIMIT 0, 500";
			
		case "HSQL":
			
			return "SELECT * FROM "+schema+"."+tablename+" LIMIT 0, 500";
			
		case "DB2":
			
			return "SELECT * FROM "+schema+"."+tablename+" FETCH FIRST 500 ROWS ONLY";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String desc_tables(String subType, String schema, String tablename) throws Exception {
		
		return Database.execute_admin_mql(null, "execute \"db.table.show_desc.get\" \"[type]\" \""+subType+"\" \"[schema]\" \""+schema+"\" \"[table]\" \""+tablename+"\"");
		
	}

	public static String analyse_db_count_tables(String subType) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "";
			
		case "SQLServer":
			
			return "";
			
		case "MySQL":
			
			return "";
			
		case "PostgreSQL":
			
			return "";
			
		case "Oracle":
			
			return "";
			
		case "H2":
			
			return "";
			
		case "HSQL":
			
			return "";
			
		case "DB2":
			
			return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_create_column(String subType, ResultSet rs) throws Exception {
		
		String col = "";
		
		switch (subType) {
		case "AS400":
			
			col = "\n	"+rs.getString(1)+" "+rs.getString(2);
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				
				col += "("+rs.getString(4);
				if (rs.getString(6)!=null && !rs.getString(6).equals("0")) {
					col += ", "+rs.getString(6);
				}
				col += ")";
				
			}
			col += " ";
			
			if (rs.getString(3).equals("N")) {
				col+= "NOT NULL ";
				
				if (rs.getString(7)!=null) {
					col+= "DEFAULT "+rs.getString(7);
				}
				
			} else {
				col+= "NULL ";
				
				if (rs.getString(7)!=null) {
					col+= "DEFAULT "+rs.getString(7);
				} else {
					col+= "DEFAULT NULL";
				}
				
			}
			
			if (rs.getString(9)!=null) {
				
				if (col.endsWith(" ")) col += "PRIMARY KEY";
				else col += " PRIMARY KEY";
				
			}
			
			return col+",";
			
		case "SQLServer":
			
			col = "\n	"+rs.getString(1)+" "+rs.getString(2);
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				
				col += "("+rs.getString(4);
				if (rs.getString(6)!=null && !rs.getString(6).equals("0")) {
					col += ", "+rs.getString(6);
				}
				col += ")";
				
			}
			col += " ";
			
			if (rs.getString(3).equals("NO")) {
				col+= "NOT NULL ";
				
				if (rs.getString(7)!=null) {
					col+= "DEFAULT '"+rs.getString(7).replace("'", "''")+"'";
				}
				
			} else {
				col+= "NULL ";
				
				if (rs.getString(7)!=null) {
					col+= "DEFAULT '"+rs.getString(7).replace("'", "''")+"'";
				} else {
					col+= "DEFAULT NULL";
				}
				
			}
			
			if (rs.getString(8)!=null && rs.getString(8).equals("1")) {
				
				if (col.endsWith(" ")) col += "IDENTITY(1,1)";
				else col += " IDENTITY(1,1)";
				
			}
			
			return col+",";
			
		case "Oracle":
			
			col = "\n	"+rs.getString(1)+" "+rs.getString(2)+"("+rs.getString(3);
			
			if (rs.getString(5)!=null && !rs.getString(5).equals("")) {
				col += ", "+rs.getString(5);
			}
			
			col+= ") ";
			
			if (rs.getString(6).equals("N")) {
				col+= "NOT NULL ";
				
				if (rs.getString(9)!=null) {
					col+= "DEFAULT '"+rs.getString(9).replace("'", "''")+"'";
				}
				
			} else {
				col+= "NULL ";
				
				if (rs.getString(9)!=null) {
					col+= "DEFAULT '"+rs.getString(9).replace("'", "''")+"'";
				} else {
					col+= "DEFAULT NULL";
				}
				
			}
			
			return col+",";
			
		case "MySQL":
			
			col = "\n	`"+rs.getString(1)+"` "+rs.getString(2)+" ";
			
			if (rs.getString(3).equals("NO")) {
				col+= "NOT NULL ";
				
				if (rs.getString(5)!=null) {
					col+= "DEFAULT '"+rs.getString(5).replace("'", "''")+"'";
				}
				
			} else {
				col+= "NULL ";
				
				if (rs.getString(5)!=null) {
					col+= "DEFAULT '"+rs.getString(5).replace("'", "''")+"'";
				} else {
					col+= "DEFAULT NULL";
				}
				
			}
			
			if (rs.getString(6)!=null && !rs.getString(6).equals("")) {
				
				if (col.endsWith(" ")) col += rs.getString(6);
				else col += " "+rs.getString(6);
				
			}
			
			return col+",";
			
		case "PostgreSQL":
			
			boolean serial = false;
			if (rs.getString(5)!=null && rs.getString(5).startsWith("nextval(") && rs.getString(2).toLowerCase().equals("bigint")) {
				serial = true;
			}
			if (serial) col = "\n	"+rs.getString(1)+" bigserial ";
			else {
				col = "\n	"+rs.getString(1)+" "+rs.getString(2).replace("\"", "");
				if (rs.getString(6)!=null && !rs.getString(6).equals("")) {
					
					col += "("+rs.getString(6)+")";
					
				}
				if (rs.getString(2).toLowerCase().equals("numeric") && rs.getString(8)!=null && !rs.getString(8).equals("")) {
					
					col += "("+rs.getString(8);
					if (rs.getString(9)!=null && !rs.getString(9).equals("")) {
						col += ", "+rs.getString(9);
					}
					col += ")";
					
				}
				col += " ";
			}
			
			if (rs.getString(3).equals("NO")) {
				col+= "NOT NULL ";
				
				if (!serial) {
					if (rs.getString(5)!=null) {
						col+= "DEFAULT '"+rs.getString(5).replace("'", "''")+"'";
					}
				}
				
			} else {
				col+= "NULL ";
				
				if (!serial) {
					if (rs.getString(5)!=null) {
						col+= "DEFAULT '"+rs.getString(5).replace("'", "''")+"'";
					} else {
						col+= "DEFAULT NULL";
					}
				}
				
			}
			
			return col+",";
			
		case "DB2":
			
			col = "\n	"+rs.getString(1)+" "+rs.getString(2).replace(" ", "");
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				
				col += "("+rs.getString(4);
				if (rs.getString(5)!=null && !rs.getString(5).equals("0")) {
					col += ", "+rs.getString(5);
				}
				col += ")";
				
			}
			col += " ";
			
			if (rs.getString(3).equals("N")) {
				col+= "NOT NULL ";
				
				if (rs.getString(8)!=null) {
					col+= "DEFAULT '"+rs.getString(8).replace("'", "''")+"'";
				}
				
			} else {
				col+= "NULL ";
				
				if (rs.getString(8)!=null) {
					col+= "DEFAULT '"+rs.getString(8).replace("'", "''")+"'";
				} else {
					col+= "DEFAULT NULL";
				}
				
			}
			
			if (rs.getString(7)!=null && rs.getString(7).equals("1")) {
				
				if (col.endsWith(" ")) col += "GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 5)";
				else col += " GENERATED ALWAYS AS IDENTITY (START WITH 100, INCREMENT BY 5)";
				
			}
			
			return col+",";
			
		case "H2":
			
			serial = false;
			if (rs.getString(7)!=null && rs.getString(7).startsWith("(NEXT VALUE")) {
				serial = true;
			}
			col = "\n	"+rs.getString(1)+" "+rs.getString(2).replace(" ", "");
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				
				col += "("+rs.getString(4);
				if (rs.getString(6)!=null && !rs.getString(6).equals("0")) {
					col += ", "+rs.getString(6);
				}
				col += ")";
				
			}
			col += " ";
			
			if (rs.getString(3).equals("NO")) {
				col+= "NOT NULL ";
				
				if (!serial) {
					if (rs.getString(7)!=null) {
						col+= "DEFAULT '"+rs.getString(7).replace("'", "''")+"'";
					}
				}
				
			} else {
				col+= "NULL ";
				
				if (!serial) {
					if (rs.getString(7)!=null) {
						col+= "DEFAULT '"+rs.getString(7).replace("'", "''")+"'";
					} else {
						col+= "DEFAULT NULL";
					}
				}
				
			}
			
			if (rs.getString(8)!=null && !rs.getString(8).equals("")) {
				
				if (col.endsWith(" ")) col += "AUTO_INCREMENT";
				else col += " AUTO_INCREMENT";
				
			}
			
			return col+",";
			
		case "HSQL":
			
			col = "\n	"+rs.getString(1)+" "+rs.getString(2).replace(" ", "");
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				
				col += "("+rs.getString(4)+")";
				
			}
			if (rs.getString(5)!=null && !rs.getString(5).equals("")) {
				
				col += "("+rs.getString(5);
				if (rs.getString(6)!=null && !rs.getString(6).equals("")) {
					col += ", "+rs.getString(6);
				}
				col += ")";
				
			}
			col += " ";
			
			if (rs.getString(3).equals("NO")) {
				col+= "NOT NULL ";
				
				if (rs.getString(7)!=null) {
					col+= "DEFAULT '"+rs.getString(7).replace("'", "''")+"'";
				}
				
			} else {
				col+= "NULL ";
			
				if (rs.getString(7)!=null) {
					col+= "DEFAULT '"+rs.getString(7).replace("'", "''")+"'";
				} else {
					col+= "DEFAULT NULL";
				}
				
			}
			
			return col+",";
			
		case "Derby":
			
			col = "\n	"+rs.getString(1)+" "+rs.getString(2)+" ";
			
			if (rs.getString(3)!=null) {
				col+= "DEFAULT '"+rs.getString(3).replace("'", "''")+"'";
			}
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				
				if (col.endsWith(" ")) col += "AUTO_INCREMENT";
				else col += " AUTO_INCREMENT";
				
			}
			
			return col+",";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_create_column_pri(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "SQLServer":
			
			if (rs.getString(8).equals("1")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "MySQL":
			
			if (rs.getString(4).equals("PRI")) {
				return "\n	`"+rs.getString(1)+"` ,";
			}
			
			return "";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).equals("PRIMARY KEY")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).equals("PRIMARY KEY")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).equals("1")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).equals("")) {
				return "\n	"+rs.getString(1)+" ,";
			}
			
			return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_expression(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "\n				"+rs.getString(1)+",";
			
		case "SQLServer":
			
			return "\n				"+rs.getString(1)+",";
			
		case "MySQL":
			
			return "\n				`"+rs.getString(1)+"`,";
			
		case "PostgreSQL":
			
			return "\n				"+rs.getString(1)+",";
			
		case "Derby":
			
			return "\n				"+rs.getString(1)+",";
			
		case "H2":
			
			return "\n				"+rs.getString(1)+",";
			
		case "HSQL":
			
			return "\n				"+rs.getString(1)+",";
			
		case "Oracle":
			
			return "\n				"+rs.getString(1)+",";
			
		case "DB2":
			
			return "\n				"+rs.getString(1)+",";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_update_expression(String subType, ResultSet rs, String subfixe) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "";
			} else return "\n				`"+rs.getString(1)+"`=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1).replace("#", "").replace("$", "")+"]) \" ,";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_update_expression(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "";
			} else return "\n				`"+rs.getString(1)+"`=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" ,";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "";
			} else return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1).replace("#", "").replace("$", "")+"]) \" ,";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_where_pk(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "\n				`"+rs.getString(1)+"`=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null &&rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "H2":
			
			if (rs.getString(9)!=null &&rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+rs.getString(1).replace("#", "").replace("$", "")+"]) \" and ";
			} else return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_where_pk(String subType, ResultSet rs, String subfixe) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "\n				`"+rs.getString(1)+"`=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null &&rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "H2":
			
			if (rs.getString(9)!=null &&rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1)+"]) \" and ";
			} else return "";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "\n				"+rs.getString(1)+"=\" (sql encode ["+subfixe+rs.getString(1).replace("#", "").replace("$", "")+"]) \" and ";
			} else return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_insert_values(String subType, ResultSet rs, String subfixe) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "SQLServer":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "MySQL":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "PostgreSQL":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "Derby":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "H2":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "HSQL":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "DB2":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1)+"]) \" ,";
			
		case "Oracle":
			
			return "\n				\" (sql encode ["+subfixe+rs.getString(1).replace("#", "").replace("$", "")+"]) \" ,";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_load_variables(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "";
			} else return "			[T_"+rs.getString(1)+"];\n";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "";
			} else return "			[T_"+rs.getString(1).replace("#", "").replace("$", "")+"];\n";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_script_param(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "  	(var \"["+rs.getString(1).replace("#", "").replace("$", "")+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			} else return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_script_param_init(String subType, ResultSet rs, String param) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			if (rs.getString(9)!=null) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\";\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "		parameter add \""+param+rs.getString(1).replace("#", "").replace("$", "")+"\" \"\";\n"+
				"	-> \"["+rs.getString(1).replace("#", "").replace("$", "")+"]\" (parameter get value \""+param+rs.getString(1).replace("#", "").replace("$", "")+"\");\n";
			} else return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_insert_script_param(String subType, ResultSet rs) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "SQLServer":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "MySQL":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "PostgreSQL":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "Derby":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "H2":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "HSQL":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "DB2":
			
			return "  	(var \"["+rs.getString(1)+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		case "Oracle":
			
			return "  	(var \"["+rs.getString(1).replace("#", "").replace("$", "")+"]\" {true} \"description ...\" is_null:false is_empty:false \"example ...\")\n";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_tablename(String subType, String schema, String tablename) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return ""+tablename+"";
			
		case "SQLServer":
			
			return ""+tablename+"";
			
		case "MySQL":
			
			return "`"+tablename+"`";
			
		case "PostgreSQL":
			
			return schema+"."+tablename;
			
		case "Derby":
			
			return schema+"."+tablename;
			
		case "H2":
			
			return schema+"."+tablename;
			
		case "HSQL":
			
			return schema+"."+tablename;
			
		case "DB2":
			
			return schema+"."+tablename;
			
		case "Oracle":
			
			return schema+"."+tablename;
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_limit(String subType, boolean addWhere) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "			 FETCH FIRST 500 ROWS ONLY;";
			
		case "SQLServer":
			
			return "";
			
		case "MySQL":
			
			return "			 LIMIT 0, 100;";
			
		case "H2":
			
			return "			 LIMIT 0, 100;";
			
		case "HSQL":
			
			return "			 LIMIT 0, 100;";
			
		case "PostgreSQL":
			
			return "			 OFFSET 0 LIMIT 500;";
			
		case "DB2":
			
			return "			 FETCH FIRST 500 ROWS ONLY;";
			
		case "Derby":
			
			return "			 FETCH FIRST 500 ROWS ONLY;";
			
		case "Oracle":
			
			if (addWhere) return "			 WHERE ROWNUM <= 500";
			else return "			 and ROWNUM <= 500";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

	public static String scrud_select_limit2(String subType, boolean addWhere) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "";
			
		case "SQLServer":
			
			return "TOP 100";
			
		case "MySQL":
			
			return "";
			
		case "H2":
			
			return "";
			
		case "HSQL":
			
			return "";
			
		case "PostgreSQL":
			
			return "";
			
		case "DB2":
			
			return "";
			
		case "Derby":
			
			return "";
			
		case "Oracle":
			
			return "";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
	}

}
