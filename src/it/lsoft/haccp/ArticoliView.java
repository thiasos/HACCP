package it.lsoft.haccp;

import java.util.Locale;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import it.lsoft.haccp.model.Articoli;
import it.lsoft.haccp.model.Fornitori;

<<<<<<< HEAD
public class ArticoliView extends ArticoliDesign implements View {
	private final FieldGroup fg = new FieldGroup();

	private final JPAContainer<Fornitori> fornitoriDS = JPAContainerFactory.make(Fornitori.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final JPAContainer<Articoli> articoliDS = JPAContainerFactory.make(Articoli.class,
			HaccpUI.PERSISTENCE_UNIT);

	public ArticoliView() {
		super();
		form.setEnabled(false);
		tableArticoli.setSelectable(true);
		tableArticoli.setMultiSelect(false);
		tableArticoli.setContainerDataSource(articoliDS);
		tableArticoli.setVisibleColumns(new Object[] { "ean", "descrizione", "fornitore" });
		tableArticoli.setColumnHeader("ean", "EAN");
		tableArticoli.setColumnHeader("descrizione", "Descrizione");
		tableArticoli.setColumnHeader("fornitore", "Fornitore");
		tableArticoli.addItemClickListener(new ItemClickListener() {

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
				articoliDS.removeItem(fg.getItemDataSource().getItemProperty("id").getValue());
				rebind(new BeanItem<>(new Articoli()));
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
			}
		});
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
				rebind(new BeanItem<>(new Articoli()));
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
					Object oId = articoliDS.addEntity(((BeanItem<Articoli>) fg.getItemDataSource()).getBean());
					tableArticoli.select(oId);
				}
				form.setEnabled(false);
			}
		});
		toolbar.setEnabled(true);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		fornitore.setConverter(new Converter() {

			@Override
			public Object convertToModel(final Object value, Class targetType, Locale locale)
					throws ConversionException {
				Fornitori fornitori = new Fornitori();
				fornitori.setId((Integer) value);
				return fornitori;
			}

			@Override
			public Object convertToPresentation(Object value, Class targetType, Locale locale)
					throws ConversionException {
				return value == null ? null : ((Fornitori) value).getId();
			}

			@Override
			public Class getModelType() {
				return Fornitori.class;
			}

			@Override
			public Class getPresentationType() {
				return Integer.class;
			}
		});
		fornitore.setContainerDataSource(fornitoriDS);
	}

	private void rebind(Item itemDataSource) {
		fg.setItemDataSource(itemDataSource);
		fg.bind(ean, "ean");
		fg.bind(descrizione, "descrizione");
		fg.bind(fornitore, "fornitore");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

=======
public class ArticoliView extends ArticoliDesign implements View{
	private static JPAContainer<Articoli> articoliDS;
	private static JPAContainer<Fornitori> fornitoriDS;
	private final FieldGroup fg = new FieldGroup();

	static {
		fornitoriDS = JPAContainerFactory.make(Fornitori.class, HaccpUI.PERSISTENCE_UNIT);
		articoliDS = JPAContainerFactory.make(Articoli.class, HaccpUI.PERSISTENCE_UNIT);
	}
	
	public ArticoliView() {
		super();
		form.setEnabled(false);
		tableArticoli.setSelectable(true);
		tableArticoli.setMultiSelect(false);
		tableArticoli.setContainerDataSource(articoliDS);
		tableArticoli.setVisibleColumns(new Object[] { "ean", "descrizione", "fornitore" });
		tableArticoli.setColumnHeader("ean", "EAN");
		tableArticoli.setColumnHeader("descrizione", "Descrizione");
		tableArticoli.setColumnHeader("fornitore", "Fornitore");
		tableArticoli.addItemClickListener(new ItemClickListener() {

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
				articoliDS.removeItem(	fg.getItemDataSource().getItemProperty("id").getValue());
				rebind(new BeanItem<>(new Articoli()));
				editButton.setEnabled(false);
				deleteButton.setEnabled(false);
			}});
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
				rebind(new BeanItem<>(new Articoli()));
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
					Object oId = articoliDS.addEntity(((BeanItem<Articoli>) fg.getItemDataSource()).getBean());
					tableArticoli.select(oId);
				}  
				form.setEnabled(false);
			}
		});
		toolbar.setEnabled(true);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		fornitore.setConverter(new Converter() {

			@Override
			public Object convertToModel(final Object value, Class targetType, Locale locale) throws ConversionException {
				Fornitori fornitori = new Fornitori();
				fornitori.setId((Integer) value);
				return fornitori ;
			}

			@Override
			public Object convertToPresentation(Object value, Class targetType, Locale locale)
					throws ConversionException {
				return value== null?null: ((Fornitori) value).getId();
			}

			@Override
			public Class getModelType() {
				return Fornitori.class;
			}

			@Override
			public Class getPresentationType() {
				return Integer.class;
			}
		});
		fornitore.setContainerDataSource(fornitoriDS);
	}
	
	private void rebind(Item itemDataSource) {
		fg.setItemDataSource(itemDataSource);
		fg.bind(ean, "ean");
		fg.bind(descrizione, "descrizione");
		fg.bind(fornitore, "fornitore");
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
>>>>>>> branch 'develop' of https://github.com/thiasos/HACCP.git
	}

}
