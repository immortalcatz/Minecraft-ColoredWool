package org.minecraftnauja.coloredwool;

/**
 * Enum for importation errors.
 */
public enum ImportError {

	Orientation("ERROR: Image axis must use different orientation axes."),

	ImageNotFound("ERROR: Image load failed.");

	/**
	 * Error message.
	 */
	private final String message;

	/**
	 * Initializing constructor.
	 * 
	 * @param message
	 *            error message.
	 */
	private ImportError(String message) {
		this.message = message;
	}

	/**
	 * Gets the error message.
	 * 
	 * @return the error message.
	 */
	public String getMessage() {
		return message;
	}

}
