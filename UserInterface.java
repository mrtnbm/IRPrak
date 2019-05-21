package src;

import java.util.Scanner;

/**
 * Main-Methode für den Benutzer, um die Stoppworteliminierung durchführen zu
 * können.
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class UserInterface {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Document obj = new Document();
		obj.importText("aesopa10.txt");
		obj.splitText(obj.str, "Fables", "temp/");

		Cleaning objc = new Cleaning();
		objc.readStopWords("englishST.txt");
		objc.elimStopWords(obj.str);
		objc.openSave("temp/", "temp2/");

		List list = new List();
		Search search = new Search();

		String searchString = "";
		do {

			Scanner scan = new Scanner(System.in);
			System.out.println("Für beenden schreiben Sie \"exit\"");
			System.out.println("Für Suche mit Stoppwörtern drücken Sie die 1, sonst 0.");
			String input = scan.next();

			if (input.equals("0") || input.equals("1") || input.equals("exit")) {

				if (input.equals("0")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					list.makeLinearList("temp2/temp/");
					search.search(list.linkedList, searchString);
				}

				if (input.equals("1")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					list.makeLinearList("temp/");
					search.search(list.linkedList, searchString);
				}

				if (input.equals("exit")) {
					System.out.println("Programm wurde beendet.");
					break;
				}
			} else {
				System.out.println("Fehler, falsche Eingabe");
				break;
			}

		} while (true);
	}

}
