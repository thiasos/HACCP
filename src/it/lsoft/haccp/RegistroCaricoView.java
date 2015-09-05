package it.lsoft.haccp;

import java.io.InputStream;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;

import it.lsoft.haccp.model.Articoli;
import it.lsoft.haccp.model.ArticoliMagazzino;
import it.lsoft.haccp.model.Etichette;
import it.lsoft.haccp.model.RegistroCarico;
import it.lsoft.haccp.model.VCaricoStampa;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

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
		btnPrint.addClickListener(event -> printAll(carico.getId()));
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

	private void printAll(Integer rid) {
		Query query = JPAContainerFactory.createEntityManagerForPersistenceUnit(HaccpUI.PERSISTENCE_UNIT)
				.createNativeQuery("SELECT haccp.articoli.`DESCRIZIONE` as descrizione,"
						+ " haccp.articolimagazzino.`LOTTO` as lotto,"
						+ " haccp.articolimagazzino.`DATASCADENZA` as dataScadenza,"
						+ " haccp.registri.`DATA` as dataRegistro," + " haccp.articolimagazzino.`ID` as id"
						+ " FROM haccp.articoli" + " INNER JOIN haccp.articolimagazzino ON "
						+ " haccp.articolimagazzino.`ARTICOLO_ID` = haccp.articoli.`ID` "
						+ " INNER JOIN haccp.registri ON "
						+ " haccp.registri.`ID` = haccp.articolimagazzino.`REGISTROCARICO_ID` "
						+ "where haccp.articolimagazzino.`REGISTROCARICO_ID`  = "+rid, "Etichette");
		JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(query.getResultList().toArray(new Etichette[0]),
				false);
		String reportName = "Etichette.jasper";
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(reportName);
		StreamResource resource = RegistriView.printReport(resourceAsStream, dataSource);
		setResource(RegistriView.MYKEY, resource);
		ResourceReference rr = ResourceReference.create(resource, this, RegistriView.MYKEY);
		Page.getCurrent().open(rr.getURL(), "new");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
