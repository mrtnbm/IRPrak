
import java.util.LinkedList;

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
		
		LinkedList<String> resultBool = new LinkedList<String>(); //Ausgabeliste
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
			if (compareAnd(tempString,word)) {
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
		for(int i = 0; i<word.length(); i++) {
			char a = tempString.charAt(i);
			if(a == word.charAt(i)) {
				if(a == '1') { //beide Werte muessen 1 sein, damit logisches UND erfuellt
					return true;					
				}
			}
		}
		return false;
	}

	
	
	
}
