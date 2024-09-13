package ui.inventario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import ui.MainFrame;

@SuppressWarnings("serial")
public class GestisciInventarioPanel extends JPanel {
	
	private JPanel mainPanel;
	private CardLayout cardLayout;
	private JPanel aggiungiProdottoPanel;
	private JPanel eliminaProdottoPanel;
	private JPanel modificaProdottoPanel;
	private JPanel visualizzaProdottiPanel;
	
	public GestisciInventarioPanel(MainFrame mainFrame) {
		setLayout(new BorderLayout());

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        aggiungiProdottoPanel = new AggiungiProdottoPanel(this);
        eliminaProdottoPanel = new EliminaProdottoPanel(this);
        modificaProdottoPanel = new ModificaProdottoPanel(this);
        visualizzaProdottiPanel = new VisualizzaProdottiPanel(this);

        mainPanel.add(aggiungiProdottoPanel, "Aggiungi Prodotto");
        mainPanel.add(eliminaProdottoPanel, "Elimina Prodotto");
        mainPanel.add(modificaProdottoPanel, "Modifica Prodotto");
        mainPanel.add(visualizzaProdottiPanel, "Visualizza Prodotti");

        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        JButton aggiungiProdottoButton = new JButton("Aggiungi Prodotto");
        JButton eliminaProdottoButton = new JButton("Elimina Prodotto");
        JButton modificaProdottoButton = new JButton("Modifica Prodotto");
        JButton visualizzaProdottiButton = new JButton("Visualizza Prodotti");

        aggiungiProdottoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Aggiungi Prodotto");
            }
        });

        eliminaProdottoButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		cardLayout.show(mainPanel, "Elimina Prodotto");
        	}
        });
        
        modificaProdottoButton.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		cardLayout.show(mainPanel, "Modifica Prodotto");
        	}
        });
        
        visualizzaProdottiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Visualizza Prodotti");
            }
        });

        buttonPanel.add(aggiungiProdottoButton);
        buttonPanel.add(eliminaProdottoButton);
        buttonPanel.add(modificaProdottoButton);
        buttonPanel.add(visualizzaProdottiButton);

        add(buttonPanel, BorderLayout.WEST);
    }
}
