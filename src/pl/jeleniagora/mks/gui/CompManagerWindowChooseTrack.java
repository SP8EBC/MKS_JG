package pl.jeleniagora.mks.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import pl.jeleniagora.mks.rte.RTE_ST;
import pl.jeleniagora.mks.types.Track;

import javax.swing.JList;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import java.awt.Button;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;

@Component
public class CompManagerWindowChooseTrack extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2157188548869439705L;

	private JPanel contentPane;
	
	ApplicationContext context;
	
	ArrayList<Track> tracks;
	
	CompManagerWindowChooseTrack frame;
	
	@Autowired
	RTE_ST rte_st;
	
	Track selected;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CompManagerWindowChooseTrack frame = new CompManagerWindowChooseTrack();
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
	public CompManagerWindowChooseTrack() {
		setResizable(false);
		setTitle("Wybierz tor na którym odbywają się zawody/trening");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 823, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.frame = this;
		
		JLabel lblLokalizacjadata = new JLabel("Lokalizacja_data");
		JLabel lblIloscwirazydata = new JLabel("ilosc_wirazy_data");
		JLabel lblDlugoscdata = new JLabel("dlugosc_data");
		JLabel lblIloscstartow = new JLabel("ilosc_startow");
		
		/*
		 * Wczytywanie pliku XML z definicjami torów
		 */
		context = new ClassPathXmlApplicationContext("luge-tracks-spring-ctx.xml");
		/*
		 * Wyciąganie wszystkich zdefiniowanych w pliku XML torów - do tablicy będą szły id po których można wyciągać
		 * obiekty metodą "getBean"
		 */
		String trackNames[] = context.getBeanDefinitionNames();
		tracks = new ArrayList<Track>();

		DefaultListModel<Track> model = new DefaultListModel<Track>();		
		
		for (String s : trackNames) {
			/*
			 * Uzupełnianie w pętli modelu o kolejne elementy klasy Track tworzone na podstawie
			 * informacji z pliku XML
			 */
			Track elem = (Track)context.getBean(s);
			tracks.add(elem);
			
			model.addElement(elem);
		}
		
		JScrollPane scrollPane = new JScrollPane();
		
		JList<Track> list = new JList<Track>(model);			//
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				ListSelectionModel selModel = (ListSelectionModel)arg0.getSource();
				
				int selection = selModel.getMaxSelectionIndex();
				
				selected = model.get(selection);
				
				lblLokalizacjadata.setText("<html><body style='width: 180px'>" + selected.location + "</body></html>");
				lblIloscstartow.setText(new Short(selected.gateNum).toString());
				lblDlugoscdata.setText(new Short(selected.lnTotal).toString());
				lblIloscwirazydata.setText(new Short(selected.curvesTotal).toString());
				
				return;				
			}
			
			
		});
		
		scrollPane.setViewportView(list);
		
		JButton btnNewButton = new JButton("Anuluj");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
			
		});
		
		JButton btnPotwierdWybrok = new JButton("Potwierdź wybór (OK)");
		btnPotwierdWybrok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rte_st.competitions.track = selected;
				frame.dispose();
			}
			
		});
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		//220px
		JPanel panel_1 = new JPanel();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 342, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE)
						.addComponent(btnPotwierdWybrok, GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 288, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(2)
									.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)))
							.addGap(18)
							.addComponent(btnPotwierdWybrok)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnNewButton)))
					.addContainerGap())
		);
		panel_1.setLayout(new MigLayout("", "[90px:n][220px:n:220px]", "[44px:n:44px,center][22px:n:22px][22:n:22][22:n:22]"));
		
		JLabel lblLokalizacja = new JLabel("Lokalizacja");
		panel_1.add(lblLokalizacja, "cell 0 0");
		
		panel_1.add(lblLokalizacjadata, "cell 1 0");
		
		JLabel lblIloWiray = new JLabel("Ilość wiraży");
		panel_1.add(lblIloWiray, "cell 0 1");
		
		panel_1.add(lblIloscwirazydata, "cell 1 1");
		
		JLabel lblDugom = new JLabel("Długość [m]");
		panel_1.add(lblDugom, "cell 0 2");
		
		panel_1.add(lblDlugoscdata, "cell 1 2");
		
		JLabel lblIloStartw = new JLabel("Ilość startów");
		panel_1.add(lblIloStartw, "cell 0 3");
		
		panel_1.add(lblIloscstartow, "cell 1 3");
		
		JLabel lblZdjcie = new JLabel("Zdjęcie");
		panel.add(lblZdjcie);
		contentPane.setLayout(gl_contentPane);
	}
}
