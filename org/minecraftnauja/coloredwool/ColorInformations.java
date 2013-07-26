package org.minecraftnauja.coloredwool;

import java.awt.Color;

public final class ColorInformations {

	private final String hex;
	private final int rgb;
	private final Color color;

	public ColorInformations(final String hex, final int rgb, final Color color) {
		this.hex = hex;
		this.rgb = rgb;
		this.color = color;
	}

	public String getHex() {
		return hex;
	}

	public int getRGB() {
		return rgb;
	}

	public Color getColor() {
		return color;
	}

}