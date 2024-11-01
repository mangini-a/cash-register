package view;

public interface PanelChangeListener {
	
	/**
	 * Ensures that the Stock tab's panels can notify each other of changes.
	 * This allows them to refresh their data accordingly.
	 */
	void onItemChanged();
	
	/**
	 * Ensures that the Staff tab's panels can notify each other of changes.
	 * This allows them to refresh their data accordingly.
	 */
	void onUserChanged();
}
