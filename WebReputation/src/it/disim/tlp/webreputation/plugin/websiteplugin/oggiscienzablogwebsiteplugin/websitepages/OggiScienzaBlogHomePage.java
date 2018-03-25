package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websitepages;

/** @author Lorenzo Addazi */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.OggiScienzaBlogWebsitePluginController;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.homepageparser.OggiScienzaBlogHomePageParser;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.homepageparser.OggiScienzaBlogHomePageParserResponse;


public class OggiScienzaBlogHomePage extends OggiScienzaBlogWebsitePluginController {
	
	private OggiScienzaBlogHomePageParser parser = new OggiScienzaBlogHomePageParser();
	
	@Override
	public List<AggregatorPost> parseResource(String resource, Date from, Date to) {
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		OggiScienzaBlogHomePageParserResponse parserResponse = parser.getPostsFromResource(resource, from, to).getWebsiteParserResponseObject();
		result.addAll(parserResponse.getWebsiteParserResponseObject());
		return result;
	}
	
}
