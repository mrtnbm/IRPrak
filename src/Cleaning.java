
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class Cleaning {

	// dynamische Größe
	LinkedList<String> stopw;

	/**
	 * Eliminierung der Stoppwörter mithilfe der Stoppwortliste, mit der wir dann
	 * Wörter entfernen können. Eingabeparameter ist ein String, auf dem die
	 * Stoppwortelimination ausgeführt werden soll. Um Groß- und Kleinschreibung zu
	 * ignorieren, wenden wir die toLowerCase-Funktion an. Im for-Loop wird dann
	 * jeweils der String der Stoppwortliste mit dem des eingegebenen Textes
	 * verglichen. Bei Gleichheit wird dieser entfernt. Wir haben hier
	 * unterschiedliche Fälle betrachtet, damit auch Leerzeilen richtig gesetzt
	 * werden, nachdem ein Stoppwort eliminiert wurde.
	 * 
	 * @param str String, auf dem die Stoppworteliminierung ausgeführt werden soll.
	 * @return Zurückgegeben wird der String ohne die Stoppwörter (diese wurden
	 *         gelöscht)
	 */
	String elimStopWords(String str) {
		str = " " + str + " ";
		str = str.toLowerCase();

		for (String sw : stopw) {
			str = str.replace(" " + sw + " ", " ");
			str = str.replace("/n" + sw + " ", "/n");
			str = str.replace(" " + sw + "/n", "/n");
			str = str.replace("/n" + sw + "/n", "/n");
		}
		return str;
	}

	/**
	 * In dieser Methode lesen wir die Stoppwörter einer gegebenen Stoppwortliste
	 * aus und speichern diese in einem String.
	 * 
	 * @param path Beschreibt den Pfad, in der sich die Datei mit der Stoppwortliste
	 *             befindet. Dazu lesen wir in einer Schleife jeden String der
	 *             Stoppwortliste und speichern diese in einem Stoppwortstring, so
	 *             lange wir eine neue Zeile lesen können.
	 */
	void readStopWords(String path) {
		String x = "";
		stopw = new LinkedList<String>();
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			do {
				// x=Stopwords
				x = br.readLine();
				stopw.add(x);

			} while (x != null);
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException f) {
			// TODO Auto-generated catch block
			f.printStackTrace();
		}

	}

	/**
	 * Liest nacheinander alle Dokumente mit den einzelnen, extrahierten Fabeln ein
	 * und speichert diese dann mit der Stoppworteliminierung in einem extra Ordner.
	 * Dazu lesen wir iterativ jede Datei aus und fügen dann alle Inhalte wieder
	 * vereint in einen String string, auf den dann die Stoppworteliminierung
	 * ausgeführt wird. Diesen String schreiben wir dann in der selben Iteration in
	 * eine neue Datei auf der die Stoppwortelimination aufgerufen worde. Diese
	 * befindet sich dann im writePath. Wir separieren also die normalen,
	 * extrahierten Dokumente von denen, die mithilfe der Stoppwortelimination
	 * entstanden sind, durch verschiedene Ordnerpfade, in denen sie gespeichert
	 * werden.
	 * 
	 * @param readPath  Der Pfad, in dem sich die einzelnen, extrahierten Fabeln mit
	 *                  den jeweils gleichnamigen Dateinamen befinden. Diese sind
	 *                  noch nicht mithilfe der Stoppworteliminierung verändert
	 *                  worden.
	 * @param writePath Der Pfad, in den die extrahierten Fabeln geschrieben werden,
	 *                  auf denen eine Stoppworteliminierung ausgeführt worden ist.
	 */
	void openSave(String readPath, String writePath) {
		String temp;
		String out = "";
		String content = "";

		try {

			final File folder = new File(readPath);
			for (final File fileEntry : folder.listFiles()) {

				FileReader fr = new FileReader(fileEntry);
				BufferedReader br = new BufferedReader(fr);
				out = "";

				do {
					temp = br.readLine();
					out += temp + "/n";
				} while (temp != null);

				content = elimStopWords(out);

				// Datei wird dann in path/file.txt geschrieben
				FileWriter fw = new FileWriter(writePath + fileEntry.getPath());
				BufferedWriter bw = new BufferedWriter(fw);

				int line = 0;
				int nextLine = 0;
				do {
					nextLine = content.indexOf("/n", line);
					if(nextLine == -1) {
						nextLine = content.length();
					}
					temp = content.substring(line, nextLine);
					bw.write(temp);
					bw.newLine();
					line = 2 + nextLine;
					
				} while (nextLine != content.length());


				bw.close();
				br.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
