
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

	LinkedList<String> makeLinearList(String path, LinkedList<String> files) {
		String tempString;
		String out = "";
		String linearWord = "";
		LinkedList<String> linkedList = new LinkedList<String>();
		int i = 0;
		int j = 0;
		int printI = 0;
		System.out.print("Lineare Listen werden erstellt");
		try {
			for (String fileName : files) {
				printI++;
				File fileEntry = new File(path+fileName);

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
				if((printI % 100) == 0) {
					System.out.print(".");
				}

				br.close();
			}
			System.out.println();

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
		LinkedList<String> tempList = new LinkedList<String>();
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

					linearWord = hashSign(linearWord);
					tempList.add(linearWord);
					if(i>2) {
						String blockString = createBlock(tempList);
						linkedList.add(blockString + "$" + fileEntry.getName());
						tempList.remove(0);
					}
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

	public String createBlock(LinkedList<String> tempList) {
		String word1 = tempList.get(0);
		String word2 = tempList.get(1);
		String word3 = tempList.get(2);
		String out = "";
		for(int i = 0; i<word1.length(); i++) {
			int a = Integer.valueOf(word1.substring(i,i+1));
			int b = Integer.valueOf(word2.substring(i,i+1));
			int c = Integer.valueOf(word3.substring(i,i+1));
			int d = a|b|c;
			out += d;
		}
		return out;
	}

	public String hashSign(String word) {
		int hash = 0;
		
		for(int i = 0; i<word.length() - 1; i++) {
			hash = (hash + word.charAt(i))*347; //wir muessen eine feste Primzahl nehmen, da ansonsten die Hash-Funktion nicht reproduzierbar
		}
		
		int bits = (int) Math.pow(2,16);//F ist 2^16 (Laenge des Binaercoes)
		hash = hash % bits;
		if(hash<0) {
			hash += bits;
		}
		
		String binaer = String.format("%16s", Integer.toBinaryString(hash)).replace(' ', '0');
			
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

	/**
	 * Implementierung der invertierten Liste
	 * 
	 * @param path Pfad, in der die Dateien zur Suche sich befinden.
	 * @return
	 */
	LinkedList<String> makeInvertedSumList(String path) {
		String tempString;
		String out = "";
		String invertedWord = "";
		LinkedList<String> invertedList = new LinkedList<String>();
		int i = 0;
		int j = 0;
		int documentI = 0;
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
							
							String filename = fileEntry.getName();
							int position = entry.indexOf(filename);
							if (position == -1) { //Das Wort existiert, aber nicht fuer dieses Dokument
								invertedList.set(wordIndex,entry + "$" + filename + "@" + 0);
							} else {
							position = entry.indexOf("@", position + 1);
							int positionEnd = entry.indexOf("$", position);
							if(positionEnd == -1) {
								positionEnd = entry.length();
							}
							
							String before = entry.substring(0, position + 1);
							String number = entry.substring(position + 1, positionEnd);
							String after = entry.substring(positionEnd, entry.length());
							
							int count = Integer.valueOf(number);
							count++;
							number = String.valueOf(count);
							
							entry = before + number + after;
							invertedList.set(wordIndex, entry); //ersetze Wort in der invertierten Liste
							}
						}
						
					}
					if (!exist) {
						invertedList.add(invertedWord + "$" + fileEntry.getName() + "@" + 0);
					}

					i++;

					j = out.indexOf(" ", i);

					// Abfangen, wenn es keinen Inhalt mehr mit " " gibt
				} while (j != -1);

				out = "";
				j = 0;
				i = 0;
				documentI++;
				
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
