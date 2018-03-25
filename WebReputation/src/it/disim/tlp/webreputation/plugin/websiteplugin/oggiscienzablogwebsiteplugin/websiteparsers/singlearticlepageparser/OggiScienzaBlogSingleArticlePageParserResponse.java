package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.singlearticlepageparser;

/** @author Lorenzo Addazi */

import java.util.List;

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.outlineparser.OggiScienzaBlogOutlinePageParserResponse;

public class OggiScienzaBlogSingleArticlePageParserResponse extends OggiScienzaBlogOutlinePageParserResponse{

	public OggiScienzaBlogSingleArticlePageParserResponse(List<AggregatorPost> parserResponse) {
		super(parserResponse);
	}

}
