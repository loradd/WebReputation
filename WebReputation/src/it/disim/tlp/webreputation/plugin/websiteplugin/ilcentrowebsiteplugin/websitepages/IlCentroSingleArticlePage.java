package it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin.websitepages;

/** @author Aldo D'Eramo */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin.IlCentroWebsitePluginController;
import it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin.websiteparsers.singlearticlepageparser.IlCentroSingleArticlePageParser;
import it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin.websiteparsers.singlearticlepageparser.IlCentroSingleArticlePageParserResponse;

public class IlCentroSingleArticlePage extends IlCentroWebsitePluginController { 
		
		IlCentroSingleArticlePageParser parser = new IlCentroSingleArticlePageParser();
	
		@Override
		public List<AggregatorPost> parseResource(String resource, Date from, Date to) {
			List<AggregatorPost> result = new ArrayList<AggregatorPost>();
			IlCentroSingleArticlePageParserResponse parserResponse = parser.getPostsFromResource(resource, from, to).getWebsiteParserResponseObject();
			result.addAll(parserResponse.getWebsiteParserResponseObject());
			return result;
		}
}