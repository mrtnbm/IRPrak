import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Boolsche Suche und Berechnung von Precision und Recall
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class Evaluate {

	/**
	 * Methode, um die Boolsche Suche zu ermoeglichen.
	 * 
	 * @param path
	 * @param list
	 */
	void evaluateBool(String path, LinkedList<String> list) {
		LinkedList<LinkedList<Integer>> resultsList = new LinkedList<LinkedList<Integer>>();
		LinkedList<String> wordList = new LinkedList<String>();

		String temp;

		try {

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			do {
				temp = br.readLine();

				int split = temp.indexOf(" - ");
				if (split == -1) {
					break;
				}

				String word = temp.substring(0, split);
				split += 2;
				LinkedList<Integer> numbers = new LinkedList<Integer>();
				do {
					int i = temp.indexOf(",", split + 1);
					if (i == -1) {
						i = temp.length();
					}
					String number = temp.substring(split + 1, i);
					int whiteSpace = number.lastIndexOf(" ");
					String numberWithoutWhite = number.substring(whiteSpace + 1, number.length());
					numbers.add(Integer.valueOf(numberWithoutWhite));
					split = i;
				} while (split != temp.length());
				resultsList.add(numbers);
				wordList.add(word);
			} while (temp != null);

			br.close();

			{

				LinkedList<Integer> list1 = resultsList.get(0);
				LinkedList<Integer> list2 = resultsList.get(1);

				String wordU = wordList.get(0) + "|" + wordList.get(1);
				String wordI = wordList.get(0) + "&" + wordList.get(1);

				LinkedList<Integer> resultUnion = new LinkedList<Integer>();
				// Union von 2 Listen

				for (Integer r : list1) {
					int i = list2.indexOf(r);
					if (i == -1) {// Pfad ist nicht im Ergebnis enthalten
						resultUnion.add(r); // Pfad wird hinzugefuegt
					} else { // Pfad ist in im Ergebnis enthalten
						resultUnion.add(r); // Pfad wird hinzugefuegt
						list2.remove(i); // Pfad wird aus Ergebnis entfernt.
					}
				}
				resultUnion.addAll(list2); // Pfade aus Ergebnis, die nicht bereits vorhanden sind, hinzufuegen

				// Schnitt von 2 Listen
				LinkedList<Integer> intersection = new LinkedList<Integer>();
				for (Integer r : list1) {
					if (list2.contains(r)) {
						intersection.add(r);
					}
				}

				Search s = new Search();
				LinkedList<Integer> resultsU = s.doSearchBool(list, wordU);
				LinkedList<Integer> resultsI = s.doSearchBool(list, wordI);
				LinkedList<Integer> relevantResultsU = new LinkedList<Integer>();
				LinkedList<Integer> relevantResultsI = new LinkedList<Integer>();

				for (Integer i : intersection) {
					if (resultsU.contains(i)) {
						relevantResultsI.add(i);
					}
				}

				for (Integer i : resultUnion) {
					if (resultsI.contains(i)) {
						relevantResultsU.add(i);
					}
				}

				float recallU = (float) relevantResultsU.size() / (float) resultUnion.size();
				float precisionU = (float) relevantResultsU.size() / (float) resultsU.size();

				float recallI = (float) relevantResultsI.size() / (float) intersection.size();
				float precisionI = (float) relevantResultsI.size() / (float) resultsI.size();

				System.out.println(wordU + "\nRecall Union: " + recallU + "\nPrecision Union:" + precisionU);
				System.out.println(
						wordI + "\nRecall Intersection: " + recallI + "\nPrecision Intersection:" + precisionI);
				// results a+b // numbers a+c // relevantResults a
			}

			System.out.println("Alle Faelle berechnet.");
		} catch (Exception e) {
		}
	}

	/**
	 * Berechnet precision und recall für die Eingaben aus der grountruth.txt
	 * 
	 * @param path Pfad der groundtruth.txt
	 * @param list
	 */
	void evaluate(String path, LinkedList<String> list) {
		String temp;

		try {

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			do {
				temp = br.readLine();

				int split = temp.indexOf(" - ");
				if (split == -1) {
					System.out.println("Alle Faelle berechnet.");
					break;
				}

				String word = temp.substring(0, split);
				split += 2;
				LinkedList<Integer> numbers = new LinkedList<Integer>();
				do {
					int i = temp.indexOf(",", split + 1);
					if (i == -1) {
						i = temp.length();
					}
					String number = temp.substring(split + 1, i);
					int whiteSpace = number.lastIndexOf(" ");
					String numberWithoutWhite = number.substring(whiteSpace + 1, number.length());
					numbers.add(Integer.valueOf(numberWithoutWhite));
					split = i;
				} while (split != temp.length());

				Search s = new Search();
				LinkedList<Integer> results = s.doSearchBool(list, word);
				LinkedList<Integer> relevantResults = new LinkedList<Integer>();

				for (Integer i : numbers) {
					if (results.contains(i)) {
						relevantResults.add(i);
					}
				}

				// System.out.println(relevantResults);
				// System.out.println(numbers);System.out.println(results);

				float recall = (float) relevantResults.size() / (float) numbers.size();
				float precision = (float) relevantResults.size() / (float) results.size();

				System.out.println(word + "\nRecall: " + recall + "\nPrecision:" + precision);
				// results a+b // numbers a+c // relevantResults a

			} while (temp != null);

			br.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}