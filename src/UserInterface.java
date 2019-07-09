
import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Main-Methode fuer den Benutzer, um wahlweise Stoppworteliminierung, Suche
 * mithilfe (invertierter) Liste, Stammformreduktion durchfuehren zu koennen.
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class UserInterface {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);

		Save save = new Save();
		Scanner scan = new Scanner(System.in);

		/*
		 * System.out.println("Moechten Sie Dateien laden? j/n");
		 * if(input.next().equals("j")) {
		 * System.out.println("Geben Sie den Pfad zur Sicherungsdatei an!"); //
		 * input.next(); //TODO };
		 */

		System.out.println("In welchem Pfad sollen die Fabeln gespeichert werden?");
		String inputUser = input.next();
		System.out.println("Zweiter Pfad für die Fabeln ohne Stoppwörter:");
		String inputUser2 = input.next();
		System.out.println("Dritter Pfad für die Fabeln mit Stammformreduktion:");
		String inputUser3 = input.next();
		/*
		 * File currentDirFile = new File("."); String helper =
		 * currentDirFile.getAbsolutePath(); String currentDir = null; try { currentDir
		 * = helper.substring(0, helper.length() -
		 * currentDirFile.getCanonicalPath().length()); } catch (IOException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); }
		 */
		String dirName = inputUser + "/";
		File origin = new File(dirName);

		if (origin.mkdir()) {
			System.out.println("Ordner" + origin + " erstellt");
		} else {
			System.out.println(origin + " konnte nicht erstellt werden");
		}

		Document obj = new Document();
		obj.importText("aesopa10.txt");
		obj.splitText(obj.str, "Fables", dirName);

		String dirName2 = inputUser2 + "/";
		File saveDirMain = new File(dirName2);
		if (saveDirMain.mkdir()) {
			System.out.println("Ordner" + saveDirMain + " erstellt");
		} else {
			System.out.println(saveDirMain + " konnte nicht erstellt werden");
		}

		File saveDir = new File(dirName2 + dirName);

		if (saveDir.mkdir()) {
			System.out.println("Ordner" + saveDir + " erstellt");
		} else {
			System.out.println(saveDir + " konnte nicht erstellt werden");
		}

		String dirName3 = inputUser3 + "/";
		File reductionDirMain = new File(dirName3);

		if (reductionDirMain.mkdir()) {
			System.out.println("Ordner" + reductionDirMain + " erstellt");
		} else {
			System.out.println(reductionDirMain + " konnte nicht erstellt werden");
		}

		File reductionDir = new File(dirName3 + dirName);

		if (reductionDir.mkdir()) {
			System.out.println("Ordner" + reductionDir + " erstellt");
		} else {
			System.out.println(reductionDir + " konnte nicht erstellt werden");
		}

		Cleaning objc = new Cleaning();
		objc.readStopWords("englishST.txt");
		objc.elimStopWords(obj.str);
		objc.openSave(dirName, dirName2);

		BasicFormReduction objb = new BasicFormReduction();
		objb.openSave(dirName, dirName3);

		List list = new List();
		Search search = new Search();

		LinkedList<String> linear0 = list.makeLinearList(dirName);
		LinkedList<String> linear1 = list.makeLinearList(dirName2 + dirName);
		LinkedList<String> linear2 = list.makeLinearList(dirName3 + dirName);

		LinkedList<String> signat = list.makeSignaturList(dirName);
		LinkedList<String> vektorList = new LinkedList<String>();

		System.out.println("\nInvertierte Listen erstellen? Dies kann einige Minuten dauern. j/n");
		boolean useInverted = false;
		LinkedList<String> inverted = null;
		if (scan.next().equals("j")) {
			inverted = list.makeInvertedList(dirName3 + dirName);
			useInverted = true;
		}

		System.out.println("\nVektorraummodell erstellen? Dies kann einige Minuten dauern. j/n");
		boolean useVektor = false;
		LinkedList<String> vektor = null;
		if (scan.next().equals("j")) {
			vektorList = list.makeInvertedSumList(dirName2 + dirName);
			useVektor = true;
		}

		String searchString = "";
		do {

			System.out.println(
					"\nBoolsche Suche nun moeglich. | fuer logisches Oder, & fuer logisches Und, ! fuer Negation.");
			System.out.println("Fuer beenden schreiben Sie \"exit\"");
			System.out.println("Fuer Suche auf originalen Dateien (inclusive Grossschreibung) druecken Sie die 0.");
			System.out.println("Fuer Suche ohne Stoppwoerter drueken Sie die 1.");
			System.out.println("Fuer Suche mit Stammformreduktion druecken Sie die 2.");
			System.out.println("Fuer Evaluation druecken Sie die 3.");
			System.out.println("Fuer Vektorraumsuche druecken Sie die 4");
			String input1 = scan.next();

			if (input1.equals("0") || input1.equals("1") || input1.equals("2") || input1.equals("3")
					|| input1.equals("4") || input1.equals("exit")) {

				if (input1.equals("0")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					LinkedList<String> resultsBinaer = search.doSearchOr(signat, searchString); // KandidatenListe
					LinkedList<String> binaer = list.makeLinearList(dirName, resultsBinaer); // Lineare Liste zum Suchen
					search.doSearchBool(binaer, searchString); // Suche mit Kandidaten
				}

				if (input1.equals("1")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					search.doSearchBool(linear1, searchString);
				}

				if (input1.equals("2")) {
					System.out.println("Geben Sie das zu suchende Wort ein!");
					searchString = scan.next();
					if (useInverted) {
						search.doSearchBool(inverted, searchString);
					}
					search.doSearchBool(linear2, searchString);
				}

				if (input1.equals("3")) {
					Evaluate e = new Evaluate();
					e.evaluate("ground_truth_correct.txt", linear2);
				}

				if (input1.equals("4")) {
					if (useVektor) {
						System.out.println("Bitte geben Sie den zu suchenden Text mit \",\" statt \" \" an!");
						String text = scan.next();
						search.baseAlgorithm(text, vektorList, dirName2 + dirName);
					} else {
						System.out.println("VektorListe wurde nicht erstellt!");
					}
				}

				if (input1.equals("exit")) {
					System.out.println("Temporaere Dateien loeschen? j/n");
					if (scan.next().equals("j")) {
						save.deleteDirectory(dirName);
						save.deleteDirectory(dirName2 + dirName);
						save.deleteDirectory(dirName3 + dirName);
						save.deleteDirectory(dirName2);
						save.deleteDirectory(dirName3);
						System.out.println("Dateien geloescht.");
					}
					System.out.println("Programm wurde beendet.");
					break;
				}
			} else {
				System.out.println("Fehler, falsche Eingabe");
			}

		} while (true);
	}

}
