package it.disim.tlp.webreputation.plugin.model.websiteplugin.websiteparser;

/** @author Lorenzo Addazi */

public class WebsiteParserResponse<RESPONSE_OBJECT_TYPE> {
	
	private RESPONSE_OBJECT_TYPE parserResponse;
	
	public WebsiteParserResponse(RESPONSE_OBJECT_TYPE parserResponse){
		super();
		this.parserResponse = parserResponse;
	}
	
	public RESPONSE_OBJECT_TYPE getWebsiteParserResponseObject(){
		return this.parserResponse;
	}
	
}
