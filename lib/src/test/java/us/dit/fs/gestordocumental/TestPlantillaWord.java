package us.dit.fs.gestordocumental;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


class TestPlantillaWord {
	private static final Logger logger = LogManager.getLogger(TestPlantillaWord.class);
	static PlantillaWord wordDocument;

	@BeforeAll
	public static void generateMSWordFile() throws Exception {
		/**
		 * El test comienza creando un documento Word llamado Quijote.docx
		 * El título será "Capítulo primero"
		 * El texto del resto del documento depende del contenido de los ficheros subtitulo.txt y parrafo.txt
		 */
		TestPlantillaWord.wordDocument = new PlantillaWord("Quijote.docx");
		wordDocument.addTitle("Capítulo primero");
		wordDocument.addSubtitle("subtitulo.txt");
		wordDocument.addParagraph("parrafo.txt");
		wordDocument.finishDocument();
	}

	/**
	 * Verifica el contenido del documento Word creado
	 *
	 * @throws Exception
	 */
	@Test
	public void whenParsingOutputDocument_thenCorrect() throws Exception {
		Path msWordPath = Paths.get("Quijote.docx");
		logger.info("path ", msWordPath);
		XWPFDocument document = new XWPFDocument(Files.newInputStream(msWordPath));
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		document.close();
		assertEquals("Capítulo primero", paragraphs.get(0).getText());
		assertEquals(wordDocument.convertTextFileToString("subtitulo.txt"), paragraphs.get(1).getText());
		assertEquals(wordDocument.convertTextFileToString("parrafo.txt"), paragraphs.get(2).getText());

	}

	@Test
	public void whenCheckingSubtitleFontSize_thenCorrect() throws Exception {
		// Ruta al documento de prueba
		Path msWordPath = Paths.get("Quijote.docx");
		logger.info("path {}", msWordPath);

		// Abrir el documento
		try (XWPFDocument document = new XWPFDocument(Files.newInputStream(msWordPath))) {
			// Obtener los párrafos
			List<XWPFParagraph> paragraphs = document.getParagraphs();

			// Comprobar que hay al menos dos párrafos
			assertTrue(paragraphs.size() > 1);

			// Obtener el segundo párrafo (subtítulo)
			XWPFParagraph subtitleParagraph = paragraphs.get(1);

			// Obtener los "runs" (segmentos de texto con estilo)
			List<XWPFRun> runs = subtitleParagraph.getRuns();

			// Comprobar que hay al menos un run
			assertFalse(runs.isEmpty());

			// Tomar el primer run (asumimos que el tamaño se define aquí)
			XWPFRun run = runs.get(0);

			// Obtener el tamaño de la fuente
			int fontSize = run.getFontSize();

			// Comprobar que el tamaño de fuente es 12
			assertEquals(16, fontSize);
		}
	}
}





   
