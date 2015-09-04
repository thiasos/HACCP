package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import it.lsoft.haccp.model.Articoli;
import it.lsoft.haccp.model.ArticoliMagazzino;
import it.lsoft.haccp.model.RegistroCarico;

public class RegistroCaricoView extends RegistroCaricoDesign implements View {

	JPAContainer<Articoli> articoliDS = JPAContainerFactory.make(Articoli.class, HaccpUI.PERSISTENCE_UNIT);
	JPAContainer<ArticoliMagazzino> magazzinoDS = JPAContainerFactory.make(ArticoliMagazzino.class,
			HaccpUI.PERSISTENCE_UNIT);

	/**
	 * 
	 */
	private static final long serialVersionUID = -6853807270909456615L;

	public RegistroCaricoView(final RegistroCarico carico) {
		magazzinoDS.removeAllContainerFilters();
		magazzinoDS.addContainerFilter(new Compare.Equal("registroCarico", carico));
		tableMagazzino.setContainerDataSource(magazzinoDS);
		tableMagazzino.setVisibleColumns(new Object[] { "id", "articolo", "dataScadenza", "lotto", "conformita",
				"trasporto", "validita", "temperatura" });
		idArticolo.setContainerDataSource(articoliDS);
		idArticolo.setItemCaptionPropertyId("descrizione");
		ean.addTextChangeListener(event -> {
			articoliDS.removeAllContainerFilters();
			articoliDS.applyFilters();
			if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getText())) {
				articoliDS.addContainerFilter("ean", event.getText(), false, false);
				articoliDS.applyFilters();
				if (articoliDS.getItemIds().isEmpty()) {
					System.err.println("Error");
					ean.setValue("");
				} else if (articoliDS.getItemIds().size() == 1) {
					idArticolo.setValue(articoliDS.getIdByIndex(0));
				}
			}
		});
		btnAdd.addClickListener(event -> {
			ArticoliMagazzino mm = new ArticoliMagazzino();
			Articoli a = new Articoli();
			a.setId((Integer) idArticolo.getValue());
			mm.setArticolo(a);
			mm.setConformita(conformita.getValue());
			mm.setDataScadenza(dataScadenza.getValue());
			mm.setLotto(lotto.getValue());
			mm.setRegistroCarico(carico);
			mm.setTemperatura(Double.parseDouble(temperatura.getValue()));
			mm.setTrasporto(trasporto.getValue());
			mm.setValidita(validita.getValue());
			magazzinoDS.addEntity(mm);
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
