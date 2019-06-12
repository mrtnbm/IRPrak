

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Die lineare Liste, in der dann nach W�rtern gesucht wird.
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class List {

	LinkedList<String> linkedList;

	/**
	 * Generierung einer linearen Liste und speichern in der Klasse selbst.
	 * Besonderheit ist der Escape Character "$", mit dem wir in der Search
	 * identifizieren k�nnen, wann das analysierte Wort zu Ende ist. Die Funktion
	 * der for-Schleife ist wieder das iterative �ffnen und einlesen der Inhalte der
	 * Fabeln, um auf denen dann eine Suche ausf�hren zu k�nnen.(Siehe openSave() in
	 * Klasse Cleaning.) Der einzige Unterschied ist, dass wir hier dann noch
	 * jeweils jeden Inhalt der Fabel an die Liste mit einem "$" anh�ngen, welches
	 * das Ende der Fabel makiert.
	 * 
	 * @param path Der Pfad, wo die Dateien liegen.
	 */
	void makeLinearList(String path) {
		String tempString;
		String out = "";
		String linearWord = "";
		linkedList = new LinkedList<String>();
		int i = 0;
		int j = 0;
		try {
			final File folder = new File(path);
			for (final File fileEntry : folder.listFiles()) {

				FileReader fr = new FileReader(fileEntry);
				BufferedReader br = new BufferedReader(fr);

				out = "";

				do {
					tempString = br.readLine();
					out += tempString;
				} while (tempString != null);

				do {
					if ((i != j) && (j < out.length() - 1) && (i < out.length() - 1) && (j != -1)) {

						linearWord = out.substring(i, j);
					}
					i = j;

					linkedList.add(linearWord + "$" + fileEntry.getName());
					i++;

					j = out.indexOf(" ", i);
					// Abfangen, wenn es keinen Inhalt mehr mit " " gibt
				} while (j != -1);

				br.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
