package it.lsoft.haccp;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class AppRootView extends AppRootDesign implements View {
	private final CarteFedeltaView fidelityCardView = new CarteFedeltaView();
	private final FornitoriView fornitoriView = new FornitoriView();
	private final RegistriView registriView = new RegistriView();
	private final ArticoliView articoliView = new ArticoliView();

	public AppRootView() {
		super();
		Command command = new Command() {

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
				case "Carte Fedelt�":
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
		// TODO Auto-generated method stub

	}
}
