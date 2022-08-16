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

import java.util.LinkedHashMap;
import java.util.Vector;

import re.jpayet.mentdb.ext.dl.ImageNeuralNetworkManager;

public class Doc_Mentalese_Higth_Algo {
	
	public static void init(LinkedHashMap<String, Vector<MQLDocumentation>> functions,
			LinkedHashMap<String, String> page_description,
			MQLDocumentation mql,
			LinkedHashMap<String, String> ghost_functions,
			LinkedHashMap<String, String> page_group) {
		
		page_group.put("Predictive analysis", "Analytics");

		functions.put("Predictive analysis", new Vector<MQLDocumentation>());
		page_description.put("Predictive analysis", "<img src='images/p.png' style='vertical-align: middle;'>Here you can make your predictive analysis.");
		mql = new MQLDocumentation(true, "pa polynomial_curve_fit_get_coeff", "Train and get polynomial curve coeff from [x,y]", "pa polynomial_curve_fit_get_coeff \"[\n" + 
				"  [1.0, 2.0],\n" + 
				"  [1.0, 2.2],\n" + 
				"  [1.0, 2.5],\n" + 
				"  [1.2, 2.0],\n" + 
				"  [1.11, 0.9],\n" + 
				"  [5.0, 3.0],\n" + 
				"  [5.2, 3.08],\n" + 
				"  [5.1, 3.0],\n" + 
				"  [5.15, 2.9]\n" + 
				"]\" \"3\"", "\"[7.499085083330208,-7.6240029798174,2.4585392371279067,-0.22286556119637468]\"", null, null, null, null, false, "");
		mql.addParam(new MQLParam("data", "The json array of x,y", "string", true));
		mql.addParam(new MQLParam("degree", "The degree of the polynomial curve", "number", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa polynomial_curve_fit_eval", "Eval a polynomial curve for specific value", "pa polynomial_curve_fit_eval \"[7.499085083330208,-7.6240029798174,2.4585392371279067,-0.22286556119637468]\" \"2\"", "0.30231158263603675", null, null, null, null, false, "");
		mql.addParam(new MQLParam("coeff", "The json array coeff", "string", true));
		mql.addParam(new MQLParam("x", "The x to eval", "number", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa polynomial_curve_fit_eval_incr", "Eval a polynomial curve for a range", "pa polynomial_curve_fit_eval_incr \"[7.499085083330208,-7.6240029798174,2.4585392371279067,-0.22286556119637468]\" 1 5 0.1", "[[1.0,2.1107557794443395],[1.1,1.7908802205034604],[1.2000000000000002,1.5054663192661772],[1.3000000000000003,1.2531768823653149],[1.4000000000000004,1.032674716433693],[1.5000000000000004,0.8426226281041327],[1.6000000000000005,0.6816834240094574],[1.7000000000000006,0.5485199107824883],[1.8000000000000007,0.44179489505604863],[1.9000000000000008,0.36017118346295796],[2.000000000000001,0.30231158263603675],[2.100000000000001,0.2668788992081117],[2.200000000000001,0.25253593981199796],[2.300000000000001,0.2579455110805249],[2.4000000000000012,0.2817704196465085],[2.5000000000000013,0.322673472142772],[2.6000000000000014,0.37931747520213843],[2.7000000000000015,0.45036523545742746],[2.8000000000000016,0.5344795595414631],[2.9000000000000017,0.6303232540870649],[3.0000000000000018,0.7365591257270543],[3.100000000000002,0.8518499810942535],[3.200000000000002,0.97485862682149],[3.300000000000002,1.1042478695415756],[3.400000000000002,1.2386805158873448],[3.500000000000002,1.3768193724916014],[3.6000000000000023,1.5173272459871887],[3.7000000000000024,1.658866943006906],[3.8000000000000025,1.8001012701835943],[3.9000000000000026,1.9396930341500633],[4.000000000000003,2.0763050415391433],[4.100000000000002,2.2086000989836396],[4.200000000000002,2.335241013116396],[4.300000000000002,2.4548905905702254],[4.400000000000001,2.5662116379779425],[4.500000000000001,2.6678669619723765],[4.6000000000000005,2.7585193691863505],[4.7,2.836831666252677],[4.8,2.9014666598041945],[4.8999999999999995,2.9510871564737036],[4.999999999999999,2.984355962894039]]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("coeff", "The json array coeff", "string", true));
		mql.addParam(new MQLParam("min_x", "The min x to eval", "number", true));
		mql.addParam(new MQLParam("max_x", "The max x to eval", "number", true));
		mql.addParam(new MQLParam("increment", "The increment", "number", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa xy_scatter", "Show a scatter", "pa xy_scatter \"[\n" + 
				"  [1.0, 2.0],\n" + 
				"  [1.0, 2.2],\n" + 
				"  [1.0, 2.5],\n" + 
				"  [1.2, 2.0],\n" + 
				"  [1.11, 0.9],\n" + 
				"  [5.0, 3.0],\n" + 
				"  [5.2, 3.08],\n" + 
				"  [5.1, 3.0],\n" + 
				"  [5.15, 2.9]\n" + 
				"]\" \"x, y\"", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "The json array of x,y", "string", true));
		mql.addParam(new MQLParam("title", "The title", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa xy_scatter", "Show a predictive analysis scatter", "pa xy_scatter \"demo_cm_mysql\" \"id\" \"quantity\" \"select * from products limit 0, 500\"", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("fieldX", "The field X", "string", true));
		mql.addParam(new MQLParam("fieldY", "The field X", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl load", "Load a linear regression from the database", "pa rl load \"reg1\" \"demo_cm_mysql\" \"id\" \"quantity\" \"select * from products limit 0, 500\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("fieldX", "The field X", "string", true));
		mql.addParam(new MQLParam("fieldY", "The field X", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl load_from_json", "Load a linear regression from a JSON array of doubles", "pa rl load_from_json \"reg1\" \"[\n" + 
				"  [1.0, 2.0],\n" + 
				"  [2.0, 3.0],\n" + 
				"  [3.0, 4.0],\n" + 
				"  [4.0, 5.0],\n" + 
				"  [5.0, 6.0]\n" + 
				"]\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("jsonArray", "The json array", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl load_empty", "Load an empty linear regression", "pa rl load_empty \"reg1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl exist", "Check if a regression already exist", "pa rl exist \"reg1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl show", "Show all regressions", "pa rl show", "[<br>  \"reg1\"<br>]", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl add_data", "Add data to a regression", "pa rl add_data \"reg1\" 5 56;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("x", "The x", "number", true));
		mql.addParam(new MQLParam("y", "The y", "number", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl slope", "Get the slope (y = intercept + slope * x)", "pa rl slope \"reg1\"", "7.25", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl intercept", "Get the intercept (y = intercept + slope * x)", "pa rl intercept \"reg1\"", "-11.5", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl predict", "Make a prediction", "pa rl predict \"reg1\" 12", "75.5", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("x", "The x", "number", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl intercept_std_err", "Returns the standard error of the intercept estimate, usually denoted s(b0).", "pa rl intercept_std_err \"reg1\"", "19.764235376052373", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl mean_square_error", "Returns the sum of squared errors divided by the degrees of freedom, usually abbreviated MSE.", "pa rl mean_square_error \"reg1\"", "390.625", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl count", "Get the number of couple", "pa rl count \"reg1\"", "6", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl r", "Returns Pearson's product moment correlation coefficient, usually denoted r.", "pa rl r \"reg1\"", "0.5564589284286688", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl sum_squares", "Returns the sum of squared deviations of the predicted y values about their mean (which equals the mean of y).", "pa rl sum_squares \"reg1\"", "700.8333333333334", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl r_square", "Returns the coefficient of determination, usually denoted r-square.", "pa rl r_square \"reg1\"", "0.3096465390279824", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl significance", "Returns the significance level of the slope (equiv) correlation.", "pa rl significance \"reg1\"", "0.2514643980065754", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl slope_confidence_interval", "Returns the half-width of a 95% confidence interval for the slope estimate.", "pa rl slope_confidence_interval \"reg1\"", "15.027949957243381", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl slope_std_err", "Returns the standard error of the slope estimate, usually denoted s(b1).", "pa rl slope_std_err \"reg1\"", "5.412658773652741", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl sum_squared_errors", "Returns the sum of squared errors (SSE) associated with the regression model.", "pa rl sum_squared_errors \"reg1\"", "1562.5", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl total_sum_squares", "Returns the sum of squared deviations of the y values about their mean.", "pa rl total_sum_squares \"reg1\"", "2263.3333333333335", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl x_sum_squares", "Returns the sum of squared deviations of the x values about their mean.", "pa rl x_sum_squares \"reg1\"", "13.333333333333334", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl slope_confidence_interval", "Returns the half-width of a (100-100*alpha)% confidence interval for the slope estimate.", "pa rl slope_confidence_interval \"reg1\" 0.2", "51.80063969449396", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("alpha", "The double alpha", "number", false));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl close", "Close a regression", "pa rl close \"reg1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl close_all", "Close all regressions", "pa rl close_all;", "1", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm load", "Load a multiple regression from the database", "pa rm load \"reg1\" \"demo_cm_mysql\" \"id\" \"quantity\" \"\" \"\" \"\" \"price\" \"select * from products limit 0, 500\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("fieldX1", "The field X1", "string", true));
		mql.addParam(new MQLParam("fieldX2", "The field X2", "string", true));
		mql.addParam(new MQLParam("fieldX3", "The field X3", "string", true));
		mql.addParam(new MQLParam("fieldX4", "The field X4", "string", true));
		mql.addParam(new MQLParam("fieldX5", "The field X5", "string", true));
		mql.addParam(new MQLParam("fieldY", "The field X", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm load_from_json", "Load a multilple regression from two JSON array of doubles", "pa rm load_from_json \"reg1\" \"[\n" + 
				"  [ 1.0, 23.457 ],\n" + 
				"  [ 2.0, 29.987 ],\n" + 
				"  [ 3.0, 89.987 ],\n" + 
				"  [ 4.0, 99.098 ],\n" + 
				"  [ 5.0, 123.08 ]\n" + 
				"]\" \"[7.5, 9.8, 14.7, 14.7, 19.4]\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("jsonArrayX", "The json array X", "string", true));
		mql.addParam(new MQLParam("jsonArrayY", "The json array Y", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm set_no_intercept", "Set no intercept", "pa rm set_no_intercept \"reg1\" true", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("bool", "The boolean", "boolean", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm calculate_adjusted_r_squared", "Returns the adjusted R-squared statistic, defined by the formula R2adj = 1 - [SSR (n - 1)] / [SSTO (n - p)]", "pa rm calculate_adjusted_r_squared \"reg1\"", "0.9930302201822587", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm calculate_residual_sum_of_squares", "Returns the sum of squared residuals.", "pa rm calculate_residual_sum_of_squares \"reg1\"", "2.6787094169120644", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm calculate_r_squared", "Returns the R-Squared statistic, defined by the formula R2 = 1 - SSR / SSTO", "pa rm calculate_r_squared \"reg1\"", "0.9972120880729035", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm calculate_total_sum_of_squares", "Returns the sum of squared deviations of Y from its mean.", "pa rm calculate_total_sum_of_squares \"reg1\"", "960.8299999999999", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_error_variance", "Estimates the variance of the error.", "pa rm estimate_error_variance \"reg1\"", "1.3393547084560322", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_regressand_variance", "Returns the variance of the regressand, ie Var(y).", "pa rm estimate_regressand_variance \"reg1\"", "21.746999999999996", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_regression_standard_error", "Estimates the standard error of the regression.", "pa rm estimate_regression_standard_error \"reg1\"", "1.1573049332202954", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_regression_parameters_variance", "Estimates the variance of the regression parameters, ie Var(b).", "pa rm estimate_regression_parameters_variance \"reg1\"", "[<br>  [<br>    1.1939297361248733,<br>    -0.6413091167219049,<br>    0.012718472658507332<br>  ],<br>  [<br>    -0.6413091167219049,<br>    1.3402027085716288,<br>    -0.04621465840546843<br>  ],<br>  [<br>    0.012718472658507332,<br>    -0.04621465840546843,<br>    0.0017221335163781243<br>  ]<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_residuals", "Estimates the residuals, ie u = y - X*b.", "pa rm estimate_residuals \"reg1\"", "[<br>  -0.21160225532182864,<br>  0.39217249531173515,<br>  0.5051039893042244,<br>  -1.3403164432563521,<br>  0.6546422139622123<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_regression_parameters_standard_errors", "Returns the standard errors of the regression parameters.", "pa rm estimate_regression_parameters_standard_errors \"reg1\"", "[<br>  1.264553444360703,<br>  1.3397786414221338,<br>  0.04802653051960952<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm estimate_regression_parameters", "Estimates the regression parameters b.", "pa rm estimate_regression_parameters \"reg1\"", "[<br>  5.036908634809639,<br>  1.3187573895222953,<br>  0.05780518527475357<br>]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm predict", "Make a prediction", "pa rm predict \"reg1\" \"[12, 34]\"", "22.827373608418803", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("jsonX", "The json that contains an array of x", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm exist", "Check if a regression already exist", "pa rm exist \"reg1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm show", "Show all regressions", "pa rm show", "[<br>  \"reg1\"<br>]", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm close", "Close a regression", "pa rm close \"reg1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm close_all", "Close all regressions", "pa rm close_all;", "1", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);

		functions.put("Data quality", new Vector<MQLDocumentation>());
		page_description.put("Data quality", "<img src='images/p.png' style='vertical-align: middle;'>Here you can check your data quality.");
		mql = new MQLDocumentation(true, "dq algorithm show", "Show all algorithms", "dq algorithm show", "{<br>  \"is_null_or_empty\": \"\\n\\tis null or empty [VAR];\\n\",<br>  \"is_hour_without_sec\": \"\\n\\ttype is_hour_without_sec [VAR];\\n\",<br>  \"is_bool\": \"\\n\\ttype is_bool [VAR] 0 1;\\n\",<br>  \"is_integer\": \"\\n\\ttype is_integer [VAR] 8;\\n\",<br>  \"is_valid_timestamp\": \"\\n\\ttype is_valid_timestamp [VAR] \\\"yyyy-MM-dd HH:mm:ss\\\";\\n\",<br>  \"is_hour\": \"\\n\\ttype is_hour [VAR];\\n\",<br>  \"is_big_int\": \"\\n\\ttype is_big_int [VAR];\\n\",<br>  \"is_matches_regex\": \"\\n\\ttype is_matches_regex [VAR] \\\".*ze.*\\\";\\n\",<br>  \"COGNITIVE_french_word\": \"\\n\\tword exist [VAR] fr;\\n\",<br>  \"is_empty\": \"\\n\\tis empty [VAR];\\n\",<br>  \"is_time\": \"\\n\\ttype is_time [VAR];\\n\",<br>  \"is_char\": \"\\n\\ttype is_char [VAR] 25;\\n\",<br>  \"is_email\": \"\\n\\ttype is_email [VAR];\\n\",<br>  \"is_enum\": \"\\n\\ttype is_enum [VAR] \\\"1,2,3\\\";\\n\",<br>  \"is_decimal\": \"\\n\\ttype is_decimal [VAR] 4 5;\\n\",<br>  \"is_null\": \"\\n\\tis null [VAR];\\n\",<br>  \"is_double\": \"\\n\\ttype is_double [VAR];\\n\",<br>  \"is_byte\": \"\\n\\ttype is_byte [VAR];\\n\",<br>  \"is_varchar\": \"\\n\\ttype is_varchar [VAR] 15;\\n\",<br>  \"is_float\": \"\\n\\ttype is_float [VAR];\\n\",<br>  \"is_int\": \"\\n\\ttype is_int [VAR];\\n\",<br>  \"is_date\": \"\\n\\ttype is_valid_date [VAR] \\\"yyyy-MM-dd\\\";\\n\",<br>  \"is_small_int\": \"\\n\\ttype is_small_int [VAR];\\n\",<br>  \"is_medium_int\": \"\\n\\ttype is_medium_int [VAR];\\n\",<br>  \"is_number\": \"\\n\\ttype is_number [VAR];\\n\"<br>}", null, null, null, null, false, "");
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm set", "Set an algorithm", "dq algorithm set \"key\" (mql {\n	type is_char [VAR] 25;\n});", "1", null, null, null, null, false, "") ;
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("mql", "The MQL source code", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm exist", "Check if an algorithm already exist", "dq algorithm exist \"key\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm get", "Get an algorithm", "dq algorithm get \"key\";", "<br>	type is_char [VAR] 25;<br>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm remove", "Remove an algorithm", "dq algorithm remove \"key\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq generate", "Generate the Data Quality MQL script", "dq generate \"demo_cm_mysql\" \"products\" \"quantity\" \"[\n" + 
				"  \\\"is_big_int\\\",\n" + 
				"  \\\"is_date\\\"\n" + 
				"]\" \"select * from products limit 0, 500\"", "json load \"dq\" \"[]\";<br><br>json load \"dq_quantity\" \"{}\";<br>json iobject \"dq_quantity\" / field \"quantity\" STR;<br>json iobject \"dq_quantity\" / algo \"{}\" OBJ;<br>json iobject \"dq_quantity\" /algo \"is_big_int\" (mql {<br>	type is_big_int [T_quantity];<br>}) STR;<br>json iobject \"dq_quantity\" /algo \"is_date\" (mql {<br>	type is_valid_date [T_quantity] \"yyyy-MM-dd\";<br>}) STR;<br>json iarray \"dq\" / (json doc \"dq_quantity\") OBJ;<br><br>dq analyse \"demo_cm_mysql\" (json doc \"dq\") \"products\" \"select * from products limit 0, 500\";", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("tablename", "The table name", "string", true));
		mql.addParam(new MQLParam("fieldname", "The field name", "string", true));
		mql.addParam(new MQLParam("jsonArrayAlgoId", "The json array with selected algorithms id", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq analyse", "Make a data quality on a database", "json load \"dq\" \"[]\";\n" + 
				"\n" + 
				"json load \"dq_name\" \"{}\";\n" + 
				"json iobject \"dq_name\" / field \"name\" STR;\n" + 
				"json iobject \"dq_name\" / algo \"{}\" OBJ;\n" + 
				"json iobject \"dq_name\" /algo \"is_float\" (mql {\n" + 
				"	type is_float [T_name];\n" + 
				"}) STR;\n" + 
				"json iobject \"dq_name\" /algo \"is_null\" (mql {\n" + 
				"	is null [T_name];\n" + 
				"}) STR;\n" + 
				"json iobject \"dq_name\" /algo \"is_small_int\" (mql {\n" + 
				"	type is_small_int [T_name];\n" + 
				"}) STR;\n" + 
				"json iobject \"dq_name\" /algo \"is_valid_timestamp\" (mql {\n" + 
				"	type is_valid_timestamp [T_name] \"yyyy-MM-dd HH:mm:ss\";\n" + 
				"}) STR;\n" + 
				"json iarray \"dq\" / (json doc \"dq_name\") OBJ;\n" + 
				"\n" + 
				"dq analyse \"demo_cm_mysql\" (json doc \"dq\") \"products\" \"SELECT * FROM `products` LIMIT 0, 500\";", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("jsonArrayAlgoId", "The json array with selected algorithms id", "string", true));
		mql.addParam(new MQLParam("title", "The title", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq analyse show", "Show an analyse", "json load \"dq\" \"[]\";\n" + 
				"\n" + 
				"json load \"dq_name\" \"{}\";\n" + 
				"json iobject \"dq_name\" / field \"name\" STR;\n" + 
				"json iobject \"dq_name\" / algo \"{}\" OBJ;\n" + 
				"json iobject \"dq_name\" /algo \"is_float\" (mql {\n" + 
				"	type is_float [T_name];\n" + 
				"}) STR;\n" + 
				"json iobject \"dq_name\" /algo \"is_null\" (mql {\n" + 
				"	is null [T_name];\n" + 
				"}) STR;\n" + 
				"json iobject \"dq_name\" /algo \"is_small_int\" (mql {\n" + 
				"	type is_small_int [T_name];\n" + 
				"}) STR;\n" + 
				"json iobject \"dq_name\" /algo \"is_valid_timestamp\" (mql {\n" + 
				"	type is_valid_timestamp [T_name] \"yyyy-MM-dd HH:mm:ss\";\n" + 
				"}) STR;\n" + 
				"json iarray \"dq\" / (json doc \"dq_name\") OBJ;\n" + 
				"\n" + 
				"dq analyse show \"demo_cm_mysql\" (json doc \"dq\") \"is_float\" \"name\" \"products\" \"SELECT * FROM `products` LIMIT 0, 500\";", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("jsonArrayAlgoId", "The json array with selected algorithms id", "string", true));
		mql.addParam(new MQLParam("algoKey", "The algo key selected", "string", true));
		mql.addParam(new MQLParam("fieldKey", "The field key selected", "string", true));
		mql.addParam(new MQLParam("title", "The title", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Data quality").add(mql);
		
		page_group.put("Machine Learning", "Artificial Intelligence");
		
		functions.put("Machine Learning", new Vector<MQLDocumentation>());
		page_description.put("Machine Learning", "<img src='images/p.png' style='vertical-align: middle;'>Machine learning.");
		mql = new MQLDocumentation(true, "ml cluster xy_scatter", "Show a machine learning scatter", "ml cluster xy_scatter \"cluster1\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster load", "Load a cluster from the database", "ml cluster load \"cluster1\" \"demo_cm_mysql\" \"id\" \"quantity\" 5 3 \"select * from products limit 0, 500\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("fieldX", "The field X", "string", true));
		mql.addParam(new MQLParam("fieldY", "The field X", "string", true));
		mql.addParam(new MQLParam("maximumRadius", "The maximum radius", "string", true));
		mql.addParam(new MQLParam("minPoint", "The minimun of points", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster load_from_json", "Load a cluster from a json array", "ml cluster load_from_json \"cluster1\" 1 2 \"[\n" + 
				"  [1.0, 2.0],\n" + 
				"  [1.0, 2.2],\n" + 
				"  [1.0, 2.5],\n" + 
				"  [1.2, 2.0],\n" + 
				"  [1.11, 0.9],\n" + 
				"  [5.0, 3.0],\n" + 
				"  [5.2, 3.08],\n" + 
				"  [5.1, 3.0],\n" + 
				"  [5.15, 2.9],\n" + 
				"  [3.0, 2.0],\n" + 
				"  [3.2, 2.08],\n" + 
				"  [3.1, 2.0],\n" + 
				"  [3.15, 1.9]\n" + 
				"]\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("maximumRadius", "The maximum radius", "string", true));
		mql.addParam(new MQLParam("minPoint", "The minimun of points", "string", true));
		mql.addParam(new MQLParam("jsonArray", "The json array", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster exist", "Check if a cluster already exist", "ml cluster exist \"cluster1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster show", "Show all clusters", "ml cluster show", "...", null, null, null, null, false, "");
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster nb", "Get the number of cluster", "ml cluster nb \"cluster1\"", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster nb_point", "Get the number of points in a cluster", "ml cluster nb_point \"cluster1\" 0", "5", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster points", "Get all points in a cluster", "ml cluster points \"cluster1\" 0", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster point_get", "Get a point in a cluster", "ml cluster point_get \"cluster1\" 0 0", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		mql.addParam(new MQLParam("pointIndex", "The point index", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster point_delete", "Get a point in a cluster", "ml cluster point_delete \"cluster1\" 0 0;", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		mql.addParam(new MQLParam("pointIndex", "The point index", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster point_add", "Add a point in a cluster", "ml cluster point_add \"cluster1\" 0 5 6;", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		mql.addParam(new MQLParam("x", "The x", "string", true));
		mql.addParam(new MQLParam("y", "The y", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster point_update", "Update a point in a cluster", "ml cluster point_update \"cluster1\" 0 0 5 6;", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		mql.addParam(new MQLParam("pointIndex", "The point index", "string", true));
		mql.addParam(new MQLParam("x", "The x", "string", true));
		mql.addParam(new MQLParam("y", "The y", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster distance", "Get the distance between two points", "ml cluster distance \"cluster1\" 1 2 4 7", "3", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("x1", "The x1 value", "string", true));
		mql.addParam(new MQLParam("y1", "The y1 value", "string", true));
		mql.addParam(new MQLParam("x2", "The x2 value", "string", true));
		mql.addParam(new MQLParam("y2", "The y2 value", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster close", "Close a cluster", "ml cluster close \"cluster1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster close_all", "Close all clusters", "ml cluster close_all;", "1", null, null, null, null, false, "");
		functions.get("Machine Learning").add(mql);

		mql = new MQLDocumentation(true, "ml h_node load_from_json", "Load heuristic node objects from json", "ml h_node load_from_json \"hid1\" true \"[\n" + 
				"  [ \\\"A\\\", \\\"B\\\", 2 ],\n" + 
				"  [ \\\"A\\\", \\\"C\\\", 4 ],\n" + 
				"  [ \\\"D\\\", \\\"A\\\", 1 ],\n" + 
				"  [ \\\"A\\\", \\\"D\\\", 3 ],\n" + 
				"  [ \\\"E\\\", \\\"F\\\", 4 ],\n" + 
				"  [ \\\"D\\\", \\\"F\\\", 2 ],\n" + 
				"  [ \\\"G\\\", \\\"H\\\", 1 ],\n" + 
				"  [ \\\"F\\\", \\\"H\\\", 5 ],\n" + 
				"  [ \\\"F\\\", \\\"I\\\", 7 ],\n" + 
				"  [ \\\"J\\\", \\\"I\\\", 2 ]\n" + 
				"]\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("isDirect", "Is direct ?", "boolean", true));
		mql.addParam(new MQLParam("jsonArray", "The JSON data array", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node add_problem", "Add a new problem", "ml h_node add_problem \"hid1\" \"probId1\" \"A\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("problemId", "The problem id", "string", true));
		mql.addParam(new MQLParam("from", "The origin to search", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node predict", "Predict the best path", "ml h_node predict \"hid1\" \"probId1\" \"dijkstra|a_star|bellman_ford|breadth_first_search|depth_first_search|depth_limited_search|i_d_a_star|multi_objective_l_s|hill_climbing\" \"I\" null", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("problemId", "The problem id", "string", true));
		mql.addParam(new MQLParam("algorithm", "The algorithm (dijkstra|a_star|bellman_ford|breadth_first_search|depth_first_search|depth_limited_search|i_d_a_star|multi_objective_l_s|hill_climbing)", "string", true));
		mql.addParam(new MQLParam("to", "The origin to search", "string", true));
		mql.addParam(new MQLParam("param", "The algorithm param (integer for 'depth_limited_search', boolean for 'hill_climbing')", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node exist", "Check if a heuristic node object already exist", "ml h_node exist \"hid1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node exist_problem", "Check if a problem already exist", "ml h_node exist_problem \"hid1\" \"probId1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("problemId", "The problem id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node show", "Show all heuristic node objects", "ml h_node show", "...", null, null, null, null, false, "");
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node show_problem", "Show all problems into a heuristic object", "ml h_node show_problem \"hid1\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node close_problem", "Close a problem", "ml h_node close_problem \"hid1\" \"probId1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("problemId", "The problem id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node close", "Close a heuristic node object", "ml h_node close \"hid1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node close_all", "Close all heuristic node objects", "ml h_node close_all;", "1", null, null, null, null, false, "");
		functions.get("Machine Learning").add(mql);
		
		functions.put("Deep Learning", new Vector<MQLDocumentation>());
		page_description.put("Deep Learning", "<img src='images/p.png' style='vertical-align: middle;'>Deep learning.");

		mql = new MQLDocumentation(true, "dl4j csv_train_and_save_model", "To Train and save a model from a CSV file.", "#------------------------------------------;\n" + 
				"# START GLOBAL PARAMETERS                  ;\n" + 
				"#------------------------------------------;\n" + 
				"json load \"dl4j_config\" \"{}\";\n" + 
				"json iobject \"dl4j_config\" / \"dataTrainFile\" \"demo/dl4j_iris.csv\" STR; #The CSV source file;\n" + 
				"json iobject \"dl4j_config\" / \"batchSizeTraining\" \"150\" STR; #The number of line to train;\n" + 
				"json iobject \"dl4j_config\" / \"csvTest\" \"demo/dl4j_iris_test.csv\" STR; #The CSV source file;\n" + 
				"json iobject \"dl4j_config\" / \"testBatchSize\" \"9\" STR; #The number of line to test;\n" + 
				"json iobject \"dl4j_config\" / \"pathToSaveModel\" \"demo/dl4j_iris.model\" STR; #Save the model;\n" + 
				"json iobject \"dl4j_config\" / \"pathToSaveNormalizer\" \"demo/dl4j_iris.normz\" STR; #Save the normalizer;\n" + 
				"json iobject \"dl4j_config\" / \"numClasses\" \"3\" STR; #The number of classes;\n" + 
				"json iobject \"dl4j_config\" / \"labelIndex\" \"4\" STR; #Where is the position of the label index (starts with 0);\n" + 
				"json iobject \"dl4j_config\" / \"iterations\" \"3000\" STR; #The number of iterations;\n" + 
				"json iobject \"dl4j_config\" / \"epochs\" \"1\" STR; #The number of fit;\n" + 
				"json iobject \"dl4j_config\" / \"seed\" \"123\" STR; #Random number;\n" + 
				"json iobject \"dl4j_config\" / \"learningRate\" 0.01 STR;\n" + 
				"json iobject \"dl4j_config\" / \"l2\" 1e-4 STR;\n" + 
				"json iobject \"dl4j_config\" / \"regularization\" true STR;\n" + 
				"json iobject \"dl4j_config\" / \"activation\" \"TANH\" STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"json iobject \"dl4j_config\" / \"weightInit\" \"XAVIER\" STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#------------------------------------------;\n" + 
				"# ADDITIONAL GLOBAL PARAMETERS             ;\n" + 
				"#------------------------------------------;\n" + 
				"#json iobject \"dl4j_config\" / \"optimizationAlgo\" null STR; #STOCHASTIC_GRADIENT_DESCENT|LINE_GRADIENT_DESCENT|LBFGS|HESSIAN_FREE|CONJUGATE_GRADIENT;\n" + 
				"#json iobject \"dl4j_config\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_config\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_config\" / \"lrPolicyDecayRate\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"lrPolicyPower\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"lrPolicySteps\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"maxNumLineSearchIterations\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"miniBatch\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"minimize\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"useDropConnect\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"dropOut\" null STR;\n" + 
				"#json iobject \"dl4j_config\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_config\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_config\" / \"cacheMode\" null STR; #NONE|HOST|DEVICE;\n" + 
				"#json iobject \"dl4j_config\" / \"convolutionMode\" null STR; #Truncate|Strict|Same;\n" + 
				"#json iobject \"dl4j_config\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_config\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#------------------------------------------;\n" + 
				"# BUILD LAYERS                             ;\n" + 
				"#------------------------------------------;\n" + 
				"json iobject \"dl4j_config\" / \"layers\" \"[]\" ARRAY;\n" + 
				"#------------------------------------------;\n" + 
				"json load \"dl4j_hidden_layer01\" \"{}\";\n" + 
				"json iobject \"dl4j_hidden_layer01\" / \"type\" \"DenseLayer\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer01\" / \"nIn\" \"4\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer01\" / \"nOut\" \"3\" STR;\n" + 
				"json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer01\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"json load \"dl4j_hidden_layer02\" \"{}\";\n" + 
				"json iobject \"dl4j_hidden_layer02\" / \"type\" \"DenseLayer\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer02\" / \"nIn\" \"3\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer02\" / \"nOut\" \"3\" STR;\n" + 
				"json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer02\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"json load \"dl4j_hidden_layer03\" \"{}\";\n" + 
				"json iobject \"dl4j_hidden_layer03\" / \"type\" \"OutputLayer\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer03\" / \"nIn\" \"3\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer03\" / \"nOut\" \"3\" STR;\n" + 
				"json iobject \"dl4j_hidden_layer03\" / \"activation\" \"SOFTMAX\" STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"json iobject \"dl4j_hidden_layer03\" / \"lossFunction\" \"NEGATIVELOGLIKELIHOOD\" STR; #XENT|SQUARED_LOSS|SQUARED_HINGE|RMSE_XENT|RECONSTRUCTION_CROSSENTROPY|POISSON|NEGATIVELOGLIKELIHOOD|MSE|MEAN_SQUARED_LOGARITHMIC_ERROR|MEAN_ABSOLUTE_PERCENTAGE_ERROR|MEAN_ABSOLUTE_ERROR|MCXENT|L2|L1|KL_DIVERGENCE|HINGE|CUSTOM|EXPLL|COSINE_PROXIMITY;\n" + 
				"json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer03\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"# EXAMPLE LAYERS                           ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"SubsamplingLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"kernelSize\" \"5:5\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"stride\" \"5:5\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"padding\" \"5:5\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"convolutionMode\" null STR; #Truncate|Strict|Same;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"eps\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"pnorm\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"poolingType\" null STR; #SUM|PNORM|NONE|MAX|AVG;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"RBM\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"hiddenUnit\" null STR; #SOFTMAX|RECTIFIED|IDENTITY|GAUSSIAN|BINARY;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"k\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lossFunction\" null STR; #XENT|SQUARED_LOSS|SQUARED_HINGE|RMSE_XENT|RECONSTRUCTION_CROSSENTROPY|POISSON|NEGATIVELOGLIKELIHOOD|MSE|MEAN_SQUARED_LOGARITHMIC_ERROR|MEAN_ABSOLUTE_PERCENTAGE_ERROR|MEAN_ABSOLUTE_ERROR|MCXENT|L2|L1|KL_DIVERGENCE|HINGE|CUSTOM|EXPLL|COSINE_PROXIMITY;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"preTrainIterations\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"sparsity\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"visibleBiasInit\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"visibleUnit\" null STR; #SOFTMAX|LINEAR|IDENTITY|GAUSSIAN|BINARY;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"LSTM\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"forgetGateBiasInit\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gateActivationFunction\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"LocalResponseNormalization\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"alpha\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"beta\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"k\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"n\" null STR;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"GravesLSTM\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"forgetGateBiasInit\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gateActivationFunction\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"GravesBidirectionalLSTM\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"forgetGateBiasInit\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gateActivationFunction\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"AutoEncoder\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"corruptionLevel\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"preTrainIterations\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"sparsity\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"visibleBiasInit\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lossFunction\" null STR; #XENT|SQUARED_LOSS|SQUARED_HINGE|RMSE_XENT|RECONSTRUCTION_CROSSENTROPY|POISSON|NEGATIVELOGLIKELIHOOD|MSE|MEAN_SQUARED_LOGARITHMIC_ERROR|MEAN_ABSOLUTE_PERCENTAGE_ERROR|MEAN_ABSOLUTE_ERROR|MCXENT|L2|L1|KL_DIVERGENCE|HINGE|CUSTOM|EXPLL|COSINE_PROXIMITY;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"BatchNormalization\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"eps\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"beta\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"decay\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gamma\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lockGammaBeta\" null STR; #Boolean;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"minibatch\" null STR; #Boolean;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"GlobalPoolingLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"poolingType\" null STR; #SUM|PNORM|NONE|MAX|AVG;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"pnorm\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"collapseDimensions\" null STR; #Boolean;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"poolingDimensions\" null STR; #3:3;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"EmbeddingLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"DropoutLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"ActivationLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"DenseLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"ConvolutionLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"kernelSize\" \"5:5\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"stride\" \"5:5\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"padding\" \"5:5\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"epsilon\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"momentum\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"rho\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"rmsDecay\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"convolutionMode\" null STR; #Truncate|Strict|Same;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"adamMeanDecay\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"adamVarDecay\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"cudnnAlgoMode\" null STR; #USER_SPECIFIED|PREFER_FASTEST|NO_WORKSPACE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"cudnnBwdDataMode\" null STR; #WINOGRAD_NONFUSED|WINOGRAD|FFT_TILING|FFT|COUNT|ALGO_1|ALGO_0;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"cudnnBwdFilterMode\" null STR; #WINOGRAD_NONFUSED|WINOGRAD|FFT_TILING|FFT|COUNT|ALGO_3|ALGO_1|ALGO_0;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"cudnnFwdMode\" null STR; #WINOGRAD_NONFUSED|WINOGRAD|IMPLICIT_PRECOMP_GEMM|IMPLICIT_GEMM|GEMM|FFT_TILING|FFT|DIRECT|COUNT;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"OutputLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lossFunction\" null STR; #XENT|SQUARED_LOSS|SQUARED_HINGE|RMSE_XENT|RECONSTRUCTION_CROSSENTROPY|POISSON|NEGATIVELOGLIKELIHOOD|MSE|MEAN_SQUARED_LOGARITHMIC_ERROR|MEAN_ABSOLUTE_PERCENTAGE_ERROR|MEAN_ABSOLUTE_ERROR|MCXENT|L2|L1|KL_DIVERGENCE|HINGE|CUSTOM|EXPLL|COSINE_PROXIMITY;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"CenterLossOutputLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"alpha\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lambda\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientCheck\" null STR; #Boolean;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lossFunction\" null STR; #XENT|SQUARED_LOSS|SQUARED_HINGE|RMSE_XENT|RECONSTRUCTION_CROSSENTROPY|POISSON|NEGATIVELOGLIKELIHOOD|MSE|MEAN_SQUARED_LOGARITHMIC_ERROR|MEAN_ABSOLUTE_PERCENTAGE_ERROR|MEAN_ABSOLUTE_ERROR|MCXENT|L2|L1|KL_DIVERGENCE|HINGE|CUSTOM|EXPLL|COSINE_PROXIMITY;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"#json load \"dl4j_hidden_layer00\" \"{}\";\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"type\" \"RnnOutputLayer\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nIn\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"nOut\" \"3\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dropOut\" \"0.25\" STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"activation\" null STR; #TANH|SOFTSIGN|SOFTPLUS|SOFTMAX|SIGMOID|SELU|RRELU|RELU|RECTIFIEDTANH|RATIONALTANH|LEAKYRELU|IDENTITY|HARDTANH|HARDSIGMOID|ELU|CUBE;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"weightInit\" null STR; #ZERO|XAVIER_UNIFORM|XAVIER_LEGACY|XAVIER_FAN_IN|XAVIER|UNIFORM|SIGMOID_UNIFORM|RELU_UNIFORM|RELU|DISTRIBUTION;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"dist\" null STR; #BinomialDistribution:0:1|NormalDistribution:0:1|UniformDistribution:0:1;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasInit\" null STR; \n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"biasLearningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l1Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"l2Bias\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalization\" null STR; #RenormalizeL2PerParamType|RenormalizeL2PerLayer|None|ClipL2PerParamType|ClipL2PerLayer|ClipElementWiseAbsoluteValue;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"gradientNormalizationThreshold\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRate\" null STR;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"learningRateDecayPolicy\" null STR; #TorchStep|Step|Sigmoid|Score|Schedule|Poly|None|Inverse|Exponential;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"updater\" null STR; #SGD|RMSPROP|NONE|NESTEROVS|NADAM|ADAMAX|ADAM|ADAGRAD|ADADELTA;\n" + 
				"#json iobject \"dl4j_hidden_layer00\" / \"lossFunction\" null STR; #XENT|SQUARED_LOSS|SQUARED_HINGE|RMSE_XENT|RECONSTRUCTION_CROSSENTROPY|POISSON|NEGATIVELOGLIKELIHOOD|MSE|MEAN_SQUARED_LOGARITHMIC_ERROR|MEAN_ABSOLUTE_PERCENTAGE_ERROR|MEAN_ABSOLUTE_ERROR|MCXENT|L2|L1|KL_DIVERGENCE|HINGE|CUSTOM|EXPLL|COSINE_PROXIMITY;\n" + 
				"#json iarray \"dl4j_config\" /layers (json doc \"dl4j_hidden_layer00\") OBJ;\n" + 
				"#------------------------------------------;\n" + 
				"# END GLOBAL PARAMETERS                    ;\n" + 
				"#------------------------------------------;\n" + 
				"json iobject \"dl4j_config\" / \"backprop\" true STR;\n" + 
				"json iobject \"dl4j_config\" / \"pretrain\" false STR;\n" + 
				"\n" + 
				"#------------------------------------------;\n" + 
				"# Train and save the model/serializer      ;\n" + 
				"#------------------------------------------;\n" + 
				"dl4j csv_train_and_save_model (json doc \"dl4j_config\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("json_config", "The JSON configuration", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl4j csv_load_model", "Load a DL4J model and normalizer into the memory", "#------------------------------------------;\n" + 
				"# Load the model and the normalizer        ;\n" + 
				"#------------------------------------------;\n" + 
				"dl4j csv_load_model \"dl4jId1\" (json doc \"dl4j_config\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dl4jId", "The memory key to get the DL4J model", "string", true));
		mql.addParam(new MQLParam("json_config", "The JSON configuration", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl4j csv_predict", "Make a prediction from a DL4J model", "#------------------------------------------;\n" + 
				"# Make predictions                         ;\n" + 
				"#------------------------------------------;\n" + 
				"dl4j csv_predict \"dl4jId1\" (json doc \"dl4j_config\") \"demo/dl4j_iris_test.csv\" 3;", "[\n" + 
						"  {\n" + 
						"    \"probs\": [\n" + 
						"      0.97952205,\n" + 
						"      0.01892847,\n" + 
						"      0.0015495354\n" + 
						"    ],\n" + 
						"    \"prob_class\": \"class1\",\n" + 
						"    \"prob_index\": 0\n" + 
						"  },\n" + 
						"  {\n" + 
						"    \"probs\": [\n" + 
						"      0.9793347,\n" + 
						"      0.01910426,\n" + 
						"      0.0015610401\n" + 
						"    ],\n" + 
						"    \"prob_class\": \"class1\",\n" + 
						"    \"prob_index\": 0\n" + 
						"  },\n" + 
						"  {\n" + 
						"    \"probs\": [\n" + 
						"      0.97940457,\n" + 
						"      0.019038359,\n" + 
						"      0.0015570737\n" + 
						"    ],\n" + 
						"    \"prob_class\": \"class1\",\n" + 
						"    \"prob_index\": 0\n" + 
						"  }\n" + 
						"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dl4jId", "The memory key to get the DL4J model", "string", true));
		mql.addParam(new MQLParam("json_config", "The JSON configuration", "string", true));
		mql.addParam(new MQLParam("csv_file", "The CSV file to predict", "string", true));
		mql.addParam(new MQLParam("nb_line_to_predict", "The number of line to predict", "number", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl4j show", "To show all DL4J networks.", "dl4j show", "[]", null, null, null, null, false, "");
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl4j exist", "To check if a DL4J network was already loaded.", "dl4j exist \"dl4jId1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dl4jId", "The dl4j id", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl4j delete", "To delete a DL4J network from the memory.", "dl4j delete \"dl4jId1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("dl4jId", "The dl4j id", "string", true));
		functions.get("Deep Learning").add(mql);
		
		mql = new MQLDocumentation(true, "dl n_bayesian create_train_file", "To get a train file.", "file create \"demo/train.txt\" \"transfer VIR RECU 854526,VIR RECU 8545269324 DE: OCTO TECHNOLOGY MOTIF: VIREMENT SALAIRES JUIN 11\n" + 
				"withdrawal CARTE X2052 RETRAI,CARTE X2052 RETRAIT DAB 22/06 21H37 CIC PARIS SAINT ROCH 10859A00\n" + 
				"payment CARTE X2052 21/06 ,CARTE X2052 21/06 PHIE LA BOETIE\n" + 
				"fees OPTION TRANQUILLIT,OPTION TRANQUILLITE\n" + 
				"fees COTISATION JAZZ,COTISATION JAZZ\n" + 
				"atmfees FRAIS PAIEMENT HOR,FRAIS PAIEMENT HORS ZONE EURO 1 PAIEMENT A 1.00 EUR NT 38.06 EUR A 2.70\n" + 
				"atmfees FRAIS PAIEMENT HOR,FRAIS PAIEMENT HORS ZONE EURO 1 PAIEMENT A 1.00 EUR NT 3.33 EUR A\";", "", null, null, null, null, false, "");
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl n_bayesian show", "To show all Naive Bayesian networks.", "dl n_bayesian show", "[]", null, null, null, null, false, "");
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl n_bayesian exist", "To check if a Naive Bayesian network already exist.", "dl n_bayesian exist \"bayesian1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl n_bayesian create_model", "To create a new Naive Bayesian network model.", "dl n_bayesian create_model \"en\" \"demo/train.txt\" 10 \"demo/model.bin\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("lang", "The language", "string", true));
		mql.addParam(new MQLParam("train_file_path", "The train file path", "string", true));
		mql.addParam(new MQLParam("iterations_param", "The iterations param", "number (ex: 10)", true));
		mql.addParam(new MQLParam("model_file_path_to_save", "The model file path to save", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl n_bayesian load", "To load a Naive Bayesian network model.", "dl n_bayesian load \"bayesian1\" \"demo/model.bin\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		mql.addParam(new MQLParam("model_file_path", "The model file path", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl n_bayesian predict", "To predict a sentence.", "dl n_bayesian predict \"bayesian1\" \"21/06 PHIE LA BOETIE\";", "{\n" + 
				"  \"input\": \"I\\u0027m happy\",\n" + 
				"  \"prediction\": \"positif\",\n" + 
				"  \"best_percent\": \"66,666667 %\",\n" + 
				"  \"best_index\": 0,\n" + 
				"  \"probabilities\": [\n" + 
				"    {\n" + 
				"      \"prob_double\": 0.6666666666666666,\n" + 
				"      \"index\": 0,\n" + 
				"      \"prob_percent\": \"66,666667 %\",\n" + 
				"      \"key\": \"positif\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"prob_double\": 0.3333333333333333,\n" + 
				"      \"index\": 1,\n" + 
				"      \"prob_percent\": \"33,333333 %\",\n" + 
				"      \"key\": \"negatif\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"best_double\": 0.6666666666666666\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		mql.addParam(new MQLParam("sentence", "The sentence", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl n_bayesian delete", "To delete a Naive Bayesian network.", "dl n_bayesian delete \"bayesian1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		functions.get("Deep Learning").add(mql);
		
		mql = new MQLDocumentation(true, "dl bayesian show", "To show all Bayesian networks.", "dl bayesian show", "[]", null, null, null, null, false, "");
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl bayesian exist", "To check if a Bayesian network already exist.", "dl bayesian exist \"bayesian1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl bayesian create", "To create a new Bayesian network.", "dl bayesian create \"bayesian1\" \"[\\\"positif\\\", \\\"negatif\\\"]\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		mql.addParam(new MQLParam("cats", "The categories (JSON array)", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl bayesian add_sentence", "To create a new Bayesian network.", "dl bayesian add_sentence \"bayesian1\" \"positif\" \"I'm happy\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		mql.addParam(new MQLParam("cat", "The category key", "string", true));
		mql.addParam(new MQLParam("sentence", "The sentence", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl bayesian init", "To init a Bayesian network.", "dl bayesian init \"bayesian1\" 1;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		mql.addParam(new MQLParam("laplace_int", "The Laplace int", "number", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl bayesian predict", "To predict a sentence.", "dl bayesian predict \"bayesian1\" \"I'm happy\";", "{\n" + 
				"  \"input\": \"I\\u0027m happy\",\n" + 
				"  \"prediction\": \"positif\",\n" + 
				"  \"best_percent\": \"66,666667 %\",\n" + 
				"  \"best_index\": 0,\n" + 
				"  \"probabilities\": [\n" + 
				"    {\n" + 
				"      \"prob_double\": 0.6666666666666666,\n" + 
				"      \"index\": 0,\n" + 
				"      \"prob_percent\": \"66,666667 %\",\n" + 
				"      \"key\": \"positif\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"prob_double\": 0.3333333333333333,\n" + 
				"      \"index\": 1,\n" + 
				"      \"prob_percent\": \"33,333333 %\",\n" + 
				"      \"key\": \"negatif\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"best_double\": 0.6666666666666666\n" + 
				"}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		mql.addParam(new MQLParam("sentence", "The sentence", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl bayesian delete", "To delete a Bayesian network.", "dl bayesian delete \"bayesian1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The network key", "string", true));
		functions.get("Deep Learning").add(mql);
		
		mql = new MQLDocumentation(true, "dl csv execute_config", "Train a CSV file.", "json load \"csv_config\" \"{}\";\n" + 
				"json iobject \"csv_config\" / \"filePath\" \"demo/iris.data.txt\" STR;\n" + 
				"json iobject \"csv_config\" / \"modelPath\" \"demo/iris.md\" STR;\n" + 
				"json iobject \"csv_config\" / \"helperPath\" \"demo/iris.hl\" STR;\n" + 
				"json iobject \"csv_config\" / \"nbLoop\" \"6\" STR;\n" + 
				"json iobject \"csv_config\" / \"validationPercent\" \"0.3\" STR;\n" + 
				"json iobject \"csv_config\" / \"shuffle\" \"true\" STR;\n" + 
				"json iobject \"csv_config\" / \"seed\" \"1001\" STR;\n" + 
				"\n" + 
				"json iobject \"csv_config\" / \"cols\" \"[]\" ARRAY;\n" + 
				"\n" + 
				"json load \"col\" \"{}\";\n" + 
				"json iobject \"col\" / \"index\" \"0\" STR;\n" + 
				"json iobject \"col\" / \"title\" \"sepal-length\" STR;\n" + 
				"json iobject \"col\" / \"type\" \"in\" STR;\n" + 
				"json iarray \"csv_config\" \"/cols\" (json doc \"col\") OBJ;\n" + 
				"\n" + 
				"json load \"col\" \"{}\";\n" + 
				"json iobject \"col\" / \"index\" \"1\" STR;\n" + 
				"json iobject \"col\" / \"title\" \"sepal-width\" STR;\n" + 
				"json iobject \"col\" / \"type\" \"in\" STR;\n" + 
				"json iarray \"csv_config\" \"/cols\" (json doc \"col\") OBJ;\n" + 
				"\n" + 
				"json load \"col\" \"{}\";\n" + 
				"json iobject \"col\" / \"index\" \"2\" STR;\n" + 
				"json iobject \"col\" / \"title\" \"petal-length\" STR;\n" + 
				"json iobject \"col\" / \"type\" \"in\" STR;\n" + 
				"json iarray \"csv_config\" \"/cols\" (json doc \"col\") OBJ;\n" + 
				"\n" + 
				"json load \"col\" \"{}\";\n" + 
				"json iobject \"col\" / \"index\" \"3\" STR;\n" + 
				"json iobject \"col\" / \"title\" \"petal-width\" STR;\n" + 
				"json iobject \"col\" / \"type\" \"in\" STR;\n" + 
				"json iarray \"csv_config\" \"/cols\" (json doc \"col\") OBJ;\n" + 
				"\n" + 
				"json load \"col\" \"{}\";\n" + 
				"json iobject \"col\" / \"index\" \"4\" STR;\n" + 
				"json iobject \"col\" / \"title\" \"species\" STR;\n" + 
				"json iobject \"col\" / \"type\" \"out\" STR;\n" + 
				"json iarray \"csv_config\" \"/cols\" (json doc \"col\") OBJ;\n" + 
				"\n" + 
				"dl csv execute_config (json doc \"csv_config\");", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonConfig", "The train JSON configuration", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl csv load_network", "Load the model and the helper into the memory.", "dl csv load_network \"demo/iris.md\" \"demo/iris.hl\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("modelFilePath", "The model file path", "string", true));
		mql.addParam(new MQLParam("helperFilePath", "The helper file path", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl csv predict", "Predict from the model.", "json load \"input\" \"[5.9, 3.0, 5.1, 1.8]\";\n" + 
				"dl csv predict (json doc \"input\");", "Iris-virginica", "json load \"input\" \"[5.6, 2.9, 3.6, 1.3]\";\n" + 
						"dl csv predict (json doc \"input\");", "Iris-versicolor", null, null, false, "");
		mql.addParam(new MQLParam("jsonArrayInput", "The JSON array that contains input values", "string", true));
		functions.get("Deep Learning").add(mql);
		
		mql = new MQLDocumentation(true, "dl img step1 create_training", "Create a training file.", "#Create the training file;\n" + 
				"file writer_open \"w1\" \"demo/animals/imgTrainConfig.txt\" true TEXT \"utf-8\";\n" + 
				"dl img step1 create_training \"w1\" 100 100 true;", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		mql.addParam(new MQLParam("width", "The image width", "number", true));
		mql.addParam(new MQLParam("height", "The image height", "number", true));
		mql.addParam(new MQLParam("isRGB", "Is RGB ? (true, false)", "bool", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img step2 add_image", "Add image into the training file.", "#Load input images;\n" + 
				"-> \"[dir]\" \"demo/animals/english_springer\";\n" + 
				"-> \"[id]\" \"english_springer\";\n" + 
				"json load \"files\" (file dir_list [dir]);\n" + 
				"-> \"[nbFiles]\" (json count \"files\" /);\n" + 
				"-> \"[iFiles]\" 0;\n" + 
				"for (-> \"[i]\" 0) (< [i] [nbFiles]) (++ \"[i]\") {\n" + 
				"\n" + 
				"	-> \"[cur_file]\" (json select \"files\" (concat \"/[\" [i] \"]\"));\n" + 
				"\n" + 
				"	if (string ends_with [cur_file] \".jpg\") {\n" + 
				"\n" + 
				"		dl img step2 add_image \"w1\" (concat [dir] \"/\" [cur_file]) [id];\n" + 
				"		++ \"[iFiles]\";\n" + 
				"\n" + 
				"	};\n" + 
				"\n" + 
				"};\n" + 
				"file writer_flush \"w1\";\n" + 
				"concat [iFiles] \" files added.\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		mql.addParam(new MQLParam("imgPath", "The image path", "string", true));
		mql.addParam(new MQLParam("identity", "The image tag", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img step3 create_hidden_layer", "Create a hidden layer.", "dl img step3 create_hidden_layer \"w1\" \"100\"\n" + 
				"file writer_flush \"w1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		mql.addParam(new MQLParam("nbNeuron", "The number of neuron in the hidden layers", "number", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img step4 create_or_load_network", "Create or load a network.", "dl img step4 create_or_load_network \"w1\" \"tanh\" \"demo/animals/network.eg\";\n" + 
				"file writer_flush \"w1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		mql.addParam(new MQLParam("activation", "The activation function (ex: "+ImageNeuralNetworkManager.activations.substring(1, ImageNeuralNetworkManager.activations.length()-1)+")", "string", true));
		mql.addParam(new MQLParam("saveNetworkPath", "The path to save the network", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img step5 train_network", "Train a network.", "dl img step5 train_network \"w1\" \"console\" 1 0.25 50 \"demo/animals/network.eg\";\n" + 
				"file writer_flush \"w1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		mql.addParam(new MQLParam("mode", "The mode (console|gui)", "string", true));
		mql.addParam(new MQLParam("minutes", "The number of minutes", "number", true));
		mql.addParam(new MQLParam("strategyError", "The strategy error (ex: 0.25)", "number", true));
		mql.addParam(new MQLParam("strategyCycles", "The strategy cycles (ex: 50)", "number", true));
		mql.addParam(new MQLParam("saveNetworkPath", "The path to save the network", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img step6 predict", "Predict an image from a neural network.", "#Load input images;\n" + 
				"-> \"[dir]\" \"demo/animals/english_springer_predict\";\n" + 
				"-> \"[id]\" \"english_springer\";\n" + 
				"json load \"files\" (file dir_list [dir]);\n" + 
				"-> \"[nbFiles]\" (json count \"files\" /);\n" + 
				"-> \"[iFiles]\" 0;\n" + 
				"for (-> \"[i]\" 0) (< [i] [nbFiles]) (++ \"[i]\") {\n" + 
				"\n" + 
				"	-> \"[cur_file]\" (json select \"files\" (concat \"/[\" [i] \"]\"));\n" + 
				"\n" + 
				"	if (string ends_with [cur_file] \".jpg\") {\n" + 
				"\n" + 
				"		dl img step6 predict \"w1\" (concat [dir] \"/\" [cur_file]) [id];\n" + 
				"		++ \"[iFiles]\";\n" + 
				"\n" + 
				"	};\n" + 
				"\n" + 
				"};\n" + 
				"file writer_flush \"w1\";\n" + 
				"concat [iFiles] \" files added.\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		mql.addParam(new MQLParam("imgPath", "The image path", "string", true));
		mql.addParam(new MQLParam("identity", "The image tag", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img step7 close_file", "Close the config file.", "#Close the config file;\nfile writer_close \"w1\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("writerId", "The writer id", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img execute_config", "Execute a config training file", "in editor {\n" + 
				"	dl img execute_config \"demo/animals/imgTrainConfig.txt\"\n" + 
				"};", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("trainConfigFilePath", "The train config file path", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img load_network", "Load a network into the memory", "dl img load_network \"demo/animals/network.eg\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("networkPath", "The network path", "string", true));
		functions.get("Deep Learning").add(mql);
		mql = new MQLDocumentation(true, "dl img predict", "Predict an image from the network", "dl img predict \"dir/image.jpg\" true 100 100 \"{\n" + 
				"  \\\"0\\\": \\\"english_springer\\\"\n" + 
				"}\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("imagePath", "The image path", "string", true));
		mql.addParam(new MQLParam("isRGB", "Is RGB ? (true|false)", "string", true));
		mql.addParam(new MQLParam("width", "The image width", "string", true));
		mql.addParam(new MQLParam("height", "The image height", "string", true));
		mql.addParam(new MQLParam("jsonIdentity", "The json identity", "string", true));
		functions.get("Deep Learning").add(mql);

		functions.put("Bot", new Vector<MQLDocumentation>());
		page_description.put("Bot", "<img src='images/p.png' style='vertical-align: middle;'>Bot.");

		mql = new MQLDocumentation(true, "bot create", "To create a new bot", "bot create \"mona\" \"fr\" 0 \"mona\" \"payet\" \"cancel_key\" \"Désolé, je ne comprends pas. Je préviens l'administrateur...\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("lang", "The language (en|fr)", "string", true));
		mql.addParam(new MQLParam("is_male", "1: male, 0: female", "string", true));
		mql.addParam(new MQLParam("firstname", "The firstname", "string", true));
		mql.addParam(new MQLParam("lastname", "The lastname", "string", true));
		mql.addParam(new MQLParam("cancel_key", "The cancel key", "string", true));
		mql.addParam(new MQLParam("not_found_response", "The not found response", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot show", "To show all bots", "bot show;", "[]", null, null, null, null, false, "");
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot get", "To get a bot", "bot get \"mona\";", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot exist", "To check if a bot already exist", "bot exist \"mona\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot reload", "To reload a bot", "bot reload \"mona\";", "Trigger: 2; Training: 1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot delete", "To delete a bot", "bot delete \"mona\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_create", "To create a new user for a bot", "bot user_create \"mona\" \"jim\" \"pwd\" \"{\n"
				+ "  \\\"[user_firstname]\\\": \\\"jimmitry\\\",\n"
				+ "  \\\"[user_lastname]\\\": \\\"payet\\\"\n"
				+ "}\" \"*\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		mql.addParam(new MQLParam("password", "The password", "string", true));
		mql.addParam(new MQLParam("json_vars", "The json variable object", "string", true));
		mql.addParam(new MQLParam("rights", "The rights separate by coma ,", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_show", "To show all users for a bot", "bot user_show \"mona\";", "[]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_get", "To get a user for a bot", "bot user_get \"mona\" \"jim\"", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_exist", "To check if a user already exist for a bot", "bot user_exist \"mona\" \"jim\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot set_wait_replay", "To set a key for a user", "bot set_wait_replay \"mona\" \"jim\" \"dire_bonjour\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot get_wait_replay", "To set a variable for a user", "bot get_wait_replay \"mona\" \"jim\";", "dire_bonjour", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_set_var", "To set a variable for a user", "bot user_set_var \"mona\" \"jim\" \"[A]\" \"9\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		mql.addParam(new MQLParam("varname", "The variable name", "string", true));
		mql.addParam(new MQLParam("value", "The value", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_get_var", "To get a variable for a user", "bot user_get_var \"mona\" \"jim\" \"[A]\";", "9", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		mql.addParam(new MQLParam("varname", "The variable name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_get_vars", "To get all variables for a user", "bot user_get_vars \"mona\" \"jim\";", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot user_delete", "To delete a user for a bot", "bot user_delete \"mona\" \"jim\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("login", "The user", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot training_merge", "To add/update a training", "json load \"trigger\" \"[]\";\n"
				+ "json iarray \"trigger\" / \"abandonnes l'affaire\" STR;\n"
				+ "json iarray \"trigger\" / \"abandonnes la tâche\" STR;\n"
				+ "json iarray \"trigger\" / \"annules la tâche en cours\" STR;\n"
				+ "json iarray \"trigger\" / \"tu peux abandonner l'affaire\" STR;\n"
				+ "json iarray \"trigger\" / \"tu peux abandonner la tâche\" STR;\n"
				+ "json iarray \"trigger\" / \"tu peux annuler la tâche en cours\" STR;\n"
				+ "json iarray \"trigger\" / \"laisses tomber l'affaire\" STR;\n"
				+ "json iarray \"trigger\" / \"tu peux laisser tomber l'affaire\" STR;\n"
				+ "json iarray \"trigger\" / \"reprends tout à zéro\" STR;\n"
				+ "json iarray \"trigger\" / \"tu peux reprendre tout à zéro\" STR;\n"
				+ "json load \"consciousness_done\" \"[]\";\n"
				+ "json iarray \"consciousness_done\" / \"j'ai repris tout à zéro.\" STR;\n"
				+ "json iarray \"consciousness_done\" / \"j'ai abandonné la tâche en cours.\" STR;\n"
				+ "json load \"consciousness_think\" \"[]\";\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je devais reprendre tout à zéro.\" STR;\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je devais abandonner la tâche en cours.\" STR;\n"
				+ "json load \"consciousness_understand\" \"[]\";\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je devais reprendre tout à zéro.\" STR;\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je devais abandonner la tâche en cours.\" STR;\n"
				+ "json load \"consciousness_subject\" \"[]\";\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait de reprendre tout à zéro.\" STR;\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait d'abandonner la tâche en cours.\" STR;\n"
				+ "json load \"consciousness\" \"{}\";\n"
				+ "json iobject \"consciousness\" / \"done\" (json doc \"consciousness_done\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"think\" (json doc \"consciousness_think\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"understand\" (json doc \"consciousness_understand\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"subject\" (json doc \"consciousness_subject\") ARRAY;\n"
				+ "bot training_merge \"mona\" \"cancel_key\" \"task\"\n"
				+ "	\"\" \"Annuler la tâche en cours\"\n"
				+ "	(json doc \"trigger\")\n"
				+ "	(mql {\n"
				+ "		######################################\n"
				+ "		# Default variables :\n"
				+ "		# [bot]\n"
				+ "		# [bot_lang]\n"
				+ "		# [bot_is_male]\n"
				+ "		# [bot_firstname]\n"
				+ "		# [bot_lastname]\n"
				+ "		\n"
				+ "		# [training_key]\n"
				+ "		# [training_context]\n"
				+ "		# [training_rights]\n"
				+ "		\n"
				+ "		# [user]\n"
				+ "		# [user_request]\n"
				+ "		# [user_variables]\n"
				+ "		# [user_firstname]\n"
				+ "		# [user_lastname]\n"
				+ "		# [user_rights]\n"
				+ "		# [user_wait_replay]\n"
				+ "		######################################;\n"
				+ "\n"
				+ "		bot set_wait_replay [bot] [user] null;\n"
				+ "		\n"
				+ "		switch (math random 5)\n"
				+ "			(0) {\"D'accord.\"}\n"
				+ "			(1) {\"Ok.\"}\n"
				+ "			(2) {\"Okay.\"}\n"
				+ "			(3) {\"C'est d'accord.\"}\n"
				+ "			{\"Bien reçu.\"}\n"
				+ "		;\n"
				+ "		\n"
				+ "	})\n"
				+ "	(json doc \"consciousness\")\n"
				+ ";\n"
				+ "\n"
				+ "json load \"trigger\" \"[]\";\n"
				+ "json iarray \"trigger\" / \"bonjour\" STR;\n"
				+ "json iarray \"trigger\" / \"salut\" STR;\n"
				+ "json load \"consciousness_done\" \"[]\";\n"
				+ "json iarray \"consciousness_done\" / \"j'ai dis bonjour.\" STR;\n"
				+ "json iarray \"consciousness_done\" / \"j'ai répondu salut.\" STR;\n"
				+ "json load \"consciousness_think\" \"[]\";\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je devais dire bonjour.\" STR;\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je devais répondre salut.\" STR;\n"
				+ "json load \"consciousness_understand\" \"[]\";\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je devais dire bonjour.\" STR;\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je devais répondre salut.\" STR;\n"
				+ "json load \"consciousness_subject\" \"[]\";\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait de dire bonjour.\" STR;\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait de répondre salut.\" STR;\n"
				+ "json load \"consciousness\" \"{}\";\n"
				+ "json iobject \"consciousness\" / \"done\" (json doc \"consciousness_done\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"think\" (json doc \"consciousness_think\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"understand\" (json doc \"consciousness_understand\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"subject\" (json doc \"consciousness_subject\") ARRAY;\n"
				+ "bot training_merge \"mona\" \"dire_bonjour\" \"politesse\"\n"
				+ "	\"\" \"Répondre à un bonjour de l'utilisateur\"\n"
				+ "	(json doc \"trigger\")\n"
				+ "	(mql {\n"
				+ "\n"
				+ "		bot set_wait_replay [bot] [user] null;\n"
				+ "		\n"
				+ "		switch (math random 2)\n"
				+ "			(0) {\"Salut.\"}\n"
				+ "			{\"Bonjour.\"}\n"
				+ "		;\n"
				+ "		\n"
				+ "	})\n"
				+ "	(json doc \"consciousness\")\n"
				+ ";\n"
				+ "\n"
				+ "json load \"trigger\" \"[]\";\n"
				+ "json iarray \"trigger\" / \"[1] + [2]\" STR;\n"
				+ "json iarray \"trigger\" / \"fait une addition avec les nombres [1] et [2]\" STR;\n"
				+ "json load \"consciousness_done\" \"[]\";\n"
				+ "json iarray \"consciousness_done\" / \"j'ai fait une addition avec les nombres [1] et [2].\" STR;\n"
				+ "json iarray \"consciousness_done\" / \"j'ai additionné les nombres [1] et [2].\" STR;\n"
				+ "json load \"consciousness_think\" \"[]\";\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je devais faire une addition avec les nombres [1] et [2].\" STR;\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je devais additionner les nombres [1] et [2].\" STR;\n"
				+ "json load \"consciousness_understand\" \"[]\";\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je devais faire une addition avec les nombres [1] et [2].\" STR;\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je devais additionner les nombres [1] et [2].\" STR;\n"
				+ "json load \"consciousness_subject\" \"[]\";\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait de faire une addition avec les nombres [1] et [2].\" STR;\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait d'additionner les nombres [1] et [2].\" STR;\n"
				+ "json load \"consciousness\" \"{}\";\n"
				+ "json iobject \"consciousness\" / \"done\" (json doc \"consciousness_done\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"think\" (json doc \"consciousness_think\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"understand\" (json doc \"consciousness_understand\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"subject\" (json doc \"consciousness_subject\") ARRAY;\n"
				+ "bot training_merge \"mona\" \"faire_une_addition\" \"math\"\n"
				+ "	\"\" \"Faire une addition avec 2 nombres\"\n"
				+ "	(json doc \"trigger\")\n"
				+ "	(mql {\n"
				+ "\n"
				+ "		json load \"vars\" [user_variables];\n"
				+ "\n"
				+ "		if (and (== (json count \"vars\" /) 2) (and (not (is null or empty (json select \"vars\" \"/[0]\"))) (not (is null or empty (json select \"vars\" \"/[1]\"))))) {\n"
				+ "\n"
				+ "			-> \"[1]\" (json select \"vars\" \"/[0]\");\n"
				+ "			-> \"[2]\" (json select \"vars\" \"/[1]\");\n"
				+ "			\n"
				+ "			-> \"[calc]\" (+ [1] [2]);\n"
				+ "\n"
				+ "			bot set_wait_replay [bot] [user] null;\n"
				+ "	\n"
				+ "			switch (math random 4)\n"
				+ "				(0) {concat \"Le résultat est \" [calc] \".\"}\n"
				+ "				(1) {concat \"Le résultat de l'addition est \" [calc] \".\"}\n"
				+ "				(2) {concat [1] \" + \" [2] \" = \" [calc]}\n"
				+ "				{[calc]}\n"
				+ "			;\n"
				+ "			\n"
				+ "		} {\n"
				+ "\n"
				+ "			case\n"
				+ "				(string starts_with (string lower [user_request]) \"utilises le nombre\") {\n"
				+ "					json load \"split\" (string get_variable (string lower [user_request]) \"utilises le nombre [1]\");\n"
				+ "					if (and (== (json count \"split\" \"/\") 1) (type is_double (json select \"split\" \"/[0]\"))) {\n"
				+ "						case\n"
				+ "							(not (env exist var \"[1]\")) {\n"
				+ "								-> \"[1]\" (json select \"split\" \"/[0]\");\n"
				+ "							}\n"
				+ "							(not (env exist var \"[2]\")) {\n"
				+ "								-> \"[2]\" (json select \"split\" \"/[0]\");\n"
				+ "							}\n"
				+ "						;\n"
				+ "					};\n"
				+ "				}\n"
				+ "				(string starts_with (string lower [user_request]) \"utilises les nombres\") {\n"
				+ "					json load \"split\" (string get_variable (string lower [user_request]) \"utilises les nombres [1] et [2]\");\n"
				+ "					if (and (and (== (json count \"split\" \"/\") 2) (type is_double (json select \"split\" \"/[0]\"))) (type is_double (json select \"split\" \"/[1]\"))) {\n"
				+ "						-> \"[1]\" (json select \"split\" \"/[0]\");\n"
				+ "						-> \"[2]\" (json select \"split\" \"/[1]\");\n"
				+ "					};\n"
				+ "				}\n"
				+ "				(\n"
				+ "					json load \"split\" (string split (string lower [user_request]) \" \" -1);\n"
				+ "					and (== (json count \"split\" \"/\") 2) (and (type is_double (json select \"split\" \"/[0]\")) (type is_double (json select \"split\" \"/[1]\")))\n"
				+ "				) {\n"
				+ "					-> \"[1]\" (json select \"split\" \"/[0]\");\n"
				+ "					-> \"[2]\" (json select \"split\" \"/[1]\");\n"
				+ "				}\n"
				+ "				(\n"
				+ "					json load \"split\" (string split (string lower [user_request]) \" \" -1);\n"
				+ "					and (== (json count \"split\" \"/\") 3) (and (equal (json select \"split\" \"/[1]\") \"et\") (and (type is_double (json select \"split\" \"/[0]\")) (type is_double (json select \"split\" \"/[2]\"))))\n"
				+ "				) {\n"
				+ "					-> \"[1]\" (json select \"split\" \"/[0]\");\n"
				+ "					-> \"[2]\" (json select \"split\" \"/[2]\");\n"
				+ "				}\n"
				+ "			;\n"
				+ "\n"
				+ "			if (and (not (env exist var \"[1]\")) (not (env exist var \"[2]\"))) {\n"
				+ "				bot set_wait_replay [bot] [user] [training_key];\n"
				+ "				\"Désolé, pour faire une addition il me faut 2 nombres.\";\n"
				+ "			} {\n"
				+ "				if (or (not (env exist var \"[1]\")) (not (env exist var \"[2]\"))) {\n"
				+ "					bot set_wait_replay [bot] [user] [training_key];\n"
				+ "					\"Désolé, pour faire une addition il me faut encore un nombre.\";\n"
				+ "				} {\n"
				+ "\n"
				+ "					bot set_wait_replay [bot] [user] null;\n"
				+ "				\n"
				+ "					-> \"[calc]\" (+ [1] [2]);\n"
				+ "			\n"
				+ "					switch (math random 4)\n"
				+ "						(0) {concat \"Le résultat est \" [calc] \".\"}\n"
				+ "						(1) {concat \"Le résultat de l'addition est \" [calc] \".\"}\n"
				+ "						(2) {concat [1] \" + \" [2] \" = \" [calc]}\n"
				+ "						{[calc]}\n"
				+ "					;\n"
				+ "					\n"
				+ "				};\n"
				+ "			};\n"
				+ "			\n"
				+ "		};\n"
				+ "		\n"
				+ "	})\n"
				+ "	(json doc \"consciousness\")\n"
				+ ";\n"
				+ "\n"
				+ "json load \"trigger\" \"[]\";\n"
				+ "json iarray \"trigger\" / \"tu te trompes ce n'est pas ce que je voulais\" STR;\n"
				+ "json iarray \"trigger\" / \"tu te trompes ce n'est pas ce que j'attendais\" STR;\n"
				+ "json load \"consciousness_done\" \"[]\";\n"
				+ "json iarray \"consciousness_done\" / \"j'ai comprenais que je m'étais trompé.\" STR;\n"
				+ "json load \"consciousness_think\" \"[]\";\n"
				+ "json iarray \"consciousness_think\" / \"j'avais pensé que je m'étais trompé.\" STR;\n"
				+ "json load \"consciousness_understand\" \"[]\";\n"
				+ "json iarray \"consciousness_understand\" / \"j'avais compris que je m'étais trompé.\" STR;\n"
				+ "json load \"consciousness_subject\" \"[]\";\n"
				+ "json iarray \"consciousness_subject\" / \"il s'agissait de comprendre que je m'étais trompé.\" STR;\n"
				+ "json load \"consciousness\" \"{}\";\n"
				+ "json iobject \"consciousness\" / \"done\" (json doc \"consciousness_done\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"think\" (json doc \"consciousness_think\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"understand\" (json doc \"consciousness_understand\") ARRAY;\n"
				+ "json iobject \"consciousness\" / \"subject\" (json doc \"consciousness_subject\") ARRAY;\n"
				+ "bot training_merge \"mona\" \"error_key\" \"task\"\n"
				+ "	\"\" \"Remarquer que je me trompe\"\n"
				+ "	(json doc \"trigger\")\n"
				+ "	(mql {\n"
				+ "\n"
				+ "		if (env exist var \"[user_last_request]\") {\n"
				+ "			sql connect \"session1\" {cm get \"MENTDB\";};\n"
				+ "			sql dml \"session1\" (concat \"INSERT INTO public.mona_not_found (\n"
				+ "					bot,\n"
				+ "					login,\n"
				+ "					input\n"
				+ "				) VALUES (\n"
				+ "					\" (sql encode [bot]) \" ,\n"
				+ "					\" (sql encode [user]) \" ,\n"
				+ "					\" (sql encode [user_last_request]) \"\n"
				+ "				);\");\n"
				+ "			sql disconnect \"session1\";\n"
				+ "		};\n"
				+ "		\n"
				+ "		switch (math random 5)\n"
				+ "			(0) {\"D'accord, je préviens l'administrateur.\"}\n"
				+ "			(1) {\"Ok, je préviens l'administrateur.\"}\n"
				+ "			(2) {\"Okay, je préviens l'administrateur.\"}\n"
				+ "			(3) {\"C'est d'accord, je préviens l'administrateur.\"}\n"
				+ "			{\"Bien reçu, je préviens l'administrateur.\"}\n"
				+ "		;\n"
				+ "		\n"
				+ "	})\n"
				+ "	(json doc \"consciousness\")\n"
				+ ";\n"
				+ "bot execute \"mona\" \"jim\" \"fait une addition\";\n"
				+ "bot execute \"mona\" \"jim\" \"utilises les nombres 99 et 100\";\n"
				+ "bot execute \"mona\" \"jim\" \"abandonnes la tâche\";\n"
				+ "bot execute \"mona\" \"jim\" \"bonjour\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		mql.addParam(new MQLParam("context", "The context", "string", true));
		mql.addParam(new MQLParam("rights", "The rights separate by |", "string", true));
		mql.addParam(new MQLParam("description", "The description", "string", true));
		mql.addParam(new MQLParam("in_trigger_json", "The triggers", "string", true));
		mql.addParam(new MQLParam("out_mql_output_json", "MQL to execute (the result is return)", "string", true));
		mql.addParam(new MQLParam("consciousness_json", "The consciousness object", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot training_get", "To get a training for a bot", "bot training_get \"mona\" \"dire_bonjour\"", "{}", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot training_exist", "To check if a training key already exist for a bot", "bot training_exist \"mona\" \"dire_bonjour\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot training_generate_merge", "To update a training", "bot training_generate_merge \"mona\" \"dire_bonjour\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot training_delete", "To delete a training for a bot", "bot training_delete \"mona\" \"dire_bonjour\";", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot training_search", "To search a training", "bot training_search \"%mona%\" \"%dire_bonjour%\" \"%%\" \"%%\" \"%%\" \"%%\" \"%%\";", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("key", "The training key", "string", true));
		mql.addParam(new MQLParam("context", "The context", "string", true));
		mql.addParam(new MQLParam("description", "The description", "string", true));
		mql.addParam(new MQLParam("in_trigger_json", "The triggers", "string", true));
		mql.addParam(new MQLParam("out_mql_output_json", "MQL to execute (the result is return)", "string", true));
		mql.addParam(new MQLParam("consciousness_json", "The consciousness object", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot execute", "To execute a talk", "bot execute \"mona\" \"jim\" \"bonjour\"", "[\"lisa\"]", null, null, null, null, false, "");
		mql.addParam(new MQLParam("bot", "The bot name", "string", true));
		mql.addParam(new MQLParam("user", "The user", "string", true));
		mql.addParam(new MQLParam("request", "The request", "string", true));
		functions.get("Bot").add(mql);
		
	}

}
