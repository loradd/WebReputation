package it.disim.tlp.webreputation.plugin.socialnetworkplugin.googleplus;

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.model.socialnetworkplugin.SocialNetworkPlugin;
import it.disim.tlp.webreputation.plugin.model.socialnetworkplugin.authentication.AuthRequest;
import it.disim.tlp.webreputation.plugin.model.socialnetworkplugin.authentication.AuthResponse;
import it.disim.tlp.webreputation.plugin.socialnetworkplugin.googleplus.authentication.GooglePlusAuthRequest;
import it.disim.tlp.webreputation.plugin.socialnetworkplugin.googleplus.authentication.GooglePlusAuthResponse;

import java.util.Date;
import java.util.List;

public class GooglePlusPlugin implements SocialNetworkPlugin<GooglePlusAuthRequest, GooglePlusAuthResponse>{

	@Override
	public List<AggregatorPost> getPosts(String resource, String type, Date from)
			throws QuotaExceededException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AggregatorPost> getPosts(String resource, String type,
			Date from, Date to) throws QuotaExceededException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthResponse<GooglePlusAuthResponse> authentication(
			AuthRequest<GooglePlusAuthRequest> request) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
