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

package re.jpayet.mentdb.ext.azure;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.json.simple.JSONArray;

import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.microsoft.aad.msal4j.PublicClientApplication;
import com.microsoft.aad.msal4j.UserNamePasswordParameters;

import re.jpayet.mentdb.ext.json.JsonManager;

public class AzureManager {

	public static String getTokenAzure(String email, String password, String clientId, String tenant, String scope) throws Exception {
		
		PublicClientApplication app = PublicClientApplication.builder(clientId)
                .authority("https://login.microsoftonline.com/"+tenant+"/oauth2/token").build();

        Set<String> scopes = new HashSet<>();
        
        JSONArray sc = (JSONArray) JsonManager.load(scope);
        for(int i=0;i<sc.size();i++) {
        		scopes.add(""+sc.get(i));
        }
        
        CompletableFuture<IAuthenticationResult> future = app.acquireToken(
        		UserNamePasswordParameters
                .builder(scopes, email, password.toCharArray()).build());
        
        IAuthenticationResult result = future.join();
        return result.accessToken();
		
	}
	
	/*
	 * public static String getTokenAzure() throws Exception {
		
		PublicClientApplication app = PublicClientApplication.builder("d61b48b2-5bee-4a2e-8ace-99f3c7603428")
                .authority("https://login.microsoftonline.com/076306e2-1bc8-4b4a-b69f-e35a53d4dd66/oauth2/token").build();

        Set<String> scope = new HashSet<>();
        //scope.add("https://outlook.office365.com/offline_access");
        scope.add("https://outlook.office365.com/IMAP.AccessAsUser.All");
        //scope.add("https://outlook.office365.com/User.Read");
        
        CompletableFuture<IAuthenticationResult> future = app.acquireToken(
        		UserNamePasswordParameters
                .builder(scope, "test1@samiallianz.onmicrosoft.com", "Tal08366".toCharArray()).build());
        
        IAuthenticationResult result = future.join();
        return result.accessToken();
		
	}
	 */
	
}
