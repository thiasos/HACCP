package it.lsoft.haccp;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class AppRootView extends AppRootDesign implements View {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5907646604589089811L;
	private final CarteFedeltaView fidelityCardView = new CarteFedeltaView();
	private final FornitoriView fornitoriView = new FornitoriView();
	private final RegistriView registriView = new RegistriView();
	private final ArticoliView articoliView = new ArticoliView();

	public AppRootView() {
		super();
		Command command = new Command() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 2688821413651446216L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				appContainer.removeAllComponents();
				switch (selectedItem.getText()) {
				case "Fornitori":
					appContainer.addComponent(fornitoriView);
					break;
				case "Registri":
					appContainer.addComponent(registriView);
					break;
				case "Articoli":
					appContainer.addComponent(articoliView);
					break;
				case "Carte Fedeltà":
					appContainer.addComponent(fidelityCardView);
					break;
				default:
					appContainer.addComponent(new Label(selectedItem.getText()));
					break;
				}
			}
		};
		for (MenuItem menuItem : menu.getItems()) {
			menuItem.setCommand(command);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
	}
}
