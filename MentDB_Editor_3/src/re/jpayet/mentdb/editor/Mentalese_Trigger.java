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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.jtattoo.plaf.fast.FastLookAndFeel;

import re.jpayet.mentdb.tools.Misc;

public class Mentalese_Trigger {

	@SuppressWarnings("rawtypes")
	public static class MyListCellThing extends JLabel implements ListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public MyListCellThing() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			// Assumes the stuff in the list has a pretty toString
			setText(value.toString());

			// based on the index you set the color.  This produces the every other effect.
			if (index % 2 == 0) setBackground(new Color(230, 230, 230));
			else setBackground(Color.WHITE);

			return this;
		}
		
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		//Create the data directory if not exist
		if (!(new File("data")).exists()) {
			(new File("data")).mkdir();
		}

		//Generate the connection object store if not exist
		if (!(new File("data/connections.store")).exists()) {
			Misc.create("data/connections.store", "[{\"name\":\"default\", \"hostname\":\"localhost\", \"port\":\"9998\", \"user\":\"admin\", \"password\":\"pwd\", \"key\":\"pwd\", \"connectTimeout\":\"10000\", \"readTimeout\":\"10000\"}]");
		}

		JSONParser jsonParser = new JSONParser();

		//Load the json object
		JSONArray store = (JSONArray) jsonParser.parse(Misc.load("data/connections.store"));

		// select the Look and Feel
		//UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");

		Properties props = new Properties();

		props.setProperty("logoString", "MentDB");

		// set your theme
		FastLookAndFeel.setTheme(props);

		//UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.graphite.GraphiteLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.smart.SmartLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.fast.FastLookAndFeel");
		//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		//UIManager.setLookAndFeel("com.jtattoo.plaf.luna.LunaLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		//UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");

		FlatLightLaf.install();
		UIManager.setLookAndFeel(new FlatDarculaLaf());
		
		// Create and set up a frame window
		JFrame.setDefaultLookAndFeelDecorated(true);
		JFrame frame = new JFrame("Mentalese Trigger");
		frame.setSize(600, 600);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width/2-frame.getSize().width/2, dim.height/2-frame.getSize().height/2);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {

				System.exit(0);

			}
		});

		// Set the window to be visible as the default to be false
		//frame.add(panel);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		JSONObject key_val = new JSONObject();
		for(int i=0;i<store.size();i++) {
			JSONObject oo = (JSONObject) store.get(i);
			String name = (String) oo.get("name");
			String hostname = (String) oo.get("hostname");
			String port = (String) oo.get("port");
			String user = (String) oo.get("user");
			key_val.put(name+"/"+user+"@"+hostname+":"+port+"/"+i, i);
		}
		List<Map.Entry<String, Integer>> ordered = new LinkedList<>( key_val.entrySet() );
		Collections.sort(ordered, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
			{
				return ( o1.getKey() ).compareTo( o2.getKey() );
			}
		} );
		
		DefaultListModel<String> model = new DefaultListModel<>();
		for(int zz = 0;zz<ordered.size();zz++) {
			
			int i = ordered.get(zz).getValue();
			
			JSONObject oo = (JSONObject) store.get(i);
			String name = (String) oo.get("name");
			String hostname = (String) oo.get("hostname");
			String port = (String) oo.get("port");
			String user = (String) oo.get("user");
			model.addElement(name+"/"+user+"@"+hostname+":"+port+"/"+i);
			
		}

		JList<String> list = new JList<>(model);
		list.setFixedCellHeight(34);
		list.setBorder(new EmptyBorder(10, 10, 10, 10));
		frame.add(list);
		
		list.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		       
				JList<String> list = (JList<String>)evt.getSource();
		        if (evt.getClickCount() == 2) {

		            //Start the editor
					String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

					/* Build the command */
					final ArrayList<String> command = new ArrayList<String>();
					command.add(javaBin);
					command.add("-cp");
					command.add(System.getProperty("java.class.path"));
					List<String> jvmArgs = ManagementFactory.getRuntimeMXBean().getInputArguments();
					
					// JVM arguments
					for (String jvmArg : jvmArgs) {
						command.add(jvmArg);
					}
					
					int pos = Integer.parseInt(Misc.atom(list.getSelectedValue(), Misc.size(list.getSelectedValue(), "/"), "/"));
					
					command.add("re.jpayet.mentdb.editor.Mentalese_Editor");
					command.add(((JSONObject) store.get(pos)).get("hostname")+"");
					command.add(((JSONObject) store.get(pos)).get("port")+"");
					command.add(((JSONObject) store.get(pos)).get("user")+"");
					command.add(((JSONObject) store.get(pos)).get("password")+"");
					command.add(((JSONObject) store.get(pos)).get("key")+"");
					command.add(((JSONObject) store.get(pos)).get("connectTimeout")+"");
					command.add(((JSONObject) store.get(pos)).get("readTimeout")+"");
					
					final ProcessBuilder builder = new ProcessBuilder(command);
					try {
						builder.start();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					list.clearSelection();
		            
		        }
		    }
		});

		JPanel panel = new JPanel(new BorderLayout());
		JLabel label = new JLabel(" Double click on a connection to continue ...");
		label.setPreferredSize(new Dimension(20, 20));
				
		list.setCellRenderer(new DefaultListCellRenderer() {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        label.setIcon(new ImageIcon("images"+File.separator+"ia.png"));
		        return label;
		    }
		});
		
		panel.add(label, BorderLayout.NORTH);
		JScrollPane sp = new JScrollPane(list);
		sp.setBorder(null);
		panel.add(sp, BorderLayout.CENTER);

		JButton add = new JButton("Add");
		add.setPreferredSize(new Dimension(80, 50));
		JButton update = new JButton("Update");
		JButton delete = new JButton("Del");
		delete.setPreferredSize(new Dimension(80, 50));

		JPanel aud = new JPanel(new BorderLayout());
		aud.add(add, BorderLayout.WEST);
		aud.add(update, BorderLayout.CENTER);
		aud.add(delete, BorderLayout.EAST);
		aud.setPreferredSize(new Dimension(200, 50));

		panel.add(aud, BorderLayout.SOUTH);

		add.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 

				Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
				Border empty = new EmptyBorder(5, 5, 5, 5);
				CompoundBorder border = new CompoundBorder(line, empty);

				final JDialog form = new JDialog(frame, "Add a new connection", true);
				JPanel p = new JPanel(new GridLayout(8, 2));
				p.add(new JLabel("Name ="));
				JTextField tName = new JTextField("");
				tName.setBorder(border);
				p.add(tName);
				tName.requestFocus();
				p.add(new JLabel("Hostname ="));
				JTextField tHostname = new JTextField("localhost");
				tHostname.setBorder(border);
				p.add(tHostname);
				p.setBorder(new EmptyBorder(10, 10, 10, 10));
				p.add(new JLabel("Port ="));
				JTextField tPort = new JTextField("9998");
				tPort.setBorder(border);
				p.add(tPort);
				p.add(new JLabel("Key ="));
				JPasswordField tKey = new JPasswordField("");
				tKey.setBorder(border);
				p.add(tKey);
				p.add(new JLabel("Login ="));
				JTextField tLogin = new JTextField("admin");
				tLogin.setBorder(border);
				p.add(tLogin);
				p.add(new JLabel("Password ="));
				JPasswordField tPassword = new JPasswordField("");
				tPassword.setBorder(border);
				p.add(tPassword);
				p.add(new JLabel("Connect Timeout ="));
				JTextField tConnect = new JTextField("10000");
				tConnect.setBorder(border);
				p.add(tConnect);
				p.add(new JLabel("Read Timeout ="));
				JTextField tRead = new JTextField("10000");
				tRead.setBorder(border);
				p.add(tRead);

				JPanel gp = new JPanel(new BorderLayout());
				gp.add(p, BorderLayout.CENTER);
				JButton addConnection = new JButton("Add");
				addConnection.setPreferredSize(new Dimension(400, 50));
				gp.add(addConnection, BorderLayout.SOUTH);
				
				addConnection.addActionListener(new ActionListener() { 
					
					public void actionPerformed(ActionEvent e) { 
						
						if (tName.getText().equals("")
								 || tHostname.getText().equals("")
								 || tPort.getText().equals("")
								 || String.valueOf(tKey.getPassword()).equals("")
								 || tLogin.getText().equals("")
								 || String.valueOf(tPassword.getPassword()).equals("")
								 || tConnect.getText().equals("")
								 || tRead.getText().equals("")) {
							
							JOptionPane.showMessageDialog(form, "Sorry, all fields are required!", "MentDB", JOptionPane.ERROR_MESSAGE);
							return;
							
						}
						
						boolean validPort = false;
						
						try {
							if (Integer.parseInt(tPort.getText())<0) {
								throw new Exception("<0");
							}
							validPort = true;
						} catch (Exception f) {}
						
						if (!validPort) {
							
							JOptionPane.showMessageDialog(form, "Sorry, the port must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
							return;
							
						}

						boolean validConnect = false;
						
						try {
							if (Integer.parseInt(tConnect.getText())<0) {
								throw new Exception("<0");
							}
							validConnect = true;
						} catch (Exception f) {}
						
						if (!validConnect) {
							
							JOptionPane.showMessageDialog(form, "Sorry, the connect timeout must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
							return;
							
						}

						boolean validRead = false;
						
						try {
							if (Integer.parseInt(tRead.getText())<0) {
								throw new Exception("<0");
							}
							validRead = true;
						} catch (Exception f) {}
						
						if (!validRead) {
							
							JOptionPane.showMessageDialog(form, "Sorry, the read timeout must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
							return;
							
						}
						
						JSONObject o = new JSONObject();
						o.put("name", tName.getText());
						o.put("hostname", tHostname.getText());
						o.put("key", String.valueOf(tKey.getPassword()));
						o.put("port", tPort.getText());
						o.put("user", tLogin.getText());
						o.put("password", String.valueOf(tPassword.getPassword()));
						o.put("connectTimeout", tConnect.getText());
						o.put("readTimeout", tRead.getText());
						
						store.add(o);
						
						try {
							Misc.create("data/connections.store", store.toJSONString());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						JSONObject key_val = new JSONObject();
						for(int i=0;i<store.size();i++) {
							JSONObject oo = (JSONObject) store.get(i);
							String name = (String) oo.get("name");
							String hostname = (String) oo.get("hostname");
							String port = (String) oo.get("port");
							String user = (String) oo.get("user");
							key_val.put(name+"/"+user+"@"+hostname+":"+port+"/"+i, i);
						}
						List<Map.Entry<String, Integer>> ordered = new LinkedList<>( key_val.entrySet() );
						Collections.sort(ordered, new Comparator<Map.Entry<String, Integer>>() {
							@Override
							public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
							{
								return ( o1.getKey() ).compareTo( o2.getKey() );
							}
						} );
						
						DefaultListModel<String> model = new DefaultListModel<>();
						for(int zz = 0;zz<ordered.size();zz++) {
							
							int i = ordered.get(zz).getValue();
							
							JSONObject oo = (JSONObject) store.get(i);
							String name = (String) oo.get("name");
							String hostname = (String) oo.get("hostname");
							String port = (String) oo.get("port");
							String user = (String) oo.get("user");
							model.addElement(name+"/"+user+"@"+hostname+":"+port+"/"+i);
							
						}
						list.setModel(model);
						
						form.dispose();
						
					}
				});

				form.setSize(400, 400);
				form.setPreferredSize(new Dimension(400, 400));
				form.setLocation(dim.width/2-form.getSize().width/2, dim.height/2-form.getSize().height/2);
				form.getContentPane().add(gp);
				form.pack();
				form.setVisible(true);

			}
		} );

		update.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {

				if (list.getSelectedIndex()>-1) {
				
					Border line = BorderFactory.createLineBorder(Color.DARK_GRAY);
					Border empty = new EmptyBorder(5, 5, 5, 5);
					CompoundBorder border = new CompoundBorder(line, empty);
					
					int pos = Integer.parseInt(Misc.atom(list.getSelectedValue(), Misc.size(list.getSelectedValue(), "/"), "/"));
					
					final JDialog form = new JDialog(frame, "Update/Copy a connection", true);
					JPanel p = new JPanel(new GridLayout(8, 2));
					p.add(new JLabel("Name ="));
					JTextField tName = new JTextField(((JSONObject) store.get(pos)).get("name")+"");
					tName.setBorder(border);
					p.add(tName);
					tName.requestFocus();
					p.add(new JLabel("Hostname ="));
					JTextField tHostname = new JTextField(((JSONObject) store.get(pos)).get("hostname")+"");
					tHostname.setBorder(border);
					p.add(tHostname);
					p.add(new JLabel("Key ="));
					JPasswordField tKey = new JPasswordField(((JSONObject) store.get(pos)).get("key")+"");
					tKey.setBorder(border);
					p.add(tKey);
					p.setBorder(new EmptyBorder(10, 10, 10, 10));
					p.add(new JLabel("Port ="));
					JTextField tPort = new JTextField(((JSONObject) store.get(pos)).get("port")+"");
					tPort.setBorder(border);
					p.add(tPort);
					p.add(new JLabel("Login ="));
					JTextField tLogin = new JTextField(((JSONObject) store.get(pos)).get("user")+"");
					tLogin.setBorder(border);
					p.add(tLogin);
					p.add(new JLabel("Password ="));
					JPasswordField tPassword = new JPasswordField(((JSONObject) store.get(pos)).get("password")+"");
					tPassword.setBorder(border);
					p.add(tPassword);
					p.add(new JLabel("Connect timeout ="));
					JTextField tConnect = new JTextField(((JSONObject) store.get(pos)).get("connectTimeout")+"");
					tConnect.setBorder(border);
					p.add(tConnect);
					p.add(new JLabel("Read timeout ="));
					JTextField tRead = new JTextField(((JSONObject) store.get(pos)).get("readTimeout")+"");
					tRead.setBorder(border);
					p.add(tRead);

					JPanel gp = new JPanel(new BorderLayout());
					gp.add(p, BorderLayout.CENTER);

					JButton updateConnection = new JButton("Update");
					updateConnection.setPreferredSize(new Dimension(350, 50));
					JButton copyConnection = new JButton("Copy");
					copyConnection.setPreferredSize(new Dimension(100, 50));
					JPanel uc = new JPanel(new BorderLayout());
					uc.add(updateConnection, BorderLayout.CENTER);
					uc.add(copyConnection, BorderLayout.EAST);
					
					gp.add(uc, BorderLayout.SOUTH);
					
					copyConnection.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) { 
							
							if (tName.getText().equals("")
									 || tHostname.getText().equals("")
									 || tPort.getText().equals("")
									 || String.valueOf(tKey.getPassword()).equals("")
									 || tLogin.getText().equals("")
									 || String.valueOf(tPassword.getPassword()).equals("")
									 || tConnect.getText().equals("")
									 || tRead.getText().equals("")) {
								
								JOptionPane.showMessageDialog(form, "Sorry, all fields are required!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}
							
							boolean validPort = false;
							
							try {
								if (Integer.parseInt(tPort.getText())<0) {
									throw new Exception("<0");
								}
								validPort = true;
							} catch (Exception f) {}
							
							if (!validPort) {
								
								JOptionPane.showMessageDialog(form, "Sorry, the port must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}

							boolean validConnect = false;
							
							try {
								if (Integer.parseInt(tConnect.getText())<0) {
									throw new Exception("<0");
								}
								validConnect = true;
							} catch (Exception f) {}
							
							if (!validConnect) {
								
								JOptionPane.showMessageDialog(form, "Sorry, the connect timeout must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}

							boolean validRead = false;
							
							try {
								if (Integer.parseInt(tRead.getText())<0) {
									throw new Exception("<0");
								}
								validRead = true;
							} catch (Exception f) {}
							
							if (!validRead) {
								
								JOptionPane.showMessageDialog(form, "Sorry, the read timeout must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}
							
							JSONObject o = new JSONObject();
							o.put("name", tName.getText());
							o.put("hostname", tHostname.getText());
							o.put("key", String.valueOf(tKey.getPassword()));
							o.put("port", tPort.getText());
							o.put("user", tLogin.getText());
							o.put("password", String.valueOf(tPassword.getPassword()));
							o.put("connectTimeout", tConnect.getText());
							o.put("readTimeout", tRead.getText());
							
							store.add(o);
							
							try {
								Misc.create("data/connections.store", store.toJSONString());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							JSONObject key_val = new JSONObject();
							for(int i=0;i<store.size();i++) {
								JSONObject oo = (JSONObject) store.get(i);
								String name = (String) oo.get("name");
								String hostname = (String) oo.get("hostname");
								String port = (String) oo.get("port");
								String user = (String) oo.get("user");
								key_val.put(name+"/"+user+"@"+hostname+":"+port+"/"+i, i);
							}
							List<Map.Entry<String, Integer>> ordered = new LinkedList<>( key_val.entrySet() );
							Collections.sort(ordered, new Comparator<Map.Entry<String, Integer>>() {
								@Override
								public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
								{
									return ( o1.getKey() ).compareTo( o2.getKey() );
								}
							} );
							
							DefaultListModel<String> model = new DefaultListModel<>();
							for(int zz = 0;zz<ordered.size();zz++) {
								
								int i = ordered.get(zz).getValue();
								
								JSONObject oo = (JSONObject) store.get(i);
								String name = (String) oo.get("name");
								String hostname = (String) oo.get("hostname");
								String port = (String) oo.get("port");
								String user = (String) oo.get("user");
								model.addElement(name+"/"+user+"@"+hostname+":"+port+"/"+i);
								
							}
							list.setModel(model);
							
							form.dispose();
							
						}
					});
					
					updateConnection.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent e) { 
							
							if (tName.getText().equals("")
									 || tHostname.getText().equals("")
									 || String.valueOf(tKey.getPassword()).equals("")
									 || tPort.getText().equals("")
									 || tLogin.getText().equals("")
									 || String.valueOf(tPassword.getPassword()).equals("")
									 || tConnect.getText().equals("")
									 || tRead.getText().equals("")) {
								
								JOptionPane.showMessageDialog(form, "Sorry, all fields are required!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}
							
							boolean validPort = false;
							
							try {
								if (Integer.parseInt(tPort.getText())<0) {
									throw new Exception("<0");
								}
								validPort = true;
							} catch (Exception f) {}
							
							if (!validPort) {
								
								JOptionPane.showMessageDialog(form, "Sorry, the port must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}
							
							boolean validConnect = false;
							
							try {
								if (Integer.parseInt(tConnect.getText())<0) {
									throw new Exception("<0");
								}
								validConnect = true;
							} catch (Exception f) {}
							
							if (!validConnect) {
								
								JOptionPane.showMessageDialog(form, "Sorry, the connect timeout must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}
							
							boolean validRead = false;
							
							try {
								if (Integer.parseInt(tRead.getText())<0) {
									throw new Exception("<0");
								}
								validRead = true;
							} catch (Exception f) {}
							
							if (!validRead) {
								
								JOptionPane.showMessageDialog(form, "Sorry, the read timeout must be a valid number!", "MentDB", JOptionPane.ERROR_MESSAGE);
								return;
								
							}
							
							JSONObject o = new JSONObject();
							o.put("name", tName.getText());
							o.put("hostname", tHostname.getText());
							o.put("key", String.valueOf(tKey.getPassword()));
							o.put("port", tPort.getText());
							o.put("user", tLogin.getText());
							o.put("password", String.valueOf(tPassword.getPassword()));
							o.put("connectTimeout", tConnect.getText());
							o.put("readTimeout", tRead.getText());
							
							int pos = Integer.parseInt(Misc.atom(list.getSelectedValue(), Misc.size(list.getSelectedValue(), "/"), "/"));
							store.set(pos, o);
							
							try {
								Misc.create("data/connections.store", store.toJSONString());
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							
							JSONObject key_val = new JSONObject();
							for(int i=0;i<store.size();i++) {
								JSONObject oo = (JSONObject) store.get(i);
								String name = (String) oo.get("name");
								String hostname = (String) oo.get("hostname");
								String port = (String) oo.get("port");
								String user = (String) oo.get("user");
								key_val.put(name+"/"+user+"@"+hostname+":"+port+"/"+i, i);
							}
							List<Map.Entry<String, Integer>> ordered = new LinkedList<>( key_val.entrySet() );
							Collections.sort(ordered, new Comparator<Map.Entry<String, Integer>>() {
								@Override
								public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
								{
									return ( o1.getKey() ).compareTo( o2.getKey() );
								}
							} );
							
							DefaultListModel<String> model = new DefaultListModel<>();
							for(int zz = 0;zz<ordered.size();zz++) {
								
								int i = ordered.get(zz).getValue();
								
								JSONObject oo = (JSONObject) store.get(i);
								String name = (String) oo.get("name");
								String hostname = (String) oo.get("hostname");
								String port = (String) oo.get("port");
								String user = (String) oo.get("user");
								model.addElement(name+"/"+user+"@"+hostname+":"+port+"/"+i);
								
							}
							list.setModel(model);
							
							form.dispose();
							
						}
					});

					form.setSize(400, 400);
					form.setPreferredSize(new Dimension(400, 400));
					form.setLocation(dim.width/2-form.getSize().width/2, dim.height/2-form.getSize().height/2);
					form.getContentPane().add(gp);
					form.pack();
					form.setVisible(true);
					
				} else {
					
					JOptionPane.showMessageDialog(frame, "Sorry, select a connection to update it.", "MentDB", JOptionPane.ERROR_MESSAGE);
					
				}

			} 
		} );

		delete.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				
				if (list.getSelectedIndex()>-1) {
					
					if (JOptionPane.showConfirmDialog(frame, "Delete the connection '"+list.getSelectedValue()+"', are you sure?")==0) {
					
						int pos = Integer.parseInt(Misc.atom(list.getSelectedValue(), Misc.size(list.getSelectedValue(), "/"), "/"));
						store.remove(pos);
						
						try {
							Misc.create("data/connections.store", store.toJSONString());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
						JSONObject key_val = new JSONObject();
						for(int i=0;i<store.size();i++) {
							JSONObject oo = (JSONObject) store.get(i);
							String name = (String) oo.get("name");
							String hostname = (String) oo.get("hostname");
							String port = (String) oo.get("port");
							String user = (String) oo.get("user");
							key_val.put(name+"/"+user+"@"+hostname+":"+port+"/"+i, i);
						}
						List<Map.Entry<String, Integer>> ordered = new LinkedList<>( key_val.entrySet() );
						Collections.sort(ordered, new Comparator<Map.Entry<String, Integer>>() {
							@Override
							public int compare( Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2 )
							{
								return ( o1.getKey() ).compareTo( o2.getKey() );
							}
						} );
						
						DefaultListModel<String> model = new DefaultListModel<>();
						for(int zz = 0;zz<ordered.size();zz++) {
							
							int i = ordered.get(zz).getValue();
							
							JSONObject oo = (JSONObject) store.get(i);
							String name = (String) oo.get("name");
							String hostname = (String) oo.get("hostname");
							String port = (String) oo.get("port");
							String user = (String) oo.get("user");
							model.addElement(name+"/"+user+"@"+hostname+":"+port+"/"+i);
							
						}
						list.setModel(model);
					
					}
					
				} else {
					
					JOptionPane.showMessageDialog(frame, "Sorry, select a connection to delete it.", "MentDB", JOptionPane.ERROR_MESSAGE);
					
				}
				
			} 
		} );



		frame.add(panel);
		frame.setVisible(true);	

	}

}
