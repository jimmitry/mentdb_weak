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
		mql = new MQLDocumentation(true, "pa xy_scatter", "Show a predictive analysis scatter", "pa xy_scatter \"[\n" + 
				"  [1.0, 2.0],\n" + 
				"  [1.0, 2.2],\n" + 
				"  [1.0, 2.5],\n" + 
				"  [1.2, 2.0],\n" + 
				"  [1.11, 0.9],\n" + 
				"  [5.0, 3.0],\n" + 
				"  [5.2, 3.08],\n" + 
				"  [5.1, 3.0],\n" + 
				"  [5.15, 2.9]\n" + 
				"]\"", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("jsonArray", "The json array of x,y", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa xy_scatter", "Show a predictive analysis scatter", "pa xy_scatter \"demo_cm_mysql\" \"id\" \"quantity\" \"select * from products limit 0, 500\"", "In editor ...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("cmId", "The database connection id", "string", true));
		mql.addParam(new MQLParam("fieldX", "The field X", "string", true));
		mql.addParam(new MQLParam("fieldY", "The field X", "string", true));
		mql.addParam(new MQLParam("sqlSource", "The select query (origin)", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl load", "Load a linear regression from the database", "pa rl load \"reg1\" \"demo_cm_mysql\" \"id\" \"quantity\" \"select * from products limit 0, 500\"", "1", null, null, null, null, false, "");
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
				"]\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		mql.addParam(new MQLParam("jsonArray", "The json array", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl load_empty", "Load an empty linear regression", "pa rl load_empty \"reg1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl exist", "Check if a regression already exist", "pa rl exist \"reg1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl show", "Show all regressions", "pa rl show", "[<br>  \"reg1\"<br>]", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl add_data", "Add data to a regression", "pa rl add_data \"reg1\" 5 56", "1", null, null, null, null, false, "");
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
		mql = new MQLDocumentation(true, "pa rl close", "Close a regression", "pa rl close \"reg1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rl close_all", "Close all regressions", "pa rl close_all", "1", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm load", "Load a multiple regression from the database", "pa rm load \"reg1\" \"demo_cm_mysql\" \"id\" \"quantity\" \"\" \"\" \"\" \"price\" \"select * from products limit 0, 500\"", "1", null, null, null, null, false, "");
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
				"]\" \"[7.5, 9.8, 14.7, 14.7, 19.4]\"", "1", null, null, null, null, false, "");
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
		mql = new MQLDocumentation(true, "pa rm close", "Close a regression", "pa rm close \"reg1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("regId", "The regression id", "string", true));
		functions.get("Predictive analysis").add(mql);
		mql = new MQLDocumentation(true, "pa rm close_all", "Close all regressions", "pa rm close_all", "1", null, null, null, null, false, "");
		functions.get("Predictive analysis").add(mql);

		functions.put("Data quality", new Vector<MQLDocumentation>());
		page_description.put("Data quality", "<img src='images/p.png' style='vertical-align: middle;'>Here you can check your data quality.");
		mql = new MQLDocumentation(true, "dq algorithm show", "Show all algorithms", "dq algorithm show", "{<br>  \"is_null_or_empty\": \"\\n\\tis null or empty [VAR];\\n\",<br>  \"is_hour_without_sec\": \"\\n\\ttype is_hour_without_sec [VAR];\\n\",<br>  \"is_bool\": \"\\n\\ttype is_bool [VAR] 0 1;\\n\",<br>  \"is_integer\": \"\\n\\ttype is_integer [VAR] 8;\\n\",<br>  \"is_valid_timestamp\": \"\\n\\ttype is_valid_timestamp [VAR] \\\"yyyy-MM-dd HH:mm:ss\\\";\\n\",<br>  \"is_hour\": \"\\n\\ttype is_hour [VAR];\\n\",<br>  \"is_big_int\": \"\\n\\ttype is_big_int [VAR];\\n\",<br>  \"is_matches_regex\": \"\\n\\ttype is_matches_regex [VAR] \\\".*ze.*\\\";\\n\",<br>  \"COGNITIVE_french_word\": \"\\n\\tword exist [VAR] fr;\\n\",<br>  \"is_empty\": \"\\n\\tis empty [VAR];\\n\",<br>  \"is_time\": \"\\n\\ttype is_time [VAR];\\n\",<br>  \"is_char\": \"\\n\\ttype is_char [VAR] 25;\\n\",<br>  \"is_email\": \"\\n\\ttype is_email [VAR];\\n\",<br>  \"is_enum\": \"\\n\\ttype is_enum [VAR] \\\"1,2,3\\\";\\n\",<br>  \"is_decimal\": \"\\n\\ttype is_decimal [VAR] 4 5;\\n\",<br>  \"is_null\": \"\\n\\tis null [VAR];\\n\",<br>  \"is_double\": \"\\n\\ttype is_double [VAR];\\n\",<br>  \"is_byte\": \"\\n\\ttype is_byte [VAR];\\n\",<br>  \"is_varchar\": \"\\n\\ttype is_varchar [VAR] 15;\\n\",<br>  \"is_float\": \"\\n\\ttype is_float [VAR];\\n\",<br>  \"is_int\": \"\\n\\ttype is_int [VAR];\\n\",<br>  \"is_date\": \"\\n\\ttype is_valid_date [VAR] \\\"yyyy-MM-dd\\\";\\n\",<br>  \"is_small_int\": \"\\n\\ttype is_small_int [VAR];\\n\",<br>  \"is_medium_int\": \"\\n\\ttype is_medium_int [VAR];\\n\",<br>  \"is_number\": \"\\n\\ttype is_number [VAR];\\n\"<br>}", null, null, null, null, false, "");
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm set", "Set an algorithm", "dq algorithm set \"key\" (mql {\n	type is_char [VAR] 25;\n})", "1", null, null, null, null, false, "") ;
		mql.addParam(new MQLParam("key", "The key", "string", true));
		mql.addParam(new MQLParam("mql", "The MQL source code", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm exist", "Check if an algorithm already exist", "dq algorithm exist \"key\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm get", "Get an algorithm", "dq algorithm get \"key\"", "<br>	type is_char [VAR] 25;<br>", null, null, null, null, false, "");
		mql.addParam(new MQLParam("key", "The key", "string", true));
		functions.get("Data quality").add(mql);
		mql = new MQLDocumentation(true, "dq algorithm remove", "Remove an algorithm", "dq algorithm remove \"key\"", "1", null, null, null, null, false, "");
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
		mql = new MQLDocumentation(true, "ml cluster xy_scatter", "Show a machine learning scatter", "ml cluster xy_scatter \"cluster1\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster load", "Load a cluster from the database", "ml cluster load \"cluster1\" \"demo_cm_mysql\" \"id\" \"quantity\" 5 3 \"select * from products limit 0, 500\"", "...", null, null, null, null, false, "");
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
				"]\"", "...", null, null, null, null, false, "");
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
		mql = new MQLDocumentation(true, "ml cluster point_delete", "Get a point in a cluster", "ml cluster point_delete \"cluster1\" 0 0", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		mql.addParam(new MQLParam("pointIndex", "The point index", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster point_add", "Add a point in a cluster", "ml cluster point_add \"cluster1\" 0 5 6", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		mql.addParam(new MQLParam("clusterIndex", "The cluster index", "string", true));
		mql.addParam(new MQLParam("x", "The x", "string", true));
		mql.addParam(new MQLParam("y", "The y", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster point_update", "Update a point in a cluster", "ml cluster point_update \"cluster1\" 0 0 5 6", "...", null, null, null, null, false, "");
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
		mql = new MQLDocumentation(true, "ml cluster close", "Close a cluster", "ml cluster close \"cluster1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("clusterId", "The cluster id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml cluster close_all", "Close all clusters", "ml cluster close_all", "1", null, null, null, null, false, "");
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
				"]\"", "...", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("isDirect", "Is direct ?", "boolean", true));
		mql.addParam(new MQLParam("jsonArray", "The JSON data array", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node add_problem", "Add a new problem", "ml h_node add_problem \"hid1\" \"probId1\" \"A\"", "1", null, null, null, null, false, "");
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
		mql = new MQLDocumentation(true, "ml h_node close_problem", "Close a problem", "ml h_node close_problem \"hid1\" \"probId1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		mql.addParam(new MQLParam("problemId", "The problem id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node close", "Close a heuristic node object", "ml h_node close \"hid1\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("hId", "The heuristic id", "string", true));
		functions.get("Machine Learning").add(mql);
		mql = new MQLDocumentation(true, "ml h_node close_all", "Close all heuristic node objects", "ml h_node close_all", "1", null, null, null, null, false, "");
		functions.get("Machine Learning").add(mql);
		
		functions.put("Deep Learning", new Vector<MQLDocumentation>());
		page_description.put("Deep Learning", "<img src='images/p.png' style='vertical-align: middle;'>Deep learning.");

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

		mql = new MQLDocumentation(true, "bot show", "To show all bots.", "bot show", "[\"lisa\"]", null, null, null, null, false, "");
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot create", "To create a new bot.", "bot create \"bob\" 1 \"fr\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("botName", "The bot name", "string", true));
		mql.addParam(new MQLParam("is_male", "Is male", "string", true));
		mql.addParam(new MQLParam("lang", "The language (fr|en)", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot remove", "To remove a bot.", "bot remove \"bob\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("botName", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot exist", "To check if a bot already exist.", "bot exist \"bob\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("botName", "The bot name", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot aiml set", "To set (overwrite) a new AIML file.", "bot aiml set \"bob\" \"hello.aiml\" \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\n" + 
				"<aiml>\n" + 
				"<!--  -->\n" + 
				"<category><pattern>BONJOUR</pattern>\n" + 
				"<template>Salut.</template>\n" + 
				"</category>\n" + 
				"<category><pattern>BONJOUR JE SUIS *</pattern>\n" + 
				"<template>Salut <star index = \\\"1\\\"/>.</template>\n" + 
				"</category>\n" + 
				"<category><pattern>* EST UN DEVELOPPEUR</pattern>\n" + 
				"<template>Oui <star index = \\\"1\\\"/> est un développeur.</template>\n" + 
				"</category>\n" + 
				"<category>\n" + 
				"   <pattern>TU CONNAIS *</pattern>\n" + 
				"   \n" + 
				"   <template>\n" + 
				"      <srai><star/> EST UN DEVELOPPEUR</srai>\n" + 
				"   </template>\n" + 
				"   \n" + 
				"</category>\n" + 
				"<category><pattern>QUI ES TU</pattern>\n" + 
				"<template>concat \\\"Je suis '\\\" (name) \\\"'.\\\"|</template>\n" + 
				"</category>\n" + 
				"<category><pattern>MERCI</pattern>\n" + 
				"<template>De rien.</template>\n" + 
				"</category>\n" + 
				"<category><pattern>COMMENT SA VAS</pattern>\n" + 
				"<template>Bien.</template>\n" + 
				"</category>\n" + 
				"</aiml>\n" + 
				"\"", "Loaded after 6 categories in 0.011 sec", null, null, null, null, false, "");
		mql.addParam(new MQLParam("botName", "The bot name", "string", true));
		mql.addParam(new MQLParam("filename", "The AIML filename", "string", true));
		mql.addParam(new MQLParam("xml", "The XML file in AIML format", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot execute", "To execute a string.", "bot execute \"bob\" \"admin\" \"how are you ?\"", "I'm fine.", null, null, null, null, false, "");
		mql.addParam(new MQLParam("botName", "The bot name", "string", true));
		mql.addParam(new MQLParam("user", "The current user", "string", true));
		mql.addParam(new MQLParam("sentence", "The sentence", "string", true));
		functions.get("Bot").add(mql);
		mql = new MQLDocumentation(true, "bot aiml remove", "To set (overwrite) a new AIML file.", "bot aiml remove \"bob\" \"hello.aiml\"", "1", null, null, null, null, false, "");
		mql.addParam(new MQLParam("botName", "The bot name", "string", true));
		mql.addParam(new MQLParam("filename", "The AIML filename", "string", true));
		functions.get("Bot").add(mql);
		
	}

}
