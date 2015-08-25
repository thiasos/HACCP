package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import it.lsoft.haccp.model.Fornitori;

public class FornitoriView extends FornitoriDesign implements View {
	private static JPAContainer<Fornitori> fornitoriDS;

	static {
		fornitoriDS = JPAContainerFactory.make(Fornitori.class, HaccpUI.PERSISTENCE_UNIT);
	}

	public FornitoriView() {
		super();
		tableFornitori.setContainerDataSource(fornitoriDS);
		tableFornitori.setVisibleColumns(new Object[] { "name", "address", "phone" });
		tableFornitori.setColumnHeader("name", "Nome");
		tableFornitori.setColumnHeader("address", "Indirizzo");
		tableFornitori.setColumnHeader("phone", "Telefono");
		newButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -5789956889812422252L;

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub

			}
		});
		toolbar.setEnabled(true);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
