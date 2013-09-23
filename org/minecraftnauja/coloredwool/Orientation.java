package org.minecraftnauja.coloredwool;

public enum Orientation {

	North(Axis.Z) {

		public Orientation getNext() {
			return South;
		}
		
		public int getDZ() {
			return -1;
		}

	},

	South(Axis.Z) {

		public Orientation getNext() {
			return East;
		}
		
		public int getDZ() {
			return 1;
		}

	},

	East(Axis.X) {

		public Orientation getNext() {
			return West;
		}
		
		public int getDX() {
			return 1;
		}

	},

	West(Axis.X) {

		public Orientation getNext() {
			return Up;
		}
		
		public int getDX() {
			return -1;
		}

	},

	Up(Axis.Y) {

		public Orientation getNext() {
			return Down;
		}
		
		public int getDY() {
			return 1;
		}

	},

	Down(Axis.Y) {

		public Orientation getNext() {
			return North;
		}
		
		public int getDY() {
			return -1;
		}

	};

	/**
	 * Enums for axis.
	 */
	public static enum Axis {

		X, Y, Z

	}

	/**
	 * Corresponding axis.
	 */
	private final Axis axis;

	/**
	 * Initializing constructor.
	 * 
	 * @param axis
	 *            corresponding axis.
	 */
	private Orientation(Axis axis) {
		this.axis = axis;
	}

	/**
	 * Gets the next orientation.
	 * 
	 * @return the next orientation.
	 */
	public abstract Orientation getNext();

	/**
	 * Indicates if two orientations are compatibles.
	 * 
	 * @param other
	 *            another orientation.
	 * @return if they are compatible.
	 */
	public boolean isCompatible(Orientation other) {
		return axis != other.axis;
	}

	/**
	 * Gets the translation on the x-axis.
	 * 
	 * @return the translation on the x-axis.
	 */
	public int getDX() {
		return 0;
	}

	/**
	 * Gets the translation on the y-axis.
	 * 
	 * @return the translation on the y-axis.
	 */
	public int getDY() {
		return 0;
	}

	/**
	 * Gets the translation on the z-axis.
	 * 
	 * @return the translation on the z-axis.
	 */
	public int getDZ() {
		return 0;
	}

}
