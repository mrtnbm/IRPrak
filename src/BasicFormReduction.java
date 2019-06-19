import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.Scanner;

/**
 * Methode, um Beugungen und Endungen von Woertern zu entfernen und somit die
 * Stammform zu erhalten. Alle wichtigen Informationen sind im Porteralgorithmus
 * enthalten, der hier umgesetzt wurde:
 * 
 * S - the stem ends with S (and similarly for the other letters).
 * 
 * v* - the stem contains a vowel.
 * 
 * d - the stem ends with a double consonant (e.g. -TT, -SS).
 * 
 * o - the stem ends cvc, where the second c is not W, X or Y (e.g. -WIL, -HOP).
 * 
 * Step 1a
 * 
 * SSES -> SS caresses -> caress IES -> I ponies -> poni ties -> ti SS -> SS
 * caress -> caress S -> cats -> cat
 * 
 * Step 1b
 * 
 * (m>0) EED -> EE feed -> feed agreed -> agree (*v*) ED -> plastered -> plaster
 * bled -> bled (*v*) ING -> motoring -> motor sing -> sing
 * 
 * If the second or third of the rules in Step 1b is successful, the following
 * is done:
 * 
 * AT -> ATE conflat(ed) -> conflate BL -> BLE troubl(ed) -> trouble IZ -> IZE
 * siz(ed) -> size (*d and not (*L or *S or *Z)) -> single letter hopp(ing) ->
 * hop tann(ed) -> tan fall(ing) -> fall hiss(ing) -> hiss fizz(ed) -> fizz (m=1
 * and *o) -> E fail(ing) -> fail fil(ing) -> file
 * 
 * The rule to map to a single letter causes the removal of one of the double
 * letter pair. The -E is put back on -AT, -BL and -IZ, so that the suffixes
 * -ATE, -BLE and -IZE can be recognised later. This E may be removed in step 4.
 * 
 * Step 1c
 * 
 * (*v*) Y -> I happy -> happi sky -> sky
 * 
 * Step 1 deals with plurals and past participles. The subsequent steps are much
 * more straightforward.
 * 
 * Step 2
 * 
 * (m>0) ATIONAL -> ATE relational -> relate (m>0) TIONAL -> TION conditional ->
 * condition rational -> rational (m>0) ENCI -> ENCE valenci -> valence (m>0)
 * ANCI -> ANCE hesitanci -> hesitance (m>0) IZER -> IZE digitizer -> digitize
 * (m>0) ABLI -> ABLE conformabli -> conformable (m>0) ALLI -> AL radicalli ->
 * radical (m>0) ENTLI -> ENT differentli -> different (m>0) ELI -> E vileli - >
 * vile (m>0) OUSLI -> OUS analogousli -> analogous (m>0) IZATION -> IZE
 * vietnamization -> vietnamize (m>0) ATION -> ATE predication -> predicate
 * (m>0) ATOR -> ATE operator -> operate (m>0) ALISM -> AL feudalism -> feudal
 * (m>0) IVENESS -> IVE decisiveness -> decisive (m>0) FULNESS -> FUL
 * hopefulness -> hopeful (m>0) OUSNESS -> OUS callousness -> callous (m>0)
 * ALITI -> AL formaliti -> formal (m>0) IVITI -> IVE sensitiviti -> sensitive
 * (m>0) BILITI -> BLE sensibiliti -> sensible
 * 
 * The test for the string S1 can be made fast by doing a program switch on the
 * penultimate letter of the word being tested. This gives a fairly even
 * breakdown of the possible values of the string S1. It will be seen in fact
 * that the S1-strings in step 2 are presented here in the alphabetical order of
 * their penultimate letter. Similar techniques may be applied in the other
 * steps.
 * 
 * Step 3
 * 
 * (m>0) ICATE -> IC triplicate -> triplic (m>0) ATIVE -> formative -> form
 * (m>0) ALIZE -> AL formalize -> formal (m>0) ICITI -> IC electriciti ->
 * electric (m>0) ICAL -> IC electrical -> electric (m>0) FUL -> hopeful -> hope
 * (m>0) NESS -> goodness -> good
 * 
 * Step 4
 * 
 * (m>1) AL -> revival -> reviv (m>1) ANCE -> allowance -> allow (m>1) ENCE ->
 * inference -> infer (m>1) ER -> airliner -> airlin (m>1) IC -> gyroscopic ->
 * gyroscop (m>1) ABLE -> adjustable -> adjust (m>1) IBLE -> defensible ->
 * defens (m>1) ANT -> irritant -> irrit (m>1) EMENT -> replacement -> replac
 * (m>1) MENT -> adjustment -> adjust (m>1) ENT -> dependent -> depend (m>1 and
 * (*S or *T)) ION -> adoption -> adopt (m>1) OU -> homologou -> homolog (m>1)
 * ISM -> communism -> commun (m>1) ATE -> activate -> activ (m>1) ITI ->
 * angulariti -> angular (m>1) OUS -> homologous -> homolog (m>1) IVE ->
 * effective -> effect (m>1) IZE -> bowdlerize -> bowdler
 * 
 * The suffixes are now removed. All that remains is a little tidying up.
 * 
 * Step 5a
 * 
 * (m>1) E -> probate -> probat rate -> rate (m=1 and not *o) E -> cease -> ceas
 * 
 * Step 5b
 * 
 * (m > 1 and *d and *L) -> single letter controll -> control roll -> roll
 * 
 * @author Johannes Wawra, Martin Behm
 *
 */
public class BasicFormReduction {

	/**
	 * Konvertiert ein Wort in eine Abfolge von CV...CV, also in eine Darstellung
	 * von Konsonanten und Vokalen.
	 * 
	 * @param input Eingabechar-Array
	 * @return Vereint alle Konsonanten-Vokal-Tupel zu einem einzigen "VC"
	 */
	String convertCV(String input) {
		char[] inputC = input.toCharArray();
		char[] outputC = new char[input.length() + 1];
		int i = 0;
		char remC = ' ';
		for (char c : inputC) {

			if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
				if (remC != 'V') {
					i += 1;
				}
				outputC[i] = 'V';
				remC = 'V';
			} else {
				if (remC != 'C') {
					i += 1;
				}
				outputC[i] = 'C';
				remC = 'C';
			}
		}
		String out = new String();
		return out.valueOf(outputC);
	}

	/**
	 * Zaehlt die Anzahl der VC-Paare und gibt [C]VC{AnzahlVC}[V] dann in einem
	 * String aus.
	 * 
	 * @param vc Die Repraesentation der Woerter in C und V aus convertCV
	 * @return [optionales C]VC{Anzahl VC}[optionales V]
	 */
	String convertM(String vc) {
		char[] inputC = vc.toCharArray();
		int i = 0;
		int vcCount = 0;
		boolean cStart = false;
		boolean vEnd = false;
		for (char c : inputC) {
			if (i == 0 && c == 'C') {
				cStart = true;
			}
			if (c == 'V') {
				vEnd = true;
			}
			if (c == 'C' && vEnd) {
				vcCount += 1;
				vEnd = false;
			}
		}
		String out;
		;
		if (cStart) {
			out = "C";
		} else {
			out = "X";
		}
		out += String.valueOf(vcCount);
		if (vEnd) {
			out += "V";
		} else {
			out += "Y";
		}
		return out;
	}

	boolean[] check(String word, String mWord, String end) {
		boolean endWord = false;
		boolean vowel = false;
		boolean hop = false;

		int endI = word.lastIndexOf(end);
		if (endI != -1) {
			if (endI == word.length() - end.length()) {
				endWord = true;
			}
		}

		String temp = mWord.substring(1, mWord.length() - 1);
		int m = Integer.parseInt(temp);
		if (m > 0 || mWord.charAt(mWord.length() - 1) == 'V') {
			vowel = true;
		}
		if ((m == 1 && mWord.charAt(0) == 'C' && mWord.charAt(mWord.length() - 1) == 'Y')
				|| (m > 1 && mWord.charAt(mWord.length() - 1) == 'Y')) {
			char lc = word.charAt(word.length() - 1);
			if (lc != 'w' && lc != 'x' && lc != 'y') {
				hop = true;
			}
		}
		boolean[] outbool = new boolean[3];
		outbool[0] = endWord;
		outbool[1] = vowel;
		outbool[2] = hop;
		return outbool;
	}

	/**
	 * Ersetzt doppelte Konsonanten durch einen Einzigen
	 * 
	 * @param word       Wortstring, der reduziert werden soll
	 * @param switch1b5b Unterscheidung zwischen Step 1b oder 5b (sind ähnlich)
	 * @return String mit reduziertem Wort, wenn kein Fehler aufgetreten ist, sonst
	 *         null.
	 */
	String replaceDD(String word, boolean switch1b5b) {
		char l = word.charAt(word.length() - 1);
		if (word.length() < 2) {
			return null;
		}
		char ll = word.charAt(word.length() - 2);
		if (l == ll) {
			if (l == 'a' || l == 'e' || l == 'i' || l == 'o' || l == 'u') {
				return null;
			}
			if (switch1b5b && (l == 'l' || l == 's' || l == 'z')) {
				return null;
			}
			if (!switch1b5b && l != 'l') {
				return null;
			}
			return word.substring(0, word.length() - 1);
		}
		return null;
	}

	/**
	 * Hilfsfunktion, mit der dann Teilwoerter eines Wortes durch einen anderen
	 * String ersetzt werden.
	 * 
	 * @param word   Wort, das reduziert werden soll
	 * @param mWord  CV-Darstellung des Wortes
	 * @param end    Endung des Wortes, die ersetzt werden soll
	 * @param newEnd Neue Endung, die für end eingesetzt werden soll
	 * @return String Wort mit der neuen Endung
	 */
	String replace(String word, String mWord, String end, String newEnd) {

		boolean[] c = this.check(word, mWord, end);
		String out = null;
		if (c[0]) {
			StringBuilder b = new StringBuilder(word);
			b = b.replace(word.lastIndexOf(end), word.length(), newEnd);
			out = b.toString();
		}
		return out;
	}

	/**
	 * Entfernt den letzten Vokal nach Regeln 1a
	 * 
	 * @param word   Wort, das reduziert werden soll
	 * @param mWord  Wort mit CV^{m}-Darstellung
	 * @param end    Endung des Wortes, die ersetzt werden soll
	 * @param newEnd Neue Endung, die für end eingesetzt werden soll
	 * @return String Wort mit der neuen Endung
	 */
	String replaceV(String word, String mWord, String end, String newEnd) {
		String out = this.replace(word, mWord, end, newEnd);
		if (out != null) {
			boolean[] test = this.check(word, mWord, "");
			if (test[1]) {
				return out;
			}
		}
		return null;
	}

	String replaceM(String word, String mWord, String end, String newEnd, int min) {
		String out = this.replace(word, mWord, end, newEnd);
		if (out != null) {
			String vcWord = this.convertCV(out);
			String mWord2 = this.convertM(vcWord);

			String temp = mWord2.substring(1, mWord2.length() - 1);
			int m = Integer.parseInt(temp);

			if (m > min) {
				return out;
			}
		}
		return null;
	}

	/**
	 * Wendet regeln 1b aus Porteralgorithmus an
	 * 
	 * @param word
	 * @param mWord Wort mit CV^{m}-Darstellung
	 * @return
	 */
	String replace1b(String word, String mWord) {
		boolean[] test = check(word, mWord, "");
		if (test[2]) {
			String temp = mWord.substring(1, mWord.length() - 1);
			int m = Integer.parseInt(temp);
			if (m == 1) {
				return replace(word, mWord, "e", "");
			}
		}
		return null;
	}

	/**
	 * Wendet Regeln 1a des Porteralgorithmus an
	 * 
	 * @param word  Wort, das reduziert werden soll
	 * @param mWord Wort mit CV^{m} Darstellung
	 * @return
	 */
	String step1a(String word, String mWord) {
		String out = replace(word, mWord, "sses", "ss");
		if (out == null) {
			out = replace(word, mWord, "ies", "i");
		}
		if (out == null) {
			out = replace(word, mWord, "ss", "ss");
		}
		if (out == null) {
			out = replace(word, mWord, "s", "");
		}
		if (out == null) {
			out = word;
		}
		return out;
	}

	/**
	 * Wendet Regeln 1b des Porter-Algorithmus an.
	 * 
	 * @param word
	 * @param mWord
	 * @return
	 */
	String step1b(String word, String mWord) {
		String out = replaceM(word, mWord, "eed", "ee", 0);
		if (out != null) {
			return out;
		}
		if (out == null) {
			out = replaceV(word, mWord, "ed", "");
		}
		if (out == null) {
			out = replaceV(word, mWord, "ing", "");
		}
		if (out != null) {
			word = out;
			String cvWord = convertCV(word);
			mWord = convertM(cvWord);

			out = replaceV(word, mWord, "at", "ate");
			if (out == null) {
				out = replaceV(word, mWord, "bl", "ble");
			}
			if (out == null) {
				out = replaceV(word, mWord, "iz", "ize");
			}
			if (out == null) {
				out = replaceDD(word, true);
			}
			if (out == null) {
				out = replace1b(word, mWord);
			}
		}
		if (out == null) {
			out = word;
		}
		return out;
	}

	/**
	 * Wendet Regeln 1c des Porter-Algorithmus an.
	 * 
	 * @param word
	 * @param mWord
	 * @return
	 */
	String step1c(String word, String mWord) {
		boolean[] test = check(word, mWord, "y");
		if (test[1]) {
			String out = replace(word, mWord, "y", "i");
			if (out != null) {
				return out;
			}
		}
		return word;
	}

	/**
	 * Wendet Regeln 2 des Porter-Algorithmus an.
	 * 
	 * @param word
	 * @param mWord
	 * @return
	 */
	String step2(String word, String mWord) {
		String temp = mWord.substring(1, mWord.length() - 1);
		int m = Integer.parseInt(temp);
		String out = null;
		if (m > 0) {
			out = replaceV(word, mWord, "ational", "ate");
			if (out == null) {
				out = replaceV(word, mWord, "tional", "tion");
			}
			if (out == null) {
				out = replaceV(word, mWord, "enci", "ence");
			}
			if (out == null) {
				out = replaceV(word, mWord, "anci", "ance");
			}
			if (out == null) {
				out = replaceV(word, mWord, "izer", "ize");
			}
			if (out == null) {
				out = replaceV(word, mWord, "abli", "able");
			}
			if (out == null) {
				out = replaceV(word, mWord, "alli", "al");
			}
			if (out == null) {
				out = replaceV(word, mWord, "entli", "ent");
			}
			if (out == null) {
				out = replaceV(word, mWord, "eli", "e");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ousli", "ous");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ization", "ize");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ation", "ate");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ator", "ate");
			}
			if (out == null) {
				out = replaceV(word, mWord, "alism", "al");
			}
			if (out == null) {
				out = replaceV(word, mWord, "iveness", "ive");
			}
			if (out == null) {
				out = replaceV(word, mWord, "fulness", "ful");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ousness", "ous");
			}
			if (out == null) {
				out = replaceV(word, mWord, "aliti", "al");
			}
			if (out == null) {
				out = replaceV(word, mWord, "iviti", "ive");
			}
			if (out == null) {
				out = replaceV(word, mWord, "biliti", "ble");
			}
		}
		if (out == null) {
			out = word;
		}
		return out;
	}

	/**
	 * Wendet Regeln 3 des Porter-Algorithmus an.
	 * 
	 * @param word
	 * @param mWord
	 * @return
	 */
	String step3(String word, String mWord) {
		String temp = mWord.substring(1, mWord.length() - 1);
		int m = Integer.parseInt(temp);
		String out = null;
		if (m > 0) {
			out = replaceV(word, mWord, "icate", "ic");
			if (out == null) {
				out = replaceV(word, mWord, "ative", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "alize", "al");
			}
			if (out == null) {
				out = replaceV(word, mWord, "iciti", "ic");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ical", "ic");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ful", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ness", "");
			}
		}
		if (out == null) {
			out = word;
		}
		return out;
	}

	/**
	 * Wendet Regeln 4 des Porter-Algorithmus an.
	 * 
	 * @param word
	 * @param mWord
	 * @return
	 */
	String step4(String word, String mWord) {
		String temp = mWord.substring(1, mWord.length() - 1);
		int m = Integer.parseInt(temp);
		String out = null;
		if (m > 1) {
			out = replaceV(word, mWord, "al", "");
			if (out == null) {
				out = replaceV(word, mWord, "ance", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ence", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "er", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ic", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "able", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ible", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ant", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ement", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ment", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ent", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "tion", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "sion", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ou", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ism", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ate", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "iti", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ous", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ive", "");
			}
			if (out == null) {
				out = replaceV(word, mWord, "ize", "");
			}
		}
		if (out == null) {
			out = word;
		}
		return out;
	}

	/**
	 * Wendet Regeln 5 des Porter-Algorithmus an.
	 * 
	 * @param word
	 * @param mWord
	 * @return
	 */
	String step5a(String word, String mWord) {
		String temp = mWord.substring(1, mWord.length() - 1);
		int m = Integer.parseInt(temp);
		if (m > 1) {
			String out = replace(word, mWord, "e", "");
			if (out != null) {
				return out;

			}
		}
		if (m == 1) {
			boolean[] test = check(word, mWord, "");
			if (!test[2]) {
				String out = replace(word, mWord, "e", "");
				if (out != null) {
					return out;

				}
			}
		}
		return word;
	}

	String step5b(String word, String mWord) {
		String temp = mWord.substring(1, mWord.length() - 1);
		int m = Integer.parseInt(temp);
		if (m > 1) {
			String out = replaceDD(word, false);
			if (out != null) {
				return out;
			}
		}
		return word;
	}

	String applyRules(String word) {
		String vcWord = convertCV(word);
		String mWord = convertM(vcWord);
		word = step1a(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		word = step1b(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		word = step1c(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		word = step2(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		word = step3(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		word = step4(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		word = step5a(word, mWord);

		vcWord = convertCV(word);
		mWord = convertM(vcWord);
		return step5b(word, mWord);
	}

	/**
	 * Wendet die Stammformreduktion auf alle Woerter im angegebenen String an
	 * 
	 * @param str Eingabestring
	 * @return String
	 */
	String truncateWords(String str) {
		str = " " + str + " ";
		str = str.toLowerCase();
		String out = "";
		int i = 0;
		int next = 0;
		while (next != -1) {
			String word = str.substring(i, next);
			out += applyRules(word) + " "; // basicformreduction
			i = next;
			int j = str.indexOf(" ", i + 1);
			int l = str.indexOf("/n", i + 1);
			next = Math.min(j, l);
		}
		System.out.print(".");
		return out;
	}

	/**
	 * Schreibt Stammformreduzierte Dateien in einen neuen Pfad
	 * 
	 * @param readPath
	 * @param writePath
	 */
	void openSave(String readPath, String writePath) {
		String temp;
		String out = "";
		String string = "";

		try {

			final File folder = new File(readPath);
			for (final File fileEntry : folder.listFiles()) {

				FileReader fr = new FileReader(fileEntry);
				BufferedReader br = new BufferedReader(fr);
				out = "";

				do {
					temp = br.readLine();
					out += temp;
				} while (temp != null);

				string = truncateWords(out);

				// Datei wird dann in path/file.txt geschrieben
				FileWriter fw = new FileWriter(writePath + fileEntry.getPath());
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(string);
				bw.close();
				br.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) { Scanner sc = new Scanner(System.in);
	 * String word = sc.next();
	 * 
	 * BasicFormReduction b = new BasicFormReduction(); String out =
	 * b.applyRules(word); System.out.println(out); }
	 */

}
