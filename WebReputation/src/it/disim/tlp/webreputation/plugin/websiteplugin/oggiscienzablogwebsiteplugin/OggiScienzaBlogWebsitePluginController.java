package it.disim.tlp.webreputation.plugin.websiteplugin.oggiscienzablogwebsiteplugin;

/** @author Lorenzo Addazi */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.disim.tlp.webreputation.exceptions.pluginexceptions.QuotaExceededException;
import it.disim.tlp.webreputation.plugin.model.aggregatorplugin.AggregatorPost;
import it.disim.tlp.webreputation.plugin.model.websiteplugin.WebsitePlugin;

public abstract class OggiScienzaBlogWebsitePluginController implements WebsitePlugin {
	
	public List<AggregatorPost> getPosts(String resource, String type, Date from) throws QuotaExceededException {
		/* Lista di AggregatorPost risultante */
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		/* la particolare implementazione di parseResource coinvolta dipende dal contesto a runtime */
		for(AggregatorPost aggregatorPost : this.parseResource(resource, from, null)){
			/* verifica della data di aggregatorPost */
			if(aggregatorPost.getDate().after(from)){
				/* aggiungo aggregatorPost al risultato se la data ricade nel range richiesto */
				result.add(aggregatorPost);
			}
		}
		/* lista finale di istanze di AggregatorPost ottenute */
		return result;
	}
	public List<AggregatorPost> getPosts(String resource, String type,Date from, Date to) throws QuotaExceededException {
		/* Lista di AggregatorPost risultante */
		List<AggregatorPost> result = new ArrayList<AggregatorPost>();
		/* la particolare implementazione di parseResource coinvolta dipende dal contesto a runtime */
		for(AggregatorPost aggregatorPost : this.parseResource(resource, from, to)){
			/* verifica della data di AggregatorPost */
			if(aggregatorPost.getDate().after(from) && aggregatorPost.getDate().before(to)){
				/* aggiungo aggregatorpost al risultato se la data ricade nel range richiesto */
				result.add(aggregatorPost);
			}
		}
		/* lista finale di istanze di AggregatorPost ottenute */
		return result;
	}

}
