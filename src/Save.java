import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/**
 * Hilfsklasse, um Dateien/Ordner zu loeschen und Listen zu speichern
 * 
 * @author Johannes Wawra
 *
 */
public class Save {
	boolean saveList(@SuppressWarnings("rawtypes") LinkedList list, String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(list);
			oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	LinkedList<String> openList(String filename) {
		LinkedList<String> list = null;
		try {
			FileInputStream fis = new FileInputStream(filename);
			ObjectInputStream ois = new ObjectInputStream(fis);
			list = (LinkedList<String>) ois.readObject();
			ois.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	boolean deleteDirectory(String path) {
		try {
			final File folder = new File(path);
			for (final File fileEntry : folder.listFiles()) {
				fileEntry.delete();
			}
			folder.delete();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
