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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.TextAction;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.ShorthandCompletion;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxUtilities;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.jdatepicker.ComponentFormatDefaults;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.UtilDateModel;
import org.jfree.data.time.TimeSeries;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import re.jpayet.mentdb.core.fx.DateFx;
import re.jpayet.mentdb.core.fx.StringFx;
import re.jpayet.mentdb.tools.MQLValue;
import re.jpayet.mentdb.tools.Misc;

public class Mentalese_Editor {

	public static boolean is_restricted = true;
	
	public static Timer clignotant = null;
	public static JButton demo_button = null;
	public static JComboBox<String> mqlList = null;
	public static JComboBox<String> clusterList = null;
	public static boolean first_call_clusterList = true;
	public static boolean busy = false;
	public static Mentalese_Editor editor = null;
	public static String secretKey = "";
	public static int connectTimeout = 0;
	public static int readTimeout = 0;
	public static JTextField searchText = null;
	public static JButton upDownScreen = new JButton();

	public static String selected_cluster = "[MAIN_SERVER]";

	static public int textDiff1 = 0;
	static public int textDiff2 = 0;

	public static JLabel maintenance_mql = new JLabel("MQL");
	public static JLabel maintenance_ws = new JLabel("WS");
	public static JLabel maintenance_web = new JLabel("WEB");
	public static JLabel maintenance_job = new JLabel("JOB");
	public static JLabel maintenance_stack = new JLabel("STACK");

	public static Cipher cipherEncode = null;
	public static Cipher cipherDecode = null;
	public static String version = "";
	public static String ai = "";
	public static String user = "";
	public static String password = "";
	public static String hostname = "";
	public static int port = 0;
	public static Vector<RSyntaxTextArea> inputs = new Vector<RSyntaxTextArea>();
	public static Vector<JLabel> outputs = new Vector<JLabel>();
	public static JSplitPane globalSplitPaneInOut = null;
	public static JSplitPane globalSplitPaneAll = null;
	public static int globalFullScreenState = 1;
	public static int globalFullScreenState_dt = 1;
	public static JTabbedPane globalMqlInput = null;
	public static JPanel globalFindReplace = null;
	public static RSyntaxTextArea globalMentdbOutput = null;
	public static JTabbedPane globalOutputTabbbedPane = null;
	public static JTextArea globalFindTxt = null;
	public static JTextArea globalReplaceTxt = null;
	public static JCheckBox globalMatchButton = null;
	public static JRadioButton globalUpButton = null;
	public static JRadioButton globalDownButton = null;
	public static JList<String> globalUserJlist = null;
	public static JFrame globaljFrame = null;
	public static DefaultMutableTreeNode globalMutableTreeNodeAdmin = null;
	public static DefaultMutableTreeNode globalMutableTreeNodeDevel = null;
	public static DefaultMutableTreeNode globalMutableTreeNodeConfig = null;
	public static JTree globalTreeAdmin = null;
	public static JTree globalTreeDevel = null;
	public static JTree globalTreeConfig = null;
	public static DefaultListModel<String> globalModelHistory = null;
	public static JButton globalUndo = null;
	public static JButton globalRedo = null;
	public static JButton globalCut = null;
	public static JButton globalCopy = null;
	public static JButton globalPaste = null;
	public static JButton globalDown = null;
	public static JButton globalUp = null;
	public static JButton globalExec = null;
	public static JButton globalBrainExe = null;
	public static JButton globalNeural = null;
	public static JButton globalLog = null;
	public static TreeRow globalFirstTreeNodeAdmin = null;
	public static TreeRow globalFirstTreeNodeDevel = null;
	public static TreeRow globalFirstTreeNodeConfig = null;
	public static long nbCmd = 0;
	public static boolean sessionClosed = false;
	public static HashMap<String, Integer> treeExpansion = new HashMap<String, Integer>();
	public static JLabel searchLabelInfos = null;

	// create some handy attribute sets
	public static SimpleAttributeSet mentdbColor = new SimpleAttributeSet();
	public static SimpleAttributeSet aiColor = new SimpleAttributeSet();
	public static SimpleAttributeSet userColor = new SimpleAttributeSet();
	public static SimpleAttributeSet errorColor = new SimpleAttributeSet();
	public static SimpleAttributeSet mentdbColorBold = new SimpleAttributeSet();
	public static SimpleAttributeSet aiColorBold = new SimpleAttributeSet();
	public static SimpleAttributeSet userColorBold = new SimpleAttributeSet();
	public static SimpleAttributeSet timeColor = new SimpleAttributeSet();
	public static JLabel editor_title = null;
	public static String server_name_origin = "";

	public static MentDBConnector connector = null;

	public static int searchEmpty() {

		int i = 0;
		for(;i<globalMqlInput.getTabCount();i++) {

			if (inputs.get(i).getText().equals("")) {
				break;
			}

		}

		if (i==globalMqlInput.getTabCount()) {
			try {
				addTab(globalMqlInput.getTabCount());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return i;

	}

	public static String error_connect_1 = null;
	public static String error_connect_2 = null;
	public static String error_connect_3 = null;

	public static void connectMentDB(String hostname, int port, int connectTimeout, int readTimeout, String key, String user, String password) throws Exception {

		//Try to connect
		try {
			
			error_connect_1 = null;
			error_connect_2 = null;
			error_connect_3 = null;

			//Server connection
			connector = new MentDBConnector(hostname, port, connectTimeout, readTimeout, key);

			//User connection
			if (connector.connect(user, password)) {



			} else {

				error_connect_1 = connector.serverConnectionState;
				error_connect_2 = connector.clientConnectionState;

				throw new Exception("STOP");

			}

		} catch (Exception e) {
			
			if (!(e.getMessage()+"").equals("STOP")) {
				error_connect_3 = ""+e.getMessage();
			}

			//Display connection error
			throw new Exception(""+e.getMessage());

		}

	}

	public static void sendToServerSilent(String mql) {

		sendToServer(mql, false);

	}

	public static void sendToServer(String mql) {

		if (!busy) {

			new Thread(new Runnable() {

				public void run(){

					busy = true;

					try {
						
						globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));
						if (sendToServer(mql, true)) globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));
						else globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));
						
					} catch (Exception e) {

						Misc.log("err: "+e.getMessage());

					}

					busy = false;

				}

			}).start();

		}

	}

	public static long startTime = 0, endTime = 0;

	public synchronized static boolean sendToServer(String mql, boolean show) {

		boolean result = true;

		nbCmd++;
		boolean refreshTreeView = false;

		if (mql.equals("restricted maintenance_get") || mql.startsWith("sql show tables ") || mql.startsWith("in editor {") || mql.startsWith("in clipboard {")) {
			
			show = false;

			if (mql.startsWith("sql show tables ")) {

				refreshTreeView = true;

			}

		}

		if (show) {

			writeToScreen(user, userColorBold, userColor, mql, false);

			if (globalModelHistory.size()==0 || !globalModelHistory.getElementAt(0).equals(mql)) {

				globalModelHistory.add(0, mql);
				upDownPosition = -1;

				refreshUpDown();

				while (globalModelHistory.size()>100) {
					globalModelHistory.remove(globalModelHistory.size()-1);
				}

			}
		}

		try {

			startTime = System.currentTimeMillis();

			String value = execute_in_cluster(mql);
			
			endTime = System.currentTimeMillis();

			if (value==null) {

				writeToScreen("mentdb", userColorBold, userColor, value, true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht0")) {

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, mentdbColor, "MentDB: Shutdown with successful.\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht1")) {

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, mentdbColor, "MentDB: Session close with successful.\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht2")) {

				result = false;

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB: User does not exist.\nSession close by the server ...\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht3")) {

				result = false;

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB: Bad password.\nSession close by the server ...\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht5")) {

				result = false;

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB: \"mentdb\" is a system user.\nSession close by the server ...\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht6")) {

				result = false;

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB: \"ai\" is a system user.\nSession close by the server ...\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht7")) {

				result = false;

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB: Protocol error.\nSession close by the server ...\nBye.", true);

			} else if (value.equals("jajidfm62mr7i8dtyfr2tyrypzea8tyyoejht")) {

				result = false;

				//Close the session
				writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB: Unknow error.\nSession close by the server ...\nBye.", true);

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t48")==0) {
				
					JSONObject maintenance = (JSONObject) (new JSONParser().parse(value.substring(39)));
					if ((boolean) maintenance.get("mql")) {
						maintenance_mql.setIcon(new ImageIcon("images"+File.separator+"ko.png"));
						writeToScreen("mentdb", mentdbColorBold, errorColor, "MentDB is in maintenance.", true);
					} else maintenance_mql.setIcon(new ImageIcon("images"+File.separator+"ok.png"));
					if ((boolean) maintenance.get("ws")) maintenance_ws.setIcon(new ImageIcon("images"+File.separator+"ko.png"));
					else maintenance_ws.setIcon(new ImageIcon("images"+File.separator+"ok.png"));
					if ((boolean) maintenance.get("web")) maintenance_web.setIcon(new ImageIcon("images"+File.separator+"ko.png"));
					else maintenance_web.setIcon(new ImageIcon("images"+File.separator+"ok.png"));
					if ((boolean) maintenance.get("job")) maintenance_job.setIcon(new ImageIcon("images"+File.separator+"ko.png"));
					else maintenance_job.setIcon(new ImageIcon("images"+File.separator+"ok.png"));
					if ((boolean) maintenance.get("stack")) maintenance_stack.setIcon(new ImageIcon("images"+File.separator+"ko.png"));
					else maintenance_stack.setIcon(new ImageIcon("images"+File.separator+"ok.png"));
					
			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t35{\"core\":{\"data\":[{\"")==0) {

				reloadTreeAdmin(value.substring(39));

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t65{\"core\":{\"data\":[{\"")==0) {

				reloadTreeDevel(value.substring(39));

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t66{\"core\":{\"data\":[{\"")==0) {

				reloadTreeConfig(value.substring(39));

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t36")==0) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						int pos = searchEmpty();
						globalMqlInput.setSelectedIndex(pos);
						inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
						inputs.get(globalMqlInput.getSelectedIndex()).setText(value.substring(39));
						
						inputs.get(globalMqlInput.getSelectedIndex()).setCaretPosition(0);

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
							}
						});
					}
				});

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t40")==0) {

				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						StringSelection stringSelection = new StringSelection(value.substring(39));
						Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
						clpbrd.setContents(stringSelection, null);
					}
				});

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t41")==0) {

				JTableInEditor jt = new JTableInEditor();
				jt.createJtable(value);

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t42")==0) {

				LineScatterEditor ln = new LineScatterEditor();
				ln.lineChart(value);

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t11")==0) {

				JSONObject res = Misc.loadObject(value.substring(39));
				SCRUD.open_form_3((String) res.get("cm"), (String) res.get("ta"));

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t43")==0) {

				LineScatterEditor sc = new LineScatterEditor();
				sc.scatterChart(value);

			} else if (value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t37")==0 ||
					value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t38")==0 ||
					value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t39")==0) {

			} else if (nbCmd==1) {

				writeToScreen("", null, mentdbColor, value, false);

				version = Misc.atom(Misc.atom(value, 2, "_"), 1, " ");
				globalFirstTreeNodeAdmin.name = "MentDB v_"+version;
				globalFirstTreeNodeDevel.name = "MentDB v_"+version;
				globalFirstTreeNodeConfig.name = "MentDB v_"+version;

			} else {

				if (!refreshTreeView) {
					
					writeToScreen("mentdb", userColorBold, userColor, value, true);
					
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {

							globalOutputTabbbedPane.setSelectedIndex(0);

						}

					});
				}

			}

		} catch (Exception e) {

			result = false;

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			refreshTreeView = false;

		}

		if (refreshTreeView) {

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {

					TreePath tp = null;

					tp = globalTreeConfig.getSelectionPath();
					refreshTreeConfig();

					treeExpansion.put(tp.toString(), 0);

					refreshTreeConfig();

				}

			});

		}

		return result;

	}

	public static void loadData(String groupType, JSONArray data, TimeSeries timeseries) throws java.text.ParseException {

		SimpleDateFormat formatterYear = new SimpleDateFormat("yyyy");
		SimpleDateFormat formatterMonth = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat formatterDay = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatterHour = new SimpleDateFormat("yyyy-MM-dd HH");
		SimpleDateFormat formatterMin = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat formatterSec = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for(int i = 0;i<data.size();i++) {

			JSONObject o = (JSONObject) data.get(i);
			
			String t = (String) o.get("t");
			String v = (String) o.get("v");

			Date date = null;

			switch (groupType) {
			case "YEAR":
				date = formatterYear.parse(t);
				timeseries.add(new org.jfree.data.time.Year(date), Long.parseLong(v));
				break;
			case "MONTH":
				date = formatterMonth.parse(t);
				timeseries.add(new org.jfree.data.time.Month(date), Long.parseLong(v));
				break;
			case "DAY":
				date = formatterDay.parse(t);
				timeseries.add(new org.jfree.data.time.Day(date), Long.parseLong(v));
				break;
			case "HOUR":
				date = formatterHour.parse(t);
				timeseries.add(new org.jfree.data.time.Hour(date), Long.parseLong(v));
				break;
			case "MIN":
				date = formatterMin.parse(t);
				timeseries.add(new org.jfree.data.time.Minute(date), Long.parseLong(v));
				break;
			case "SEC":
				date = formatterSec.parse(t);
				timeseries.add(new org.jfree.data.time.Second(date), Long.parseLong(v));
				break;
			}

		}

	}

	public static String getDataFromServer(String mql) throws Exception {

		busy = true;

		try {

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));

			String str =  execute_in_cluster(mql);

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

			return str;

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

			throw e;

		} finally {

			busy = false;

		}

	}

	public static String getDataFromServerUpDownload(String mql) throws Exception {

		try {

			return execute_in_cluster(mql);

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);

			throw e;

		}

	}

	public static String getServerMode() {

		try {

			return execute_in_cluster("restricted server_mode");

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

		return "server_mode_to_define";

	}
	
	public static void is_restricted() {

		try {
			
			if (execute_in_cluster("restricted is").equals("1")) is_restricted = true;
			else is_restricted = false;

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

	}

	public static String getServerName() {

		try {

			return execute_in_cluster("restricted server_name").toLowerCase();

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

		return "server_name_to_define";

	}

	public static void getClusterList() {

		try {

			clusterList.removeAllItems();
			selected_cluster = null;

			JSONParser jsonParser = new JSONParser();
			JSONArray cluster_list_a = (JSONArray) jsonParser.parse(connector.execute("restricted cm_show \"mentdb\";"));

			clusterList.addItem("[MAIN_SERVER]");

			for(int i=0;i<cluster_list_a.size();i++) {
				clusterList.addItem((String) cluster_list_a.get(i));
			}

			selected_cluster = "[MAIN_SERVER]";

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

	}

	public static void refreshTreeAdmin() {

		busy = true;

		try {

			String value = execute_in_cluster("refresh admin");

			if (value!=null && value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t35{\"core\":{\"data\":[{\"")==0) {

				reloadTreeAdmin(value.substring(39));

			}

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

		busy = false;

	}

	public static void refreshTreeDevel() {

		busy = true;

		try {

			String value = null;

			if (searchText.getText().equals("Type your search and press [ENTER]")) {

				value = execute_in_cluster("refresh devel \"\"");

			} else {

				value = execute_in_cluster("refresh devel \""+searchText.getText().replace("\"", "\\\"")+"\"");

			}

			if (value!=null && value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t65{\"core\":{\"data\":[{\"")==0) {

				reloadTreeDevel(value.substring(39));

			}
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

		busy = false;

	}

	public static String execute_in_cluster(String mql_to_execute) throws Exception {

		if (selected_cluster==null || selected_cluster.equals("[MAIN_SERVER]")) {
			
			return connector.execute(mql_to_execute);

		} else {

			return connector.execute("tunnel execute_hot \""+selected_cluster+"\" {cm get \""+selected_cluster+"\";} (mql {"+mql_to_execute+"});");

		}

	}

	public static void refreshTreeConfig() {

		busy = true;

		try {

			String value = execute_in_cluster("refresh config");

			if (value!=null && value.indexOf("j23i88m90m76i39t04r09y35p14a96y09e57t66{\"core\":{\"data\":[{\"")==0) {

				reloadTreeConfig(value.substring(39));

			}

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

		} catch (Exception e) {

			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashrr.png"));

		}

		busy = false;

	}

	public static String encrypt(String data) {

		try {

			byte[] hasil = cipherEncode.doFinal(data.getBytes());

			return Base64.getEncoder().encodeToString(hasil);

		} catch (Exception e) {
			return "";
		}

	}

	public static String decrypt(String data) {

		try {

			byte[] hasil = cipherDecode.doFinal(Base64.getDecoder().decode(data));
			hasil = Base64.getDecoder().decode(new String(hasil));
			return new String(hasil);

		} catch (Exception e) {
			return "";
		}

	}

	public static String encode_b64(String string)
	{

		if (string==null || string.equals("")) return string;

		//Initialization
		byte[] encoded = Base64.getEncoder().encode(string.getBytes());

		return new String(encoded);

	}

	public static String decode_b64(String string)
	{

		if (string==null || string.equals("")) return string;

		//Initialization
		byte[] decoded = Base64.getDecoder().decode(string.getBytes());

		return new String(decoded);

	}

	public static Font font = null;

	public static String currentSelectedNode = "";



	public static void writeToScreen(String user, SimpleAttributeSet userColor, SimpleAttributeSet color, String message, boolean beep) {

		writeToScreenMentdb(user, userColor, color, message, beep);

	}

	public static boolean justeWriting = false;

	public static void writeToScreenDiff(SimpleAttributeSet color, String message) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				justeWriting = false;

				try {

					globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), message, color);

				} catch (BadLocationException e) {}

				globalMentdbOutput.moveCaretPosition(globalMentdbOutput.getText().length());
				globalMentdbOutput.setSelectionStart(globalMentdbOutput.getText().length());
				globalMentdbOutput.setSelectionEnd(globalMentdbOutput.getText().length());

				justeWriting = true;

			}
		});

	}

	public static void writeToScreenMentdb(String userReceive, SimpleAttributeSet userColor, SimpleAttributeSet color, String message, boolean beep) {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				justeWriting = false;

				try {

					if (userReceive.equals("mentdb")) globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), "#"+userReceive+" - "+DateFx.current_timestamp()+" ("+(endTime-startTime)+" ms);", timeColor);
					else globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), "#"+userReceive+" - "+DateFx.current_timestamp()+";", timeColor);

					globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), System.getProperty("line.separator"), userColor);

					if (message==null) {

						outputs.get(globalMqlInput.getSelectedIndex()).setText("Last output ["+nbCmd+"]: null");
						outputs.get(globalMqlInput.getSelectedIndex()).setForeground(new Color(51, 153, 255));
						outputs.get(globalMqlInput.getSelectedIndex()).setIcon(new ImageIcon("images"+File.separator+"start_g.png"));

						globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), "null"+System.getProperty("line.separator")+System.getProperty("line.separator"), userColor);

					} else {

						int pos = message.lastIndexOf("\n");
						if (pos<0) pos = 0;
						outputs.get(globalMqlInput.getSelectedIndex()).setText("Last output ["+nbCmd+"]: "+message.substring(pos));

						if (userReceive.equals(user)) globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), message+System.getProperty("line.separator")+System.getProperty("line.separator"), color);
						else {
							if (color.equals(errorColor)) {
								String tmpMsg = message;
								if (message.startsWith("\n")) tmpMsg = tmpMsg.substring(1);
								globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), "'"+tmpMsg.replace("'", "\\'")+"'"+System.getProperty("line.separator")+System.getProperty("line.separator"), color);

								outputs.get(globalMqlInput.getSelectedIndex()).setForeground(new Color(255, 0, 0));
								outputs.get(globalMqlInput.getSelectedIndex()).setIcon(new ImageIcon("images"+File.separator+"start_r.png"));

							} else {
								globalMentdbOutput.getDocument().insertString(globalMentdbOutput.getDocument().getLength(), "\""+message.replace("\"", "\\\"")+"\""+System.getProperty("line.separator")+System.getProperty("line.separator"), color);

								outputs.get(globalMqlInput.getSelectedIndex()).setForeground(new Color(0, 255, 0));
								outputs.get(globalMqlInput.getSelectedIndex()).setIcon(new ImageIcon("images"+File.separator+"start_g.png"));

							}
						}

					}
					
					if (beep) {
						Toolkit.getDefaultToolkit().beep();
					}

				} catch (BadLocationException e) {}

				globalMentdbOutput.moveCaretPosition(globalMentdbOutput.getText().length());
				globalMentdbOutput.setSelectionStart(globalMentdbOutput.getText().length());
				globalMentdbOutput.setSelectionEnd(globalMentdbOutput.getText().length());

				justeWriting = true;
				
			}
		});

	}

	public static void userShow(JSONArray users, JSONArray target, JSONArray userWhoTalkingWith) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				if (users!=null && users.size()>0) {

					DefaultListModel<String> listModel = (DefaultListModel<String>) globalUserJlist.getModel();
					listModel.clear();

					Vector<String> l1 = new Vector<String>();
					Vector<String> l2 = new Vector<String>();
					Vector<String> l3 = new Vector<String>();
					Vector<String> l4 = new Vector<String>();

					for(int i = 0;i<users.size();i++) {

						String e = "", f = "";
						String u = users.get(i)+"";

						if (!user.equals(u)) {
							if (target.contains(u)) e+= ">-";
							else e += " -";

							if (userWhoTalkingWith.contains(u)) e+= "< ";
							else e += "  ";

							if (u.equals("ai")) {
								f = ai+" (A.I.)";
							} else f = u;

							if (e.equals(">-< ")) l1.add(e+f);
							else if (e.equals(">-  ")) l2.add(e+f);
							else if (e.equals(" -< ")) l3.add(e+f);
							else l4.add(e+f);

						}

					}

					for(int i = 0;i<l1.size();i++) listModel.addElement(l1.get(i));
					for(int i = 0;i<l2.size();i++) listModel.addElement(l2.get(i));
					for(int i = 0;i<l3.size();i++) listModel.addElement(l3.get(i));
					for(int i = 0;i<l4.size();i++) listModel.addElement(l4.get(i));

					globalUserJlist.updateUI();

				}
			}
		});

	}

	public static int upDownPosition = 0;
	public static String upDownLastCommand = "";

	public static void refreshUpDown() {

		if (globalModelHistory.size()==0) {
			globalUp.setEnabled(false);
			globalDown.setEnabled(false);
		} else {
			if (upDownPosition<globalModelHistory.size()-1) globalUp.setEnabled(true);
			else globalUp.setEnabled(false);
			if (upDownPosition==-1) globalDown.setEnabled(false);
			else globalDown.setEnabled(true);
		}

	}

	public static void main(String[] args) throws Exception {
		
		try {

			searchLabelInfos = new JLabel("Click to find ...");
			searchLabelInfos.setFont(new Font(searchLabelInfos.getFont().getName(), Font.PLAIN, 12));
			searchLabelInfos.setIcon(new ImageIcon("images"+File.separator+"infos.png"));
			searchLabelInfos.setHorizontalAlignment(JLabel.LEFT);

			Locale.setDefault(new Locale("us"));

			System.setProperty("org.graphstream.ui.renderer",
					"org.graphstream.ui.j2dviewer.J2DGraphRenderer");

			System.setProperty("apple.eawt.quitStrategy", "CLOSE_ALL_WINDOWS");

			if (!(new File("data")).exists()) {
				(new File("data")).mkdir();
				Misc.create("data"+File.separator+"mql_assistance.json", "[]");
			}

			if (args.length != 7) {

				//Display an error message and exit
				System.err.println("MentDB usage: java MentDB <host name> <port number> <login> <password> <secret key> <connectTimeout> <readTimeout>");
				System.exit(1);

			} else {

				try {

					//Get the port number
					hostname = args[0];
					port = Integer.parseInt(args[1]);
					user = args[2];
					password = args[3];
					secretKey = args[4];
					connectTimeout = Integer.parseInt(args[5]);
					readTimeout = Integer.parseInt(args[6]);

				} catch (Exception e) {

					//Display an error message and exit
					System.err.println("MentDB usage: java MentDB <host name> <port number> <login> <password> <secret key> <connectTimeout> <readTimeout>");
					System.exit(1);

				}

			}

			byte[] keyData = secretKey.getBytes();
			SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");
			cipherEncode = Cipher.getInstance("Blowfish");
			cipherEncode.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			cipherDecode = Cipher.getInstance("Blowfish");
			cipherDecode.init(Cipher.DECRYPT_MODE, secretKeySpec);

			//Connect to the server
			try {
				connectMentDB(hostname, port, connectTimeout, readTimeout, secretKey, user, password);
				try {is_restricted();} catch (Exception ef) {};
			} catch (Exception ef) {
				writeToScreen("mentdb", mentdbColorBold, errorColor, ""+ef.getMessage(), true);
			};

			startEditor("mql://"+hostname+":"+port+"/mentdb/");
			globalExec.setText("WAIT... ");

			editor = new Mentalese_Editor(new URI( "wss://"+hostname+":"+port+"/mentdb/" ) );

			if (error_connect_1!=null) writeToScreen("mentdb", mentdbColorBold, errorColor, error_connect_1, true);
			if (error_connect_2!=null) writeToScreen("mentdb", mentdbColorBold, errorColor, error_connect_2, true);
			if (error_connect_3!=null) writeToScreen("mentdb", mentdbColorBold, errorColor, error_connect_3, true);

			sendToServerSilent("mentdb");

			getClusterList();

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));
			globalExec.repaint();

			
			try {refreshTreeDevel();} catch (Exception e) {};
			try {refreshTreeAdmin();} catch (Exception e) {};
			try {refreshTreeConfig();} catch (Exception e) {};
			globalTreeAdmin.expandRow(1);
			globalTreeDevel.expandRow(1);
			globalTreeConfig.expandRow(1);
			

			globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

			refreshButton();
			refreshCutCopyPaste();
			refreshUpDown();


			try {globalExec.setText(getServerMode()+"   ");} catch (Exception ef) {};
			try {server_name_origin = getServerName();} catch (Exception ef) {};
			editor_title.setText(" mentdb://"+server_name_origin+"/");
			try {sendToServer("restricted maintenance_get");} catch (Exception ef) {};
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
				}
			});


		} catch (Exception e) {

			Misc.log("error: "+e.getMessage());

		}

	}

	public static boolean loop = true;

	public Mentalese_Editor(URI serverURI) throws Exception {

	}

	public static void closeEditor() {

		try {
			connector.close();
		} catch (Exception e) {}

	}

	public static JDialog dialog = null;

	public static void showFinder() {

		if (dialog==null) {

			dialog = new JDialog(globaljFrame,
					"Find and Replace",
					false);

			dialog.getContentPane().add(globalFindReplace);

			dialog.pack();
			dialog.setLocationRelativeTo(globaljFrame);
			dialog.setVisible(true);

		} else {

			dialog.setVisible(true);

		}

	}

	public static JDialog download = null;
	public static JDialog upload = null;
	
	public static String ud_dir = "";

	public static void showDownload() throws Exception {

		if (download==null) {
			
			JTextField tf_download = new JTextField("");
			addCutCopy(tf_download);
			
			if (is_restricted) {
				ud_dir = "tmp/"+user;
			} else {
				ud_dir = Misc.lrtrim(Misc.ini("conf/client.conf", "EDITOR", "up_down_load_directory")).replace("\n", "").replace("\r", "");
				if (ud_dir.endsWith("/")) {ud_dir = ud_dir.substring(0, ud_dir.length()-1);}
			}
			
			download = new JDialog(globaljFrame,
					"Download a file from '"+ud_dir+"/' folder...",
					false);
			
			JPanel jp = new JPanel(new BorderLayout());
			jp.add(new JLabel(" "+ud_dir+"/"), BorderLayout.WEST);
			jp.add(tf_download, BorderLayout.CENTER);
			JButton dw = new JButton("Download");
			dw.setIcon(new ImageIcon("images"+File.separator+"download.png"));
			jp.add(dw, BorderLayout.EAST);
			jp.setPreferredSize(new Dimension(500, 50));

			dw.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					try {
						
						String file = ud_dir+"/"+tf_download.getText();
						
						String exist_file = getDataFromServer("restricted file_exist \""+file.replace("\"", "\\\"")+"\";");

						if (exist_file.equals("0")) {

							JOptionPane.showMessageDialog(download,
									"Sorry, the file '"+file+"' does not exist.",
									"KO",
									JOptionPane.ERROR_MESSAGE);

						} else {

							String is_dir = getDataFromServer("restricted file_is_directory \""+file.replace("\"", "\\\"")+"\";");

							if (is_dir.equals("1")) {

								JOptionPane.showMessageDialog(download,
										"Sorry, '"+file+"' is a directory.",
										"KO",
										JOptionPane.ERROR_MESSAGE);

							} else {

								download.setVisible(false);
								ExportFromEditor.download(file);

							}

						}

					} catch (Exception ee) {

					}

				}
			});

			download.getContentPane().add(jp);

			download.pack();
			download.setLocationRelativeTo(globaljFrame);
			download.setVisible(true);

		} else {

			download.setVisible(true);

		}

	}

	public static void showUpload() {

		try {
			ExportFromEditor.upload();
		} catch (Exception e) {
			writeToScreen("mentdb", mentdbColorBold, errorColor, ""+e.getMessage(), true);
		}

	}

	public static boolean upDownScreen_bool = false;
	

	public static void startEditor(String url) throws Exception {

		// create some handy attribute sets
		StyleConstants.setForeground(mentdbColor, new Color(0,60,200));
		StyleConstants.setForeground(aiColor, new Color(186, 28, 0));
		StyleConstants.setForeground(userColor, new Color(0, 0, 0));
		StyleConstants.setForeground(errorColor, new Color(255, 0, 0));
		StyleConstants.setForeground(mentdbColorBold, new Color(0,60,255));
		StyleConstants.setBold(mentdbColorBold, true);
		StyleConstants.setForeground(aiColorBold, new Color(186, 28, 0));
		StyleConstants.setBold(aiColorBold, true);
		StyleConstants.setForeground(userColorBold, new Color(0, 0, 0));
		StyleConstants.setBold(userColorBold, true);
		StyleConstants.setForeground(timeColor, new Color(200, 200, 200));
		font = new Font( "Monospaced", Font.PLAIN, 12 );
		
		UIManager.put("TabbedPane.contentAreaColor ",ColorUIResource.GREEN);
		UIManager.put("TabbedPane.selected", new Color(91,91,91));
		UIManager.put("TabbedPane.background",new Color(41,41,41));
		UIManager.put("TabbedPane.shadow",ColorUIResource.GREEN);
		UIManager.put("TabbedPane.borderColor", new Color(31,31,31));
		UIManager.put("TabbedPane.darkShadow", new Color(31,31,31));
		UIManager.put("TabbedPane.light", new Color(31,31,31));
		UIManager.put("TabbedPane.highlight", new Color(31,31,31));
		UIManager.put("TabbedPane.focus", new Color(31,31,31));
		UIManager.put("TabbedPane.unselectedBackground", new Color(31,31,31));
		UIManager.put("TabbedPane.selectHighlight", new Color(255, 0, 0));
		UIManager.put("TabbedPane.tabAreaBackground", new Color(31,31,31));
		UIManager.put("TabbedPane.borderHightlightColor", new Color(31,31,31));
		
		UIManager.put("SplitPane.background", new Color(41,41,41));
		UIManager.put("SplitPane.shadow", new Color(41,41,41));
        UIManager.put("SplitPaneDivider.draggingColor" , new Color(41,41,41));
		
        UIManager.put("Table.background", new Color(41,41,41));
        UIManager.put("Table.gridColor", new Color(58,58,58));
        UIManager.put("Table.foreground", new Color(255,255,255));
		
		UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());

		// Create and set up a frame window
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame(user+"@"+url);
		globaljFrame = frame;
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		frame.setSize(rect.width, rect.height);
		//frame.setUndecorated(true);
		frame.setResizable(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				closeEditor();

				System.exit(0);

			}
		});

		JPanel findReplace = new JPanel(new BorderLayout());
		//findReplace.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
		findReplace.setPreferredSize(new Dimension(240, 90));
		findReplace.setLayout(new BoxLayout(findReplace, BoxLayout.Y_AXIS));

		JTextArea findtxt = new JTextArea("");findtxt.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
		globalFindTxt = findtxt;
		findtxt.setForeground(Color.BLACK);
		findtxt.addKeyListener(new KeyListener() {

			public String OS = Misc.os();

			@Override
			public void keyPressed(KeyEvent e) {

				if ((e.getKeyCode() == KeyEvent.VK_TAB)) {

					globalReplaceTxt.requestFocus();

					e.consume();

					return;

				}

				if (OS.equals("mac")) {

					if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());
						input.requestFocus();

					} else if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						findtxt.setSelectionStart(0);
						findtxt.setSelectionEnd(findtxt.getText().length());

					} else if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (findtxt.getSelectedText()!=null && !findtxt.getSelectedText().equals("")) {

							StringSelection stringSelection = new StringSelection(findtxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);

							String str = findtxt.getText();
							int startPos = findtxt.getSelectionStart();
							int endPos = findtxt.getSelectionEnd();

							String txt = str.substring(0, startPos)+str.substring(endPos);

							findtxt.setText(txt);
							findtxt.setCaretPosition(startPos);

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (findtxt.getSelectedText()!=null && !findtxt.getSelectedText().equals("")) {
							StringSelection stringSelection = new StringSelection(findtxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);
						}

					} else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (findtxt.getSelectedText()!=null && !findtxt.getSelectedText().equals("")) {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								findtxt.replaceSelection(data);
							} catch (Exception e1) {} 
						} else {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								findtxt.insert(data, findtxt.getCaretPosition());
							} catch (Exception e1) {} 
						}

					}

				} else {

					if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());
						input.requestFocus();

					} else if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						findtxt.setSelectionStart(0);
						findtxt.setSelectionEnd(findtxt.getText().length());

					} else if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (findtxt.getSelectedText()!=null && !findtxt.getSelectedText().equals("")) {

							StringSelection stringSelection = new StringSelection(findtxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);

							String str = findtxt.getText();
							int startPos = findtxt.getSelectionStart();
							int endPos = findtxt.getSelectionEnd();

							String txt = str.substring(0, startPos)+str.substring(endPos);

							findtxt.setText(txt);
							findtxt.setCaretPosition(startPos);

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (findtxt.getSelectedText()!=null && !findtxt.getSelectedText().equals("")) {
							StringSelection stringSelection = new StringSelection(findtxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);
						}

					} else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						/*if (findtxt.getSelectedText()!=null && !findtxt.getSelectedText().equals("")) {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								findtxt.replaceSelection(data);
							} catch (Exception e1) {} 
						} else {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								findtxt.insert(data, findtxt.getCaretPosition());
							} catch (Exception e1) {} 
						}*/

					}

				}
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {



			}
		});
		findtxt.setText("");
		findtxt.setBorder(BorderFactory.createCompoundBorder(
				findtxt.getBorder(), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));

		JTextArea replacetxt = new JTextArea("");replacetxt.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
		globalReplaceTxt = replacetxt;

		replacetxt.setForeground(Color.BLACK);
		replacetxt.addKeyListener(new KeyListener(){

			public String OS = Misc.os();

			@Override
			public void keyPressed(KeyEvent e){

				if (OS.equals("mac")) {

					if ((e.getKeyCode() == KeyEvent.VK_TAB) && ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0)) {

						globalFindTxt.requestFocus();

						e.consume();

						return;

					}

					if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());
						input.requestFocus();

					} else if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						replacetxt.setSelectionStart(0);
						replacetxt.setSelectionEnd(replacetxt.getText().length());

					} else if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (replacetxt.getSelectedText()!=null && !replacetxt.getSelectedText().equals("")) {

							StringSelection stringSelection = new StringSelection(replacetxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);

							String str = replacetxt.getText();
							int startPos = replacetxt.getSelectionStart();
							int endPos = replacetxt.getSelectionEnd();

							String txt = str.substring(0, startPos)+str.substring(endPos);

							replacetxt.setText(txt);
							replacetxt.setCaretPosition(startPos);

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (replacetxt.getSelectedText()!=null && !replacetxt.getSelectedText().equals("")) {
							StringSelection stringSelection = new StringSelection(replacetxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);
						}

					} else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (replacetxt.getSelectedText()!=null && !replacetxt.getSelectedText().equals("")) {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								replacetxt.replaceSelection(data);
							} catch (Exception e1) {} 
						} else {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								replacetxt.insert(data, replacetxt.getCaretPosition());
							} catch (Exception e1) {} 
						}

					}

				} else {

					if ((e.getKeyCode() == KeyEvent.VK_TAB) && ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0)) {

						globalFindTxt.requestFocus();

						e.consume();

						return;

					}

					if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());
						input.requestFocus();

					} else if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						replacetxt.setSelectionStart(0);
						replacetxt.setSelectionEnd(replacetxt.getText().length());

					} else if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (replacetxt.getSelectedText()!=null && !replacetxt.getSelectedText().equals("")) {

							StringSelection stringSelection = new StringSelection(replacetxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);

							String str = replacetxt.getText();
							int startPos = replacetxt.getSelectionStart();
							int endPos = replacetxt.getSelectionEnd();

							String txt = str.substring(0, startPos)+str.substring(endPos);

							replacetxt.setText(txt);
							replacetxt.setCaretPosition(startPos);

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (replacetxt.getSelectedText()!=null && !replacetxt.getSelectedText().equals("")) {
							StringSelection stringSelection = new StringSelection(replacetxt.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);
						}

					} else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						/*if (replacetxt.getSelectedText()!=null && !replacetxt.getSelectedText().equals("")) {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								replacetxt.replaceSelection(data);
							} catch (Exception e1) {} 
						} else {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								replacetxt.insert(data, replacetxt.getCaretPosition());
							} catch (Exception e1) {} 
						}*/

					}

				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		replacetxt.setText("");
		replacetxt.setBorder(BorderFactory.createCompoundBorder(
				replacetxt.getBorder(), 
				BorderFactory.createEmptyBorder(3, 3, 3, 3)));
		JCheckBox matchbutton = new JCheckBox("Match case");
		globalMatchButton = matchbutton;
		matchbutton.setSelected(true);
		JRadioButton upbutton = new JRadioButton("Up");
		globalUpButton = upbutton;
		JRadioButton downbutton = new JRadioButton("Dw");
		globalDownButton = downbutton;
		downbutton.setSelected(true);
		ButtonGroup groupButton = new ButtonGroup();
		groupButton.add(upbutton);
		groupButton.add(downbutton);
		JPanel upDown = new JPanel(new GridLayout(0,2));
		upDown.add(upbutton);
		upDown.add(downbutton);
		JPanel matchUpDown = new JPanel(new GridLayout(0,1));
		matchUpDown.add(matchbutton);
		matchUpDown.add(upDown);
		matchUpDown.setBorder(new EmptyBorder(5, 5, 5, 5));

		JButton findbutton = new JButton("Find next");
		findbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				doFindText();

			}
		});

		JButton findupbutton = new JButton("");
		findupbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				globalUpButton.setSelected(true);
				doFindText();

			}
		});

		JButton finddownbutton = new JButton("");
		finddownbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				globalDownButton.setSelected(true);
				doFindText();

			}
		});

		JButton replacefindbutton = new JButton("Replace/Find");
		replacefindbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				doReplaceFindText();

			}
		});
		JButton replacebutton = new JButton("Replace");
		replacebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				doReplaceText();

			}
		});
		JButton replaceallbutton = new JButton("Replace all");
		replaceallbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				doReplaceAllText();

			}
		});
		JScrollPane findtxtScroll = new JScrollPane(findtxt);
		findtxtScroll.setBorder(null);
		findReplace.add(findtxtScroll);
		JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
		seperator.setMaximumSize( new Dimension(10, 10) );
		findReplace.add(seperator);
		JScrollPane replacetxtScroll = new JScrollPane(replacetxt);
		replacetxtScroll.setBorder(null);
		findReplace.add(replacetxtScroll);
		findReplace.add(seperator);

		JPanel findReplaceButtons = new JPanel(new GridLayout(0,3));
		findReplaceButtons.setPreferredSize( new Dimension(225, 90) );
		findReplaceButtons.setMinimumSize(new Dimension(225, 90) );
		findReplaceButtons.setMaximumSize(new Dimension(225, 90) );

		findReplaceButtons.add(findupbutton);
		findReplaceButtons.add(findbutton);
		findReplaceButtons.add(replacefindbutton);
		findReplaceButtons.add(finddownbutton);
		findReplaceButtons.add(replacebutton);
		findReplaceButtons.add(replaceallbutton);

		findupbutton.setIcon(new ImageIcon("images"+File.separator+"up.png"));
		finddownbutton.setIcon(new ImageIcon("images"+File.separator+"down.png"));

		JPanel p = new JPanel(new BorderLayout());
		p.add(searchLabelInfos, BorderLayout.NORTH);
		p.add(findReplaceButtons, BorderLayout.CENTER);
		p.setPreferredSize( new Dimension(700, 90) );
		p.setMinimumSize(new Dimension(700, 90) );
		p.setMaximumSize(new Dimension(700, 90) );

		p.add(matchUpDown, BorderLayout.EAST);
		matchUpDown.setPreferredSize( new Dimension(125, 90) );
		matchUpDown.setMinimumSize(new Dimension(125, 90) );
		matchUpDown.setMaximumSize(new Dimension(125, 90) );

		findReplace.add(p);

		//Create a split pane
		JPanel source = new JPanel(new BorderLayout());
		JToolBar toolBar = new JToolBar("Still draggable");
		toolBar.setFloatable(false);
		toolBar.setBackground(new Color(61, 61, 61));
		toolBar.setOpaque(true);
		toolBar.setPreferredSize(new Dimension(125, 50) );

		JButton exec = new JButton("");
		globalExec = exec;
		exec.setIcon(new ImageIcon("images"+File.separator+"flashb.png"));
		exec.setBackground(new Color(51,51,51));
		exec.setForeground(Color.WHITE);
		exec.setOpaque(true);
		exec.setBorderPainted(false);
		toolBar.add(exec);
		exec.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try {

					if (!busy) {

						String mql = inputs.get(globalMqlInput.getSelectedIndex()).getText();
						//inputs.get(globalMqlInput.getSelectedIndex()).setText("");
						inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();

						if (!upDownScreen_bool) {
							sendToServer(mql);
						} else {
							sendToServer("in editor {\n" + 
									"	"+mql+"\n" + 
									"}");
						}

						upDownPosition = -1;

						refreshUpDown();

					}

				} catch (Exception f) {}
			} 
		} );
		
		if (!is_restricted) {

			toolBar.add(Box.createHorizontalStrut(8));
	
			JButton plus = new JButton();
			plus.setIcon(new ImageIcon("images"+File.separator+"plus.png"));
			plus.setBackground(new Color(51,51,51));
			plus.setOpaque(true);
			plus.setBorderPainted(false);
			toolBar.add(plus);
			plus.setFocusPainted(false);
			plus.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					
					try {
						sendToServer("in editor {mql {script create get|post|put|delete|conf|exe \"folder.folder.your_script_name\" false 1\n"
								+ "  (param\n"
								+ "  	(var \"[v1]\" {true} \"description ...\" is_null:true is_empty:true \"10\")\n"
								+ "  	(var \"[v2]\" {type is_double [v2]} \"description ...\" is_null:true is_empty:true \"20\")\n"
								+ "  )\n"
								+ "  \"description ...\"\n"
								+ "{\n"
								+ "	\n"
								+ "	#Your MQL source code here...;\n"
								+ "	\n"
								+ "} \"Return ...\";};};");
					} catch (Exception f) {};
					
				} 
			} );

		}
			
		toolBar.add(Box.createHorizontalStrut(8));

		upDownScreen.setFocusPainted(false);
		upDownScreen.setIcon(new ImageIcon("images"+File.separator+"up_e.png"));
		upDownScreen.setBackground(new Color(51,51,51));
		upDownScreen.setForeground(Color.WHITE);
		upDownScreen.setOpaque(true);
		upDownScreen.setBorderPainted(false);
		toolBar.add(upDownScreen);
		upDownScreen.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				upDownScreen_bool = !upDownScreen_bool;
				
				// if selected print selected in console 
				if (upDownScreen_bool) { 

					upDownScreen.setIcon(new ImageIcon("images"+File.separator+"down_e.png"));

				} else { 

					upDownScreen.setIcon(new ImageIcon("images"+File.separator+"up_e.png"));

				} 
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
					}
				});
				
			} 
		});

		toolBar.add(Box.createHorizontalStrut(2));

		JButton fullScreen = new JButton();
		fullScreen.setIcon(new ImageIcon("images"+File.separator+"fullscreen0.png"));
		fullScreen.setBackground(new Color(51,51,51));
		fullScreen.setOpaque(true);
		fullScreen.setBorderPainted(false);
		toolBar.add(fullScreen);
		fullScreen.setFocusPainted(false);
		fullScreen.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				fullScreen.setIcon(new ImageIcon("images"+File.separator+"fullscreen"+globalFullScreenState+".png"));

				if (globalFullScreenState==0) {

					globalFullScreenState=1;

				} else {

					globalFullScreenState=0;

				}

				if (globalFullScreenState==0) {

					BasicSplitPaneUI ui = (BasicSplitPaneUI) globalSplitPaneInOut.getUI();
					JButton oneClick = (JButton) ui.getDivider().getComponent(globalFullScreenState);
					oneClick.doClick();

					ui = (BasicSplitPaneUI) globalSplitPaneAll.getUI();
					oneClick = (JButton) ui.getDivider().getComponent(globalFullScreenState);
					oneClick.doClick();

				} else {

					BasicSplitPaneUI ui = (BasicSplitPaneUI) globalSplitPaneAll.getUI();
					JButton oneClick = (JButton) ui.getDivider().getComponent(globalFullScreenState);
					oneClick.doClick();

					ui = (BasicSplitPaneUI) globalSplitPaneInOut.getUI();
					oneClick = (JButton) ui.getDivider().getComponent(globalFullScreenState);
					oneClick.doClick();

				}
			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JTextField aAScreen = new JTextField();
		aAScreen.setBackground(new Color(255,255,255));
		aAScreen.setOpaque(true);
		try {
			aAScreen.setText(Integer.parseInt(Misc.ini("conf/fontsize.conf", "CONF", "fontsize"))+"");
		} catch (Exception ee) {
			
			aAScreen.setText("13");
			
		}
		toolBar.add(aAScreen);
		aAScreen.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				try {
					
					for(int i=0;i<globalMqlInput.getTabCount();i++) {
						
						inputs.get(i).setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(aAScreen.getText())));

					}
					
					globalMentdbOutput.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(aAScreen.getText())));

					treeConfig.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(aAScreen.getText())));
					treeAdmin.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(aAScreen.getText())));
					treeDevel.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(aAScreen.getText())));
					
					Misc.create("conf/fontsize.conf", "[CONF]\nfontsize="+aAScreen.getText());
					
				} catch (Exception ee) {
					
					JOptionPane.showMessageDialog(globaljFrame, ee.getMessage()+"");
					
				}
			} 
		});

		toolBar.add(Box.createHorizontalStrut(8));

		JButton clearInput = new JButton();
		clearInput.setFocusPainted(false);
		clearInput.setIcon(new ImageIcon("images"+File.separator+"clear_all.png"));
		clearInput.setBackground(new Color(51,51,51));
		clearInput.setOpaque(true);
		clearInput.setBorderPainted(false);
		toolBar.add(clearInput);
		clearInput.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				while (globalMqlInput.getTabCount()>1) {

					allTabs.remove(1);
					outputs.removeElementAt(1);
					inputs.remove(1);
					inputLabels.remove(1);
					increment_mqls.remove(1);

					globalMqlInput.remove(1);

				}

				((JButton) allTabs.get(0).getClientProperty("btnAddTab")).setVisible(true);

				inputs.get(0).setText("");

				outputs.get(0).setText("Last output ["+nbCmd+"]");
				outputs.get(0).setForeground(null);
				outputs.get(0).setIcon(new ImageIcon("images"+File.separator+"start_g.png"));

				showTab(inputs.get(0), 0);
				globalMqlInput.setSelectedIndex(0);
				inputs.get(0).requestFocus();
			}
		} );

		toolBar.add(Box.createHorizontalStrut(16));

		JButton up = new JButton();
		globalUp = up;
		up.setIcon(new ImageIcon("images"+File.separator+"up.png"));
		up.setBackground(new Color(51,51,51));
		up.setOpaque(true);
		up.setBorderPainted(false);
		toolBar.add(up);
		up.setFocusPainted(false);
		up.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				if (upDownPosition==-1) upDownLastCommand = inputs.get(globalMqlInput.getSelectedIndex()).getText();

				upDownPosition++;
				inputs.get(globalMqlInput.getSelectedIndex()).setText(globalModelHistory.getElementAt(upDownPosition));
				refreshUpDown();

			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton down = new JButton();
		globalDown = down;
		down.setFocusPainted(false);
		down.setIcon(new ImageIcon("images"+File.separator+"down.png"));
		down.setBackground(new Color(51,51,51));
		down.setOpaque(true);
		down.setBorderPainted(false);
		toolBar.add(down);
		down.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				upDownPosition--;

				if (upDownPosition==-1) inputs.get(globalMqlInput.getSelectedIndex()).setText(upDownLastCommand);
				else inputs.get(globalMqlInput.getSelectedIndex()).setText(globalModelHistory.getElementAt(upDownPosition));
				refreshUpDown();

			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton cut = new JButton();
		globalCut = cut;
		cut.setFocusPainted(false);
		cut.setIcon(new ImageIcon("images"+File.separator+"cut.png"));
		cut.setBackground(new Color(51,51,51));
		cut.setOpaque(true);
		cut.setBorderPainted(false);
		toolBar.add(cut);
		cut.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				inputs.get(globalMqlInput.getSelectedIndex()).cut();
			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton copy = new JButton();
		globalCopy = copy;
		copy.setFocusPainted(false);
		copy.setIcon(new ImageIcon("images"+File.separator+"copy.png"));
		copy.setBackground(new Color(51,51,51));
		copy.setOpaque(true);
		copy.setBorderPainted(false);
		toolBar.add(copy);
		copy.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				inputs.get(globalMqlInput.getSelectedIndex()).copy();
			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton paste = new JButton();
		globalPaste = paste;
		paste.setFocusPainted(false);
		paste.setIcon(new ImageIcon("images"+File.separator+"paste.png"));
		paste.setBackground(new Color(51,51,51));
		paste.setOpaque(true);
		paste.setBorderPainted(false);
		toolBar.add(paste);
		paste.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				inputs.get(globalMqlInput.getSelectedIndex()).paste();
			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton undo = new JButton();
		globalUndo = undo;
		undo.setFocusPainted(false);
		undo.setIcon(new ImageIcon("images"+File.separator+"undo.png"));
		undo.setBackground(new Color(51,51,51));
		undo.setOpaque(true);
		undo.setBorderPainted(false);
		toolBar.add(undo);
		undo.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				inputs.get(globalMqlInput.getSelectedIndex()).undoLastAction();
			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton redo = new JButton();
		globalRedo = redo;
		redo.setFocusPainted(false);
		redo.setIcon(new ImageIcon("images"+File.separator+"redo.png"));
		redo.setBackground(new Color(51,51,51));
		redo.setOpaque(true);
		redo.setBorderPainted(false);
		toolBar.add(redo);
		redo.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				inputs.get(globalMqlInput.getSelectedIndex()).redoLastAction();
			} 
		} );
		
		if (!is_restricted) {

			toolBar.add(Box.createHorizontalStrut(16));
	
			JButton log = new JButton();
			globalLog = log;
			log.setFocusPainted(false);
			log.setIcon(new ImageIcon("images"+File.separator+"log32.png"));
			log.setBackground(new Color(51,51,51));
			log.setOpaque(true);
			log.setBorderPainted(false);
			toolBar.add(log);
			log.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					try {
						sendToServer("log show;");
					} catch (Exception f) {};
				} 
			} );
			
		}
	
			toolBar.add(Box.createHorizontalStrut(16));
	
			JButton jdownload = new JButton();
			jdownload.setFocusPainted(false);
			jdownload.setIcon(new ImageIcon("images"+File.separator+"download.png"));
			jdownload.setBackground(new Color(51,51,51));
			jdownload.setOpaque(true);
			jdownload.setBorderPainted(false);
			toolBar.add(jdownload);
			jdownload.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					try {
						showDownload();
					} catch (Exception f) {
						writeToScreen("mentdb", mentdbColorBold, errorColor, ""+f.getMessage(), true);
					}
	
				} 
			} );
	
			toolBar.add(Box.createHorizontalStrut(2));
	
			JButton jtmp = new JButton();
			jtmp.setFocusPainted(false);
			jtmp.setIcon(new ImageIcon("images"+File.separator+"tmp.png"));
			jtmp.setBackground(new Color(51,51,51));
			jtmp.setOpaque(true);
			jtmp.setBorderPainted(false);
			toolBar.add(jtmp);
			jtmp.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					try {
						
						if (is_restricted) {
							sendToServer("restricted dir_list \"tmp/"+user+"\";");
							ud_dir = "tmp/"+user;
						} else {
							ud_dir = Misc.ini("conf/client.conf", "EDITOR", "up_down_load_directory").replace("\n", "").replace("\r", "");
							if (ud_dir.endsWith("/")) {ud_dir = ud_dir.substring(0, ud_dir.length()-1);}
							sendToServer("restricted dir_list \""+ud_dir+"\";");
						}
						
					} catch (Exception f) {}
	
				} 
			} );
	
			toolBar.add(Box.createHorizontalStrut(2));
	
			JButton jupload = new JButton();
			jupload.setFocusPainted(false);
			jupload.setIcon(new ImageIcon("images"+File.separator+"upload.png"));
			jupload.setBackground(new Color(51,51,51));
			jupload.setOpaque(true);
			jupload.setBorderPainted(false);
			toolBar.add(jupload);
			jupload.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					try {
						showUpload();
					} catch (Exception f) {}
	
				} 
			} );
	
			toolBar.add(Box.createHorizontalStrut(16));
	
			JButton jopenStackLines = new JButton();
			jopenStackLines.setFocusPainted(false);
			jopenStackLines.setIcon(new ImageIcon("images"+File.separator+"stat.png"));
			jopenStackLines.setBackground(new Color(51,51,51));
			jopenStackLines.setOpaque(true);
			jopenStackLines.setBorderPainted(false);
			toolBar.add(jopenStackLines);
			jopenStackLines.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					try {
						openStackLines();
					} catch (Exception f) {}
	
				} 
			} );
	
			toolBar.add(Box.createHorizontalStrut(2));
	
			JButton jopenStackProcess = new JButton();
			jopenStackProcess.setFocusPainted(false);
			jopenStackProcess.setIcon(new ImageIcon("images"+File.separator+"stack.png"));
			jopenStackProcess.setBackground(new Color(51,51,51));
			jopenStackProcess.setOpaque(true);
			jopenStackProcess.setBorderPainted(false);
			toolBar.add(jopenStackProcess);
			jopenStackProcess.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					try {
						openStackProcess();
					} catch (Exception f) {}
	
				} 
			} );
	
			toolBar.add(Box.createHorizontalStrut(2));
			
			JButton jopenLogsBeforeArchive = new JButton();
			jopenLogsBeforeArchive.setFocusPainted(false);
			jopenLogsBeforeArchive.setIcon(new ImageIcon("images"+File.separator+"plog.png"));
			jopenLogsBeforeArchive.setBackground(new Color(51,51,51));
			jopenLogsBeforeArchive.setOpaque(true);
			jopenLogsBeforeArchive.setBorderPainted(false);
			toolBar.add(jopenLogsBeforeArchive);
			jopenLogsBeforeArchive.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					try {
						openLogsBeforeArchive();
					} catch (Exception f) {}
	
				} 
			} );
			
		if (!is_restricted) {
	
			toolBar.add(Box.createHorizontalStrut(8));
	
			JButton process = new JButton("");
			process.setIcon(new ImageIcon("images"+File.separator+"process.png"));
			process.setBackground(new Color(51,51,51));
			process.setOpaque(true);
			process.setBorderPainted(false);
			toolBar.add(process);
			process.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					try {
	
						if (!busy) {
	
							if (!upDownScreen_bool) {
								sendToServer("metric sessions");
							} else {
								sendToServer("in editor {\n" + 
										"	metric sessions\n" + 
										"}");
							}
	
						}
	
					} catch (Exception f) {}
				} 
			} );
	
			toolBar.add(Box.createHorizontalStrut(2));
	
			JButton processm = new JButton("");
			processm.setIcon(new ImageIcon("images"+File.separator+"processm.png"));
			processm.setBackground(new Color(51,51,51));
			processm.setOpaque(true);
			processm.setBorderPainted(false);
			toolBar.add(processm);
			processm.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					try {
	
						if (!busy) {
	
							if (!upDownScreen_bool) {
								sendToServer("if (equal (eval (file ini \"conf/server.conf\" \"SQL\" \"light_mode\")) 1) {\n"
										+ "	sql select MENTDB \"select * from information_schema.sessions;\" \"H2 Db Process\" ; \n"
										+ "} {\n"
										+ "	sql select MENTDB \"show full processlist\" \"MySQL Process\" ; \n"
										+ "}");
							} else {
								sendToServer("in editor {\n" + 
										"	if (equal (eval (file ini \"conf/server.conf\" \"SQL\" \"light_mode\")) 1) {\n"
										+ "	sql select MENTDB \"select * from information_schema.sessions;\" \"H2 Db Process\" ; \n"
										+ "} {\n"
										+ "	sql select MENTDB \"show full processlist\" \"MySQL Process\" ; \n"
										+ "}\n" + 
										"}");
							}
	
						}
	
					} catch (Exception f) {}
				} 
			} );

		}
		
		toolBar.add(Box.createHorizontalStrut(8));
	
		JButton etl = new JButton("");
		etl.setIcon(new ImageIcon("images"+File.separator+"etl.png"));
		etl.setFocusPainted(false);
		etl.setFont(new Font("monospace", Font.PLAIN, 19));
		etl.setBackground(new Color(51,51,51));
		etl.setForeground(new Color(100,100,100));
		etl.setOpaque(true);
		etl.setBorderPainted(false);
		toolBar.add(etl);
		etl.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				try {

					ETL1.open_form_1();
					
				} catch (Exception f) {
	
					Misc.log("error: "+f.getMessage());
	
				}
				
			} 
		});
		
		toolBar.add(Box.createHorizontalStrut(2));
	
		JButton scrud = new JButton("");
		scrud.setIcon(new ImageIcon("images"+File.separator+"scrud.png"));
		scrud.setFocusPainted(false);
		scrud.setFont(new Font("monospace", Font.PLAIN, 19));
		scrud.setBackground(new Color(51,51,51));
		scrud.setForeground(new Color(100,100,100));
		scrud.setOpaque(true);
		scrud.setBorderPainted(false);
		toolBar.add(scrud);
		scrud.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				
				try {
					
					SCRUD.open_form_1();
	
				} catch (Exception f) {
	
					Misc.log("error: "+f.getMessage());
	
				}
				
			} 
		});
		
		toolBar.add(Box.createHorizontalGlue());

		JButton jopenhelp = new JButton();
		jopenhelp.setFocusPainted(false);
		jopenhelp.setIcon(new ImageIcon("images"+File.separator+"help.png"));
		jopenhelp.setBackground(new Color(51,51,51));
		jopenhelp.setOpaque(true);
		jopenhelp.setBorderPainted(false);
		toolBar.add(jopenhelp);
		jopenhelp.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				try {

					int pos = searchEmpty();
					globalMqlInput.setSelectedIndex(pos);
					inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
					inputs.get(globalMqlInput.getSelectedIndex()).setText("List of shortcuts: \n" + 
							"\n" + 
							"([ctrl] or [apple]) + E or S = Execute the current MQL tab\n" + 
							"([ctrl] or [apple]) + C = Copy\n" + 
							"([ctrl] or [apple]) + V = paste\n" + 
							"([ctrl] or [apple]) + X = cut\n" + 
							"([ctrl] or [apple]) + A = select all mql source code\n" + 
							"([ctrl] or [apple]) + Z = undo\n" + 
							"([ctrl] or [apple]) + Y = redo\n" + 
							"([ctrl] or [apple]) + F = Find window\n" + 
							"([ctrl] or [apple]) + M = open popup Menu\n" + 
							"([ctrl] or [apple]) + O = Old executed MQL\n" + 
							"([ctrl] or [apple]) + N = New executed MQL\n" + 
							"([ctrl] or [apple]) + R = tab change on the Right\n" + 
							"([ctrl] or [apple]) + L = tab change on the Left");

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
						}
					});

				} catch (Exception f) {}

			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		demo_button = new JButton();
		demo_button.setIcon(new ImageIcon("images"+File.separator+"youtube1.png"));
		demo_button.setBackground(new Color(51,51,51));
		demo_button.setOpaque(true);
		demo_button.setBorderPainted(false);
		toolBar.add(demo_button);
		demo_button.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				try {

					if (clignotant!=null) {
						clignotant.stop();
					}

					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
						Desktop.getDesktop().browse(new URI("https://www.mentdb.org/mentdb_weak_training.html"));
					}

				} catch (Exception f) {

					Misc.log("error: "+f.getMessage());

				}

			} 
		} );

		toolBar.add(Box.createHorizontalStrut(2));

		JButton jopenabout = new JButton();
		jopenabout.setFocusPainted(false);
		jopenabout.setIcon(new ImageIcon("images"+File.separator+"question.png"));
		jopenabout.setBackground(new Color(51,51,51));
		jopenabout.setOpaque(true);
		jopenabout.setBorderPainted(false);
		toolBar.add(jopenabout);
		jopenabout.setPreferredSize(new Dimension(32, 32));
		jopenabout.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				try {

					int pos = searchEmpty();
					globalMqlInput.setSelectedIndex(pos);
					inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
					inputs.get(globalMqlInput.getSelectedIndex()).setText("INNOV-AI | RESEARCH AND DEVELOPMENT\n"
							+ "1 B RUE ALSACE CORRE 97413 CILAOS - REUNION ISLAND (French Department)\n" + 
							"SIREN: 753 859 727\n" + 
							"SIRET (siege): 75385972700020\n" + 
							"Activity (NAF or APE code): Computer programming (6201Z)\n" + 
							"Legal form: Individual entrepreneur\n"
							+ "Manager: Jimmitry PAYET\n"
							+ "Contact: contact@innov-ai.com | jim@innov-ai.com\n"
							+ "Tel: (+262)0693-83-40-99");

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
						}
					});

				} catch (Exception f) {}

			} 
		} );
		
		if (!is_restricted) {

			toolBar.add(Box.createHorizontalStrut(10));
	
			//Create the combo box, select item at index 4.
			//Indices start at 0, so 4 specifies the pig.
			mqlList = new JComboBox<String>();
			//mqlList.setPreferredSize(new Dimension(70, 10));
			mqlList.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
			String[] file_list = new File("mql").list();
			List<String> al = new ArrayList<String>();
			al = Arrays.asList(file_list);
			Collections.sort(al); 
			for(String file : al) {
				if (file.endsWith(".mql")) {
					mqlList.addItem(file);
				}
			}
	
			//mqlList.addActionListener(this);
			mqlList.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
	
					String mqlName = (String)mqlList.getSelectedItem();
					try {
						int pos = searchEmpty();
						globalMqlInput.setSelectedIndex(pos);
						inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
						inputs.get(globalMqlInput.getSelectedIndex()).setText(Misc.load("mql"+File.separator+mqlName));
	
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
							}
						});
					} catch (Exception e1) {
						//Misc.log("mql"+File.pathSeparator+mqlName+":::"+e1.getMessage());
					}
	
				} 
			} );
			toolBar.add(mqlList);
			
		}

		toolBar.add(Box.createHorizontalStrut(10));

		JButton reconnect = new JButton();
		reconnect.setIcon(new ImageIcon("images"+File.separator+"connect.png"));
		reconnect.setBackground(new Color(51,51,51));
		reconnect.setOpaque(true);
		reconnect.setBorderPainted(false);
		toolBar.add(reconnect);
		reconnect.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				try {

					try {
						connector.close();
					} catch (Exception f) {}

					//Connect to the server
					connectMentDB(hostname, port, connectTimeout, readTimeout, secretKey, user, password);

					sendToServer("restricted maintenance_get");
					sendToServer("Connected.");

				} catch (Exception f) {

					if (error_connect_1!=null) writeToScreen("mentdb", mentdbColorBold, errorColor, error_connect_1, true);
					if (error_connect_2!=null) writeToScreen("mentdb", mentdbColorBold, errorColor, error_connect_2, true);
					if (error_connect_3!=null) writeToScreen("mentdb", mentdbColorBold, errorColor, error_connect_3, true);
					
					Misc.log("error: "+f.getMessage());

				}

			} 
		} );

		toolBar.add(Box.createHorizontalStrut(15));

		JLabel labelInfosInput = new JLabel("Help: https://www.mentdb.org | contact@mentdb.org | contact@innov-ai.com");
		labelInfosInput.setFont(new Font(labelInfosInput.getFont().getName(), Font.PLAIN, 12));
		labelInfosInput.setIcon(new ImageIcon("images"+File.separator+"help.png"));
		labelInfosInput.setHorizontalAlignment(JLabel.LEFT);
		labelInfosInput.setBorder(new EmptyBorder(5, 5, 5, 5));
		labelInfosInput.setForeground(Color.WHITE);

		source.add(toolBar, BorderLayout.NORTH);
		source.add(input(), BorderLayout.CENTER);
		source.add(labelInfosInput, BorderLayout.SOUTH);
		source.setBackground(new Color(51, 51, 51));
		source.setOpaque(true);

		globalFindReplace = findReplace;
		globalFindReplace.setPreferredSize(new Dimension(700, 300));

		JPanel fulloutput = new JPanel(new BorderLayout());
		fulloutput.add(output(), BorderLayout.CENTER);
		fulloutput.setBackground(new Color(51, 51, 51));

		JSplitPane splitPaneInOut = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fulloutput, source);
		globalSplitPaneInOut = splitPaneInOut;
		splitPaneInOut.setOneTouchExpandable(true);
		splitPaneInOut.setEnabled(true);
		splitPaneInOut.setUI(new BasicSplitPaneUI() {
		    @Override
		    public BasicSplitPaneDivider createDefaultDivider() {
		        return new BasicSplitPaneDivider(this) {
		            private static final long serialVersionUID = -6000773723083732304L;

		            @Override
		            public void paint(Graphics g) {
		                //Divider gets no painting
		            }
		        };
		    }
		});
		//splitPaneInOut.setDividerLocation(350);
		//splitPaneInOut.setDividerLocation(0.9);

		// Define the panel to hold the buttons	
		//Tree
		Object[] o = treeAndUsers(frame);
		JPanel config = (JPanel) o[1];
		JPanel admin = (JPanel) o[2];
		JPanel develPanel = (JPanel) o[0];
		JPanel historyPanel = (JPanel) o[3];
		historyPanel.setForeground(new Color(255, 255 ,255));
		historyPanel.setBackground(new Color(61, 61 ,61));
		historyPanel.setOpaque(true);

		JSplitPane l2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, develPanel, historyPanel);
		l2.setOneTouchExpandable(true);
		l2.setEnabled(true);
		l2.setResizeWeight(.65d);
		l2.setUI(new BasicSplitPaneUI() {
		    @Override
		    public BasicSplitPaneDivider createDefaultDivider() {
		        return new BasicSplitPaneDivider(this) {
		            private static final long serialVersionUID = -6000773723083732304L;

		            @Override
		            public void paint(Graphics g) {
		                //Divider gets no painting
		            }
		        };
		    }
		});

		JTabbedPane tabbedPaneAll = new JTabbedPane();
		tabbedPaneAll.addTab("DEVELOPMENT", l2);
		JLabel lblTitle0 = null;
		if (is_restricted) lblTitle0 = new JLabel("<html><body><table border=0 width=100%><tr><td>RESTRICTED SESSION</td></tr></table></body></html>");
		else lblTitle0 = new JLabel("<html><body><table border=0 width=100%><tr><td>DEVEL</td></tr></table></body></html>");
		lblTitle0.setForeground(Color.WHITE);
		lblTitle0.setIcon(new ImageIcon("images"+File.separator+"group.png"));
		tabbedPaneAll.setTabComponentAt(0, lblTitle0);
		
		if (!is_restricted) {

			tabbedPaneAll.addTab("CONNECT", config);
			JLabel lblTitle1 = new JLabel("<html><body><table border=0 width=100%><tr><td>CONNECT</td></tr></table></body></html>");
			lblTitle1.setForeground(Color.WHITE);
			lblTitle1.setIcon(new ImageIcon("images"+File.separator+"connection.png"));
			tabbedPaneAll.setTabComponentAt(1, lblTitle1);
			
			tabbedPaneAll.addTab("ADMIN", admin);
			JLabel lblTitle2 = new JLabel("<html><body><table border=0 width=100%><tr><td>ADMIN</td></tr></table></body></html>");
			lblTitle2.setForeground(Color.WHITE);
			lblTitle2.setIcon(new ImageIcon("images"+File.separator+"activity.png"));
			tabbedPaneAll.setTabComponentAt(2, lblTitle2);
			
		}

		tabbedPaneAll.setUI(new MetalTabbedPaneUI());

		//JLabel t = new JLabel("Mentalese Editor         ");

		editor_title = new JLabel(" mentdb://");
		editor_title.setFont(new Font("MonoSpaced", Font.BOLD, 16));
		editor_title.setBorder(new EmptyBorder(8, 8, 8, 8));
		editor_title.setForeground(Color.WHITE);
		editor_title.setBackground(new Color(0, 100, 175));
		editor_title.setOpaque(true);
		//editor_title.setPreferredSize(new Dimension(155, 45));
		editor_title.setIcon(new ImageIcon("images"+File.separator+"ia.png"));
		editor_title.setHorizontalAlignment(JLabel.LEFT);

		JButton refreshButtonCluster = new JButton();
		refreshButtonCluster.setBorder(null);
		refreshButtonCluster.setPreferredSize(new Dimension(40, 40));
		refreshButtonCluster.setIcon(new ImageIcon("images"+File.separator+"refresh.png"));
		refreshButtonCluster.setBackground(new Color(51,51,51));
		refreshButtonCluster.setForeground(Color.WHITE);
		refreshButtonCluster.setOpaque(true);
		refreshButtonCluster.setBorderPainted(false);
		refreshButtonCluster.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try {

					globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));

					if (((String) clusterList.getSelectedItem())==null || ((String) clusterList.getSelectedItem()).equals("[MAIN_SERVER]")) {

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								getClusterList();
							}
						});

					}

				} catch (Exception f) {}
			} 
		} );

		JPanel titlePanel1 = new JPanel(new BorderLayout());
		titlePanel1.add(tabbedPaneAll, BorderLayout.CENTER);
		titlePanel1.setBackground(new Color(51, 51, 51));
		titlePanel1.setOpaque(true);

		JSplitPane splitPaneAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, titlePanel1, splitPaneInOut);
		globalSplitPaneAll = splitPaneAll;
		splitPaneAll.setOneTouchExpandable(true);
		splitPaneAll.setDividerLocation(400);
		splitPaneAll.setUI(new BasicSplitPaneUI() {
		    @Override
		    public BasicSplitPaneDivider createDefaultDivider() {
		        return new BasicSplitPaneDivider(this) {
		            private static final long serialVersionUID = -6000773723083732304L;

		            @Override
		            public void paint(Graphics g) {
		                //Divider gets no painting
		            }
		        };
		    }
		});

		/*JSplitPane splitPaneConfigAdmin = new JSplitPane(JSplitPane.VERTICAL_SPLIT, config, admin);
		splitPaneConfigAdmin.setOneTouchExpandable(true);
		splitPaneConfigAdmin.setEnabled(false);
		splitPaneConfigAdmin.setDividerLocation(420);*/


		splitPaneAll.setBorder(BorderFactory.createEmptyBorder());
		panel.add(splitPaneAll, BorderLayout.CENTER);

		JPanel search_cluster = new JPanel(new BorderLayout());
		search_cluster.add(editor_title, BorderLayout.CENTER);
		JPanel cluster_p = new JPanel(new BorderLayout());
		cluster_p.add(clusterList, BorderLayout.CENTER);
		clusterList.setBackground(new Color(0, 100, 175));
		if (is_restricted) {
			clusterList.setVisible(false);
			refreshButtonCluster.setVisible(false);
		}
		cluster_p.add(refreshButtonCluster, BorderLayout.EAST);
		refreshButtonCluster.setBackground(new Color(0, 100, 175));
		cluster_p.setPreferredSize(new Dimension(400, 50));
		cluster_p.setBackground(new Color(0, 100, 175));

		maintenance_mql.setBorder(new EmptyBorder(5,5,5,5));
		maintenance_mql.setIcon(new ImageIcon("images"+File.separator+"dk.png"));
		maintenance_mql.setForeground(Color.WHITE);
		maintenance_ws.setBorder(new EmptyBorder(5,5,5,5));
		maintenance_ws.setIcon(new ImageIcon("images"+File.separator+"dk.png"));
		maintenance_ws.setForeground(Color.WHITE);
		maintenance_web.setBorder(new EmptyBorder(5,5,5,5));
		maintenance_web.setIcon(new ImageIcon("images"+File.separator+"dk.png"));
		maintenance_web.setForeground(Color.WHITE);
		maintenance_job.setBorder(new EmptyBorder(5,5,5,5));
		maintenance_job.setIcon(new ImageIcon("images"+File.separator+"dk.png"));
		maintenance_job.setForeground(Color.WHITE);
		maintenance_stack.setBorder(new EmptyBorder(5,5,5,15));
		maintenance_stack.setIcon(new ImageIcon("images"+File.separator+"dk.png"));
		maintenance_stack.setForeground(Color.WHITE);
		
		JPanel jp_maintenance_stack = new JPanel(new BorderLayout());
		jp_maintenance_stack.add(maintenance_stack, BorderLayout.WEST);
		if (!is_restricted) jp_maintenance_stack.add(cluster_p, BorderLayout.CENTER);
		jp_maintenance_stack.setBackground(new Color(0, 100, 175));
		JPanel jp_maintenance_job = new JPanel(new BorderLayout());
		jp_maintenance_job.add(maintenance_job, BorderLayout.WEST);
		jp_maintenance_job.add(jp_maintenance_stack, BorderLayout.CENTER);
		jp_maintenance_job.setBackground(new Color(0, 100, 175));
		JPanel jp_maintenance_web = new JPanel(new BorderLayout());
		jp_maintenance_web.add(maintenance_web, BorderLayout.WEST);
		jp_maintenance_web.add(jp_maintenance_job, BorderLayout.CENTER);
		jp_maintenance_web.setBackground(new Color(0, 100, 175));
		JPanel jp_maintenance_ws = new JPanel(new BorderLayout());
		jp_maintenance_ws.add(maintenance_ws, BorderLayout.WEST);
		jp_maintenance_ws.add(jp_maintenance_web, BorderLayout.CENTER);
		jp_maintenance_ws.setBackground(new Color(0, 100, 175));
		JPanel jp_maintenance_mql = new JPanel(new BorderLayout());
		jp_maintenance_mql.add(maintenance_mql, BorderLayout.WEST);
		jp_maintenance_mql.add(jp_maintenance_ws, BorderLayout.CENTER);
		jp_maintenance_mql.setBackground(new Color(0, 100, 175));
		
		search_cluster.add(jp_maintenance_mql, BorderLayout.EAST);
		search_cluster.setForeground(Color.WHITE);
		search_cluster.setBackground(new Color(0, 100, 175));
		search_cluster.setOpaque(true);

		panel.add(search_cluster, BorderLayout.NORTH);

		frame.add(panel);
		//frame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
		frame.setVisible(true);	


	}

	public static void doFindText() { 

		RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());

		if (globalDownButton.isSelected()) {

			int nextIndex = 0;

			if (globalMatchButton.isSelected()) nextIndex = input.getText().indexOf(globalFindTxt.getText(), input.getCaretPosition());
			else nextIndex = input.getText().toLowerCase().indexOf(globalFindTxt.getText().toLowerCase(), input.getCaretPosition());

			if (nextIndex==-1) {

				searchLabelInfos.setText("/ Found 0");

			} else {

				input.setCaretPosition(nextIndex+globalFindTxt.getText().length());

				input.setSelectionStart( nextIndex );
				input.setSelectionEnd( nextIndex + globalFindTxt.getText().length() );

				searchLabelInfos.setText("/ Found "+StringFx.count(input.getText(), globalFindTxt.getText()));

			}

		} else {

			int caretPosition = input.getCaretPosition()-1;
			String newText = (new StringBuffer(input.getText().substring(0, caretPosition)).reverse().toString());
			String newTextToFind = (new StringBuffer(globalFindTxt.getText()).reverse().toString());

			int nextIndex = 0;

			if (globalMatchButton.isSelected()) nextIndex = newText.indexOf(newTextToFind);
			else nextIndex = newText.toLowerCase().indexOf(newTextToFind.toLowerCase());

			if (nextIndex==-1) {

				searchLabelInfos.setText("/ Found 0");

			} else {

				int startPos = caretPosition-nextIndex-newTextToFind.length();
				input.setCaretPosition(startPos);
				input.setSelectionStart( startPos );
				input.setSelectionEnd( startPos+globalFindTxt.getText().length() );

				searchLabelInfos.setText("/ Found "+StringFx.count(input.getText(), globalFindTxt.getText()));

			}
		}

	}

	public static void doReplaceText() {

		RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());

		String selectedText = input.getSelectedText();

		if(selectedText!=null) {

			int caretPos = input.getSelectionStart();

			input.setText(
					input.getText().substring(0, input.getSelectionStart())
					+globalReplaceTxt.getText()
					+input.getText().substring(input.getSelectionEnd())
					);

			if (globalDownButton.isSelected()) {
				input.setCaretPosition(caretPos+globalReplaceTxt.getText().length());
			} else {
				input.setCaretPosition(caretPos);
			}

			searchLabelInfos.setText("/ Replace 1");

		} else {

			searchLabelInfos.setText("/ Replace 0");

		}

	}

	public static void doReplaceFindText() { 

		RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());

		String selectedText = input.getSelectedText();

		if(selectedText!=null) {

			int caretPos = input.getSelectionStart();

			input.setText(
					input.getText().substring(0, input.getSelectionStart())
					+globalReplaceTxt.getText()
					+input.getText().substring(input.getSelectionEnd())
					);

			if (globalDownButton.isSelected()) {
				input.setCaretPosition(caretPos+globalReplaceTxt.getText().length());
			} else {
				input.setCaretPosition(caretPos);
			}

			doFindText();

			searchLabelInfos.setText("/ Found "+StringFx.count(input.getText(), globalFindTxt.getText()));

		} else {

			searchLabelInfos.setText("/ Replace 0");

		}

	}

	public static void doReplaceAllText() { 

		RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());

		//Count the number of occurrence
		String allText = input.getText();
		String find = globalFindTxt.getText();

		int count = 0;
		int indexOf = 0;

		while (indexOf > -1)
		{
			if (globalMatchButton.isSelected()) indexOf = allText.indexOf(find, indexOf);
			else indexOf = allText.toLowerCase().indexOf(find.toLowerCase(), indexOf);
			if (indexOf > -1) {
				count++;
				indexOf+=find.length();
			}
		}

		if(count>0) {

			if (globalMatchButton.isSelected()) input.setText(allText.replace(find, globalReplaceTxt.getText()));
			else {
				Pattern p = Pattern.compile(find, Pattern.CASE_INSENSITIVE);
				Matcher m = p.matcher(allText);
				input.setText(m.replaceAll(globalReplaceTxt.getText()));
			}

			searchLabelInfos.setText("/ Replace "+count);

		} else {

			searchLabelInfos.setText("/ Replace 0");

		}

	}

	//The output
	public static JTabbedPane output() {

		JTabbedPane outputTabbedPane = new JTabbedPane();
		globalOutputTabbbedPane = outputTabbedPane;
		outputTabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		outputTabbedPane.setUI(new MetalTabbedPaneUI());

		RSyntaxTextArea mentdbOutput = new RSyntaxTextArea(20, 60);

		JPopupMenu popupmenu = mentdbOutput.getPopupMenu();
		popupmenu.add(new JPopupMenu.Separator(), 0);
		popupmenu.add(new JMenuItem(new show_pid()), 0);
		
		if (!is_restricted) {
			popupmenu.add(new JPopupMenu.Separator(), 0);
			popupmenu.add(new JMenuItem(new file_load()), 0);
			popupmenu.add(new JMenuItem(new dir_list()), 0);
			popupmenu.add(new JPopupMenu.Separator(), 0);
			popupmenu.add(new JMenuItem(new CheckDiff()), 0);
			popupmenu.add(new JPopupMenu.Separator(), 0);
			popupmenu.add(new JMenuItem(new eval_mql()), 0);
			popupmenu.add(new JPopupMenu.Separator(), 0);
	
			popupmenu.add(new JMenuItem(new GotoTabbed("Open script")), 0);
		}

		globalMentdbOutput = mentdbOutput;

		mentdbOutput.setEditable(true);
		mentdbOutput.setMarginLineEnabled(false);
		mentdbOutput.setSyntaxEditingStyle("text/mql");

		RTextScrollPane spMentdbOutput = new RTextScrollPane(mentdbOutput);
		spMentdbOutput.setBorder(null);
		spMentdbOutput.setLineNumbersEnabled(false);
		mentdbOutput.setBorder(new EmptyBorder(10, 10, 10, 10));
		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		spMentdbOutput.getVerticalScrollBar().setBackground(new Color(51,51,51));
		spMentdbOutput.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		spMentdbOutput.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		spMentdbOutput.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		atmf.putMapping("text/mql", "re.jpayet.mentdb.editor.MQLTokenMaker");

		Theme theme = null;
		try {

			InputStream in = new FileInputStream("tools"+File.separator+"mqloutput.xml");
			theme = Theme.load(in);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		theme.apply(mentdbOutput);
		try {
			mentdbOutput.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(Misc.ini("conf/fontsize.conf", "CONF", "fontsize"))));
		} catch (Exception ee) {
			mentdbOutput.setFont(new Font("MonoSpaced", Font.PLAIN, 13));
		}

		outputTabbedPane.addTab("MAIN OUTPUT", spMentdbOutput);

		JPanel pnlTab = new JPanel(new GridBagLayout());
		pnlTab.setOpaque(false);
		JLabel lblTitle = new JLabel("<html><body><table border=0 width=100%><tr><td>MAIN OUTPUT</td></tr></table></body></html>");
		lblTitle.setForeground(Color.WHITE);
		lblTitle.setIcon(new ImageIcon("images"+File.separator+"server.png"));
		JButton btnClose = new JButton();
		btnClose.setBackground(new Color(51,51,51));
		btnClose.setForeground(Color.WHITE);
		btnClose.setOpaque(true);
		btnClose.setBorderPainted(false);
		btnClose.setIcon(new ImageIcon("images"+File.separator+"closealltab.png"));
		btnClose.setPreferredSize(new Dimension(20, 20));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;

		pnlTab.add(lblTitle, gbc);

		btnClose.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				int nb = globalOutputTabbbedPane.getTabCount();
				for(int i=1;i<nb;i++) {
					globalOutputTabbbedPane.removeTabAt(1);
				}

			} 
		} );
		JButton btnClearOut = new JButton();
		btnClearOut.setBackground(new Color(51,51,51));
		btnClearOut.setForeground(Color.WHITE);
		btnClearOut.setOpaque(true);
		btnClearOut.setBorderPainted(false);
		btnClearOut.setIcon(new ImageIcon("images"+File.separator+"clear_out.png"));
		btnClearOut.setPreferredSize(new Dimension(20, 20));
		btnClearOut.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				globalMentdbOutput.setText("");

			} 
		} );

		JButton btnFullScreen = new JButton();
		btnFullScreen.setBackground(new Color(51,51,51));
		btnFullScreen.setForeground(Color.WHITE);
		btnFullScreen.setOpaque(true);
		btnFullScreen.setBorderPainted(false);
		btnFullScreen.setIcon(new ImageIcon("images"+File.separator+"dt_fullscreen0.png"));
		btnFullScreen.setPreferredSize(new Dimension(20, 20));

		btnFullScreen.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				btnFullScreen.setIcon(new ImageIcon("images"+File.separator+"dt_fullscreen"+globalFullScreenState_dt+".png"));

				if (globalFullScreenState_dt==0) {

					globalFullScreenState_dt=1;

				} else {

					globalFullScreenState_dt=0;

				}

				if (globalFullScreenState_dt==0) {

					BasicSplitPaneUI ui = (BasicSplitPaneUI) globalSplitPaneInOut.getUI();
					JButton oneClick = (JButton) ui.getDivider().getComponent(1);
					oneClick.doClick();

					ui = (BasicSplitPaneUI) globalSplitPaneAll.getUI();
					oneClick = (JButton) ui.getDivider().getComponent(0);
					oneClick.doClick();

				} else {

					BasicSplitPaneUI ui = (BasicSplitPaneUI) globalSplitPaneAll.getUI();
					JButton oneClick = (JButton) ui.getDivider().getComponent(globalFullScreenState);
					oneClick.doClick();

					ui = (BasicSplitPaneUI) globalSplitPaneInOut.getUI();
					oneClick = (JButton) ui.getDivider().getComponent(0);
					oneClick.doClick();

				}

			} 
		} );

		gbc.gridx++;
		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnFullScreen, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClearOut, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		pnlTab.add(btnClose, gbc);

		globalOutputTabbbedPane.setTabComponentAt(0, pnlTab);

		return outputTabbedPane;

	}

	public static Vector<JLabel> inputLabels = new Vector<JLabel>();

	public static void set_input_tab_title(RSyntaxTextArea textArea, int position, String title, boolean beginPoint) {

		int nbChar = 25;
		if (beginPoint) {
			if (title.length()>nbChar) {
				title = title.substring(0, nbChar)+"...";
			}
		} else {
			if (title.length()>nbChar) {
				title = "..."+title.substring(title.length()-nbChar, title.length());
			}
		}

		if (textArea.getClientProperty("SERVER_MODE").equals("")) {
			textArea.putClientProperty("SERVER_MODE", Misc.lrtrim(globalExec.getText()));
		}

		inputLabels.get(position).setText("<html><body><table border=0 width=100%><tr><td>"+(textArea.getClientProperty("SERVER_MODE").equals("")?"":textArea.getClientProperty("SERVER_MODE")+": ")+title+"&nbsp; </td></tr></table></body></html>");

		globalMqlInput.repaint();

	}

	public static Vector<JPanel> allTabs = new Vector<JPanel>();

	public static void set_input_tab_label(RSyntaxTextArea textArea, String label, JPanel secondOutputPanel) {

		try {

			JPanel pnlTab = new JPanel(new GridBagLayout());

			int position = globalMqlInput.getTabCount()-1;
			allTabs.add(secondOutputPanel);
			globalMqlInput.setTabComponentAt(position, pnlTab);

			pnlTab.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			pnlTab.setOpaque(false);

			if (textArea.getClientProperty("SERVER_MODE").equals("")) {
				textArea.putClientProperty("SERVER_MODE", Misc.lrtrim(globalExec.getText()));
			}

			JLabel lblTitle = new JLabel("<html><body><table border=0><tr><td>"+(textArea.getClientProperty("SERVER_MODE").equals("")?"":textArea.getClientProperty("SERVER_MODE")+": ")+label+"&nbsp; </td></tr></table></body></html>");
			//lblTitle.setPreferredSize(new Dimension(204, 16));
			lblTitle.setFont(new Font("Monospaced", Font.BOLD, 12));
			lblTitle.setForeground(Color.WHITE);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;

			pnlTab.add(lblTitle, gbc);

			JButton btnClearScreen = new JButton();
			secondOutputPanel.putClientProperty("btnClearScreen", btnClearScreen);

			btnClearScreen.setIcon(new ImageIcon("images"+File.separator+"eraseTab.png"));
			btnClearScreen.setBackground(new Color(51,51,51));
			btnClearScreen.setForeground(Color.WHITE);
			btnClearScreen.setOpaque(true);
			btnClearScreen.setBorderPainted(false);
			btnClearScreen.setPreferredSize(new Dimension(20, 20));

			btnClearScreen.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 

					textArea.putClientProperty("SERVER_MODE", "");

					int local_position = globalMqlInput.indexOfComponent(secondOutputPanel);

					globalMqlInput.setSelectedIndex(local_position);
					inputs.get(globalMqlInput.getSelectedIndex()).setText("");

					outputs.get(globalMqlInput.getSelectedIndex()).setText("Last output ["+nbCmd+"]");
					outputs.get(globalMqlInput.getSelectedIndex()).setForeground(null);
					outputs.get(globalMqlInput.getSelectedIndex()).setIcon(new ImageIcon("images"+File.separator+"start_g.png"));

					showTab(inputs.get(globalMqlInput.getSelectedIndex()), globalMqlInput.getSelectedIndex());

					globalMqlInput.setSelectedIndex(local_position);
					inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();

				} 
			} );

			gbc.gridx++;
			gbc.weightx = 0;
			pnlTab.add(btnClearScreen, gbc);

			if (globalMqlInput.getTabCount()>1) {

				JButton btnDelTab = new JButton();
				btnDelTab.setIcon(new ImageIcon("images"+File.separator+"closetab.png"));
				btnDelTab.setBackground(new Color(51,51,51));
				btnDelTab.setForeground(Color.WHITE);
				btnDelTab.setOpaque(true);
				btnDelTab.setBorderPainted(false);
				btnDelTab.setPreferredSize(new Dimension(20, 20));
				secondOutputPanel.putClientProperty("btnDelTab", btnDelTab);

				btnDelTab.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) { 

						int local_position = globalMqlInput.indexOfComponent(secondOutputPanel);

						try {

							allTabs.remove(secondOutputPanel);
							outputs.removeElementAt(local_position);
							inputs.remove(local_position);
							inputLabels.remove(local_position);
							increment_mqls.remove(local_position);

							for(int pos=0; pos<allTabs.size();pos++) {
								if (pos==allTabs.size()-1) {
									((JButton) allTabs.get(pos).getClientProperty("btnAddTab")).setVisible(true);
								} else {
									((JButton) allTabs.get(pos).getClientProperty("btnAddTab")).setVisible(false);
								}
							}

							globalMqlInput.remove(local_position);

							globalMqlInput.setSelectedIndex(local_position-1);
							inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();

						} catch (Exception f) {
							Misc.log("e="+f.getMessage());
						}

					} 
				} );

				gbc.gridx++;
				gbc.weightx = 0;
				pnlTab.add(btnDelTab, gbc);

			}

			JButton btnAddTab = new JButton();
			btnAddTab.setBackground(new Color(51,51,51));
			btnAddTab.setForeground(Color.WHITE);
			btnAddTab.setOpaque(true);
			btnAddTab.setBorderPainted(false);
			secondOutputPanel.putClientProperty("btnAddTab", btnAddTab);

			for(int pos=0; pos<allTabs.size();pos++) {
				if (pos==allTabs.size()-1) {
					((JButton) allTabs.get(pos).getClientProperty("btnAddTab")).setVisible(true);
				} else {
					((JButton) allTabs.get(pos).getClientProperty("btnAddTab")).setVisible(false);
				}
			}

			btnAddTab.setIcon(new ImageIcon("images"+File.separator+"addtab.png"));
			btnAddTab.setPreferredSize(new Dimension(20, 20));

			btnAddTab.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 

					try {

						addTab(globalMqlInput.getTabCount());
						globalMqlInput.setSelectedIndex(globalMqlInput.getTabCount()-1);
						inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();

					} catch (Exception ee) {

					}

				} 
			} );

			gbc.gridx++;
			gbc.weightx = 0;
			pnlTab.add(btnAddTab, gbc);

			pnlTab.setAlignmentX(JPanel.LEFT_ALIGNMENT);

			inputLabels.add(lblTitle);

		} catch (Exception e) {
			Misc.log("Err load tab: "+e.getMessage());
		}

	}

	@SuppressWarnings("rawtypes")
	public static class BorderListCellRenderer implements ListCellRenderer {

		private Border insetBorder;

		private DefaultListCellRenderer defaultRenderer;

		public BorderListCellRenderer() {
			this.insetBorder = new EmptyBorder(5, 5, 5, 5);
			this.defaultRenderer = new DefaultListCellRenderer();
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JLabel renderer = (JLabel) defaultRenderer
					.getListCellRendererComponent(list, value, index, isSelected,
							cellHasFocus);
			renderer.setBorder(insetBorder);
			return renderer;
		}

	}

	public static double zoomInDelta=.2, zoomOutDelta=.2, maxZoomPercent=.5, minZoomPercent=2.0;

	public static class show_pid extends TextAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public show_pid() {
			super("Show PID ...");
		}

		public void actionPerformed(ActionEvent e) {

			JTextComponent tc = getTextComponent(e);

			try {

				String pid = tc.getSelectedText();

				if (pid!=null && !pid.equals("")) {

					String mql = "restricted show_pid "+pid;

					sendToServer(mql);

				}

			} catch (Exception ex) {

			}

		}

	}

	public static class file_load extends TextAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public file_load() {
			super("Load file ...");
		}

		public void actionPerformed(ActionEvent e) {

			JTextComponent tc = getTextComponent(e);

			try {

				String file = tc.getSelectedText();

				if (file!=null && !file.equals("")) {

					String mql = "file load \""+(file.replace("\"", "\\\""))+"\"";

					if (!upDownScreen_bool) {
						sendToServer(mql);
					} else {
						sendToServer("in editor {\n" + 
								"	"+mql+"\n" + 
								"}");
					}

				}

			} catch (Exception ex) {

			}

		}

	}

	public static class dir_list extends TextAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public dir_list() {
			super("Dir list ...");
		}

		public void actionPerformed(ActionEvent e) {

			JTextComponent tc = getTextComponent(e);

			try {

				String dir = tc.getSelectedText();

				if (dir!=null && !dir.equals("")) {

					String mql = "file dir_list \""+(dir.replace("\"", "\\\""))+"\"";
					
					if (!upDownScreen_bool) {
						sendToServer(mql);
					} else {
						sendToServer("in editor {\n" + 
								"	"+mql+"\n" + 
								"}");
					}

				}

			} catch (Exception ex) {

			}

		}

	}

	public static class eval_mql extends TextAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public eval_mql() {
			super("Eval MQL ...");
		}

		public void actionPerformed(ActionEvent e) {

			JTextComponent tc = getTextComponent(e);

			try {

				String script = tc.getSelectedText();

				if (script!=null && !script.equals("")) {

					String mql = script;

					if (!upDownScreen_bool) {
						sendToServer(mql);
					} else {
						sendToServer("in editor {\n" + 
								"	"+mql+"\n" + 
								"}");
					}

				}

			} catch (Exception ex) {

			}

		}

	}

	public static class GotoTabbed extends TextAction {

		public GotoTabbed(String name) {
			super(name);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {

			JTextComponent tc = getTextComponent(e);

			try {

				String script = tc.getSelectedText();

				if (script!=null && !script.equals("")) {

					String mql = "in editor {\n" + 
							"	script generate update \""+script.replace("\"", "\\\"")+"\";\n" + 
							"};";

					sendToServerSilent(mql);

				}

			} catch (Exception ex) {

			}

		}

	}

	public static class CheckDiff extends TextAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CheckDiff() {
			super("Text diff on [ORI, REV]");
		}

		public void actionPerformed(ActionEvent e) {

			try {

				Theme theme = null;
				try {

					InputStream in = new FileInputStream("tools"+File.separator+"mqloutput.xml");
					theme = Theme.load(in);

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				String[] values = Misc.text_diff(inputs.get(textDiff2).getText(), inputs.get(textDiff1).getText());
				String left = values[0];
				String right = values[1];

				JPanel panel = new JPanel(new GridLayout(1, 2));

				RSyntaxTextArea textAreaL = new RSyntaxTextArea(20, 60);
				textAreaL.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
				textAreaL.setEditable(false);
				RTextScrollPane spL = new RTextScrollPane(textAreaL);
				spL.setBorder(null);
				textAreaL.setText(left);
				spL.getVerticalScrollBar().setBackground(new Color(51,51,51));
				spL.getHorizontalScrollBar().setBackground(new Color(51,51,51));
				spL.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
				    @Override
				    protected void configureScrollBarColors() {
				        this.thumbColor = new Color(21,21,21);
				    }
				});
				spL.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
				    @Override
				    protected void configureScrollBarColors() {
				        this.thumbColor = new Color(21,21,21);
				    }
				});

				theme.apply(textAreaL);

				RSyntaxTextArea textAreaR = new RSyntaxTextArea(20, 60);
				textAreaR.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSS);
				textAreaR.setEditable(false);
				RTextScrollPane spR = new RTextScrollPane(textAreaR);
				spR.setBorder(null);
				textAreaR.setText(right);
				spR.getVerticalScrollBar().setBackground(new Color(51,51,51));
				spR.getHorizontalScrollBar().setBackground(new Color(51,51,51));
				spR.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
				    @Override
				    protected void configureScrollBarColors() {
				        this.thumbColor = new Color(21,21,21);
				    }
				});
				spR.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
				    @Override
				    protected void configureScrollBarColors() {
				        this.thumbColor = new Color(21,21,21);
				    }
				});

				theme.apply(textAreaR);

				AdjustmentListener listenerL = new MyAdjustmentListener(spR);
				spL.getVerticalScrollBar().addAdjustmentListener(listenerL);

				AdjustmentListener listenerR = new MyAdjustmentListener(spL);
				spR.getVerticalScrollBar().addAdjustmentListener(listenerR);

				panel.add(spL);
				panel.add(spR);

				textAreaL.setCaretPosition(0);

				JPanel pnlTab = new JPanel(new GridBagLayout());
				pnlTab.setOpaque(false);
				JLabel lblTitle = new JLabel("<html><body><table border=0 width=100%><tr><td>text diff (ORI, REV)</td></tr></table></body></html>");
				lblTitle.setForeground(Color.WHITE);
				lblTitle.setIcon(new ImageIcon("images"+File.separator+"flag2.png"));
				JButton btnClose = new JButton();
				btnClose.setBackground(new Color(51,51,51));
				btnClose.setForeground(Color.WHITE);
				btnClose.setOpaque(true);
				btnClose.setBorderPainted(false);
				btnClose.setIcon(new ImageIcon("images"+File.separator+"closetab.png"));
				btnClose.setPreferredSize(new Dimension(16, 16));
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.weightx = 1;

				pnlTab.add(lblTitle, gbc);

				btnClose.addActionListener(new ActionListener() { 
					public void actionPerformed(ActionEvent e) { 

						Component selected = Mentalese_Editor.globalOutputTabbbedPane.getSelectedComponent();
						if (selected != null) {

							JComponent source = (JComponent) e.getSource();
							Container tabComponent = source.getParent();
							int tabIndex = Mentalese_Editor.globalOutputTabbbedPane.indexOfTabComponent(tabComponent);
							Mentalese_Editor.globalOutputTabbbedPane.removeTabAt(tabIndex);

						}

					} 
				} );

				gbc.gridx++;
				gbc.weightx = 0;
				pnlTab.add(btnClose, gbc);
				Mentalese_Editor.globalOutputTabbbedPane.add("text diff<>", panel);
				Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1, pnlTab);
				Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1);

			} catch (Exception ex) {

				Misc.log(ex.getMessage()+"");

			}

		}

	}

	public static Vector<Integer> increment_mqls = new Vector<Integer>();
	public static int increment_mql = 0;

	public static void addTab(int i) throws Exception {

		increment_mql++;
		increment_mqls.add(increment_mql);

		RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
		textArea.setSyntaxEditingStyle("text/mql");
		textArea.setCodeFoldingEnabled(false);
		textArea.setMarkOccurrences(true);
		textArea.setAutoIndentEnabled(true);
		textArea.putClientProperty("SERVER_MODE", Misc.lrtrim(globalExec.getText()));

		RTextScrollPane sp = new RTextScrollPane(textArea);
		sp.setBorder(null);
		sp.getVerticalScrollBar().setBackground(new Color(51,51,51));
		sp.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		sp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		sp.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});

		JPopupMenu popupmenu = textArea.getPopupMenu();

		popupmenu.add(new JPopupMenu.Separator(), 0);
		popupmenu.add(new JMenuItem(new show_pid()), 0);
		
		if (!is_restricted) {
			popupmenu.add(new JPopupMenu.Separator(), 0);
			popupmenu.add(new JMenuItem(new file_load()), 0);
			popupmenu.add(new JMenuItem(new dir_list()), 0);
			popupmenu.add(new JPopupMenu.Separator(), 0);
			popupmenu.add(new JMenuItem(new CheckDiff()), 0);
			popupmenu.add(new JPopupMenu.Separator(), 0);
	
			popupmenu.add(new JMenuItem(new eval_mql()), 0);
			popupmenu.add(new JPopupMenu.Separator(), 0);
	
			popupmenu.add(new JMenuItem(new GotoTabbed("Open script")), 0);
		}

		JPanel secondOutputPanel = new JPanel(new BorderLayout());

		secondOutputPanel.putClientProperty("increment_mql", increment_mql);

		JLabel out = new JLabel("Last output ["+nbCmd+"]");
		out.setFont(new Font("MonoSpaced", Font.PLAIN, 12));
		out.setIcon(new ImageIcon("images"+File.separator+"start_g.png"));
		out.setHorizontalAlignment(JLabel.LEFT);
		out.setPreferredSize(new Dimension(20, 25));
		out.setBackground(new Color(51, 51, 51));
		out.setForeground(new Color(150, 150, 150));
		out.setOpaque(true);
		//Border border = BorderFactory.createLineBorder(new Color(215, 215, 215), 1);
		//out.setBorder(border);
		outputs.addElement(out);

		secondOutputPanel.add(out, BorderLayout.NORTH);
		secondOutputPanel.add(sp, BorderLayout.CENTER);

		globalMqlInput.addTab("MQL "+increment_mql, secondOutputPanel);
		set_input_tab_label(textArea, "MQL "+increment_mql, secondOutputPanel);

		new JVSAutoCompletionProvider(textArea);

		theme.apply(textArea);

		try {
			textArea.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(Misc.ini("conf/fontsize.conf", "CONF", "fontsize"))));
		} catch (Exception ee) {
			textArea.setFont(new Font("MonoSpaced", Font.PLAIN, 13));
		}

		inputs.addElement(textArea);

		textArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				refreshButton();

				showTab(textArea, globalMqlInput.getSelectedIndex());

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				refreshButton();

				showTab(textArea, globalMqlInput.getSelectedIndex());

			}

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				refreshButton();

				showTab(textArea, globalMqlInput.getSelectedIndex());

			}
		});

		textArea.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

				String OS = Misc.os();

				if (OS.equals("mac")) {

					if ((e.getKeyCode() == KeyEvent.VK_M) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						try {
							int offset = textArea.getCaretPosition();
							Rectangle location = textArea.modelToView(offset);
							textArea.getPopupMenu().show(textArea, location.x, location.y);
						} catch (BadLocationException e1) {}

					} else if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (globalUp.isEnabled()) {

							globalUp.doClick();

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (globalDown.isEnabled()) {

							globalDown.doClick();

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_R) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						int sel_index = globalMqlInput.getSelectedIndex();
						sel_index++;
						if (sel_index==globalMqlInput.getTabCount()) {
							sel_index = 0;
						}

						globalMqlInput.setSelectedIndex(sel_index);

					} else if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						int sel_index = globalMqlInput.getSelectedIndex();
						sel_index--;
						if (sel_index<0) {
							sel_index = globalMqlInput.getTabCount()-1;
						}

						globalMqlInput.setSelectedIndex(sel_index);

					}

				} else {

					if ((e.getKeyCode() == KeyEvent.VK_M) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						try {
							int offset = textArea.getCaretPosition();
							Rectangle location = textArea.modelToView(offset);
							textArea.getPopupMenu().show(textArea, location.x, location.y);
						} catch (BadLocationException e1) {}

					} else if ((e.getKeyCode() == KeyEvent.VK_O) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (globalUp.isEnabled()) {

							globalUp.doClick();

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_N) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (globalDown.isEnabled()) {

							globalDown.doClick();

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_R) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						int sel_index = globalMqlInput.getSelectedIndex();
						sel_index++;
						if (sel_index==globalMqlInput.getTabCount()) {
							sel_index = 0;
						}

						globalMqlInput.setSelectedIndex(sel_index);

					} else if ((e.getKeyCode() == KeyEvent.VK_L) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						int sel_index = globalMqlInput.getSelectedIndex();
						sel_index--;
						if (sel_index<0) {
							sel_index = globalMqlInput.getTabCount()-1;
						}

						globalMqlInput.setSelectedIndex(sel_index);

					}

				}

			}

		});

		textArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {

				refreshCutCopyPaste();

				int length = textArea.getSelectionEnd() - textArea.getSelectionStart();
				if (length>0) {

					if (textArea.getSelectedText().length()<100000) {

						double sum = 0, min=0, max=0;
						long nb = 0;
						String[] words = textArea.getSelectedText().replace("\r", " ").replace("\n", " ").split(" ");
						int nb_line = textArea.getSelectedText().replace("\r\n", "\n").split("\n").length;
						for(int i=0;i<words.length;i++) {

							try {

								double d = Double.parseDouble(words[i]);
								nb++;

								if (nb==1) {
									min = d;
									max = d;
								}

								if (d<min) {
									min = d;
								}
								if (d>max) {
									max = d;
								}
								sum+= d;

							} catch (Exception ee) {}

						}

						if (nb>0) outputs.get(globalMqlInput.getSelectedIndex()).setText("Start: "+textArea.getSelectionStart()+" | End: "+textArea.getSelectionEnd()+" | Len: "+length+" | Line: "+nb_line+" | Sum: "+sum+" | Avg: "+(sum/nb)+" | Min: "+min+" | Max: "+max);
						else outputs.get(globalMqlInput.getSelectedIndex()).setText("Start: "+textArea.getSelectionStart()+" | End: "+textArea.getSelectionEnd()+" | Len: "+length+" | Line: "+nb_line+" | Sum: "+sum+" | Avg: <no value> | Min: <no value> | Max: <no value>");

					} else {

						outputs.get(globalMqlInput.getSelectedIndex()).setText("Start: "+textArea.getSelectionStart()+" | End: "+textArea.getSelectionEnd()+" | Len: "+length+" | Sum: <too long>");

					}

					outputs.get(globalMqlInput.getSelectedIndex()).setForeground(new Color(0, 255, 0));
					outputs.get(globalMqlInput.getSelectedIndex()).setIcon(new ImageIcon("images"+File.separator+"start_g.png"));

				}

			}
		});

		try {

			globalMqlInput.setSelectedIndex(i);

		} catch (Exception e) {}

	}

	public static Theme theme = null;

	//The input
	public static JTabbedPane input() throws Exception {

		AbstractTokenMakerFactory atmf = (AbstractTokenMakerFactory)TokenMakerFactory.getDefaultInstance();
		atmf.putMapping("text/mql", "re.jpayet.mentdb.editor.MQLTokenMaker");

		try {

			InputStream in = new FileInputStream("tools"+File.separator+"mql.xml");
			theme = Theme.load(in);

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		JTabbedPane mqlInput = new JTabbedPane();
		mqlInput.setUI(new MetalTabbedPaneUI());
		mqlInput.setTabPlacement(JTabbedPane.NORTH);
		mqlInput.setFont(new Font("Monospaced", Font.BOLD, 13));
		mqlInput.setAlignmentX(JTabbedPane.LEFT_ALIGNMENT);
		mqlInput.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		globalMqlInput=mqlInput;

		addTab(0);

		globalMqlInput.setSelectedIndex(0);

		mqlInput.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {

				refreshButton();
				refreshCutCopyPaste();
				refreshUpDown();

				textDiff1 = textDiff2;
				textDiff2 = globalMqlInput.getSelectedIndex();

				inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();

			}
		});

		return mqlInput;

	}

	//Split without empty
	public static Vector<Vector<MQLValue>> splitCommand(String str) throws Exception {

		Vector<Vector<MQLValue>> commands = null;

		//Initialization
		int line = 1;

		StringBuilder tmpstr = new StringBuilder(str);
		commands = new Vector<Vector<MQLValue>>();
		commands.add(new Vector<MQLValue>());
		int endIndex = 1;
		int depth = 0;
		StringBuilder tmpCmd = new StringBuilder("");

		//Parse the command string
		while (!tmpstr.toString().equals("")) {

			switch (tmpstr.substring(0, 1)) {
			case ";":

				if (depth==0) {
					commands.get(commands.size()-1).add(new MQLValue(";", 0, line));
					commands.add(new Vector<MQLValue>());
				} else {
					tmpCmd.append(";");
				}
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case "\n":

				line++;

				if (depth!=0) {
					tmpCmd.append("\n");
				}
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case " ":

				if (depth!=0) {
					tmpCmd.append(" ");
				}
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case "\t":

				if (depth!=0) {
					tmpCmd.append("\t");
				}
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case "{":

				if (depth>0) {
					tmpCmd.append("{");
				}
				depth += 1;
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case "(":

				if (depth>0) {
					tmpCmd.append("(");
				}
				depth += 1;
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case "}":

				depth -= 1;

				if (depth==0) {

					if (!Misc.lrtrim(tmpCmd.toString()).endsWith(";")) {
						tmpCmd.append(";");
					}

					int nbSize = commands.size()-1;

					if (commands.get(nbSize).size()>0) {

						switch (commands.get(nbSize).get(0).value) {
						case "and": case "or": case "if": case "for": case "while": case "repeat": case "exception": case "try": case "parallel": case "mql": case "switch": case "case": 

							commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							break;

						case "sql":

							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "csv":

							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "html":

							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "json":

							if (commands.get(nbSize).get(1).value.equals("parse_obj") || commands.get(nbSize).get(1).value.equals("parse_array")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "script":

							if (commands.get(nbSize).get(1).value.equals("add") 
									|| commands.get(nbSize).get(1).value.equals("create")
									|| commands.get(nbSize).get(1).value.equals("insert")
									|| commands.get(nbSize).get(1).value.equals("update")) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else if (commands.get(nbSize).get(1).value.equals("set")
									&& (commands.get(nbSize).get(2).value.equals("delay"))) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "relation":

							if ((commands.get(nbSize).get(1).value.equals("condition") ||
									commands.get(nbSize).get(1).value.equals("filter")) && 
									commands.get(nbSize).get(2).value.equals("set")) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else if (commands.get(nbSize).get(1).value.equals("action") && 
									(commands.get(nbSize).get(2).value.equals("add")
											|| commands.get(nbSize).get(2).value.equals("update"))) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "data":

							if (commands.get(nbSize).get(1).value.equals("type")
									&& commands.get(nbSize).get(2).value.equals("create")) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						default: 

							commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));

						}

					} else commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));

					tmpCmd = new StringBuilder("");

				} else {
					tmpCmd.append("}");
				}
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case ")":

				depth -= 1;

				if (depth==0) {

					if (!Misc.lrtrim(tmpCmd.toString()).endsWith(";")) {
						tmpCmd.append(";");
					}

					int nbSize = commands.size()-1;

					if (commands.get(nbSize).size()>0) {

						switch (commands.get(nbSize).get(0).value) {
						case "and": case "or": case "if": case "for": case "while": case "repeat": case "exception": case "try": case "parallel": case "mql": case "switch": case "case": 

							commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							break;

						case "sql":

							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "csv":

							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "html":

							if (commands.get(nbSize).get(1).value.equals("parse")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "json":

							if (commands.get(nbSize).get(1).value.equals("parse_obj") || commands.get(nbSize).get(1).value.equals("parse_array")) {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));
							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "script":

							if (commands.get(nbSize).get(1).value.equals("add") 
									|| commands.get(nbSize).get(1).value.equals("create")
									|| commands.get(nbSize).get(1).value.equals("insert")
									|| commands.get(nbSize).get(1).value.equals("update")) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else if (commands.get(nbSize).get(1).value.equals("set")
									&& (commands.get(nbSize).get(2).value.equals("delay"))) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "relation":

							if ((commands.get(nbSize).get(1).value.equals("condition") ||
									commands.get(nbSize).get(1).value.equals("filter")) && 
									commands.get(nbSize).get(2).value.equals("set")) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else if (commands.get(nbSize).get(1).value.equals("action") && 
									(commands.get(nbSize).get(2).value.equals("add")
											|| commands.get(nbSize).get(2).value.equals("update"))) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						case "data":

							if (commands.get(nbSize).get(1).value.equals("type")
									&& commands.get(nbSize).get(2).value.equals("create")) {

								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 0, line));

							} else {
								commands.get(nbSize).add(new MQLValue(tmpCmd.toString(), 2, line));
							}

							break;

						default: 

							commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));

						}

					} else commands.get(commands.size()-1).add(new MQLValue(tmpCmd.toString(), 2, line));

					tmpCmd = new StringBuilder("");

				} else {
					tmpCmd.append(")");
				}
				tmpstr = new StringBuilder(tmpstr.substring(1));

				break;

			case "\"":

				//It is a string
				endIndex = tmpstr.indexOf("\"", 1);
				while (endIndex!=-1 && tmpstr.substring(endIndex-1, endIndex).equals("\\")) {
					endIndex = tmpstr.indexOf("\"", endIndex+1);
				}

				//Generate an error if the string is never close
				if (endIndex==-1) {

					throw new Exception("Sorry, Invalid command (string must be close)");

				} else {

					//The string is close
					String tmptmpstr = tmpstr.substring(1, endIndex);

					if (depth>0) {

						tmpCmd.append("\""+tmptmpstr+"\"");

					} else {

						commands.get(commands.size()-1).add(new MQLValue(tmptmpstr.replace("\\\"", "\""), 0, line));

					}
					line += count_int(tmptmpstr.toString(), "\n");

					tmpstr = new StringBuilder(tmpstr.substring(endIndex+1));

				}

				break;

			default: 

				//It is not a string
				//Find the space
				HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
				int i0 = tmpstr.indexOf("\t");hm.put(i0, 0);
				int i1 = tmpstr.indexOf(" ");hm.put(i1, 0);
				int i2 = tmpstr.indexOf(";");hm.put(i2, 0);
				int i3 = tmpstr.indexOf("{");hm.put(i3, 0);
				int i4 = tmpstr.indexOf("}");hm.put(i4, 0);
				int i5 = tmpstr.indexOf("(");hm.put(i5, 0);
				int i6 = tmpstr.indexOf(")");hm.put(i6, 0);
				int i7 = tmpstr.indexOf("\n");hm.put(i7, 0);

				if (hm.size()==1) {
					endIndex = -1;
				} else {
					SortedSet<Integer> keys = new TreeSet<Integer>(hm.keySet());
					Object[] iKeys = keys.toArray();
					if (((Integer) iKeys[0])!=-1) {
						endIndex = (Integer) iKeys[0];
					} else {
						endIndex = (Integer) iKeys[1];
					}

				}

				//Go to the next key
				if (endIndex==-1) {

					String tmptmpstr = tmpstr.substring(0, tmpstr.length());

					if (!tmptmpstr.equals("")) {

						if (depth>0) {
							tmpCmd.append(tmptmpstr);
						} else {

							if (tmptmpstr.startsWith("@[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, -1, line));
							} else if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 1, line));
							} else {
								if (tmptmpstr.equals("false") || tmptmpstr.equals("true") || tmptmpstr.equals("null")) commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 3, line));
								else commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 0, line));
							}

						}

					}
					tmpstr = new StringBuilder("");

				} else {

					String tmptmpstr = tmpstr.substring(0, endIndex);

					if (!tmptmpstr.equals("")) {

						if (depth>0) {
							tmpCmd.append(tmptmpstr);
						} else {

							if (tmptmpstr.startsWith("@[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, -1, line));
							} else if (tmptmpstr.startsWith("[") && tmptmpstr.endsWith("]")) {
								commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 1, line));
							} else {
								if (tmptmpstr.equals("false") || tmptmpstr.equals("true") || tmptmpstr.equals("null")) commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 3, line));
								else commands.get(commands.size()-1).add(new MQLValue(tmptmpstr, 0, line));
							}

						}

					}

					tmpstr = new StringBuilder(tmpstr.substring(endIndex));

				}

			}

		}

		if (depth!=0) {
			throw new Exception("Sorry, Invalid command ('{' or '}' or '(' or ')' must be close)");
		}

		//Remove the last if empty
		int nbDeleted = 0, nbTab = commands.size();
		for(int i=0;i<nbTab;i++) {
			if (commands.get(i-nbDeleted).size()==0) {
				commands.remove(i-nbDeleted);
				nbDeleted++;
			}
		}

		//Return all command lines
		return commands;

	}

	public static int count_int(String string, String find)
	{

		int count = 0;
		int indexOf = 0;

		while (indexOf > -1)
		{
			indexOf = string.indexOf(find, indexOf);
			if (indexOf > -1) {
				count++;
				indexOf++;
			}
		}

		return count;

	}

	public static String split_mql(String str, String index) {

		//Try to split
		try {

			return splitCommand(str).get(0).get(Integer.parseInt(index)).value;

		} catch (Exception e) {

			//If an error was generated then return null in a basic data object
			return null;

		}

	}

	public static void showTab(RSyntaxTextArea textArea, int i) {

		if (!textArea.getText().equals("")) {

			if (textArea.getText().startsWith("script create ") || 
					textArea.getText().startsWith("script insert ") || 
					textArea.getText().startsWith("script add ")) {

				String mql = textArea.getText().split("\n")[0];
				String m = split_mql(mql, "2");
				String s = split_mql(mql, "3")+"."+m;

				set_input_tab_title(textArea, i, s, false);

			} else if (textArea.getText().startsWith("script update ")) {

				String mql = textArea.getText().split("\n")[0];
				String s = split_mql(mql, "2");

				set_input_tab_title(textArea, i, s, false);

			} else {

				String txt = "";
				if (textArea.getText().length()>34) {
					txt = textArea.getText().substring(0, 34)+"...";
				} else {
					txt = textArea.getText();
				}

				set_input_tab_title(textArea, i, txt, true);

			}

		} else {

			set_input_tab_title(textArea, i, "MQL "+increment_mqls.get(i), false);

		}

	}

	public static void refreshCutCopyPaste() {

		RSyntaxTextArea textArea = inputs.get(globalMqlInput.getSelectedIndex());
		int length = textArea.getSelectionEnd() - textArea.getSelectionStart();
		if (length>0) {
			globalCut.setEnabled(true);
			globalCopy.setEnabled(true);
		} else {
			globalCut.setEnabled(false);
			globalCopy.setEnabled(false);
		}

		try {
			String data = (String) Toolkit.getDefaultToolkit()
					.getSystemClipboard().getData(DataFlavor.stringFlavor);

			if (data!=null && !data.equals("")) {

				globalPaste.setEnabled(true);

			} else {

				globalPaste.setEnabled(false);

			}
		} catch (Exception e) {

			globalPaste.setEnabled(true);

		} 

	}

	public static void refreshButton() {

		if (inputs.get(globalMqlInput.getSelectedIndex()).canUndo()) {
			globalUndo.setEnabled(true);
		} else {
			globalUndo.setEnabled(false);
		}
		if (inputs.get(globalMqlInput.getSelectedIndex()).canRedo()) {
			globalRedo.setEnabled(true);
		} else {
			globalRedo.setEnabled(false);
		}

	}

	/**
	 * Create a simple provider that adds some Java-related completions.
	 * @throws Exception 
	 */
	private static CompletionProvider createCompletionProvider() throws Exception {

		// A DefaultCompletionProvider is the simplest concrete implementation
		// of CompletionProvider. This provider has no understanding of
		// language semantics. It simply checks the text entered up to the
		// caret position for a match against known completions. This is all
		// that is needed in the majority of cases.
		DefaultCompletionProvider provider = new DefaultCompletionProvider();

		provider.addCompletion(new BasicCompletion(provider, "false"));
		provider.addCompletion(new BasicCompletion(provider, "true"));

		JSONArray json = (JSONArray) JSONValue.parse(Misc.load("tools"+File.separator+"mql.json"));

		for(int i=0;i<json.size();i=i+4) {

			String fx = (String) json.get(i);
			String fxAll = Misc.lrtrim((String) json.get(i+1));
			String fxDesc = (String) json.get(i+2);

			provider.addCompletion(new ShorthandCompletion(provider, 
					fx.replace(" ", "_"),
					fxAll, null, fxDesc));

		}

		return provider;
	}

	static class JVSAutoCompletionProvider extends AutoCompletion {

		public static String OS = Misc.os();

		public JVSAutoCompletionProvider(RSyntaxTextArea TextPane) throws Exception {
			super(createCompletionProvider());
			setShowDescWindow(true);
			install(TextPane);
			this.setAutoCompleteSingleChoices(false);
			TextPane.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					int ch = e.getKeyChar();
					
					if((ch == 95 || ch == 46 || ch == 42 || ch == 43 || ch == 45 || ch == 47 || ch == 33
							|| ((ch >= 48) && (ch <= 57)) 
							|| ((ch >= 65) && (ch <= 90)) 
							|| ((ch >= 60) && (ch <= 62)) 
							|| ((ch >= 97) && (ch <= 122)))) {
						
						int pos = inputs.get(globalMqlInput.getSelectedIndex()).getCaretPosition();
						Token t = RSyntaxUtilities.getTokenAtOffset(inputs.get(globalMqlInput.getSelectedIndex()), pos-1);
						
						if (t!=null && t.getType()==13) {
							
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									hideChildWindows();
								}
							});
							
						} else {

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									showPopupWindow();
								}
							});
							
						}

					} else {

						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								hideChildWindows();
							}
						});
						
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {

					if (OS.equals("mac")) {

						if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

							showFinder();
							globalFindTxt.requestFocus();
							if (inputs.get(globalMqlInput.getSelectedIndex()).getSelectedText()!=null && !inputs.get(globalMqlInput.getSelectedIndex()).getSelectedText().equals("")) {
								globalFindTxt.setText(inputs.get(globalMqlInput.getSelectedIndex()).getSelectedText());
								RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());

								searchLabelInfos.setText("/ Found "+StringFx.count(input.getText(), globalFindTxt.getText()));
							}

						} else if (((e.getKeyCode() == KeyEvent.VK_S) || (e.getKeyCode() == KeyEvent.VK_E)) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

							globalExec.doClick();

						}

					} else {

						if ((e.getKeyCode() == KeyEvent.VK_F) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

							showFinder();
							globalFindTxt.requestFocus();
							if (inputs.get(globalMqlInput.getSelectedIndex()).getSelectedText()!=null && !inputs.get(globalMqlInput.getSelectedIndex()).getSelectedText().equals("")) {
								globalFindTxt.setText(inputs.get(globalMqlInput.getSelectedIndex()).getSelectedText());
								RSyntaxTextArea input = inputs.get(globalMqlInput.getSelectedIndex());

								searchLabelInfos.setText("/ Found "+StringFx.count(input.getText(), globalFindTxt.getText()));
							}

						} else if (((e.getKeyCode() == KeyEvent.VK_S) || (e.getKeyCode() == KeyEvent.VK_E)) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

							globalExec.doClick();

						}

					}

				}

				@Override
				public void keyReleased(KeyEvent e) {}
			}
					);
		}
		public void showPopupWindow() {
			this.refreshPopupWindow();
		}
	}

	public static void printNode(DefaultMutableTreeNode node, int[] position, JTree tree) {

		TreePath tp = new TreePath(node.getPath());

		if (treeExpansion.containsKey(tp.toString())) {

			tree.expandPath(new TreePath(node.getPath()));

		}

		int childCount = node.getChildCount();

		for (int i = 0; i < childCount; i++) {

			position[0]++;

			DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);
			printNode(childNode, position, tree);

		}

	}

	public static void setExpansionStateAdmin() {

		int position[] = {0};
		printNode(globalMutableTreeNodeAdmin, position, globalTreeAdmin);

	}

	public static void setExpansionStateDevel() {

		int position[] = {0};
		printNode(globalMutableTreeNodeDevel, position, globalTreeDevel);

	}

	public static void setExpansionStateConfig() {

		int position[] = {0};
		printNode(globalMutableTreeNodeConfig, position, globalTreeConfig);

	}

	public static int iTitle = 0;
	public static JTree treeAdmin = null;

	public static JScrollPane initJtreeAdmin() {

		globalFirstTreeNodeAdmin = new TreeRow("MentDB", "", "db16x16.png", null, null, null, null, null);
		DefaultMutableTreeNode mutableTreeNodeAdmin = new DefaultMutableTreeNode(globalFirstTreeNodeAdmin);
		globalMutableTreeNodeAdmin = mutableTreeNodeAdmin;

		treeAdmin = new JTree(mutableTreeNodeAdmin);
		try {
			treeAdmin.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(Misc.ini("conf/fontsize.conf", "CONF", "fontsize"))));
		} catch (Exception ee) {
			treeAdmin.setFont(new Font("MonoSpaced", Font.PLAIN, 13));
		}
		treeAdmin.setScrollsOnExpand(true);
		treeAdmin.setCellRenderer(new MyTreeCellRenderer());
		treeAdmin.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							treeAdmin.getLastSelectedPathComponent();

					if (node == null) return;
					if( node.getUserObject() instanceof TreeRow) {

						TreeRow nodeInfo = (TreeRow) node.getUserObject();
						String mql = nodeInfo.mql;
						String direct = nodeInfo.direct;

						if (mql!=null) {
							if (direct.equals("1")) {
								try {
									sendToServer(mql);
								} catch (Exception f) {};
							} else {
								if (!mql.startsWith("editor open form ")) {

									int pos = searchEmpty();
									globalMqlInput.setSelectedIndex(pos);
									inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
									inputs.get(globalMqlInput.getSelectedIndex()).setText(mql);

									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
										}
									});

								} else {

									if (mql.startsWith("editor open form stack lines")) {

										try {
											openStackLines();
										} catch (Exception f) {}

									} else if (mql.startsWith("editor open form stack process")) {

										try {
											openStackProcess();
										} catch (Exception f) {}

									} else if (mql.startsWith("editor open form logs before archive")) {

										try {
											openLogsBeforeArchive();
										} catch (Exception f) {}

									}

								}
							}
						}
					}

				}
			}
		});

		globalTreeAdmin = treeAdmin;

		TreeExpansionListener treeExpandListener = new TreeExpansionListener() {

			public void treeExpanded(TreeExpansionEvent event) {

				TreePath path = event.getPath();

				treeExpansion.put(path.toString(), 0);

			}

			public void treeCollapsed(TreeExpansionEvent event) {

				TreePath path = event.getPath();

				treeExpansion.remove(path.toString());

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				closeChild(node);

			}

			public void closeChild(DefaultMutableTreeNode node) {

				int childCount = node.getChildCount();

				for (int i = 0; i < childCount; i++) {

					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);

					TreePath tp = new TreePath(childNode.getPath());
					treeExpansion.remove(tp.toString());

					closeChild(childNode);

				}

			}

		};

		treeAdmin.addTreeExpansionListener(treeExpandListener);

		treeAdmin.setRowHeight(20);
		treeAdmin.setBackground(new Color(61, 61, 61));
		treeAdmin.setOpaque(true);
		treeAdmin.setBorder(BorderFactory.createCompoundBorder(
				treeAdmin.getBorder(), 
				BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		JScrollPane treeView = new JScrollPane(treeAdmin);
		treeView.setBorder(null);
		treeView.setPreferredSize(new Dimension(240, 170));
		treeView.getVerticalScrollBar().setBackground(new Color(51,51,51));
		treeView.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		treeView.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		treeView.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		treeAdmin.addMouseListener ( new MouseAdapter ()
		{
			public void mousePressed ( MouseEvent e )
			{
				if ( SwingUtilities.isRightMouseButton ( e ) )
				{
					showPopupMenu(e, treeAdmin, null);
				}
			}

		} );
		treeAdmin.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						treeAdmin.getLastSelectedPathComponent();

				if (node == null) return;

				TreeRow r = (TreeRow) node.getUserObject();
				if (r.img.equals("images/mental.png")) {
					showPopupMenu(null, treeAdmin, treeAdmin.getSelectionPath());
				}

			}
		});

		return treeView;

	}

	public static String getSelectedButtonText(ButtonGroup buttonGroup) {
		for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();

			if (button.isSelected()) {
				return button.getText();
			}
		}

		return null;
	}

	public static JDialog stackLines = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void openStackLines() throws Exception {

		if (stackLines==null) {

			stackLines = new JDialog(globaljFrame, "Stack lines", true);
			stackLines.setSize(new Dimension(434, 270));
			stackLines.setBounds(0,0,434, 270);

			JPanel radioPanel = new JPanel();

			ButtonGroup bg = new ButtonGroup();
			JRadioButton br1 = new JRadioButton("SEC");
			JRadioButton br2 = new JRadioButton("MIN");
			JRadioButton br3 = new JRadioButton("HOUR");
			JRadioButton br4 = new JRadioButton("DAY");br4.setSelected(true);
			JRadioButton br5 = new JRadioButton("MONTH");
			JRadioButton br6 = new JRadioButton("YEAR");

			bg.add(br1);
			bg.add(br2);
			bg.add(br3);
			bg.add(br4);
			bg.add(br5);
			bg.add(br6);

			radioPanel.add(br1, BorderLayout.NORTH);
			radioPanel.add(br2, BorderLayout.NORTH);
			radioPanel.add(br3, BorderLayout.NORTH);
			radioPanel.add(br4, BorderLayout.NORTH);
			radioPanel.add(br5, BorderLayout.NORTH);
			radioPanel.add(br6, BorderLayout.NORTH);

			String curDateLeft = DateFx.diff(DateFx.curdate(), "DAY", "30");
			String curDateRight = DateFx.curdate();

			ComponentFormatDefaults defaults = ComponentFormatDefaults.getInstance();

			defaults.setFormat(ComponentFormatDefaults.Key.SELECTED_DATE_FIELD, new SimpleDateFormat("yyyy-MM-dd"));

			UtilDateModel modelLeft = new UtilDateModel();
			modelLeft.setSelected(true);
			modelLeft.setDate(Integer.parseInt(curDateLeft.substring(0,  4)),
					Integer.parseInt(curDateLeft.substring(5,  7))-1,
					Integer.parseInt(curDateLeft.substring(8,  10)));
			JDatePicker dpLeft = new JDatePicker(modelLeft);

			UtilDateModel modelRight = new UtilDateModel();
			modelRight.setSelected(true);
			modelRight.setDate(Integer.parseInt(curDateRight.substring(0,  4)),
					Integer.parseInt(curDateRight.substring(5,  7))-1,
					Integer.parseInt(curDateRight.substring(8,  10)));
			JDatePicker dpRight = new JDatePicker(modelRight);

			JPanel datePanelLabel = new JPanel(new BorderLayout());
			datePanelLabel.add(new JLabel("Start date"), BorderLayout.WEST);
			datePanelLabel.add(new JLabel("End date"), BorderLayout.EAST);
			datePanelLabel.setBorder(BorderFactory.createCompoundBorder(
					datePanelLabel.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel dtLeft = new JPanel(new BorderLayout());
			dtLeft.add(dpLeft, BorderLayout.NORTH);
			JPanel timeLeft = new JPanel(new BorderLayout());
			JComboBox jComboBoxLeftHour = new JComboBox();
			for(int i=0;i<=23;i++) {
				jComboBoxLeftHour.addItem(""+i);
			}
			jComboBoxLeftHour.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftHour, BorderLayout.NORTH);
			JComboBox jComboBoxLeftMin = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxLeftMin.addItem(""+i);
			}
			jComboBoxLeftMin.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftMin, BorderLayout.CENTER);
			JComboBox jComboBoxLeftSec = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxLeftSec.addItem(""+i);
			}
			jComboBoxLeftSec.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftSec, BorderLayout.SOUTH);
			dtLeft.add(timeLeft, BorderLayout.CENTER);

			JPanel dtRight = new JPanel(new BorderLayout());
			dtRight.add(dpRight, BorderLayout.NORTH);
			JPanel timeRight = new JPanel(new BorderLayout());
			JComboBox jComboBoxRightHour = new JComboBox();
			for(int i=0;i<=23;i++) {
				jComboBoxRightHour.addItem(""+i);
			}
			jComboBoxRightHour.setSelectedIndex(23);
			timeRight.add(jComboBoxRightHour, BorderLayout.NORTH);
			JComboBox jComboBoxRightMin = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxRightMin.addItem(""+i);
			}
			jComboBoxRightMin.setSelectedIndex(59);
			timeRight.add(jComboBoxRightMin, BorderLayout.CENTER);
			JComboBox jComboBoxRightSec = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxRightSec.addItem(""+i);
			}
			jComboBoxRightSec.setSelectedIndex(59);
			timeRight.add(jComboBoxRightSec, BorderLayout.SOUTH);
			dtRight.add(timeRight, BorderLayout.CENTER);

			JPanel datePanel = new JPanel(new BorderLayout());
			datePanel.add(dtLeft, BorderLayout.WEST);
			datePanel.add(dtRight, BorderLayout.EAST);

			JPanel dateP = new JPanel(new BorderLayout());
			dateP.add(datePanelLabel, BorderLayout.NORTH);
			dateP.add(datePanel, BorderLayout.CENTER);



			JPanel p = new JPanel(new BorderLayout());
			p.add(radioPanel, BorderLayout.NORTH);
			p.add(dateP, BorderLayout.CENTER);

			p.setBorder(BorderFactory.createCompoundBorder(
					p.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel pp = new JPanel(new BorderLayout());
			pp.add(p, BorderLayout.CENTER);
			JButton search = new JButton("Search ...");
			search.setPreferredSize(new Dimension(0, 35));
			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					try {

						int hRight = jComboBoxRightHour.getSelectedIndex();
						int mRight = jComboBoxRightMin.getSelectedIndex();
						int sRight = jComboBoxRightSec.getSelectedIndex();
						int hLeft = jComboBoxLeftHour.getSelectedIndex();
						int mLeft = jComboBoxLeftMin.getSelectedIndex();
						int sLeft = jComboBoxLeftSec.getSelectedIndex();
						sendToServer("restricted show activity "+getSelectedButtonText(bg)+"\n" + 
								"	\""+dpLeft.getFormattedTextField().getText()+" "+(hLeft<10?"0"+hLeft:hLeft)+":"+(mLeft<10?"0"+mLeft:mLeft)+":"+(sLeft<10?"0"+sLeft:sLeft)+"\"\n" + 
								"	\""+dpRight.getFormattedTextField().getText()+" "+(hRight<10?"0"+hRight:hRight)+":"+(mRight<10?"0"+mRight:mRight)+":"+(sRight<10?"0"+sRight:sRight)+"\"");
						stackLines.setVisible(false);

					} catch (Exception f) {Misc.log(f.getMessage()+"");};

				}
			});
			pp.add(search, BorderLayout.SOUTH);
			pp.setBorder(BorderFactory.createCompoundBorder(
					pp.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			stackLines.getContentPane().add(pp);

			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Dimension screenSize = toolkit.getScreenSize();
			final int x = (screenSize.width - stackLines.getWidth()) / 2;
			final int y = (screenSize.height - stackLines.getHeight()) / 2;
			stackLines.setLocation(x, y);

			stackLines.pack();
			stackLines.setVisible(true);

		} else {

			stackLines.setVisible(true);

		}

	}

	public static JDialog stackProcess = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void openStackProcess() throws Exception {

		if (stackProcess==null) {

			stackProcess = new JDialog(globaljFrame, "Stack Process", true);
			stackProcess.setSize(new Dimension(434, 300));
			stackProcess.setBounds(0,0,434, 300);

			JPanel radioPanel = new JPanel();

			ButtonGroup bg = new ButtonGroup();
			JRadioButton br1 = new JRadioButton("dt_exe");br1.setSelected(true);
			JRadioButton br2 = new JRadioButton("dt_closed");
			JRadioButton br3 = new JRadioButton("dt_error");
			JRadioButton br4 = new JRadioButton("dt_create");
			JRadioButton br5 = new JRadioButton("dt_lastattempt");

			bg.add(br1);
			bg.add(br2);
			bg.add(br3);
			bg.add(br4);
			bg.add(br5);

			radioPanel.add(br1, BorderLayout.NORTH);
			radioPanel.add(br2, BorderLayout.NORTH);
			radioPanel.add(br3, BorderLayout.NORTH);
			radioPanel.add(br4, BorderLayout.NORTH);
			radioPanel.add(br5, BorderLayout.NORTH);

			String curDateLeft = DateFx.diff(DateFx.curdate(), "DAY", "30");
			String curDateRight = DateFx.curdate();

			ComponentFormatDefaults defaults = ComponentFormatDefaults.getInstance();

			defaults.setFormat(ComponentFormatDefaults.Key.SELECTED_DATE_FIELD, new SimpleDateFormat("yyyy-MM-dd"));

			UtilDateModel modelLeft = new UtilDateModel();
			modelLeft.setSelected(true);
			modelLeft.setDate(Integer.parseInt(curDateLeft.substring(0,  4)),
					Integer.parseInt(curDateLeft.substring(5,  7))-1,
					Integer.parseInt(curDateLeft.substring(8,  10)));
			JDatePicker dpLeft = new JDatePicker(modelLeft);

			UtilDateModel modelRight = new UtilDateModel();
			modelRight.setSelected(true);
			modelRight.setDate(Integer.parseInt(curDateRight.substring(0,  4)),
					Integer.parseInt(curDateRight.substring(5,  7))-1,
					Integer.parseInt(curDateRight.substring(8,  10)));
			JDatePicker dpRight = new JDatePicker(modelRight);

			JPanel datePanelLabel = new JPanel(new BorderLayout());
			datePanelLabel.add(new JLabel("Start date"), BorderLayout.WEST);
			datePanelLabel.add(new JLabel("End date"), BorderLayout.EAST);
			datePanelLabel.setBorder(BorderFactory.createCompoundBorder(
					datePanelLabel.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel dtLeft = new JPanel(new BorderLayout());
			dtLeft.add(dpLeft, BorderLayout.NORTH);
			JPanel timeLeft = new JPanel(new BorderLayout());
			JComboBox jComboBoxLeftHour = new JComboBox();
			for(int i=0;i<=23;i++) {
				jComboBoxLeftHour.addItem(""+i);
			}
			jComboBoxLeftHour.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftHour, BorderLayout.NORTH);
			JComboBox jComboBoxLeftMin = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxLeftMin.addItem(""+i);
			}
			jComboBoxLeftMin.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftMin, BorderLayout.CENTER);
			JComboBox jComboBoxLeftSec = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxLeftSec.addItem(""+i);
			}
			jComboBoxLeftSec.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftSec, BorderLayout.SOUTH);
			dtLeft.add(timeLeft, BorderLayout.CENTER);

			JPanel dtRight = new JPanel(new BorderLayout());
			dtRight.add(dpRight, BorderLayout.NORTH);
			JPanel timeRight = new JPanel(new BorderLayout());
			JComboBox jComboBoxRightHour = new JComboBox();
			for(int i=0;i<=23;i++) {
				jComboBoxRightHour.addItem(""+i);
			}
			jComboBoxRightHour.setSelectedIndex(23);
			timeRight.add(jComboBoxRightHour, BorderLayout.NORTH);
			JComboBox jComboBoxRightMin = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxRightMin.addItem(""+i);
			}
			jComboBoxRightMin.setSelectedIndex(59);
			timeRight.add(jComboBoxRightMin, BorderLayout.CENTER);
			JComboBox jComboBoxRightSec = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxRightSec.addItem(""+i);
			}
			jComboBoxRightSec.setSelectedIndex(59);
			timeRight.add(jComboBoxRightSec, BorderLayout.SOUTH);
			dtRight.add(timeRight, BorderLayout.CENTER);

			JPanel datePanel = new JPanel(new BorderLayout());
			datePanel.add(dtLeft, BorderLayout.WEST);
			datePanel.add(dtRight, BorderLayout.EAST);

			JPanel dateP = new JPanel(new BorderLayout());
			dateP.add(datePanelLabel, BorderLayout.NORTH);
			dateP.add(datePanel, BorderLayout.CENTER);



			JPanel p = new JPanel(new BorderLayout());
			p.add(radioPanel, BorderLayout.NORTH);
			p.add(dateP, BorderLayout.CENTER);

			p.setBorder(BorderFactory.createCompoundBorder(
					p.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel ascPanel = new JPanel(new BorderLayout());
			ascPanel.add(p, BorderLayout.CENTER);
			JPanel ascNumNum  = new JPanel();
			JComboBox jComboBoxASC = new JComboBox();
			jComboBoxASC.addItem("ASC");
			jComboBoxASC.addItem("DESC");
			jComboBoxASC.setSelectedIndex(1);
			ascNumNum.add(jComboBoxASC);
			JTextField page = new JTextField("1");
			page.setPreferredSize(new Dimension(100, 20));
			ascNumNum.add(new JLabel("Page (start to 1) : "));
			ascNumNum.add(page);
			JTextField nbByPage = new JTextField("500");
			nbByPage.setPreferredSize(new Dimension(100, 20));
			ascNumNum.add(new JLabel("Row : "));
			ascNumNum.add(nbByPage);
			ascPanel.add(ascNumNum, BorderLayout.SOUTH);


			ButtonGroup bgt = new ButtonGroup();

			JPanel pp = new JPanel(new BorderLayout());
			pp.add(ascPanel, BorderLayout.CENTER);
			JButton search = new JButton("Search ...");
			search.setPreferredSize(new Dimension(0, 35));
			pp.add(search, BorderLayout.SOUTH);

			JPanel scriptFilterPanel = new JPanel(new BorderLayout());
			scriptFilterPanel.add(new JLabel("Scriptname/Flowname : "), BorderLayout.WEST);
			JTextField scriptFilter = new JTextField();
			addCutCopy(scriptFilter);
			scriptFilterPanel.add(scriptFilter, BorderLayout.CENTER);
			
			JPanel scriptPanel = new JPanel(new BorderLayout());
			scriptPanel.add(scriptFilterPanel, BorderLayout.NORTH);
			scriptPanel.add(pp, BorderLayout.CENTER);
			scriptPanel.setBorder(BorderFactory.createCompoundBorder(
					scriptPanel.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel typePanel = new JPanel(new BorderLayout());

			JPanel tPanel = new JPanel();

			JRadioButton b1 = new JRadioButton("wait");
			JRadioButton b2 = new JRadioButton("running");
			JRadioButton b3 = new JRadioButton("closed");
			JRadioButton b4 = new JRadioButton("error");b4.setSelected(true);

			bgt.add(b1);
			bgt.add(b2);
			bgt.add(b3);
			bgt.add(b4);

			tPanel.add(b1);
			tPanel.add(b2);
			tPanel.add(b3);
			tPanel.add(b4);

			typePanel.add(tPanel, BorderLayout.NORTH);
			typePanel.add(scriptPanel, BorderLayout.CENTER);

			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					try {

						int hRight = jComboBoxRightHour.getSelectedIndex();
						int mRight = jComboBoxRightMin.getSelectedIndex();
						int sRight = jComboBoxRightSec.getSelectedIndex();
						int hLeft = jComboBoxLeftHour.getSelectedIndex();
						int mLeft = jComboBoxLeftMin.getSelectedIndex();
						int sLeft = jComboBoxLeftSec.getSelectedIndex();
						sendToServer("restricted stack_search \""+getSelectedButtonText(bgt)+"\" \n" + 
								"	"+(!Misc.lrtrim(scriptFilter.getText()).equals("")?"\""+scriptFilter.getText().replace("\"", "\\\"")+"\"":"null")+" \n" + 
								"	\""+getSelectedButtonText(bg).substring(3)+"\" \n" + 
								"	\""+dpLeft.getFormattedTextField().getText()+" "+(hLeft<10?"0"+hLeft:hLeft)+":"+(mLeft<10?"0"+mLeft:mLeft)+":"+(sLeft<10?"0"+sLeft:sLeft)+"\"\n" + 
								"	\""+dpRight.getFormattedTextField().getText()+" "+(hRight<10?"0"+hRight:hRight)+":"+(mRight<10?"0"+mRight:mRight)+":"+(sRight<10?"0"+sRight:sRight)+"\"\n" +
								"	"+jComboBoxASC.getSelectedItem()+" "+page.getText()+" "+nbByPage.getText()+";");
						stackProcess.setVisible(false);

					} catch (Exception f) {Misc.log(f.getMessage()+"");};

				}
			});


			stackProcess.getContentPane().add(typePanel);

			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Dimension screenSize = toolkit.getScreenSize();
			final int x = (screenSize.width - stackProcess.getWidth()) / 2;
			final int y = (screenSize.height - stackProcess.getHeight()) / 2;
			stackProcess.setLocation(x, y);

			stackProcess.pack();
			stackProcess.setVisible(true);

		} else {

			stackProcess.setVisible(true);

		}

	}

	public static JDialog logsBeforeArchive = null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void openLogsBeforeArchive() throws Exception {

		if (logsBeforeArchive==null) {

			logsBeforeArchive = new JDialog(globaljFrame, "Logs before archive", true);
			logsBeforeArchive.setSize(new Dimension(434, 400));
			logsBeforeArchive.setBounds(0,0,434, 400);

			String curDateLeft = DateFx.diff(DateFx.curdate(), "DAY", "30");
			String curDateRight = DateFx.curdate();

			ComponentFormatDefaults defaults = ComponentFormatDefaults.getInstance();

			defaults.setFormat(ComponentFormatDefaults.Key.SELECTED_DATE_FIELD, new SimpleDateFormat("yyyy-MM-dd"));

			UtilDateModel modelLeft = new UtilDateModel();
			modelLeft.setSelected(true);
			modelLeft.setDate(Integer.parseInt(curDateLeft.substring(0,  4)),
					Integer.parseInt(curDateLeft.substring(5,  7))-1,
					Integer.parseInt(curDateLeft.substring(8,  10)));
			JDatePicker dpLeft = new JDatePicker(modelLeft);

			UtilDateModel modelRight = new UtilDateModel();
			modelRight.setSelected(true);
			modelRight.setDate(Integer.parseInt(curDateRight.substring(0,  4)),
					Integer.parseInt(curDateRight.substring(5,  7))-1,
					Integer.parseInt(curDateRight.substring(8,  10)));
			JDatePicker dpRight = new JDatePicker(modelRight);

			JPanel datePanelLabel = new JPanel(new BorderLayout());
			datePanelLabel.add(new JLabel("Start date"), BorderLayout.WEST);
			datePanelLabel.add(new JLabel("End date"), BorderLayout.EAST);
			datePanelLabel.setBorder(BorderFactory.createCompoundBorder(
					datePanelLabel.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel dtLeft = new JPanel(new BorderLayout());
			dtLeft.add(dpLeft, BorderLayout.NORTH);
			JPanel timeLeft = new JPanel(new BorderLayout());
			JComboBox jComboBoxLeftHour = new JComboBox();
			for(int i=0;i<=23;i++) {
				jComboBoxLeftHour.addItem(""+i);
			}
			jComboBoxLeftHour.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftHour, BorderLayout.NORTH);
			JComboBox jComboBoxLeftMin = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxLeftMin.addItem(""+i);
			}
			jComboBoxLeftMin.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftMin, BorderLayout.CENTER);
			JComboBox jComboBoxLeftSec = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxLeftSec.addItem(""+i);
			}
			jComboBoxLeftSec.setSelectedIndex(0);
			timeLeft.add(jComboBoxLeftSec, BorderLayout.SOUTH);
			dtLeft.add(timeLeft, BorderLayout.CENTER);

			JPanel dtRight = new JPanel(new BorderLayout());
			dtRight.add(dpRight, BorderLayout.NORTH);
			JPanel timeRight = new JPanel(new BorderLayout());
			JComboBox jComboBoxRightHour = new JComboBox();
			for(int i=0;i<=23;i++) {
				jComboBoxRightHour.addItem(""+i);
			}
			jComboBoxRightHour.setSelectedIndex(23);
			timeRight.add(jComboBoxRightHour, BorderLayout.NORTH);
			JComboBox jComboBoxRightMin = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxRightMin.addItem(""+i);
			}
			jComboBoxRightMin.setSelectedIndex(59);
			timeRight.add(jComboBoxRightMin, BorderLayout.CENTER);
			JComboBox jComboBoxRightSec = new JComboBox();
			for(int i=0;i<=59;i++) {
				jComboBoxRightSec.addItem(""+i);
			}
			jComboBoxRightSec.setSelectedIndex(59);
			timeRight.add(jComboBoxRightSec, BorderLayout.SOUTH);
			dtRight.add(timeRight, BorderLayout.CENTER);

			JPanel datePanel = new JPanel(new BorderLayout());
			datePanel.add(dtLeft, BorderLayout.WEST);
			datePanel.add(dtRight, BorderLayout.EAST);

			JPanel dateP = new JPanel(new BorderLayout());
			dateP.add(datePanelLabel, BorderLayout.NORTH);
			dateP.add(datePanel, BorderLayout.CENTER);

			JPanel ascPanel = new JPanel(new BorderLayout());
			ascPanel.add(dateP, BorderLayout.CENTER);
			JPanel ascNumNum  = new JPanel();
			JComboBox jComboBoxASC = new JComboBox();
			jComboBoxASC.addItem("ASC");
			jComboBoxASC.addItem("DESC");
			jComboBoxASC.setSelectedIndex(1);
			ascNumNum.add(jComboBoxASC);
			JTextField page = new JTextField("1");
			page.setPreferredSize(new Dimension(100, 20));
			ascNumNum.add(new JLabel("Page (start to 1) : "));
			ascNumNum.add(page);
			JTextField nbByPage = new JTextField("500");
			nbByPage.setPreferredSize(new Dimension(100, 20));
			ascNumNum.add(new JLabel("Row : "));
			ascNumNum.add(nbByPage);
			ascPanel.add(ascNumNum, BorderLayout.SOUTH);


			ButtonGroup bgt = new ButtonGroup();

			JPanel pp = new JPanel(new BorderLayout());
			pp.add(ascPanel, BorderLayout.CENTER);
			JButton search = new JButton("Search ...");
			search.setPreferredSize(new Dimension(0, 35));
			pp.add(search, BorderLayout.SOUTH);
			
			JPanel scriptFilterPanel = new JPanel(new BorderLayout());
			scriptFilterPanel.add(new JLabel("Scriptname : "), BorderLayout.WEST);
			JTextField scriptFilter = new JTextField();
			addCutCopy(scriptFilter);
			scriptFilterPanel.add(scriptFilter, BorderLayout.CENTER);
			
			JPanel keyClientPanel = new JPanel(new BorderLayout());
			keyClientPanel.add(scriptFilterPanel, BorderLayout.NORTH);
			JPanel labelKeyPanel = new JPanel();
			JLabel keyLabelClient = new JLabel("Key : ");keyLabelClient.setPreferredSize(new Dimension(70, 25));
			JTextField keyClient = new JTextField();
			addCutCopy(keyClient);
			keyClient.setPreferredSize(new Dimension(355, 25));
			labelKeyPanel.add(keyLabelClient);
			labelKeyPanel.add(keyClient);
			labelKeyPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			keyClientPanel.add(labelKeyPanel, BorderLayout.SOUTH);

			JPanel valClientPanel = new JPanel(new BorderLayout());
			valClientPanel.add(keyClientPanel, BorderLayout.NORTH);
			JPanel labelValPanel = new JPanel();
			JLabel valLabelClient = new JLabel("Value : ");valLabelClient.setPreferredSize(new Dimension(70, 25));
			JTextField valClient = new JTextField();
			addCutCopy(valClient);
			valClient.setPreferredSize(new Dimension(355, 25));
			labelValPanel.add(valLabelClient);
			labelValPanel.add(valClient);
			labelValPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			valClientPanel.add(labelValPanel, BorderLayout.SOUTH);

			JPanel msgErrPanel = new JPanel(new BorderLayout());
			msgErrPanel.add(valClientPanel, BorderLayout.NORTH);
			JPanel labelMsgErrPanel = new JPanel();
			JLabel msgErrLabelClient = new JLabel("Msg err : ");msgErrLabelClient.setPreferredSize(new Dimension(70, 25));
			JTextField msgErrClient = new JTextField();
			addCutCopy(msgErrClient);
			msgErrClient.setPreferredSize(new Dimension(355, 25));
			labelMsgErrPanel.add(msgErrLabelClient);
			labelMsgErrPanel.add(msgErrClient);
			labelMsgErrPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			msgErrPanel.add(labelMsgErrPanel, BorderLayout.SOUTH);

			JPanel scriptPanel = new JPanel(new BorderLayout());
			scriptPanel.add(msgErrPanel, BorderLayout.NORTH);
			scriptPanel.add(pp, BorderLayout.CENTER);
			scriptPanel.setBorder(BorderFactory.createCompoundBorder(
					scriptPanel.getBorder(), 
					BorderFactory.createEmptyBorder(8, 8, 8, 8)));

			JPanel typePanel = new JPanel(new BorderLayout());

			JPanel tPanel = new JPanel();

			JRadioButton b1 = new JRadioButton("ok");
			JRadioButton b2 = new JRadioButton("ko");b2.setSelected(true);
			JRadioButton b3 = new JRadioButton("*");

			bgt.add(b1);
			bgt.add(b2);
			bgt.add(b3);

			tPanel.add(b1);
			tPanel.add(b2);
			tPanel.add(b3);

			typePanel.add(tPanel, BorderLayout.NORTH);
			typePanel.add(scriptPanel, BorderLayout.CENTER);

			search.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					try {

						int hRight = jComboBoxRightHour.getSelectedIndex();
						int mRight = jComboBoxRightMin.getSelectedIndex();
						int sRight = jComboBoxRightSec.getSelectedIndex();
						int hLeft = jComboBoxLeftHour.getSelectedIndex();
						int mLeft = jComboBoxLeftMin.getSelectedIndex();
						int sLeft = jComboBoxLeftSec.getSelectedIndex();
						sendToServer("restricted log_search \""+getSelectedButtonText(bgt)+"\" \n" + 
								"	"+(!Misc.lrtrim(scriptFilter.getText()).equals("")?"\""+scriptFilter.getText().replace("\"", "\\\"")+"\"":"null")+" \n" + 
								"	\""+StringFx.lrtrim(keyClient.getText())+"\" \n" + 
								"	\""+StringFx.lrtrim(valClient.getText())+"\" \n" + 
								"	\""+StringFx.lrtrim(msgErrClient.getText())+"\" \n" + 
								"	\""+dpLeft.getFormattedTextField().getText()+" "+(hLeft<10?"0"+hLeft:hLeft)+":"+(mLeft<10?"0"+mLeft:mLeft)+":"+(sLeft<10?"0"+sLeft:sLeft)+"\"\n" + 
								"	\""+dpRight.getFormattedTextField().getText()+" "+(hRight<10?"0"+hRight:hRight)+":"+(mRight<10?"0"+mRight:mRight)+":"+(sRight<10?"0"+sRight:sRight)+"\"\n" +
								"	"+jComboBoxASC.getSelectedItem()+" "+page.getText()+" "+nbByPage.getText()+";");
						logsBeforeArchive.setVisible(false);

					} catch (Exception f) {Misc.log(f.getMessage()+"");};

				}
			});


			logsBeforeArchive.getContentPane().add(typePanel);

			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Dimension screenSize = toolkit.getScreenSize();
			final int x = (screenSize.width - logsBeforeArchive.getWidth()) / 2;
			final int y = (screenSize.height - logsBeforeArchive.getHeight()) / 2;
			logsBeforeArchive.setLocation(x, y);

			logsBeforeArchive.pack();
			logsBeforeArchive.setVisible(true);

		} else {

			logsBeforeArchive.setVisible(true);

		}

	}
	
	public static void addCutCopy(JTextField control) {
		
		control.addKeyListener(new KeyListener() {

			public String OS = Misc.os();

			@Override
			public void keyPressed(KeyEvent e) {

				if ((e.getKeyCode() == KeyEvent.VK_TAB)) {

					globalReplaceTxt.requestFocus();

					e.consume();

					return;

				}

				if (OS.equals("mac")) {

					if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						control.setSelectionStart(0);
						control.setSelectionEnd(control.getText().length());

					} else if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (control.getSelectedText()!=null && !control.getSelectedText().equals("")) {

							StringSelection stringSelection = new StringSelection(control.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);

							String str = control.getText();
							int startPos = control.getSelectionStart();
							int endPos = control.getSelectionEnd();

							String txt = str.substring(0, startPos)+str.substring(endPos);

							control.setText(txt);
							control.setCaretPosition(startPos);

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (control.getSelectedText()!=null && !control.getSelectedText().equals("")) {
							StringSelection stringSelection = new StringSelection(control.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);
						}

					} else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						if (control.getSelectedText()!=null && !control.getSelectedText().equals("")) {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								control.replaceSelection(data);
							} catch (Exception e1) {} 
						} else {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								control.getDocument().insertString(control.getCaretPosition(), data, null);
							} catch (Exception e1) {} 
						}

					}

				} else {

					if ((e.getKeyCode() == KeyEvent.VK_A) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						control.setSelectionStart(0);
						control.setSelectionEnd(control.getText().length());

					} else if ((e.getKeyCode() == KeyEvent.VK_X) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (control.getSelectedText()!=null && !control.getSelectedText().equals("")) {

							StringSelection stringSelection = new StringSelection(control.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);

							String str = control.getText();
							int startPos = control.getSelectionStart();
							int endPos = control.getSelectionEnd();

							String txt = str.substring(0, startPos)+str.substring(endPos);

							control.setText(txt);
							control.setCaretPosition(startPos);

						}

					} else if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						if (control.getSelectedText()!=null && !control.getSelectedText().equals("")) {
							StringSelection stringSelection = new StringSelection(control.getSelectedText());
							Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
							clpbrd.setContents(stringSelection, null);
						}

					} else if ((e.getKeyCode() == KeyEvent.VK_V) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						/*if (control.getSelectedText()!=null && !control.getSelectedText().equals("")) {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								control.replaceSelection(data);
							} catch (Exception e1) {} 
						} else {
							try {
								String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
								control.getDocument().insertString(control.getCaretPosition(), data, null);
							} catch (Exception e1) {} 
						}*/

					}

				}
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {



			}
		});
		
	}
	
	public static JTree treeDevel = null;

	public static JScrollPane initJtreeDevel() {

		globalFirstTreeNodeDevel = new TreeRow("MentDB", "", "db16x16.png", null, null, null, null, null);
		DefaultMutableTreeNode mutableTreeNode = new DefaultMutableTreeNode(globalFirstTreeNodeDevel);
		globalMutableTreeNodeDevel = mutableTreeNode;

		treeDevel = new JTree(mutableTreeNode);
		try {
			treeDevel.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(Misc.ini("conf/fontsize.conf", "CONF", "fontsize"))));
		} catch (Exception ee) {
			treeDevel.setFont(new Font("MonoSpaced", Font.PLAIN, 13));
		}
		treeDevel.setScrollsOnExpand(true);
		treeDevel.setCellRenderer(new MyTreeCellRenderer());
		treeDevel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							treeDevel.getLastSelectedPathComponent();

					if (node == null) return;
					if( node.getUserObject() instanceof TreeRow) {

						TreeRow nodeInfo = (TreeRow) node.getUserObject();
						String mql = nodeInfo.mql;
						String direct = nodeInfo.direct;

						if (mql!=null) {
							if (direct.equals("1")) {
								try {
									sendToServer(mql);
								} catch (Exception f) {};
							} else {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(mql);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
						}
					}

				}
			}
		});

		globalTreeDevel = treeDevel;

		TreeExpansionListener treeExpandListener = new TreeExpansionListener() {

			public void treeExpanded(TreeExpansionEvent event) {

				TreePath path = event.getPath();

				treeExpansion.put(path.toString(), 0);

			}

			public void treeCollapsed(TreeExpansionEvent event) {

				TreePath path = event.getPath();

				treeExpansion.remove(path.toString());

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				closeChild(node);

			}

			public void closeChild(DefaultMutableTreeNode node) {

				int childCount = node.getChildCount();

				for (int i = 0; i < childCount; i++) {

					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);

					TreePath tp = new TreePath(childNode.getPath());
					treeExpansion.remove(tp.toString());

					closeChild(childNode);

				}

			}

		};

		treeDevel.addTreeExpansionListener(treeExpandListener);

		treeDevel.setRowHeight(20);
		treeDevel.setBackground(new Color(61, 61, 61));
		treeDevel.setOpaque(true);
		treeDevel.setBorder(BorderFactory.createCompoundBorder(
				treeDevel.getBorder(), 
				BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		JScrollPane treeView = new JScrollPane(treeDevel);
		treeView.setBorder(null);
		treeView.setPreferredSize(new Dimension(240, 100));
		treeView.getVerticalScrollBar().setBackground(new Color(51,51,51));
		treeView.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		treeView.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		treeView.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		treeDevel.addMouseListener ( new MouseAdapter ()
		{
			public void mousePressed ( MouseEvent e )
			{
				if ( SwingUtilities.isRightMouseButton ( e ) )
				{
					showPopupMenuDevel(e, treeDevel, null);
				}
			}

		} );
		treeDevel.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						treeDevel.getLastSelectedPathComponent();

				if (node == null) return;

				/*TreeRow r = (TreeRow) node.getUserObject();
				if (r.img.equals("images/mental.png")) {
					showPopupMenuDevel(null, treeDevel, treeDevel.getSelectionPath());
				}*/

			}
		});

		return treeView;

	}
	
	public static JTree treeConfig = null;

	public static JScrollPane initJtreeConfig() {

		globalFirstTreeNodeConfig = new TreeRow("MentDB", "", "db16x16.png", null, null, null, null, null);
		DefaultMutableTreeNode mutableTreeNode = new DefaultMutableTreeNode(globalFirstTreeNodeConfig);
		globalMutableTreeNodeConfig = mutableTreeNode;

		treeConfig = new JTree(mutableTreeNode);
		try {
			treeConfig.setFont(new Font("MonoSpaced", Font.PLAIN, Integer.parseInt(Misc.ini("conf/fontsize.conf", "CONF", "fontsize"))));
		} catch (Exception ee) {
			treeConfig.setFont(new Font("MonoSpaced", Font.PLAIN, 13));
		}
		treeConfig.setScrollsOnExpand(true);
		treeConfig.setCellRenderer(new MyTreeCellRenderer());
		treeConfig.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)
							treeConfig.getLastSelectedPathComponent();

					if (node == null) return;
					if( node.getUserObject() instanceof TreeRow) {

						TreeRow nodeInfo = (TreeRow) node.getUserObject();
						String mql = nodeInfo.mql;
						String direct = nodeInfo.direct;

						if (mql!=null) {
							if (direct.equals("1")) {
								try {
									sendToServer(mql);
								} catch (Exception f) {};
							} else {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(mql);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
						}
					}

				}
			}
		});

		globalTreeConfig = treeConfig;

		TreeExpansionListener treeExpandListener = new TreeExpansionListener() {

			public void treeExpanded(TreeExpansionEvent event) {

				TreePath path = event.getPath();

				treeExpansion.put(path.toString(), 0);

			}

			public void treeCollapsed(TreeExpansionEvent event) {

				TreePath path = event.getPath();

				treeExpansion.remove(path.toString());

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				closeChild(node);

			}

			public void closeChild(DefaultMutableTreeNode node) {

				int childCount = node.getChildCount();

				for (int i = 0; i < childCount; i++) {

					DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) node.getChildAt(i);

					TreePath tp = new TreePath(childNode.getPath());
					treeExpansion.remove(tp.toString());

					closeChild(childNode);

				}

			}

		};

		treeConfig.addTreeExpansionListener(treeExpandListener);

		treeConfig.setRowHeight(20);
		treeConfig.setBackground(new Color(61, 61, 61));
		treeConfig.setOpaque(true);
		treeConfig.setBorder(BorderFactory.createCompoundBorder(
				treeConfig.getBorder(), 
				BorderFactory.createEmptyBorder(4, 4, 4, 4)));
		JScrollPane treeView = new JScrollPane(treeConfig);
		treeView.setBorder(null);
		treeView.setPreferredSize(new Dimension(240, 170));
		treeView.getVerticalScrollBar().setBackground(new Color(51,51,51));
		treeView.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		treeView.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		treeView.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		treeConfig.addMouseListener ( new MouseAdapter ()
		{
			public void mousePressed ( MouseEvent e )
			{
				if ( SwingUtilities.isRightMouseButton ( e ) )
				{
					showPopupMenu(e, treeConfig, null);
				}
			}

		} );
		treeConfig.addTreeSelectionListener(new TreeSelectionListener() {

			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)
						treeConfig.getLastSelectedPathComponent();

				if (node == null) return;

				TreeRow r = (TreeRow) node.getUserObject();
				if (r.img.equals("images/mental.png")) {
					showPopupMenu(null, treeConfig, treeConfig.getSelectionPath());
				}

			}
		});

		return treeView;

	}

	public static class HistoryListCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -7799441088157759804L;
		private JLabel label;

		HistoryListCellRenderer() {
			label = new JLabel();
		}

		@Override
		public Component getListCellRendererComponent(
				@SuppressWarnings("rawtypes") JList list,
				Object value,
				int index,
				boolean selected,
				boolean expanded) {

			label.setText((String) value);
			label.setIcon(new ImageIcon("images"+File.separator+"start_b.png"));
			label.setForeground(Color.WHITE);

			return label;
		}
	}

	public static class MsgListCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -7799441088157759804L;
		private JLabel label;

		MsgListCellRenderer() {
			label = new JLabel();
		}

		@Override
		public Component getListCellRendererComponent(
				@SuppressWarnings("rawtypes") JList list,
				Object value,
				int index,
				boolean selected,
				boolean expanded) {

			label.setText((String) value);
			label.setIcon(new ImageIcon("images"+File.separator+"send.png"));

			return label;
		}
	}

	//The tree
	public static Object[] treeAndUsers(JFrame frame) {

		JScrollPane treeViewAdmin = initJtreeAdmin();
		JScrollPane treeViewDevel = initJtreeDevel();
		JScrollPane treeViewConfig = initJtreeConfig();

		// History
		DefaultListModel<String> modelHistory = new DefaultListModel<String>();
		globalModelHistory = modelHistory;
		JList<String> listHistory = new JList<String>(modelHistory);
		listHistory.setCellRenderer(new HistoryListCellRenderer());

		//listHistory.setCellRenderer(new SelectedListCellRenderer());
		listHistory.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<String> listHistory = (JList<String>)evt.getSource();
				if (evt.getClickCount() == 2) {

					// Double-click detected
					int index = listHistory.locationToIndex(evt.getPoint());

					if (index>-1) {
						int pos = searchEmpty();
						globalMqlInput.setSelectedIndex(pos);
						inputs.get(globalMqlInput.getSelectedIndex()).setText(listHistory.getSelectedValue());
						inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
					}

				}
			}
		});
		listHistory.setSelectionBackground(Color.WHITE);
		listHistory.setFont(new Font("monospaced", Font.PLAIN, 13));
		listHistory.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		listHistory.setLayoutOrientation(JList.VERTICAL);
		listHistory.setVisibleRowCount(5);
		listHistory.setForeground(new Color(255, 255 ,255));
		listHistory.setBackground(new Color(61, 61 ,61));
		listHistory.setOpaque(true);
		JScrollPane listScrollerHistory = new JScrollPane(listHistory);
		listScrollerHistory.setBorder(null);
		listScrollerHistory.getVerticalScrollBar().setBackground(new Color(51,51,51));
		listScrollerHistory.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		listScrollerHistory.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		listScrollerHistory.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		listHistory.setBorder(BorderFactory.createCompoundBorder(
				listHistory.getBorder(), 
				BorderFactory.createEmptyBorder(8, 8, 8, 8)));

		KeyListener keyListener = new KeyListener() {
			public void keyPressed(KeyEvent keyEvent) {
				if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));
					
					new Thread(new Runnable() {

						public void run(){

							refreshTreeDevel();

						}

					}).start();
					
				}
			}

			public void keyReleased(KeyEvent keyEvent) {

			}

			public void keyTyped(KeyEvent keyEvent) {

			}
		};

		searchText = new JTextField();
		searchText.addKeyListener(keyListener);
		searchText.setPreferredSize(new Dimension(0, 35));
		searchText.setText("Type your search and press [ENTER]");
		searchText.setForeground(Color.GRAY);
		searchText.setBackground(new Color(31,31,31));
		searchText.setBorder(new EmptyBorder(10, 10, 10, 10));
		searchText.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				if (searchText.getText().equals("Type your search and press [ENTER]")) {
					searchText.setText("");
					searchText.setForeground(Color.WHITE);
				}
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (Misc.lrtrim(searchText.getText()).equals("")) {
					searchText.setText("Type your search and press [ENTER]");
					searchText.setForeground(Color.GRAY);
				}
			}
		});
		addCutCopy(searchText);

		JPanel editorTitle1 = new JPanel(new BorderLayout());


		clusterList = new JComboBox<String>();
		clusterList.setBackground(new Color(21, 21, 21));
		clusterList.setPreferredSize(new Dimension(70, 40));
		clusterList.setFont(new Font("MonoSpaced", Font.BOLD, 14));
		clusterList.setForeground(new Color(255, 125, 0));
		clusterList.setBorder(null);
		((JLabel)clusterList.getRenderer()).setHorizontalAlignment(SwingConstants.RIGHT);
		clusterList.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				if (selected_cluster!=null) {

					busy = true;

					globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));
					globalExec.setText("WAIT... ");
					
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {

							try {

								selected_cluster = (String)clusterList.getSelectedItem();
								
								try {refreshTreeDevel();} catch (Exception ef) {};
								try {refreshTreeAdmin();} catch (Exception ef) {};
								try {refreshTreeConfig();} catch (Exception ef) {};
								try {globalExec.setText(getServerMode()+"   ");} catch (Exception ef) {};
								try {
									if (selected_cluster.equals("[MAIN_SERVER]")) editor_title.setText(" mentdb://"+getServerName()+"/");
									else editor_title.setText(" mentdb://"+server_name_origin+"/"+getServerName()+"/");
								} catch (Exception ef) {};
								try {sendToServer("restricted maintenance_get");} catch (Exception ef) {};
								
								

								globalTreeAdmin.expandRow(1);
								globalTreeDevel.expandRow(1);
								globalTreeConfig.expandRow(1);

								globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

								busy = false;

								if (!first_call_clusterList) {
									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											JOptionPane.showMessageDialog(globaljFrame, "You are connected to: '"+selected_cluster+"'.");
										}
									});
								}

								first_call_clusterList = false;

							} catch (Exception ee) {

								globalExec.setIcon(new ImageIcon("images"+File.separator+"flashgg.png"));

								busy = false;

							}

						}
					});

				}

			} 
		} );

		JButton refreshButtonAdmin = new JButton();
		refreshButtonAdmin.setIcon(new ImageIcon("images"+File.separator+"refresh.png"));
		refreshButtonAdmin.setBackground(new Color(51,51,51));
		refreshButtonAdmin.setForeground(Color.WHITE);
		refreshButtonAdmin.setOpaque(true);
		refreshButtonAdmin.setBorderPainted(false);
		refreshButtonAdmin.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try {

					globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							refreshTreeAdmin();
						}
					});

				} catch (Exception f) {}
			} 
		} );

		JButton refreshButtonDevel = new JButton();
		refreshButtonDevel.setIcon(new ImageIcon("images"+File.separator+"refresh.png"));
		refreshButtonDevel.setBackground(new Color(51,51,51));
		refreshButtonDevel.setForeground(Color.WHITE);
		refreshButtonDevel.setOpaque(true);
		refreshButtonDevel.setBorderPainted(false);
		refreshButtonDevel.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try {

					globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							refreshTreeDevel();
						}
					});

				} catch (Exception f) {}
			} 
		} );

		JButton refreshButtonConfig = new JButton();
		refreshButtonConfig.setIcon(new ImageIcon("images"+File.separator+"refresh.png"));
		refreshButtonConfig.setBackground(new Color(51,51,51));
		refreshButtonConfig.setForeground(Color.WHITE);
		refreshButtonConfig.setOpaque(true);
		refreshButtonConfig.setBorderPainted(false);
		refreshButtonConfig.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				try {

					globalExec.setIcon(new ImageIcon("images"+File.separator+"flashr.png"));

					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							refreshTreeConfig();
						}
					});

				} catch (Exception f) {}
			} 
		} );

		JPanel search_refresh = new JPanel(new BorderLayout());
		search_refresh.add(searchText, BorderLayout.CENTER);
		search_refresh.add(refreshButtonDevel, BorderLayout.EAST);
		search_refresh.setBackground(new Color(61, 61, 61));
		search_refresh.setOpaque(true);

		editorTitle1.add(search_refresh, BorderLayout.SOUTH);

		JPanel editorTitle2 = new JPanel(new BorderLayout());
		editorTitle2.add(refreshButtonAdmin, BorderLayout.CENTER);
		editorTitle2.setBackground(new Color(41,41,41));
		editorTitle2.setOpaque(true);

		JPanel editorTitle3 = new JPanel(new BorderLayout());
		editorTitle3.add(refreshButtonConfig, BorderLayout.CENTER);
		editorTitle3.setBackground(new Color(41,41,41));
		editorTitle3.setOpaque(true);

		Date todaysDate = new Date();
		DateFormat df6 = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss", Locale.US);

		JLabel labelInfosConfig = new JLabel(df6.format(todaysDate));
		labelInfosConfig.setFont(new Font(labelInfosConfig.getFont().getName(), Font.PLAIN, 12));
		labelInfosConfig.setIcon(new ImageIcon("images"+File.separator+"info.png"));
		labelInfosConfig.setHorizontalAlignment(JLabel.LEFT);
		labelInfosConfig.setBorder(new EmptyBorder(5, 5, 5, 5));
		labelInfosConfig.setForeground(Color.WHITE);

		JLabel labelInfosDevel = new JLabel(df6.format(todaysDate));
		labelInfosDevel.setFont(new Font(labelInfosDevel.getFont().getName(), Font.PLAIN, 12));
		labelInfosDevel.setIcon(new ImageIcon("images"+File.separator+"info.png"));
		labelInfosDevel.setHorizontalAlignment(JLabel.LEFT);
		labelInfosDevel.setBorder(new EmptyBorder(5, 5, 5, 5));
		labelInfosDevel.setForeground(Color.WHITE);

		JLabel labelInfosAdmin = new JLabel(df6.format(todaysDate));
		labelInfosAdmin.setFont(new Font(labelInfosAdmin.getFont().getName(), Font.PLAIN, 12));
		labelInfosAdmin.setIcon(new ImageIcon("images"+File.separator+"info.png"));
		labelInfosAdmin.setHorizontalAlignment(JLabel.LEFT);
		labelInfosAdmin.setBorder(new EmptyBorder(5, 5, 5, 5));
		labelInfosAdmin.setForeground(Color.WHITE);

		JPanel configPanel = new JPanel(new BorderLayout());
		configPanel.add(editorTitle3, BorderLayout.NORTH);
		configPanel.add(treeViewConfig, BorderLayout.CENTER);
		configPanel.add(labelInfosConfig, BorderLayout.SOUTH);
		configPanel.setBackground(new Color(41,41,41));
		configPanel.setOpaque(true);

		JPanel develPanel = new JPanel(new BorderLayout());
		develPanel.add(editorTitle1, BorderLayout.NORTH);
		develPanel.add(treeViewDevel, BorderLayout.CENTER);

		JButton jbhistory = new JButton("History");
		jbhistory.setBackground(new Color(51,51,51));
		jbhistory.setForeground(Color.WHITE);
		jbhistory.setOpaque(true);
		jbhistory.setBorderPainted(false);
		JPanel historyPanel = new JPanel(new BorderLayout());
		historyPanel.add(jbhistory, BorderLayout.NORTH);
		historyPanel.add(listScrollerHistory, BorderLayout.CENTER);
		historyPanel.add(labelInfosDevel, BorderLayout.SOUTH);
		historyPanel.setBackground(new Color(41,41,41));
		historyPanel.setOpaque(true);

		JPanel adminPanel = new JPanel(new BorderLayout());
		adminPanel.add(editorTitle2, BorderLayout.NORTH);
		adminPanel.add(treeViewAdmin, BorderLayout.CENTER);
		adminPanel.add(labelInfosAdmin, BorderLayout.SOUTH);
		adminPanel.setBackground(new Color(41,41,41));
		adminPanel.setOpaque(true);

		Object[] r = new Object[4];
		r[0] = develPanel;
		r[1] = configPanel;
		r[2] = adminPanel;
		r[3] = historyPanel;

		return r;

	}

	public static void showPopupMenu(MouseEvent e, JTree tree, TreePath p) {

		TreePath path = null;
		if (e!=null) {
			path = tree.getPathForLocation ( e.getX (), e.getY () );
		} else {
			path = p;
		}

		Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
		if (e==null || (pathBounds != null && pathBounds.contains ( e.getX (), e.getY () )) )
		{
			JPopupMenu menu = new JPopupMenu ();
			JMenu menuItem = new JMenu("Other(s) ...");

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

			if( node.getUserObject() instanceof TreeRow) {

				TreeRow r = (TreeRow) node.getUserObject();

				Vector<String> titles = r.titles;
				Vector<String> actions = r.actions;
				Vector<String> directs = r.directs;
				if (titles!=null) {
					for(iTitle=0; iTitle<titles.size();iTitle++) {

						if (iTitle==12) {
							menu.add(menuItem);
						}

						JMenuItem item = new JMenuItem ( titles.get(iTitle) );

						item.setActionCommand(directs.get(iTitle)+""+actions.get(iTitle));
						item.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								String direct = item.getActionCommand().substring(0, 1);
								String cmd = item.getActionCommand().substring(1);

								try {
									if (direct.equals("0")) {
										int pos = searchEmpty();
										globalMqlInput.setSelectedIndex(pos);
										inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
										inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

										SwingUtilities.invokeLater(new Runnable() {
											public void run() {
												inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
											}
										});
									}
									else sendToServer(cmd);
								} catch (Exception f) {}

							}
						});
						if (iTitle<12) {
							menu.add ( item );
						} else {
							menuItem.add ( item );
						}
					}

					menu.show ( tree, pathBounds.x, pathBounds.y + pathBounds.height );
				}

			} else if (!is_restricted) {

				JMenuItem item = new JMenuItem ( "AI Firstname" );

				item.setActionCommand("1ai firstname;");
				item.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						String direct = item.getActionCommand().substring(0, 1);
						String cmd = item.getActionCommand().substring(1);

						try {
							if (direct.equals("0")) {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
							else sendToServer(cmd);
						} catch (Exception f) {}

					}
				});
				menu.add ( item );
				JMenuItem item2 = new JMenuItem ( "AI Lastname" );

				item2.setActionCommand("1ai lastname;");
				item2.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						String direct = item2.getActionCommand().substring(0, 1);
						String cmd = item2.getActionCommand().substring(1);

						try {
							if (direct.equals("0")) {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
							else sendToServer(cmd);
						} catch (Exception f) {}

					}
				});
				menu.add ( item2 );
				JMenuItem item5 = new JMenuItem ( "Identity" );

				item5.setActionCommand("1metric system;");
				item5.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						String direct = item5.getActionCommand().substring(0, 1);
						String cmd = item5.getActionCommand().substring(1);

						try {
							if (direct.equals("0")) {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
							else sendToServer(cmd);
						} catch (Exception f) {}

					}
				});
				menu.add ( item5 );
				JMenuItem item3 = new JMenuItem ( "Stop ID" );

				item3.setActionCommand("1id;");
				item3.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						String direct = item3.getActionCommand().substring(0, 1);
						String cmd = item3.getActionCommand().substring(1);

						if (direct.equals("0")) {
							int pos = searchEmpty();
							globalMqlInput.setSelectedIndex(pos);
							inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
							inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
								}
							});
						}
						else sendToServer(cmd);

					}
				});
				menu.add ( item3 );
				JMenuItem item4 = new JMenuItem ( "Version" );

				item4.setActionCommand("1version;");
				item4.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						String direct = item4.getActionCommand().substring(0, 1);
						String cmd = item4.getActionCommand().substring(1);

						try {
							if (direct.equals("0")) {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
							else sendToServer(cmd);
						} catch (Exception f) {}

					}
				});
				menu.add ( item4 );

				menu.show ( tree, pathBounds.x, pathBounds.y + pathBounds.height );

			}
		}

	}

	public static void add_right_click(String s, Vector<String> titles, Vector<String> actions, Vector<String> directs) throws ParseException, Exception {

		if (actions.contains("script generate delay \""+s.replace("\"", "\\\"")+"\";")
				|| actions.contains("restricted execute \""+s.replace("\"", "\\\"")+"\";")) {
			return;
		}
		
		if (is_restricted) {
			
			titles.add("Execute immediate (with default parameters)");
			actions.add("restricted eval \""+s.replace("\"", "\\\"")+"\";");
			directs.add("1");

			titles.add("Execute (modify parameters)");
			actions.add("restricted execute \""+s.replace("\"", "\\\"")+"\";");
			directs.add("1");

			titles.add("Stack (modify parameters)");
			actions.add("restricted stack \""+s.replace("\"", "\\\"")+"\";");
			directs.add("1");

			titles.add("Include (modify parameters)");
			actions.add("restricted include \""+s.replace("\"", "\\\"")+"\";");
			directs.add("1");

			titles.add("Call (modify parameters)");
			actions.add("restricted call \""+s.replace("\"", "\\\"")+"\";");
			directs.add("1");
			
			return;
			
		}

		titles.add("Edit");
		actions.add("in editor {\n	script generate merge \""+s.replace("\"", "\\\"")+"\";\n};");
		directs.add("1");

		titles.add("Set delay");
		actions.add("script generate delay \""+s.replace("\"", "\\\"")+"\";");
		directs.add("1");

		String list_group = "";
		JSONParser jsonParser = new JSONParser();
		JSONObject cluster_list_a = (JSONObject) jsonParser.parse(execute_in_cluster("script show groups \""+s.replace("\"", "\\\"")+"\";"));
		for(Object o:cluster_list_a.keySet()) {
			list_group+=","+o;
		}
		if (list_group.length()>0) list_group = list_group.substring(1);

		titles.add("Commit");
		actions.add("version commit_script \""+list_group+"\" \""+s.replace("\"", "\\\"")+"\" \"first commit\";");
		directs.add("0");

		titles.add("Check or deploy");

		String list_cluster = "";
		for(int i=0;i<clusterList.getItemCount();i++) {
			if (!clusterList.getItemAt(i).equals("[MAIN_SERVER]")) {
				list_cluster+="|"+clusterList.getItemAt(i);
			}
		}
		if (list_cluster.length()>0) list_cluster = list_cluster.substring(1);

		actions.add("execute \"src.tools.deploy.exe\"\n" + 
				"	\"[startsWith]\" \""+s.replace("\"", "\\\"")+"\"\n" + 
				"	\"[mentdbCmId]\" \""+list_cluster+"\"\n" + 
				"	\"[groups]\" \""+list_group+"\"\n" + 
				"	\"[check]\" \"1\"\n" + 
				";");
		directs.add("0");
		
		titles.add("Execute immediate");
		actions.add("eval (script generate execute \""+s.replace("\"", "\\\"")+"\";)");
		directs.add("1");

		titles.add("Execute (clipboard)");
		actions.add("in clipboard {\n	script generate execute \""+s.replace("\"", "\\\"")+"\";\n};");
		directs.add("1");

		titles.add("Stack (clipboard)");
		actions.add("in clipboard {\n	script generate stack \""+s.replace("\"", "\\\"")+"\";\n};");
		directs.add("1");

		titles.add("Include (clipboard)");
		actions.add("in clipboard {\n	script generate include \""+s.replace("\"", "\\\"")+"\";\n};");
		directs.add("1");

		titles.add("Call (clipboard)");
		actions.add("in clipboard {\n	script generate call \""+s.replace("\"", "\\\"")+"\";\n};");
		directs.add("1");

		titles.add("Concat (clipboard)");
		actions.add("in clipboard {\n	script generate sub_include \""+s.replace("\"", "\\\"")+"\";\n};");
		directs.add("1");

		titles.add("Set priority");
		actions.add("node iobject \"script["+s.replace("\"", "\\\"")+"]\" / priority 2 STR");
		directs.add("0");

		titles.add("Set nb in thread");
		actions.add("node iobject \"script["+s.replace("\"", "\\\"")+"]\" / nb_in_thread 2 STR");
		directs.add("0");

		titles.add("Export ...");
		actions.add("-> \"[mql]\" \"\";\n" + 
				"json load \"list\" (script show);\n" + 
				"json parse_obj \"list\" \"/\" \"[scriptname]\" \"[val]\" {\n" + 
				"\n" + 
				"	if (string starts_with [scriptname] \""+s.replace("\"", "\\\"")+"\") {\n" + 
				"		concat_var \"[mql]\" [_n_] [_n_] (script generate create [scriptname]);\n" + 
				"	};\n" + 
				"\n" + 
				"};\n" + 
				"in editor {[mql]};");
		directs.add("0");

		titles.add("Copy");
		actions.add("script copy \""+s.replace("\"", "\\\"")+"\" "+Misc.atom(s, Misc.size(s, "."), ".").toLowerCase()+" \""+s.substring(0, s.lastIndexOf(".")).replace("\"", "\\\"")+"\";");
		directs.add("0");

		titles.add("Copy all");
		actions.add("script copy all \""+s.replace("\"", "\\\"")+"\" \""+s.replace("\"", "\\\"")+"\";");
		directs.add("0");

		titles.add("Rename");
		actions.add("script rename \""+s.replace("\"", "\\\"")+"\" "+Misc.atom(s, Misc.size(s, "."), ".").toLowerCase()+" \""+s.substring(0, s.lastIndexOf(".")).replace("\"", "\\\"")+"\";");
		directs.add("0");

		titles.add("Rename all");
		actions.add("script rename all \""+s.replace("\"", "\\\"")+"\" \""+s.replace("\"", "\\\"")+"\";");
		directs.add("0");

		titles.add("Show user");
		actions.add("script show users \""+s.replace("\"", "\\\"")+"\";");
		directs.add("1");

		titles.add("Show group");
		actions.add("script show groups \""+s.replace("\"", "\\\"")+"\";");
		directs.add("1");

		titles.add("Grant group");
		actions.add("group grant script \""+s.replace("\"", "\\\"")+"\" \"<<<group>>>\";");
		directs.add("0");

		titles.add("Ungrant group");
		actions.add("group ungrant script \""+s.replace("\"", "\\\"")+"\" \"<<<group>>>\";");
		directs.add("0");

		titles.add("Grant group (starts with)");
		actions.add("group grant_all script \""+s.replace("\"", "\\\"")+"\" \"<<<group>>>\";");
		directs.add("0");

		titles.add("Ungrant group (starts with)");
		actions.add("group ungrant_all script \""+s.replace("\"", "\\\"")+"\" \"<<<group>>>\";");
		directs.add("0");

		titles.add("History...");
		actions.add("sql select \"MENTDB\" \"select * from history \n"
				+ "where \n"
				+ "	mql_script like '%"+(s.replace("\"", "\\\""))+"%'\n"
				+ "	-- and dt_insert between '"+DateFx.diff(DateFx.sysdate(), "DAY", "30")+" 00:00:00' and '"+DateFx.sysdate()+" 23:59:59'\n"
				+ "	-- and mql_user = 'admin'\n"
				+ "order by dt_insert desc, mql_script\n"
				+ "limit 0, 100\" \n"
				+ "\"MQL_HISTORY\";");
		directs.add("0");

		titles.add("Add a job");
		actions.add("job add \"<<<jobId>>>\" \""+s.replace("\"", "\\\"")+"\" \"0 30 * * * ?\" true;\njob resume \"<<<jobId>>>\";");
		directs.add("0");

		titles.add("Delete");
		actions.add("script delete \""+s.replace("\"", "\\\"")+"\";");
		directs.add("0");

		titles.add("Delete all");
		actions.add("script delete all \""+s.replace("\"", "\\\"")+"\";");
		directs.add("0");

	}

	public static void showPopupMenuDevel(MouseEvent e, JTree tree, TreePath p) {

		try {

			TreePath path = null;
			if (e!=null) {
				path = tree.getPathForLocation ( e.getX (), e.getY () );
			} else {
				path = p;
			}

			Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
			if (e==null || (pathBounds != null && pathBounds.contains ( e.getX (), e.getY () )) )
			{

				JPopupMenu menu = new JPopupMenu ();
				JMenu menuItem = new JMenu("Other(s) ...");

				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

				if( node.getUserObject() instanceof TreeRow) {

					TreeRow r = (TreeRow) node.getUserObject();

					Vector<String> titles = r.titles;
					Vector<String> actions = r.actions;
					Vector<String> directs = r.directs;

					if (!r.s.equals("")) {

						add_right_click(r.s, titles, actions, directs);

					}

					if (titles!=null) {
						for(iTitle=0; iTitle<titles.size();iTitle++) {

							if (iTitle==12) {
								menu.add(menuItem);
							}

							JMenuItem item = new JMenuItem ( titles.get(iTitle) );

							item.setActionCommand(directs.get(iTitle)+""+actions.get(iTitle));
							item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {

									String direct = item.getActionCommand().substring(0, 1);
									String cmd = item.getActionCommand().substring(1);

									try {
										if (direct.equals("0")) {
											int pos = searchEmpty();
											globalMqlInput.setSelectedIndex(pos);
											inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
											inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

											SwingUtilities.invokeLater(new Runnable() {
												public void run() {
													inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
												}
											});
										}
										else sendToServer(cmd);
									} catch (Exception f) {}

								}
							});
							if (iTitle<12) {
								menu.add ( item );
							} else {
								menuItem.add ( item );
							}
						}

						menu.show ( tree, pathBounds.x, pathBounds.y + pathBounds.height );
					}

				} else if (!is_restricted) {

					JMenuItem item = new JMenuItem ( "AI Firstname" );

					item.setActionCommand("1ai firstname;");
					item.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							String direct = item.getActionCommand().substring(0, 1);
							String cmd = item.getActionCommand().substring(1);

							try {
								if (direct.equals("0")) {
									int pos = searchEmpty();
									globalMqlInput.setSelectedIndex(pos);
									inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
									inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
										}
									});
								}
								else sendToServer(cmd);
							} catch (Exception f) {}

						}
					});
					menu.add ( item );
					JMenuItem item2 = new JMenuItem ( "AI Lastname" );

					item2.setActionCommand("1ai lastname;");
					item2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							String direct = item2.getActionCommand().substring(0, 1);
							String cmd = item2.getActionCommand().substring(1);

							try {
								if (direct.equals("0")) {
									int pos = searchEmpty();
									globalMqlInput.setSelectedIndex(pos);
									inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
									inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
										}
									});
								}
								else sendToServer(cmd);
							} catch (Exception f) {}

						}
					});
					menu.add ( item2 );
					JMenuItem item5 = new JMenuItem ( "Identity" );

					item5.setActionCommand("1metric system;");
					item5.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							String direct = item5.getActionCommand().substring(0, 1);
							String cmd = item5.getActionCommand().substring(1);

							try {
								if (direct.equals("0")) {
									int pos = searchEmpty();
									globalMqlInput.setSelectedIndex(pos);
									inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
									inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
										}
									});
								}
								else sendToServer(cmd);
							} catch (Exception f) {}

						}
					});
					menu.add ( item5 );
					JMenuItem item3 = new JMenuItem ( "Stop ID" );

					item3.setActionCommand("1id;");
					item3.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							String direct = item3.getActionCommand().substring(0, 1);
							String cmd = item3.getActionCommand().substring(1);

							if (direct.equals("0")) {
								int pos = searchEmpty();
								globalMqlInput.setSelectedIndex(pos);
								inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
								inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

								SwingUtilities.invokeLater(new Runnable() {
									public void run() {
										inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
									}
								});
							}
							else sendToServer(cmd);

						}
					});
					menu.add ( item3 );
					JMenuItem item4 = new JMenuItem ( "Version" );

					item4.setActionCommand("1version;");
					item4.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {

							String direct = item4.getActionCommand().substring(0, 1);
							String cmd = item4.getActionCommand().substring(1);

							try {
								if (direct.equals("0")) {
									int pos = searchEmpty();
									globalMqlInput.setSelectedIndex(pos);
									inputs.get(globalMqlInput.getSelectedIndex()).putClientProperty("SERVER_MODE", "");
									inputs.get(globalMqlInput.getSelectedIndex()).setText(cmd);

									SwingUtilities.invokeLater(new Runnable() {
										public void run() {
											inputs.get(globalMqlInput.getSelectedIndex()).requestFocus();
										}
									});
								}
								else sendToServer(cmd);
							} catch (Exception f) {}

						}
					});
					menu.add ( item4 );

					menu.show ( tree, pathBounds.x, pathBounds.y + pathBounds.height );

				}
			}

		} catch (Exception ee) {

			JOptionPane.showMessageDialog(globaljFrame, "err="+ee.getMessage());

		}

	}

	public static void reloadTreeAdmin(String json) throws Exception {

		globalMutableTreeNodeAdmin.removeAllChildren();

		globalMutableTreeNodeAdmin.setUserObject("Administration");

		JSONParser jsonParser = new JSONParser();
		JSONObject tree = null;
		try {

			tree = (JSONObject) jsonParser.parse(json);
		} catch (ParseException e) {try {
			Misc.create("tree_error.json", json);
		} catch (Exception e1) {
			Misc.create("tree_error.log", e1.getMessage()+"\n"+json);
		}};

		JSONObject core = (JSONObject) tree.get("core");

		JSONArray data = (JSONArray) core.get("data");

		Vector<String> titles = new Vector<String>();
		Vector<String> actions = new Vector<String>();
		Vector<String> directs = new Vector<String>();

		DefaultMutableTreeNode child;
		for(int i=0;i<data.size();i++) {

			JSONObject curObj = (JSONObject) data.get(i);

			titles = new Vector<String>();
			actions = new Vector<String>();
			directs = new Vector<String>();
			int iTitle = 1;
			while (curObj.get("title"+iTitle)!=null) {
				titles.add(""+curObj.get("title"+iTitle));
				actions.add(""+curObj.get("action"+iTitle));
				directs.add(""+curObj.get("direct"+iTitle));
				iTitle++;
			}

			child = new DefaultMutableTreeNode(new TreeRow(curObj.get("text")+"", "", curObj.get("icon")+"", (String) curObj.get("mql"), (String) curObj.get("direct"), titles, actions, directs));

			globalMutableTreeNodeAdmin.add(child);

			if (curObj.get("children")!=null) {
				JSONArray children = (JSONArray) curObj.get("children");
				for(int j=0;j<children.size();j++) {

					JSONObject curObjL2 = (JSONObject) children.get(j);

					titles = new Vector<String>();
					actions = new Vector<String>();
					directs = new Vector<String>();
					iTitle = 1;
					while (curObjL2.get("title"+iTitle)!=null) {
						titles.add(""+curObjL2.get("title"+iTitle));
						actions.add(""+curObjL2.get("action"+iTitle));
						directs.add(""+curObjL2.get("direct"+iTitle));
						iTitle++;
					}

					DefaultMutableTreeNode childL2 = new DefaultMutableTreeNode(new TreeRow(curObjL2.get("text")+"", "", curObjL2.get("icon")+"", (String) curObjL2.get("mql"), (String) curObjL2.get("direct"), titles, actions, directs));

					child.add(childL2);

					if (curObjL2.get("children")!=null) {

						HashMap<String, DefaultMutableTreeNode> alreadyCreated = new HashMap<String, DefaultMutableTreeNode>();

						JSONArray childrenL2 = (JSONArray) curObjL2.get("children");
						for(int k=0;k<childrenL2.size();k++) {

							JSONObject curObjL3 = (JSONObject) childrenL2.get(k);

							titles = new Vector<String>();
							actions = new Vector<String>();
							directs = new Vector<String>();
							iTitle = 1;
							while (curObjL3.get("title"+iTitle)!=null) {
								titles.add(""+curObjL3.get("title"+iTitle));
								actions.add(""+curObjL3.get("action"+iTitle));
								directs.add(""+curObjL3.get("direct"+iTitle));
								iTitle++;
							}

							String scriptName = curObjL3.get("text")+"";
							int nbFilePath = Misc.size(scriptName, ".");

							if (!(""+curObjL3.get("icon")).equals("images/mental.png")) {

								DefaultMutableTreeNode childL3 = new DefaultMutableTreeNode(new TreeRow(scriptName, "", curObjL3.get("icon")+"", (String) curObjL3.get("mql"), (String) curObjL3.get("direct"), titles, actions, directs));
								childL2.add(childL3);

								if (curObjL3.get("children")!=null) {

									JSONArray childrenL3 = (JSONArray) curObjL3.get("children");
									for(int p=0;p<childrenL3.size();p++) {

										JSONObject curObjL4 = (JSONObject) childrenL3.get(p);

										titles = new Vector<String>();
										actions = new Vector<String>();
										directs = new Vector<String>();
										iTitle = 1;
										while (curObjL4.get("title"+iTitle)!=null) {
											titles.add(""+curObjL4.get("title"+iTitle));
											actions.add(""+curObjL4.get("action"+iTitle));
											directs.add(""+curObjL4.get("direct"+iTitle));
											iTitle++;
										}

										DefaultMutableTreeNode childL4 = new DefaultMutableTreeNode(new TreeRow(curObjL4.get("text")+"", "", curObjL4.get("icon")+"", (String) curObjL4.get("mql"), (String) curObjL4.get("direct"), titles, actions, directs));

										childL3.add(childL4);

									}

								}

							} else {

								String checkPath = "";
								DefaultMutableTreeNode firstNode = null;
								DefaultMutableTreeNode lastNode = null;

								for(int iPath=1;iPath<nbFilePath;iPath++) {

									String currentPath = Misc.atom(scriptName, iPath, ".");

									checkPath += "."+currentPath;

									if (!alreadyCreated.containsKey(checkPath)) {

										DefaultMutableTreeNode c = new DefaultMutableTreeNode(new TreeRow(currentPath, "", "images/folder.png", "", "", new Vector<String>(), new Vector<String>(), new Vector<String>()));
										alreadyCreated.put(checkPath, c);

										if (lastNode!=null) lastNode.add(c);

										if (firstNode==null) firstNode = c;
										lastNode = c;

									} else {
										if (firstNode==null) firstNode = alreadyCreated.get(checkPath);
										lastNode = alreadyCreated.get(checkPath);
									}


								}

								DefaultMutableTreeNode childL3 = new DefaultMutableTreeNode(new TreeRow(Misc.atom(scriptName, nbFilePath, "."), "", curObjL3.get("icon")+"", (String) curObjL3.get("mql"), (String) curObjL3.get("direct"), titles, actions, directs));

								lastNode.add(childL3);
								childL2.add(firstNode);

							}

						}

					}

				}
			}

		}

		DefaultTreeModel model=(DefaultTreeModel)globalTreeAdmin.getModel();

		model.reload(globalMutableTreeNodeAdmin);

		setExpansionStateAdmin();

	}



	public static void reloadTreeDevel(String json) throws Exception {

		globalMutableTreeNodeDevel.removeAllChildren();

		if (is_restricted) globalMutableTreeNodeDevel.setUserObject("Execution");
		else globalMutableTreeNodeDevel.setUserObject("Development");

		JSONParser jsonParser = new JSONParser();
		JSONObject tree = null;
		try {

			tree = (JSONObject) jsonParser.parse(json);
		} catch (ParseException e) {try {
			Misc.create("tree_error.json", json);
		} catch (Exception e1) {
			Misc.create("tree_error.log", e1.getMessage()+"\n"+json);
		}};

		JSONObject core = (JSONObject) tree.get("core");

		JSONArray data = (JSONArray) core.get("data");

		Vector<String> titles = new Vector<String>();
		Vector<String> actions = new Vector<String>();
		Vector<String> directs = new Vector<String>();

		DefaultMutableTreeNode child;
		for(int i=0;i<data.size();i++) {

			JSONObject curObj = (JSONObject) data.get(i);

			titles = new Vector<String>();
			actions = new Vector<String>();
			directs = new Vector<String>();
			int iTitle = 1;
			while (curObj.get("title"+iTitle)!=null) {
				titles.add(""+curObj.get("title"+iTitle));
				actions.add(""+curObj.get("action"+iTitle));
				directs.add(""+curObj.get("direct"+iTitle));
				iTitle++;
			}

			child = new DefaultMutableTreeNode(new TreeRow(curObj.get("text")+"", "", curObj.get("icon")+"", (String) curObj.get("mql"), (String) curObj.get("direct"), titles, actions, directs));

			globalMutableTreeNodeDevel.add(child);

			if (curObj.get("children")!=null) {
				JSONArray children = (JSONArray) curObj.get("children");
				for(int j=0;j<children.size();j++) {

					JSONObject curObjL2 = (JSONObject) children.get(j);

					titles = new Vector<String>();
					actions = new Vector<String>();
					directs = new Vector<String>();
					iTitle = 1;
					while (curObjL2.get("title"+iTitle)!=null) {
						titles.add(""+curObjL2.get("title"+iTitle));
						actions.add(""+curObjL2.get("action"+iTitle));
						directs.add(""+curObjL2.get("direct"+iTitle));
						iTitle++;
					}

					DefaultMutableTreeNode childL2 = new DefaultMutableTreeNode(new TreeRow(curObjL2.get("text")+"", "", curObjL2.get("icon")+"", (String) curObjL2.get("mql"), (String) curObjL2.get("direct"), titles, actions, directs));

					child.add(childL2);

					if (curObjL2.get("children")!=null) {

						HashMap<String, DefaultMutableTreeNode> alreadyCreated = new HashMap<String, DefaultMutableTreeNode>();

						JSONArray childrenL2 = (JSONArray) curObjL2.get("children");
						for(int k=0;k<childrenL2.size();k++) {

							JSONObject curObjL3 = (JSONObject) childrenL2.get(k);

							titles = new Vector<String>();
							actions = new Vector<String>();
							directs = new Vector<String>();
							iTitle = 1;
							while (curObjL3.get("title"+iTitle)!=null) {
								titles.add(""+curObjL3.get("title"+iTitle));
								actions.add(""+curObjL3.get("action"+iTitle));
								directs.add(""+curObjL3.get("direct"+iTitle));
								iTitle++;
							}

							String scriptName = curObjL3.get("text")+"";
							int nbFilePath = Misc.size(scriptName, ".");

							if (!(""+curObjL3.get("icon")).equals("images/mental.png")) {

								DefaultMutableTreeNode childL3 = new DefaultMutableTreeNode(new TreeRow(scriptName, "", curObjL3.get("icon")+"", (String) curObjL3.get("mql"), (String) curObjL3.get("direct"), titles, actions, directs));
								childL2.add(childL3);

								if (curObjL3.get("children")!=null) {

									JSONArray childrenL3 = (JSONArray) curObjL3.get("children");
									for(int p=0;p<childrenL3.size();p++) {

										JSONObject curObjL4 = (JSONObject) childrenL3.get(p);

										titles = new Vector<String>();
										actions = new Vector<String>();
										directs = new Vector<String>();
										iTitle = 1;
										while (curObjL4.get("title"+iTitle)!=null) {
											titles.add(""+curObjL4.get("title"+iTitle));
											actions.add(""+curObjL4.get("action"+iTitle));
											directs.add(""+curObjL4.get("direct"+iTitle));
											iTitle++;
										}

										DefaultMutableTreeNode childL4 = new DefaultMutableTreeNode(new TreeRow(curObjL4.get("text")+"", "", curObjL4.get("icon")+"", (String) curObjL4.get("mql"), (String) curObjL4.get("direct"), titles, actions, directs));

										childL3.add(childL4);

									}

								}

							} else {

								String checkPath = "";
								DefaultMutableTreeNode firstNode = null;
								DefaultMutableTreeNode lastNode = null;

								for(int iPath=1;iPath<nbFilePath;iPath++) {

									String currentPath = Misc.atom(scriptName, iPath, ".");

									checkPath += "."+currentPath;

									if (!alreadyCreated.containsKey(checkPath)) {

										DefaultMutableTreeNode c = new DefaultMutableTreeNode(new TreeRow(currentPath, "", "images/folder.png", "", "", new Vector<String>(), new Vector<String>(), new Vector<String>()));
										alreadyCreated.put(checkPath, c);

										if (lastNode!=null) lastNode.add(c);

										if (firstNode==null) firstNode = c;
										lastNode = c;

									} else {
										if (firstNode==null) firstNode = alreadyCreated.get(checkPath);
										lastNode = alreadyCreated.get(checkPath);
									}


								}

								DefaultMutableTreeNode childL3 = new DefaultMutableTreeNode(new TreeRow(Misc.atom(scriptName, nbFilePath, "."), scriptName, curObjL3.get("icon")+"", (String) curObjL3.get("mql"), (String) curObjL3.get("direct"), titles, actions, directs));

								lastNode.add(childL3);
								childL2.add(firstNode);

							}

						}

					}

				}
			}

		}

		DefaultTreeModel model=(DefaultTreeModel)globalTreeDevel.getModel();
		model.reload(globalMutableTreeNodeDevel);

		setExpansionStateDevel();

	}


	public static void reloadTreeConfig(String json) throws Exception {

		globalMutableTreeNodeConfig.removeAllChildren();

		globalMutableTreeNodeConfig.setUserObject("Connection");

		JSONParser jsonParser = new JSONParser();
		JSONObject tree = null;
		try {
			tree = (JSONObject) jsonParser.parse(json);
		} catch (ParseException e) {try {
			Misc.create("tree_error.json", json);
		} catch (Exception e1) {
			Misc.create("tree_error.log", e1.getMessage()+"\n"+json);
		}};

		JSONObject core = (JSONObject) tree.get("core");

		JSONArray data = (JSONArray) core.get("data");

		Vector<String> titles = new Vector<String>();
		Vector<String> actions = new Vector<String>();
		Vector<String> directs = new Vector<String>();

		DefaultMutableTreeNode child;
		for(int i=0;i<data.size();i++) {

			JSONObject curObj = (JSONObject) data.get(i);

			titles = new Vector<String>();
			actions = new Vector<String>();
			directs = new Vector<String>();
			int iTitle = 1;
			while (curObj.get("title"+iTitle)!=null) {
				titles.add(""+curObj.get("title"+iTitle));
				actions.add(""+curObj.get("action"+iTitle));
				directs.add(""+curObj.get("direct"+iTitle));
				iTitle++;
			}

			child = new DefaultMutableTreeNode(new TreeRow(curObj.get("text")+"", "", curObj.get("icon")+"", (String) curObj.get("mql"), (String) curObj.get("direct"), titles, actions, directs));

			globalMutableTreeNodeConfig.add(child);

			if (curObj.get("children")!=null) {
				JSONArray children = (JSONArray) curObj.get("children");
				for(int j=0;j<children.size();j++) {

					JSONObject curObjL2 = (JSONObject) children.get(j);

					titles = new Vector<String>();
					actions = new Vector<String>();
					directs = new Vector<String>();
					iTitle = 1;
					while (curObjL2.get("title"+iTitle)!=null) {
						titles.add(""+curObjL2.get("title"+iTitle));
						actions.add(""+curObjL2.get("action"+iTitle));
						directs.add(""+curObjL2.get("direct"+iTitle));
						iTitle++;
					}

					DefaultMutableTreeNode childL2 = new DefaultMutableTreeNode(new TreeRow(curObjL2.get("text")+"", "", curObjL2.get("icon")+"", (String) curObjL2.get("mql"), (String) curObjL2.get("direct"), titles, actions, directs));

					child.add(childL2);

					if (curObjL2.get("children")!=null) {

						HashMap<String, DefaultMutableTreeNode> alreadyCreated = new HashMap<String, DefaultMutableTreeNode>();

						JSONArray childrenL2 = (JSONArray) curObjL2.get("children");
						for(int k=0;k<childrenL2.size();k++) {

							JSONObject curObjL3 = (JSONObject) childrenL2.get(k);

							titles = new Vector<String>();
							actions = new Vector<String>();
							directs = new Vector<String>();
							iTitle = 1;
							while (curObjL3.get("title"+iTitle)!=null) {
								titles.add(""+curObjL3.get("title"+iTitle));
								actions.add(""+curObjL3.get("action"+iTitle));
								directs.add(""+curObjL3.get("direct"+iTitle));
								iTitle++;
							}

							String scriptName = curObjL3.get("text")+"";
							int nbFilePath = Misc.size(scriptName, ".");

							if (!(""+curObjL3.get("icon")).equals("images/mental.png")) {

								DefaultMutableTreeNode childL3 = new DefaultMutableTreeNode(new TreeRow(scriptName, "", curObjL3.get("icon")+"", (String) curObjL3.get("mql"), (String) curObjL3.get("direct"), titles, actions, directs));
								childL2.add(childL3);

								if (curObjL3.get("children")!=null) {

									JSONArray childrenL3 = (JSONArray) curObjL3.get("children");
									for(int p=0;p<childrenL3.size();p++) {

										JSONObject curObjL4 = (JSONObject) childrenL3.get(p);

										titles = new Vector<String>();
										actions = new Vector<String>();
										directs = new Vector<String>();
										iTitle = 1;
										while (curObjL4.get("title"+iTitle)!=null) {
											titles.add(""+curObjL4.get("title"+iTitle));
											actions.add(""+curObjL4.get("action"+iTitle));
											directs.add(""+curObjL4.get("direct"+iTitle));
											iTitle++;
										}

										DefaultMutableTreeNode childL4 = new DefaultMutableTreeNode(new TreeRow(curObjL4.get("text")+"", "", curObjL4.get("icon")+"", (String) curObjL4.get("mql"), (String) curObjL4.get("direct"), titles, actions, directs));

										childL3.add(childL4);

									}

								}

							} else {

								String checkPath = "";
								DefaultMutableTreeNode firstNode = null;
								DefaultMutableTreeNode lastNode = null;

								for(int iPath=1;iPath<nbFilePath;iPath++) {

									String currentPath = Misc.atom(scriptName, iPath, ".");

									checkPath += "."+currentPath;

									if (!alreadyCreated.containsKey(checkPath)) {

										DefaultMutableTreeNode c = new DefaultMutableTreeNode(new TreeRow(currentPath, "", "images/folder.png", "", "", new Vector<String>(), new Vector<String>(), new Vector<String>()));
										alreadyCreated.put(checkPath, c);

										if (lastNode!=null) lastNode.add(c);

										if (firstNode==null) firstNode = c;
										lastNode = c;

									} else {
										if (firstNode==null) firstNode = alreadyCreated.get(checkPath);
										lastNode = alreadyCreated.get(checkPath);
									}


								}

								DefaultMutableTreeNode childL3 = new DefaultMutableTreeNode(new TreeRow(Misc.atom(scriptName, nbFilePath, "."), "", curObjL3.get("icon")+"", (String) curObjL3.get("mql"), (String) curObjL3.get("direct"), titles, actions, directs));

								lastNode.add(childL3);
								childL2.add(firstNode);

							}

						}

					}

				}

			}

		}

		DefaultTreeModel model=(DefaultTreeModel)globalTreeConfig.getModel();
		model.reload(globalMutableTreeNodeConfig);

		setExpansionStateConfig();

	}

	//Tree row
	private static class TreeRow {

		private String name;
		private String s;
		private String img;
		private String mql;
		private String direct;
		private Vector<String> titles;
		private Vector<String> actions;
		private Vector<String> directs;

		public TreeRow(String name, String s, String img, String mql, String direct, Vector<String> titles, Vector<String> actions, Vector<String> directs) {
			this.name = name;
			this.s = s;
			this.img = img;
			this.mql = mql;
			this.direct = direct;
			this.titles = titles;
			this.actions = actions;
			this.directs = directs;
		}

		@Override
		public String toString() {
			return name;
		}

	}

	// this is what you want
	private static class MyTreeCellRenderer extends DefaultTreeCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		ImageIcon imgFlag2 = new ImageIcon("images"+File.separator+"concentration.png");
		ImageIcon imgMentdb = new ImageIcon("images"+File.separator+"db16x16.png");
		ImageIcon imgActivity = new ImageIcon("images"+File.separator+"activity.png");
		ImageIcon imgStat = new ImageIcon("images"+File.separator+"stat.png");
		ImageIcon imgHelp = new ImageIcon("images"+File.separator+"help.png");
		ImageIcon imgMental = new ImageIcon("images"+File.separator+"mental.png");
		ImageIcon imgMentalJob = new ImageIcon("images"+File.separator+"mental_job.png");
		ImageIcon imgFolder = new ImageIcon("images"+File.separator+"folder.png");
		ImageIcon imgCm = new ImageIcon("images"+File.separator+"connection.png");
		ImageIcon imgCmTable = new ImageIcon("images"+File.separator+"connection_table.png");
		ImageIcon imgCmDB = new ImageIcon("images"+File.separator+"connection_db.png");
		ImageIcon imgCmDBW = new ImageIcon("images"+File.separator+"connection_db_w.png");
		ImageIcon imgCmFTP = new ImageIcon("images"+File.separator+"connection_ftp.gif");
		ImageIcon imgCmFTPS = new ImageIcon("images"+File.separator+"connection_ftps.gif");
		ImageIcon imgCmSFTP = new ImageIcon("images"+File.separator+"connection_sftp.gif");
		ImageIcon imgCmSSH = new ImageIcon("images"+File.separator+"connection_ssh.png");
		ImageIcon imgCmIMAP = new ImageIcon("images"+File.separator+"connection_imap.png");
		ImageIcon imgCmPOP3 = new ImageIcon("images"+File.separator+"connection_pop3.png");
		ImageIcon imgCmSMTP = new ImageIcon("images"+File.separator+"connection_smtp.png");
		ImageIcon imgCmCIFS = new ImageIcon("images"+File.separator+"connection_cifs.png");
		ImageIcon imgCmFILE = new ImageIcon("images"+File.separator+"connection_file.png");
		ImageIcon imgCmMentDB = new ImageIcon("images"+File.separator+"db16x16.png");
		ImageIcon imgVhost = new ImageIcon("images"+File.separator+"vhost.png");
		ImageIcon imgAction = new ImageIcon("images"+File.separator+"action.png");
		ImageIcon imgGroup = new ImageIcon("images"+File.separator+"group.png");
		ImageIcon imgUser = new ImageIcon("images"+File.separator+"user1.png");
		ImageIcon imgStack = new ImageIcon("images"+File.separator+"stack.png");
		ImageIcon imgPlog = new ImageIcon("images"+File.separator+"plog.png");
		ImageIcon imgApp = new ImageIcon("images"+File.separator+"app.png");
		ImageIcon imgJob = new ImageIcon("images"+File.separator+"timer.png");
		ImageIcon imgJobRunning = new ImageIcon("images"+File.separator+"timerrunning.png");
		ImageIcon imgJobRun = new ImageIcon("images"+File.separator+"timerrun.png");
		ImageIcon imgSeq = new ImageIcon("images"+File.separator+"seq.png");
		ImageIcon imgBrain = new ImageIcon("images"+File.separator+"brain.png");
		ImageIcon imgStatus = new ImageIcon("images"+File.separator+"status.png");
		ImageIcon imgStart = new ImageIcon("images"+File.separator+"start.png");
		ImageIcon imgStop = new ImageIcon("images"+File.separator+"stop.png");
		ImageIcon imgBrowser = new ImageIcon("images"+File.separator+"grid.png");
		ImageIcon imgConcentration = new ImageIcon("images"+File.separator+"concentration.png");
		ImageIcon imgLanguage = new ImageIcon("images"+File.separator+"flag.png");
		ImageIcon imgFs = new ImageIcon("images"+File.separator+"fs.png");
		ImageIcon imgParam = new ImageIcon("images"+File.separator+"conf.png");
		ImageIcon imgServer = new ImageIcon("images"+File.separator+"server.png");
		ImageIcon imgRestrictedSession = new ImageIcon("images"+File.separator+"restricted_session.png");

		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			
			setForeground(new Color(255, 255 ,255));
			setBackground(new Color(61, 61 ,61));
			setOpaque(true);

			if(((DefaultMutableTreeNode)value).isRoot()) setIcon(imgFlag2);

			// decide what icons you want by examining the node
			if (value instanceof DefaultMutableTreeNode) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

				if (node.getUserObject() instanceof TreeRow) {
					TreeRow r = (TreeRow) node.getUserObject();

					switch (r.img) {
					case "images/flag2.png": setIcon(imgFlag2);
					break;
					case "images/folder.png": setIcon(imgFolder);
					break;
					case "images/activity.png": setIcon(imgActivity);
					break;
					case "images/restricted_session.png": setIcon(imgRestrictedSession);
					break;
					case "images/stat.png": setIcon(imgStat);
					break;
					case "images/connection.png": setIcon(imgCm);
					break;
					case "images/connection_table.png": setIcon(imgCmTable);
					break;
					case "images/connection_db.png": setIcon(imgCmDB);
					break;
					case "images/connection_db_w.png": setIcon(imgCmDBW);
					break;
					case "images/connection_ftp.gif": setIcon(imgCmFTP);
					break;
					case "images/connection_ftps.gif": setIcon(imgCmFTPS);
					break;
					case "images/connection_sftp.gif": setIcon(imgCmSFTP);
					break;
					case "images/connection_mentdb.png": setIcon(imgCmMentDB);
					break;
					case "images/connection_ssh.png": setIcon(imgCmSSH);
					break;
					case "images/connection_imap.png": setIcon(imgCmIMAP);
					break;
					case "images/connection_pop3.png": setIcon(imgCmPOP3);
					break;
					case "images/connection_smtp.png": setIcon(imgCmSMTP);
					break;
					case "images/connection_cifs.png": setIcon(imgCmCIFS);
					break;
					case "images/connection_file.png": setIcon(imgCmFILE);
					break;
					case "images/vhost.png": setIcon(imgVhost);
					break;
					case "images/help.png": setIcon(imgHelp);
					break;
					case "images/mentdb.png": setIcon(imgMentdb);
					break;
					case "images/mental.png": setIcon(imgMental);
					break;
					case "images/mental_job.png": setIcon(imgMentalJob);
					break;
					case "images/action.png": setIcon(imgAction);
					break;
					case "images/group.png": setIcon(imgGroup);
					break;
					case "images/user1.png": setIcon(imgUser);
					break;
					case "images/timer.png": setIcon(imgJob);
					break;
					case "images/timerrun.png": setIcon(imgJobRun);
					break;
					case "images/timerrunning.png": setIcon(imgJobRunning);
					break;
					case "images/stack.png": setIcon(imgStack);
					break;
					case "images/plog.png": setIcon(imgPlog);
					break;
					case "images/app.png": setIcon(imgApp);
					break;
					case "images/seq.png": setIcon(imgSeq);
					break;
					case "images/brain.png": setIcon(imgBrain);
					break;
					case "images/status.png": setIcon(imgStatus);
					break;
					case "images/start.png": setIcon(imgStart);
					break;
					case "images/stop.png": setIcon(imgStop);
					break;
					case "images/grid.png": setIcon(imgBrowser);
					break;
					case "images/concentration.png": setIcon(imgConcentration);
					break;
					case "images/flag.png": setIcon(imgLanguage);
					break;
					case "images/fs.png": setIcon(imgFs);
					break;
					case "images/conf.png": setIcon(imgParam);
					break;
					case "images/server.png": setIcon(imgServer);
					break;
					default: 
						break;
					}

				}
			}

			return this;
		}

	}

}
