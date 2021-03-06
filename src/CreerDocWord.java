import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STHdrFtr;


public class CreerDocWord {

    	static String docTitre="Titre";
    	static String docAuteur="Auteur";
    	static String docContexte="Contexte";
    	static String docDate="Date";
    	static String docURL="URL du document";
    	static String docMessagePersonnaliseeHeader="Message a Personnalisee dans Header";

        
        public static void captureEcran(String name) throws Exception{
 //       	BufferedImage image;
 //   		image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
 //   		ImageIO.write(image, "png", new File(name));
        	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        	  Rectangle screenRectangle = new Rectangle(screenSize);
        	  Robot robot = new Robot();
        	  BufferedImage image = robot.createScreenCapture(screenRectangle);
        	  ImageIO.write(image, "png", new File(name));
        }
        /**
         * 
         * @param name
         * @throws Exception
         */
        public static void InsererCaptureEcran(String imgFile,FileOutputStream fos ,XWPFDocument doc,XWPFParagraph title,XWPFRun run  ) throws Exception{
      	  
    		
    	    FileInputStream is = new FileInputStream(imgFile);
    	    
    	    int format=XWPFDocument.PICTURE_TYPE_PNG; // par defaut le format est PNG
            if(imgFile.endsWith(".emf")) format = XWPFDocument.PICTURE_TYPE_EMF;
            else if(imgFile.endsWith(".wmf")) format = XWPFDocument.PICTURE_TYPE_WMF;
            else if(imgFile.endsWith(".pict")) format = XWPFDocument.PICTURE_TYPE_PICT;
            else if(imgFile.endsWith(".jpeg") || imgFile.endsWith(".jpg")) format = XWPFDocument.PICTURE_TYPE_JPEG;
            else if(imgFile.endsWith(".png")) format = XWPFDocument.PICTURE_TYPE_PNG;
            else if(imgFile.endsWith(".dib")) format = XWPFDocument.PICTURE_TYPE_DIB;
            else if(imgFile.endsWith(".gif")) format = XWPFDocument.PICTURE_TYPE_GIF;
            else if(imgFile.endsWith(".tiff")) format = XWPFDocument.PICTURE_TYPE_TIFF;
            else if(imgFile.endsWith(".eps")) format = XWPFDocument.PICTURE_TYPE_EPS;
            else if(imgFile.endsWith(".bmp")) format = XWPFDocument.PICTURE_TYPE_BMP;
            else if(imgFile.endsWith(".wpg")) format = XWPFDocument.PICTURE_TYPE_WPG;
            else {
                System.err.println("Unsupported picture: " + imgFile +
                        ". Expected emf|wmf|pict|jpeg|png|dib|gif|tiff|eps|bmp|wpg");
            }

    	    run.addPicture(is,format, imgFile, Units.toEMU(450), Units.toEMU(450)); // 200x200 pixels
            run.addBreak();
    	    run.setText("Capture d' �cran");
    	    run.setBold(true);
    	    run.setUnderline(UnderlinePatterns.WORDS);
    	    title.setAlignment(ParagraphAlignment.CENTER);

    	    
    	    is.close();
    	//	fos.flush();

    	   	  
    	
    }   
        
      
        
        
        public static void InsererTableau( FileOutputStream out ,XWPFDocument document,XWPFParagraph title ) throws Exception{
        	
        	   
    	        
        	   //create table
        	   XWPFTable table = document.createTable();
        	
        	   //create first row
        	   XWPFTableRow tableRowOne = table.getRow(0);
        	   tableRowOne.getCell(0).setText("Titre");
        	   tableRowOne.addNewTableCell().setText(docTitre);
        
        	   //create second row
        	   XWPFTableRow tableRowTwo = table.createRow();
        	   tableRowTwo.getCell(0).setText("Auteur");
        	   tableRowTwo.getCell(1).setText(docAuteur);

        	   //create third row
        	   XWPFTableRow tableRowThree = table.createRow();
        	   tableRowThree.getCell(0).setText("Contexte");
        	   tableRowThree.getCell(1).setText(docContexte);
        	   
        	   //create third row
        	   XWPFTableRow tableRowFour = table.createRow();
        	   tableRowFour.getCell(0).setText("Date du test :");
        	   tableRowFour.getCell(1).setText(docDate);
        }
      	  
        
    public static void ajoutnumberpage(XWPFDocument doc ){
    	
    	CTP ctp = CTP.Factory.newInstance();
    	//this add page number incremental
    	ctp.addNewR().addNewPgNum();
 
    	
    	XWPFParagraph codePara = new XWPFParagraph(ctp, doc);
    	XWPFParagraph[] paragraphs = new XWPFParagraph[1];
    	paragraphs[0] = codePara;
    	//position of number
    	codePara.setAlignment(ParagraphAlignment.RIGHT);
    	CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
    	try {
    		XWPFHeaderFooterPolicy hfPolicy = null;
    	    XWPFFooter footer = null;
    	    List<XWPFParagraph> footerParas = null;
    	    XWPFParagraph para = null;
    		hfPolicy = doc.getHeaderFooterPolicy();
    		//if i haven't duplication of footer
    		if(hfPolicy!=null){
                footer = hfPolicy.getFooter(1);
                footerParas = footer.getParagraphs();
                para = footerParas.get(0);
                //if i haven't duplication of footer
    			if(para==null || para.getText()=="" || para.getText()==" " )
    			{
    			    XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);
    	    	    headerFooterPolicy.createFooter(STHdrFtr.DEFAULT, paragraphs);
    			}
            }else{
            	 XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);
    	    	 headerFooterPolicy.createFooter(STHdrFtr.DEFAULT, paragraphs);
            }

    	} catch (Exception e) {
    	    e.printStackTrace();
    	}	
    }
    
    public static void ajoutheader(XWPFDocument doc,String headerText) throws Exception{
 	   
    	CTP ctp = CTP.Factory.newInstance();
    	//this add page number incremental
   
		CTR ctrHeader = ctp.addNewR();
		CTText ctHeader = ctrHeader.addNewT();
		ctHeader.setStringValue(headerText);	
    	
    	XWPFParagraph codePara = new XWPFParagraph(ctp, doc);
    	XWPFParagraph[] paragraphs = new XWPFParagraph[1];
    	paragraphs[0] = codePara;
    	//position of number
    	codePara.setAlignment(ParagraphAlignment.RIGHT);
    	CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
    	try {
    		XWPFHeaderFooterPolicy hfPolicy = null;
    	    XWPFHeader header = null;
    	    List<XWPFParagraph> headerParas = null;
    	    XWPFParagraph para = null;
    		hfPolicy = doc.getHeaderFooterPolicy();
    		//if i haven't duplication of header
    		if(hfPolicy!=null){
                header = hfPolicy.getHeader(1);
                headerParas = header.getParagraphs();
                para = headerParas.get(0);
                //if i haven't duplication of header
    			if(para==null || para.getText()=="" || para.getText()==" " )
    			{
    			    XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);
    	    	    headerFooterPolicy.createHeader(STHdrFtr.DEFAULT, paragraphs);
    			}
            }else{
            	 XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy(doc, sectPr);
    	    	 headerFooterPolicy.createHeader(STHdrFtr.DEFAULT, paragraphs);
            }

    	} catch (Exception e) {
    	    e.printStackTrace();
    	}	
		
//		else{
//
//			//write header content
////			CTP ctpHeader = CTP.Factory.newInstance();
////		        CTR ctrHeader = ctpHeader.addNewR();
//			CTText ctHeader = ctrHeader.addNewT();
//			ctHeader.setStringValue(headerText);	
//			XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, doc);
//		        XWPFParagraph[] parsHeader = new XWPFParagraph[1];
//		        parsHeader[0] = headerParagraph;
//		        policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);
//		}
    }
                            
    	/**
    	 * @param args
    	 */
    	public static void  main(String[] args)throws Exception {
    		System.out.println("***********************************");
    		System.out.println("***********************************");
    		System.out.println("Ce programme effectue une capture d'ecran et la met dans un document word ");
    		System.out.println("Ce programme prend en parametre toutes les informations qui concernent le bug detecte");
    		System.out.println("***********************************");
    		System.out.println("Attention : il ne peut s'executer que si le document word de bug est ferm�");
    		System.out.println("***********************************");
    		System.out.println("***********************************");
    		int i ;
    		for( i = 0; i < args.length; i++) {            
                System.out.println("arg["+i+"]="+args[i]);
            }
    		if (i>4){
    			docTitre=args[0];
    			docAuteur=args[1];
    			docContexte=args[2];
    			docDate=args[3];
    			docURL=args[4];
    			docMessagePersonnaliseeHeader=args[5]; 
    		}else{
    			System.out.println("bad arg");
    		}		
    		
   		
    		String nameScreenShot = "/TestKarren/logiciel/words/screenshot2.png";
    		//String nameScreenShot = "/screenshot2.png";
    		FileInputStream in = new FileInputStream(docURL);
    		
    		XWPFDocument doc = new XWPFDocument(in); 
     		in.close();
    		XWPFParagraph title = doc.createParagraph();    
    		XWPFRun run = title.createRun();

    		FileOutputStream out = new FileOutputStream(docURL);
    				
    		

    	

    		
    		InsererTableau(out,doc,title);
    		
    	//	captureEcran(nameScreenShot);
    		run.addBreak(BreakType.PAGE);   //  saut de page 
    		InsererCaptureEcran(nameScreenShot,out,doc,title,run);


    		//ajoutnumberpage(doc);

    	//	ajoutheader(doc,docMessagePersonnaliseeHeader);
    		
    		
    		/**
    		 * �criture et fermeture
    		 */
 /*   	*/	
    		doc.write(out);
    		out.flush();
    		out.close();   
   
    		System.out.println("fin de creer document word");
    		return ;
    		

    		   
                

    	}

}
