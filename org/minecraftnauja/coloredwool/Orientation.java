package org.minecraftnauja.coloredwool;

public enum Orientation {

	North(Axis.Z) {

		@Override
		public Orientation getNext() {
			return South;
		}

		@Override
		public int getDZ() {
			return -1;
		}

	},

	South(Axis.Z) {

		@Override
		public Orientation getNext() {
			return East;
		}

		@Override
		public int getDZ() {
			return 1;
		}

	},

	East(Axis.X) {

		@Override
		public Orientation getNext() {
			return West;
		}

		@Override
		public int getDX() {
			return 1;
		}

	},

	West(Axis.X) {

		@Override
		public Orientation getNext() {
			return Up;
		}

		@Override
		public int getDX() {
			return -1;
		}

	},

	Up(Axis.Y) {

		@Override
		public Orientation getNext() {
			return Down;
		}

		@Override
		public int getDY() {
			return 1;
		}

	},

	Down(Axis.Y) {

		@Override
		public Orientation getNext() {
			return North;
		}

		@Override
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
	private Orientation(final Axis axis) {
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
	public boolean isCompatible(final Orientation other) {
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
