package application;

import model.UserRole;
import view.HomeView;

public class DesktopApp {
	
	public static void main(String[] args) {
		try {
			HomeView homeView = new HomeView(UserRole.TEMP);
			homeView.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
