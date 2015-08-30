package it.lsoft.haccp;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import it.lsoft.haccp.model.Fornitori;

public class FornitoriView extends FornitoriDesign implements View {
<<<<<<< HEAD
	private final JPAContainer<Fornitori> fornitoriDS = JPAContainerFactory.make(Fornitori.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final FieldGroup fg = new FieldGroup();
=======
	private static JPAContainer<Fornitori> fornitoriDS;
	private final FieldGroup fg = new FieldGroup();

	static {
		fornitoriDS = JPAContainerFactory.make(Fornitori.class, HaccpUI.PERSISTENCE_UNIT);
	}
>>>>>>> branch 'develop' of https://github.com/thiasos/HACCP.git

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
		tableFornitori.addItemClickListener(new ItemClickListener() {

			@Override
			public void itemClick(ItemClickEvent event) {
				rebind(event.getItem());
				editButton.setEnabled(true);
				deleteButton.setEnabled(true);

			}
		});
		deleteButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
<<<<<<< HEAD
				fornitoriDS.removeItem(fg.getItemDataSource().getItemProperty("id").getValue());
				rebind(new BeanItem<>(new Fornitori()));
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
			}
		});
=======
				fornitoriDS.removeItem(	fg.getItemDataSource().getItemProperty("id").getValue());
				rebind(new BeanItem<>(new Fornitori()));
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
			}});
>>>>>>> branch 'develop' of https://github.com/thiasos/HACCP.git
		editButton.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				form.setEnabled(true);
			}
		});
		newButton.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -5789956889812422252L;

			@Override
			public void buttonClick(ClickEvent event) {
				rebind(new BeanItem<>(new Fornitori()));
				form.setEnabled(true);
			}

		});
		btnSave.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fg.commit();
				} catch (CommitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (fg.getItemDataSource().getItemProperty("id").getValue() == null) {
					Object oId = fornitoriDS.addEntity(((BeanItem<Fornitori>) fg.getItemDataSource()).getBean());
					tableFornitori.select(oId);
<<<<<<< HEAD
				}
=======
				}  
>>>>>>> branch 'develop' of https://github.com/thiasos/HACCP.git
				form.setEnabled(false);
			}
		});
		toolbar.setEnabled(true);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
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
