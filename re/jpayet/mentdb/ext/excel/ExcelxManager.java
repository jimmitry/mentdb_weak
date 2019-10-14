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

package re.jpayet.mentdb.ext.excel;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.ext.env.EnvManager;
import re.jpayet.mentdb.ext.fx.AtomFx;
import re.jpayet.mentdb.ext.fx.FileFx;
import re.jpayet.mentdb.ext.json.JsonManager;

public class ExcelxManager {

	public static String create(EnvManager env, String excelId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the excel document id already exist
		if (exist(env, excelId).equals("1")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' already exist.");
			
		}
		
		//Try to create the workbook
		try {

			env.excelx.put(excelId, new XSSFWorkbook());
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}

	public static String load_from_file(EnvManager env, String excelId, String path) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the excel document id already exist
		if (exist(env, excelId).equals("1")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' already exist.");
			
		}
		
		//Generate an error if the file path does not exist or is not a file
		if (FileFx.is_directory(path).equals("1")) {
			
			throw new Exception("Sorry, the file path does not exist or is not a file.");
			
		}
		
		//Check if the file is in Excel format
		try {

			env.excelx.put(excelId, new XSSFWorkbook(new FileInputStream(path)));
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, the file is not an Excel file. "+e.getMessage());
			
		}
		
		return result;
		
	}

	public static String exist(EnvManager env, String excelId) throws Exception {
		
		//Check if the document already exist
		if (env.excelx.containsKey(excelId)) {
			
			return "1";
			
		} else {
			
			return "0";
			
		}
		
	}
	
	public static String sheet_add(EnvManager env, String excelId, String sheetName) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Try to create the sheet
		try {

			((XSSFWorkbook) env.excelx.get(excelId)).createSheet(sheetName);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	public static String sheet_delete(EnvManager env, String excelId, String sheetName) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Try to remove the sheet
		try {

			((XSSFWorkbook) env.excelx.get(excelId)).removeSheetAt(
					((XSSFWorkbook) env.excelx.get(excelId)).getSheetIndex(sheetName)
			);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray sheet_show(EnvManager env, String excelId) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Try to create the sheet
		try {

			//Parse all sheets
			for(int i=0;i<((XSSFWorkbook) env.excelx.get(excelId)).getNumberOfSheets();i++) {
				
				//Get the current sheet
				result.add(((XSSFWorkbook) env.excelx.get(excelId)).getSheetName(i));
			
			}
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, "+e.getMessage()+".");
			
		}
		
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public static JSONArray show(EnvManager env) throws Exception {
		
		//Initialization
		JSONArray result = new JSONArray();
		
		for(String k : env.excelx.keySet()) {
			
			result.add(k);
			
		}
		
		return result;
		
	}

	public static String sheet_max_row(EnvManager env, String excelId, String sheetName) throws Exception {
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Generate an error if the sheet name is not valid
		if ((sheetName==null) || (sheetName.equals(""))) {
			
			throw new Exception("Sorry, the sheet name cannot be null or empty.");
			
		}
		
		//Get the sheet
		XSSFSheet sh = null;
		
		//Try to get the sheet
		try {
			
			sh = ((XSSFWorkbook) env.excelx.get(excelId)).getSheet(sheetName);
			
			if (sh==null) throw new Exception("err");
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot get the sheet with the name '"+sheetName+"'.");
			
		}
		
		return ""+sh.getLastRowNum();
		
	}

	@SuppressWarnings("unchecked")
	public static JSONObject cell_ref(String cell) throws Exception {
		
		//Initialization
		CellReference cellRef = new CellReference(cell);
		
		JSONObject result = new JSONObject();

		result.put("row", cellRef.getRow());
		result.put("col", cellRef.getCol());
		
		return result;
		
	}

	public static void cell_set(EnvManager env, String excelId, String sheetName, String indexRow, String indexCell, String value, String type) throws Exception {
		
		//Initialization
		int indexRowNumber = 0, indexCellNumber = 0;
		
		if (value==null) {
			
			value = "";
			
		}
		
		//Generate an error if the index of the cell is not a number
		try {
			
			indexCellNumber = Integer.parseInt(indexCell);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexCellNumber<0) {
			
			throw new Exception("Sorry, the index of the cell cannot be < 0.");
			
		}
		
		//Generate an error if the index of the row is not a number
		try {
			
			indexRowNumber = Integer.parseInt(indexRow);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexRowNumber<0) {
			
			throw new Exception("Sorry, the index of the row cannot be < 0.");
			
		}
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Generate an error if the sheet name is not valid
		if ((sheetName==null) || (sheetName.equals(""))) {
			
			throw new Exception("Sorry, the sheet name cannot be null or empty.");
			
		}
		
		//Generate an error if the type is not valid
		if ((type==null) || (type.equals(""))) {
			
			throw new Exception("Sorry, the type cannot be null or empty.");
			
		}
		
		type = type.toLowerCase();
		
		if (!type.equals("str") && !type.equals("num") && !type.equals("bool") && !type.equals("datetime") && !type.equals("formula") && !type.equals("blank")) {
			
			throw new Exception("Sorry, the type must be 'str|num|bool|datetime|formula|blank'.");
			
		}
		
		//Get the sheet
		XSSFSheet sh = null;
		XSSFWorkbook wb = ((XSSFWorkbook) env.excelx.get(excelId));
		
		//Try to get the sheet
		try {
			sh = wb.getSheet(sheetName);
			if (sh==null) throw new Exception("err");
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot get the sheet with the name '"+sheetName+"'.");
			
		}
		
		//Get the current cell
		XSSFRow row = sh.getRow(indexRowNumber);
		
		if (row == null) {
			
			row = sh.createRow(indexRowNumber);
		
		}
		
		XSSFCell cell = row.getCell(indexCellNumber, XSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		
		if (type.equals("bool")) {
			
			if (value.equals("1")) cell.setCellValue(true);
			else cell.setCellValue(false);
			
		} else if (type.equals("str")) {
			
			cell.setCellValue(value);
			
		} else if (type.equals("num")) {
			
			cell.setCellValue(Double.parseDouble(value));
			
		} else if (type.equals("datetime")) {
			
			SimpleDateFormat typeFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
			
			Date maDateFinale = typeFormat.parse(value);
			
			cell.setCellValue(maDateFinale);
			
		} else if (type.equals("blank")) {
			
			String v = null;
			cell.setCellValue(v);
			
		} else if (type.equals("formula")) {
			
			cell.setCellFormula(value);
			
		}
		
	}

	public static void cell_format(EnvManager env, String excelId, String sheetName, String indexRow, String indexCell, String config, String format) throws Exception {
		
		//Initialization
		int indexRowNumber = 0, indexCellNumber = 0;
		
		JSONObject conf = (JSONObject) JsonManager.load(config);
		
		//Generate an error if the index of the cell is not a number
		try {
			
			indexCellNumber = Integer.parseInt(indexCell);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexCellNumber<0) {
			
			throw new Exception("Sorry, the index of the cell cannot be < 0.");
			
		}
		
		//Generate an error if the index of the row is not a number
		try {
			
			indexRowNumber = Integer.parseInt(indexRow);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexRowNumber<0) {
			
			throw new Exception("Sorry, the index of the row cannot be < 0.");
			
		}
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Generate an error if the sheet name is not valid
		if ((sheetName==null) || (sheetName.equals(""))) {
			
			throw new Exception("Sorry, the sheet name cannot be null or empty.");
			
		}
		
		//Get the sheet
		XSSFSheet sh = null;
		XSSFWorkbook wb = ((XSSFWorkbook) env.excelx.get(excelId));
		
		//Try to get the sheet
		try {
			sh = wb.getSheet(sheetName);
			if (sh==null) throw new Exception("err");
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot get the sheet with the name '"+sheetName+"'.");
			
		}
		
		//Get the current cell
		XSSFRow row = sh.getRow(indexRowNumber);
		
		if (row == null) {
			
			row = sh.createRow(indexRowNumber);
		
		}
		
		XSSFCell cell = row.getCell(indexCellNumber, XSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
		
		XSSFCellStyle cellStyle = wb.createCellStyle();
		cell.setCellStyle(cellStyle);
		
		if (cell.getCellTypeEnum()==CellType.STRING) {
			
			if (format!=null && !format.equals("")) {
				XSSFDataFormat form = wb.createDataFormat();
				cellStyle.setDataFormat(form.getFormat(format));
			}
			
		} else if (cell.getCellTypeEnum()==CellType.NUMERIC) {
			
			if (format!=null && !format.equals("")) {
				XSSFDataFormat form = wb.createDataFormat();
				cellStyle.setDataFormat(form.getFormat(format));
			}
			
		} else if (cell.getCellTypeEnum()==CellType.FORMULA) {
			
			if (format!=null && !format.equals("")) {
				XSSFDataFormat form = wb.createDataFormat();
				cellStyle.setDataFormat(form.getFormat(format));
			}
			
		}
		
		//Boolean config
		if (conf.containsKey("Hidden")) {
			if (((String) conf.get("Hidden")).equals("1")) cellStyle.setHidden(true);
			else cellStyle.setHidden(false);
		}
		if (conf.containsKey("Locked")) {
			if (((String) conf.get("Locked")).equals("1")) cellStyle.setLocked(true);
			else cellStyle.setLocked(false);
		}
		if (conf.containsKey("QuotePrefixed")) {
			if (((String) conf.get("QuotePrefixed")).equals("1")) cellStyle.setQuotePrefixed(true);
			else cellStyle.setQuotePrefixed(false);
		}
		if (conf.containsKey("ShrinkToFit")) {
			if (((String) conf.get("ShrinkToFit")).equals("1")) cellStyle.setShrinkToFit(true);
			else cellStyle.setShrinkToFit(false);
		}
		if (conf.containsKey("WrapText")) {
			if (((String) conf.get("WrapText")).equals("1")) cellStyle.setWrapText(true);
			else cellStyle.setWrapText(false);
		}
		
		//Horizontal alignment
		if (conf.containsKey("HorizontalAlignment")) {
			
			String horizontalAlignment = (String) conf.get("HorizontalAlignment");
			
			if (horizontalAlignment.toLowerCase().equals("center")) {
				cellStyle.setAlignment(HorizontalAlignment.CENTER);
			} else if (horizontalAlignment.toLowerCase().equals("center_selection")) {
				cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
			} else if (horizontalAlignment.toLowerCase().equals("distributed")) {
				cellStyle.setAlignment(HorizontalAlignment.DISTRIBUTED);
			} else if (horizontalAlignment.toLowerCase().equals("fill")) {
				cellStyle.setAlignment(HorizontalAlignment.FILL);
			} else if (horizontalAlignment.toLowerCase().equals("general")) {
				cellStyle.setAlignment(HorizontalAlignment.GENERAL);
			} else if (horizontalAlignment.toLowerCase().equals("justify")) {
				cellStyle.setAlignment(HorizontalAlignment.JUSTIFY);
			} else if (horizontalAlignment.toLowerCase().equals("left")) {
				cellStyle.setAlignment(HorizontalAlignment.LEFT);
			} else if (horizontalAlignment.toLowerCase().equals("right")) {
				cellStyle.setAlignment(HorizontalAlignment.RIGHT);
			}
			
		}
		
		//Vertical alignment
		if (conf.containsKey("VerticalAlignment")) {
			
			String verticalAlignment = (String) conf.get("VerticalAlignment");
			
			if (verticalAlignment.toLowerCase().equals("bottom")) {
				cellStyle.setVerticalAlignment(VerticalAlignment.BOTTOM);
			} else if (verticalAlignment.toLowerCase().equals("distributed")) {
				cellStyle.setVerticalAlignment(VerticalAlignment.DISTRIBUTED);
			} else if (verticalAlignment.toLowerCase().equals("center")) {
				cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			} else if (verticalAlignment.toLowerCase().equals("justify")) {
				cellStyle.setVerticalAlignment(VerticalAlignment.JUSTIFY);
			} else if (verticalAlignment.toLowerCase().equals("top")) {
				cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
			}
			
		}
		
		//Border bottom
		if (conf.containsKey("BorderBottom")) {
			
			String borderBottom = (String) conf.get("BorderBottom");
			
			if (borderBottom.toLowerCase().equals("dash_dot")) {
				cellStyle.setBorderBottom(BorderStyle.DASH_DOT);
			} else if (borderBottom.toLowerCase().equals("dash_dot_dot")) {
				cellStyle.setBorderBottom(BorderStyle.DASH_DOT_DOT);
			} else if (borderBottom.toLowerCase().equals("dashed")) {
				cellStyle.setBorderBottom(BorderStyle.DASHED);
			} else if (borderBottom.toLowerCase().equals("dotted")) {
				cellStyle.setBorderBottom(BorderStyle.DOTTED);
			} else if (borderBottom.toLowerCase().equals("double")) {
				cellStyle.setBorderBottom(BorderStyle.DOUBLE);
			} else if (borderBottom.toLowerCase().equals("hair")) {
				cellStyle.setBorderBottom(BorderStyle.HAIR);
			} else if (borderBottom.toLowerCase().equals("medium")) {
				cellStyle.setBorderBottom(BorderStyle.MEDIUM);
			} else if (borderBottom.toLowerCase().equals("medium_dash_dot")) {
				cellStyle.setBorderBottom(BorderStyle.MEDIUM_DASH_DOT);
			} else if (borderBottom.toLowerCase().equals("medium_dash_dot_dot")) {
				cellStyle.setBorderBottom(BorderStyle.MEDIUM_DASH_DOT_DOT);
			} else if (borderBottom.toLowerCase().equals("medium_dashed")) {
				cellStyle.setBorderBottom(BorderStyle.MEDIUM_DASHED);
			} else if (borderBottom.toLowerCase().equals("none")) {
				cellStyle.setBorderBottom(BorderStyle.NONE);
			} else if (borderBottom.toLowerCase().equals("slanted_dash_dot")) {
				cellStyle.setBorderBottom(BorderStyle.SLANTED_DASH_DOT);
			} else if (borderBottom.toLowerCase().equals("thick")) {
				cellStyle.setBorderBottom(BorderStyle.THICK);
			} else if (borderBottom.toLowerCase().equals("thin")) {
				cellStyle.setBorderBottom(BorderStyle.THIN);
			}
			
		}
		
		//Border top
		if (conf.containsKey("BorderTop")) {
			
			String borderTop = (String) conf.get("BorderTop");
			
			if (borderTop.toLowerCase().equals("dash_dot")) {
				cellStyle.setBorderTop(BorderStyle.DASH_DOT);
			} else if (borderTop.toLowerCase().equals("dash_dot_dot")) {
				cellStyle.setBorderTop(BorderStyle.DASH_DOT_DOT);
			} else if (borderTop.toLowerCase().equals("dashed")) {
				cellStyle.setBorderTop(BorderStyle.DASHED);
			} else if (borderTop.toLowerCase().equals("dotted")) {
				cellStyle.setBorderTop(BorderStyle.DOTTED);
			} else if (borderTop.toLowerCase().equals("double")) {
				cellStyle.setBorderTop(BorderStyle.DOUBLE);
			} else if (borderTop.toLowerCase().equals("hair")) {
				cellStyle.setBorderTop(BorderStyle.HAIR);
			} else if (borderTop.toLowerCase().equals("medium")) {
				cellStyle.setBorderTop(BorderStyle.MEDIUM);
			} else if (borderTop.toLowerCase().equals("medium_dash_dot")) {
				cellStyle.setBorderTop(BorderStyle.MEDIUM_DASH_DOT);
			} else if (borderTop.toLowerCase().equals("medium_dash_dot_dot")) {
				cellStyle.setBorderTop(BorderStyle.MEDIUM_DASH_DOT_DOT);
			} else if (borderTop.toLowerCase().equals("medium_dashed")) {
				cellStyle.setBorderTop(BorderStyle.MEDIUM_DASHED);
			} else if (borderTop.toLowerCase().equals("none")) {
				cellStyle.setBorderTop(BorderStyle.NONE);
			} else if (borderTop.toLowerCase().equals("slanted_dash_dot")) {
				cellStyle.setBorderTop(BorderStyle.SLANTED_DASH_DOT);
			} else if (borderTop.toLowerCase().equals("thick")) {
				cellStyle.setBorderTop(BorderStyle.THICK);
			} else if (borderTop.toLowerCase().equals("thin")) {
				cellStyle.setBorderTop(BorderStyle.THIN);
			}
			
		}
		
		//Border left
		if (conf.containsKey("BorderLeft")) {
			
			String borderLeft = (String) conf.get("BorderLeft");
			
			if (borderLeft.toLowerCase().equals("dash_dot")) {
				cellStyle.setBorderLeft(BorderStyle.DASH_DOT);
			} else if (borderLeft.toLowerCase().equals("dash_dot_dot")) {
				cellStyle.setBorderLeft(BorderStyle.DASH_DOT_DOT);
			} else if (borderLeft.toLowerCase().equals("dashed")) {
				cellStyle.setBorderLeft(BorderStyle.DASHED);
			} else if (borderLeft.toLowerCase().equals("dotted")) {
				cellStyle.setBorderLeft(BorderStyle.DOTTED);
			} else if (borderLeft.toLowerCase().equals("double")) {
				cellStyle.setBorderLeft(BorderStyle.DOUBLE);
			} else if (borderLeft.toLowerCase().equals("hair")) {
				cellStyle.setBorderLeft(BorderStyle.HAIR);
			} else if (borderLeft.toLowerCase().equals("medium")) {
				cellStyle.setBorderLeft(BorderStyle.MEDIUM);
			} else if (borderLeft.toLowerCase().equals("medium_dash_dot")) {
				cellStyle.setBorderLeft(BorderStyle.MEDIUM_DASH_DOT);
			} else if (borderLeft.toLowerCase().equals("medium_dash_dot_dot")) {
				cellStyle.setBorderLeft(BorderStyle.MEDIUM_DASH_DOT_DOT);
			} else if (borderLeft.toLowerCase().equals("medium_dashed")) {
				cellStyle.setBorderLeft(BorderStyle.MEDIUM_DASHED);
			} else if (borderLeft.toLowerCase().equals("none")) {
				cellStyle.setBorderLeft(BorderStyle.NONE);
			} else if (borderLeft.toLowerCase().equals("slanted_dash_dot")) {
				cellStyle.setBorderLeft(BorderStyle.SLANTED_DASH_DOT);
			} else if (borderLeft.toLowerCase().equals("thick")) {
				cellStyle.setBorderLeft(BorderStyle.THICK);
			} else if (borderLeft.toLowerCase().equals("thin")) {
				cellStyle.setBorderLeft(BorderStyle.THIN);
			}
			
		}
		
		//Border right
		if (conf.containsKey("BorderRight")) {
			
			String borderRight = (String) conf.get("BorderRight");
			
			if (borderRight.toLowerCase().equals("dash_dot")) {
				cellStyle.setBorderRight(BorderStyle.DASH_DOT);
			} else if (borderRight.toLowerCase().equals("dash_dot_dot")) {
				cellStyle.setBorderRight(BorderStyle.DASH_DOT_DOT);
			} else if (borderRight.toLowerCase().equals("dashed")) {
				cellStyle.setBorderRight(BorderStyle.DASHED);
			} else if (borderRight.toLowerCase().equals("dotted")) {
				cellStyle.setBorderRight(BorderStyle.DOTTED);
			} else if (borderRight.toLowerCase().equals("double")) {
				cellStyle.setBorderRight(BorderStyle.DOUBLE);
			} else if (borderRight.toLowerCase().equals("hair")) {
				cellStyle.setBorderRight(BorderStyle.HAIR);
			} else if (borderRight.toLowerCase().equals("medium")) {
				cellStyle.setBorderRight(BorderStyle.MEDIUM);
			} else if (borderRight.toLowerCase().equals("medium_dash_dot")) {
				cellStyle.setBorderRight(BorderStyle.MEDIUM_DASH_DOT);
			} else if (borderRight.toLowerCase().equals("medium_dash_dot_dot")) {
				cellStyle.setBorderRight(BorderStyle.MEDIUM_DASH_DOT_DOT);
			} else if (borderRight.toLowerCase().equals("medium_dashed")) {
				cellStyle.setBorderRight(BorderStyle.MEDIUM_DASHED);
			} else if (borderRight.toLowerCase().equals("none")) {
				cellStyle.setBorderRight(BorderStyle.NONE);
			} else if (borderRight.toLowerCase().equals("slanted_dash_dot")) {
				cellStyle.setBorderRight(BorderStyle.SLANTED_DASH_DOT);
			} else if (borderRight.toLowerCase().equals("thick")) {
				cellStyle.setBorderRight(BorderStyle.THICK);
			} else if (borderRight.toLowerCase().equals("thin")) {
				cellStyle.setBorderRight(BorderStyle.THIN);
			}
			
		}
		
		if (conf.containsKey("BottomBorderColor")) {
			String value = (String) conf.get("BottomBorderColor");
			cellStyle.setBottomBorderColor(new XSSFColor(new Color(
					Integer.parseInt(AtomFx.get(value, "1", ",")), 
					Integer.parseInt(AtomFx.get(value, "2", ",")), 
					Integer.parseInt(AtomFx.get(value, "3", ",")))));
			
		}
		
		if (conf.containsKey("LeftBorderColor")) {
			String value = (String) conf.get("LeftBorderColor");
			cellStyle.setLeftBorderColor(new XSSFColor(new Color(
					Integer.parseInt(AtomFx.get(value, "1", ",")), 
					Integer.parseInt(AtomFx.get(value, "2", ",")), 
					Integer.parseInt(AtomFx.get(value, "3", ",")))));
		}
		
		if (conf.containsKey("RightBorderColor")) {
			String value = (String) conf.get("RightBorderColor");
			cellStyle.setRightBorderColor(new XSSFColor(new Color(
					Integer.parseInt(AtomFx.get(value, "1", ",")), 
					Integer.parseInt(AtomFx.get(value, "2", ",")), 
					Integer.parseInt(AtomFx.get(value, "3", ",")))));
		}
		
		if (conf.containsKey("TopBorderColor")) {
			String value = (String) conf.get("TopBorderColor");
			cellStyle.setTopBorderColor(new XSSFColor(new Color(
					Integer.parseInt(AtomFx.get(value, "1", ",")), 
					Integer.parseInt(AtomFx.get(value, "2", ",")), 
					Integer.parseInt(AtomFx.get(value, "3", ",")))));
		}
		
		if (conf.containsKey("Font")) {
			
			JSONObject values = (JSONObject) conf.get("Font");
			String fontname = (String) values.get("fontName");
			String size = (String) values.get("size");
			String color = (String) values.get("color");
			String bold = (String) values.get("bold");
			String italic = (String) values.get("italic");
			
			XSSFFont xSSFFont = wb.createFont();
			xSSFFont.setFontName(fontname);
			xSSFFont.setFontHeightInPoints(Short.parseShort(size));

			xSSFFont.setColor(new XSSFColor(new Color(
					Integer.parseInt(AtomFx.get(color, "1", ",")), 
					Integer.parseInt(AtomFx.get(color, "2", ",")), 
					Integer.parseInt(AtomFx.get(color, "3", ",")))));
	        
	        if (bold.equals("1")) {
	        		xSSFFont.setBold(true);
	        } else {
	        		xSSFFont.setBold(false);
	        }
	        if (italic.equals("1")) {
	        		xSSFFont.setItalic(true);
	        } else {
	        		xSSFFont.setItalic(false);
	        }
	        cellStyle.setFont(xSSFFont);
			
		}
		
		if (conf.containsKey("Rotation")) {
			
			String value = (String) conf.get("Rotation");
	        cellStyle.setRotation(Short.parseShort(value));
			
		}
		
		if (conf.containsKey("FillPattern")) {
			
			String fillPatternType = (String) conf.get("FillPattern");
			if (fillPatternType.toLowerCase().equals("solid_foreground")) {
				cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			} else if (fillPatternType.toLowerCase().equals("alt_bars")) {
				cellStyle.setFillPattern(FillPatternType.ALT_BARS);
			} else if (fillPatternType.toLowerCase().equals("big_spots")) {
				cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
			} else if (fillPatternType.toLowerCase().equals("bricks")) {
				cellStyle.setFillPattern(FillPatternType.BRICKS);
			} else if (fillPatternType.toLowerCase().equals("diamonds")) {
				cellStyle.setFillPattern(FillPatternType.DIAMONDS);
			} else if (fillPatternType.toLowerCase().equals("fine_dots")) {
				cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
			} else if (fillPatternType.toLowerCase().equals("least_dots")) {
				cellStyle.setFillPattern(FillPatternType.LEAST_DOTS);
			} else if (fillPatternType.toLowerCase().equals("less_dots")) {
				cellStyle.setFillPattern(FillPatternType.LESS_DOTS);
			} else if (fillPatternType.toLowerCase().equals("no_fill")) {
				cellStyle.setFillPattern(FillPatternType.NO_FILL);
			} else if (fillPatternType.toLowerCase().equals("sparse_dots")) {
				cellStyle.setFillPattern(FillPatternType.SPARSE_DOTS);
			} else if (fillPatternType.toLowerCase().equals("squares")) {
				cellStyle.setFillPattern(FillPatternType.SQUARES);
			} else if (fillPatternType.toLowerCase().equals("thick_backward_diag")) {
				cellStyle.setFillPattern(FillPatternType.THICK_BACKWARD_DIAG);
			} else if (fillPatternType.toLowerCase().equals("thick_forward_diag")) {
				cellStyle.setFillPattern(FillPatternType.THICK_FORWARD_DIAG);
			} else if (fillPatternType.toLowerCase().equals("thick_horz_bands")) {
				cellStyle.setFillPattern(FillPatternType.THICK_HORZ_BANDS);
			} else if (fillPatternType.toLowerCase().equals("thick_vert_bands")) {
				cellStyle.setFillPattern(FillPatternType.THICK_VERT_BANDS);
			} else if (fillPatternType.toLowerCase().equals("thin_backward_diag")) {
				cellStyle.setFillPattern(FillPatternType.THIN_BACKWARD_DIAG);
			} else if (fillPatternType.toLowerCase().equals("thin_forward_diag")) {
				cellStyle.setFillPattern(FillPatternType.THIN_FORWARD_DIAG);
			} else if (fillPatternType.toLowerCase().equals("thin_horz_bands")) {
				cellStyle.setFillPattern(FillPatternType.THIN_HORZ_BANDS);
			} else if (fillPatternType.toLowerCase().equals("thin_vert_bands")) {
				cellStyle.setFillPattern(FillPatternType.THIN_VERT_BANDS);
			}
			
		}
		if (conf.containsKey("FillForegroundColor")) {
			
			String color = (String) conf.get("FillForegroundColor");
			
			cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.valueOf(color).getIndex());
			
		}
		if (conf.containsKey("FillBackgroundColor")) {
			
			String color = (String) conf.get("FillBackgroundColor");
			
			cellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.valueOf(color).getIndex());
			
		}
		
	}

	public static String cell_get(EnvManager env, String excelId, String sheetName, String indexRow, String indexCell) throws Exception {
		
		//Initialization
		int indexRowNumber = 0, indexCellNumber = 0;
		
		//Generate an error if the index of the cell is not a number
		try {
			
			indexCellNumber = Integer.parseInt(indexCell);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexCellNumber<0) {
			
			throw new Exception("Sorry, the index of the cell cannot be < 0.");
			
		}
		
		//Generate an error if the index of the row is not a number
		try {
			
			indexRowNumber = Integer.parseInt(indexRow);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexRowNumber<0) {
			
			throw new Exception("Sorry, the index of the row cannot be < 0.");
			
		}
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Generate an error if the sheet name is not valid
		if ((sheetName==null) || (sheetName.equals(""))) {
			
			throw new Exception("Sorry, the sheet name cannot be null or empty.");
			
		}
		
		//Get the sheet
		XSSFSheet sh = null;
		
		//Try to get the sheet
		try {
			
			sh = ((XSSFWorkbook) env.excelx.get(excelId)).getSheet(sheetName);
			
			if (sh==null) throw new Exception("err");
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot get the sheet with the name '"+sheetName+"'.");
			
		}
		
		//Generate an error if the index row > to the last row line number
		if (indexRowNumber<0 || indexRowNumber>sh.getLastRowNum()) {
			
			throw new Exception("Sorry, the index of the row is out of range.");
			
		}
		
		//Get the current cell
		XSSFRow row = sh.getRow(indexRowNumber);
		
		return processCellValue(((XSSFWorkbook) env.excelx.get(excelId)), row, Integer.parseInt(indexCell), false);
		
	}
	


	public static String cell_eval(EnvManager env, String excelId, String sheetName, String indexRow, String indexCell) throws Exception {
		
		//Initialization
		int indexRowNumber = 0, indexCellNumber = 0;
		
		//Generate an error if the index of the cell is not a number
		try {
			
			indexCellNumber = Integer.parseInt(indexCell);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexCellNumber<0) {
			
			throw new Exception("Sorry, the index of the cell cannot be < 0.");
			
		}
		
		//Generate an error if the index of the row is not a number
		try {
			
			indexRowNumber = Integer.parseInt(indexRow);
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, the index row number is not a number.");
			
		}
		
		//Generate an error if the index is < 0
		if (indexRowNumber<0) {
			
			throw new Exception("Sorry, the index of the row cannot be < 0.");
			
		}
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Generate an error if the sheet name is not valid
		if ((sheetName==null) || (sheetName.equals(""))) {
			
			throw new Exception("Sorry, the sheet name cannot be null or empty.");
			
		}
		
		//Get the sheet
		XSSFSheet sh = null;
		
		//Try to get the sheet
		try {
			
			sh = ((XSSFWorkbook) env.excelx.get(excelId)).getSheet(sheetName);
			
			if (sh==null) throw new Exception("err");
			
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot get the sheet with the name '"+sheetName+"'.");
			
		}
		
		//Generate an error if the index row > to the last row line number
		if (indexRowNumber<0 || indexRowNumber>sh.getLastRowNum()) {
			
			throw new Exception("Sorry, the index of the row is out of range.");
			
		}
		
		//Get the current cell
		XSSFRow row = sh.getRow(indexRowNumber);
		
		return processCellValue(((XSSFWorkbook) env.excelx.get(excelId)), row, Integer.parseInt(indexCell), true);
		
	}
	
	public static String processCellValue(XSSFWorkbook wb, XSSFRow row, int cell, boolean eval) throws NullPointerException {
		
		if (null == row) {
			return "";
		}
		//Initialization
		XSSFCell headCell = row.getCell(cell);
		
		if (null == headCell) {
			return "";
		}
		
		String itemName = "";
		
		if (CellType.STRING == headCell.getCellTypeEnum()) {
			
			itemName = headCell.getStringCellValue();
			
		} else if (CellType.NUMERIC == headCell.getCellTypeEnum()) {
			
			if (HSSFDateUtil.isCellDateFormatted(headCell)) {
				
				SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
				double d = headCell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				itemName = formater.format(date);
				
			} else {
				
				BigDecimal a = new BigDecimal(String.valueOf(headCell
						.getNumericCellValue()));
				itemName = (a.setScale(2, BigDecimal.ROUND_HALF_UP)).toString();
				
			}
			
		} else if (CellType.BOOLEAN == headCell.getCellTypeEnum()) {
			
			if (headCell.getBooleanCellValue()) itemName = "1";
			else itemName = "0";
			
		} else if (CellType.BLANK == headCell.getCellTypeEnum()) {
			itemName = "";
		} else if (CellType.FORMULA == headCell.getCellTypeEnum()) {
			if (eval) {
				
				FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
				CellValue cellValue = evaluator.evaluate(headCell);
				
				if (CellType.BOOLEAN == cellValue.getCellTypeEnum()) {
					
					if (cellValue.getBooleanValue()) itemName = "1";
					else itemName = "0";
					
				} else if (CellType.NUMERIC == cellValue.getCellTypeEnum()) {
					
					itemName = ""+cellValue.getNumberValue();
					
				} else if (CellType.STRING == cellValue.getCellTypeEnum()) {
					
					itemName = cellValue.getStringValue();
					
				} else if (CellType.BLANK == cellValue.getCellTypeEnum()) {
					
					itemName = "";
					
				} else if (CellType.ERROR == cellValue.getCellTypeEnum()) {
					
					itemName = "err";
					
				}
				
			} else {
				itemName = headCell.getCellFormula();
			}
		}
		
		return itemName;
		
	}

	public static String save(EnvManager env, String excelId, String path) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Generate an error if the file path is empty or null
		if (path==null || path.equals("")) {
			
			throw new Exception("Sorry, the file path cannot be null or empty.");
			
		}
		
		//Export the Excel document
		try {

			((XSSFWorkbook) env.excelx.get(excelId)).write(new FileOutputStream(path));
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot export the Excel document "+e.getMessage()+".");
			
		}
		
		return result;
		
	}

	public static String close(EnvManager env, String excelId) throws Exception {
		
		//Initialization
		String result = "1";
		
		//Generate an error if the excel document id does not exist
		if (exist(env, excelId).equals("0")) {
			
			throw new Exception("Sorry, the excel document id '"+excelId+"' does not exist.");
			
		}
		
		//Close the Excel document
		try {

			((XSSFWorkbook) env.excelx.get(excelId)).close();
			env.excelx.remove(excelId);
		
		} catch (Exception e) {
			
			throw new Exception("Sorry, cannot close the Excel document.");
			
		}
		
		return result;
		
	}

	public static String closeall(EnvManager env) throws Exception {
		
		//Initialization
		int nbClosed = 0;
		Vector<String> allKeysToDelete = new Vector<String>();
			
		//Get all keys to close
		for (Entry<String, XSSFWorkbook> e : env.excelx.entrySet()) {
			
			allKeysToDelete.add(e.getKey());
			
		}
		
		//Parse all keys to close from the vector object
		for(int i=0;i<allKeysToDelete.size();i++) {
			
			try {
			
				//Close the document
				close(env, allKeysToDelete.get(i));
				nbClosed++;
				
			} catch (Exception e) {
				
				//Nothing to do
				
			}
			
		}
		
		return ""+nbClosed;
		
	}

}
