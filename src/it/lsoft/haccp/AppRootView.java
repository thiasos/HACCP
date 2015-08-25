package it.lsoft.haccp;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class AppRootView extends AppRootDesign implements View {
	public AppRootView() {
		super();
		Command command = new Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				switch (selectedItem.getText()) {
				case "Fornitori":
					appContainer.removeAllComponents();
					appContainer.addComponent(new FornitoriView());
					break;

				default:
					appContainer.removeAllComponents();
					appContainer.addComponent(new Label("aaa"));
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
