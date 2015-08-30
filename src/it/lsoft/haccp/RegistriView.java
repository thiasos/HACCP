package it.lsoft.haccp;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.util.filter.Between;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

import it.lsoft.haccp.model.Registri;
import it.lsoft.haccp.model.Registri.TipoRegistroEnum;
import it.lsoft.haccp.model.RegistroCarico;
import it.lsoft.haccp.model.RegistroScarico;

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

	private final JPAContainer<Registri> documentiDS = JPAContainerFactory.make(Registri.class,
			HaccpUI.PERSISTENCE_UNIT);

	public RegistriView() {
		super();
		{
			calendar.setHeight("100px");
			calendar.setWidth("100%");
			documentiDS.sort(new String[] { "data" }, new boolean[] { true });
			setRows(2);
			addComponent(calendar, 0, 0);
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
				find = CollectionUtils.find(arrayList,
						new PredicateImplementation(event.getDate(), TipoRegistroEnum.S));
				if (find == null) {
					RegistroScarico entity2 = new RegistroScarico();
					entity2.setData(event.getDate());
					documentiDS.addEntity(entity2);
					calendar.markAsDirty();
				}
			});

		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
