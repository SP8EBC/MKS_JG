package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import java.awt.Font;
import com.toedter.calendar.JCalendar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JButton;

public class CompManagerNameAndDateWindow {

	private JFrame frmUstawNazwI;
	private JTextField compsName;
	private JLabel lblNazwaZwodw;
	private JLabel lblData;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerNameAndDateWindow window = new CompManagerNameAndDateWindow();
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
	public CompManagerNameAndDateWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUstawNazwI = new JFrame();
		frmUstawNazwI.setTitle("Ustaw nazwę i datę");
		frmUstawNazwI.setBounds(100, 100, 456, 395);
		frmUstawNazwI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		compsName = new JTextField();
		compsName.setFont(new Font("Dialog", Font.PLAIN, 16));
		compsName.setColumns(10);
		
		JCalendar calendar = new JCalendar();
		
		
		lblNazwaZwodw = new JLabel("Nazwa zawodów / sesji treningowej");
		lblNazwaZwodw.setHorizontalAlignment(SwingConstants.CENTER);
		
		lblData = new JLabel("Data");
		lblData.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton btnOk = new JButton("OK");
		
		JButton btnAnuluj = new JButton("Anuluj");
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
