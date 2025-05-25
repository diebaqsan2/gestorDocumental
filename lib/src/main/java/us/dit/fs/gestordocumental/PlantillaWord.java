package us.dit.fs.gestordocumental;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Clase que facilita la creación de un documento word
 * 
 */
public class PlantillaWord {
	private String resultado;
    private XWPFDocument document;
    
    /**
     * Constructor con los datos
     * @param titulo Título del documento
     * @param ficheroResultado Nombre del fichero word que se va a generar
     */
    public PlantillaWord(String titulo, String ficheroResultado) {
		super();
		document = new XWPFDocument();		
		this.resultado = ficheroResultado;
		addTitle(titulo);		
	}    
    
    /**
     * Constructor con el dato
     * @param ficheroResultado Nombre del fichero word que se va a generar
     */
    public PlantillaWord(String ficheroResultado) {
		super();			
		document = new XWPFDocument();		
		this.resultado = ficheroResultado;				
	}
    
    /**
     * Método para añadir un párrafo con un formato determinado
     * @param ficheroParrafo Nombre del fichero de texto que contiene el texto del párrafo
     */
    public void addParagraph(String ficheroParrafo) {
    	 String texto = convertTextFileToString(ficheroParrafo);
    	 XWPFParagraph para1 = document.createParagraph();
         para1.setAlignment(ParagraphAlignment.BOTH);          
         XWPFRun para1Run = para1.createRun();
         para1Run.setFontFamily("Courier");
         para1Run.setColor("0000FF");
         para1Run.setFontSize(20);
         para1Run.setText(texto);
         para1Run.setBold(true);
    }    
    /**
     * Método para añadir un subtítulo con un formato determinado
     * @param ficheroParrafo Nombre del fichero de texto que contiene el texto del subtítulo
     */
    public void addSubtitle(String ficheroSubtitulo) {
    	 String texto = convertTextFileToString(ficheroSubtitulo);
    	 XWPFParagraph subTitle = document.createParagraph();
         subTitle.setAlignment(ParagraphAlignment.CENTER);         
         XWPFRun subTitleRun = subTitle.createRun();
         subTitleRun.setText(texto);
         subTitleRun.setColor("00CC44");
         subTitleRun.setFontFamily("Courier");         
         subTitleRun.setFontSize(16);
         subTitleRun.setTextPosition(20);
         subTitleRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
    }    
    
    /**
     * Método para añadir un título con un formato determinado     * 
     * @param titulo Texto del título
     */
    public void addTitle(String titulo) {
    	XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(titulo);
        titleRun.setColor("FF4500");       
        titleRun.setFontFamily("Courier");
        titleRun.setFontSize(12);
    }   
    /**
     * Método para cerrar y guardar el documento
     */
    public void finishDocument() {
  	  FileOutputStream out;
		try {
			out = new FileOutputStream(resultado);
			document.write(out);
	        out.close();
	        document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
  }   
    /**
     * Método para convertir el contenido de un fichero de texto en un String
     * @param fileName Nombre del fichero de texto
     * @return El contenido del fichero de texto como un objeto String
     */
    public String convertTextFileToString(String fileName) {
        try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemResource(fileName).toURI()))) {
            return stream.collect(Collectors.joining(" "));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }


}
