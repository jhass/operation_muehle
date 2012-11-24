package de.hshannover.operation_muehle.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOOperation {

	/**
	 * Loads the SaveState from the file specified with name.
	 * 
	 * @param name Name of the file.
	 * @return A SaveState from the file.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @see SaveState
	 */
	public static SaveState loadGameInfo(String name) throws IOException, ClassNotFoundException {
		SaveState s = null; // Eclipse complaining if not initializing here....
			FileInputStream file = new FileInputStream(name);
			ObjectInputStream pump = new ObjectInputStream(file);
			s = (SaveState) pump.readObject();
			pump.close();
		return s;
	}

	/**
	 * Saves the given GameState in a file with the given Name.
	 * 
	 * @param name Name of the Savefile.
	 * @param data The Data thats to be saved.
	 * @throws IOException 
	 * @see GameState
	 */
	public static void saveGameInfo(String name, SaveState data) throws IOException {

			FileOutputStream file = new FileOutputStream(name);
			ObjectOutputStream pump = new ObjectOutputStream(file);
			pump.writeObject(data);
			pump.close();
			System.out.println("Game Saved."); // GUI should print this. How?

	}

}
