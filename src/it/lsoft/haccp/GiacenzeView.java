package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;

import it.lsoft.haccp.model.ArticoliMagazzino;

public class GiacenzeView extends GiacenzeDesign {

	private final UserHandler userHandler;
	private final JPAContainer<ArticoliMagazzino> magazzinoDS = JPAContainerFactory.make(ArticoliMagazzino.class,
			HaccpUI.PERSISTENCE_UNIT);
	public GiacenzeView(UserHandler userHandler) {
		super();
		this.userHandler = userHandler;
		table.setContainerDataSource(magazzinoDS);
		table.setVisibleColumns(new Object[] { "id", "articolo", "dataScadenza", "lotto", "conformita",
				"trasporto", "validita", "temperatura" });
		magazzinoDS.addContainerFilter(new IsNull("registroScarico"));

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9143604269793607535L;

}
