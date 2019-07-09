
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Klasse für die generelle Dokumentverwaltung
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class Document {
	ArrayList<String> text;
	String str = "";

	/**
	 * Methode, um einen Text einzulesen und diesen in einen String zu speichern, um
	 * später auf diesem Wortsuchen zu machen.
	 * 
	 * @param path Der Pfad, in dem sich die Textdatei befindet.
	 */
	void importText(String path) {
		FileReader doc = null;
		try {
			doc = new FileReader(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader read2 = new BufferedReader(doc);
		String tempstr;
		try {
			do {
				tempstr = read2.readLine();

				str = str + "/n" + tempstr;

			} while (tempstr != null);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Aufteilen des Textes in den Titel und den Inhalt, damit der Titel als Name
	 * der Textdatei gesetzt werden kann.
	 * 
	 * In der ersten do-while-Schleife wird mithilfe von start und dem Laufindex i
	 * der Titel aller Fabeln extrahiert und in eine neue Datei jeweils geschrieben.
	 * 
	 * In der zweiten while-Schleife wird der Content für jeweils alle Fabeln
	 * extrahiert und in die dazugehörige Datei geschrieben. Außerdem wird der Titel
	 * in Kleinschreibweise geändert und dann die Leerzeichen durch Unterstriche
	 * ausgetauscht. Dieser Name wird dann der Dateiname der txt-Datei.
	 * 
	 * @param text   Der zu teilende Text
	 * @param start  Start des eigentlichen Textes ohne das Intro
	 * @param saveTo Der Pfad, in dem die Datei gespeichert werden soll.
	 */
	void splitText(String text, String start, String saveTo) {
		int i = 0;
		int j = 0;
		int x = 0;
		// Anfang vom Titel
		do {
			x = i;
			i += 6;
			i = text.indexOf(start, i);

			// Abfangen, wenn es keinen Inhalt mehr mit start gibt.
		} while (!(i == (-1)));

		i = x;
		i += start.length();

		int counter = 1;

		while (i < text.length() - 8) {
			i += 8;

			// Anfang des Contents
			j = text.indexOf("/n/n/n", i);

			if (j == -1) {
				break;
			}

			String title = text.substring(i, j);
			// Abfangen, falls j größer als Textlänge
			if (j > text.length() - 4) {
				break;
			}
			j += 6;

			// Anfang nächster Titel
			i = text.indexOf("/n/n/n/n", j);

			/*
			 * Abfangen, wenn es keinen Inhalt mehr mit "/n/n/n/n" gibt. Dann gibt die
			 * indexOf()-Funktion -1 aus."
			 */
			if (i == -1) {
				break;
			}
			// Den Inhalt der Fabel aus dem Text extrahieren
			String content = text.substring(j, i);
			// Geben den Content zur Überprüfung aus
			System.out.println("Content= " + content);

			title = title.toLowerCase();
			title = title.replace(" ", "_") + "#" + counter + ".txt";
			System.out.println("Titel: " + title);

			content = content.replace(".", " ");
			content = content.replace(":", " ");
			content = content.replace(",", " ");
			content = content.replace(";", " ");
			content = content.replace("\"", " ");
			content = content.replace("!", " ");
			content = content.replace("?", " ");

			counter++;

			try {
				FileWriter fw = new FileWriter(saveTo + title);
				BufferedWriter bw = new BufferedWriter(fw);

				int line = 0;
				int nextLine = 0;
				String temp;
				do {
					nextLine = content.indexOf("/n", line);
					if (nextLine == -1) {
						nextLine = content.length();
					}
					temp = content.substring(line, nextLine);
					bw.write(temp);
					bw.newLine();
					line = 2 + nextLine;

				} while (nextLine != content.length());

				bw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
