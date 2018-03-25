package it.disim.tlp.webreputation.plugin.model.websiteplugin.websiteparser;

/** @author Lorenzo Addazi */

import java.util.Date;

public interface WebsiteParser<RESPONSE_OBJECT_TYPE> {
	
	public WebsiteParserResponse<RESPONSE_OBJECT_TYPE> getPostsFromResource(String resource, Date from, Date to);
	
}
