package pl.jeleniagora.mks.gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import pl.jeleniagora.mks.settings.LanguageS;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;

/**
 * Klasa obsługująca okno startowe służące do wyboru języka, podstawowych ustawień i przechodzenia do modułów funkcjonalnych
 * aplikcaji. To okno będzie tworzone jako pierwsze po Splash screen.
 * 
 * Klasa jest po części generowana automatycznie przez WindowBuilder
 */
public class StartWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3454958336648367538L;
	private JPanel contentPane;
	
	private JLabel lblJzyk;
	private JLabel lblBazaDanych;
	
	/**
	 * Normalnie punkt wejści do aplikacji będzie przeniesiony do osobnej klasy 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StartWindow frame = new StartWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Tworzenie okna i jego wszystkich elementów
	 */
	public StartWindow() {
		setTitle("MKS_JG - Ekran Startowy");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 317);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnJzyklanguage = new JMenu("Język (Language)");
		menuBar.add(mnJzyklanguage);
		
		JMenuItem mntmPolski = new JMenuItem("Polski");
		mntmPolski.setName("menuitem_StartWindow_PL");
		mntmPolski.setMnemonic(KeyEvent.VK_P);
		mntmPolski.addActionListener(this);
		mnJzyklanguage.add(mntmPolski);
		
		JMenuItem mntmlnskoGodka = new JMenuItem("Ślůnsko Godka");
		mntmlnskoGodka.setName("menuitem_StartWindow_SL");
		mntmlnskoGodka.setMnemonic(KeyEvent.VK_S);
		mntmlnskoGodka.addActionListener(this);
		mnJzyklanguage.add(mntmlnskoGodka);
		
		JMenuItem mntmesk = new JMenuItem("Český");
		mntmesk.setName("menuitem_StartWindow_CZ");
		mntmesk.setMnemonic(KeyEvent.VK_C);
		mntmesk.addActionListener(this);
		mnJzyklanguage.add(mntmesk);
		
		JMenuItem mntmEnglish = new JMenuItem("English");
		mntmEnglish.setName("menuitem_StartWindow_EN");
		mntmEnglish.setMnemonic(KeyEvent.VK_E);
		mntmEnglish.addActionListener(this);
		mnJzyklanguage.add(mntmEnglish);
		
		JMenu mnTrybPracymode = new JMenu("Tryb Pracy (Mode of Operation)");
		menuBar.add(mnTrybPracymode);
		
		JMenuItem mntmLokalnePlikiXml = new JMenuItem("Lokalne pliki XML (Local files)");
		mntmLokalnePlikiXml.setName("menuitem_StartWindow_XML");
		mntmLokalnePlikiXml.addActionListener(this);
		mnTrybPracymode.add(mntmLokalnePlikiXml);
		
		JMenuItem mntmBazaDanychdatabase = new JMenuItem("Baza Danych (Database)");
		mntmBazaDanychdatabase.setName("menuitem_StartWindow_DB");
		mntmBazaDanychdatabase.addActionListener(this);
		mnTrybPracymode.add(mntmBazaDanychdatabase);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		/*
		 * Panel statusu na dole okna wyświetlający aktualnie wybrany język i tryb pracy
		 */
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setName("panel_StartWindow_status");
		
		/*
		 * Przycisk wywołujący menadżera zawodników
		 */
		JButton btnMenaderZawodnikw = new JButton("MENADŻER ZAWODNIKÓW");
		btnMenaderZawodnikw.setName("btn_StartWindow_manager");
		
		/*
		 * Przycik przechodzący do obsługi zawodników
		 */
		JButton btnObsugaZawodw = new JButton("OBSŁUGA ZAWODÓW I TRENINGÓW");
		btnObsugaZawodw.setName("btn_StartWindow_comp");
		
		JLabel lblMksjgWersja = new JLabel("MKS_JG wersja 0.0.1-10032018");
		lblMksjgWersja.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblAutorMateuszLubecki = new JLabel("Autor: Mateusz Lubecki SP8EBC, tel: +48 660 43 44 46");
		lblAutorMateuszLubecki.setHorizontalAlignment(SwingConstants.CENTER);
		
		/*
		 * Layout ustawiający pozycję przycisków i opisu
		 */
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnObsugaZawodw, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnMenaderZawodnikw, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblAutorMateuszLubecki, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblMksjgWersja, GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)
					.addContainerGap())
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(53)
					.addComponent(btnMenaderZawodnikw)
					.addGap(18)
					.addComponent(btnObsugaZawodw)
					.addGap(58)
					.addComponent(lblMksjgWersja)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblAutorMateuszLubecki)
					.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		panel.setLayout(new BorderLayout(0, 0));
		
		/*
		 * Etykiety w pasku statusu są zdeklarowane globalnie dla klasy żeby można je było modyfikować
		 * z metody obsługującej zdarzenia
		 */
		this.lblJzyk = new JLabel("JĘZYK POLSKI");
		lblJzyk.setFont(new Font("Dialog", Font.BOLD, 10));
		panel.add(lblJzyk, BorderLayout.WEST);
		
		this.lblBazaDanych = new JLabel("PRACA Z PLIKAMI XML");
		lblBazaDanych.setFont(new Font("Dialog", Font.BOLD, 10));
		panel.add(lblBazaDanych, BorderLayout.EAST);
		contentPane.setLayout(gl_contentPane);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * 
	 * Metoda obsługująca zdarzenia od kliknięć w pozycje menu i przyciski. Ta jest w pełni ręcznie pisana
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		/* Wyciąganie obiektu który był źródłem akcji */
		Object actionSource = arg0.getSource();
		
		/* nazwa objektu który wygenerował akcję, na tej podstawie określa się konkretne przyciski/menu */
		String objectName = "";
		
		/* sprawdzanie jakiego typu jest obiekt generujący akcję (przycisk, pozycja menu) */
		if (actionSource instanceof JMenuItem) {
			/* rzutowanie na konkretny typ i przepisywanie nazwy obiektu */
			objectName = ((JMenuItem) actionSource).getName();
		}
		else if (actionSource instanceof JButton) {
			objectName = ((JButton) actionSource).getName();
		}
		else {
			;
		}
		
		/* wykonywanie konkretnych akcji dla konkretnych przycisków, pozycji menu itp. */
		switch (objectName) {
		
		/* język polski */
		case "menuitem_StartWindow_PL": {
			LanguageS.setLanguage(LanguageS.eLanguage.POLISH);
			this.lblJzyk.setText("JĘZYK POLSKI");
			break;
		}
		case "menuitem_StartWindow_SL": {
			LanguageS.setLanguage(LanguageS.eLanguage.SILESIAN);
			this.lblJzyk.setText("ŚLůNSKO GODKA");
			break;
		}
		case "menuitem_StartWindow_CZ": {
			LanguageS.setLanguage(LanguageS.eLanguage.CZECH);
			this.lblJzyk.setText("ČESKY JAZYK");
			break;
		}
		case "menuitem_StartWindow_EN": {
			LanguageS.setLanguage(LanguageS.eLanguage.ENGLISH);
			this.lblJzyk.setText("ENGLISH LANGUAGE");
			break;
		}
		case "menuitem_StartWindow_DB": {
			this.lblBazaDanych.setText("PRACA Z BAZĄ DANYCH");
			break;
		}
		case "menuitem_StartWindow_XML": {
			this.lblBazaDanych.setText("PRACA Z PLIKAMI XML");
			break;
		}
		}
		return;

	}
}
