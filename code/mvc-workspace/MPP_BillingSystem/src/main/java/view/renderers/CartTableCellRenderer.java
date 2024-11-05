package view.renderers;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class CartTableCellRenderer extends DefaultTableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		// Center align the text
		setHorizontalAlignment(SwingConstants.CENTER); 
		
		// Check if the current column is the "Unit Price" column
        if (column == table.getColumnCount() - 1 && value instanceof Number) {
            // Format the value as currency in €
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY);
            String formattedValue = currencyFormat.format(value);
            setText(formattedValue);
        } else {
            // For other columns, just set the text as is
            setText(value != null ? value.toString() : "");
        }

        // Set the background color for selected rows
        if (isSelected) {
            cell.setBackground(Color.YELLOW);
            cell.setForeground(Color.BLACK);
        } else {
            cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternate row colors
            cell.setForeground(Color.BLACK);
        }
		
		return cell;
	}
}
