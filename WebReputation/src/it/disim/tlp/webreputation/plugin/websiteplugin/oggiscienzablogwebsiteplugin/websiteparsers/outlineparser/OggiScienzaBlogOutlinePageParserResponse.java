package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin.websiteparsers.outlineparser;

/** @author Lorenzo Addazi */

import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.model.websiteplugin.websiteparser.WebsiteParserResponse;

import java.util.List;

public class OggiScienzaBlogOutlinePageParserResponse extends WebsiteParserResponse<List<AggregatorPost>> {

	public OggiScienzaBlogOutlinePageParserResponse(List<AggregatorPost> parserResponse) {
		super(parserResponse);
	}

}
