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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import re.jpayet.mentdb.tools.Misc;

public class LineScatterEditor {

	public String mql = null;

	public void scatterChart(String value) {

		try {
			
			JPanel panel = new JPanel(new BorderLayout());
			
			XYSeriesCollection dataset = new XYSeriesCollection();
			
			//Load the json object
			String json = value.substring(39);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = null;
			try {
				jsonObj = (JSONObject) jsonParser.parse(json);
			} catch (ParseException e) {};

			mql = (String) jsonObj.get("mql");
			JSONArray series = (JSONArray) jsonObj.get("data");
			for(int i=0;i<series.size();i++) {

				XYSeries serie = new XYSeries("S"+(i+1));
				
				JSONArray data = (JSONArray) series.get(i);
				
				for(int j=0;j<data.size();j++) {
					
					JSONArray d = (JSONArray) data.get(j);
					double x = (Double) d.get(0);
					double y = (Double) d.get(1);
					serie.add(x, y);
					
				}

			    dataset.addSeries(serie);
				
			}
		    
		    // Create chart
		    JFreeChart chart = ChartFactory.createScatterPlot(
		        "", "", "", dataset);

			chart.setBackgroundPaint(new Color(41, 41, 41));
			
			final XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(new Color(41, 41, 41));
			plot.setDomainGridlinePaint(Color.WHITE);
			plot.setRangeGridlinePaint(Color.WHITE);

			XYItemRenderer renderer = plot.getRenderer();

			XYToolTipGenerator xyToolTipGenerator = new XYToolTipGenerator()
			{
				public String generateToolTip(XYDataset dataset, int series, int item)
				{

					double X = dataset.getX(series, item).doubleValue();
					double Y = dataset.getY(series, item).doubleValue();
					
					return "<html><span style='color:#0000FF;'><b>("+X+", "+Y+")</b></span></html>";

				}
				
			};
			
			for(int i=0;i<series.size();i++) {
				renderer.setSeriesToolTipGenerator(i, xyToolTipGenerator);
			}
			plot.setRenderer(renderer);

			ChartPanel CP = new ChartPanel(chart);
			
			CP.addChartMouseListener(new ChartMouseListener() {
	            @Override
	            public void chartMouseClicked(ChartMouseEvent e) {
	            	
	            		
	            	
	            }

	            @Override
	            public void chartMouseMoved(ChartMouseEvent e) {
	                
		            	ChartEntity ce = e.getEntity();
	                if (ce instanceof XYItemEntity) {
	                			                    
	                		Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText(ce.getToolTipText());
		    				
	                }
	            	
	            }
	        });
			
			panel.add(CP, BorderLayout.CENTER);
			
			String title = "scatter <"+jsonObj.get("title")+">  ";

			JPanel pnlTab = new JPanel(new GridBagLayout());
			pnlTab.setOpaque(false);
			JLabel lblTitle = new JLabel("<html><body><table border=0 width=100%><tr><td>"+title.replace("<", "&lt;")+"</td></tr></table></body></html>");
			lblTitle.setForeground(Color.WHITE);
			JButton btnClose = new JButton();
			btnClose.setIcon(new ImageIcon("images"+File.separator+"closetab.png"));
			btnClose.setBackground(new Color(51,51,51));
			btnClose.setForeground(Color.WHITE);
			btnClose.setOpaque(true);
			btnClose.setBorderPainted(false);
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
			

			((NumberAxis) plot.getRangeAxis()).setTickLabelPaint(new Color(255, 255, 255));
			((NumberAxis) plot.getDomainAxis()).setTickLabelPaint(new Color(255, 255, 255));
			
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
			
			chart.getLegend(0).setBackgroundPaint(new Color(41,41,41));
			chart.getLegend(0).setItemPaint(new Color(255, 255, 255));

			int existPosTitle = -1;
			for(int i=0;i<Mentalese_Editor.globalOutputTabbbedPane.getTabCount();i++) {
				if (Mentalese_Editor.globalOutputTabbbedPane.getTitleAt(i).equals(title)) {
					existPosTitle = i;
					break;
				}
			}

			if (existPosTitle>-1) {
				Mentalese_Editor.globalOutputTabbbedPane.setComponentAt(existPosTitle, panel);
			} else Mentalese_Editor.globalOutputTabbbedPane.add(title, panel);

			if (existPosTitle>-1) {
				Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(existPosTitle, pnlTab);
				Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(existPosTitle);
			} else {
				Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1, pnlTab);
				Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1);
			}

		} catch (Exception e) {

			Misc.log("error: "+e.getMessage());

		}

	}

	public void lineChart(String value) {

		try {
			JPanel panel = new JPanel(new BorderLayout());
			
			// Create a simple XY chart
			TimeSeries closedSeries = new TimeSeries("closed");
			TimeSeries errorSeries = new TimeSeries("error");

			//Load the json object
			String json = value.substring(39);
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = null;
			try {
				jsonObj = (JSONObject) jsonParser.parse(json);
			} catch (ParseException e) {};

			String groupType = (String) jsonObj.get("groupType");

			mql = (String) jsonObj.get("mql");

			Mentalese_Editor.loadData(groupType, (JSONArray) jsonObj.get("closed"), closedSeries);
			Mentalese_Editor.loadData(groupType, (JSONArray) jsonObj.get("error"), errorSeries);

			// Add the series to your data set
			TimeSeriesCollection dataset = new TimeSeriesCollection();
			dataset.addSeries(errorSeries);
			dataset.addSeries(closedSeries);
			
			// Generate the graph
			JFreeChart chart = ChartFactory.createTimeSeriesChart(
					"", // Title
					"", // x-axis Label
					"", // y-axis Label
					dataset
					);
			
			

			chart.setBackgroundPaint(new Color(41, 41, 41));
			
			final XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(new Color(41, 41, 41));
			plot.setDomainGridlinePaint(Color.WHITE);
			plot.setRangeGridlinePaint(Color.WHITE);

			final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setSeriesShapesVisible(0, true);
			renderer.setSeriesShapesVisible(1, true);

			XYToolTipGenerator xyToolTipGenerator = new XYToolTipGenerator()
			{
				public String generateToolTip(XYDataset dataset, int series, int item)
				{

					SimpleDateFormat formatter = null;
					switch (groupType) {
					case "YEAR":
						formatter = new SimpleDateFormat("yyyy");
						break;
					case "MONTH":
						formatter = new SimpleDateFormat("yyyy-MM");
						break;
					case "DAY":
						formatter = new SimpleDateFormat("yyyy-MM-dd");
						break;
					case "HOUR":
						formatter = new SimpleDateFormat("yyyy-MM-dd HH");
						break;
					case "MIN":
						formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						break;
					case "SEC":
						formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						break;
					}
					long x1 = dataset.getX(series, item).longValue();
					Date time = new Date(x1);
					Number y1 = dataset.getY(series, item);
					StringBuilder stringBuilder = new StringBuilder();
					if ((""+dataset.getSeriesKey(series)).equals("closed")) {
						stringBuilder.append(String.format("<html><p style='color:#0000FF;'>Closed process</p>", dataset.getSeriesKey(series)));
						stringBuilder.append(formatter.format(time)+" = <span style='color:#0000FF;'><b>"+y1.longValue()+"</b></span>");
					} else {
						stringBuilder.append(String.format("<html><p style='color:#FF0000;'>Process in error</p>", dataset.getSeriesKey(series)));
						stringBuilder.append(formatter.format(time)+" = <span style='color:#FF0000;'><b>"+y1.longValue()+"</b></span>");
					}

					stringBuilder.append("</html>");

					return stringBuilder.toString();

				}
			};
			
			renderer.setSeriesToolTipGenerator(0, xyToolTipGenerator);
			renderer.setSeriesToolTipGenerator(1, xyToolTipGenerator);
			plot.setRenderer(renderer);

			DateAxis axis = (DateAxis) plot.getDomainAxis();
			
			((NumberAxis) plot.getRangeAxis()).setTickLabelPaint(new Color(255, 255, 255));
			
			chart.getLegend(0).setBackgroundPaint(new Color(41, 41, 41));
			chart.getLegend(0).setItemPaint(new Color(255, 255, 255));
			
			DateTickUnit unit = null;
			switch (groupType) {
			case "YEAR":
				axis.setDateFormatOverride(new SimpleDateFormat("yyyy"));
			    unit = new DateTickUnit(DateTickUnitType.YEAR, 1, new SimpleDateFormat("yyyy"));
			    axis.setVerticalTickLabels(false);
				break;
			case "MONTH":
				axis.setDateFormatOverride(new SimpleDateFormat("MM"));
			    unit = new DateTickUnit(DateTickUnitType.MONTH, 1, new SimpleDateFormat("MM"));
			    axis.setVerticalTickLabels(true);
				break;
			case "DAY":
				axis.setDateFormatOverride(new SimpleDateFormat("dd"));
			    unit = new DateTickUnit(DateTickUnitType.DAY, 1, new SimpleDateFormat("dd"));
			    axis.setVerticalTickLabels(true);
				break;
			case "HOUR":
				axis.setDateFormatOverride(new SimpleDateFormat("HH"));
			    unit = new DateTickUnit(DateTickUnitType.HOUR, 1, new SimpleDateFormat("HH"));
			    axis.setVerticalTickLabels(true);
				break;
			case "MIN":
				axis.setDateFormatOverride(new SimpleDateFormat("HH:mm"));
			    unit = new DateTickUnit(DateTickUnitType.MINUTE, 1, new SimpleDateFormat("mm"));
			    axis.setVerticalTickLabels(true);
				break;
			case "SEC":
				axis.setDateFormatOverride(new SimpleDateFormat("mm:ss"));
			    unit = new DateTickUnit(DateTickUnitType.SECOND, 1, new SimpleDateFormat("ss"));
			    axis.setVerticalTickLabels(true);
				break;
			}
			axis.setAutoTickUnitSelection(true);
		    axis.setTickUnit(unit);

			ChartPanel CP = new ChartPanel(chart);
			
			CP.addChartMouseListener(new ChartMouseListener() {
	            @Override
	            public void chartMouseClicked(ChartMouseEvent e) {
	            	
	            		
	            	
	            }

	            @Override
	            public void chartMouseMoved(ChartMouseEvent e) {
	                
		            	ChartEntity ce = e.getEntity();
	                if (ce instanceof XYItemEntity) {
	                			                    
	                	Mentalese_Editor.outputs.get(Mentalese_Editor.globalMqlInput.getSelectedIndex()).setText(ce.getToolTipText());
	    					
	                }
	            	
	            }
	        });
			
			panel.add(CP, BorderLayout.CENTER);

			JPanel pnlTab = new JPanel(new GridBagLayout());
			pnlTab.setOpaque(false);
			JLabel lblTitle = new JLabel("<html><body><table border=0 width=100%><tr><td>Lines</td></tr></table></body></html>");
			lblTitle.setForeground(Color.WHITE);
			lblTitle.setIcon(new ImageIcon("images"+File.separator+"stat.png"));
			
			JButton btnClose = new JButton();
			btnClose.setBackground(new Color(41,41,41));
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
				btnRelaod.setBackground(new Color(41,41,41));
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

			int existPosTitle = -1;
			for(int i=0;i<Mentalese_Editor.globalOutputTabbbedPane.getTabCount();i++) {
				if (Mentalese_Editor.globalOutputTabbbedPane.getTitleAt(i).equals("Lines  ")) {
					existPosTitle = i;
					break;
				}
			}

			if (existPosTitle>-1) {
				Mentalese_Editor.globalOutputTabbbedPane.setComponentAt(existPosTitle, panel);
			} else Mentalese_Editor.globalOutputTabbbedPane.add("Lines  ", panel);

			if (existPosTitle>-1) {
				Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(existPosTitle, pnlTab);
				Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(existPosTitle);
			} else {
				Mentalese_Editor.globalOutputTabbbedPane.setTabComponentAt(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1, pnlTab);
				Mentalese_Editor.globalOutputTabbbedPane.setSelectedIndex(Mentalese_Editor.globalOutputTabbbedPane.getTabCount()-1);
			}

		} catch (Exception e) {

			Misc.log("error: "+e.getMessage());

		}

	}

}
