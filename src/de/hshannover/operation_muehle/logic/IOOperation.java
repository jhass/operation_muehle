package de.hshannover.operation_muehle.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IOOperation {
	
	/**
	 * Loads the SaveState from the file specified with name.
	 * @param name Name of the file.
	 * @return A SaveState from the file.
	 * @see SaveState
	 */
	public static SaveState loadGameInfo(String name) {
		SaveState s = null; //Eclipse complaining if not initializing here....
		Boolean problems = true;
		while(problems) {
			try {
				FileInputStream file = new FileInputStream(name);
				ObjectInputStream pump = new ObjectInputStream(file);
				s = (SaveState) pump.readObject();
				pump.close();
				problems = false;
			} catch (FileNotFoundException e) {
				System.out.println("Can't find/acces file "+name);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Errormessage?
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Someone help me with this one, whats the problem?
				e.printStackTrace();
			}
		}
		return s;
	}
	
	
	/**
	 * Saves the given GameState in a file with the given Name.
	 * @param name Name of the Savefile.
	 * @param data The Data thats to be saved.
	 * @see GameState
	 */
	public static void saveGameInfo(String name, GameState data) {
		boolean problems = true;
		while(problems) {
			try {
				FileOutputStream file = new FileOutputStream(name);
				ObjectOutputStream pump = new ObjectOutputStream(file);
				pump.writeObject(data);
				pump.close();
				problems = false;
				System.out.println("Game Saved"); //GUI should print this. How?
			} catch (FileNotFoundException e) {
				System.out.println("Can't create/access the file "+name); //TODO Errormessage?
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Errormessage?
				e.printStackTrace();
			}
		}
	}



}
