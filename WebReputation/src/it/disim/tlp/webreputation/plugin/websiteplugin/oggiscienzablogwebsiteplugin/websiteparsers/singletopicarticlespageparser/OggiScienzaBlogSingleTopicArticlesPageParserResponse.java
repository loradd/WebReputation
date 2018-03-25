package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singletopicarticlespageparser;

/** @author Lorenzo Addazi */

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.outlineparser.OggiScienzaBlogOutlinePageParserResponse;

import java.util.List;

public class OggiScienzaBlogSingleTopicArticlesPageParserResponse extends OggiScienzaBlogOutlinePageParserResponse{

	public OggiScienzaBlogSingleTopicArticlesPageParserResponse(List<AggregatorPost> parserResponse) {
		super(parserResponse);
	}

}
