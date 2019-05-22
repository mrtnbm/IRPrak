

import java.util.LinkedList;

/**
 * F�r das Suchen eines Wortes in allen Fabeln.
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class Search {

	/**
	 * Die Suche-Funktion sucht ein Wort in allen Fabeln und printed die Dateinamen
	 * der Fabeln aus, in dem sich das Wort befand. Wenn es keine Fabel gibt, die
	 * dieses Wort enth�lt, wird nichts ausgegeben.
	 * 
	 * @param linkList     Die Liste, in der gesucht werden sollen.
	 * @param searchString Der eingegebene String vom Benutzer, nach dem gesucht
	 *                     werden soll. Mithilfe des Escape-Charakters "$" k�nnen
	 *                     wir genau die L�nge des Inhaltes des Strings bestimmen.
	 *                     Dann extrahieren wir genau diese Teilstring aus dem
	 *                     Hauptstring heraus und erhalten nur das N�tigste.
	 */
	void search(LinkedList<String> linkList, String searchString) {
		int i = 0;
		String tempString;
		String pathToFile;

		for (String string : linkList) {

			i = string.indexOf("$");
			tempString = string.substring(0, i);

			if (tempString.equals(searchString)) {
				pathToFile = string.substring(i + 1, string.length());
				System.out.println(pathToFile);
			}

		}
	}
}
