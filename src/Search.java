
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.stream.Stream;

/**
 * Für das Suchen eines Wortes in allen Fabeln.
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class Search {

	LinkedList<String> searchList = new LinkedList<String>();

	/**
	 * Die Suche-Funktion sucht ein Wort in allen Fabeln und printed die Dateinamen
	 * der Fabeln aus, in dem sich das Wort befand. Wenn es keine Fabel gibt, die
	 * dieses Wort enthält, wird nichts ausgegeben.
	 * 
	 * @param linkList     Die Liste, in der gesucht werden sollen.
	 * @param searchString Der eingegebene String vom Benutzer, nach dem gesucht
	 *                     werden soll. Mithilfe des Escape-Charakters "$" können
	 *                     wir genau die Länge des Inhaltes des Strings bestimmen.
	 *                     Dann extrahieren wir genau diese Teilstring aus dem
	 *                     Hauptstring heraus und erhalten nur das Nötigste.
	 */
	void search(LinkedList<String> linkList, String searchString) {
		int i = 0;
		String tempString;
		String pathToFile;

		for (String string : linkList) {

			i = string.indexOf("$");
			tempString = string.substring(0, i);
			tempString = tempString.toLowerCase();
			if (tempString.equals(searchString)) {
				pathToFile = string.substring(i + 1, string.length());
			}

		}
	}

	/**
	 * Boolsche Suche
	 * 
	 * @param list         LinkedList String list
	 * @param searchString String mit dem Text, auf dem gesucht werden soll
	 * @return
	 */
	LinkedList<Integer> doSearchBool(LinkedList<String> list, String searchString) {
		this.searchList = list;
		LinkedList<String> input = new LinkedList<String>();
		searchString = searchString.toLowerCase();
		input.add(searchString);
		input.add("1"); // lastPositon darf nicht kleiner 1 sein.
		LinkedList<String> output = boolCombin(input);
		if (output.size() == 0) {
			System.out.println("Keine Ergebnisse.");
		}

		LinkedList<Integer> numbers = new LinkedList<Integer>();
		for (String result : output) {
			int numberStart = result.indexOf("#");
			int numberEnd = result.indexOf(".txt");
			int number = Integer.valueOf(result.substring(numberStart + 1, numberEnd));
			if (!numbers.contains(number)) {
				System.out.println(result);
				numbers.add(number);
			}
		}
		return numbers;
	}

	LinkedList<String> searchBool(String searchString) {
		LinkedList<String> linkList = this.searchList;
		int i = 0;
		String tempString;
		String pathToFile;
		LinkedList<String> results = new LinkedList<String>();

		for (String string : linkList) {
			i = string.indexOf("$");
			tempString = string.substring(0, i);
			tempString = tempString.toLowerCase();
			if (tempString.equals(searchString)) {
				int j = i;
				do {
					j++;
					int k = string.indexOf("$", j);
					if (k == -1) {
						k = string.length();
					}
					pathToFile = string.substring(j, k);
					results.add(pathToFile);
					j = k;
				} while (j != string.length());
			}
		}
		return results;

	}

	LinkedList<String> searchAll() {
		LinkedList<String> linkList = this.searchList;
		int i = 0;
		String tempString;
		String pathToFile;
		LinkedList<String> results = new LinkedList<String>();

		for (String string : linkList) {

			i = string.indexOf("$");
			tempString = string.substring(0, i);

			pathToFile = string.substring(i + 1, string.length());
			if (!results.contains(pathToFile)) {
				results.add(pathToFile);
			}
		}
		return results;
	}

	LinkedList<String> boolCombin(LinkedList<String> all) {

		String positionString = all.get(1);
		int lastPosition = Integer.parseInt(positionString);

		String input = all.get(0);

		String searchChar = input.substring(lastPosition - 1, lastPosition); // logische Operation vor dem Wort
		String searchNotChar = null;

		boolean not = false;
		if (lastPosition < input.length() - 1) {// falls der zu suchende Character noch nicht an der letzten Position
			searchNotChar = input.substring(lastPosition, lastPosition + 1);
			if (searchNotChar.equals("!")) {
				not = true;
				lastPosition++;
			}
		}

		int notIndex = input.indexOf("!", lastPosition);
		int orIndex = input.indexOf("|", lastPosition);
		int andIndex = input.indexOf("&", lastPosition);
		int logic = -1; // gibt Ende des Wortes an

		if (orIndex == -1) { // minimales naechstes Zeichen
			if (andIndex == -1) {
				logic = notIndex;
			} else {
				if (notIndex == -1) {
					logic = andIndex;
				} else {
					logic = Math.min(andIndex, notIndex);
				}
			}
		} else if (andIndex == -1) {
			if (notIndex == -1) {
				logic = orIndex;
			} else {
				logic = Math.min(notIndex, orIndex);
			}
		} else if (notIndex == -1) {
			logic = Math.min(orIndex, andIndex);
		} else {
			int orAnd = Math.min(orIndex, andIndex);
			logic = Math.min(orAnd, notIndex);
		}

		if (logic == -1) {// letztes Wort erreicht
			logic = input.length() - 1;
		}
		logic++;

		String word;
		if (logic == input.length()) { // wenn letztes Wort erreicht
			word = input.substring(lastPosition, logic); // aktuelles Wort
		} else {
			word = input.substring(lastPosition, logic - 1); // aktuelles Wort
		}

		LinkedList<String> results = searchBool(word);
		{
			all.remove(0);
			all.remove(0);

			LinkedList<String> resultBool = new LinkedList<String>();
			LinkedList<String> resultNot = new LinkedList<String>();

			if (not) {
				LinkedList<String> allDocs = searchAll();
				for (String firstWord : allDocs) {
					if (!results.contains(firstWord)) {
						resultNot.add(firstWord);
					}
				}
				results = resultNot;
			}
			if (searchChar.equals("|")) {
				for (String firstPath : all) {
					int i = results.indexOf(firstPath);
					if (i == -1) {// Pfad ist nicht im Ergebnis enthalten
						resultBool.add(firstPath); // Pfad wird hinzugefuegt
					} else { // Pfad ist in im Ergebnis enthalten
						resultBool.add(firstPath); // Pfad wird hinzugefuegt
						results.remove(i); // Pfad wird aus Ergebnis entfernt.
					}
				}
				resultBool.addAll(results); // Pfade aus Ergebnis, die nicht bereits vorhanden sind, hinzufuegen
			} else if (searchChar.equals("&")) {
				for (String firstWord : all) {
					if (results.contains(firstWord)) {
						resultBool.add(firstWord);
					}
				}

			} else if (searchChar.equals("!")) {
				LinkedList<String> allDocs = searchAll();
				for (String firstWord : allDocs) {
					if (!results.contains(firstWord)) {
						resultBool.add(firstWord);
					}
				}
			} else { // vor dem Wort existiert kein Ausdruck
				if (logic == input.length()) { // wenn letztes Wort erreicht
					word = input.substring(0, logic); // aktuelles Wort
				} else {
					word = input.substring(0, logic - 1); // aktuelles Wort
				}
				results = searchBool(word);
				resultBool = results;
			}

			if (logic == input.length()) { // wenn letztes Wort erreicht
				return resultBool;
			}

			resultBool.add(0, String.valueOf(logic)); // remember last boolean
			resultBool.add(0, input);
			return boolCombin(resultBool);
		}
	}

	LinkedList<String> doSearchOr(LinkedList<String> list, String searchString) {
		this.searchList = list;
		LinkedList<String> input = new LinkedList<String>();
		searchString = searchString.toLowerCase();
		input.add(searchString);
		input.add("1"); // lastPositon darf nicht kleiner 1 sein.
		LinkedList<String> output = orCombin(input);

		return output;
	}

	LinkedList<String> orCombin(LinkedList<String> all) {

		List binaer = new List();

		String positionString = all.get(1);
		int lastPosition = Integer.parseInt(positionString);

		String input = all.get(0);

		String searchChar = input.substring(lastPosition - 1, lastPosition); // logische Operation vor dem Wort

		int orIndex = input.indexOf("|", lastPosition);
		int andIndex = input.indexOf("&", lastPosition);
		int logic = -1; // gibt Ende des Wortes an

		if (orIndex == -1) { // minimales naechstes Zeichen
			if (andIndex == -1) {
			} else {
				logic = andIndex;
			}
		} else if (andIndex == -1) {
			logic = orIndex;
		} else {
			logic = Math.min(orIndex, andIndex);
		}

		if (logic == -1) {// letztes Wort erreicht
			logic = input.length();
		}

		String word = input.substring(lastPosition, logic); // aktuelles Wort
		word = binaer.hashSign(word);

		LinkedList<String> resultBool = new LinkedList<String>(); // Ausgabeliste
		LinkedList<String> results = searchSignature(word);
		{
			all.remove(0);
			all.remove(0);

			if (searchChar.equals("|")) {
				for (String firstPath : all) {
					int i = results.indexOf(firstPath);
					if (i == -1) {// Pfad ist nicht im Ergebnis enthalten
						resultBool.add(firstPath); // Pfad wird hinzugefuegt
					} else { // Pfad ist in im Ergebnis enthalten
						resultBool.add(firstPath); // Pfad wird hinzugefuegt
						results.remove(i); // Pfad wird aus Ergebnis entfernt.
					}
				}
				resultBool.addAll(results); // Pfade aus Ergebnis, die nicht bereits vorhanden sind, hinzufuegen
				all = resultBool;
			} else if (searchChar.equals("&")) {
				for (String firstWord : all) {
					if (results.contains(firstWord)) {
						resultBool.add(firstWord);
					}
				}
				all = resultBool;
			} else { // vor dem Wort existiert kein Ausdruck
				word = input.substring(lastPosition - 1, logic); // aktuelles Wort
				word = binaer.hashSign(word);
				all = searchSignature(word);
			}

			if (logic == input.length()) { // beim letzten Wort
				return all;
			}

			logic++;
			all.add(0, String.valueOf(logic)); // remember last boolean
			all.add(0, input);
			return orCombin(all);
		}
	}

	private LinkedList<String> searchSignature(String word) {
		LinkedList<String> linkList = this.searchList;
		int i = 0;
		String tempString;
		String pathToFile;
		LinkedList<String> results = new LinkedList<String>();

		for (String string : linkList) {
			i = string.indexOf("$");
			tempString = string.substring(0, i);
			tempString = tempString.toLowerCase();
			if (compareAnd(tempString, word)) {
				int j = i;
				do {
					j++;
					int k = string.indexOf("$", j);
					if (k == -1) {
						k = string.length();
					}
					pathToFile = string.substring(j, k);
					results.add(pathToFile);
					j = k;
				} while (j != string.length());
			}
		}
		return results;
	}

	private boolean compareAnd(String tempString, String word) {
		for (int i = 0; i < word.length(); i++) {
			char a = tempString.charAt(i);
			if (a == word.charAt(i)) {
				if (a == '1') { // beide Werte muessen 1 sein, damit logisches UND erfuellt
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Errechnung des Nenners der Salton-Formel
	 * 
	 * @param file       Dokument, mit dem die Formel berechnet werden soll
	 * @param pathString Pfad, indem sich die Datei befindet.
	 * @return double
	 */
	double sumWordFrequency(String file, String pathString) {
		double bigN = 0;
		int bigNi = 0;

		final File folder = new File(pathString);
		for (final File fileEntry : folder.listFiles()) {
			bigNi++;
		}
		bigN = bigNi;

		int i = 0;
		String invertedWord = "";
		String out;

		FileReader fr;

		double outNumber = 0;

		try {
			fr = new FileReader(pathString + file);
			BufferedReader br = new BufferedReader(fr);

			out = "";

			String tempString;
			do {

				tempString = br.readLine();
				out += tempString;
			} while (tempString != null);

			int j = out.indexOf(" ", i);
			LinkedList<String> found = new LinkedList<String>();

			do { // durch das Dokument durchgehen und alle Woerter ermitteln
				if ((i != j) && (j < out.length() - 1) && (i < out.length() - 1) && (j != -1)) {
					invertedWord = out.substring(i, j);
				}
				i = j;

				double tfdi = 0;
				int ni = 0;

				for (int wordIndex = 0; wordIndex < searchList.size(); wordIndex++) {
					String entry = searchList.get(wordIndex);

					String word = entry.substring(0, entry.indexOf("$"));
					if (invertedWord.equals(word)) {
						if (!found.contains(word)) {
							found.add(word);

							int position = entry.indexOf(file);
							if (position == -1) { // Das Wort existiert, aber nicht fuer dieses Dokument

							} else {
								position = entry.indexOf("@", position + 1);
								int positionEnd = entry.indexOf("$", position);
								if (positionEnd == -1) {
									positionEnd = entry.length();
								}

								String number = entry.substring(position + 1, positionEnd);
								tfdi = Integer.valueOf(number);

								int indexPath = 0;

								do {
									indexPath = entry.indexOf("$", indexPath);
									indexPath++;
									ni++;
								} while (indexPath != 0);
							}
						}
					}

				}

				if (ni != 0) {
					double tempNumber = Math.log(bigN / ((double) ni));
					double sumNumber = Math.pow((tfdi * tempNumber), 2);

					outNumber += sumNumber;
				}
				i++;

				j = out.indexOf(" ", i);

				// Abfangen, wenn es keinen Inhalt mehr mit " " gibt
			} while (j != -1);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Math.pow(outNumber, 0.5);
	}

	/**
	 * Errechnung des Zaehlers der Salton-Formel
	 * 
	 * @param file
	 * @param pathString Pfadstring
	 * @param word
	 * @return double
	 */
	double wordFrequency(String file, String pathString, String word) {
		double bigN = 0;
		int bigNi = 0;

		final File folder = new File(pathString);
		for (final File fileEntry : folder.listFiles()) {
			bigNi++;
		}
		bigN = bigNi;

		double tfdk = 0;
		int nk = 0;

		for (int wordIndex = 0; wordIndex < searchList.size(); wordIndex++) {
			String entry = searchList.get(wordIndex);
			String entryWord = entry.substring(0, entry.indexOf("$"));

			if (word.equals(entryWord)) {
				int position = entry.indexOf(file);
				if (position == -1) { // Das Wort existiert, aber nicht fuer dieses Dokument

				} else {
					position = entry.indexOf("@", position + 1);
					int positionEnd = entry.indexOf("$", position);
					if (positionEnd == -1) {
						positionEnd = entry.length();
					}

					String number = entry.substring(position + 1, positionEnd);
					tfdk = Integer.valueOf(number);

					int indexPath = 0;

					do {
						indexPath = entry.indexOf("$", indexPath);
						indexPath++;
						nk++;
					} while (indexPath != 0);

					double tempNumber = Math.log(bigN / ((double) nk));
					double out = tfdk * tempNumber;
					return out;

				}
			}
		}
		return -1.0;
	}

	/**
	 * Errechnung des Gewichts mithilfe der Salton-Formel
	 * 
	 * @param file s.o
	 * @param path Pfad, in dem die Dokumente sich befinden
	 * @param word
	 * @return double
	 */
	double getWeight(String file, String path, String word) {
		double freq = wordFrequency(file, path, word);
		if (freq >= 0) {
			double norm = sumWordFrequency(file, path);
			return freq / norm;
		} else {
			return -1.0;
		}
	}

	/**
	 * Sortierung der Gewichte in aufsteigender Reihenfolge
	 * 
	 * @param path
	 * @param word
	 * @param vektorList
	 * @return
	 */
	LinkedList<String> sortDocuments(String path, String word, LinkedList<String> vektorList) {
		LinkedList<String> out = new LinkedList<String>();
		searchList = vektorList;
		System.out.print("Ergebnis wird berechnet");
		final File folder = new File(path);
		for (final File file : folder.listFiles()) {
			String filename = file.getName();
			double weight = getWeight(filename, path, word);

			if (weight >= 0) {
				out.add(weight + "$" + filename);
			}

			System.out.print(".");
		}
		System.out.println();

		Collections.sort(out, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Collator.getInstance().compare(o2, o1);
			}
		});

		for (String o : out) {
			System.out.println(o);
		}

		return out;
	}

	/**
	 * Basisalgorithmus fuer invertierte Listen nach Henrich
	 * 
	 * @param query      Anfragevektor
	 * @param slist      Suchliste
	 * @param pathString Pfade der Dokumente
	 */
	void baseAlgorithm(String query, LinkedList<String> slist, String pathString) {
		searchList = slist;

		int bigN = 0;
		final File folder = new File(pathString);
		for (final File fileEntry : folder.listFiles()) {
			bigN++;
		}

		double outNumber = 0;
		int j;
		String queryWord;

		int maxNumber = 1;
		LinkedList<String> found = new LinkedList<String>();
		int i = -1;

		do { // Alle Woerter der Anfrage

			i++;
			j = query.indexOf(",", i);
			if (j == -1) {
				j = query.length();
			}
			queryWord = query.substring(i, j);

			boolean didFound = false;
			for (int k = 0; k < found.size(); k++) {
				String f = found.get(k);

				String foundWord = f.substring(f.indexOf("$") + 1, f.length());
				if (foundWord.equals(queryWord)) {
					didFound = true;
					String numberString = f.substring(0, f.indexOf("$"));
					int number = Integer.valueOf(numberString);
					number++;

					if (number > maxNumber) {
						maxNumber = number;
					}

					numberString = String.valueOf(number);
					numberString = ("000" + numberString).substring(numberString.length());

					found.set(k, numberString + "$" + queryWord);
				}
			}
			if (!didFound) {
				found.add("001" + "$" + queryWord);
			}

			i = j;

			// Abfangen, wenn es keinen Inhalt mehr mit " " gibt
		} while (j != query.length());

		Collections.sort(found, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Collator.getInstance().compare(o2, o1);
			}
		});

		LinkedList<String> documents = new LinkedList<String>();
		LinkedList<String> temp = new LinkedList<String>();

		for (String f : found) {
			System.out.println(f);

			f = f.substring(f.indexOf("$") + 1, f.length());
			double wqk = saltonBuckley(found, maxNumber, query, f, bigN);
			temp = sortDocuments(pathString, f, slist);

			for (String t : temp) {
				String wdkString = t.substring(0, t.indexOf("$"));
				String name = t.substring(t.indexOf("$"), t.length());
				double wdk = Double.valueOf(wdkString);
				double weight = wdk * wqk;
				boolean exist = false;
				for (int z = 0; z < documents.size(); z++) {
					String d = documents.get(z);

					String wName = d.substring(d.indexOf("$"), d.length());
					if (wName.equals(name)) {
						exist = true;
						String wString = d.substring(0, d.indexOf("$"));
						double w = Double.valueOf(wString);
						double sumWeight = w + weight;
						documents.set(z, sumWeight + name);
					}
				}
				if (!exist) {
					documents.add(weight + name);
				}
			}

		}

		Collections.sort(documents, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Collator.getInstance().compare(o2, o1);
			}
		});

		System.out.println("=========================================================");
		System.out.println("Das Ergebnis ist:");
		for (String d : documents) {
			System.out.println(d);
		}

	}

	/**
	 * Errechnen des Anfragevektors mithilfe der Salton-Buckley Formel
	 * 
	 * @param found     bool Wort gefunde
	 * @param maxNumber groesste Vorkommenszahl eines Wortes
	 * @param query     Anfrage
	 * @param word
	 * @param bigN      N sei die Anzahl der Dokumente in unserer
	 *                  Dokumentenkollektion
	 * @return double Anfragegewicht
	 */
	double saltonBuckley(LinkedList<String> found, int maxNumber, String query, String word, int bigN) {

		String invertedWord = "";

		double tfqk = 0;

		for (String f : found) {
			String foundWord = f.substring(0, f.indexOf("$"));
			if (foundWord.equals(word)) {
				String numberString = f.substring(f.indexOf("$") + 1, f.length());
				tfqk = Integer.valueOf(numberString);
				break;
			}
		}

		int nk = 0;

		for (int wordIndex = 0; wordIndex < searchList.size(); wordIndex++) {
			String entry = searchList.get(wordIndex);
			invertedWord = entry.substring(0, entry.indexOf("$"));
			if (invertedWord.equals(word)) {

				int indexPath = 0;

				do {
					indexPath = entry.indexOf("$", indexPath);
					indexPath++;
					nk++;
				} while (indexPath != 0);
			}
		}

		if (nk != 0) {
			double weight = (0.5 + (0.5 * tfqk) / maxNumber) * Math.log(bigN / nk);
			System.out.println(weight);
			return weight;
		}

		return 0.0;

	}

}
