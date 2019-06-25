
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Die lineare Liste, in der dann nach Wörtern gesucht wird.
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class List {

	/**
	 * Generierung einer linearen Liste und speichern in der Klasse selbst.
	 * Besonderheit ist der Escape Character "$", mit dem wir in der Search
	 * identifizieren können, wann das analysierte Wort zu Ende ist. Die Funktion
	 * der for-Schleife ist wieder das iterative öffnen und einlesen der Inhalte der
	 * Fabeln, um auf denen dann eine Suche ausführen zu können.(Siehe openSave() in
	 * Klasse Cleaning.) Der einzige Unterschied ist, dass wir hier dann noch
	 * jeweils jeden Inhalt der Fabel an die Liste mit einem "$" anhängen, welches
	 * das Ende der Fabel makiert.
	 * 
	 * @param path Der Pfad, wo die Dateien liegen.
	 */
	LinkedList<String> makeLinearList(String path) {
		String tempString;
		String out = "";
		String linearWord = "";
		LinkedList<String> linkedList = new LinkedList<String>();
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
		return linkedList;
	}

	LinkedList<String> makeSignaturList(String path) {
		String tempString;
		String out = "";
		String linearWord = "";
		LinkedList<String> linkedList = new LinkedList<String>();
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

					linearWord = hashSign(linearWord, i);

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
		return linkedList;
	}

	private String hashSign(String word, Integer index) {
		int[] primes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89,
				97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197,
				199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317,
				331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449,
				457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593,
				599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727,
				733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863,
				877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997 };

		index = index % (primes.length);
		if(index<0) {
			index += primes.length;
		}

		int hash = 0;
		
		for(int i = 0; i<word.length() - 1; i++) {
			hash = (hash + word.charAt(i))*primes[index];
		}
		hash = hash % 2^32;//F ist 2^32 (Laenge des Binaercoes)
		String binaer = String.format("%32s", Integer.toBinaryString(hash)).replace(' ', '0');
			
		return binaer;
	}

	/**
	 * Implementierung der invertierten Liste
	 * 
	 * @param path Pfad, in der die Dateien zur Suche sich befinden.
	 * @return
	 */
	LinkedList<String> makeInvertedList(String path) {
		String tempString;
		String out = "";
		String invertedWord = "";
		LinkedList<String> invertedList = new LinkedList<String>();
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

						invertedWord = out.substring(i, j);
					}
					i = j;

					boolean exist = false;
					for (int wordIndex = 0; wordIndex < invertedList.size(); wordIndex++) {
						String entry = invertedList.get(wordIndex);

						String word = entry.substring(0, entry.indexOf("$"));
						if (invertedWord.equals(word)) {
							exist = true;
							entry += "$" + fileEntry.getName();
							invertedList.set(wordIndex, entry);
						}
					}
					if (!exist) {
						invertedList.add(invertedWord + "$" + fileEntry.getName());
					}

					i++;

					j = out.indexOf(" ", i);

					// Abfangen, wenn es keinen Inhalt mehr mit " " gibt
				} while (j != -1);

				br.close();

				System.out.println(fileEntry);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return invertedList;
	}

}
