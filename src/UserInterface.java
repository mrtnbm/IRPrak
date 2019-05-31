
import java.io.File;
import java.io.IOException;
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

		System.out.println("In welchem Pfad sollen die Fabeln gespeichert werden? example/");
		Scanner input = new Scanner(System.in);
		String inputUser = input.next();
		System.out.println("Zweiter Pfad.");
		String inputUser2 = input.next();
		/*
		 * File currentDirFile = new File("."); String helper =
		 * currentDirFile.getAbsolutePath(); String currentDir = null; try { currentDir
		 * = helper.substring(0, helper.length() -
		 * currentDirFile.getCanonicalPath().length()); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		String currentDir = System.getProperty("user.dir");
		String dirName = inputUser;
		File dir = new File(currentDir + "\\" + dirName);

		if (dir.mkdir()) {
			System.out.println("Ordner erstellt");
		} else {
			System.out.println(dir + " konnte nicht erstellt werden");
		}

		Document obj = new Document();
		obj.importText("aesopa10.txt");
		obj.splitText(obj.str, "Fables", dir.getParent());

		String dirName2 = inputUser2;
		File dir2 = new File(currentDir + "\\" + dirName2);

		if (dir2.mkdir()) {
			System.out.println("Ordner erstellt");
		} else {
			System.out.println(dir2 + " konnte nicht erstellt werden");
		}

		Cleaning objc = new Cleaning();
		objc.readStopWords("englishST.txt");
		objc.elimStopWords(obj.str);
		objc.openSave(dir.getParent(), dir2.getParent());

		List list = new List();
		Search search = new Search();

		String searchString = "";
		do {

			Scanner scan = new Scanner(System.in);
			System.out.println("Für beenden schreiben Sie \"exit\"");
			System.out.println("Für Suche mit Stoppwörtern drücken Sie die 1, sonst 0.");
			String input1 = scan.next();

			if (input1.equals("0") || input1.equals("1") || input1.equals("exit")) {

				if (input1.equals("0")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					list.makeLinearList(dirName2 + "/temp");
					search.search(list.linkedList, searchString);
				}

				if (input1.equals("1")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					list.makeLinearList(dirName);
					search.search(list.linkedList, searchString);
				}

				if (input1.equals("exit")) {
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
