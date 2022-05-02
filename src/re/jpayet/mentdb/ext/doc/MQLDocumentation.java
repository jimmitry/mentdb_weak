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

package re.jpayet.mentdb.ext.doc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import re.jpayet.mentdb.core.db.basic.MQLValue;
import re.jpayet.mentdb.core.db.command.CommandFullAccess;
import re.jpayet.mentdb.ext.fx.StringFx;
import re.jpayet.mentdb.ext.server.Start;
import re.jpayet.mentdb.ext.session.SessionThread;
import re.jpayet.mentdb.ext.session.SessionThreadAgent;
import re.jpayet.mentdb.ext.tools.Misc;

//The MQL documentation object
public class MQLDocumentation {

	public static LinkedHashMap<String, Vector<MQLDocumentation>> functions = new LinkedHashMap<String, Vector<MQLDocumentation>>();
	public static LinkedHashMap<String, String> ghost_functions = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> page_description = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> page_group = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> video = new LinkedHashMap<String, String>();
	
	public static int nbFunction = 0;
	
	//Function to help to develop JSP pages
	public static void test() {
		
		
		
	}
	
	//Properties
	public boolean valid = false;
	public String FX = "";
	public String functionId = "";
	public String description = "";
	public String example1 = null;
	public String result1 = null;
	public String example2 = null;
	public String result2 = null;
	public String example3 = null;
	public String result3 = null;
	public String synonymous = "";
	public boolean webSocket = false;
	public String warning = "";
	public Vector<MQLParam> parameters = new Vector<MQLParam>();
	
	//Add parameter
	public void addParam(MQLParam param) {
		
		parameters.addElement(param);
		
	}
	
	//Constructor
	public MQLDocumentation(String FX, boolean valid, String functionId, String description, 
			String example1, String result1, 
			String example2, String result2, 
			String example3, String result3, 
			boolean webSocket, String synonymous) {

		this.valid = valid;
		this.FX = FX;
		this.functionId = functionId;
		this.description = description;
		this.example1 = example1;
		this.result1 = result1;
		this.example2 = example2;
		this.result2 = result2;
		this.example3 = example3;
		this.result3 = result3;
		this.webSocket = webSocket;
		this.synonymous = synonymous;
		
		nbFunction++;
		
	}
	
	//Constructor
	public MQLDocumentation(boolean valid, String functionId, String description, 
			String example1, String result1, 
			String example2, String result2, 
			String example3, String result3, 
			boolean webSocket, String synonymous) {

		this.valid = valid;
		this.functionId = functionId;
		this.description = description;
		this.example1 = example1;
		this.result1 = result1;
		this.example2 = example2;
		this.result2 = result2;
		this.example3 = example3;
		this.result3 = result3;
		this.webSocket = webSocket;
		this.synonymous = synonymous;

		nbFunction++;
		
	}
	
	//To HTML
	public static String menu_to_html() {
		
		//Initialization
		String result = "";
		String menu = "<div style='display: inline-block;padding-top: 5px;padding-bottom: 10px;'>";
	
		int z = 0;
		for (Entry<String, Vector<MQLDocumentation>> e : MQLDocumentation.functions.entrySet()) {
			
			if (!ghost_functions.containsKey(e.getKey())) {

				if (z>0) result += "<div style='clear:left;height:20px'></div>";
				
				if (page_group.containsKey(e.getKey())) {
					menu += "<div id='"+page_group.get(e.getKey()).replace(" ", "_").toLowerCase()+"' style='padding: 10px;\n" + 
							"    font-size: 20px;\n" + 
							"    background-color: #E9E9E9;float:left;display:inline-block;width:100%;margin-top: 10px;\n" + 
							"    margin-bottom: 10px;box-sizing: border-box;'>"+page_group.get(e.getKey())+"</div>";
				}
				menu += "<a style='color: #000;font-weight:bold;font-size: 18px;padding:2px 8px 7px 0px;float:left;display:inline-block;width:400px;text-decoration:underline' href='#mql_"+e.getKey()+"'>"+e.getKey()+"</a>";
	
				result += "<div style='line-height: 32px;clear:left;font-size: 17px;font-weight:bold;padding:0px 8px 4px 0px;'><a href='mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html' id='mql_"+e.getKey()+"' style='color: #000;padding: 5px 8px;border-bottom: 1px solid #D9DEE4;margin-left: -8px;width: 100%;display: block;font-size:20px;text-decoration:underline'>"+e.getKey()+"</a></div>";
				result += "<div style='display: inline-block;padding-top: 5px;padding-bottom: 10px;'>";
				for(int i=0;i<e.getValue().size();i++) {
				
					//Get the current MQLDocumentation object
					
					MQLDocumentation currentFunction = e.getValue().get(i);
				
					String color = "#000";
					if (!currentFunction.warning.equals("")) color = "rgb(0, 102, 255)";
					
					String valid = "";
					if (currentFunction.valid) valid = ";color:"+color;
					else valid = ";color:#d70000";
					
					if (!currentFunction.FX.equals("")) {
						if (i==0) result += "<h3 style='clear: left;padding-top: 0px;padding-bottom: 0px;margin-bottom: 10px;margin-top: 0px;'>"+currentFunction.FX+"</h3>";
						else result += "<h3 style='clear: left;padding-top: 15px;padding-bottom: 0px;margin-bottom: 10px;'>"+currentFunction.FX+"</h3>";
					}
					
					if (currentFunction.webSocket) result += "<a title='"+currentFunction.warning.replace("'", "&#39;")+"' style='"+valid+";font-size: 16px;padding:2px 8px 7px 0px;float:left;display:inline-block;width:400px;text-decoration:underline' href='mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html#fx_"+currentFunction.functionId.replace(" ", "_")+"'>@"+currentFunction.functionId+(!currentFunction.synonymous.equals("")?" <span style='font-size:8px;'>"+currentFunction.synonymous+"</span>":"")+"</a>";
					else result += "<a title='"+currentFunction.warning.replace("'", "&#39;")+"' style='"+valid+";font-size: 16px;padding:2px 8px 7px 0px;float:left;display:inline-block;width:400px;text-decoration:underline' href='mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html#fx_"+currentFunction.functionId.replace(" ", "_")+"'>"+currentFunction.functionId+(!currentFunction.synonymous.equals("")?" <span style='font-size:12px;'>"+currentFunction.synonymous+"</span>":"")+"</a>";
				
				}
				
				result += "</div>";
				z++;
				
			}
			
		}
		result += "<div style='clear:left;'></div>";

		result += "<div style='border-bottom: 1px solid #D9DEE4;'></div>";
		
		menu += "</div>";
		
		return "<div style='height:1px;width:100%;background-color:#999'></div>"+menu+"<div style='height:1px;width:100%;background-color:#999'/></div><br>"+result;
		
	}
	
	//To HTML
	public static String mqlPage_to_html() throws Exception {
		
		//Initialization
		String result = "";
		
		SessionThread mentdbInternalSession = SessionThreadAgent.allServerThread.get(0L).serverThread;
	
		//Description
		for (Entry<String, Vector<MQLDocumentation>> e : MQLDocumentation.functions.entrySet()) {
			
			result = "";

			result += "<div style='line-height: 32px;clear:left;font-weight:bold;padding:0px 8px 4px 0px;font-size:24px;'>"+e.getKey()+"</div>";
			if (page_description.containsKey(e.getKey())) result += "<div style='border-bottom: 1px solid #D9DEE4;'></div><h1 style='margin-top: 25px;margin-bottom: 15px;font-size: 17px;color: #3a3a3a;font-weight: normal;line-height: 25px;'>"+page_description.get(e.getKey())+"</h1>";
			if (!ghost_functions.containsKey(e.getKey())) {
				result += "<div style='display: inline-block;padding-top: 18px;padding-bottom: 18px;padding-left: 15px;background-color: #F0F0F0;border: 1px #D9D9D9 solid;'>";
				for(int i=0;i<e.getValue().size();i++) {
				
					//Get the current MQLDocumentation object
					
					MQLDocumentation currentFunction = e.getValue().get(i);
				
					String color = "#333";
					if (!currentFunction.warning.equals("")) color = "rgb(0, 102, 255)";
					
					String valid = "";
					if (currentFunction.valid) valid = ";color:"+color;
					else valid = ";color:#d70000";
					
					if (!currentFunction.FX.equals("")) {
						if (i==0) result += "<h3 style='clear: left;padding-top: 0px;padding-bottom: 0px;margin-bottom: 10px;margin-top: 0px;'>"+currentFunction.FX+"</h3>";
						else result += "<h3 style='clear: left;padding-top: 15px;padding-bottom: 0px;margin-bottom: 10px;'>"+currentFunction.FX+"</h3>";
					}
					
					if (currentFunction.webSocket) result += "<a title='"+currentFunction.warning.replace("'", "&#39;")+"' style='"+valid+";font-size: 16px;text-decoration: underline;padding:2px 8px 7px 0px;float:left;display:inline-block;width:400px;' href='mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html#fx_"+currentFunction.functionId.replace(" ", "_")+"'>@"+currentFunction.functionId+(!currentFunction.synonymous.equals("")?" <span style='font-size:12px;'>"+currentFunction.synonymous+"</span>":"")+"</a>";
					else result += "<a title='"+currentFunction.warning.replace("'", "&#39;")+"' style='"+valid+";font-size: 16px;text-decoration: underline;padding:2px 8px 7px 0px;float:left;display:inline-block;width:400px;' href='mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html#fx_"+currentFunction.functionId.replace(" ", "_")+"'>"+currentFunction.functionId+(!currentFunction.synonymous.equals("")?" <span style='font-size:12px;'>"+currentFunction.synonymous+"</span>":"")+"</a>";
				
				}
				
				result += "</div>";
			}
			result += "<div style='clear:left;'></div>";
			
			if (video.containsKey(e.getKey())) {
				
				result += "<iframe style='border:0px;margin-top:10px' width='800' height='400'";
				result += "		src='"+video.get(e.getKey())+"' allowfullscreen='allowfullscreen' mozallowfullscreen='mozallowfullscreen' msallowfullscreen='msallowfullscreen' oallowfullscreen='oallowfullscreen' webkitallowfullscreen='webkitallowfullscreen'>";
				result += "</iframe>";
				
			}
						
			for(int i=0;i<e.getValue().size();i++) {
			
				//Get the current MQLDocumentation object
				
				MQLDocumentation currentFunction = e.getValue().get(i);
				
				String currentDocId = "Doc > "+currentFunction.functionId;
				if (currentDocId.length()<60) Misc.system_out_print("\r"+StringFx.rpad(currentDocId, " ", "61"), false, "", "");
				else Misc.system_out_print("\r"+StringFx.rpad(currentDocId.substring(0, 60), " ", "61"), false, "", "");
				
				result += "<div style='-moz-box-sizing: border-box;box-sizing: border-box;border-bottom:1px #F0F0F0 solid;padding-bottom:20px;font-size:16px'>";
				result += "<div id='fx_"+currentFunction.functionId.replace(" ", "_")+"'><h3 style='color:#000'>"+(currentFunction.webSocket?"@":"")+currentFunction.functionId;
				if (currentFunction.parameters.size()>0) for(int r=0;r<currentFunction.parameters.size();r++) {
					result += " &lt;<span style='color:#808080;font-weight:normal'>"+currentFunction.parameters.get(r).name+"</span>&gt;";
				}
				result += "</h3></div>";
				result += "<div style='margin-left:0px;'>";
				if (!currentFunction.synonymous.equals("")) result += "<div style='color:#000'><b>Synonymous</b><br><br> &nbsp;  &nbsp; "+currentFunction.synonymous+"</div><br>";
				if (!currentFunction.description.equals("")) result += "<div style='color:#000'><b>Description</b><br><br> &nbsp;  &nbsp; "+currentFunction.description+"</div><br>";
				if (currentFunction.parameters.size()>0) {
					result += "<div style='color:#000'><b>Parameters</b><br>";
					
					for(int r=0;r<currentFunction.parameters.size();r++) {
						result += "<br> &nbsp;  &nbsp; <span style='color:#808080'>"+currentFunction.parameters.get(r).name+"</span>: &nbsp; "+currentFunction.parameters.get(r).description+" - <span style='color:#333'>"+currentFunction.parameters.get(r).type+"</span> - "+(currentFunction.parameters.get(r).required?"<span style='color:#333'>required</span>":"<span style='color:#333'>not required</span>");
					}
					result += "</div>";
				}
				if (currentFunction.example1!=null) {
					result += "<div style='overflow:auto;line-height: 20px;margin-top: 15px;padding:10px 15px 0px 15px;background-color: #F8F8F8;border:1px #F0F0F0 solid'>";
					if (e.getKey().equals("XML")) {
						result += "<pre style='color:#000;margin-top: 0px;'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example1.replace("<", "&lt;"))+"</pre>";
						result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result1.replace("<", "&lt;"))+"</pre>";
					} else {
						result += "<pre style='color:#000;margin-top: 0px;'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example1)+"</pre>";
						result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result1)+"</pre>";
					}
					if (currentFunction.valid && Start.CHECK_DOCUMENTATION==1) {
						String mqlResult = "";
						try {
							
							Vector<Vector<MQLValue>> inputVectorCmds = Misc.splitCommand(currentFunction.example1.replace("<br>", "\n"));
							for(int ii=0;ii<inputVectorCmds.size();ii++) {
								
								Vector<MQLValue> inputVectorCmd = inputVectorCmds.get(ii);
								mqlResult = CommandFullAccess.execute(mentdbInternalSession, inputVectorCmd, mentdbInternalSession.env, null, null);
	
							}

							result += "<pre style='color:#090'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult)+"</pre>";
							result += "<pre style='color:#009'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult.replace("\n", "&lt;br&gt;").replace("\\", "\\\\").replace("\"", "\\\""))+"</pre>";
							
						} catch (Exception z) {
							
							mqlResult = z.getMessage()+"";
							result += "<pre style='color:#F00'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult)+"</pre>";
							
						}
						
					}
					result += "</div>";
				}
				if (currentFunction.example2!=null) {
					result += "<div style='overflow:auto;line-height: 20px;margin-top: 15px;padding:10px 15px 0px 15px;background-color: #F8F8F8;border:1px #F0F0F0 solid'>";if (e.getKey().equals("XML")) {
						result += "<pre style='color:#000;margin-top: 0px;'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example2.replace("<", "&lt;"))+"</pre>";
						result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result2.replace("<", "&lt;"))+"</pre>";
					} else {
						result += "<pre style='color:#000;margin-top: 0px;'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example2)+"</pre>";
						result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result2)+"</pre>";
					}
					if (currentFunction.valid && Start.CHECK_DOCUMENTATION==1) {
						String mqlResult = "";
						try {
							
							Vector<Vector<MQLValue>> inputVectorCmds = Misc.splitCommand(currentFunction.example2.replace("<br>", "\n"));
							for(int ii=0;ii<inputVectorCmds.size();ii++) {
								
								Vector<MQLValue> inputVectorCmd = inputVectorCmds.get(ii);
								mqlResult = CommandFullAccess.execute(mentdbInternalSession, inputVectorCmd, mentdbInternalSession.env, null, null);
	
							}
							
							result += "<pre style='color:#090'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult)+"</pre>";
							result += "<pre style='color:#009'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult.replace("\n", "&lt;br&gt;").replace("\\", "\\\\").replace("\"", "\\\""))+"</pre>";
							
						} catch (Exception z) {
							
							mqlResult = z.getMessage()+"";
							result += "<pre style='color:#F00'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult)+"</pre>";
							
						}
						
					}
					result += "</div>";
				}
				if (currentFunction.example3!=null) {
					result += "<div style='overflow:auto;line-height: 20px;margin-top: 15px;padding:10px 15px 0px 15px;background-color: #F8F8F8;border:1px #F0F0F0 solid'>";
					if (e.getKey().equals("XML")) {
						result += "<pre style='color:#000;margin-top: 0px;'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example3.replace("<", "&lt;"))+"</pre>";
						result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result3.replace("<", "&lt;"))+"</pre>";
					} else {
						result += "<pre style='color:#000;margin-top: 0px;'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example3)+"</pre>";
						result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result3)+"</pre>";
					}
					if (currentFunction.valid && Start.CHECK_DOCUMENTATION==1) {
						String mqlResult = "";
						try {
							
							Vector<Vector<MQLValue>> inputVectorCmds = Misc.splitCommand(currentFunction.example3.replace("<br>", "\n"));
							for(int ii=0;ii<inputVectorCmds.size();ii++) {
								
								Vector<MQLValue> inputVectorCmd = inputVectorCmds.get(ii);
								mqlResult = CommandFullAccess.execute(mentdbInternalSession, inputVectorCmd, mentdbInternalSession.env, null, null);
	
							}
							
							result += "<pre style='color:#090'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult)+"</pre>";
							result += "<pre style='color:#009'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult.replace("\n", "&lt;br&gt;").replace("\\", "\\\\").replace("\"", "\\\""))+"</pre>";
							
						} catch (Exception z) {
							
							mqlResult = z.getMessage()+"";
							result += "<pre style='color:#F00'><span style='font-size: 18px;'><b>checker</b></span><br>"+Misc.splitCommandHtml(mqlResult)+"</pre>";
							
						}
						
					}
					result += "</div>";
				}
				result += "</div>";
				result += "</div>";
				
			}
			
			Misc.create("docs/mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html", Misc.load("docs/mql_fx_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[PAGE_TITLE]", ""+e.getKey()).replace("[MQL_FX]", result).replace("[MQL_VERSION]", Start.version));
			if ((new File("/Users/jimmitry/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions")).exists()) {
				Misc.create("/Users/jimmitry/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions/mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html", Misc.load("docs"+File.separator+"mentdb.org"+File.separator+"mql_fx_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[PAGE_TITLE]", ""+e.getKey()).replace("[PAGE_TITLE_U]", (""+e.getKey()).toUpperCase()).replace("[MQL_FX]", result).replace("[MQL_VERSION]", Start.version));
			}
			if ((new File("/Users/jim/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions")).exists()) {
				Misc.create("/Users/jim/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions/mql_"+e.getKey().toLowerCase().replace(" ", "_")+".html", Misc.load("docs"+File.separator+"mentdb.org"+File.separator+"mql_fx_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[PAGE_TITLE]", ""+e.getKey()).replace("[PAGE_TITLE_U]", (""+e.getKey()).toUpperCase()).replace("[MQL_FX]", result).replace("[MQL_VERSION]", Start.version));
			}
			
		}
		
		return result;
		
	}
	
	//To HTML
	public static String mqlFuntion_to_html(String idClass, String idFunction) throws Exception {
		
		//Initialization
		String result = "";
		
		Vector<MQLDocumentation> e = MQLDocumentation.functions.get(idClass);
						
		for(int i=0;i<e.size();i++) {
		
			//Get the current MQLDocumentation object
			
			MQLDocumentation currentFunction = e.get(i);
			
			if (currentFunction.functionId.equals(idFunction)) {
			
				result += "<div style='-moz-box-sizing: border-box;box-sizing: border-box;padding-bottom:20px;font-size:16px'>";
				result += "<div id='fx_"+currentFunction.functionId.replace(" ", "_")+"'><h3 style='color:#000'><a href='#top'><img src='images/top.png' style='width:16px'></a> "+(currentFunction.webSocket?"@":"")+currentFunction.functionId;
				if (currentFunction.parameters.size()>0) for(int r=0;r<currentFunction.parameters.size();r++) {
					result += " &lt;<span style='color:#d70000;font-weight:normal'>"+currentFunction.parameters.get(r).name+"</span>&gt;";
				}
				result += "</h3></div>";
				result += "<div style='margin-left:20px;'>";
				if (!currentFunction.synonymous.equals("")) result += "<div style='color:#000'><b>Synonymous</b><br> &nbsp;  &nbsp; "+currentFunction.synonymous+"</div>";
				if (!currentFunction.description.equals("")) result += "<div style='color:#000'><b>Description</b><br> &nbsp;  &nbsp; "+currentFunction.description+"</div>";
				if (currentFunction.parameters.size()>0) {
					result += "<div style='color:#000'><b>Parameters</b>";
					
					for(int r=0;r<currentFunction.parameters.size();r++) {
						result += "<br> &nbsp;  &nbsp; <span style='color:#d70000'>"+currentFunction.parameters.get(r).name+"</span>: &nbsp; "+currentFunction.parameters.get(r).description+" - <span style='color:#4C9ED9'>"+currentFunction.parameters.get(r).type+"</span> - "+(currentFunction.parameters.get(r).required?"<span style='color:#d70000'>required</span>":"<span style='color:#0d0'>not required</span>");
					}
					result += "</div>";
				}
				if (currentFunction.example1!=null) {
					result += "<div style='overflow:auto;line-height: 20px;margin-top: 15px;padding:10px 15px 0px 15px;background-color: #F8F8F8;border:1px #F0F0F0 solid'>";
					result += "<div style='color:#000'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example1)+"</div>";
					result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result1)+"</pre>";
					result += "</div>";
				}
				if (currentFunction.example2!=null) {
					result += "<div style='overflow:auto;line-height: 20px;margin-top: 15px;padding:10px 15px 0px 15px;background-color: #F8F8F8;border:1px #F0F0F0 solid'>";
					result += "<div style='color:#000'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example2)+"</div>";
					result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result2)+"</pre>";
					result += "</div>";
				}
				if (currentFunction.example3!=null) {
					result += "<div style='overflow:auto;line-height: 20px;margin-top: 15px;padding:10px 15px 0px 15px;background-color: #F8F8F8;border:1px #F0F0F0 solid'>";
					result += "<div style='color:#000'><span style='font-size: 18px;'><b>admin</b></span><br>"+Misc.splitCommandHtml(currentFunction.example3)+"</div>";
					result += "<pre style='color:#000'><span style='font-size: 18px;'><b>mentdb</b></span><br>"+Misc.splitCommandHtml(currentFunction.result3)+"</pre>";
					result += "</div>";
				}
				result += "</div>";
				result += "</div>";
				
			}
			
		}
		
		return result;
		
	}
	
	//Initialization
	@SuppressWarnings("unchecked")
	public static void init() throws Exception {
		
		MQLDocumentation mql = null;
		Doc_Mentalese_Application.init(functions, page_description, mql, ghost_functions, page_group);
		Doc_Mentalese_SOA.init(functions, page_description, mql, ghost_functions, page_group);
		Doc_Mentalese_Data_Transform.init(functions, page_description, mql, ghost_functions, page_group);
		Doc_Mentalese_Extract_Load.init(functions, page_description, mql, ghost_functions, page_group);
		Doc_Mentalese_ESB.init(functions, page_description, mql, ghost_functions, page_group);
		
		Doc_Mentalese_Higth_Algo.init(functions, page_description, mql, ghost_functions, page_group);
		Doc_Mentalese_Application.init(functions, page_description, mql, ghost_functions, page_group);
		
		page_group.put("Language", "Mentalese");

		functions.put("Language", new Vector<MQLDocumentation>());
		page_description.put("Language", "<img src='images/p.png' style='vertical-align: middle;'>Here you can find the functions needed to create new languages in the MentDB engine."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A created language can not be deleted.");
		mql = new MQLDocumentation(true, "language create", "To insert a new language", "language create \"en\";", "Language en created with successful.", null, null, null, null, false, "language create|insert|add");
		mql.addParam(new MQLParam("lang", "The language id", "string", true));
		functions.get("Language").add(mql);
		functions.get("Language").add(new MQLDocumentation(true, "language show", "To show all languages", "language show", "[<br>  \"en\",<br>  \"fr\",<br>  \"io\"<br>]", "@[r_fr_?_est_une_langue]", "RG[b]", "graph relation (@[r_fr_?_est_une_langue]);", "j23i88m90m76i39t04r09y35p14a96y09e57t39{\"e\":[{\"n1\":\"V0_RG[b]\",\"n2\":\"T1_TH[]\",\"k\":\"$V0_RG[b]_T1_TH[]\"},{\"n1\":\"T1_TH[]\",\"n2\":\"T2_TH[2]\",\"k\":\"$T1_TH[]_T2_TH[2]\"},{\"n1\":\"T2_TH[2]\",\"n2\":\"T3_TH[8]\",\"k\":\"$T2_TH[2]_T3_TH[8]\"},{\"n1\":\"T3_TH[8]\",\"n2\":\"T4_TH[e]\",\"k\":\"$T3_TH[8]_T4_TH[e]\"},{\"n1\":\"V0_RG[b]\",\"n2\":\"M5_[RG[b] 1\\/3]\",\"k\":\"$V0_RG[b]_5\"},{\"n1\":\"M5_[RG[b] 1\\/3]\",\"n2\":\"M6_[RG[b] 2\\/3]\",\"k\":\"$M5_[RG[b] 1\\/3]_6\"},{\"n1\":\"M6_[RG[b] 2\\/3]\",\"n2\":\"B7_L\",\"k\":\"M6_[RG[b] 2\\/3]_B7_L\"},{\"n1\":\"B7_L\",\"n2\":\"R8_RL[4x]\",\"k\":\"B7_L_R8_RL[4x]\"},{\"n1\":\"R8_RL[4x]\",\"n2\":\"R9_RL[4w]\",\"k\":\"R8_RL[4x]_R9_RL[4w]\"},{\"n1\":\"R9_RL[4w]\",\"n2\":\"R10_RL[4v]\",\"k\":\"R9_RL[4w]_R10_RL[4v]\"},{\"n1\":\"M6_[RG[b] 2\\/3]\",\"n2\":\"M11_[RG[b] 3\\/3]\",\"k\":\"$M6_[RG[b] 2\\/3]_11\"},{\"n1\":\"M11_[RG[b] 3\\/3]\",\"n2\":\"B12_L\",\"k\":\"M11_[RG[b] 3\\/3]_B12_L\"},{\"n1\":\"B12_L\",\"n2\":\"R13_RL[4x]\",\"k\":\"B12_L_R13_RL[4x]\"},{\"n1\":\"R13_RL[4x]\",\"n2\":\"R14_RL[4w]\",\"k\":\"R13_RL[4x]_R14_RL[4w]\"},{\"n1\":\"R14_RL[4w]\",\"n2\":\"R15_RL[4v]\",\"k\":\"R14_RL[4w]_R15_RL[4v]\"}],\"n\":{\"M5_[RG[b] 1\\/3]\":\"1\",\"T4_TH[e]\":\"TH[e]\",\"R10_RL[4v]\":\"RL[4v]\",\"M6_[RG[b] 2\\/3]\":\"2\",\"T2_TH[2]\":\"TH[2]\",\"B12_L\":\"L\",\"V0_RG[b]\":\"RG[b]\",\"R14_RL[4w]\":\"RL[4w]\",\"T1_TH[]\":\"TH[]\",\"T3_TH[8]\":\"TH[8]\",\"R8_RL[4x]\":\"RL[4x]\",\"R13_RL[4x]\":\"RL[4x]\",\"R9_RL[4w]\":\"RL[4w]\",\"M11_[RG[b] 3\\/3]\":\"3\",\"R15_RL[4v]\":\"RL[4v]\",\"B7_L\":\"L\"}}", false, ""));
		
		functions.put("Symbol", new Vector<MQLDocumentation>());
		page_description.put("Symbol", "<img src='images/p.png' style='vertical-align: middle;'>Words are based on symbols linked together congnitively."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>When you stimulate a word, you also stimulate all the symbols making up the word."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>You can not delete a symbol.");
		mql = new MQLDocumentation(true, "symbol create", "To insert a new symbol", "-> \"[c_lang]\" \"en\";\n" + 
				"#Add the symbols A-Z;\n" + 
				"for (-> \"[i]\" 65) (<= [i] 90) (++ \"[i]\") {\n" + 
				"	symbol create (string char [i];) [c_lang]; \n" + 
				"};\n" + 
				"#Add the symbols 0-9;\n" + 
				"for (-> \"[i]\" 48) (<= [i] 57) (++ \"[i]\") {\n" + 
				"	symbol create (string char [i];) [c_lang]; \n" + 
				"};\n" + 
				"#Add the symbols a-z;\n" + 
				"for (-> \"[i]\" 97) (<= [i] 122) (++ \"[i]\") {\n" + 
				"	symbol create (string char [i];) [c_lang]; \n" + 
				"};\n" + 
				"symbol create \".\" [c_lang]; \n" + 
				"symbol create \",\" [c_lang]; \n" + 
				"symbol create \"-\" [c_lang]; \n" + 
				"symbol create \";\" [c_lang]; \n" + 
				"symbol create \":\" [c_lang]; \n" + 
				"symbol create \"!\" [c_lang]; \n" + 
				"symbol create \"?\" [c_lang];", "Symbol S[z] created with successful in the language 'en'.", "symbol create \"-\" en", "Symbol S[-] created with successful in the language 'en'.", null, null, false, "symbol create|insert|add");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol first", "To get the first symbol tab link", "symbol first s en", "TL[657370616365 1 en]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol last", "To get the last symbol tab link", "symbol last s en", "TL[63686f7365 3 en]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol show", "To show symbol tab links", "symbol show s en", "[<br>  \"TL[657370616365 1 en]\",<br>  \"TL[73e9706172e9 0 en]\",<br>  \"TL[706872617365 4 en]\",<br>  \"TL[646976697365 4 en]\",<br>  \"TL[7375707072696d65 0 en]\",<br>  \"TL[6c69737465 2 en]\",<br>  \"TL[706173 2 en]\",<br>  \"TL[73616c7574 0 en]\",<br>  \"TL[73696e6f6e 0 en]\",<br>  \"TL[616c6f7273 4 en]\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol get", "To get the symbol tab link", "symbol get s 0 en", "TL[657370616365 1 en]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("position", "The position", "integer>=0", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol perception", "To show symbol tab link perception", "symbol perception s en", "[<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[657370616365 1 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[73e9706172e9 0 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[706872617365 4 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[646976697365 4 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[7375707072696d65 0 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[6c69737465 2 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[706173 2 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[73616c7574 0 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[73696e6f6e 0 en]\"<br>  },<br>  {<br>    \"v\": 0.0,<br>    \"w\": 0,<br>    \"nw\": 19,<br>    \"k\": \"TL[616c6f7273 4 en]\"<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol show words", "To show symbol words", "symbol show words s en", "[<br>  \"espace\",<br>  \"séparé\",<br>  \"phrase\",<br>  \"divise\",<br>  \"supprime\",<br>  \"liste\",<br>  \"pas\",<br>  \"salut\",<br>  \"sinon\",<br>  \"alors\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol show languages", "To show symbol languages", "symbol show languages s", "[<br>  \"io\",<br>  \"en\",<br>  \"fr\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbol", "The symbol", "string", true));
		functions.get("Symbol").add(mql);
		mql = new MQLDocumentation(true, "symbol stimulate", "To stimulate a symbol tab link", "symbol stimulate \"TL[63686f7365 3 en]\";", "Symbol tab link stimulated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("tabLinkId", "The symbol tab link id", "string", true));
		functions.get("Symbol").add(mql);
		
		functions.put("Word", new Vector<MQLDocumentation>());
		page_description.put("Word", "<img src='images/p.png' style='vertical-align: middle;'>Words and symbols form a gigantic index that points to thoughts."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>You can easily search for a word with just a few symbols."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A word can exist in several languages, or sometimes in the same language.");
		mql = new MQLDocumentation(true, "word create", "To insert a new word in a language", "word create dog en false;", "TH[6q]", "word create chien fr false", "TH[6r]", "word create cat en false", "TH[6s]", false, "word create|insert|add");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("type", "The type", "string", true));
		mql.addParam(new MQLParam("lock_translation", "Lock the translation", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word create", "To insert a new word in a language with separator", "word create \"New-York\" en \"-\" false;", "TH[6v]", null, null, null, null, false, "word create|insert|add");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("separator", "The separator", "string", true));
		mql.addParam(new MQLParam("type", "The type", "string", true));
		mql.addParam(new MQLParam("lock_translation", "Lock the translation", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word exist", "To check if a word already exist in a language", "word exist dog en", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word first", "To get the first word tab link", "word first dog en", "TH[6q]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word last", "To get the last word tab link", "word last dog en", "TH[6q]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word get", "To get the word tab link", "word get dog 0 en", "TH[6q]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("position", "The position", "integer>=0", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word lang probability", "To get the language probability", "word lang probability \"dog\"", "[<br>  {<br>    \"v\": 100.0,<br>    \"k\": \"en\"<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sentence", "The sentence", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word search", "To search words", "word search do \"do.*\" top en", "[{\"dog\":100.0}]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbols", "Symbols", "string", true));
		mql.addParam(new MQLParam("regex", "The regex", "string", true));
		mql.addParam(new MQLParam("order", "The order", "string (asc|desc|top)", true));
		mql.addParam(new MQLParam("lang", "The lang", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word levenshtein", "To search levenshtein distance between words", "word levenshtein dg top en", "[{\"Dog\":33.33333333333333}]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("symbols", "Symbols", "string", true));
		mql.addParam(new MQLParam("order", "The order", "string (asc|desc|top)", true));
		mql.addParam(new MQLParam("lang", "The lang", "string", false));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word show languages", "To show all languages for a specific word", "word show languages dog;", "[<br>  \"en\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word show", "To show word tab links in all languages", "word show dog", "[<br>  {<br>    \"thoughtId\": \"TH[6q]\",<br>    \"lang\": \"en\"<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word show", "To show word tab links", "word show dog en", "[<br>  \"TH[6q]\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word perception", "To show symbol tab link perceptions for a specific word (average % tab link)", "word perception dog en", "[\n" + 
				"  {\n" + 
				"    \"v\": 100.0,\n" + 
				"    \"w\": 1,\n" + 
				"    \"mw\": 1,\n" + 
				"    \"k\": \"TL[646f67 0 en]\"\n" + 
				"  },\n" + 
				"  {\n" + 
				"    \"v\": 0.0,\n" + 
				"    \"w\": 0,\n" + 
				"    \"mw\": 1,\n" + 
				"    \"k\": \"TL[646f67 1 en]\"\n" + 
				"  },\n" + 
				"  {\n" + 
				"    \"v\": 0.0,\n" + 
				"    \"w\": 0,\n" + 
				"    \"mw\": 1,\n" + 
				"    \"k\": \"TL[646f67 2 en]\"\n" + 
				"  }\n" + 
				"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word perception symbol", "To show symbol perception for a specific word (average % symbol)", "word perception symbol dog en", "[\n" + 
				"  {\n" + 
				"    \"v\": 33.33333333333333,\n" + 
				"    \"w\": 2,\n" + 
				"    \"mw\": 6,\n" + 
				"    \"k\": \"S[d]\"\n" + 
				"  },\n" + 
				"  {\n" + 
				"    \"v\": 50.0,\n" + 
				"    \"w\": 3,\n" + 
				"    \"mw\": 6,\n" + 
				"    \"k\": \"S[o]\"\n" + 
				"  },\n" + 
				"  {\n" + 
				"    \"v\": 16.666666666666664,\n" + 
				"    \"w\": 1,\n" + 
				"    \"mw\": 6,\n" + 
				"    \"k\": \"S[g]\"\n" + 
				"  }\n" + 
				"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word perception thought", "To show thought perception for a specific word", "word perception thought dog en", "[\n" + 
				"  {\n" + 
				"    \"v\": 0.0,\n" + 
				"    \"w\": 0,\n" + 
				"    \"mw\": 0,\n" + 
				"    \"k\": \"TH[0]\"\n" + 
				"  }\n" + 
				"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word stimulate", "To stimulate a word", "word stimulate dog en;", "Word W[dog] stimulated with successful in the language 'en'.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word delete", "To delete a word in a language", "word delete dog en;", "Word W[dog] deleted with successful in the language 'en'.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Word").add(mql);
		mql = new MQLDocumentation(true, "word delete", "To delete a word", "word delete dog;", "Word W[dog] deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		functions.get("Word").add(mql);
		
		functions.put("Thought", new Vector<MQLDocumentation>());
		page_description.put("Thought", "<img src='images/p.png' style='vertical-align: middle;'>Depending on the language, a word may have identical or different meanings."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>The same word can have many different thoughts, eg: the hand, to hand ..."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A thought can be stimulated and all these relationships can go up.");
		mql = new MQLDocumentation(true, "thought create", "insert thought", "thought insert cat \"\" en false;", "TH[6t]", null, null, null, null, false, "thought create|insert|add");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("separator", "The separator", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("lock_translation", "Lock the translation", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought show", "To show all thoughts linked with a word in a language", "thought show cat en", "[<br>  \"TH[6t]\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought show", "To show all thoughts linked with a word in all languages", "thought show cat", "[<br>  {<br>    \"thoughtId\": \"TH[6t]\",<br>    \"lang\": \"en\"<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought first", "To get the first thought", "thought first cat en", "TH[6t]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought last", "To get the last thought", "thought last cat en", "TH[6t]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought get", "To get a thought", "thought get cat 0 en", "TH[6t]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("position", "The position", "integer>=0", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought merge", "To merge thoughts", "thought merge \"th\" TH[6t] TH[6s];", "Thoughts merged with successful.", "thought merge \"th\" (<br>	word create snake en false;<br>) (<br>	word create serpent fr false;<br>);", "Thoughts merged with successful.", null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|...)", "string", true));
		mql.addParam(new MQLParam("thoughtId1", "The thought id 1", "string", true));
		mql.addParam(new MQLParam("thoughtId2", "The thought id 2", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought prob_in_words", "To get the thoughts probability in words", "thought prob_in_words \"TH[6]\" \"cat\";", "100", null, null, null, null, false, "");
		mql.addParam(new MQLParam("ths", "The thoughts", "string", true));
		mql.addParam(new MQLParam("words", "The words", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought show words", "To show all words in a thought", "thought show words TH[6]", "[<br>  {<br>    \"lang\": \"en\",<br>    \"word\": \"W[cat]\"<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought show words", "To show all words in a thought for a specific language", "thought show words TH[6] en", "[<br>  {<br>    \"lang\": \"fr\",<br>    \"word\": \"W[cat]\"<br>  }<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought stimulate", "To stimulate a thought", "thought stimulate TH[6t];", "Thought TH[6t] stimulated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought delete", "To delete a specific thought", "thought delete (word create chicken en false);", "Thought TH[115] deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought delete by lang", "To delete a specific thought in a language", "thought delete by lang en (word create chicken en false);", "Thought TH[115] deleted with successful in the language 'en'.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought delete by word", "To delete a specific thought in a word", "thought delete by word chicken (word create chicken en false);", "Thought TH[115] deleted with successful in the word 'chicken'.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		functions.get("Thought").add(mql);
		mql = new MQLDocumentation(true, "thought delete by word lang", "To delete a specific thought in a word and in a language", "thought delete by word lang chicken en (word create chicken en false);", "Thought TH[115] deleted with successful in the word 'chicken' and the language en.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("word", "The word", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("thoughtId", "The thought id", "string", true));
		functions.get("Thought").add(mql);

		functions.put("Relation", new Vector<MQLDocumentation>());
		page_description.put("Relation", "<img src='images/p.png' style='vertical-align: middle;'>Here you can create relationships between thoughts, or other relationships."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>A relation can be stimulated and all these sub relationships can go up.");
		mql = new MQLDocumentation(true, "relation create", "To insert a new relation", "relation create (concat \n" + 
				"	(word create a en false) \" \"\n" + 
				"	(word create dog en false) \" \"\n" + 
				"	(word create is en false) \" \"\n" + 
				"	(word create an en false) \" \"\n" + 
				"	(word create animal en false) \" \"\n" + 
				"	(word create . en false)\n" + 
				") en \"context1\" (mql {\n" + 
				"	\"ok\";\n" + 
				"});", "RL[v]", "relation show sentence RL[v]", "something is something", null, null, false, "relation create|insert|add");
		mql.addParam(new MQLParam("thoughtOrRelations", "Thoughts or relations", "string", true));
		mql.addParam(new MQLParam("lang", "The language id", "string", true));
		mql.addParam(new MQLParam("context", "The context of the relation", "string", true));
		mql.addParam(new MQLParam("mql", "The source code to execute", "string", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation show sentence", "To show the sentence of a relation", "relation show sentence RL[w];", "a dog is an animal", null, null, null, null, false, "");
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation translate", "Try to get a translation of a relation", "relation translate RL[w] 100 en \"\";", "a dog is an animal", null, null, null, null, false, "");
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		mql.addParam(new MQLParam("cooperation", "The percent cooperation (between 0 and 100)", "number", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("level", "The level (vous)", "string", true));
		mql.addParam(new MQLParam("th_N", "The TH value", "string", false));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation search", "To make a basic search on thoughts", "relation search \"dog\" \"en\" false", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("text", "The text (separate by a space)", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("searchPunctuation", "Search punctuations (1|0)", "boolean", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation execute", "To execute a relation", "relation execute \"dog\" \"{}\" 30 \"en\" false;", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("text", "The text (separate by a space)", "string", true));
		mql.addParam(new MQLParam("context_obj", "The current context object", "string", true));
		mql.addParam(new MQLParam("context_size", "The current context size", "string", true));
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("searchPunctuation", "Search punctuations (1|0)", "boolean", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation show thoughts", "To show all thoughts in a relation", "relation show thoughts RL[w]", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation show thought nodes", "To show all thought nodes in a relation", "relation show thought nodes RL[w]", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation stimulate", "To stimulate a relation", "relation stimulate RL[w];", "Relation RL[w] stimulated with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Relation").add(mql);
		mql = new MQLDocumentation(true, "relation delete", "To a relation", "relation delete RL[w];", "Relation 'RL[w]' deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Relation").add(mql);
		
		functions.put("Circle", new Vector<MQLDocumentation>());
		page_description.put("Circle", "<img src='images/p.png' style='vertical-align: middle;'>Here you can add a relation into a circle of relations."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>All relations in a circle have the same meaning (with the same or different languages)."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>Warning: All relations within a circle must be combined together."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>Warning: Position blocks are separate with a space (and each block separate with | ).");
		mql = new MQLDocumentation(true, "circle merge", "To merge two circles.", "circle merge \"r\" RL[4] RL[5];", "CI[0]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("thoughtId1", "The MAIN thought id", "string", true));
		mql.addParam(new MQLParam("thoughtId2", "The thought id", "string", true));
		functions.get("Circle").add(mql);
		mql = new MQLDocumentation(true, "circle exist", "To check if a link already existe into a circle", "circle exist \"r\" [R1]", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("relationIdToFind", "The relation id to find", "string", true));
		functions.get("Circle").add(mql);
		mql = new MQLDocumentation(true, "circle contains", "To check if a circle contains a relation", "circle contains \"r\" [R1] [R2]", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		mql.addParam(new MQLParam("relationIdToFind", "The relation id to find", "string", true));
		functions.get("Circle").add(mql);
		mql = new MQLDocumentation(true, "circle show", "To show all relations from a circle", "circle show \"r\" [R1]", "{<br>  \"en RL[4z] RL[50]\": \"1 2 3 3\",<br>  \"en RL[50] RL[4z]\": \"1 2 3|4\"<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Circle").add(mql);
		mql = new MQLDocumentation(true, "circle ids", "To show all relations ids from a circle", "circle ids \"r\" [R1]", "[<br>  \"RL[4z]\",<br>  \"RL[50]\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("lang", "The prefered language (ex: fr|en)", "string", false));
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Circle").add(mql);
		mql = new MQLDocumentation(true, "circle id", "To get a relation id from a circle", "circle id \"r\" fr [R1]", "RL[4z]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("lang", "The prefered language (ex: fr|en)", "string", true));
		mql.addParam(new MQLParam("relationId", "The relation id", "string", true));
		functions.get("Circle").add(mql);
		mql = new MQLDocumentation(true, "circle delete", "To delete all links by relation from circle", "circle delete \"r\" [R1];", "The relation is out of the circle.", "circle show \"r\" [R2]", "{}", "circle show \"r\" [R1]", "{}", false, "");
		mql.addParam(new MQLParam("level", "The level (ex: th|r)", "string", true));
		mql.addParam(new MQLParam("relationIdToDelete", "The relation id to delete", "string", true));
		functions.get("Circle").add(mql);
		
		functions.put("Concentration", new Vector<MQLDocumentation>());
		page_description.put("Concentration", "<img src='images/p.png' style='vertical-align: middle;'>All research is done by a degree of concentration."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>If the concentration is large, the brain will do extensive research."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>If the concentration is low, the brain will search quickly.");
		mql = new MQLDocumentation(true, "concentration show", "To show all the concentrations depth", "concentration show", "{<br>  \"C[deep-search]\": \"10\",<br>  \"C[symbol]\": \"200\",<br>  \"C[relation]\": \"200\",<br>  \"C[language]\": \"25\",<br>  \"C[user]\": \"25\",<br>  \"C[emotion]\": \"25\",<br>  \"C[process-tab]\": \"25\"<br>}", null, null, null, null, false, "");
		functions.get("Concentration").add(mql);
		mql = new MQLDocumentation(true, "concentration set depth", "To set the concentration depth", "concentration set depth C[symbol] 200;", "Concentration depth saved with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The concentration key", "string", true));
		mql.addParam(new MQLParam("depth", "The depth value", "integer > 0", true));
		functions.get("Concentration").add(mql);
		mql = new MQLDocumentation(true, "concentration depth", "To show the concentration depth", "concentration depth C[symbol]", "200", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The concentration key", "string", true));
		functions.get("Concentration").add(mql);
		
		page_group.put("Sequence", "NoSQL Database");
		
		functions.put("Sequence", new Vector<MQLDocumentation>());
		page_description.put("Sequence", "<img src='images/p.png' style='vertical-align: middle;'>You can manage sequences (base 26) as a standard database.");
		mql = new MQLDocumentation(true, "sequence show", "To show all sequences", "sequence show", "{<br>  \"relation-O\": \"-1\",<br>  \"thought\": \"89\",<br>  \"relation-I\": \"5\",<br>  \"relation-L\": \"54\",<br>  \"relation-U\": \"n\",<br>  \"relation-G\": \"w\",<br>  \"circle\": \"0\",<br>  \"relation-R\": \"3\",<br>  \"relation-A\": \"7\",<br>  \"relation-D\": \"1\",<br>  \"relation-S\": \"1\"<br>}", null, null, null, null, false, "");
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence get current", "To get the current sequence value", "sequence get current \"thought\"", "6z", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence add", "To add a new sequence", "sequence add \"test\" 0;", "Sequence added with successful.", null, null, null, null, false, "sequence create|insert|add");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "integer", true));
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence exist", "To check if a sequence exist", "sequence exist \"test\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence increment", "To increment a sequence value+1", "sequence increment \"test\";", "1", "sequence increment \"test\"", "2", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence update", "To update a sequence", "sequence update \"test\" 67;", "Sequence updated with successful.", "sequence get current \"test\"", "67", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "integer", true));
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence generate update", "To generate an updated sequence request", "sequence generate update \"test\";", "sequence update \"test\" \"67\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Sequence").add(mql);
		mql = new MQLDocumentation(true, "sequence remove", "To remove a sequence", "sequence remove \"test\";", "Sequence removed with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Sequence").add(mql);
		
		functions.put("Node", new Vector<MQLDocumentation>());
		page_description.put("Node", "<img src='images/p.png' style='vertical-align: middle;'>The nodes are stored in the storage structure in JSON."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>You can change them at any time."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>Beware, however, of data corruptions.");
		mql = new MQLDocumentation(true, "node create", "To create a new empty node", "node create \"keyId\";", "Node keyId created with successful.", null, null, null, null, false, "node create|insert|add");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node exist", "To check if a node already exist", "node exist \"keyId\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node delete", "To remove a node", "node delete \"keyId\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node iobject", "To insert an element into an object in a node", 
				"node iobject \"keyId\" / a 5 NUM", "Inserted with successful.", "node iobject \"keyId\" / tab \"[]\" ARRAY", "Inserted with successful.", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("fieldname", "The fieldname", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node show", "To show a specific node", "node show \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"tab\": [],<br>  \"key\": \"keyId\"<br>}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("table", "The table", "string", false));
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node count", "To count element in a node", "node count \"keyId\" /", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node fields", "To get all fields into an object in a node", "node fields \"keyId\" /", "[<br>  \"a\",<br>  \"tab\",<br>  \"key\"<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node iarray", "To insert an element into an array in a node", "node iarray \"keyId\" /tab test STR;", "Inserted with successful.", "node iarray \"keyId\" /tab test2 STR", "Inserted with successful.", "node show \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"tab\": [<br>    \"test\",<br>    \"test2\"<br>  ],<br>  \"key\": \"keyId\"<br>}", false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node iarray", "To insert an element into an array in a node", "node iarray \"keyId\" /tab 0 test STR;", "Inserted with successful.", "node iarray \"keyId\" /tab test2 STR", "Inserted with successful.", "node show \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"tab\": [<br>    \"test\",<br>    \"test\",<br>    \"test2\",<br>    \"test2\"<br>  ],<br>  \"key\": \"keyId\"<br>}", false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("index", "The index position", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node is array", "To check if an element is an array in a node", "node is array \"keyId\" /tab", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node is object", "To check if an element is an object in a node", "node is object \"keyId\" /", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node select", "To select an element in a node", "node select \"keyId\" /a", "5.0", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node uarray", "To update an element into an array in a node", "node uarray \"keyId\" /tab 1 test2 STR;", "Updated with successful.", "node show \"keyId\"", "{<br>  \"a\": 5.0,<br>  \"tab\": [<br>    \"test\",<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ],<br>  \"key\": \"keyId\"<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("index", "The index", "integer", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node uobject", "To update an element into an object in a node", "node uobject \"keyId\" / a 8 NUM;", "Updated with successful.", "node show \"keyId\"", "{<br>  \"a\": 8.0,<br>  \"tab\": [<br>    \"test\",<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ],<br>  \"key\": \"keyId\"<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("fieldname", "The fieldname", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		mql.addParam(new MQLParam("type", "The type (NUM|INT|STR|BOOL|BOOL2|ARRAY|OBJ)", "string", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node darray", "To delete an element from an array in a node", "node darray \"keyId\" /tab 0;", "Deleted with successful.", "node show \"keyId\"", "{<br>  \"a\": 8.0,<br>  \"tab\": [<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ],<br>  \"key\": \"keyId\"<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("index", "The index", "integer", true));
		functions.get("Node").add(mql);
		mql = new MQLDocumentation(true, "node dobject", "To delete an element from an object in a node", "node dobject \"keyId\" / a;", "Deleted with successful.", "node show \"keyId\"", "{<br>  \"tab\": [<br>    \"test2\",<br>    \"test2\",<br>    \"test2\"<br>  ],<br>  \"key\": \"keyId\"<br>}", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("jPath", "The JSON path", "string", true));
		mql.addParam(new MQLParam("fieldname", "The fieldname", "string", true));
		functions.get("Node").add(mql);
		
		page_group.put("Metric", "Server");
		
		functions.put("Metric", new Vector<MQLDocumentation>());
		page_description.put("Metric", "<img src='images/p.png' style='vertical-align: middle;'>You have a lot of metric functions.");
		mql = new MQLDocumentation(true, "metric sessions", "To show all metrics about sessions", "metric sessions", "Cmd on 31 days (total: 16): [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,16]<br>Session on 31 days (total: 4): [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4]<br>Open sessions: 2/20<br>  - id: 1, user: ai, nbExecution=2, life: 142/600s, used: 142, maxUsable: 600<br>  - id: 3, user: admin, nbExecution=8, life: 0/600s, used: 0, maxUsable: 600", null, null, null, null, false, "");
		functions.get("Metric").add(mql);
		mql = new MQLDocumentation(true, "metric system", "To show all stats about system", "metric system", "AI: Lisa Payet<br>ID: NNeWO1VGLmeI<br>MentDB: v_alpha-0.3.1<br>Author: Jimmitry PAYET, Programmer Analyst<br>Contact: jim@mentdb.org<br>Java: v_1.8.0_25, Oracle Corporation<br>Mac OS X: v_10.10.5, x86_64, nb_proc: 4<br>Cpu jvm: 0.0%, Cpu sys: 76.1%, Used jvm mem: 255/757/1820M, Used mem: 7490/8192M, Used swap mem: 2456/4096M<br>Cpu on 24 hours: [0.0]<br>Mem on 24 hours: [85]<br>File system roots:<br>  - [/] 81%, usedSpace: 195027M, freeSpace: 43220M, totalSpace: 238248M<br>", null, null, null, null, false, "");
		functions.get("Metric").add(mql);
		functions.get("Metric").add(new MQLDocumentation(true, "metric current cpu jvm", "To show current jvm cpu value", "metric current cpu jvm", "0.0", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric current cpu system", "To show current system cpu value", "metric current cpu system", "9.0", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric current free mem", "To show current free memory value", "metric current free mem", "503", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric current free swap mem", "To show current free swap memory value", "metric current free swap mem", "1093", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric current mem jvm", "To show current jvm memory status", "metric current mem jvm", "32/123/1820", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric current used mem", "To show current usable memory value", "metric current used mem", "7690", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric current used swap mem", "To show current used swap memory value", "metric current used swap mem", "1978", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric date", "To show all dates at the last 31 days", "metric date", "[2016-02-27, 2016-02-28, 2016-02-29, 2016-03-01, 2016-03-02, 2016-03-03, 2016-03-04, 2016-03-05, 2016-03-06, 2016-03-07, 2016-03-08, 2016-03-09, 2016-03-10, 2016-03-11, 2016-03-12, 2016-03-13, 2016-03-14, 2016-03-15, 2016-03-16, 2016-03-17, 2016-03-18, 2016-03-19, 2016-03-20, 2016-03-21, 2016-03-22, 2016-03-23, 2016-03-24, 2016-03-25, 2016-03-26, 2016-03-27, 2016-03-28]", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric file system roots", "To show statistics to all system roots", "metric file system roots", "[{\"usedSpace\":\"148708\",\"freeSpace\":\"89539\",\"absolutePath\":\"\\/\",\"totalSpace\":\"238248\"}]", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric java vendor", "To show the Java vendor", "metric java vendor", "Oracle Corporation", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric java version", "To show the Java version", "metric java version", "1.8.0_25", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric system architecture", "To show the architecture of the operating system", "metric system architecture", "x86_64", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric system name", "To show the name of the operating system", "metric system name", "Mac OS X", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric system nb processor", "To show the processor number of the operating system", "metric system nb processor", "4", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric system version", "To show the version of the operating system", "metric system version", "10.10.5", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric total mem", "To show current total memory value", "metric total mem", "8192", null, null, null, null, false, ""));
		functions.get("Metric").add(new MQLDocumentation(true, "metric total swap mem", "To show current total swap memory value", "metric total swap mem", "3072", null, null, null, null, false, ""));

		functions.put("Environment variable", new Vector<MQLDocumentation>());
		page_description.put("Environment variable", "<img src='images/p.png' style='vertical-align: middle;'>You can save data into variables in your session.");
		mql = new MQLDocumentation(true, "env set var", "To set a variable into the environment", "env set var \"[var1]\" 15;", "15", "-> \"[var2]\" 16", "16", null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "concat_var", "Concat all parameters and save the result into the environment.", "concat_var \"[var1]\" \"a\" \"r\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "String", true));
		mql.addParam(new MQLParam("str1", "The string", "String", true));
		mql.addParam(new MQLParam("strN", "The string", "String", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "env incr var", "To increment a variable into the environment", "env incr var \"[var1]\" 1;", "16", "++ \"[var1]\"", "17", null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "string", true));
		mql.addParam(new MQLParam("increment", "The increment", "integer", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "env decr var", "To decrement a variable into the environment", "env decr var \"[var1]\" 1;", "16", "-- \"[var1]\"", "15", null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "string", true));
		mql.addParam(new MQLParam("decrement", "The decrement", "integer", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "env get var", "To get a variable from into the environment", "env get var \"[var1]\"", "15", null, null, null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "string", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "env exist var", "To check if a variable exist into the environment", "env exist var \"[var1]\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "string", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "env del var", "To delete a variable from the environment", "env del var \"[var1]\";", "Variable deleted with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("varName", "The variable name", "string", true));
		functions.get("Environment variable").add(mql);
		mql = new MQLDocumentation(true, "env show", "To show the environment variable", "env show", "{<br>  \"[err]\": \"1: your messffage ...\",<br>  \"[var2]\": \"16\",<br>  \"[i]\": \"45\",<br>  \"[ERROR]\": \"1: your messffage ...\"<br>}", null, null, null, null, false, "");
		functions.get("Environment variable").add(mql);

		functions.put("Session", new Vector<MQLDocumentation>());
		page_description.put("Session", "<img src='images/p.png' style='vertical-align: middle;'>You have control functions of web socket session.<br>"+
				"<img src='images/p.png' style='vertical-align: middle;'>You have control functions of database session.");
		functions.get("Session").add(new MQLDocumentation("WS Session", true, "chat", "To change the mode to CHAT.", "@chat", "CHAT mode activated (do not use directly with the editor, only in your application with the web socket driver).", null, null, null, null, true, ""));
		functions.get("Session").add(new MQLDocumentation(true, "cmdid", "To show the number of web socket command executed by the current user", "@cmdid", "5", null, null, null, null, true, ""));
		functions.get("Session").add(new MQLDocumentation(true, "count sessions", "To count all open web socket sessions", "@count sessions", "1", null, null, null, null, true, ""));
		functions.get("Session").add(new MQLDocumentation(true, "sessions", "To show open web socket sessions", "@sessions", "[<br>  {<br>    \"maxUsable\": 600,<br>    \"nbExecution\": 5,<br>    \"id\": 2,<br>    \"used\": 0,<br>    \"user\": \"admin\",<br>    \"life\": \"0/600s\"<br>  }<br>]", null, null, null, null, true, ""));
		functions.get("Session").add(new MQLDocumentation(true, "sid", "To show current web socket session id", "@sid", "3", null, null, null, null, true, ""));
		
		functions.get("Session").add(new MQLDocumentation("DB Session", true, "bye", "To close the session (or exit or quit)", "bye", "Session close with successful.", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "cmdid", "To show the number of commands executed by the current user", "cmdid", "25", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "count sessions", "To count all open sessions", "count sessions", "3", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "exit", "To close the session (or bye or quit)", "exit", "Session close with successful.", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "help", "To show this help menu", null, null, null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "quit", "To close the session (or exit or quit)", "quit", "Session close with successful.", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "sessions", "To show all open sessions", "sessions", "[<br>  {<br>    \"maxUsable\": 600,<br>    \"nbExecution\": 2,<br>    \"used\": 0,<br>    \"id\": 4,<br>    \"user\": \"admin\",<br>    \"life\": \"0/600s\"<br>  }<br>]", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "sid", "To show current session id", "sid", "5", null, null, null, null, false, ""));
		functions.get("Session").add(new MQLDocumentation(true, "pid", "To show current Java PID", "pid", "5423", null, null, null, null, true, ""));
		functions.get("Session").add(new MQLDocumentation(true, "who", "To show the current user", "who", "admin", null, null, null, null, false, ""));
		
		functions.put("Parameter", new Vector<MQLDocumentation>());
		page_description.put("Parameter", "<img src='images/p.png' style='vertical-align: middle;'>You have an ultra-fast global parameter engine.");
		mql = new MQLDocumentation(true, "parameter add", "To add a new parameter", "parameter add test value;", "Parameter added with successful.", null, null, null, null, false, "parameter create|insert|add");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "integer", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter merge", "To merge a parameter", "parameter merge test value;", "Parameter merged with successful.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "integer", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter exist", "To check if a parameter exist", "parameter exist test", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter get value", "To get the parameter value", "parameter get value test", "value", "get_param test", "value", null, null, false, "get_param");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter remove", "To remove a parameter", "parameter remove test;", "Sorry, the parameter test is locked.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter show", "To show all parameters", "parameter show", "{<br>  \"C[basic-search]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"10\"<br>  },<br>  \"C[symbol]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"200\"<br>  },<br>  \"test\": {<br>    \"locked\": \"1\",<br>    \"value\": \"value\"<br>  },<br>  \"C[relation]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"200\"<br>  },<br>  \"C[language]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"25\"<br>  },<br>  \"C[user]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"25\"<br>  },<br>  \"C[emotion]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"25\"<br>  },<br>  \"C[process-tab]\": {<br>    \"locked\": \"1\",<br>    \"value\": \"25\"<br>  }<br>}", null, null, null, null, false, "");
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter update", "To update a parameter", "parameter update test 67;", "Parameter updated with successful.", "parameter get value test", "67", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "integer", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter lock_if_null", "To lock a parameter if the parameter is null. Return 0 if already locked. Return 1 if the parameter has been locked.", "parameter lock_if_null test 67;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("value", "The value", "integer", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter generate update", "To generate an update parameter update request", "parameter generate update test;", "parameter update \"test\" \"67\";", "parameter remove test", "Parameter removed with successful.", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Parameter").add(mql);
		mql = new MQLDocumentation(true, "parameter generate merge", "To generate a merge request", "parameter generate merge test;", "parameter merge \"test\" \"67\" false;", "parameter remove test", "Parameter removed with successful.", null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Parameter").add(mql);
		
		functions.put("Log", new Vector<MQLDocumentation>());
		page_description.put("Log", "<img src='images/p.png' style='vertical-align: middle;'>You have a log engine in rolling.");
		mql = new MQLDocumentation(true, "log trace", "To write a message into the log system", "log trace test;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("text", "The text", "string", true));
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log write", "To write a message into MySQL", "log write \"My message ...\" OK \"cmd\" 425;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("msg", "The message to write", "string", true));
		mql.addParam(new MQLParam("status", "The status", "string", true));
		mql.addParam(new MQLParam("clientKey", "The client key", "string", true));
		mql.addParam(new MQLParam("clientValue", "The client value", "string", true));
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log show", "To show last write text into the log system", "log show 5;", "2017-06-05 10:35:42.375+0400: 43<br>2017-06-05 10:35:42.375+0400: 44<br>2017-06-05 10:35:42.380+0400: 1: your messffage ...<br>2017-06-05 10:35:42.896+0400: test<br>", "log show", "2017-06-05 10:35:42.375+0400: 43<br>2017-06-05 10:35:42.375+0400: 44<br>2017-06-05 10:35:42.380+0400: 1: your messffage ...<br>2017-06-05 10:35:42.896+0400: test<br>", null, null, false, "");
		mql.addParam(new MQLParam("nbLine", "The number of line", "integer", false));
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log current id", "To show the current log id", "log current id", "1", null, null, null, null, false, "");
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log retention day", "To show the log retention day", "log retention day", "50", null, null, null, null, false, "");
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log remove", "To remove logs with retention day", "log remove;", "1", null, null, null, null, false, "");
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log archive size", "To show the size for one archive file", "log archive size", "30", null, null, null, null, false, "");
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log archive path", "Get the path to save archives", "log archive path", "archives/logs", null, null, null, null, false, "");
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log reset", "To reset the log system", "log reset \"xxx\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("code", "The code to reset all tables (18061980)", "integer", false));
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log search", "To search log", "log search \"ko\" \n" + 
				"	null \n" + 
				"	null \n" + 
				"	null \n" + 
				"	null \n" + 
				"	(date datedifft (concat (date sysdate) \" 00:00:00\") \"DAY\" 0)\n" + 
				"	(concat (date sysdate) \" 23:59:59\") \n" + 
				"	ASC 1 5;", "{\"column_types\":[\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"LONG\"],\"data\":[],\"columns\":[\"dtInsert\",\"status\",\"script\",\"parent_pid\",\"pid\",\"c_key\",\"c_val\",\"msg\",\"id\"],\"title\":\"log \"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("status", "The status (ok|ko)", "string", true));
		mql.addParam(new MQLParam("script", "The script (can be null)", "string", true));
		mql.addParam(new MQLParam("c_key", "The client key", "string", true));
		mql.addParam(new MQLParam("c_value", "The client value", "string", true));
		mql.addParam(new MQLParam("msgFilter", "The message filter", "string", true));
		mql.addParam(new MQLParam("dtMin", "The min date", "string", true));
		mql.addParam(new MQLParam("dtMax", "The max date", "string", true));
		mql.addParam(new MQLParam("dtOrder", "The order (ASC|DESC)", "string", true));
		mql.addParam(new MQLParam("page", "The page", "number", true));
		mql.addParam(new MQLParam("nbByPage", "The number of line by page", "number", true));
		functions.get("Log").add(mql);
		mql = new MQLDocumentation(true, "log show_time", "To show log throught the time", "log show_time 17", "{\"column_types\":[\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"STRING\",\"LONG\"],\"data\":[{\"msg\":\"[---- Begin script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-01-09 15:44:40\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"887\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- End script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-01-09 15:44:40\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"888\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- Begin script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-01-22 07:12:22\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"1307\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- End script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-01-22 07:12:22\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"1308\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- Begin script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-02-13 16:08:54\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"2589\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- End script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-02-13 16:08:54\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"2590\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- Begin script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-02-20 10:15:29\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"2781\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- End script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-02-20 10:15:29\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"2782\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- Begin script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-02-23 13:51:35\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"3029\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"},{\"msg\":\"[---- End script ----]\",\"c_val\":\"\",\"dtInsert\":\"2018-02-23 13:51:35\",\"parent_pid\":\"\",\"pid\":\"17\",\"id\":\"3030\",\"c_key\":\"\",\"script\":\"boot.execute.on.start.post\",\"status\":\"ok\"}],\"columns\":[\"dtInsert\",\"status\",\"script\",\"parent_pid\",\"pid\",\"c_key\",\"c_val\",\"msg\",\"id\"],\"title\":\"PID <17>\"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("nodename", "The node name", "string", false));
		mql.addParam(new MQLParam("pid", "The process id", "string", true));
		functions.get("Log").add(mql);
		
		functions.put("OS", new Vector<MQLDocumentation>());
		page_description.put("OS", "<img src='images/p.png' style='vertical-align: middle;'>Here you can call the OS.");
		functions.get("OS").add(new MQLDocumentation(true, "os version", "To get the OS version", "os version", "10.12.6", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os arch", "To get the OS architecture", "os arch", "x86_64", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os name", "To get the OS name", "os name", "Mac OS X", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os type", "To get the OS type", "os type", "Mac OS X", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os hostname", "To get the OS hostname", "os hostname", "MacBook-Pro-de-Jimmitry.local", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os user_timezone", "To get the OS user timezone", "os user_timezone", "Indian/Reunion", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os user_name", "To get the OS user name", "os user_name", "jimmitry", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os user_lang", "To get the OS user lang", "os user_lang", "fr", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os user_home", "To get the OS user home", "os user_home", "/Users/jimmitry", null, null, null, null, false, ""));
		functions.get("OS").add(new MQLDocumentation(true, "os user_dir", "To get the OS user dir", "os user_dir", "/Users/jimmitry/Documents/jpayet/INNOV-AI/MENTDB/MentDB_Server", null, null, null, null, false, ""));
		mql = new MQLDocumentation(true, "os execute", "To execute command OS", "os execute \"[\\\"uname\\\", \\\"-a\\\"]\" 1000;", "Darwin MacBook-Pro-de-Jimmitry.local 16.7.0 Darwin Kernel Version 16.7.0: Thu Jun 15 17:36:27 PDT 2017; root:xnu-3789.70.16~2/RELEASE_X86_64 x86_64", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonCmdArray", "The json command array", "string", true));
		mql.addParam(new MQLParam("minWait", "The min wait to get the result", "number", true));
		functions.get("OS").add(mql);
		
		functions.put("Server", new Vector<MQLDocumentation>());
		page_description.put("Server", "<img src='images/p.png' style='vertical-align: middle;'>You have some functions to control the execution of the server."+
				"<br><img src='images/p.png' style='vertical-align: middle;'>To learn more about the version of the server, and see the AI ID card.");
		functions.get("Server").add(new MQLDocumentation(true, "id", "To get the id of the server", "id", "WLMJ4JpIGAYY", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "ai firstname", "To show the AI firstname", "ai firstname", "lisa", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "ai lastname", "To show the AI lastname", "ai lastname", "payet", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "name", "To show the AI name", "name", "lisa payet", null, null, null, null, false, "ai name"));
		mql = new MQLDocumentation(true, "shutdown", "To stop the server (or stop)", "shutdown \"ZVQNt8PJDt1h_\"", "MentDB: Shutdown with successful.<br>Bye.<br><br>Disconnected.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("identificationCode", "The identification code", "string", true));
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "stop", "To stop the server (or shutdown)", "stop \"ZVQNt8PJDt1h_\"", "MentDB: Shutdown with successful.<br>Bye.<br><br>Disconnected.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("identificationCode", "The identification code", "string", true));
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "wait", "To force the server to wait", "wait \"10000\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("time", "The time in millisecond", "string", true));
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "maintenance get", "To get the status maintenances", "maintenance get;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("time", "The time in millisecond", "string", true));
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "maintenance set", "To set the status maintenances", "maintenance set 0 0 0 0 0;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("time", "The MQL door", "boolean", true));
		mql.addParam(new MQLParam("time", "The WS door", "boolean", true));
		mql.addParam(new MQLParam("time", "The WEB door", "boolean", true));
		mql.addParam(new MQLParam("time", "The JOB door", "boolean", true));
		mql.addParam(new MQLParam("time", "The STACK door", "boolean", true));
		functions.get("Server").add(mql);
		functions.get("Server").add(new MQLDocumentation(true, "version", "To show the version of the server", "version", Start.version, null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "exceeded sessions", "To show the number of exceeded sessions", "exceeded sessions", "0", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "reset exceeded sessions", "To reset the number of exceeded sessions", "reset exceeded sessions;", "Exceeded sessions has been reset successfully.", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "exceeded sessions", "To show the number of exceeded web socket sessions", "@exceeded sessions", "0", null, null, null, null, true, ""));
		functions.get("Server").add(new MQLDocumentation(true, "reset exceeded sessions", "To reset the number of exceeded web socket sessions", "@reset exceeded sessions;", "Exceeded sessions has been reset successfully.", null, null, null, null, true, ""));
		functions.get("Server").add(new MQLDocumentation(true, "src count", "To count the number of Java lines", "src count", "35679", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "function count", "To count the number of MQL functions", "function count", "690", null, null, null, null, false, ""));
		mql = new MQLDocumentation(true, "refresh admin", "To refresh the tree admin in the editor", "refresh admin", "In editor ...", null, null, null, null, false, "");
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "refresh devel", "To refresh the tree devel in the editor", "refresh devel \"as400\"", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("filter", "The string to search", "string", true));
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "refresh config", "To refresh the tree config in the editor", "refresh config", "In editor ...", null, null, null, null, false, "");
		functions.get("Server").add(mql);
		functions.get("Server").add(new MQLDocumentation(true, "in editor", "To refresh the tree in the editor", "in editor {file load \"home/file.txt\"};", "...", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "in clipboard", "To copy a string into the clipboard", "in clipboard {script generate update \"division\"};", "...", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "in out_editor", "Show data into the editor", "in out_editor {\n	sql show data \"demo_db_mysql\" \"SELECT * FROM products LIMIT 0, 200\";\n}", "...", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "in activity", "Show the line process", "in activity {...};", "...", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "in scatter", "Show the scatter for AP", "in scatter {...};", "...", null, null, null, null, false, ""));
		functions.get("Server").add(new MQLDocumentation(true, "config reload", "To the config file", "config reload;", "1", null, null, null, null, false, ""));
		mql = new MQLDocumentation(true, "kill", "To kill a session", "kill 23", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("sessionId", "The session id", "string", true));
		functions.get("Server").add(mql);
		mql = new MQLDocumentation(true, "kill_process", "To kill a process from the stack", "kill_process 23a;", "The process will be stoped at the next MQL command ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("pid", "The process id", "string", true));
		functions.get("Server").add(mql);
		
		if ((new File("MentDB_Editor_"+(Start.version.substring(0,  1))+File.separator+"tools"+File.separator+"mql.json")).exists()) {
		
			//Write the MQL file
			try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		              new FileOutputStream("MentDB_Editor_"+(Start.version.substring(0,  1))+File.separator+"tools"+File.separator+"mql.json"), "utf-8"))) {
				
				JSONArray json = new JSONArray();
	
				for (Entry<String, Vector<MQLDocumentation>> e : MQLDocumentation.functions.entrySet()) {
					
					for(int i=0;i<e.getValue().size();i++) {
						
						String fx = "", fxAll = "", fxDesc = "";
											
						//Get the current MQLDocumentation object
						
						MQLDocumentation currentFunction = e.getValue().get(i);
						
						fx+= (currentFunction.webSocket?"@":"")+currentFunction.functionId;
						fxDesc += "Description: "+currentFunction.description+"<hr>";
						if (currentFunction.synonymous!= null && !currentFunction.synonymous.equals("")) fxDesc += "Synonymous: "+currentFunction.synonymous+"<hr>";
						for(int r=0;r<currentFunction.parameters.size();r++) {
							
							fxAll += " \"<b>"+currentFunction.parameters.get(r).name+"</b>\"";
							fxDesc += "<b>"+currentFunction.parameters.get(r).name+"</b>: "+currentFunction.parameters.get(r).description+" - "+currentFunction.parameters.get(r).type+" - required="+currentFunction.parameters.get(r).required+"<br>";
	
						}
						if (currentFunction.parameters.size()>0) fxDesc += "<hr>";
						if (currentFunction.example1!=null) fxDesc += "Example 1<br><pre style='color:#2A7CD9'>"+currentFunction.example1.replace("<br>", "\n")+"</pre>";
						else fxDesc += "Example 1<br>?????<br>";
						fxDesc += "Result 1<br><pre style='color:#2AB77C'>"+currentFunction.result1+"</pre>";
						if (currentFunction.example2!= null && !currentFunction.example2.equals("")) fxDesc += "Example 2<br><pre style='color:#2A7CD9'>"+currentFunction.example2+"</pre>";
						if (currentFunction.result2!= null && !currentFunction.result2.equals("")) fxDesc += "Result 2<br><pre style='color:#2AB77C'>"+currentFunction.result2+"</pre><br>";
						if (currentFunction.example3!= null && !currentFunction.example3.equals("")) fxDesc += "Example 3<br><pre style='color:#2A7CD9'>"+currentFunction.example3+"</pre>";
						if (currentFunction.result3!= null && !currentFunction.result3.equals("")) fxDesc += "Result 3<br><pre style='color:#2AB77C'>"+currentFunction.result3+"</pre>";
						
						json.add(fx);
						if (currentFunction.example1!=null) json.add(currentFunction.example1.replace("<br>", "\n"));
						else json.add(fx+fxAll);
						json.add("<div style='white-space: nowrap;'><span style='color:#995500'><b>"+fx+"</b></span>"+fxAll+"<hr>"+fxDesc+"</div>");
						json.add(e.getKey());
						
						if (!currentFunction.synonymous.equals("")) {
							
							fx = "";
							fxAll = "";
							fxDesc = "";
							
							//Get the current synonymous MQLDocumentation object
							
							fx+= (currentFunction.webSocket?"@":"")+currentFunction.synonymous;
							fxDesc += "Description: "+currentFunction.description+"<hr>";
							if (currentFunction.synonymous!= null && !currentFunction.synonymous.equals("")) fxDesc += "Synonymous: "+currentFunction.synonymous+"<hr>";
							
							for(int r=0;r<currentFunction.parameters.size();r++) {
								
								fxAll += " \"<b>"+currentFunction.parameters.get(r).name+"</b>\"";
								fxDesc += "<b>"+currentFunction.parameters.get(r).name+"</b>: "+currentFunction.parameters.get(r).description+" - "+currentFunction.parameters.get(r).type+" - required="+currentFunction.parameters.get(r).required+"<br>";
	
							}
							if (currentFunction.parameters.size()>0) fxDesc += "<hr>";
							if (currentFunction.example1!=null) fxDesc += "Example 1<br>"+currentFunction.example1.replace("<br>", "\n")+"<br>";
							else fxDesc += "Example 1<br>?????<br>";
							fxDesc += "Result 1<br>"+currentFunction.result1+"<br><br>";
							if (currentFunction.example2!= null && !currentFunction.example2.equals("")) fxDesc += "Example 2<br>"+currentFunction.example2+"<br>";
							if (currentFunction.result2!= null && !currentFunction.result2.equals("")) fxDesc += "Result 2<br>"+currentFunction.result2+"<br><br>";
							if (currentFunction.example3!= null && !currentFunction.example3.equals("")) fxDesc += "Example 3<br>"+currentFunction.example3+"<br>";
							if (currentFunction.result3!= null && !currentFunction.result3.equals("")) fxDesc += "Result 3<br>"+currentFunction.result3;
							
							json.add(fx);
							if (currentFunction.example1!=null) json.add(currentFunction.example1.replace("<br>", "\n"));
							else json.add(fx+fxAll);
							json.add("<div style='white-space: nowrap;'><span style='color:#995500'><b>"+fx+"</b></span>"+fxAll+"<hr>"+fxDesc+"</div>");
							json.add(e.getKey());
							
						}
						
					}
				}
	
				writer.write(json.toJSONString());
				
				
			}
			
		}
		
		Misc.create("docs"+File.separator+"mql.html", Misc.load("docs"+File.separator+"mql_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[PAGE_TITLE]", "MQL").replace("[MQL]", MQLDocumentation.menu_to_html()).replace("[MQL_VERSION]", Start.version).replace("[MQL_NB_FUNCTIONS]", ""+MQLDocumentation.nbFunction));
		Misc.create("docs"+File.separator+"index.html", Misc.load("docs"+File.separator+"index_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[MQL_VERSION]", Start.version));

		if ((new File("/Users/jimmitry/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions")).exists()) {
			Misc.create("/Users/jimmitry/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions/index.html", Misc.load("docs"+File.separator+"mentdb.org"+File.separator+"mql_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[MQL]", MQLDocumentation.menu_to_html()).replace("[MQL_VERSION]", Start.version).replace("[MQL_NB_FUNCTIONS]", ""+MQLDocumentation.nbFunction));
		}
		if ((new File("/Users/jim/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions")).exists()) {
			Misc.create("/Users/jim/Dropbox/INNOV-AI/WebDocuments/mentdb.org/functions/index.html", Misc.load("docs"+File.separator+"mentdb.org"+File.separator+"mql_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[MQL]", MQLDocumentation.menu_to_html()).replace("[MQL_VERSION]", Start.version).replace("[MQL_NB_FUNCTIONS]", ""+MQLDocumentation.nbFunction));
		}
		
		Doc_required.load();Misc.create("docs"+File.separator+"required.html", Misc.load("docs"+File.separator+"doc_page_tmp.html").replace("[SERVER_YEAR]", Start.copyright).replace("[PAGE_TITLE]", ((JSONObject) Doc.pages.get("required")).get("title")+"").replace("[DOC]", Doc.to_html("required")).replace("[MQL_VERSION]", Start.version));
		
		mqlPage_to_html();
		
	}

}