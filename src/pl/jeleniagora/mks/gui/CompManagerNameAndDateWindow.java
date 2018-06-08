package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import java.awt.Font;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.toedter.calendar.JCalendar;

import pl.jeleniagora.mks.rte.RTE_ST;

import javax.swing.LayoutStyle.ComponentPlacement;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CompManagerNameAndDateWindow {

	JFrame frmUstawNazwI;
	private JTextField compsName;
	private JLabel lblNazwaZwodw;
	private JLabel lblData;
	
	private AnnotationConfigApplicationContext context;
	
	public int selectedDay = 0, selectedMonth = 0, selectedYear = 0;
	
	LocalDate selectedDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerNameAndDateWindow window = new CompManagerNameAndDateWindow(null);
					window.frmUstawNazwI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CompManagerNameAndDateWindow(AnnotationConfigApplicationContext ctx) {
		context = ctx;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("deprecation")
	private void initialize() {
		RTE_ST rte_st = (RTE_ST)context.getBean("RTE_ST");
		ZoneId zone = ZoneId.systemDefault();
		selectedDate = rte_st.competitions.date;
		
		frmUstawNazwI = new JFrame();
		frmUstawNazwI.setTitle("Ustaw nazwę i datę");
		frmUstawNazwI.setBounds(100, 100, 456, 395);
		frmUstawNazwI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		compsName = new JTextField();
		compsName.setFont(new Font("Dialog", Font.PLAIN, 16));
		compsName.setColumns(10);
		compsName.setText(rte_st.competitions.name);
		
		JCalendar calendar = new JCalendar();
		calendar.addPropertyChangeListener(new CompManagerNameAndDateCalndrListener(this));
		Date date = new Date();
		long epoch = selectedDate.toEpochDay();	// TODO: poprawić na epoch sekundy!!
		date.setTime(epoch);
		calendar.setDate(date);
		
		lblNazwaZwodw = new JLabel("Nazwa zawodów / sesji treningowej");
		lblNazwaZwodw.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblData = new JLabel("Data");
		lblData.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int answer = JOptionPane.YES_NO_OPTION;
				answer = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz wyjść i zapisać zmiany?", "Pozor!", answer);
				
				if (answer == JOptionPane.YES_OPTION) {
					if (context != null) {
						RTE_ST rte_st = (RTE_ST)context.getBean("RTE_ST");
						
						rte_st.competitions.date = selectedDate;
						rte_st.competitions.name = compsName.getText();
						
						frmUstawNazwI.dispose();
					}
				}
				
			}
		});
		
		JButton btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmUstawNazwI.dispose();

			}
		});
		GroupLayout groupLayout = new GroupLayout(frmUstawNazwI.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addGap(49)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(btnAnuluj, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnOk, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(calendar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblData, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblNazwaZwodw, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
						.addComponent(compsName, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
					.addContainerGap(57, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNazwaZwodw)
					.addGap(18)
					.addComponent(compsName, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblData)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(calendar, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnOk)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnAnuluj)
					.addContainerGap(19, Short.MAX_VALUE))
		);
		frmUstawNazwI.getContentPane().setLayout(groupLayout);
	}
}
