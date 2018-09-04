package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.exceptions.UninitializedCompEx;
import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.LugerCompetitor;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;

public class CompManagerWindowEditStartNum extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3186692480781258839L;

	private JPanel contentPane;
	
	JSpinner spinner;
	
	JFrame window;
	
	LugerCompetitor edited;

	/**
	 * Create the frame.
	 */
	public CompManagerWindowEditStartNum(
											LugerCompetitor cmptr,
											Competition competition,
											CompManagerScoreTableModel model) 
	{
		HashMap<LugerCompetitor, Short> startList = competition.startList;
		Map<Short, LugerCompetitor> invertedStartList = competition.invertedStartList;
		
		this.window = this;
		edited = cmptr;
		
		setTitle("Edycja numeru startowego");
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 430, 165);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][40px:n:40px,fill][200px:n:200px]", "[][][][]"));
		
		JLabel lblEdytowanyZawodnik = new JLabel("Edytowany zawodnik:");
		contentPane.add(lblEdytowanyZawodnik, "cell 0 0");
		
		JLabel lblImieINazwisko = new JLabel(cmptr.toString());
		contentPane.add(lblImieINazwisko, "cell 1 0 2 1");
		
		JLabel lblNumerStartowy = new JLabel("Numer startowy:");
		contentPane.add(lblNumerStartowy, "cell 0 1");
		
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		spinner.setFont(new Font("Dialog", Font.BOLD, 14));
		if (cmptr.getStartNumber() > 0)
			spinner.setValue(cmptr.getStartNumber());
		contentPane.add(spinner, "cell 1 1");
		
		JButton btnZapiszIWyjd = new JButton("Zapisz i wyjdź");
		btnZapiszIWyjd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Short oldStartNumber = edited.getStartNumber();
				Short newStartNumber = (((Integer)spinner.getValue()).shortValue());
				
				// sprawdzanie czy pod nowym numerze startowym nie ma żadnego zawodnika
				if (invertedStartList.containsKey(newStartNumber)) {
					String msg = "Zawodnik o numerze " + newStartNumber.toString() + " już istnieje: " 
								+ invertedStartList.get(newStartNumber).toString() + ". Zamienić ich miejscami i wyjść?";
					int a = JOptionPane.showConfirmDialog(window, msg, "Pozor!", JOptionPane.YES_NO_OPTION);
					
					if (a == JOptionPane.YES_OPTION) {
						LugerCompetitor toSwap = invertedStartList.get(newStartNumber);
						
						startList.remove(toSwap);
						startList.remove(edited);
						startList.put(toSwap, oldStartNumber);
						startList.put(edited, newStartNumber);
						
						edited.setStartNumber(newStartNumber);
						toSwap.setStartNumber(oldStartNumber);
						
						invertedStartList.remove(oldStartNumber);
						invertedStartList.remove(newStartNumber);
						invertedStartList.put(oldStartNumber, toSwap);
						invertedStartList.put(newStartNumber, edited);
						
						try {
							model.updateTableData(competition, false);
						} catch (UninitializedCompEx e) {
							e.printStackTrace();
						}
						model.fireTableDataChanged();
						window.dispose();
					}
					else {
						;
					}
				}
				else {
					edited.setStartNumber(newStartNumber);
					
					startList.remove(edited);
					startList.put(edited, newStartNumber);
					invertedStartList.remove(oldStartNumber);
					invertedStartList.put(newStartNumber, edited);

					try {
						model.updateTableData(competition, false);
					} catch (UninitializedCompEx e) {
						e.printStackTrace();
					}
					
					model.fireTableDataChanged();
					window.dispose();
				}
			}
		});
		contentPane.add(btnZapiszIWyjd, "cell 0 2 3 1,growx");
		
		JButton btnZamknijBezZapisywania = new JButton("Zamknij bez zapisywania");
		btnZamknijBezZapisywania.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnZamknijBezZapisywania, "cell 0 3 3 1,growx");
	}

}
