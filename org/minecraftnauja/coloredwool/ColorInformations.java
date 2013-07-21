package org.minecraftnauja.coloredwool;

import java.awt.Color;

public final class ColorInformations {

	private String hex;
	private int rgb;
	private Color color;

	public ColorInformations(String hex, int rgb, Color color) {
		this.hex = hex;
		this.rgb = rgb;
		this.color = color;
	}

	public String getHex() {
		return this.hex;
	}

	public int getRGB() {
		return this.rgb;
	}

	public Color getColor() {
		return this.color;
	}

}