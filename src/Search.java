
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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

			if (tempString.equals(searchString)) {
				pathToFile = string.substring(i + 1, string.length());
				System.out.println(pathToFile);
			}

		}
	}
	
	void doSearchBool(LinkedList<String> list, String searchString) {
		searchList = list;
		LinkedList<String> input = new LinkedList<String>();
		input.add(searchString);
		input.add("0");
		LinkedList<String> output = boolCombin(input);
		for(String result:output) {
			System.out.println(result);
		}
	}

	LinkedList<String> searchBool(String searchString) {
		LinkedList<String> linkList = searchList;
		int i = 0;
		String tempString;
		String pathToFile;
		LinkedList<String> results = new LinkedList<String>();

		for (String string : linkList) {

			i = string.indexOf("$");
			tempString = string.substring(0, i);

			if (tempString.equals(searchString)) {
				pathToFile = string.substring(i + 1, string.length());
				results.add(pathToFile);
			}
		}
		return results;
	}

	LinkedList<String> boolCombin(LinkedList<String> all) {
		String input = all.get(0);
System.out.println(all);
System.out.println(input);
		int orIndex = input.indexOf("|");
		int andIndex = input.indexOf("&");
		int notIndex = input.indexOf("!");

		int orAnd = Math.max(orIndex, andIndex);
		int logic = Math.max(orAnd, notIndex);

		if (logic == -1) {
			return all;
		}
		if(orIndex == -1) {
			if(andIndex == -1) {
				logic = notIndex;
			} else {
				if(notIndex == -1) {
					logic = andIndex;
				} else {
				logic = Math.min(andIndex, notIndex);
				}
			}
		} else if(andIndex == -1) {
			if(notIndex == -1) {
				logic = orIndex;
			} else {
			logic = notIndex;
			}
		} else if(notIndex == -1) {
			logic = Math.min(orIndex, andIndex);
		} else {
			orAnd = Math.min(orIndex, andIndex);
			logic = Math.min(orAnd, notIndex);	
		}
		String searchChar = input.substring(logic, logic + 1);

		String positionString = all.get(1);
		int lastPosition = Integer.parseInt(positionString);

		String word = input.substring(lastPosition, logic);
		LinkedList<String> results = searchBool(word);
		if (lastPosition == 0) {
			all.addAll(results);
			all.set(1,String.valueOf(logic));
			return boolCombin(all);
		} else {
			LinkedList<String> resultBool = new LinkedList<String>();
			if (searchChar.equals("|")) {
		
				Set<String> set = new HashSet<String>();
				set.addAll(all);
				set.addAll(results);
				
				resultBool = new LinkedList<String>(set);
			
			} else if (searchChar.equals("&")) {
				for(String firstWord:all) {
					if(results.contains(firstWord)) {
						resultBool.add(firstWord);
					}
				}
			
			} else if (searchChar.equals("!")) {
				for (String oldWord : all) {
					if (!results.contains(oldWord)) { // results shall not be in resultBool
						resultBool.add(oldWord);
					}
				}
			}

			resultBool.add(0, String.valueOf(logic)); // remember last boolean
			resultBool.add(0, input);
System.out.println(resultBool);
			return boolCombin(resultBool);
		}
	}

}
