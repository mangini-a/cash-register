package view.renderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class AccountingTableCellRenderer extends DefaultTableCellRenderer {

	private final int loggedManagerId;

	public AccountingTableCellRenderer(int loggedManagerId) {
        this.loggedManagerId = loggedManagerId;
    }

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		setHorizontalAlignment(SwingConstants.CENTER); // Center align the text

		// Get the user ID from the table model
		Integer userId = (Integer) table.getValueAt(row, 1); // The ID is in the second column

		// Highlight the row if it matches the logged-in manager's ID
		if (userId != null && userId == loggedManagerId) {
			cell.setBackground(new Color(173, 216, 230)); // Light blue for highlighting
		} else {
			cell.setBackground(Color.WHITE); // Default background
		}

		return cell;
	}
}
