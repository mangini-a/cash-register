package view;

public interface PanelChangeListener {
	
	/**
	 * Ensures that panels can notify each other of changes, allowing them to refresh their data accordingly.
	 */
	void onItemChanged();
}
