<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" import="re.jpayet.mentdb.ext.app.AppManager,re.jpayet.mentdb.ext.log.Log" %><%
try {
	out.print(AppManager.execute(request, response, "http", "demo", "MENTDB", "web"));
} catch (Exception e) {
	System.out.println((""+e.getMessage()).replace("\n", "<br>"));
	Log.trace((""+e.getMessage()));
}
%>
