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

public class SQLQueryManager {

	public static String show_tables(String subType, String schema) throws Exception {
		
		switch (subType) {
		case "AS400":
			
			return "select distinct TABLE_NAME from QSYS2.SYSTABLES WHERE TABLE_SCHEMA='"+schema+"' ORDER BY TABLE_NAME";
			
		case "SQLServer":
			
			return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE' ORDER BY TABLE_NAME";
			
		case "MySQL":
			
			return "show tables";
			
		case "Derby":
			
			return "select t.tablename  \n" + 
					"     from sys.systables t, sys.sysschemas s  \n" + 
					"     where t.schemaid = s.schemaid \n" + 
					"          and t.tabletype = 'T' \n" + 
					"          and s.schemaname = '"+schema+"' \n" + 
					"     order by t.tablename";
				
		case "PostgreSQL":
			
			return "SELECT\n" + 
					"    table_name\n" + 
					"FROM\n" + 
					"    information_schema.tables\n" + 
					"WHERE\n" + 
					"    table_type = 'BASE TABLE'\n" + 
					"    and table_schema = '"+schema+"'"
							+ " ORDER BY table_name;";
				
		case "Oracle":
			
			return "SELECT\n" + 
					"    table_name\n" + 
					"FROM\n" + 
					"    all_tables\n" + 
					"WHERE\n" + 
					"    owner = '"+schema+"'"
							+ " ORDER BY table_name";
				
		case "H2":
			
			return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='"+schema+"' ORDER BY TABLE_NAME";
				
		case "HSQL":
			
			return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.SYSTEM_TABLES WHERE TABLE_SCHEM='"+schema+"' ORDER BY TABLE_NAME";
				
		case "DB2":
			
			return "select distinct name from SYSIBM.SYSTABLES WHERE type = 'T' and creator = '"+schema+"' ORDER BY name";
			
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
		
		switch (subType) {
		case "AS400":
			
			return "select c.COLUMN_NAME,"
					+ "c.DATA_TYPE, "
					+ "c.IS_NULLABLE, "
					+ "c.LENGTH, "
					+ "c.NUMERIC_PRECISION, "
					+ "c.NUMERIC_SCALE, "
					+ "c.COLUMN_DEFAULT, "
					+ "c.COLUMN_TEXT,"
					+ "k.asc_or_desc"
					+ " from qsys2.syscolumns c\n" + 
					"  join qsys2.systables    t\n" + 
					"    on c.table_schema = t.table_schema\n" + 
					"   and c.table_name   = t.table_name\n" + 
					"  left outer join sysibm.sqlstatistics k\n" + 
					"    on c.table_schema = k.table_schem\n" + 
					"   and c.table_name   = k.table_name\n" + 
					"   and c.table_name   = k.index_name\n" + 
					"   and c.column_name  = k.column_name where c.table_schema = '"+schema+"' and c.table_name = '"+tablename+"'";
			
		case "SQLServer":
			
			return "SELECT COLUMN_NAME, "
					+ "DATA_TYPE, "
					+ "IS_NULLABLE, "
					+ "CHARACTER_MAXIMUM_LENGTH, "
					+ "NUMERIC_PRECISION, "
					+ "NUMERIC_SCALE, "
					+ "COLUMN_DEFAULT, "
					+ "(CASE \n" + 
					"                  WHEN " + "COLS.COLUMN_NAME in (SELECT Col.Column_Name from \n" + 
							"    INFORMATION_SCHEMA.TABLE_CONSTRAINTS Tab, \n" + 
							"    INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE Col \n" + 
							"WHERE \n" + 
							"    Col.Constraint_Name = Tab.Constraint_Name\n" + 
							"    AND Col.Table_Name = Tab.Table_Name\n" + 
							"    AND Constraint_Type = 'PRIMARY KEY'\n" + 
							"    AND Col.Table_Name = '"+tablename+"')" + 
					"                     THEN 1 \n" + 
					"                  ELSE 0 \n" + 
					"             END) AS PRI "
					+ "FROM INFORMATION_SCHEMA.COLUMNS COLS" + 
					" WHERE TABLE_NAME = '"+tablename+"'";
			
		case "MySQL":
			
			return "desc `"+tablename+"`";
			
		case "H2":
			
			//c.schema_name, c.column_name, c.column_name, c.column_name
			return "SELECT \n" + 
					"		A.column_name, \n" + 
					"		A.type_name, \n" + 
					"		A.is_nullable, \n" + 
					"		A.CHARACTER_MAXIMUM_LENGTH, \n" + 
					"		A.numeric_precision, \n" + 
					"		A.numeric_scale, \n" + 
					"		A.column_default, \n" + 
					"		A.sequence_name, \n" + 
					"		B.constraint_type\n" + 
					"	FROM INFORMATION_SCHEMA.COLUMNS A\n" + 
					"	LEFT JOIN INFORMATION_SCHEMA.CONSTRAINTS B\n" + 
					"		ON \n" + 
					"			A.TABLE_SCHEMA=B.TABLE_SCHEMA and \n" + 
					"			A.TABLE_NAME=B.TABLE_NAME and \n" + 
					"			A.COLUMN_NAME=B.column_list\n" + 
					"	WHERE \n" + 
					"		A.table_schema = '"+schema+"' and \n" + 
					"		A.table_name = '"+tablename+"'";
			
		case "HSQL":
			
			//c.schema_name, c.column_name, c.column_name, c.column_name
			return "SELECT \n" + 
					"		A.COLUMN_NAME, \n" + 
					"		A.DATA_TYPE, \n" + 
					"		A.IS_NULLABLE, \n" + 
					"		A.CHARACTER_MAXIMUM_LENGTH, \n" + 
					"		A.NUMERIC_PRECISION, \n" + 
					"		A.NUMERIC_SCALE, \n" + 
					"		A.COLUMN_DEFAULT, \n" + 
					"		C.CONSTRAINT_TYPE\n" + 
					"	FROM INFORMATION_SCHEMA.COLUMNS A\n" + 
					"	LEFT JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE B\n" +
					"		ON \n" + 
					"			A.TABLE_SCHEMA=B.TABLE_SCHEMA and \n" + 
					"			A.TABLE_NAME=B.TABLE_NAME and \n" + 
					"			A.COLUMN_NAME=B.COLUMN_NAME and\n" + 
					"			B.CONSTRAINT_NAME like 'SYS_PK_%'\n" + 
					"	LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS C\n" + 
					"		ON \n" + 
					"			B.CONSTRAINT_CATALOG=C.CONSTRAINT_CATALOG and \n" + 
					"			B.CONSTRAINT_SCHEMA=C.CONSTRAINT_SCHEMA and \n" + 
					"			B.CONSTRAINT_NAME=C.CONSTRAINT_NAME \n" + 
					"			and C.CONSTRAINT_TYPE='PRIMARY KEY'"+
					"	WHERE \n" + 
					"		A.TABLE_SCHEMA = '"+schema+"' and \n" + 
					"		A.TABLE_NAME = '"+tablename+"'";
			
		case "PostgreSQL":
			
			//c.schema_name, c.column_name, c.column_name, c.column_name
			return "SELECT c.column_name, c.data_type, c.is_nullable, tc.constraint_type, c.column_default, c.character_maximum_length, c.character_octet_length, c.numeric_precision, c.numeric_scale, c.datetime_precision, c.interval_type, c.interval_precision\n" + 
			"FROM INFORMATION_SCHEMA.columns c \n" + 
			"LEFT JOIN INFORMATION_SCHEMA.constraint_column_usage AS ccu ON c.table_schema = ccu.table_schema AND c.table_name = ccu.table_name AND c.column_name = ccu.column_name \n" + 
			"LEFT JOIN INFORMATION_SCHEMA.table_constraints AS tc ON c.table_schema = tc.table_schema AND c.table_name = tc.table_name AND ccu.constraint_name = tc.constraint_name\n" + 
			"where concat(c.table_schema, '.', c.table_name)='"+schema+"."+tablename+"';";
			
		case "Oracle":
			
			return "select \n" + 
					"user_tab_columns.COLUMN_NAME, user_tab_columns.DATA_TYPE, user_tab_columns.DATA_LENGTH, user_tab_columns.DATA_PRECISION, user_tab_columns.DATA_SCALE, user_tab_columns.NULLABLE, user_tab_columns.COLUMN_ID, user_tab_columns.DEFAULT_LENGTH, user_tab_columns.DATA_DEFAULT, user_tab_columns.NUM_DISTINCT, user_cons_columns.constraint_name \n" + 
					"from user_tab_columns\n" + 
					"LEFT JOIN user_cons_columns ON user_cons_columns.table_name = user_tab_columns.table_name and user_cons_columns.column_name = user_tab_columns.column_name \n" + 
					"LEFT JOIN user_constraints ON user_constraints.table_name = user_cons_columns.table_name and user_constraints.constraint_name = user_cons_columns.constraint_name and user_constraints.constraint_type = 'P'\n" + 
					"WHERE user_tab_columns.table_name ='"+tablename+"'";
			
		case "DB2":
			
			return "Select name, coltype, nulls, length, scale, colno, keyseq, default from Sysibm.syscolumns where TBCREATOR = '"+schema+"' and tbname = '"+tablename+"'";
			
		case "Derby":
			
			return "select sc.columnname, sc.columndatatype, sc.columndefault, sc.autoincrementvalue\n" + 
					"	from sys.syscolumns sc\n" + 
					"	inner join sys.systables st on sc.REFERENCEID=st.TABLEID\n" + 
					"	inner join sys.sysschemas sh on st.SCHEMAID=sh.SCHEMAID\n" + 
					"	where \n" + 
					"	sh.schemaname = '"+schema+"' and st.tablename = '"+tablename+"' and st.tabletype='T'";
			
		default:
			
			throw new Exception("never used = "+subType);
			
		}
		
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
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "SQLServer":
			
			if (rs.getString(8).toUpperCase().equals("1")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "MySQL":
			
			if (rs.getString(4).toUpperCase().equals("PRI")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "PostgreSQL":
			
			if (rs.getString(4)!=null && rs.getString(4).toUpperCase().equals("PRIMARY KEY")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "Derby":
			
			if (rs.getString(4)!=null && !rs.getString(4).equals("")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "H2":
			
			if (rs.getString(9)!=null && rs.getString(9).toUpperCase().equals("PRIMARY KEY")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "HSQL":
			
			if (rs.getString(8)!=null && rs.getString(8).equals("PRIMARY KEY")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "DB2":
			
			if (rs.getString(7)!=null && rs.getString(7).toUpperCase().equals("1")) {
				return "		parameter add \""+param+rs.getString(1)+"\" \"\" 0;\n"+
				"	-> \"["+rs.getString(1)+"]\" (parameter get value \""+param+rs.getString(1)+"\");\n";
			} else return "";
			
		case "Oracle":
			
			if (rs.getString(11)!=null && !rs.getString(11).toUpperCase().equals("")) {
				return "		parameter add \""+param+rs.getString(1).replace("#", "").replace("$", "")+"\" \"\" 0;\n"+
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
