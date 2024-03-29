//package com.filechoser;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FileChoser extends JFrame implements ActionListener {

	// GUI features
	private BufferedReader fileInput;
	private JTextArea textArea;
	private JButton openButton, parseButton;
	private static HashMap<String, Doc> mainHmp = new HashMap<String, Doc>();
	// Other fields
	private File[] fileName;
	private String[] data;
	private int numLines;
	JTextField _fileNameTF = new JTextField(15);
	public static CodeSheet codesheet = new CodeSheet();



	public FileChoser(String s) {
		super(s);

		// Content pane
		Container container = getContentPane();
		//container.setBackground(Color.black);
		//container.setLayout(new BorderLayout(5,5)); // 5 pixel gaps
		container.setLayout(new FlowLayout());
		container.add(new JLabel("File:"));
		container.add(_fileNameTF);
		JMenuBar menubar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenuItem openItem = new JMenuItem("Open...");
		openItem.addActionListener(this);

		//... Assemble the menu
		menubar.add(fileMenu);
		fileMenu.add(openItem);

		//... Set window characteristics

		// Open button
		/*    openButton = new JButton("Open File");
		    openButton.addActionListener(this);
		    container.add(openButton,BorderLayout.WEST);
		 */
		// Read file button
		parseButton = new JButton("Parse");
		parseButton.addActionListener(this);
		parseButton.setEnabled(false);
		container.add(parseButton);
		this.setJMenuBar(menubar);
		this.setContentPane(container);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack(); // Layout components.
		this.setLocationRelativeTo(null); // Center window.
		/*
		// Text area	
		textArea = new JTextArea(10, 15);
		    container.add(new JScrollPane(textArea),BorderLayout.SOUTH);*/
	}

	/* ACTION PERFORMED */

	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand().equals("Open..."))
			getFileName();
		if (event.getActionCommand().equals("Parse"))
			parseFile(fileName);
	}

	/* OPEN THE FILE */

	private void getFileName() { // Display file dialog so user can select file to open
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled(true);
		int result = fileChooser.showOpenDialog(this);

		// If cancel button selected return
		if (result == JFileChooser.CANCEL_OPTION)
			return;

		// Obtain selected file

		fileName = fileChooser.getSelectedFiles();
		String fileString = "";
		for (File file1 : fileName) {

			fileString = fileString.concat(file1.getName().concat(";"));
		}
		_fileNameTF.setText(fileString);
		for (File file : fileName)
			//if (checkFileName(file)) {

			// openButton.setEnabled(false);
			parseButton.setEnabled(true);
		// }
	}

	/* READ FILE */

	private void parseFile(File[] fileName) {
		// Disable read button


		parseButton.setEnabled(false);
		for (File dir : fileName) {
			File[] files = dir.listFiles();

			for (File file : files) {

				File myFile = new File(file.getAbsolutePath());
				List<String> lines = new ArrayList<String>();
				if (myFile.getName().substring(myFile.getName().indexOf("."))
						.equalsIgnoreCase("docx")) {
					try {
						ZipFile docxFile = new ZipFile(myFile);
						ZipEntry documentXML = docxFile
						.getEntry("word/document.xml");
						InputStream documentXMLIS = docxFile
						.getInputStream(documentXML);
						DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
						Document doc = dbf.newDocumentBuilder().parse(
								documentXMLIS);

						Element tElement = doc.getDocumentElement();
						NodeList n = (NodeList) tElement
						.getElementsByTagName("w:p");

						for (int j = 0; j < n.getLength(); j++) {

							Node child = n.item(j);
							String line = child.getTextContent();

							if (line != null && !line.trim().isEmpty()) {

								lines.add(line.trim());
								//System.out.println("" + line);
							}

						}

					} catch (Exception e) {
						//e.printStackTrace();
					}
				} else {


					
					readMyDocument(myFile.getName().substring(0,
							myFile.getName().indexOf(".")), myFile
							.getAbsolutePath());
				}
			}




			//			Iterator iterator = mainHmp.keySet().iterator();
			//
			//			
			//			
			//			while (iterator.hasNext()) {
			//				String key = iterator.next().toString();
			//				Doc value = (Doc)mainHmp.get(key);
			//
			//				System.out.println("key=="+key );
			//				System.out.println("values: " + "comments" + value.getComments() +   " title : "+value.getTitle() );
			//			}

		}

		//print hamp1
		codesheet = new CodeSheet();
		codesheet.execute();
		HashMap<String,HashMap<Integer, String>> mapkeyVal = codesheet.getMapkeyVal();
		codesheet.printHMap(mapkeyVal);
		//print values from files;
		System.out.println("----------------------------------------------------------------------");
		Utilities.printHash();
		processDataToExcel(codesheet,FileChoser.getMainHmp());

	}

	private void processDataToExcel(CodeSheet codesheet2,
			HashMap<String, Doc> mainHMap) {


		HSSFWorkbook workbook = initWorkBook();
		Iterator iterator = mainHMap.keySet().iterator();

		int counter=0;
		while (iterator.hasNext()) {
			counter++;
			int column=0;
			String[] Titles={"DocId","Title","start date","end date","lengthOfProtest",
					"country","State","Location","eventDescription","protestingParties",
					"selfDefinition","issues","protestTarget","targetResponding","protestingOrganization",
					"numberOfProtestors","connectionToOtherEvents","outcome","comments","stateForceAction",
					"numberOfStateForce","numberOfProtestorArrested","Protestors Injured",
					"State Forces Injured","Others Injured","Protesters Killed",
					"State Forces Killed","Others Killed","propertyDamage"};



			String key = iterator.next().toString();
			Doc value = (Doc) mainHMap.get(key);
			HSSFRow rowA = workbook.getSheetAt(0).createRow(counter);

			HSSFCell cellA = rowA.createCell(column);
			cellA.setCellValue(new HSSFRichTextString(key));

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getTitle().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getTitle()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getEventdate().getStartDate().toString().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getEventdate().getStartDate().toString()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getEventdate().getStartDayOfTheWeek().toString().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getEventdate().getStartDayOfTheWeek().toString()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getEventdate().getEndDate().toString().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getEventdate().getEndDate().toString()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getEventdate().getEndDayOfTheWeek().toString().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getEventdate().getEndDayOfTheWeek().toString()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getLengthOfProtest().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getLengthOfProtest()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getCountry().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getCountry()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getState().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getState()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getLocation().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getLocation()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getEventDescription().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getEventDescription()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getProtestingParties().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getProtestingParties()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getSelfDefinition().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getSelfDefinition()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getIssues().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getIssues()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getProtestTarget().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getProtestTarget()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getTargetResponding().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getTargetResponding()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getProtestingOrganization().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getProtestingOrganization()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberOfProtestors().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberOfProtestors()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getConnectionToOtherEvents().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getConnectionToOtherEvents()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getOutcome().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getOutcome()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getComments().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getComments()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberOfStateForce().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberOfStateForce()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberOfProtestorArrested().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberOfProtestorArrested()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberInjured().getProtestors().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberInjured().getProtestors()));

			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberInjured().getStateForces().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberInjured().getStateForces()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberInjured().getOthers().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberInjured().getOthers()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberKilled().getProtestors().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberKilled().getProtestors()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{
				if(value.getNumberKilled().getStateForces().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberKilled().getStateForces()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}


			column++;
			cellA = rowA.createCell(column);
			try{

				if(value.getNumberKilled().getOthers().equalsIgnoreCase("SD")) throw new Exception();
				cellA.setCellValue(new HSSFRichTextString(value.getNumberKilled().getOthers()));
			}catch (Exception e) {
				// TODO: handle exception
				cellA.setCellValue(99);
			}

			System.out.println("sun test len : " + column +", " + Titles.length);
		}


		FileOutputStream fos = null;
		try {
			File file=new File("Data.xls");
			boolean exists = file.exists();
			if (!exists) {
				fos = new FileOutputStream(new File("Data.xls"));
			}else{
				file.delete();
				file=new File("Data.xls");
				fos = new FileOutputStream(new File("Data.xls"));
			}

			workbook.write(fos);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private HSSFWorkbook initWorkBook() {
		// TODO Auto-generated method stub
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet firstSheet = workbook.createSheet("DataSheet");
		HSSFRow rowA = firstSheet.createRow(0);
		String[] Titles={"DocId","Title","start date","start day","end date","end day","lengthOfProtest",
				"country","State","Location","eventDescription","protestingParties",
				"selfDefinition","issues","protestTarget","targetResponding","protestingOrganization",
				"numberOfProtestors","connectionToOtherEvents","outcome","comments","stateForceAction",
				"numberOfStateForce","numberOfProtestorArrested","Protestors Injured",
				"State Forces Injured","Others Injured","Protesters Killed",
				"State Forces Killed","Others Killed","propertyDamage"};


		for(int k=0;k<Titles.length;k++){
			HSSFCell cellA = rowA.createCell(k);

			cellA.setCellValue(new HSSFRichTextString(Titles[k]));	
		}

		return workbook;
	}

	public static void readMyDocument(String fileName, String filePath) {


		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(filePath));

			HWPFDocument doc = new HWPFDocument(fs);

			/** Read the content **/
			readParagraphs(fileName, doc);

			// int pageNumber=1;

			/** We will try reading the header for page 1**/
			// readHeader(doc, pageNumber);
			/** Let's try reading the footer for page 1**/
			//  readFooter(doc, pageNumber);
			/** Read the document summary**/
			//readDocumentSummary(doc);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	public static void readParagraphs(String fileName, HWPFDocument doc)
	throws Exception {
		WordExtractor we = new WordExtractor(doc);

		/**Get the total number of paragraphs**/
		String[] paragraphs = we.getParagraphText();
		//System.out.println("Total Paragraphs: " + paragraphs.length);
		Utilities utl = new Utilities();

		Doc document = utl.createDoc(paragraphs,codesheet);
		mainHmp.put(fileName, document);


		//utl.makeHashmap(fileName,paragraphs);
		for (int i = 0; i < paragraphs.length; i++) {

			//System.out.println("Length of paragraph " + (i + 1) + ": "
			//+ paragraphs[i].length());
			//System.out.println(paragraphs[i].toString());

		}

	}


	public static void readDocumentSummary(HWPFDocument doc) {
		DocumentSummaryInformation summaryInfo = doc
		.getDocumentSummaryInformation();
		String category = summaryInfo.getCategory();
		String company = summaryInfo.getCompany();
		int lineCount = summaryInfo.getLineCount();
		int sectionCount = summaryInfo.getSectionCount();
		int slideCount = summaryInfo.getSlideCount();

		//System.out.println("---------------------------");
		//System.out.println("Category: " + category);
		//System.out.println("Company: " + company);
		//System.out.println("Line Count: " + lineCount);
		//System.out.println("Section Count: " + sectionCount);
		//System.out.println("Slide Count: " + slideCount);

	}

	public static void main(String[] args) throws IOException {
		// Create instance of class FileChooser
		FileChoser newFile = new FileChoser("File chooser");

		// Make window vissible
		newFile.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		newFile.setSize(500, 400);
		newFile.setVisible(true);
	}

	public static HashMap<String, Doc> getMainHmp() {
		return mainHmp;
	}

	public static void setMainHmp(HashMap<String, Doc> mainHmp) {
		FileChoser.mainHmp = mainHmp;
	}
} // Dimension data structure
//getNumberOfLines();
//data = new String[numLines];

// Read file
//readTheFile();

// Output to text area	
//textArea.setText(data[0] + "\n");
//for(int index=1;index < data.length;index++)
//  textArea.append(data[index] + "\n");

// Rnable open button
//openButton.setEnabled(true);

/* GET NUMBER OF LINES */

/* Get number of lines in file and prepare data structure. */

/* READ FILE */
