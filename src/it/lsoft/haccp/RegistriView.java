package it.lsoft.haccp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.management.RuntimeErrorException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Between;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page;
import com.vaadin.server.ResourceReference;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

import it.lsoft.haccp.model.Registri;
import it.lsoft.haccp.model.Registri.TipoRegistroEnum;
import it.lsoft.haccp.model.RegistroCarico;
import it.lsoft.haccp.model.RegistroScarico;
import it.lsoft.haccp.model.VCaricoStampa;
import it.lsoft.haccp.model.VScaricoStampa;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class RegistriView extends RegistriDesign implements View {
	private final Calendar calendar = new Calendar();

	private final class PredicateImplementation implements Predicate {
		private final Date date;
		private final TipoRegistroEnum reg;

		private PredicateImplementation(Date date, TipoRegistroEnum reg) {
			this.date = date;
			this.reg = reg;
		}

		@Override
		public boolean evaluate(Object object) {
			Registri r = (Registri) object;
			return r.getData().equals(date) && r.getTipoRegistro() == reg;
		}
	}

	private final JPAContainer<VScaricoStampa> printScaricoDS = JPAContainerFactory.make(VScaricoStampa.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final JPAContainer<VCaricoStampa> printCaricoDS = JPAContainerFactory.make(VCaricoStampa.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final JPAContainer<Registri> documentiDS = JPAContainerFactory.make(Registri.class,
			HaccpUI.PERSISTENCE_UNIT);

	public RegistriView(UserHandler userHandler) {
		super();
		calendar.setHeight("100px");
		calendar.setWidth("100%");
		documentiDS.sort(new String[] { "data" }, new boolean[] { true });
		setRows(2);
		Button btnPrintCarico = new Button("Stampa R. Carico");
		Button btnPrintScarico = new Button("Stampa R. Scarico");
		addComponent(new HorizontalLayout(calendar, new VerticalLayout(btnPrintCarico, btnPrintScarico)), 0, 0);
		calendar.setEventProvider((startDate, endDate) -> {
			documentiDS.removeAllContainerFilters();
			documentiDS.addContainerFilter(new Between("data", startDate, endDate));
			documentiDS.applyFilters();
			ArrayList<CalendarEvent> arrayList = new ArrayList<>();
			for (Object itemId : documentiDS.getItemIds()) {
				arrayList.add(new RegistroEvent(documentiDS.getItem(itemId).getEntity()));
			}
			return arrayList;
		});
		calendar.setFirstVisibleHourOfDay(0);
		calendar.setLastVisibleHourOfDay(0);
		calendar.setHandler((EventClickHandler) event -> {
			RegistroEvent e = (RegistroEvent) event.getCalendarEvent();
			Component component;
			removeComponent(0, 1);
			switch (e.getItem().getTipoRegistro()) {
			case C:
				component = new RegistroCaricoView((RegistroCarico) e.getItem());
				break;
			case S:
				component = new RegistroScaricoView((RegistroScarico) e.getItem());
				break;
			default:
				component = new Label(e.getItem().getTipoRegistro().toString());
				break;
			}
			addComponent(component, 0, 1);
			setRowExpandRatio(1, 10f);
		});
		calendar.setHandler((DateClickHandler) event -> {
			ArrayList<Registri> arrayList = new ArrayList<>();
			for (Object itemId : documentiDS.getItemIds()) {
				arrayList.add(documentiDS.getItem(itemId).getEntity());
			}
			Object find = CollectionUtils.find(arrayList,
					new PredicateImplementation(event.getDate(), TipoRegistroEnum.C));
			if (find == null) {
				RegistroCarico entity1 = new RegistroCarico();
				entity1.setData(event.getDate());
				documentiDS.addEntity(entity1);
				calendar.markAsDirty();
			}
			find = CollectionUtils.find(arrayList, new PredicateImplementation(event.getDate(), TipoRegistroEnum.S));
			if (find == null) {
				RegistroScarico entity2 = new RegistroScarico();
				entity2.setData(event.getDate());
				documentiDS.addEntity(entity2);
				calendar.markAsDirty();
			}
		});
		btnPrintCarico.addClickListener(event -> printCarico(calendar.getStartDate(), calendar.getEndDate()));
		btnPrintScarico.addClickListener(event -> printScarico(calendar.getStartDate(), calendar.getEndDate()));
	}

	static final String MYKEY = "download";

	private void printCarico(Date startDate, Date endDate) {
		printCaricoDS.sort(new Object[] { "registriData" }, new boolean[] { true });
		printCaricoDS.removeAllContainerFilters();
		printCaricoDS.addContainerFilter(new Between("registriData", startDate, endDate));
		printCaricoDS.applyFilters();
		ArrayList<VCaricoStampa> arr = new ArrayList<>();
		for (Object itemId : printCaricoDS.getItemIds()) {
			arr.add(printCaricoDS.getItem(itemId).getEntity());
		}
		JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(arr.toArray(new VCaricoStampa[0]), false);
		String reportName = "CaricoLS.jasper";
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(reportName);
		StreamResource resource = printReport(resourceAsStream, dataSource);
		setResource(MYKEY, resource);
		ResourceReference rr = ResourceReference.create(resource, this, MYKEY);
		Page.getCurrent().open(rr.getURL(), "new");
	}

	private void printScarico(Date startDate, Date endDate) {
		printScaricoDS.sort(new Object[] { "registriData" }, new boolean[] { true });
		printScaricoDS.removeAllContainerFilters();
		printScaricoDS.addContainerFilter(new Between("registriData", startDate, endDate));
		printScaricoDS.applyFilters();
		ArrayList<VScaricoStampa> arr = new ArrayList<>();
		for (Object itemId : printScaricoDS.getItemIds()) {
			arr.add(printScaricoDS.getItem(itemId).getEntity());
		}
		JRBeanArrayDataSource dataSource = new JRBeanArrayDataSource(arr.toArray(new VScaricoStampa[0]), false);
		String reportName = "ScaricoLS.jasper";
		InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(reportName);
		StreamResource resource = printReport(resourceAsStream, dataSource);
		setResource(MYKEY, resource);
		ResourceReference rr = ResourceReference.create(resource, this, MYKEY);
		Page.getCurrent().open(rr.getURL(), "new");
	}

	public static StreamResource printReport(InputStream report, JRBeanArrayDataSource dataSource) {
		try {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(report, new HashMap<>(), dataSource);
			JasperExportManager.exportReportToPdfStream(jprint, outputStream);
			outputStream.flush();
			outputStream.close();
			final ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			StreamResource res = new StreamResource(new StreamResource.StreamSource() {

				public InputStream getStream() {
					return new ByteArrayInputStream(outputStream.toByteArray());
				}
			}, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".pdf");
			return res;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
