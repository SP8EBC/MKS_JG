package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.rte.RTE_GUI;
import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Competition;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@Component
@Lazy
public class CompManagerWindowDeleteCompetition extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8133727540384402301L;

	private JPanel contentPane;
	
	@Autowired
	RTE_ST rte_st;
	
	@Autowired
	RTE_GUI rte_gui;
	
	@Autowired
	CompManagerCSelectorUpdater selectorUpdater;	
	
	CompManagerWindowDeleteCompetition window;
	
	JComboBox<Competition> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowDeleteCompetition frame = new CompManagerWindowDeleteCompetition();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void initializeComboBox() {
		for (Competition c : rte_st.competitions.competitions) {
			comboBox.addItem(c);
		}
	}
	
	/**
	 * Create the frame.
	 */
	public CompManagerWindowDeleteCompetition() {
		this.window = this;
		
		setResizable(false);
		setTitle("Wybierz konkurencję do usunięcia z programu");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[][][][][][][][]"));
		
		JLabel lblKonkurencjaDoUsunicia = new JLabel("Konkurencja do usunięcia:");
		contentPane.add(lblKonkurencjaDoUsunicia, "cell 0 0,alignx trailing");
		
		comboBox = new JComboBox<Competition>();
		contentPane.add(comboBox, "cell 1 0,growx");
//		for (Competition c : rte_st.competitions.competitions) {
//			comboBox.addItem(c);
//		}
		
		JLabel lblUwagaUycieTej = new JLabel("<html><body style='width: 320px;'><p align=center>Uwaga! Użycie tej opcji spowoduje bezpowrotne usunięcie konkurencji i wszystkich zapisanych w niej czasach ślizgów. Operacji tej nie będzie można cofnąć inaczej niż przez ponowne wczytanie zapisanego wcześniej pliku XML!</p></body></html>");
		contentPane.add(lblUwagaUycieTej, "cell 0 2 2 1");
		
		JButton btnUsu = new JButton("Usuń");
		btnUsu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Competition toDelete = (Competition)comboBox.getSelectedItem();
				
				Competition current = rte_st.currentCompetition;
				
				if (toDelete.equals(current)) {
					JOptionPane.showMessageDialog(null, "Nie można usunąć aktualne rozgrywanej konkurencji!");

				}
				else {
					// usuwanie wybranej konkurencji
					rte_st.competitions.competitions.remove(toDelete);
					
					selectorUpdater.updateSelectorContent(rte_st.competitions.competitions);
					
					window.dispose();
				}
				
			}
		});
		contentPane.add(btnUsu, "cell 0 7,growx");
		
		JButton btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.dispose();
			}
		});
		contentPane.add(btnAnuluj, "cell 1 7,growx");
	}

}
