package re.jpayet.mentdb.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.fx.StringFx;
import re.jpayet.mentdb.tools.Misc;

public class SCRUD {

	public static void open_form_1() throws Exception {
		
		JDialog scrud1 = new JDialog(Mentalese_Editor.globaljFrame, "Step 1 - SCRUD Generator", false);
		scrud1.setSize(new Dimension(1000, 670));
		scrud1.setPreferredSize(new Dimension(1000, 670));
		
		scrud1.setBounds(0,0,1000, 670);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		JSONArray cm = Misc.loadArray(Mentalese_Editor.execute_in_cluster("cm show_scrud;"));
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(int i=0;i<cm.size();i++) {
			model.addElement((String) cm.get(i));
		}
		JList<String> list = new JList<String>(model);
		list.setSelectionBackground(Color.DARK_GRAY);
		list.setFont(new Font("monospaced", Font.PLAIN, 13));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);
		list.setForeground(new Color(255, 255 ,255));
		list.setBackground(new Color(61, 61 ,61));
		list.setOpaque(true);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBorder(null);
		listScroller.getVerticalScrollBar().setBackground(new Color(51,51,51));
		listScroller.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		listScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		listScroller.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		list.setBorder(BorderFactory.createCompoundBorder(
				list.getBorder(), 
				BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>)evt.getSource();
				if (evt.getClickCount() == 2) {

					// Double-click detected
					int index = list.locationToIndex(evt.getPoint());

					if (index>-1) {
						scrud1.setVisible(false);
						scrud1.dispose();
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								try {
									open_form_2(list.getSelectedValue());
								} catch (Exception e) {
									Misc.log("error: "+e.getMessage());
								}
							}
						});
					}

				}
			}
		});
		panel.add(listScroller, BorderLayout.CENTER);
		panel.setBackground(new Color(61, 61 ,61));

		JLabel label = new JLabel("Double click on a database to select it...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		JLabel label_compatible = new JLabel("Compatible with: Oracle, PostgreSQL, SQLServer, MySQL, H2, HSQL and Apache Derby.");
		label_compatible.setFont(new Font(label_compatible.getFont().getName(), Font.PLAIN, 12));
		label_compatible.setHorizontalAlignment(JLabel.CENTER);
		label_compatible.setBorder(new EmptyBorder(5, 5, 5, 5));
		label_compatible.setForeground(Color.ORANGE);
		label_compatible.setBackground(new Color(51,51,51));
		label_compatible.setOpaque(true);
		panel.add(label_compatible, BorderLayout.SOUTH);

		panel.add(label, BorderLayout.NORTH);
		scrud1.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - scrud1.getWidth()) / 2;
		final int y = (screenSize.height - scrud1.getHeight()) / 2;
		scrud1.setLocation(x, y);

		scrud1.pack();
		scrud1.setVisible(true);
		
	}

	public static void open_form_2(String db) throws Exception {
		
		JDialog scrud2 = new JDialog(Mentalese_Editor.globaljFrame, db+" | Step 2 - SCRUD Generator", false);
		scrud2.setSize(new Dimension(1000, 670));
		scrud2.setPreferredSize(new Dimension(1000, 670));
		
		scrud2.setBounds(0,0,1000, 670);
		
		JPanel panel = new JPanel(new BorderLayout());
		
		JSONObject tb = Misc.loadObject(Mentalese_Editor.execute_in_cluster("try {\n"
				+ "\n"
				+ "	sql connect \"session1\" {cm get \""+db+"\";};\n"
				+ "	json load \"result\" (sql to json \"session1\" \"products\" (execute \"db.table.show.get\"\n"
				+ "		\"[type]\" (node select \"CM["+db+"]\" /subType)\n"
				+ "		\"[schema]\" (node select \"CM["+db+"]\" /defaultSchema)\n"
				+ "	));\n"
				+ "	sql disconnect \"session1\";\n"
				+ "	json doc \"result\";\n"
				+ "\n"
				+ "} {\n"
				+ "\n"
				+ "	try {sql disconnect \"session1\";} {} \"[err_sql]\";\n"
				+ "\n"
				+ "} \"[err]\";"));
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		JSONArray cm = (JSONArray) tb.get("data");
		for(int i=0;i<cm.size();i++) {
			JSONObject o = (JSONObject) cm.get(i);
			model.addElement((String) o.get("tablename"));
		}
		
		JList<String> list = new JList<String>(model);
		list.setSelectionBackground(Color.DARK_GRAY);
		list.setFont(new Font("monospaced", Font.PLAIN, 13));
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);
		list.setForeground(new Color(255, 255 ,255));
		list.setBackground(new Color(61, 61 ,61));
		list.setOpaque(true);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBorder(null);
		listScroller.getVerticalScrollBar().setBackground(new Color(51,51,51));
		listScroller.getHorizontalScrollBar().setBackground(new Color(51,51,51));
		listScroller.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		
		listScroller.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
		    @Override
		    protected void configureScrollBarColors() {
		        this.thumbColor = new Color(21,21,21);
		    }
		});
		list.setBorder(BorderFactory.createCompoundBorder(
				list.getBorder(), 
				BorderFactory.createEmptyBorder(8, 8, 8, 8)));
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>)evt.getSource();
				if (evt.getClickCount() == 2) {

					// Double-click detected
					int index = list.locationToIndex(evt.getPoint());

					if (index>-1) {
						scrud2.setVisible(false);
						scrud2.dispose();
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								try {
									open_form_3(db, list.getSelectedValue());
								} catch (Exception e) {
									Misc.log("error: "+e.getMessage());
								}
							}
						});
					}

				}
			}
		});
		
		panel.add(listScroller, BorderLayout.CENTER);
		panel.setBackground(new Color(61, 61 ,61));
		
		JLabel label = new JLabel("Double click on a table to select it ...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		JLabel label_compatible = new JLabel("Compatible with: Oracle, PostgreSQL, SQLServer, MySQL, H2, HSQL and Apache Derby.");
		label_compatible.setFont(new Font(label_compatible.getFont().getName(), Font.PLAIN, 12));
		label_compatible.setHorizontalAlignment(JLabel.CENTER);
		label_compatible.setBorder(new EmptyBorder(5, 5, 5, 5));
		label_compatible.setForeground(Color.ORANGE);
		label_compatible.setBackground(new Color(51,51,51));
		label_compatible.setOpaque(true);
		panel.add(label_compatible, BorderLayout.SOUTH);
		
		panel.add(label, BorderLayout.NORTH);
		scrud2.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - scrud2.getWidth()) / 2;
		final int y = (screenSize.height - scrud2.getHeight()) / 2;
		scrud2.setLocation(x, y);

		scrud2.pack();
		scrud2.setVisible(true);
		
	}

	public static void open_form_3(String db, String table) throws Exception {
		
		JDialog scrud3 = new JDialog(Mentalese_Editor.globaljFrame, db+"."+table+" | Step 3 - SCRUD Generator", false);
		scrud3.setSize(new Dimension(1000, 670));
		scrud3.setPreferredSize(new Dimension(1000, 670));
		
		scrud3.setBounds(0,0,1000, 670);
		
		JPanel panel = new JPanel(new BorderLayout());

		JSONArray contexts_http = Misc.loadArray(Mentalese_Editor.execute_in_cluster("app show \"http\";"));
		JSONArray contexts_https = Misc.loadArray(Mentalese_Editor.execute_in_cluster("app show \"https\";"));
		JSONArray data = (JSONArray) Misc.loadObject(Mentalese_Editor.execute_in_cluster("sql show desc \""+db+"\" \""+table+"\";;")).get("data");
		
		HashSet<String> contexts = new HashSet<String>();
		for(int i=0;i<contexts_http.size();i++) {
			contexts.add((String) contexts_http.get(i));
		}
		for(int i=0;i<contexts_https.size();i++) {
			contexts.add((String) contexts_https.get(i));
		}
		List<String> sortedList = new ArrayList<>(contexts);
		Collections.sort(sortedList);
		
		JPanel panelTop1 = new JPanel(new BorderLayout());
		JLabel l_app_context = new JLabel("App context  ");
		l_app_context.setForeground(new Color(255,255,255));
		JPanel p_app_context = new JPanel(new BorderLayout());
		p_app_context.setBackground(new Color(61, 61 ,61));
		
		String[] ctx = new String[sortedList.size()];
		for(int i=0;i<sortedList.size();i++) ctx[i] = sortedList.get(i);
		JComboBox<String> app_context = new JComboBox<>(ctx);
		
		app_context.setPreferredSize(new Dimension(300, 30));
		p_app_context.add(l_app_context, BorderLayout.WEST);
		p_app_context.add(app_context, BorderLayout.CENTER);
		
		JPanel p_prefix = new JPanel(new BorderLayout());
		JLabel l_prefix = new JLabel("MQL Prefix  ");
		l_prefix.setForeground(new Color(255,255,255));
		p_prefix.setBackground(new Color(61, 61 ,61));
		JTextField prefix = new JTextField("app.100.template.default.actions.");
		p_prefix.add(l_prefix, BorderLayout.WEST);
		p_prefix.add(prefix, BorderLayout.CENTER);

		JPanel p_prefix_context = new JPanel(new BorderLayout());
		p_prefix_context.add(p_app_context, BorderLayout.NORTH);
		p_prefix_context.add(p_prefix, BorderLayout.SOUTH);
		
		panelTop1.add(p_prefix_context, BorderLayout.NORTH);

		JPanel panelTop2 = new JPanel(new BorderLayout());
		
		DefaultTableModel model = new DefaultTableModel(); 
		JTable controls = new JTable(model) {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 0 ? false : true;
		    }
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass(); 
			}
		};
		model.addColumn("COLUMN NAME"); 
		model.addColumn("SELECT"); 
		model.addColumn("INSERT"); 
		model.addColumn("UPDATE"); 
		model.addColumn("SEARCH");
		controls.getColumn("COLUMN NAME").setPreferredWidth(150);
		controls.setRowHeight(30);

		TableColumn col2 = controls.getColumnModel().getColumn(2);
		TableColumn col3 = controls.getColumnModel().getColumn(3);
		TableColumn col4 = controls.getColumnModel().getColumn(4);
		JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.addItem("NOT_USED");
        comboBox.addItem("checkbox_inline");
        comboBox.addItem("checkbox_line");
        comboBox.addItem("hidden");
        comboBox.addItem("radio_inline");
        comboBox.addItem("radio_line");
        comboBox.addItem("select_mono");
        comboBox.addItem("select_multiple");
        comboBox.addItem("textarea");
        comboBox.addItem("textarea_cke");
        comboBox.addItem("textbox.color");
        comboBox.addItem("textbox.file");
        comboBox.addItem("textbox.mail");
        comboBox.addItem("textbox.number");
        comboBox.addItem("textbox.password");
        comboBox.addItem("textbox.range");
        comboBox.addItem("textbox.tel");
        comboBox.addItem("textbox.text");
        comboBox.addItem("textbox.time");
        comboBox.addItem("textbox.date_en");
        comboBox.addItem("textbox.date_fr");
        comboBox.addItem("textbox.datetime_en");
        comboBox.addItem("textbox.datetime_fr");
        col2.setCellEditor(new DefaultCellEditor(comboBox));
        col3.setCellEditor(new DefaultCellEditor(comboBox));
        col4.setCellEditor(new DefaultCellEditor(comboBox));
		
		for(int i=0;i<data.size();i++) {
			JSONObject row = (JSONObject) data.get(i);

			String insert = "textbox.text";
			String update = "textbox.text";
			String search = "textbox.text";
			
			if (((String) row.get("TYPE")).startsWith("longtext")) {
				insert = "textarea";
				update = "textarea";
			} else if ((((String) row.get("TYPE")).startsWith("datetime")) || (((String) row.get("TYPE")).startsWith("timestamp"))) {
				insert = "textbox.datetime_en";
				update = "textbox.datetime_en";
				search = "textbox.datetime_en";
			} else if (((String) row.get("TYPE")).startsWith("enum")) {
				insert = "radio_inline";
				update = "radio_inline";
				search = "radio_inline";
			} else if ((((String) row.get("TYPE")).startsWith("smallint")) || (((String) row.get("TYPE")).startsWith("mediumint")) || (((String) row.get("TYPE")).startsWith("int")) || (((String) row.get("TYPE")).startsWith("bigint")) || (((String) row.get("TYPE")).startsWith("double")) || (((String) row.get("TYPE")).startsWith("float")) || (((String) row.get("TYPE")).startsWith("decimal"))) {
				insert = "textbox.number";
				update = "textbox.number";
				search = "textbox.number";
			}

			if (((String) row.get("KEY")).equals("PRI")) {
				
				if (((String) row.get("EXTRA")).equals("auto_increment")) {
					insert = "hidden";
					update = "hidden";
				} else {
					update = "hidden";
				}
				
			} else {
				
				if (((String) row.get("EXTRA")).equals("auto_increment")) {
					insert = "hidden";
					update = "hidden";
				} else {
					
				}
				
			}
			
			model.addRow(new Object[]{(String) row.get("FIELD"), true, insert, update, search});
		}
		
		controls.setBounds(30,40,200,300);          
		JScrollPane spcontrols=new JScrollPane(controls);    
		panelTop2.add(panelTop1, BorderLayout.NORTH);
		panelTop2.add(spcontrols, BorderLayout.CENTER);

		panelTop1.setBackground(new Color(61, 61 ,61));
		panelTop2.setBackground(new Color(61, 61 ,61));
		panel.setBackground(new Color(61, 61 ,61));

		panelTop2.setBorder(new EmptyBorder(8, 8, 8, 8));
		panel.add(panelTop2, BorderLayout.CENTER);
		
		JButton ok = new JButton("GENERATE SCRUD OPERATIONS ...");
		ok.setFont(new Font(ok.getFont().getName(), Font.PLAIN, 18));
		ok.setBorder(new EmptyBorder(5, 5, 5, 5));
		ok.setForeground(Color.BLACK);
		ok.setBackground(new Color(200,100,0));
		panelTop2.add(ok, BorderLayout.SOUTH);
		ok.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						try {

							scrud3.setVisible(false);
							scrud3.dispose();
							
							JSONObject config = new JSONObject();
							config.put("SELECT", new JSONObject());
							config.put("INSERT", new JSONObject());
							config.put("UPDATE", new JSONObject());
							config.put("SEARCH", new JSONObject());
							
							for(int i=0;i<controls.getRowCount();i++) {
								
								if ((boolean) controls.getValueAt(i, 1)) {
									((JSONObject) config.get("SELECT")).put(controls.getValueAt(i, 0), "1");
								}
	
								((JSONObject) config.get("INSERT")).put(controls.getValueAt(i, 0), controls.getValueAt(i, 2));
								((JSONObject) config.get("UPDATE")).put(controls.getValueAt(i, 0), controls.getValueAt(i, 3));
								((JSONObject) config.get("SEARCH")).put(controls.getValueAt(i, 0), controls.getValueAt(i, 4));
								
							}
							
							String mql = "in editor {\n	execute \"app.100.scrud.generate.exe\" \n		\"[app_name]\" \""+app_context.getSelectedItem()+"\"\n		\"[prefix]\" \""+prefix.getText()+"\" \n		\"[database]\" \""+db.replace("\"", "\\\"")+"\" \n		\"[table]\" \""+table.replace("\"", "\\\"")+"\"\n		\"[confjson]\" \""+(StringFx.encode_b64(config.toJSONString()))+"\"	\n;\n};";
							
							if (!Mentalese_Editor.upDownScreen_bool) {
								
								Mentalese_Editor.sendToServer(mql);
								
							} else {
								
								Mentalese_Editor.sendToServer("in editor {\n" + 
										"	"+mql+"\n" + 
										"}");
								
							}
							
						} catch (Exception e) {
							Misc.log("error: "+e.getMessage());
						}

					}
				});
				
			}
		});
		
		JLabel label = new JLabel("Select context and choose control types ...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		JLabel label_compatible = new JLabel("Compatible with: Oracle, PostgreSQL, SQLServer, MySQL, H2, HSQL and Apache Derby.");
		label_compatible.setFont(new Font(label_compatible.getFont().getName(), Font.PLAIN, 12));
		label_compatible.setHorizontalAlignment(JLabel.CENTER);
		label_compatible.setBorder(new EmptyBorder(5, 5, 5, 5));
		label_compatible.setForeground(Color.ORANGE);
		label_compatible.setBackground(new Color(51,51,51));
		label_compatible.setOpaque(true);
		panel.add(label_compatible, BorderLayout.SOUTH);
		
		panel.add(label, BorderLayout.NORTH);
		scrud3.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - scrud3.getWidth()) / 2;
		final int y = (screenSize.height - scrud3.getHeight()) / 2;
		scrud3.setLocation(x, y);

		scrud3.pack();
		scrud3.setVisible(true);
		
	}
	
}
