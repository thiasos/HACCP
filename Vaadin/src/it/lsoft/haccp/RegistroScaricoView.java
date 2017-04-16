package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Button.ClickEvent;

import it.lsoft.haccp.model.Articoli;
import it.lsoft.haccp.model.ArticoliMagazzino;
import it.lsoft.haccp.model.RegistroCarico;
import it.lsoft.haccp.model.RegistroScarico;

public class RegistroScaricoView extends RegistroScaricoDesign implements View {

	private static JPAContainer<ArticoliMagazzino> articoliDS;
	private static JPAContainer<ArticoliMagazzino> magazzinoDS;

	static {
		articoliDS = JPAContainerFactory.make(ArticoliMagazzino.class, HaccpUI.PERSISTENCE_UNIT);
		magazzinoDS = JPAContainerFactory.make(ArticoliMagazzino.class, HaccpUI.PERSISTENCE_UNIT);

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6853807270909456615L;

	public RegistroScaricoView(final RegistroScarico registroScarico) {
		magazzinoDS.removeAllContainerFilters();
		magazzinoDS.addContainerFilter(new Compare.Equal("registroScarico", registroScarico));
		tableMagazzino.setContainerDataSource(magazzinoDS);
		tableMagazzino.setVisibleColumns(new Object[] { "id", "articolo", "dataScadenza", "lotto", });
		reapplyFiter();
		movimento.setContainerDataSource(articoliDS);
		movimento.setItemCaptionMode(ItemCaptionMode.ITEM);

		idMovimento.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				reapplyFiter();
				if (org.apache.commons.lang3.StringUtils.isNotBlank(event.getText())) {
					articoliDS.addContainerFilter("id", event.getText(), false, false);
					articoliDS.applyFilters();
					if (articoliDS.getItemIds().isEmpty()) {
						System.err.println("Error");
						idMovimento.setValue("");
					} else if (articoliDS.getItemIds().size() == 1) {
						movimento.setValue(articoliDS.getIdByIndex(0));
					}
				}
			}
		});
		btnAdd.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				articoliDS.getItem(	movimento.getValue()).getItemProperty("registroScarico").setValue(registroScarico);
				magazzinoDS.refresh();
				reapplyFiter();
			}
		});
	}

	private void reapplyFiter() {
		articoliDS.removeAllContainerFilters();
		articoliDS.addContainerFilter(new IsNull("registroScarico"));
		articoliDS.applyFilters();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
