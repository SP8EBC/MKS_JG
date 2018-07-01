package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import net.miginfocom.swing.MigLayout;
import pl.jeleniagora.mks.rte.RTE_ST;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@Component
public class CompManagerWindowJudges extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7767466502381639632L;
	
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JButton btnZapiszZmianyI;
	private JButton btnAnuluj;

	CompManagerWindowJudges frame;
	
	@Autowired
	RTE_ST rte_st;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowJudges frame = new CompManagerWindowJudges();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CompManagerWindowJudges() {
		this.frame = this;
		
		setTitle("Podaj personalia sędziów");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 260);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[][grow]", "[10px:n:10px][30px:n:30px][30px:n:30px][30px:n:30px][][][]"));
		
		JLabel lblSdziaPierwszy = new JLabel("Sędzia pierwszy:");
		contentPane.add(lblSdziaPierwszy, "cell 0 1,alignx trailing");
		
		textField = new JTextField();
		contentPane.add(textField, "cell 1 1,growx");
		textField.setColumns(10);
		
		JLabel lblSdziaDrugi = new JLabel("Sędzia Drugi:");
		contentPane.add(lblSdziaDrugi, "cell 0 2,alignx trailing");
		
		textField_1 = new JTextField();
		contentPane.add(textField_1, "cell 1 2,growx");
		textField_1.setColumns(10);
		
		JLabel lblSdziaTrzeci = new JLabel("Sędzia trzeci:");
		contentPane.add(lblSdziaTrzeci, "cell 0 3,alignx trailing");
		
		textField_2 = new JTextField();
		contentPane.add(textField_2, "cell 1 3,growx");
		textField_2.setColumns(10);
		
		btnZapiszZmianyI = new JButton("Zapisz zmiany i zamknij (OK)");
		btnZapiszZmianyI.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String s1 = textField.getText();
				String s2 = textField_1.getText();
				String s3 = textField_2.getText();
				
				if (s1.length() > 0)
					rte_st.competitions.Judge1st = textField.getText();
				else 
					rte_st.competitions.Judge1st = null;
				
				if (s2.length() > 0)
					rte_st.competitions.Judge2nd = textField_1.getText();
				else
					rte_st.competitions.Judge2nd = null;
				
				if (s3.length() > 0)
					rte_st.competitions.Judge3rd = textField_2.getText();
				else
					rte_st.competitions.Judge3rd = null;
				
				frame.dispose();
			}
		});
		contentPane.add(btnZapiszZmianyI, "cell 0 5 2 1,growx");
		
		btnAnuluj = new JButton("Anuluj");
		btnAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		contentPane.add(btnAnuluj, "cell 0 6 2 1,growx");
	}

}
