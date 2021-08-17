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

package re.jpayet.mentdb.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import re.jpayet.mentdb.core.fx.FileFx;
import re.jpayet.mentdb.tools.Misc;

public class ExportFromEditor {

	public static String curDir = null;

	public static String curDirDownload = null;
	public static String curDirUpload = null;
	
	public static void download(String file) throws Exception {
		
		JFileChooser chooser = new JFileChooser();
		if (curDirDownload==null || !(new File(curDirDownload)).exists()) {
			chooser.setCurrentDirectory(new java.io.File("."));
		}
		else {
			chooser.setCurrentDirectory(new java.io.File(curDirDownload));
		}

		chooser.setSelectedFile(new File(file));
		chooser.setDialogTitle("Download a file from '"+Mentalese_Editor.ud_dir+"' folder...");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setMultiSelectionEnabled(false);
		
		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			
			curDirDownload = chooser.getCurrentDirectory().getAbsolutePath();
			
			boolean overwrite = true;
			
			String filename = chooser.getSelectedFile().getAbsolutePath();
			
			if ((new File(filename)).exists()) {
				
				int dialogResult = JOptionPane.showConfirmDialog (null, "Overwrite the file ?","Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					overwrite = true;
				} else {
					overwrite = false;
				}
				
			}
			
			if (overwrite) {

				Mentalese_Editor.busy = true;
				Mentalese_Editor.globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));
				Mentalese_Editor.globalExec.repaint();
				
				Runnable threadTask = new Runnable() {
					 
		            @Override
		            public void run() {
						
						FileOutputStream fo = null;

						try {
							
							fo = FileFx.writer_open(filename);

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("Download: 0%");
								}
							});
							
							Mentalese_Editor.getDataFromServerUpDownload("restricted file_reader_open \"r1\" \""+file.replace("\"", "\\\"")+"\" BINARY null;");

							double size = Double.parseDouble(Mentalese_Editor.getDataFromServerUpDownload("restricted file_size \""+file.replace("\"", "\\\"")+"\";"));
							Double nb = Math.ceil(size/262144);
							
							String data = Mentalese_Editor.getDataFromServerUpDownload("restricted file_reader_get_bytes \"r1\" 262144");
							double index = 0;
							while (data!=null) {
								
								FileFx.writer_add_bytes(fo, data);
								
								data = Mentalese_Editor.getDataFromServerUpDownload("restricted file_reader_get_bytes \"r1\" 262144");
								
								FileFx.writer_flush(fo);
								index++;
								
								Double percent = index*100/nb;
								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("Download: "+percent.intValue()+"%");
									}
								});
								
							}

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("Download: 100%");
								}
							});
							
							Mentalese_Editor.getDataFromServerUpDownload("restricted file_reader_close \"r1\";");
							
							FileFx.writer_close(fo);
							
							JOptionPane.showMessageDialog(null,
									"Your file was saved with successful !",
									"OK",
									JOptionPane.INFORMATION_MESSAGE);
							
						} catch (Exception e) {
							
							try {
								FileFx.writer_close(fo);
							} catch (Exception f) {}
							
							JOptionPane.showMessageDialog(null,
									"ERROR: "+e.getMessage(),
									"KO",
									JOptionPane.ERROR_MESSAGE);
							
						}
						
						Mentalese_Editor.globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));
						Mentalese_Editor.busy = false;
						
					}
				};
				new Thread(threadTask).start();
				
			}
			
		}
		
	}
	
	public static void upload() throws Exception {
		
		if (Mentalese_Editor.is_restricted) Mentalese_Editor.ud_dir = "tmp/"+Mentalese_Editor.user;
		else {
			Mentalese_Editor.ud_dir = Misc.ini("conf/client.conf", "EDITOR", "up_down_load_directory").replace("\n", "").replace("\r", "");
			if (Mentalese_Editor.ud_dir.endsWith("/")) {Mentalese_Editor.ud_dir = Mentalese_Editor.ud_dir.substring(0, Mentalese_Editor.ud_dir.length()-1);}
		}
		
		JFileChooser chooser = new JFileChooser();
		
		if (curDirUpload==null || !(new File(curDirUpload)).exists()) {
			chooser.setCurrentDirectory(new java.io.File("."));
		}
		else {
			chooser.setCurrentDirectory(new java.io.File(curDirUpload));
		}
		chooser.setDialogTitle("Upload a file into '"+Mentalese_Editor.ud_dir+"/' folder...");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setMultiSelectionEnabled(false);
		
		if (chooser.showSaveDialog(Mentalese_Editor.globaljFrame) == JFileChooser.APPROVE_OPTION) {
			
			boolean overwrite = true;

			String filename = chooser.getSelectedFile().getName();
			String filename_path = chooser.getSelectedFile().getAbsolutePath();
			
			curDirUpload = chooser.getCurrentDirectory().getAbsolutePath();
			
			try {
				
				String exist_remote_file = Mentalese_Editor.getDataFromServerUpDownload("restricted file_exist \""+Mentalese_Editor.ud_dir+"/"+filename.replace("\"", "\\\"")+"\";");
				
				if (exist_remote_file.equals("1")) {
					
					int dialogResult = JOptionPane.showConfirmDialog (null, "Overwrite the file ?","Warning",JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
						overwrite = true;
					} else {
						overwrite = false;
					}
					
				}
				
				if (overwrite) {
					
					Mentalese_Editor.busy = true;
					Mentalese_Editor.globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));
					Mentalese_Editor.globalExec.repaint();
					
					Runnable threadTask = new Runnable() {
						 
			            @Override
			            public void run() {
					
							FileInputStream fi = null;
							
							try {
								
								fi = FileFx.reader_open(filename_path);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("Upload: 0%");
									}
								});
								
								double size = Double.parseDouble(""+new File(filename_path).length());
								Double nb = Math.ceil(size/262144);
								
								Mentalese_Editor.getDataFromServerUpDownload("restricted file_writer_open \"w1\" \""+((Mentalese_Editor.ud_dir+"/"+filename).replace("\"", "\\\""))+"\" false BINARY null;");
								
								String data = FileFx.reader_get_bytes(fi, "262144");
								double index = 0;
								while (data!=null) {
									
									Mentalese_Editor.getDataFromServerUpDownload("restricted file_writer_add_bytes \"w1\" \""+data+"\";");
									
									data = FileFx.reader_get_bytes(fi, "262144");
									
									Mentalese_Editor.getDataFromServerUpDownload("restricted file_writer_flush \"w1\";");

									index++;
									
									Double percent = index*100/nb;
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("Upload: "+percent.intValue()+"%");
										}
									});
									
								}

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("Upload: 100%");
									}
								});
								
								FileFx.reader_close(fi);
								Mentalese_Editor.getDataFromServerUpDownload("restricted file_writer_close \"w1\";");
								
								JOptionPane.showMessageDialog(null,
										"Your file was saved with successful !",
										"OK",
										JOptionPane.INFORMATION_MESSAGE);
							
							} catch (Exception e) {
								
								try {
									FileFx.reader_close(fi);
								} catch (Exception f) {}
								
								JOptionPane.showMessageDialog(null,
										"ERROR: "+e.getMessage(),
										"KO",
										JOptionPane.ERROR_MESSAGE);
								
							}
							
							Mentalese_Editor.globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));
							Mentalese_Editor.busy = false;
							
						}
						
					};
					new Thread(threadTask).start();
					
				}
				
			} catch (Exception e) {
				
				JOptionPane.showMessageDialog(null,
						"ERROR: "+e.getMessage(),
						"KO",
						JOptionPane.ERROR_MESSAGE);
				
			}
			
		}
		
	}

	public static void export(JTable jt, String title) {
		
		int startInt = 7;
		if (title.startsWith("dq_table")) startInt = 10;

		JFileChooser chooser = new JFileChooser();
		if (curDir==null || !(new File(curDir)).exists()) {
			chooser.setCurrentDirectory(new java.io.File("."));
			chooser.setSelectedFile(new File(title.substring(startInt, title.length()-1)));
		}
		else {
			chooser.setCurrentDirectory(new java.io.File(curDir));
			chooser.setSelectedFile(new File(title.substring(startInt, title.length()-1)));
		}
		chooser.setDialogTitle("Save the export file");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setMultiSelectionEnabled(false);

		FileNameExtensionFilter xlsxFilter = new FileNameExtensionFilter("ExcelX files (*.xlsx)", "xlsx");
		chooser.addChoosableFileFilter(xlsxFilter);
		FileNameExtensionFilter xlsFilter = new FileNameExtensionFilter("Excel files (*.xls)", "xls");
		chooser.addChoosableFileFilter(xlsFilter);
		FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON files (*.json)", "json");
		chooser.addChoosableFileFilter(jsonFilter);
		FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter("XML files (*.xml)", "xml");
		chooser.addChoosableFileFilter(xmlFilter);
		FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV files (*.csv)", "csv");
		chooser.addChoosableFileFilter(csvFilter);
		FileNameExtensionFilter htmlFilter = new FileNameExtensionFilter("HTML files (*.html)", "html");
		chooser.addChoosableFileFilter(htmlFilter);

		if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			try {

				curDir = chooser.getCurrentDirectory().getAbsolutePath();
				
				boolean overwrite = true;
				
				String filename = chooser.getSelectedFile().getAbsolutePath();
				String filter = ((FileNameExtensionFilter) chooser.getFileFilter()).getExtensions()[0];
				if (!filename.endsWith("."+filter)) filename = filename+"."+filter;
				
				if ((new File(filename)).exists()) {
					
					int dialogResult = JOptionPane.showConfirmDialog (null, "Overwrite the file ?","Warning",JOptionPane.YES_NO_OPTION);
					if(dialogResult == JOptionPane.YES_OPTION){
						overwrite = true;
					} else {
						overwrite = false;
					}
					
				}
				
				if (overwrite) {
					
					write_file(jt, title, chooser.getSelectedFile(), (FileNameExtensionFilter) chooser.getFileFilter());
					JOptionPane.showMessageDialog(null,
						"Your file was saved with successful !",
						"OK",
						JOptionPane.INFORMATION_MESSAGE);
					
				}

			} catch (Exception e) {

				JOptionPane.showMessageDialog(null,
						""+e.getMessage(),
						"Error",
						JOptionPane.ERROR_MESSAGE);

			}
		} else {
			//System.out.println("No Selection ");
		}

	}

	public static void write_file(JTable jt, String title, File selectedFile, FileNameExtensionFilter filter) throws Exception {

		String ft = filter.getExtensions()[0];

		switch (ft.toLowerCase()) {
		case "xlsx":
			write_xlsx(jt, title, selectedFile.getAbsolutePath(), ft);
			break;
		case "xls":
			write_xls(jt, title, selectedFile.getAbsolutePath(), ft);
			break;
		case "json":
			write_json(jt, title, selectedFile.getAbsolutePath(), ft);
			break;
		case "xml":
			write_xml(jt, title, selectedFile.getAbsolutePath(), ft);
			break;
		case "csv":
			write_csv(jt, selectedFile.getAbsolutePath(), ft);
			break;
		case "html":
			write_html(jt, title, selectedFile.getAbsolutePath(), ft);
			break;
		}

	}

	public static void write_xlsx(JTable jt, String title, String selectedFile, String filter) throws Exception {


		XSSFWorkbook excel = null;
		
		try {
			
			excel = new XSSFWorkbook();
			
			excel.createSheet(title.substring(7, title.length()-1));
			XSSFSheet sh = excel.getSheet(title.substring(7, title.length()-1));
			XSSFRow row = sh.createRow(0);
			
			TableModel md = jt.getModel();
			int nbRows = md.getRowCount();
			int nbColumns = md.getColumnCount();
			
			JTableHeader th = jt.getTableHeader();
			
			for (int c = 0; c < nbColumns; c++) {
	
				String name = th.getColumnModel().getColumn(c).getHeaderValue()+"";
	
				XSSFCell cell = row.getCell(c, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellValue(name);
	
			}
	
			int j = 0;
			for (int r = 0; r < nbRows; r++) {
				
				j++;
	
				//Reset the line
				row = sh.createRow(j);
	
				for (int c = 0; c < nbColumns; c++) {
	
					Object o = md.getValueAt(r, c);
	
					XSSFCell cell = row.getCell(c, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if (o!=null) {
						cell.setCellValue(o+"");
					}
	
				}
	
			}
	
			if (selectedFile.endsWith("."+filter)) {
				excel.write(new FileOutputStream(selectedFile));
			} else {
				excel.write(new FileOutputStream(selectedFile+"."+filter));
			}
			
		} catch (Exception e) {
			
			throw new Exception(""+e.getMessage());
			
		} finally {
			
			try {

				//Close the excel document
				excel.close();

			} catch (Exception f) {}
			
		}

	}

	public static void write_xls(JTable jt, String title, String selectedFile, String filter) throws Exception {
		
		HSSFWorkbook excel = null;
		
		try {
			
			excel = new HSSFWorkbook();
			
			excel.createSheet(title.substring(7, title.length()-1));
			HSSFSheet sh = excel.getSheet(title.substring(7, title.length()-1));
			HSSFRow row = sh.createRow(0);
			
			TableModel md = jt.getModel();
			int nbRows = md.getRowCount();
			int nbColumns = md.getColumnCount();
			
			JTableHeader th = jt.getTableHeader();
			
			for (int c = 0; c < nbColumns; c++) {
	
				String name = th.getColumnModel().getColumn(c).getHeaderValue()+"";
	
				HSSFCell cell = row.getCell(c, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				cell.setCellValue(name);
	
			}
	
			int j = 0;
			for (int r = 0; r < nbRows; r++) {
				
				j++;
	
				//Reset the line
				row = sh.createRow(j);
	
				for (int c = 0; c < nbColumns; c++) {
	
					Object o = md.getValueAt(r, c);
	
					HSSFCell cell = row.getCell(c, HSSFRow.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					if (o!=null) {
						cell.setCellValue(o+"");
					}
	
				}
	
			}
	
			if (selectedFile.endsWith("."+filter)) {
				excel.write(new FileOutputStream(selectedFile));
			} else {
				excel.write(new FileOutputStream(selectedFile+"."+filter));
			}
			
		} catch (Exception e) {
			
			throw new Exception(""+e.getMessage());
			
		} finally {
			
			try {

				//Close the excel document
				excel.close();

			} catch (Exception f) {}
			
		}

	}

	@SuppressWarnings("unchecked")
	public static void write_json(JTable jt, String title, String selectedFile, String filter) throws Exception {

		JSONObject table = new JSONObject();
		table.put("table", title.substring(7, title.length()-1));
		JSONArray columns = new JSONArray();
		table.put("columns", columns);
		JSONArray data = new JSONArray();
		table.put("data", data);

		TableModel md = jt.getModel();
		int nbRows = md.getRowCount();
		int nbColumns = md.getColumnCount();

		JTableHeader th = jt.getTableHeader();

		for (int c = 0; c < nbColumns; c++) {

			String name = th.getColumnModel().getColumn(c).getHeaderValue()+"";

			columns.add(name);

		}

		for (int r = 0; r < nbRows; r++) {

			//Reset the line
			JSONObject line = new JSONObject();

			for (int c = 0; c < nbColumns; c++) {

				Object o = md.getValueAt(r, c);

				if (o==null) line.put((String) columns.get(c), null);
				else line.put((String) columns.get(c), o+"");

			}

			data.add(line);

		}

		if (selectedFile.endsWith("."+filter)) Misc.create(selectedFile, format(table.toJSONString()));
		else Misc.create(selectedFile+"."+filter, format(table.toJSONString()));

	}
	
	public static String format(String json) {
		
		Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(json);
		return gson.toJson(je);
        
    }

	public static void write_xml(JTable jt, String title, String selectedFile, String filter) throws Exception {

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Vector<String> colnames = new Vector<String>();

		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("table");
		doc.appendChild(rootElement);
		
		// table name elements
		Element table = doc.createElement("table");
		table.appendChild(doc.createTextNode(title.substring(7, title.length()-1)));
		rootElement.appendChild(table);

		// columns elements
		Element columns = doc.createElement("columns");
		rootElement.appendChild(columns);

		// data elements
		Element data = doc.createElement("data");
		rootElement.appendChild(data);

		TableModel md = jt.getModel();
		int nbRows = md.getRowCount();
		int nbColumns = md.getColumnCount();

		JTableHeader th = jt.getTableHeader();

		for (int c = 0; c < nbColumns; c++) {

			String name = th.getColumnModel().getColumn(c).getHeaderValue()+"";

			Element col = doc.createElement("item");
			col.appendChild(doc.createTextNode(name));
			columns.appendChild(col);

			colnames.add(name);

		}

		for (int r = 0; r < nbRows; r++) {

			//Reset the line
			Element line = doc.createElement("item");

			for (int c = 0; c < nbColumns; c++) {

				Object o = md.getValueAt(r, c);

				Element e = doc.createElement(colnames.get(c));
				if (o==null) {
					e.appendChild(doc.createTextNode(""));
					e.setAttribute("nil", "true");
				}
				else e.appendChild(doc.createTextNode(o+""));
				line.appendChild(e);

			}

			data.appendChild(line);

		}

		if (selectedFile.endsWith("."+filter)) Misc.create(selectedFile, nodeToString(doc));
		else Misc.create(selectedFile+"."+filter, nodeToString(doc));

	}

	//Convert a node to string representation
	public static String nodeToString(Node node) throws Exception {

		//Initialization
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(node);
		transformer.transform(source, result);
		String xmlString = result.getWriter().toString();
		return xmlString;

	}

	public static void write_csv(JTable jt, String selectedFile, String filter) throws Exception {

		String columnSeparator = ",", quoteChar = "\"";
		StringBuilder tableBuilder = new StringBuilder();
		TableModel md = jt.getModel();
		int nbRows = md.getRowCount();
		int nbColumns = md.getColumnCount();
		String crlf = "\n";

		if (Misc.os().equals("win")) {
			crlf = "\r\n";
		}

		JTableHeader th = jt.getTableHeader();

		//Reset the line
		StringBuilder lineBuilder = new StringBuilder();
		String line = "";

		for (int c = 0; c < nbColumns; c++) {

			lineBuilder.append(columnSeparator + csv_value(th.getColumnModel().getColumn(c).getHeaderValue()+"", columnSeparator, quoteChar));

		}

		line = lineBuilder.toString();

		//Clear the first char
		if (!line.equals("")) {
			line = line.substring(1);
		}

		tableBuilder.append(line);

		for (int r = 0; r < nbRows; r++) {

			//Reset the line
			lineBuilder = new StringBuilder();
			line = "";

			for (int c = 0; c < nbColumns; c++) {

				Object o = md.getValueAt(r, c);

				if (o==null) lineBuilder.append(columnSeparator + "");
				else lineBuilder.append(columnSeparator + csv_value(o+"", columnSeparator, quoteChar));

			}

			line = lineBuilder.toString();

			//Clear the first char
			if (!line.equals("")) {
				line = line.substring(1);
			}

			tableBuilder.append(crlf + line);

		}

		if (selectedFile.endsWith("."+filter)) Misc.create(selectedFile, tableBuilder.toString());
		else Misc.create(selectedFile+"."+filter, tableBuilder.toString());

	}

	public static String csv_value(String data, String columnSeparator, String quoteChar) {

		try {

			if (data.indexOf(columnSeparator)>-1 
					|| data.indexOf(quoteChar)>-1
					|| data.indexOf("\n")>-1
					|| data.indexOf("\r")>-1) 
				return quoteChar+data.replace(quoteChar, quoteChar+quoteChar)+quoteChar;
			else return data;

		} catch (Exception e) {

			return null;

		}

	}

	public static void write_html(JTable jt, String title, String selectedFile, String filter) throws Exception {

		if (selectedFile.endsWith("."+filter)) Misc.create(selectedFile, get_html(jt, title, selectedFile, filter));
		else Misc.create(selectedFile+"."+filter, get_html(jt, title, selectedFile, filter));

	}

	public static String get_html(JTable jt, String title, String selectedFile, String filter) {

		StringBuilder result = new StringBuilder("<html><head>\n" + 
				"<style>\n" + 
				"table, td, th {\n" + 
				"    border: 1px solid black;\n" + 
				"    padding: 8px;\n" + 
				"}\n" + 
				"#table1 {\n" + 
				"    border-collapse: collapse;\n" + 
				"}\n" + 
				"</style>\n" + 
				"</head><body><h3>Table: <b>"+title.substring(7, title.length()-1)+"</b></h3><br/>\n");
		
		TableModel md = jt.getModel();
		int nbRows = md.getRowCount();
		int nbColumns = md.getColumnCount();

		JTableHeader th = jt.getTableHeader();

		result.append("<table id='table1'><tr>");

		for (int c = 0; c < nbColumns; c++) {
			result.append("<th>"+th.getColumnModel().getColumn(c).getHeaderValue()+""+"</th>");
		}

		result.append("</tr>");

		for (int r = 0; r < nbRows; r++) {

			result.append("<tr>");

			for (int c = 0; c < nbColumns; c++) {

				Object o = md.getValueAt(r, c);

				if (o==null) result.append("<td style='color:#FF0000'>[NULL]</td>");
				else result.append("<td>"+((o+"").replace("<", "&lt;"))+"</td>");

			}
			
			result.append("</tr>");

		}

		result.append("</table></body></html>");
		
		return result.toString();

	}

}
