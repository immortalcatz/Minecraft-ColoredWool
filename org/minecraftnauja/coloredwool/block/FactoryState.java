package org.minecraftnauja.coloredwool.block;

/**
 * Enums for factories states.
 */
public enum FactoryState {

	/**
	 * The factory is active and burning.
	 */
	Burning {

		public String getSuffix() {
			return "Burning";
		}

		public String getActiveSuffix() {
			return Active.getActiveSuffix();
		}

	},

	/**
	 * The factory is active.
	 */
	Active {

		public String getSuffix() {
			return "Active";
		}

	},

	/**
	 * The factory is inactive.
	 */
	Idle {

		public String getSuffix() {
			return "Idle";
		}

	};

	/**
	 * Gets the icons suffix associated to this state.
	 * 
	 * @return the icons suffix associated to this state.
	 */
	public abstract String getSuffix();

	/**
	 * Gets the icons suffix associated to this state.
	 * 
	 * @return the icons suffix associated to this state.
	 */
	public String getActiveSuffix() {
		return getSuffix();
	}

}
