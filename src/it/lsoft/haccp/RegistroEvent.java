package it.lsoft.haccp;

import java.util.Date;

import com.vaadin.ui.components.calendar.event.CalendarEvent;

import it.lsoft.haccp.model.Registri;

public class RegistroEvent implements CalendarEvent {

	private final Registri item;

	public RegistroEvent(Registri item) {
		this.item = item;
	}

	@Override
	public Date getStart() {
		return getItem().getData();
	}

	@Override
	public Date getEnd() {
		return getItem().getData();
	}

	@Override
	public String getCaption() {
		return String.format("%s (%s)",  getItem().getTipoRegistro(), getItem().getMovimenti().size());
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getStyleName() {
		return getItem().getTipoRegistro().name();
	}

	@Override
	public boolean isAllDay() {
		return true;
	}

	public Registri getItem() {
		return item;
	}

}
