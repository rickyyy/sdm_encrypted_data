package eit.nl.utwente.sdm.encryptsql.guis;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import eit.nl.utwente.sdm.encryptsql.FinancialData;
import eit.nl.utwente.sdm.encryptsql.actors.Client;

public class GUIClient extends JFrame {

	private List<Client> clients;
	private JPanel mainPanel;
	private JComboBox clList;
	private JTable table;
	private JScrollPane tableContainer;
	private JTextField searchTextField;
	private JButton searchButton;
	private JPanel searchPanel;
	private JPanel northPanel;

	public GUIClient(List<Client> clients) {
		super("GUI Clients");
		this.clients = clients;
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		List<String> clientNames = new ArrayList<String>();
		for (Client c : clients) {
			clientNames.add(c.getName());
		}
		clList = new JComboBox(clientNames.toArray());
		clList.setPreferredSize(new Dimension(600, 30));
		clList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				int clientIndex = cb.getSelectedIndex();
				updateUI(GUIClient.this.clients.get(clientIndex), "select * from financial_data");
			}

		});
		northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.add(clList);
		searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		searchTextField = new JTextField();
		searchPanel.add(searchTextField);
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int clIndex = clList.getSelectedIndex();
				updateUI(GUIClient.this.clients.get(clIndex), searchTextField.getText());
			}
		});
		searchPanel.add(searchButton);
		northPanel.add(searchPanel);
		mainPanel.add(northPanel, BorderLayout.NORTH);

		Client currentClient = clients.get(0);
		updateUI(currentClient, "select * from financial_data");
		
		getContentPane().add(mainPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JButton addButton = new JButton("Add Financial Record");
		addButton.addActionListener(new ActionListener() {
			
			private JPanel addPanel;

			@Override
			public void actionPerformed(ActionEvent arg0) {
//				GUIPatient.this.remove(tableContainer);
				int clientIndex = clList.getSelectedIndex();
				Client cl = GUIClient.this.clients.get(clientIndex);
				mainPanel.remove(northPanel);
				searchPanel.setVisible(true);
				mainPanel.remove(tableContainer);
				tableContainer.setVisible(false);
				addPanel = new JPanel();
				addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
				addPanel.add(new JLabel("ID Client"));
				final JTextField clientTF = new JTextField(cl.getId() + "");
				clientTF.setEditable(false);
				addPanel.add(clientTF);
				addPanel.add(new JLabel("ID Consultant"));
				final JTextField consultantTF = new JTextField(cl.getIdConsultant() + "");
				consultantTF.setEditable(false);
				addPanel.add(consultantTF);
				addPanel.add(new JLabel("Statement"));
				final JTextField statementTF = new JTextField();
				addPanel.add(statementTF);
				addPanel.add(new JLabel("Investment"));
				final JTextField investmentTF = new JTextField();
				addPanel.add(investmentTF);
				addPanel.add(new JLabel("Interest Rate"));
				final JTextField interestRateTF = new JTextField();
				addPanel.add(interestRateTF);
				JButton saveButton = new JButton("Save");
				saveButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mainPanel.remove(addPanel);
						addPanel.setVisible(false);
						searchPanel.setVisible(true);
						mainPanel.add(northPanel, BorderLayout.NORTH);
						int clientIndex = clList.getSelectedIndex();
						Client c = GUIClient.this.clients.get(clientIndex);
						c.store(c.getIdConsultant(), c.getId(), Integer.parseInt(investmentTF.getText()), Integer.parseInt(interestRateTF.getText()), statementTF.getText());
		
						GUIClient.this.updateUI(c, "select * from financial_data");
//						updateTableModel(p);
//						mainPanel.add(tableContainer);
//						tableContainer.setVisible(true);
//						pack();
					}
				});
				addPanel.add(saveButton);
				addPanel.add(Box.createRigidArea(new Dimension(500, 300)));
				mainPanel.add(addPanel, BorderLayout.CENTER);
				mainPanel.revalidate();
				pack();
			}
		});
		mainPanel.add(addButton, BorderLayout.SOUTH);
		mainPanel.revalidate();
		pack();
		setVisible(true);

	}

	private void updateUI(Client currentClient, String sqlQuery) {
		searchPanel.setVisible(true);
		List<String> columns = new ArrayList<String>();
		List<String[]> values = new ArrayList<String[]>();
		searchTextField.setText(sqlQuery);
		columns.add("id");
		columns.add("id_cons");
		columns.add("id_client");
		columns.add("statement");
		columns.add("investment");
		columns.add("interest_rate");
		List<FinancialData> fds = currentClient.searchEncData(sqlQuery);
		for (FinancialData fd : fds) {
			
			values.add(new String[] { fd.id + "", fd.idCons + "",
					fd.idClient + "", fd.statement + "",
					fd.investment + "", fd.interest + ""});
		}

		table = new JTable() {
			

			public boolean isCellEditable(int row, int column) {
				return false;
			}
						
		};
		table.setModel(new DefaultTableModel(
				values.toArray(new Object[][] {}), columns.toArray()));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		if (tableContainer != null)
			mainPanel.remove(tableContainer);
		tableContainer = new JScrollPane(table);
		mainPanel.add(tableContainer, BorderLayout.CENTER);
	
		mainPanel.revalidate();
		pack();
	}	
	
}
