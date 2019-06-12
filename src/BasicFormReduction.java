
public class BasicFormReduction {

	String convertCV(String input) {
		char[] inputC = input.toCharArray();
		char[] outputC = new char[input.length()+1];
		int i = 0;
		char remC = ' ';
		for(char c:inputC) {
			
			if(c == 'a' || c =='e' || c =='i' || c =='o' || c =='u') {
				if(remC != 'V') {
					i += 1;
				}
				outputC[i] = 'V';
				remC = 'V';
			} else {
				if(remC != 'C') {
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
			if(i==0 && c == 'C') {
				cStart = true;
			}
			if(c == 'V') {
				vEnd = true;
			}
			if(c=='C' && vEnd) {
				vcCount += 1;
				vEnd = false;
			}
		}
		String out;;
		if(cStart) {
			out = "C";			
		} else {
			out = "X";				
		}
		out += String.valueOf(vcCount);
		if(vEnd) {
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
			endWord = true;
		}
		
		String temp = mWord.substring(1,mWord.length() -1);
		int m = Integer.parseInt(temp);
		if(m>0 || mWord.charAt(mWord.length()-1) == 'V') {
			vowel = true;
		}
		if((m == 1 && mWord.charAt(0) == 'C' && mWord.charAt(mWord.length()-1) == 'Y')
				||(m > 1 && mWord.charAt(mWord.length()-1) == 'Y')) {
			char lc = word.charAt(word.length()-1);
			if(lc != 'w' && lc != 'x' && lc != 'y') {
				hop = true;	
			}	
		}
		boolean[] outbool= new boolean[3];
		outbool[0] = endWord;
		outbool[1] = vowel;
		outbool[2] = hop;
		return outbool;
	}
	
	String replaceDD(String word, boolean switch1b5b) {
		char l = word.charAt(word.length()-1);
		if(word.length()<2) {
			return null;
		}
		char ll = word.charAt(word.length()-2);
		if(l == ll) {
			if (l == 'a'||l == 'e'||l == 'i'||l == 'o'||l == 'u') {
				return null;
			}
			if (switch1b5b && (l == 'l' ||l == 's' ||l == 'z')){
				return null;
			}
			if (!switch1b5b && l != 'l'){
				return null;
			}
			return word.substring(0,word.length()-2);
		}
		return null;
	}
	
	
	
	String replace(String word, String mWord, String end, String newEnd) {
		boolean[] c= this.check(word, mWord, end);
		String out = null;
		if(c[0]) {
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
			if(test[1]) {
				return out;
			}
		}
		return word;
	}
	
	String replaceM (String word, String mWord, String end, String newEnd, int min) {
		String out = this.replace(word, mWord, end, newEnd);
		if (out != null) {
			String vcWord = this.convertCV(out);
			String mWord2 = this.convertM(vcWord);
			
			String temp = mWord2.substring(1,mWord2.length() -1);
			int m = Integer.parseInt(temp);
			
			if(m>min) {
				return out;
			}
		}
		return word;	
	}
	
	
	
	
	String step1a(String word, String mWord) {
		String out = replace(word,mWord, "sses", "ss");
		if(out == null) {
			out = replace(word,mWord, "ies", "i");			
		}
		if(out == null) {
			out = replace(word,mWord, "ss", "ss");			
		}
		if(out == null) {
			out = replace(word,mWord, "s", "");			
		}
		if(out == null) {
			out = word;			
		}
		return out;
	}
	
	String step1b(String word, String mWord) {
		String out = replaceM(word,mWord, "eed", "ee", 0);
		if(out != null) {
			return out;
		}
		if(out == null) {
			out = replaceV(word,mWord, "ed", "");			
		}
		if(out == null) {
			out = replaceV(word,mWord, "ing", "");			
		}
		if(out != null) {
			if(out == null) {
				out = replaceV(word,mWord, "at", "ate");			
			}
			if(out == null) {
				out = replaceV(word,mWord, "bl", "ble");			
			}
			if(out == null) {
				out = replaceV(word,mWord, "iz", "ize");			
			}
			
			
			
			
			
			
			
		}
		if(out == null) {
			out = word;			
		}
		return out;
	}
	
	
	
	
	
	
	
	
	
	
	String step1c(String word, String mWord) {
		String out = replaceM(word,mWord, "eed", "ee", 0);
		if(out == null) {
			out = replaceV(word,mWord, "ed", "");			
		}
		if(out == null) {
			out = replaceV(word,mWord, "ing", "");			
		}
		if(out == null) {
			out = word;			
		}
		return out;
	}
	
	
	public static void main(String[] args) {
		BasicFormReduction b = new BasicFormReduction();
		String word = "caress";
		String vcWord = b.convertCV(word);
		System.out.println(vcWord);
		String mWord = b.convertM(vcWord);
		System.out.println(mWord);
		boolean[] ch = b.check(word, mWord, "cat");
		for(int i = 0; i<3; i++) {
			System.out.print(ch[i]);
		}
		String convert = b.step1a(word, mWord);
		System.out.println("\n" + convert);
	}
}
