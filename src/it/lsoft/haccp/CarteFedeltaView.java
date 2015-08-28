package it.lsoft.haccp;

import java.util.Date;

import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.IsNull;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import it.lsoft.haccp.model.CarteFedelta;
import it.lsoft.haccp.model.MovimentiFedelta;

public class CarteFedeltaView extends CarteFedeltaDesign implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5928556157312268110L;
	private static JPAContainer<CarteFedelta> carteDS;
	private static JPAContainer<MovimentiFedelta> movimentiDS;
	private final FieldGroup fg = new FieldGroup();

	static {
		carteDS = JPAContainerFactory.make(CarteFedelta.class, HaccpUI.PERSISTENCE_UNIT);
		movimentiDS = JPAContainerFactory.make(MovimentiFedelta.class, HaccpUI.PERSISTENCE_UNIT);
	}

	public CarteFedeltaView() {
		super();

		saldo.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		saldo.setReadOnly(true);
		table.setContainerDataSource(movimentiDS);
		table.setVisibleColumns(new Object[] { "data", "descrizione", "punti" });
		point.setPropertyDataSource(new ObjectProperty<Integer>(0, Integer.class));
		movimentiDS.addItemSetChangeListener(new ItemSetChangeListener() {
			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				Integer saldoValue = 0;
				for (Object itemId : movimentiDS.getItemIds()) {
					saldoValue += movimentiDS.getItem(itemId).getEntity().getPunti();
				}
				saldo.getPropertyDataSource().setValue(saldoValue);
				btnGift.setEnabled(saldoValue >= 10);
			}
		});
		movimentiDS.addContainerFilter(new IsNull("carta"));
		movimentiDS.applyFilters();
		cardNumber.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				carteDS.removeAllContainerFilters();
				carteDS.addContainerFilter("barCode", event.getText(), false, false);
				carteDS.applyFilters();
				movimentiDS.removeAllContainerFilters();
				if (carteDS.getItemIds().isEmpty()) {
					email.setReadOnly(true);
					email.setPropertyDataSource(new ObjectProperty<String>("Carta non associata"));
					movimentiDS.addContainerFilter(new IsNull("carta"));
				} else {
					email.setReadOnly(false);
					email.setPropertyDataSource(carteDS.getItem(carteDS.getIdByIndex(0)).getItemProperty("email"));
					movimentiDS.addContainerFilter(
							new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
				}
				movimentiDS.applyFilters();
			}
		});
		btnAddPoint.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (carteDS.getItemIds().isEmpty()) {
					CarteFedelta entity = new CarteFedelta();
					entity.setBarCode(cardNumber.getValue());
					Object addEntity = carteDS.addEntity(entity);
					carteDS.applyFilters();
					email.setValue(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity().getEmail());

				}
				Integer value = (Integer) point.getPropertyDataSource().getValue();
				if (value != 0) {
					MovimentiFedelta entity2 = new MovimentiFedelta();
					entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
					entity2.setDescrizione("Accredito Punti");
					entity2.setData(new Date());
					entity2.setPunti(value);
					movimentiDS.addEntity(entity2);
					movimentiDS.removeAllContainerFilters();
					movimentiDS.addContainerFilter(
							new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
					movimentiDS.applyFilters();
					point.getPropertyDataSource().setValue(0);
				}

			}
		});

		btnGift.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				MovimentiFedelta entity2 = new MovimentiFedelta();
				entity2.setCarta(carteDS.getItem(carteDS.getIdByIndex(0)).getEntity());
				entity2.setDescrizione("Ritiro Omaggio");
				entity2.setData(new Date());
				entity2.setPunti(-10);
				movimentiDS.addEntity(entity2);
				movimentiDS.removeAllContainerFilters();
				movimentiDS
						.addContainerFilter(new Equal("carta", carteDS.getItem(carteDS.getIdByIndex(0)).getEntity()));
				movimentiDS.applyFilters();
			}
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub

	}

}
