package pl.jeleniagora.mks.reports;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

import pl.jeleniagora.mks.types.Competition;
import pl.jeleniagora.mks.types.Competitions;
import pl.jeleniagora.mks.types.LugerCompetitor;
import pl.jeleniagora.mks.types.Run;

/**
 * Klasa służąca do generowania raportów końcowych i częściowych z konkurencji
 * @author mateusz
 *
 */
public class CompetitionReportGenerator {

	final static String karpacz = "./res/karpacz_zawsze_gora.jpg";
	final static String bielsko = "./res/BB_logo.jpg";
	final static String mks = "./res/mks_logo_small.jpg";
	
	String baseDir;
	
	PdfFont font;
	PdfFont bold;
		
	Competition competitionToGenerateFrom;
	Competitions competitions;
	
	float headerFontSize = 14.0f;	// rozmiar czcionki w nagłówkowym napisie z nazwą zawodów i datą
	float tableFontSize = 10.0f;	// rozmiar czcionki nagłówka i treści tabeli
	float intoFontSize = 12.0f;	// rozmiar tekstu pod tabelą z wynikami
	
	int trainingRunsCount;
	int scoredRunsCount;
	
	public CompetitionReportGenerator(Competition c, Competitions cmps, String dir, int headerFontSize, int tableFontSize, int infoFontSize) {
		competitionToGenerateFrom = c;
		competitions = cmps;
		
		this.tableFontSize = tableFontSize;
		this.headerFontSize = headerFontSize;
		this.intoFontSize = infoFontSize;
		
		this.baseDir = dir;
	}

	public CompetitionReportGenerator(Competition c, Competitions cmps, String dir) {
		competitionToGenerateFrom = c;
		competitions = cmps;
		
		this.baseDir = dir;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, int rowspan, int colspan,  boolean sideBorder, boolean bottomBorder) {
	    Cell cell = new Cell(rowspan, colspan).add(new Paragraph(text).setFont(font).setFontSize(tableFontSize));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
	    if (bottomBorder)
	    	cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderBottom(Border.NO_BORDER);
	    if (sideBorder)
	    	cell.setBorderLeft(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    if (sideBorder)
	    	 cell.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	 cell.setBorderRight(Border.NO_BORDER);	    
	    return cell;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, boolean sideBorder, boolean bottomBorder) {
	    Cell cell = new Cell().add(new Paragraph(text).setFont(font));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//	    cell.setBorder(Border.NO_BORDER);
	    if (bottomBorder)
	    	cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderBottom(Border.NO_BORDER);
	    if (sideBorder)
	    	cell.setBorderLeft(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    if (sideBorder)
	    	 cell.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	 cell.setBorderRight(Border.NO_BORDER);	    
	    return cell;
	}
	
	public Cell getCell(String text, TextAlignment alignment, PdfFont font, float fontSize, boolean sideBorder, boolean bottomBorder) {
	    Cell cell = new Cell().add(new Paragraph(text).setFont(font).setFontSize(fontSize));
	    cell.setPadding(0);
	    cell.setTextAlignment(alignment);
	    cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//	    cell.setBorder(Border.NO_BORDER);
	    if (bottomBorder)
	    	cell.setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderBottom(Border.NO_BORDER);
	    if (sideBorder)
	    	cell.setBorderLeft(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	cell.setBorderLeft(Border.NO_BORDER);
	    cell.setBorderTop(Border.NO_BORDER);
	    if (sideBorder)
	    	 cell.setBorderRight(new SolidBorder(ColorConstants.BLACK, 1.0f));
	    else
	    	 cell.setBorderRight(Border.NO_BORDER);
	    //cell.setBorderRight(Border.NO_BORDER);
	    return cell;
	}
	
	private void createHeader(Table scoreTable, int runsNumber) {
		scoreTable.addHeaderCell(getCell("Lokata", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Numer Startowy", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Imię i Nazwisko", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Klub", TextAlignment.CENTER, font, 2, 1, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.addHeaderCell(getCell("Czasy ślizgów", TextAlignment.CENTER, font, 1, runsNumber, true, true).setBorderTop(new SolidBorder(ColorConstants.BLACK, 1.0f)));
		scoreTable.startNewRow();
		for (int i = 1; i <= trainingRunsCount; i++)
			scoreTable.addHeaderCell(getCell("Tr " + i, TextAlignment.CENTER, font, tableFontSize,  true, true));
		for (int i = 1; i <= scoredRunsCount; i++)
			scoreTable.addHeaderCell(getCell("Pkt " + i, TextAlignment.CENTER, font, tableFontSize,  true, true));
	}
	
	/**
	 * Główna metoda wywoływana po zakończeniu konkurencji / ślizgu
	 * @throws IOException
	 */
	public void generate() throws IOException {
		
		trainingRunsCount = competitionToGenerateFrom.numberOfTrainingRuns;
		scoredRunsCount = competitionToGenerateFrom.numberOfAllRuns - trainingRunsCount;
		int runsNumber = trainingRunsCount + scoredRunsCount;
		
		String contestName = competitions.name;
		String competitionName = competitionToGenerateFrom.toString();
		String date = competitions.date.toString();
		
		// ustawiane na true jeżeli aktualna konkurencja nie jest jeszcze w pełni zakończona
		boolean partialOrComplete = false;
		
		// ustawiane na true w sytuacji generowania listy startowej aby w cześci na czasy rysowć wszystkie krawędzie
		boolean bottomBorder = false;
		
		// referencja na ostatni w pełni zakończoy ślizg
		Run lastDone = null;
		
		// nazwa wyjściowego pliku PDF
		String fn = null;
		
		LocalTime zero = LocalTime.of(0, 0, 0, 0);

		float[] columnsWidth = new float[runsNumber + 4];
		columnsWidth[0] = 2;
		columnsWidth[1] = 1;
		columnsWidth[2] = 4;
		columnsWidth[3] = 6;
		
		for (int i = 4; i < runsNumber + 4; i++)
			columnsWidth[i] = 2;
		
		font = PdfFontFactory.createFont("./res/DejaVuSans.ttf", PdfEncodings.IDENTITY_H, true);
		bold = PdfFontFactory.createFont("./res/Uni_Sans_Heavy.otf", PdfEncodings.IDENTITY_H, true);
		
		Image bielskoImage = new Image(ImageDataFactory.create(bielsko), 0, 450, 200);	// lewy górny róg
		Image karpaczImage = new Image(ImageDataFactory.create(karpacz), 720, 475, 100);	// prawy górny róg
		
		/*
		 * Pętla jedzie po wszystkich ślizgach aby sprawdzić czy konkurencja jest już zakończona czy nie
		 */
		for (Run r : competitionToGenerateFrom.runsTimes) {
			if (!r.done)
				break;
			
			// ostatni raz to przypisanie zostanie zrobione dla ostatnio zakończonego ślizgu/zjazdu
			lastDone = r;
		}
		
		// sprawdzanie czy użytkownik nie wywoław generatora dla jeszcze nie rozpoczętej konkurencji
		if (lastDone == null) {
			fn = baseDir + "/" + competitionToGenerateFrom.toString().replaceAll(" ", "_").toLowerCase() 
					 + "_lista_startowa_" + competitions.toString().replaceAll(" ", "_").toLowerCase() + ".pdf";	
			bottomBorder = true;
		}
		
		// generowanie nazwy pliku PDF w którym będzie raport
		if (fn == null && partialOrComplete) {
			fn = baseDir + "/" + competitionToGenerateFrom.toString().replaceAll(" ", "_").toLowerCase() 
					+  "_koncowy_" + competitions.toString().replaceAll(" ", "_").toLowerCase() + ".pdf";
		}
		else if (fn == null && !partialOrComplete) {
			fn = baseDir + "/" + competitionToGenerateFrom.toString().replaceAll(" ", "_").toLowerCase() 
					+ "_czesciowy_po_" + lastDone.toString().replaceAll(" ", "_") + "_" + competitions.toString().replaceAll(" ", "_").toLowerCase() + ".pdf";
		}
		
		PdfDocument pdf = new PdfDocument(new PdfWriter(fn));
		Document document = new Document(pdf);
		
		// format A4 poziomo
		pdf.setDefaultPageSize(PageSize.A4.rotate());
		
		// dodawnie stopki na każdej stronie
		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new FooterHandler(document, font, competitionName + " " + contestName));
		
		// dodawanie znaku wodnego w tle na drugiej i kolejnych stronach
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new BackgroundHandler());
		
		// tableta tytułowa pomiędzy logotypami na pierwszej stronie
		Table titleTable = new Table(1);
		titleTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
		if (partialOrComplete && lastDone != null)
			titleTable.addCell(getCell("Raport końcowy z konkurencji", TextAlignment.CENTER, bold, headerFontSize + 4.0f, false, false));
		else if (!partialOrComplete && lastDone != null)
			titleTable.addCell(getCell("Raport częściowy z konkurencji", TextAlignment.CENTER, bold, headerFontSize + 4.0f, false, false));	
		else 
			titleTable.addCell(getCell("Lista startowa", TextAlignment.CENTER, bold, headerFontSize + 4.0f, false, false));	
		
		// kolejna część tytułowa pomiędzy logotypami
		Table contestTable = new Table(1);
		contestTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
		contestTable.addCell(getCell(competitionName, TextAlignment.CENTER, bold, headerFontSize + 2.0f, false, false));
		contestTable.addCell(getCell("", TextAlignment.CENTER, font, headerFontSize, false, false));
		contestTable.addCell(getCell(contestName, TextAlignment.CENTER, font, headerFontSize, false, false));
		contestTable.addCell(getCell(date, TextAlignment.CENTER, font, headerFontSize, false, false));
		
		// główna tabela z czasami przejazdu
		Table scoreTable = new Table(columnsWidth);
		scoreTable.setWidth(new UnitValue(UnitValue.PERCENT, 100));
		
		// generownie nagłówka tabeli z wynikami, będzie on automatycznie powielany na każdej kolejnej stronie
		this.createHeader(scoreTable, runsNumber);
		
		// public Map<LugerCompetitor, Short> ranks;
		// entry set z wynikami
		Set<Entry<LugerCompetitor, Short>> ranksSet = competitionToGenerateFrom.ranks.entrySet();
		
		// tworzenie wektora z pozycjami z mapy
		Vector<Entry<LugerCompetitor, Short>> ranksVct = new Vector<Entry<LugerCompetitor, Short>>(ranksSet);
		
		// sortowanie wektora po lokatach rosnąco czyli tak jak ma się to wyświetlać w tabeli
		ranksVct.sort(
				new Comparator<Entry<LugerCompetitor, Short>>() {

					@Override
					public int compare(Entry<LugerCompetitor, Short> arg0, Entry<LugerCompetitor, Short> arg1) {
						Short rank0 = arg0.getValue();
						Short rank1 = arg1.getValue();
						
						Short startNum0 = arg0.getKey().getStartNumber();
						Short startNum1 = arg0.getKey().getStartNumber();
						
						if (rank0 > rank1)
							return 1;
						else if (rank0 < rank1)
							return -1;
						else if (rank0.equals(rank1)) {
							// jeżeli dwóch ma ten sam czas to trzeba zwrócić po nr startowych
							if (startNum0 > startNum1) 
								return 1;
							else return -1;
						}
						return 0;
					}
					
				}
				);
		
		// dodawanie pozycji to tabeli w raporcie
		for (Entry<LugerCompetitor, Short> e : ranksVct) {
			
			// dodawanie ostatniego wiersza z krawędzią a dole
			if (ranksVct.lastElement().equals(e)) {
				// e.getValue().toString()
				scoreTable.startNewRow();
				scoreTable.addCell(getCell(e.getValue().toString(), TextAlignment.CENTER, font,this.tableFontSize,  true, false).setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f)));
				scoreTable.addCell(getCell(new Short(e.getKey().getStartNumber()).toString(), TextAlignment.CENTER, font, this.tableFontSize, true, false).setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f)));
				scoreTable.addCell(getCell(e.getKey().toString(), TextAlignment.CENTER, font, this.tableFontSize, true, false).setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f)));
				scoreTable.addCell(getCell(e.getKey().clubToString(), TextAlignment.CENTER, font, this.tableFontSize, true, false).setBorderBottom(new SolidBorder(ColorConstants.BLACK, 1.0f)));
				
				// dodawnie kolejnych czasów ślizgu/zjazdu
				for (Run r : competitionToGenerateFrom.runsTimes) {
					// wyciąganie czasu w kolejnych ślizgach
					LocalTime timeToAdd = r.totalTimes.get(e.getKey());
					
					// jeżeli czas ślizgu jest zerowy to wyświetl puste pole
					if (timeToAdd.equals(zero))
						scoreTable.addCell(getCell(" ", TextAlignment.CENTER, font, this.tableFontSize, true, bottomBorder));
					else
						scoreTable.addCell(getCell(timeToAdd.toString(), TextAlignment.CENTER, font, this.tableFontSize, true, bottomBorder));				}
				break;
			}
			
			// e.getValue().toString()
			scoreTable.startNewRow();
			scoreTable.addCell(getCell(e.getValue().toString(), TextAlignment.CENTER, font,this.tableFontSize,  true, false));
			scoreTable.addCell(getCell(new Short(e.getKey().getStartNumber()).toString(), TextAlignment.CENTER, font, this.tableFontSize, true, false));
			scoreTable.addCell(getCell(e.getKey().toString(), TextAlignment.CENTER, font, this.tableFontSize, true, false));
			scoreTable.addCell(getCell(e.getKey().clubToString(), TextAlignment.CENTER, font, this.tableFontSize, true, false));
			
			// dodawnie kolejnych czasów ślizgu/zjazdu
			for (Run r : competitionToGenerateFrom.runsTimes) {
				// wyciąganie czasu w kolejnych ślizgach
				LocalTime timeToAdd = r.totalTimes.get(e.getKey());
				
				// jeżeli czas ślizgu jest zerowy to wyświetl puste pole
				if (timeToAdd.equals(zero))
					scoreTable.addCell(getCell(" ", TextAlignment.CENTER, font, this.tableFontSize, true, bottomBorder));
				else
					scoreTable.addCell(getCell(timeToAdd.toString(), TextAlignment.CENTER, font, this.tableFontSize, true, bottomBorder));
			}
			

		}
		
		document.add(bielskoImage);
		document.add(karpaczImage);
		
		// dodawanie znaku wodnego na pierwszej stronie
		AddBackgroundWatermark.addBackgroundWatermark(pdf, 1);
		document.add(titleTable);
		document.add(contestTable);
		
		Paragraph p = new Paragraph();
		p.setMultipliedLeading(2.0f);
		p.add(new Text("  ").setFontSize(20.0f));
		document.add(p);
		document.add(p);
		document.add(p);
		
		document.add(scoreTable);
		
		document.close();

	}
		
}
