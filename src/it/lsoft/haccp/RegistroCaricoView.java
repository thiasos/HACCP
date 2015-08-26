package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import it.lsoft.haccp.model.Articoli;
import it.lsoft.haccp.model.ArticoliMagazzino;
import it.lsoft.haccp.model.RegistroCarico;

public class RegistroCaricoView extends RegistroCaricoDesign implements View {

	private static JPAContainer<Articoli> articoliDS;
	private static JPAContainer<ArticoliMagazzino> magazzinoDS;

	static {
		articoliDS = JPAContainerFactory.make(Articoli.class, HaccpUI.PERSISTENCE_UNIT);
		magazzinoDS = JPAContainerFactory.make(ArticoliMagazzino.class, HaccpUI.PERSISTENCE_UNIT);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6853807270909456615L;

	public RegistroCaricoView(final RegistroCarico carico) {
		tableMagazzino.setContainerDataSource(magazzinoDS);
		tableMagazzino.setVisibleColumns(new Object[] {"id","articolo","dataScadenza","lotto", "conformita","trasporto","validita","temperatura"});
		idArticolo.setContainerDataSource(articoliDS);
		idArticolo.setItemCaptionPropertyId("descrizione");
		ean.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				articoliDS.removeAllContainerFilters();
				articoliDS.applyFilters();
				if (org.apache.commons.lang3.StringUtils.isNotBlank( event.getText())) {
					articoliDS.addContainerFilter("ean", event.getText(), false, false);
					articoliDS.applyFilters();
					if (articoliDS.getItemIds().isEmpty()) {
						System.err.println("Error");
						ean.setValue("");
					} else if (articoliDS.getItemIds().size() == 1) {
						idArticolo.setValue(articoliDS.getIdByIndex(0));
					}
				}
			}
		});
		btnAdd.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				ArticoliMagazzino mm  = new ArticoliMagazzino();
				mm.setArticolo((Articoli) idArticolo.getValue());
				mm.setConformita(conformita.getValue());
				mm.setDataScadenza(dataScadenza.getValue());
				mm.setLotto(lotto.getValue());
				mm.setRegistroCarico(carico);
				mm.setTemperatura(Double.parseDouble(temperatura.getValue()));
				mm.setTrasporto(trasporto.getValue());
				mm.setValidita(validita.getValue());
			}
		});
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
