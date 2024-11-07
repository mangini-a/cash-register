package view.renderers;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import view.colors.AppColors;

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
		
		// Center align the text
		setHorizontalAlignment(SwingConstants.CENTER); 
		
		// Customize selected rows' color
		if (isSelected) {
			cell.setForeground(Color.BLACK);
		} else {
			cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternate row colors
		}
		
		// Check if the current column is the "Amount" column
        if (column == table.getColumnCount() - 1 && value instanceof Number) {
            // Format the value as currency in €
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
            String formattedValue = currencyFormat.format(value);
            setText(formattedValue);
        } else {
            // For other columns, just set the text as is
            setText(value != null ? value.toString() : "");
        }

		// Get the user ID from the table model
		Integer userId = (Integer) table.getValueAt(row, 1); // The ID is in the second column

		// Highlight the row if it matches the logged-in manager's ID
		if (userId != null && userId == loggedManagerId) {
			cell.setBackground(AppColors.HIGHLIGHT_COLOR); // Light teal for highlighting
			cell.setForeground(Color.WHITE);
		} else {
			cell.setBackground(Color.WHITE); // Default background
			cell.setForeground(Color.BLACK);
		}

		return cell;
	}
}
