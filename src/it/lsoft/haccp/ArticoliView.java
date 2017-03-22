package it.lsoft.haccp;

import java.util.Locale;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import it.lsoft.haccp.model.Articoli;
import it.lsoft.haccp.model.Fornitori;

public class ArticoliView extends ArticoliDesign implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5250926951973168177L;

	private final FieldGroup fg = new FieldGroup();

	private final JPAContainer<Fornitori> fornitoriDS = JPAContainerFactory.make(Fornitori.class,
			HaccpUI.PERSISTENCE_UNIT);
	private final JPAContainer<Articoli> articoliDS = JPAContainerFactory.make(Articoli.class,
			HaccpUI.PERSISTENCE_UNIT);

	public ArticoliView(UserHandler userHandler) {
		super();
		form.setEnabled(false);
		tableArticoli.setSelectable(true);
		tableArticoli.setMultiSelect(false);
		tableArticoli.setContainerDataSource(articoliDS);
		tableArticoli.setVisibleColumns(new Object[] { "ean", "descrizione", "fornitore" });
		tableArticoli.setColumnHeader("ean", "EAN");
		tableArticoli.setColumnHeader("descrizione", "Descrizione");
		tableArticoli.setColumnHeader("fornitore", "Fornitore");
		tableArticoli.addItemClickListener(event -> {
			rebind(event.getItem());
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);

		});
		deleteButton.addClickListener(event -> {
			articoliDS.removeItem(fg.getItemDataSource().getItemProperty("id").getValue());
			rebind(new BeanItem<>(new Articoli()));
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		});
		editButton.addClickListener(event -> form.setEnabled(true));
		newButton.addClickListener(event -> {
			rebind(new BeanItem<>(new Articoli()));
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
				@SuppressWarnings("unchecked")
				Object oId = articoliDS.addEntity(((BeanItem<Articoli>) fg.getItemDataSource()).getBean());
				tableArticoli.select(oId);
			}
			form.setEnabled(false);
		});
		toolbar.setEnabled(true);
		editButton.setEnabled(false);
		deleteButton.setEnabled(false);
		fornitore.setConverter(new Converter<Object, Fornitori>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = -5545987960015929012L;

			@SuppressWarnings("rawtypes")
			@Override
			public Fornitori convertToModel(final Object value, Class targetType, Locale locale)
					throws ConversionException {
				Fornitori fornitori = new Fornitori();
				fornitori.setId((Integer) value);
				return fornitori;
			}

			@Override
			@SuppressWarnings("rawtypes")
			public Object convertToPresentation(Fornitori value, Class targetType, Locale locale)
					throws ConversionException {
				return value == null ? null : ((Fornitori) value).getId();
			}

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
			public Class getModelType() {
				return Fornitori.class;
			}

			@Override
			@SuppressWarnings({ "rawtypes", "unchecked" })
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

	}

}
