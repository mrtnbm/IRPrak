import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.Scanner;

public class BasicFormReduction {

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
			if(endI == word.length()-end.length()) {
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

	String step1c(String word, String mWord) {
		boolean[] test = check(word, mWord, "y");
		if (test[1]) {
			String out = replace(word, mWord, "y", "i");
			if(out != null) {
				return out;
			}
		}
		return word;
	}

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
	
	
	String truncateWords(String str) {
		str = " " + str + " ";
		str = str.toLowerCase();
		String out = "";
		int i = 0;
		int next = 0;
		while(next != -1) {
			String word = str.substring(i,next);
			out += applyRules(word) + " "; //basicformreduction
			i = next;
			int j = str.indexOf(" ",i+1);
			int l = str.indexOf("/n",i+1);
			next = Math.min(j,l);
		}
		System.out.println(".");
		return out;
	}
	
	
	
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
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String word = sc.next();
		
		BasicFormReduction b = new BasicFormReduction();
		String out = b.applyRules(word);
		System.out.println(out);
	}
*/
	
}
