package re.jpayet.mentdb.editor;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import re.jpayet.mentdb.tools.Misc;

public class ETL1 {

	public static void open_form_1() {
		
		JDialog etl = new JDialog(Mentalese_Editor.globaljFrame, "Step 1 - ETL Generator (2116 combinations)", false);
		etl.setSize(new Dimension(500, 300));
		etl.setPreferredSize(new Dimension(500, 300));
		etl.setBounds(0,0,500, 300);
		
		JPanel panel = new JPanel(new BorderLayout());

	    JPanel pnl = new JPanel(new GridLayout(4, 2));
	    pnl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		ButtonGroup buttonGroup = new ButtonGroup();
	    JRadioButton b_local = new JRadioButton("LOCAL"); buttonGroup.add(b_local); pnl.add(b_local);
	    JRadioButton b_ftp = new JRadioButton("FTP"); buttonGroup.add(b_ftp); pnl.add(b_ftp);
	    JRadioButton b_ftps = new JRadioButton("FTPS"); buttonGroup.add(b_ftps); pnl.add(b_ftps);
	    JRadioButton b_sftp = new JRadioButton("SFTP"); buttonGroup.add(b_sftp); pnl.add(b_sftp);
	    JRadioButton b_ssh = new JRadioButton("SCP"); buttonGroup.add(b_ssh); pnl.add(b_ssh);
	    JRadioButton b_cifs = new JRadioButton("CIFS"); buttonGroup.add(b_cifs); pnl.add(b_cifs);
	    JRadioButton b_mentdb = new JRadioButton("MENTDB"); buttonGroup.add(b_mentdb); pnl.add(b_mentdb);
	    
		panel.add(pnl, BorderLayout.CENTER);
		
		JButton ok = new JButton("NEXT STEP...");
		ok.setFont(new Font(ok.getFont().getName(), Font.PLAIN, 18));
		ok.setBorder(new EmptyBorder(5, 5, 5, 5));
		ok.setForeground(Color.BLACK);
		ok.setBackground(new Color(200,100,0));
		panel.add(ok, BorderLayout.SOUTH);

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						try {
							
							String value = "";
							for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
					            AbstractButton button = buttons.nextElement();

					            if (button.isSelected()) {
					            	value = button.getText();
					            	break;
					            }
					        }
							
							if (!value.equals("")) {

								etl.setVisible(false);
								etl.dispose();
								
								open_form_2(value);
								
							}
							
						} catch (Exception e) {
							Misc.log("error: "+e.getMessage());
						}

					}
				});
				
			}
		});
		
		JLabel label = new JLabel("Select a source connection...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		panel.add(label, BorderLayout.NORTH);
		
		etl.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - etl.getWidth()) / 2;
		final int y = (screenSize.height - etl.getHeight()) / 2;
		etl.setLocation(x, y);

		etl.pack();
		etl.setVisible(true);
		
	}

	public static void open_form_2(String step1_val) {
		
		JDialog etl = new JDialog(Mentalese_Editor.globaljFrame, "Step 2 - ETL Generator (2116 combinations)", false);
		etl.setSize(new Dimension(500, 300));
		etl.setPreferredSize(new Dimension(500, 300));
		etl.setBounds(0,0,500, 300);
		
		JPanel panel = new JPanel(new BorderLayout());

	    JPanel pnl = null;

		ButtonGroup buttonGroup = new ButtonGroup();
		
		if (step1_val.equals("LOCAL")) {
			
			pnl = new JPanel(new GridLayout(5, 2));
		    pnl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

		    JRadioButton b_csv = new JRadioButton("CSV"); buttonGroup.add(b_csv);pnl.add(b_csv);
		    JRadioButton b_json = new JRadioButton("JSON"); buttonGroup.add(b_json);pnl.add(b_json);
		    JRadioButton b_xml = new JRadioButton("XML"); buttonGroup.add(b_xml);pnl.add(b_xml);
		    JRadioButton b_file = new JRadioButton("TXT_FILE"); buttonGroup.add(b_file);pnl.add(b_file);
		    JRadioButton b_excel = new JRadioButton("EXCEL"); buttonGroup.add(b_excel);pnl.add(b_excel);
		    JRadioButton b_excelx = new JRadioButton("EXCELX"); buttonGroup.add(b_excelx);pnl.add(b_excelx);
		    JRadioButton b_rest = new JRadioButton("REST"); buttonGroup.add(b_rest);pnl.add(b_rest);
		    JRadioButton b_soap = new JRadioButton("SOAP"); buttonGroup.add(b_soap);pnl.add(b_soap);
		    JRadioButton b_sql = new JRadioButton("SQL"); buttonGroup.add(b_sql);pnl.add(b_sql);
		    JRadioButton b_mail = new JRadioButton("MAIL"); buttonGroup.add(b_mail);pnl.add(b_mail);
		    
		} else {

			pnl = new JPanel(new GridLayout(3, 2));
		    pnl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
			
		    JRadioButton b_csv = new JRadioButton("CSV"); buttonGroup.add(b_csv);pnl.add(b_csv);
		    JRadioButton b_json = new JRadioButton("JSON"); buttonGroup.add(b_json);pnl.add(b_json);
		    JRadioButton b_xml = new JRadioButton("XML"); buttonGroup.add(b_xml);pnl.add(b_xml);
		    JRadioButton b_file = new JRadioButton("TXT_FILE"); buttonGroup.add(b_file);pnl.add(b_file);
		    JRadioButton b_excel = new JRadioButton("EXCEL"); buttonGroup.add(b_excel);pnl.add(b_excel);
		    JRadioButton b_excelx = new JRadioButton("EXCELX"); buttonGroup.add(b_excelx);pnl.add(b_excelx);
		    
		}
		
		panel.add(pnl, BorderLayout.CENTER);
		
		JButton ok = new JButton("NEXT STEP...");
		ok.setFont(new Font(ok.getFont().getName(), Font.PLAIN, 18));
		ok.setBorder(new EmptyBorder(5, 5, 5, 5));
		ok.setForeground(Color.BLACK);
		ok.setBackground(new Color(200,100,0));
		panel.add(ok, BorderLayout.SOUTH);

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						try {
							
							String value = "";
							for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
					            AbstractButton button = buttons.nextElement();

					            if (button.isSelected()) {
					            	value = button.getText();
					            	break;
					            }
					        }
							
							if (!value.equals("")) {

								etl.setVisible(false);
								etl.dispose();
								
								open_form_3(step1_val, value);
								
							}
							
						} catch (Exception e) {
							Misc.log("error: "+e.getMessage());
						}

					}
				});
				
			}
		});
	    
		JLabel label = new JLabel("Select a source format...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		panel.add(label, BorderLayout.NORTH);
		
		etl.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - etl.getWidth()) / 2;
		final int y = (screenSize.height - etl.getHeight()) / 2;
		etl.setLocation(x, y);

		etl.pack();
		etl.setVisible(true);
		
	}

	public static void open_form_3(String step1_val, String step2_val) {
		
		JDialog etl = new JDialog(Mentalese_Editor.globaljFrame, "Step 3 - ETL Generator (2116 combinations)", false);
		etl.setSize(new Dimension(500, 300));
		etl.setPreferredSize(new Dimension(500, 300));
		etl.setBounds(0,0,500, 300);
		
		JPanel panel = new JPanel(new BorderLayout());

	    JPanel pnl = new JPanel(new GridLayout(4, 2));
	    pnl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
		
		ButtonGroup buttonGroup = new ButtonGroup();
	    JRadioButton b_local = new JRadioButton("LOCAL"); buttonGroup.add(b_local); pnl.add(b_local);
	    JRadioButton b_ftp = new JRadioButton("FTP"); buttonGroup.add(b_ftp); pnl.add(b_ftp);
	    JRadioButton b_ftps = new JRadioButton("FTPS"); buttonGroup.add(b_ftps); pnl.add(b_ftps);
	    JRadioButton b_sftp = new JRadioButton("SFTP"); buttonGroup.add(b_sftp); pnl.add(b_sftp);
	    JRadioButton b_ssh = new JRadioButton("SCP"); buttonGroup.add(b_ssh); pnl.add(b_ssh);
	    JRadioButton b_cifs = new JRadioButton("CIFS"); buttonGroup.add(b_cifs); pnl.add(b_cifs);
	    JRadioButton b_mentdb = new JRadioButton("MENTDB"); buttonGroup.add(b_mentdb); pnl.add(b_mentdb);

		panel.add(pnl, BorderLayout.CENTER);
		
		JButton ok = new JButton("NEXT STEP...");
		ok.setFont(new Font(ok.getFont().getName(), Font.PLAIN, 18));
		ok.setBorder(new EmptyBorder(5, 5, 5, 5));
		ok.setForeground(Color.BLACK);
		ok.setBackground(new Color(200,100,0));
		panel.add(ok, BorderLayout.SOUTH);

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						try {
							
							String value = "";
							for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
					            AbstractButton button = buttons.nextElement();

					            if (button.isSelected()) {
					            	value = button.getText();
					            	break;
					            }
					        }
							
							if (!value.equals("")) {

								etl.setVisible(false);
								etl.dispose();
								
								open_form_4(step1_val, step2_val, value);
								
							}
							
						} catch (Exception e) {
							Misc.log("error: "+e.getMessage());
						}

					}
				});
				
			}
		});
		
		JLabel label = new JLabel("Select a destination connection...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		panel.add(label, BorderLayout.NORTH);
		
		etl.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - etl.getWidth()) / 2;
		final int y = (screenSize.height - etl.getHeight()) / 2;
		etl.setLocation(x, y);

		etl.pack();
		etl.setVisible(true);
		
	}

	public static void open_form_4(String step1_val, String step2_val, String step3_val) {
		
		JDialog etl = new JDialog(Mentalese_Editor.globaljFrame, "Step 4 - ETL Generator (2116 combinations)", false);
		etl.setSize(new Dimension(500, 300));
		etl.setPreferredSize(new Dimension(500, 300));
		etl.setBounds(0,0,500, 300);
		
		JPanel panel = new JPanel(new BorderLayout());

	    JPanel pnl = null;

		ButtonGroup buttonGroup = new ButtonGroup();
		
		if (step3_val.equals("LOCAL")) {
			
			pnl = new JPanel(new GridLayout(5, 2));
		    pnl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

		    JRadioButton b_csv = new JRadioButton("CSV"); buttonGroup.add(b_csv);pnl.add(b_csv);
		    JRadioButton b_json = new JRadioButton("JSON"); buttonGroup.add(b_json);pnl.add(b_json);
		    JRadioButton b_xml = new JRadioButton("XML"); buttonGroup.add(b_xml);pnl.add(b_xml);
		    JRadioButton b_file = new JRadioButton("TXT_FILE"); buttonGroup.add(b_file);pnl.add(b_file);
		    JRadioButton b_excel = new JRadioButton("EXCEL"); buttonGroup.add(b_excel);pnl.add(b_excel);
		    JRadioButton b_excelx = new JRadioButton("EXCELX"); buttonGroup.add(b_excelx);pnl.add(b_excelx);
		    JRadioButton b_rest = new JRadioButton("REST"); buttonGroup.add(b_rest);pnl.add(b_rest);
		    JRadioButton b_soap = new JRadioButton("SOAP"); buttonGroup.add(b_soap);pnl.add(b_soap);
		    JRadioButton b_sql = new JRadioButton("SQL"); buttonGroup.add(b_sql);pnl.add(b_sql);
		    JRadioButton b_mail = new JRadioButton("MAIL"); buttonGroup.add(b_mail);pnl.add(b_mail);
		    
		} else {

			pnl = new JPanel(new GridLayout(3, 2));
		    pnl.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
			
		    JRadioButton b_csv = new JRadioButton("CSV"); buttonGroup.add(b_csv);pnl.add(b_csv);
		    JRadioButton b_json = new JRadioButton("JSON"); buttonGroup.add(b_json);pnl.add(b_json);
		    JRadioButton b_xml = new JRadioButton("XML"); buttonGroup.add(b_xml);pnl.add(b_xml);
		    JRadioButton b_file = new JRadioButton("TXT_FILE"); buttonGroup.add(b_file);pnl.add(b_file);
		    JRadioButton b_excel = new JRadioButton("EXCEL"); buttonGroup.add(b_excel);pnl.add(b_excel);
		    JRadioButton b_excelx = new JRadioButton("EXCELX"); buttonGroup.add(b_excelx);pnl.add(b_excelx);
		    
		}
		
		panel.add(pnl, BorderLayout.CENTER);
		
		JButton ok = new JButton("NEXT STEP...");
		ok.setFont(new Font(ok.getFont().getName(), Font.PLAIN, 18));
		ok.setBorder(new EmptyBorder(5, 5, 5, 5));
		ok.setForeground(Color.BLACK);
		ok.setBackground(new Color(200,100,0));
		panel.add(ok, BorderLayout.SOUTH);

		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						
						try {
							
							String value = "";
							for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
					            AbstractButton button = buttons.nextElement();

					            if (button.isSelected()) {
					            	value = button.getText();
					            	break;
					            }
					        }
							
							if (!value.equals("")) {

								etl.setVisible(false);
								etl.dispose();
								
								generate(step1_val, step2_val, step3_val, value);
								
							}
							
						} catch (Exception e) {
							Misc.log("error: "+e.getMessage());
						}

					}
				});
				
			}
		});
	    
		JLabel label = new JLabel("Select a destination format...");
		label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 18));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setBorder(new EmptyBorder(5, 5, 5, 5));
		label.setForeground(Color.BLACK);
		label.setBackground(new Color(200,200,200));
		label.setOpaque(true);
		panel.add(label, BorderLayout.NORTH);
		
		etl.getContentPane().add(panel);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - etl.getWidth()) / 2;
		final int y = (screenSize.height - etl.getHeight()) / 2;
		etl.setLocation(x, y);

		etl.pack();
		etl.setVisible(true);
		
	}

	public static void generate(String step1_val, String step2_val, String step3_val, String step4_val) throws Exception {
		
		String mql = 
				  "###############################################;\n"
				+ "# GENERATED ETL SCRIPT - YOU MUST UPDATE IT... ;\n"+
				  "###############################################;\n\n";

		try {mql += Misc.load("etl/ETL_"+step1_val+"_"+step2_val+"_"+step3_val+"_"+step4_val+".mql");} catch (Exception e) {}
		
		int pos = Mentalese_Editor.searchEmpty();
		Mentalese_Editor.globalMqlInput.setSelectedIndex(pos);
		Mentalese_Editor.inputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText(mql);
		
	}
	
}
