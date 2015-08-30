package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import it.lsoft.haccp.model.Fornitori;

public class FornitoriView extends FornitoriDesign implements View {
	private final JPAContainer<Fornitori> fornitoriDS = JPAContainerFactory.make(Fornitori.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final FieldGroup fg = new FieldGroup();

	public FornitoriView() {
		super();
		form.setEnabled(false);
		tableFornitori.setSelectable(true);
		tableFornitori.setMultiSelect(false);
		tableFornitori.setContainerDataSource(fornitoriDS);
		tableFornitori.setVisibleColumns(new Object[] { "nome", "indirizzo", "telefono" });
		tableFornitori.setColumnHeader("nome", "Nome");
		tableFornitori.setColumnHeader("indirizzo", "Indirizzo");
		tableFornitori.setColumnHeader("telefono", "Telefono");
		tableFornitori.addItemClickListener(event -> {
			rebind(event.getItem());
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);

		});
		deleteButton.addClickListener(event -> {
			fornitoriDS.removeItem(fg.getItemDataSource().getItemProperty("id").getValue());
			rebind(new BeanItem<>(new Fornitori()));
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		});
 
		editButton.addClickListener(event -> form.setEnabled(true));
		newButton.addClickListener(event -> {
			rebind(new BeanItem<>(new Fornitori()));
			form.setEnabled(true);
		});
		btnSave.addClickListener(event -> {
			try {
				fg.commit();
			} catch (CommitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (fg.getItemDataSource().getItemProperty("id").getValue() == null) {
				Object oId = fornitoriDS.addEntity(((BeanItem<Fornitori>) fg.getItemDataSource()).getBean());
				tableFornitori.select(oId);
			}
			form.setEnabled(false);
		});toolbar.setEnabled(true);editButton.setEnabled(false);deleteButton.setEnabled(false);

	}

	private void rebind(Item itemDataSource) {
		fg.setItemDataSource(itemDataSource);
		fg.bind(nome, "nome");
		fg.bind(indirizzo, "indirizzo");
		fg.bind(telefono, "telefono");
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

}
