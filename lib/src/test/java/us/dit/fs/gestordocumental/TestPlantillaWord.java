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

	/**
	 * Verifica que el subtítulo tenga tamaño de fuente 16 puntos
	 *
	 * @throws Exception
	 */
	@Test
	public void whenSubtitleHasCorrectFontSize_thenPass() throws Exception {
		Path msWordPath = Paths.get("Quijote.docx");
		logger.info("Verificando tamaño del subtítulo en {}", msWordPath);

		try (XWPFDocument document = new XWPFDocument(Files.newInputStream(msWordPath))) {
			List<XWPFParagraph> paragraphs = document.getParagraphs();

			// El subtítulo es el segundo párrafo
			XWPFParagraph subtitleParagraph = paragraphs.get(1);

			boolean fontSizeCorrect = false;
			int actualFontSize = -1; // Valor por defecto si no se encuentra tamaño
			for (XWPFRun run : subtitleParagraph.getRuns()) {
				if (run.getFontSize() != -1) { // -1 si no está definido explícitamente
					actualFontSize = run.getFontSize();
					logger.info("Tamaño encontrado en run: {}", actualFontSize);
					if (actualFontSize == 16) {
						fontSizeCorrect = true;
						break;
					}
				}
			}

			// Mensaje con valores "Expected" y "Actual"
			assertTrue(fontSizeCorrect,
					String.format("Expected: 16, Actual: %d", actualFontSize));
		}
	}
}





   
