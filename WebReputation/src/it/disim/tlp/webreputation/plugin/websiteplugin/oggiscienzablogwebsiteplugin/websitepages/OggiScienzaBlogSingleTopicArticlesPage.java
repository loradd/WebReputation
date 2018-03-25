package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websitepages;

/** @author Lorenzo Addazi */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.OggiScienzaBlogWebsitePluginController;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singletopicarticlespageparser.OggiScienzaBlogSingleTopicArticlesPageParser;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singletopicarticlespageparser.OggiScienzaBlogSingleTopicArticlesPageParserResponse;

public class OggiScienzaBlogSingleTopicArticlesPage extends OggiScienzaBlogWebsitePluginController {
	
	private OggiScienzaBlogSingleTopicArticlesPageParser parser = new OggiScienzaBlogSingleTopicArticlesPageParser();
	
	@Override
	public List<AggregatorPost> parseResource(String resource, Date from, Date to) {
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		OggiScienzaBlogSingleTopicArticlesPageParserResponse parserResponse = parser.getPostsFromResource(resource, from, to).getWebsiteParserResponseObject();
		result.addAll(parserResponse.getWebsiteParserResponseObject());
		return result;
	}
	
}
