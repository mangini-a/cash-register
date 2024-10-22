package application;

import view.HomeView;

public class DesktopApp {
	
	public static void main(String[] args) {
		try {
			HomeView homeView = new HomeView();
			homeView.display();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
