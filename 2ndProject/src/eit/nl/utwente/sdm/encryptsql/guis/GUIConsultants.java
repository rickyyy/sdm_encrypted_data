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
import eit.nl.utwente.sdm.encryptsql.actors.Consultant;
import eit.nl.utwente.sdm.encryptsql.helpers.DBUtils;

public class GUIConsultants extends JFrame {

	private List<Consultant> consultants;
	private JPanel mainPanel;
	private JComboBox clList;
	private JTable table;
	private JScrollPane tableContainer;
	private JTextField searchTextField;
	private JButton searchButton;
	private JPanel searchPanel;
	private JPanel northPanel;
	private JButton addButton;

	public GUIConsultants(List<Consultant> consultants) {
		super("GUI Consultants");
		setBounds(new Rectangle(700, 30, 600, 400));
		this.consultants = consultants;
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		List<String> consultantNames = new ArrayList<String>();
		for (Consultant c : consultants) {
			consultantNames.add(c.getName());
		}
		clList = new JComboBox(consultantNames.toArray());
		clList.setPreferredSize(new Dimension(600, 30));
		clList.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox)e.getSource();
				int clientIndex = cb.getSelectedIndex();
				updateUI(GUIConsultants.this.consultants.get(clientIndex), "select * from financial_data");
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
				updateUI(GUIConsultants.this.consultants.get(clIndex), searchTextField.getText());
			}
		});
		searchPanel.add(searchButton);
		northPanel.add(searchPanel);
		mainPanel.add(northPanel, BorderLayout.NORTH);

		Consultant currentConsultant = consultants.get(0);
		updateUI(currentConsultant, "select * from financial_data");
		
		getContentPane().add(mainPanel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addButton = new JButton("Add Financial Record");
		addButton.addActionListener(new ActionListener() {
			
			private JPanel addPanel;

			@Override
			public void actionPerformed(ActionEvent arg0) {
//				GUIPatient.this.remove(tableContainer);
				int clientIndex = clList.getSelectedIndex();
				Consultant cl = GUIConsultants.this.consultants.get(clientIndex);
//				northPanel.remove(searchPanel);
				mainPanel.remove(northPanel);
				searchPanel.setVisible(true);
				mainPanel.remove(tableContainer);
				tableContainer.setVisible(false);
				addPanel = new JPanel();
				addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.Y_AXIS));
				addPanel.add(new JLabel("ID Consultant"));
				final JTextField consultantTF = new JTextField(cl.getId() + "");
				consultantTF.setEditable(false);
				addPanel.add(consultantTF);
				addPanel.add(new JLabel("ID Client"));
				final List<Client> clients = DBUtils.getClientsForConsultant(cl);
				List<String> clNames = new ArrayList<String>();
				for (Client client : clients) {
					clNames.add(client.getName());
				}
				final JComboBox namesClients = new JComboBox(clNames.toArray(new String[0]));
				addPanel.add(namesClients);
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
						mainPanel.add(northPanel, BorderLayout.NORTH);
						mainPanel.remove(addPanel);
						mainPanel.add(addButton, BorderLayout.SOUTH);
						addPanel.setVisible(false);
						searchPanel.setVisible(true);
						northPanel.add(searchPanel);
						int clientIndex = clList.getSelectedIndex();
						Consultant c = GUIConsultants.this.consultants.get(clientIndex);
						Client client = clients.get(namesClients.getSelectedIndex());
						c.store(c.getId(), client.getId(), Integer.parseInt(investmentTF.getText()), Integer.parseInt(interestRateTF.getText()), statementTF.getText());
		
						GUIConsultants.this.updateUI(c, "select * from financial_data");
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

	private void updateUI(Consultant currentClient, String sqlQuery) {
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
