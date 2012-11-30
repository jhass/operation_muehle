package de.hshannover.operation_muehle.ai;

import java.util.HashMap;
import java.util.Iterator;

import de.hshannover.inform.muehle.strategy.Move;
import de.hshannover.inform.muehle.strategy.Slot;

public class AIBoard {

	public static class Coord {
		public Coord(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Coord(char x, int y) {
			this.x = x - AIBoard.CHAROFFSET + 1;
			this.y = y;
		}

		public int x, y;

		public int getVerticalDistance() {
			int result = 0;

			if (x == 1 || x == 7)
				result = 3;
			else if (x == 2 || x == 6)
				result = 2;
			else
				result = 1;

			return result;
		}

		public int getHorizontalDistance() {
			int result = 0;

			int tmp = this.x;
			this.x = this.y;
			this.y = tmp;

			result = this.getVerticalDistance();

			tmp = this.x;
			this.x = this.y;
			this.y = tmp;

			return result;
		}
	}

	public enum Position {
		LEFT, RIGHT, UNDER, ABOVE;
		public Coord toCoord() {
			int x = -1;
			int y = 0;
			switch (this) {
			case RIGHT:
				x = 1;
				break;
			case UNDER:
				x = 0;
				y = 1;
				break;
			case ABOVE:
				x = 0;
				y = -1;
				break;
			}
			return new Coord(x, y);
		}
	}

	public class Field {
		public int address;
		public Status status;

		public Field(int address, Status status) {
			this.address = address;
			this.status = status;
		}
	}

	public static final int CHAROFFSET = 'A';

	private HashMap<Integer, Status> fields;

	public AIBoard() {
		this.fields = new HashMap<Integer, Status>();
		initializeFields();
	}

	private void initializeFields() {
		for (int i = 1; i < 4; i++) {
			createField('A', 1 + (i - 1) * 3);
			createField('B', 2 * i);
			createField('C', i + 2);
			createField('D', i);
			createField('D', 4 + i);
			createField('E', i + 2);
			createField('F', 2 * i);
			createField('G', 1 + (i - 1) * 3);
		}

	}

	public void createField(char col, int row) {
		this.fields.put(this.generateAddress(col, row), Status.EMPTY);
	}

	public Slot generateSlotByAddress(int address) {
		int row = address % 10;
		char col = (char) ((address - row) / 10);
		return new G2Slot(col, row);
	}

	public int generateAddress(char col, int row) {
		return (col * 10) + row;
	}

	public int generateAddress(Slot slot) {
		return this.generateAddress(slot.getColumn(), slot.getRow());
	}

	public void placeStone(Slot slot, Status status) {
		if (!status.isStone()) {
			throw new IllegalArgumentException("Given Status " + status
					+ " does not represent a Stone.");
		}
		if (slot != null) {
			int address = this.generateAddress(slot);
			if (this.fields.containsKey(address)) {
				Status s = this.fields.get(address);
				if (s == Status.EMPTY) {
					if (status == Status.MYSTONE) {
					} else {
					}
					this.fields.put(address, status);
				} else {
					throw new IllegalArgumentException(
							("Field " + slot.getColumn())
									+ slot.getRow()
									+ " for placing a Stone has already a Stone.");
				}
			} else {
				throw new IllegalArgumentException(
						("Address " + slot.getColumn()) + slot.getRow()
								+ " is not valid.");
			}
		}
	}

	public void moveStone(Move move, Status status) {
		if (move != null) {
			Slot from = move.fromSlot();
			if (from != null) {
				this.removeStone(from);
			} else {
			}
			this.placeStone(move.toSlot(), status);
		}
	}

	public void removeStone(Slot remove) {
		if (remove != null) {
			int address = this.generateAddress(remove);
			Status status = this.fields.get(address);
			if (!status.isStone()) {
				Slot slot = this.generateSlotByAddress(address);
				throw new IllegalArgumentException(
						("Field " + slot.getColumn()) + slot.getRow()
								+ " does not contain a stone.");
			}
			if (status == Status.MYSTONE) {
			} else {
			}
			this.fields.put(address, Status.EMPTY);
		}
	}

	public HashMap<Integer, Status> getFields() {
		return this.fields;
	}

	public HashMap<Integer, Status> getFieldsWith(Status status) {
		HashMap<Integer, Status> result = new HashMap<Integer, Status>();

		Iterator<Integer> iterator = fields.keySet().iterator();
		Integer i;
		Status s;
		while (iterator.hasNext()) {
			i = iterator.next();
			s = fields.get(i);
			if (s == status) {
				result.put(i, status);
			}
		}

		return result;

	}

	public HashMap<Integer, Status> getNeighbours(Slot slot) {
		HashMap<Integer, Status> result = new HashMap<Integer, Status>();

		Coord coord = new Coord(slot.getColumn(), slot.getRow());
		int xDistance = coord.getHorizontalDistance();
		int yDistance = coord.getVerticalDistance();

		int address = this.generateAddress(
				(char) (slot.getColumn() + xDistance), slot.getRow());
		Status status = this.fields.get(address);
		if (status != null)
			result.put(address, status);
		address = this.generateAddress((char) (slot.getColumn() - xDistance),
				slot.getRow());
		status = this.fields.get(address);
		if (status != null)
			result.put(address, status);
		address = this.generateAddress((char) (slot.getColumn()),
				slot.getRow() + yDistance);
		status = this.fields.get(address);
		if (status != null)
			result.put(address, status);
		address = this.generateAddress((char) (slot.getColumn()),
				slot.getRow() - yDistance);
		status = this.fields.get(address);
		if (status != null)
			result.put(address, status);

		return result;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer("\n");
		String sym;

		for (int i = 7; i >= 1; i--) {
			result.append(i + "| ");
			for (int j = 'A'; j <= 'G'; j++) {
				Status status = this.fields.get(this.generateAddress((char) j,
						i));
				if (status == null) {
					sym = " ";
				} else {
					sym = status.toString();
				}
				result.append(sym).append(" ");
			}
			result.append("\n");
		}
		result.append(" ----------------\n");
		result.append("   A B C D E F G\n");

		return result.toString();
	}
}