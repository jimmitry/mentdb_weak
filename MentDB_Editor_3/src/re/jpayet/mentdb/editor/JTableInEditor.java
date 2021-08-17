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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import re.jpayet.mentdb.tools.Misc;

public class JTableInEditor {

	public JMenuItem logShowTime = null;
	public JMenuItem fieldAnalyse = null;
	public String columnXName = "";
	public String columnX1Name = "";
	public String columnX2Name = "";
	public String columnX3Name = "";
	public String columnX4Name = "";
	public String columnX5Name = "";
	public String columnYName = "";
	public String mql = "";
	
	public static void copy(JTable jt) {

		int[] rows = jt.getSelectedRows();
		int[] cols = jt.getSelectedColumns();
		
		StringBuilder data = new StringBuilder();
				
		for(int i=0;i<rows.length;i++) {
			
			StringBuilder line = new StringBuilder();
			
			for(int j=0;j<cols.length;j++) {
				
				String val = "\t";
				Object o = jt.getValueAt(rows[i], cols[j]);
				if (o!=null) {
					val+=""+o;
				}
				
				line.append(val);
				
			}
			
			if (i>0) data.append("\n"+line.toString().substring(1));
			else data.append(line.toString().substring(1));
			
		}
		
		StringSelection stringSelection = new StringSelection(data.toString());
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
		
	}
	
	public static void addCtrlAppleCopy(JTable jt) {

		jt.addKeyListener(new KeyListener() {

			public String OS = Misc.os();

			@Override
			public void keyPressed(KeyEvent e) {

				if (OS.equals("mac")) {

					if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiersEx() & KeyEvent.META_DOWN_MASK) != 0)) {

						copy(jt);

					}

				} else {

					if ((e.getKeyCode() == KeyEvent.VK_C) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {

						copy(jt);

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

	public static int rightClickJtable_col = -1;
	public static int rightClickJtable_row = -1;

	public void createJtable(String value) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				try {
					String json = value.substring(39);
					
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObj = null;
					try {
						jsonObj = (JSONObject) jsonParser.parse(json);
					} catch (ParseException e) {};
					
					JSONArray column_types = (JSONArray) jsonObj.get("column_types");
					JSONArray ent = (JSONArray) jsonObj.get("columns");
					JSONArray data = (JSONArray) jsonObj.get("data");
					mql = (String) jsonObj.get("mql");

					String i_cmId = (String) jsonObj.get("i_cmId");
					String i_json = (String) jsonObj.get("i_json");
					String i_title = (String) jsonObj.get("i_title");
					String i_query = (String) jsonObj.get("i_query");
					
					//Parse headers
					String[] entetes = new String[ent.size()];
					@SuppressWarnings("rawtypes")
					Class[] type = new Class[ent.size()];
					for(int i=0;i<ent.size();i++) {

						entetes[i] = (String) ent.get(i);

						if (((String) column_types.get(i)).equals("LONG")) {
							type[i] = Long.class;
						} else if (((String) column_types.get(i)).equals("DOUBLE")) {
							type[i] = Double.class;
						} else {
							type[i] = String.class;
						}

					}
					
					Object[][] d = new Object[data.size()][];
					
					//Parse data
					for(int i=0;i<data.size();i++) {
						
						JSONObject obj = (JSONObject) data.get(i);
						
						Object[] row = new Object[ent.size()];
						
						for(int j=0;j<ent.size();j++) {
							
							Object o = obj.get(entetes[j]);
							if (o==null) {
								row[j] = null;
							} else {
								if (type[j].equals(Long.class)) row[j] = Long.parseLong((String) o);
								else if (type[j].equals(Double.class)) row[j] = Double.parseDouble((String) o);
								else row[j] = (String) o;
							}
							
						}
						
						d[i] = row;
						
					}
					
					JPanel panel = new JPanel(new BorderLayout());

					JTable jt = new JTable() {

						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
						
						@Override
				        public Component prepareRenderer(TableCellRenderer renderer,int row,int column)
				        {
				            Component comp=super.prepareRenderer(renderer,row, column);
				           int modelRow=convertRowIndexToModel(row);
				           if(!isRowSelected(modelRow))
				               comp.setBackground(new Color(61, 61, 61));
				           else
				               comp.setBackground(new Color(21, 21, 21));
				           return comp;
				        }

						//Implement table cell tool tips.           
						public String getToolTipText(MouseEvent e) {
							String tip = null;
							java.awt.Point p = e.getPoint();
							int rowIndex = rowAtPoint(p);
							int colIndex = columnAtPoint(p);

							try {
								String val = getValueAt(rowIndex, colIndex).toString();
								if (val.startsWith("\n")) val = val.substring(1);
								tip = "<html>"+val.replace("<", "&lt;").replace("\n", "<br>")+"</html>";
							} catch (RuntimeException e1) {
								//catch null pointer exception if mouse is over an empty line
							}

							return tip;
						}

					};
					jt.setCellSelectionEnabled(true);
					addCtrlAppleCopy(jt);
					jt.setBorder(null);
					
					final JPopupMenu popupMenu = new JPopupMenu();
					
					jt.addMouseListener(new MouseAdapter() {
					    @Override
					    public void mousePressed(MouseEvent e) {
					    		
					    		if(e.getButton() == MouseEvent.BUTTON1) {
					    			JTable source = (JTable)e.getSource();
					            	int row = source.rowAtPoint( e.getPoint() );
					            int column = source.columnAtPoint( e.getPoint() );
					 
					            if (!source.isRowSelected(row))
					                source.changeSelection(row, column, false, false);
					        }
					        
					        rightClickJtable_col  = jt.columnAtPoint(e.getPoint());
					        rightClickJtable_row  = jt.rowAtPoint(e.getPoint());

					        /*JOptionPane.showMessageDialog(null, 
					        		e.isPopupTrigger()+"===rightClickJtable_row="+rightClickJtable_row+"\n rightClickJtable_col="+rightClickJtable_col+"",
					                " titre ",
					                JOptionPane.WARNING_MESSAGE);*/
					        
					        int rowindex = jt.getSelectedRow();
					        if (rowindex < 0)
					            return;
					        
					        if(e.getButton() == MouseEvent.BUTTON3) {
					        		if (e.getComponent() instanceof JTable ) {
						            popupMenu.show(e.getComponent(), e.getX(), e.getY());
						        }
					        }
					        
					    }
					});

					final String cmId = (String) (jsonObj.get("cmId"));
					final String query = (String) (jsonObj.get("query"));
					String title = (jsonObj.get("title")+"");
					if (title.startsWith("stack <") || title.startsWith("log <") || title.startsWith("PID <")) {
						
						JMenuItem clipboard = new JMenuItem("Copy (clipboard)");
						clipboard.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								copy(jt);

							}
						});
						popupMenu.add(clipboard);
						logShowTime = new JMenuItem("PID <pid>");
						logShowTime.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								
								String val = ""+jt.getValueAt(rightClickJtable_row, rightClickJtable_col);

								Mentalese_Editor.sendToServer("restricted show_pid "+val);

								logShowTime.setText("PID <"+val+">");

							}
						});
						popupMenu.add(logShowTime);
						//jt.setComponentPopupMenu(popupMenu);
						popupMenu.addPopupMenuListener(new PopupMenuListener() {

							@Override
							public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
								SwingUtilities.invokeLater(new Runnable() {
									@Override
									public void run() {

										logShowTime.setText("PID <"+jt.getValueAt(rightClickJtable_row, rightClickJtable_col)+">");

									}
								});
							}

							@Override
							public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

							}

							@Override
							public void popupMenuCanceled(PopupMenuEvent e) {

							}
						});

					} else if (title.startsWith("table <")) {

						
						JMenuItem clipboard = new JMenuItem("Copy (clipboard)");
						clipboard.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								copy(jt);

							}
						});
						popupMenu.add(clipboard);
						
						if (!Mentalese_Editor.is_restricted) {
						
							JMenuItem setXItem = new JMenuItem("PA <X="+columnXName+">");
							setXItem.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									
									if (!columnXName.equals("") && columnXName.equals(jt.getColumnName(rightClickJtable_col))) {
										columnXName = "";
									} else {
										columnXName = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setXItem);
							JMenuItem setYItem = new JMenuItem("PA <Y="+columnYName+">");
							setYItem.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									if (!columnYName.equals("") && columnYName.equals(jt.getColumnName(rightClickJtable_col))) {
										columnYName = "";
									} else {
										columnYName = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setYItem);
							JMenuItem scatterItem = new JMenuItem("PA <Show scatter (X,Y)>");
							scatterItem.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									Mentalese_Editor.sendToServer("pa xy_scatter \""+cmId.replace("\"", "\\\"")+"\" \""+columnXName.replace("\"", "\\\"")+"\" \""+columnYName.replace("\"", "\\\"")+"\" \""+query.replace("\"", "\\\"")+"\"");
									
								}
							});
							popupMenu.add(scatterItem);
							JMenuItem RL = new JMenuItem("PA <Linear regression (X,Y)>");
							RL.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									int pos = Mentalese_Editor.searchEmpty();
									Mentalese_Editor.globalMqlInput.setSelectedIndex(pos);
									Mentalese_Editor.inputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("pa rl load \"reg1\" \""+cmId.replace("\"", "\\\"")+"\" \""+columnXName.replace("\"", "\\\"")+"\" \""+columnYName.replace("\"", "\\\"")+"\" \""+query.replace("\"", "\\\"")+"\";\n"+
									"pa rl predict \"reg1\" 12;");
									
								}
							});
							popupMenu.add(RL);
							JMenuItem clusterItem = new JMenuItem("ML <Load cluster (X,Y)>");
							clusterItem.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									int pos = Mentalese_Editor.searchEmpty();
									Mentalese_Editor.globalMqlInput.setSelectedIndex(pos);
									Mentalese_Editor.inputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("ml cluster load \"cluster1\" \""+cmId.replace("\"", "\\\"")+"\" \""+columnXName.replace("\"", "\\\"")+"\" \""+columnYName.replace("\"", "\\\"")+"\" 5 3 \""+query.replace("\"", "\\\"")+"\";");
									
								}
							});
							popupMenu.add(clusterItem);
							JMenuItem setX1Item = new JMenuItem("PA <X1="+columnX1Name+">");
							setX1Item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									if (!columnX1Name.equals("") && columnX1Name.equals(jt.getColumnName(rightClickJtable_col))) {
										columnX1Name = "";
									} else {
										columnX1Name = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setX1Item);
							JMenuItem setX2Item = new JMenuItem("PA <X2="+columnX2Name+">");
							setX2Item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									if (!columnX2Name.equals("") && columnX2Name.equals(jt.getColumnName(rightClickJtable_col))) {
										columnX2Name = "";
									} else {
										columnX2Name = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setX2Item);
							JMenuItem setX3Item = new JMenuItem("PA <X3="+columnX3Name+">");
							setX3Item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									if (!columnX3Name.equals("") && columnX3Name.equals(jt.getColumnName(rightClickJtable_col))) {
										columnX3Name = "";
									} else {
										columnX3Name = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setX3Item);
							JMenuItem setX4Item = new JMenuItem("PA <X4="+columnX4Name+">");
							setX4Item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									if (!columnX4Name.equals("") && columnX4Name.equals(jt.getColumnName(rightClickJtable_col))) {
										columnX4Name = "";
									} else {
										columnX4Name = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setX4Item);
							JMenuItem setX5Item = new JMenuItem("PA <X5="+columnX5Name+">");
							setX5Item.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									if (!columnX5Name.equals("") && columnX5Name.equals(jt.getColumnName(rightClickJtable_col))) {
										columnX5Name = "";
									} else {
										columnX5Name = jt.getColumnName(rightClickJtable_col);
									}
	
								}
							});
							popupMenu.add(setX5Item);
							JMenuItem RM = new JMenuItem("PA <Multiple regression (X1,X2,X3,X4,X5,Y)>");
							RM.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
	
									int pos = Mentalese_Editor.searchEmpty();
									Mentalese_Editor.globalMqlInput.setSelectedIndex(pos);
									Mentalese_Editor.inputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText("pa rm load \"reg1\" \""+cmId.replace("\"", "\\\"")+"\" \""+columnX1Name.replace("\"", "\\\"")+"\" \""+columnX2Name.replace("\"", "\\\"")+"\" \""+columnX3Name.replace("\"", "\\\"")+"\" \""+columnX4Name.replace("\"", "\\\"")+"\" \""+columnX5Name.replace("\"", "\\\"")+"\" \""+columnYName.replace("\"", "\\\"")+"\" \""+query.replace("\"", "\\\"")+"\";\n"+
									"pa rm predict \"reg1\" \"[12, 23]\";");
									
								}
							});
							popupMenu.add(RM);

							
							//jt.setComponentPopupMenu(popupMenu);(popupMenu);
							popupMenu.addPopupMenuListener(new PopupMenuListener() {

								@Override
								public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {

											fieldAnalyse.setText("DQ <"+jt.getColumnName(rightClickJtable_col)+">");

											setXItem.setText("PA <X="+columnXName+">");
											setYItem.setText("PA <Y="+columnYName+">");
											setX1Item.setText("PA <X1="+columnX1Name+">");
											setX2Item.setText("PA <X2="+columnX2Name+">");
											setX3Item.setText("PA <X3="+columnX3Name+">");
											setX4Item.setText("PA <X4="+columnX4Name+">");
											setX5Item.setText("PA <X5="+columnX5Name+">");
											
										}
									});
								}

								@Override
								public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

								}

								@Override
								public void popupMenuCanceled(PopupMenuEvent e) {

								}
							});

							fieldAnalyse = new JMenuItem("DQ > ");
							fieldAnalyse.addActionListener(new ActionListener() {
								@SuppressWarnings({ "unchecked", "rawtypes" })
								@Override
								public void actionPerformed(ActionEvent e) {

									final JDialog frame = new JDialog(Mentalese_Editor.globaljFrame, "Select algorithms | Data Quality", true);
									frame.setSize(new Dimension(700, 600));
									frame.setBounds(0,0,700, 600);
									
									ArrayList<String> lines = new ArrayList<String>();
									
									JSONParser jsonParser = new JSONParser();
									JSONObject algorithm = null;
									try {
										algorithm = (JSONObject) jsonParser.parse(Mentalese_Editor.getDataFromServer("dq algorithm show;"));
									} catch (Exception f) {};
									
									if (algorithm!=null) for(Object o : algorithm.keySet()) {
										
										String key = (String) o;
										String mql = (String) algorithm.get(key);
										
										lines.add(key+" : "+mql);
										
									}
									
									JList list = new JList(lines.toArray());
									ListModel model = list.getModel();
								    String[] strings = new String[model.getSize()];
								    for(int i=0;i<strings.length;i++){
								        strings[i]=model.getElementAt(i).toString();
								    }
								    Arrays.sort(strings);
								    list.setListData(strings);    
									
									list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
									list.setPreferredSize(new Dimension(700, 500));
									list.setBorder(BorderFactory.createCompoundBorder(
											list.getBorder(), 
											BorderFactory.createEmptyBorder(10, 10, 10, 10)));
									
									JButton b = new JButton("OK");
									b.setPreferredSize(new Dimension(0, 35));
									b.addActionListener(new ActionListener() {
										@Override
										public void actionPerformed(ActionEvent e) {

											List values = list.getSelectedValuesList();
											JSONArray a = new JSONArray();
											for(int z=0;z<values.size();z++) {
												
												String s = (String) values.get(z);
												s = s.substring(0, s.indexOf(":"));
												s = s.substring(0, s.length()-1);
												a.add(s);
												
											}
											
											String r = "";
											
											try {
												r = Mentalese_Editor.getDataFromServer("dq generate \""+cmId.replace("\"", "\\\"")+"\" \""+title.substring(7, title.length()-1)+"\" \""+jt.getColumnName(rightClickJtable_col).replace("\"", "\\\"")+"\" \""+a.toJSONString().replace("\"", "\\\"")+"\" \""+query.replace("\"", "\\\"")+"\";");
											} catch (Exception f) {
												
												r = "Error: "+f.getMessage();
												
											}
											
											int pos = Mentalese_Editor.searchEmpty();
											Mentalese_Editor.globalMqlInput.setSelectedIndex(pos);
											Mentalese_Editor.inputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText(r);
											
											frame.dispose();

										}
									});
									
									JPanel p = new JPanel(new BorderLayout());
									p.add(list, BorderLayout.CENTER);
									p.add(b, BorderLayout.SOUTH);
									frame.getContentPane().add(p);
									
									final Toolkit toolkit = Toolkit.getDefaultToolkit();
									final Dimension screenSize = toolkit.getScreenSize();
									final int x = (screenSize.width - frame.getWidth()) / 2;
									final int y = (screenSize.height - frame.getHeight()) / 2;
									frame.setLocation(x, y);
									
									frame.pack();
									frame.setVisible(true);
									
								}
							});
							popupMenu.add(fieldAnalyse);
							
						}
						
						addCountAndFilterOptions(jt, popupMenu, title);
						
						JMenuItem export = new JMenuItem("Export");
						export.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								
								ExportFromEditor.export(jt, title);
								
							}
							
						});

						popupMenu.add(export);
						
					} else if (title.startsWith("dq <")) {
						
						JMenuItem clipboard = new JMenuItem("Copy (clipboard)");
						clipboard.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								copy(jt);

							}
						});
						popupMenu.add(clipboard);
						//jt.setComponentPopupMenu(popupMenu);
						
						if (!Mentalese_Editor.is_restricted) {
						
							JMenuItem showData = new JMenuItem("Show data");
							showData.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									
									String algoKey = ""+jt.getColumnName(rightClickJtable_col);
									String fieldKey = jt.getValueAt(rightClickJtable_row, 0).toString();
									
									Mentalese_Editor.sendToServer("dq analyse show "
											+ "\""+i_cmId.replace("\"", "\\\"")+"\" \""+i_json.replace("\"", "\\\"")+"\" "
											+ "\""+algoKey.replace("\"", "\\\"")+"\" \""+fieldKey.replace("\"", "\\\"")+"\" \""+i_title.replace("\"", "\\\"")+"\" "
											+ "\""+i_query.replace("\"", "\\\"")+"\";");
	
								}
							});
							popupMenu.add(showData);
							
						}
						
					} else if (title.startsWith("dq_table <")) {
						
						
						JMenuItem clipboard = new JMenuItem("Copy (clipboard)");
						clipboard.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {

								copy(jt);

							}
						});
						popupMenu.add(clipboard);

						addCountAndFilterOptions(jt, popupMenu, title);
						
						JMenuItem export = new JMenuItem("Export");
						export.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								
								ExportFromEditor.export(jt, title);
								
							}
							
						});

						popupMenu.add(export);
						//jt.setComponentPopupMenu(popupMenu);
						
					}
					
					jt.setAutoCreateRowSorter(true);
					jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					//jt.setDefaultRenderer(Object.class, new MyCellRenderer());
					
					@SuppressWarnings("serial")
					class CustomTableModel extends DefaultTableModel {
						public CustomTableModel(Object rowData[][], Object columnNames[]) {
							super(rowData, columnNames);
						}

						@SuppressWarnings("rawtypes")
						private Class[] classList = null;

						public void setClassList(@SuppressWarnings("rawtypes") Class[] cl) {
							classList = cl;
						}

						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}

						@SuppressWarnings({ "unchecked", "rawtypes" })
						@Override
						public Class getColumnClass(int column) {
							return classList[column];
						}
					};
					CustomTableModel dm = new CustomTableModel(d, entetes);
					jt.setModel(dm);
					
					if (title.startsWith("dq <")) {
						jt.getColumnModel().getColumn(0).setPreferredWidth(450);
						jt.getColumnModel().getColumn(1).setPreferredWidth(250);
					} else if (title.startsWith("log <")) {
						jt.getColumnModel().getColumn(0).setPreferredWidth(150);
						jt.getColumnModel().getColumn(1).setPreferredWidth(50);
						jt.getColumnModel().getColumn(2).setPreferredWidth(250);
						jt.getColumnModel().getColumn(3).setPreferredWidth(150);
						jt.getColumnModel().getColumn(4).setPreferredWidth(150);
						jt.getColumnModel().getColumn(5).setPreferredWidth(80);
						jt.getColumnModel().getColumn(6).setPreferredWidth(80);
						jt.getColumnModel().getColumn(7).setPreferredWidth(500);
						jt.getColumnModel().getColumn(8).setPreferredWidth(50);
					} else if (title.startsWith("stack <")) {
						jt.getColumnModel().getColumn(0).setPreferredWidth(150);
						jt.getColumnModel().getColumn(1).setPreferredWidth(200);
						jt.getColumnModel().getColumn(2).setPreferredWidth(150);
						jt.getColumnModel().getColumn(3).setPreferredWidth(150);
						jt.getColumnModel().getColumn(4).setPreferredWidth(150);
						jt.getColumnModel().getColumn(5).setPreferredWidth(150);
						jt.getColumnModel().getColumn(6).setPreferredWidth(50);
						jt.getColumnModel().getColumn(7).setPreferredWidth(50);
						jt.getColumnModel().getColumn(8).setPreferredWidth(150);
						jt.getColumnModel().getColumn(9).setPreferredWidth(150);
						jt.getColumnModel().getColumn(10).setPreferredWidth(500);
					} else if (title.startsWith("PID <")) {
						jt.getColumnModel().getColumn(0).setPreferredWidth(150);
						jt.getColumnModel().getColumn(1).setPreferredWidth(50);
						jt.getColumnModel().getColumn(2).setPreferredWidth(250);
						jt.getColumnModel().getColumn(3).setPreferredWidth(150);
						jt.getColumnModel().getColumn(4).setPreferredWidth(150);
						jt.getColumnModel().getColumn(5).setPreferredWidth(80);
						jt.getColumnModel().getColumn(6).setPreferredWidth(80);
						jt.getColumnModel().getColumn(7).setPreferredWidth(500);
						jt.getColumnModel().getColumn(8).setPreferredWidth(50);
					} else {
						for(int i=0;i<jt.getColumnModel().getColumnCount();i++) {
							jt.getColumnModel().getColumn(i).setPreferredWidth(150);
						}
					}
					
					dm.setClassList(type);

					jt.setPreferredScrollableViewportSize(jt.getPreferredSize());
	                jt.setFillsViewportHeight(true);
					jt.setBackground(new Color(61,61,61));
					jt.setForeground(new Color(255,255,255));
					
					jt.setSelectionBackground(new Color(81,81,81));
					
					JScrollPane jsp = new JScrollPane(jt, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					jsp.setBorder(BorderFactory.createEmptyBorder());
					jsp.getHorizontalScrollBar().setUnitIncrement(20);
					jsp.getViewport().setBackground(new Color(61,61,61));
					jsp.getVerticalScrollBar().setBackground(new Color(51,51,51));
					jsp.getHorizontalScrollBar().setBackground(new Color(51,51,51));
					jsp.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
					    @Override
					    protected void configureScrollBarColors() {
					        this.thumbColor = new Color(21,21,21);
					    }
					});
					jsp.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {
					    @Override
					    protected void configureScrollBarColors() {
					        this.thumbColor = new Color(21,21,21);
					    }
					});
					panel.add(jsp, BorderLayout.CENTER);

					int existPosTitle = -1;
					for(int i=0;i<Mentalese_Editor.globalOutputTabbbedPane.getTabCount();i++) {
						if (Mentalese_Editor.globalOutputTabbbedPane.getTitleAt(i).equals(jsonObj.get("title")+"  ")) {
							existPosTitle = i;
							break;
						}
					}
					
					if (existPosTitle>-1) {
						Mentalese_Editor.globalOutputTabbbedPane.setComponentAt(existPosTitle, panel);
					} else Mentalese_Editor.globalOutputTabbbedPane.add(jsonObj.get("title")+"  ", panel);

					JPanel pnlTab = new JPanel(new GridBagLayout());
					pnlTab.setOpaque(false);
					JLabel lblTitle = new JLabel("<html><body><table border=0 width=100%><tr><td>"+((String) jsonObj.get("title")).replace("<", "&lt;")+"</td></tr></table></body></html>");
					lblTitle.setForeground(Color.WHITE);
					
					if (title.startsWith("dq <")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"search.png"));
					} else if (title.startsWith("log <")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"plog.png"));
					} else if (title.startsWith("stack <")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"stack.png"));
					} else if (title.startsWith("PID <")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"proc.png"));
					} else if (title.startsWith("desc <")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"table_config.png"));
					} else if (title.equals("table <MentDB Process>")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"process.png"));
					} else if (title.equals("table <MySQL Process>") || title.equals("table <H2 Db Process>")) {
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"processm.png"));
					} else {
						//Table
						lblTitle.setIcon(new ImageIcon("images"+File.separator+"connection_table.png"));
					}
					
					JButton btnClose = new JButton();
					btnClose.setBackground(new Color(51,51,51));
					btnClose.setForeground(Color.WHITE);
					btnClose.setOpaque(true);
					btnClose.setBorderPainted(false);
					btnClose.setIcon(new ImageIcon("images"+File.separator+"closetab.png"));
					btnClose.setPreferredSize(new Dimension(20, 20));
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
					
					if (mql!=null) {
						
						JButton btnRelaod = new JButton();
						btnRelaod.setBackground(new Color(51,51,51));
						btnRelaod.setForeground(Color.WHITE);
						btnRelaod.setOpaque(true);
						btnRelaod.setBorderPainted(false);
						btnRelaod.setIcon(new ImageIcon("images"+File.separator+"reload.png"));
						btnRelaod.setPreferredSize(new Dimension(20, 20));
						
						btnRelaod.addActionListener(new ActionListener() { 
							public void actionPerformed(ActionEvent e) { 

								Mentalese_Editor.sendToServer(mql);

							} 
						} );

						gbc.gridx++;
						gbc.gridx++;
						gbc.weightx = 0;
						pnlTab.add(btnRelaod, gbc);

					}
					
					gbc.gridx++;
					gbc.weightx = 0;
					pnlTab.add(btnClose, gbc);

					if (existPosTitle>-1) {
						Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(existPosTitle, pnlTab);
						Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(existPosTitle);
					} else {
						Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1, pnlTab);
						Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1);
					}
					
					jt.requestFocus();
					
				} catch (Exception e) {
					Misc.log("errtest: "+e.getMessage()+(ExceptionUtils.getStackTrace(e)));
				}

			}
		});

	}
	
	private static void addCountAndFilterOptions(JTable jt, JPopupMenu popupMenu, String title) {
		
		JMenuItem dq_count = new JMenuItem("DQ [count]");
		dq_count.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JOptionPane.showMessageDialog(null,
						jt.getRowCount()+" element(s).",
						"DQ [count]",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});

		popupMenu.add(dq_count);
		
		JMenuItem dq_count_distinct_with = new JMenuItem("DQ [count distinct without null]");
		dq_count_distinct_with.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Get the selected column
				int selCol = rightClickJtable_col;
				int nbRow = jt.getRowCount();
				
				HashMap<String, String> dataDistinct = new HashMap<String, String>();
				
				for(int i=0;i<nbRow;i++) {
					
					Object d = jt.getValueAt(i, selCol);
					if (d!=null) dataDistinct.put(d+"", "");
					
				}
				
				JOptionPane.showMessageDialog(null,
						dataDistinct.size()+" element(s).",
						"DQ [count distinct with null]",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});

		popupMenu.add(dq_count_distinct_with);
		
		JMenuItem dq_count_distinct_without = new JMenuItem("DQ [count distinct with null]");
		dq_count_distinct_without.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Get the selected column
				int selCol = rightClickJtable_col;
				int nbRow = jt.getRowCount();
				
				HashMap<String, String> dataDistinct = new HashMap<String, String>();
				
				for(int i=0;i<nbRow;i++) {
					
					Object d = jt.getValueAt(i, selCol);
					if (d==null) dataDistinct.put(null, "");
					else dataDistinct.put(d+"", "");
					
				}
				
				JOptionPane.showMessageDialog(null,
						dataDistinct.size()+" element(s).",
						"DQ [count distinct without null]",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});

		popupMenu.add(dq_count_distinct_without);
		
		JMenuItem dq_count_empty = new JMenuItem("DQ [count empty]");
		dq_count_empty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Get the selected column
				int selCol = rightClickJtable_col;
				int nbRow = jt.getRowCount();
				
				int count = 0;
				
				for(int i=0;i<nbRow;i++) {
					
					Object d = jt.getValueAt(i, selCol);
					if (d!=null && (d+"").equals("")) count++;
					
				}
				
				JOptionPane.showMessageDialog(null,
						count+" element(s).",
						"DQ [count empty]",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});

		popupMenu.add(dq_count_empty);
		
		JMenuItem dq_count_null = new JMenuItem("DQ [count null]");
		dq_count_null.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Get the selected column
				int selCol = rightClickJtable_col;
				int nbRow = jt.getRowCount();int count = 0;
				
				for(int i=0;i<nbRow;i++) {
					
					Object d = jt.getValueAt(i, selCol);
					if (d==null) count++;
					
				}
				
				JOptionPane.showMessageDialog(null,
						count+" element(s).",
						"DQ [count null]",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
			
		});

		popupMenu.add(dq_count_null);
		
		JMenuItem dq_filter_null = new JMenuItem("DQ [filter on null]");
		dq_filter_null.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Get the selected column
				int selCol = rightClickJtable_col;
				int nbRow = jt.getRowCount();
				
				DefaultTableModel dtm = ((DefaultTableModel)jt.getModel());

				for(int i=0;i<nbRow;i++) {
					
					Object d = jt.getValueAt(i, selCol);
					if (d!=null) {
						
						dtm.removeRow(i);
						
						nbRow--;
						i--;
					}
					
				}
							
			}
			
		});

		popupMenu.add(dq_filter_null);
		
		JMenuItem dq_filter_value = new JMenuItem("DQ [filter on value]");
		dq_filter_value.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				//Get the selected column
				int selCol = rightClickJtable_col;
				int nbRow = jt.getRowCount();
				
				String val = JOptionPane.showInputDialog(null, "Enter a value:", "DQ [filter on value]", JOptionPane.INFORMATION_MESSAGE);
				
				if (val!=null) {
				
					DefaultTableModel dtm = ((DefaultTableModel)jt.getModel());
	
					for(int i=0;i<nbRow;i++) {
						
						Object d = jt.getValueAt(i, selCol);
						if (d==null || !(d+"").equals(val)) {
							
							dtm.removeRow(i);
							
							nbRow--;
							i--;
						}
						
					}
					
				}
							
			}
			
		});

		popupMenu.add(dq_filter_value);
		
	}

}
