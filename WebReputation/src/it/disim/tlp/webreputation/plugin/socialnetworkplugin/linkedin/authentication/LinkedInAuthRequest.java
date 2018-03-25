package it.disim.tlp.webreputation.plugin.socialnetworkplugin.linkedin.authentication;

/** @author Lorenzo Addazi */

public class LinkedInAuthRequest {
	
	/* LinkedIn Application API Key */
	public final String apiKey;
	/* LinkedIn Application Secret Key*/
	public final String secretKey;
	/* LinkedIn oAuth User Token */
	public final String oAuthUserToken;
	/* LinkedIn oAuth User Secret Code */
	public final String oAuthUserSecret;
	
	/* Custom User Authentication Request Constructor */
	public LinkedInAuthRequest(String apiKey, String secretKey, String oAuthUserToken, String oAuthUserSecret){
		this.apiKey = apiKey;
		this.secretKey = secretKey;
		this.oAuthUserToken = oAuthUserToken;
		this.oAuthUserSecret = oAuthUserSecret;
	}
	
	/* Default User Authentication Request Constructor */
	public LinkedInAuthRequest(){
		this.apiKey = "7726wxcv6jcfqn";
		this.secretKey = "EzM9wMfMq7mUC9Jx";
		this.oAuthUserToken = "28f0aabe-2daa-4134-855c-5e5d5b22fba4";
		this.oAuthUserSecret = "626fe7a7-f9ed-4dbf-86c8-90557c481e69";
	}
	/* Get User API Key */
	public String getApiKey() {
		return apiKey;
	}
	/* Get User Secret Key */
	public String getSecretKey() {
		return secretKey;
	}
	/* Get OAuth User Token */
	public String getoAuthUserToken() {
		return oAuthUserToken;
	}
	/* Get OAuth User Secret Code */
	public String getoAuthUserSecret() {
		return oAuthUserSecret;
	}

}
