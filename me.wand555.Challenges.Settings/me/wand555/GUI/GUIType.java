package me.wand555.GUI;

public enum GUIType {
	OVERVIEW(null), BACKPACK(null), PUNISHMENT(OVERVIEW);

	private final GUIType goBack;
	
	GUIType(GUIType goBack) {
		this.goBack = goBack;
	}
	
	public GUIType getGoBack() {
		return this.goBack;
	}
}