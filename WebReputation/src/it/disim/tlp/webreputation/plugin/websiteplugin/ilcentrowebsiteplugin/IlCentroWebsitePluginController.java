package it.disim.tlp.webreputation.plugin.websiteplugin.ilcentrowebsiteplugin;

/** @author Aldo D'Eramo */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.model.websiteplugin.WebsitePlugin;


public abstract class IlCentroWebsitePluginController implements WebsitePlugin {
	
	@Override
	public List<AggregatorPost> getPosts(String resource, String type, Date from) throws QuotaExceededException {
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		result.addAll(this.parseResource(resource, from, null));
		return result;
	}

	@Override
	public List<AggregatorPost> getPosts(String resource, String type, Date from, Date to) throws QuotaExceededException {
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		result.addAll(this.parseResource(resource, from, to));
		return result;
	}

}
