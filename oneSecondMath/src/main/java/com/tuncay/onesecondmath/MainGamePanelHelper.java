package com.tuncay.onesecondmath;

import com.tuncay.onesecondmath.component.SoundManager;
import com.tuncay.onesecondmath.entity.SoundMenu;

public class MainGamePanelHelper {
	private SoundMenu soundMenu;
	private SoundManager soundManager;
	

	public MainGamePanelHelper() {
	}

	public SoundMenu getSoundMenu() {
		return soundMenu;
	}

	public void setSoundMenu(SoundMenu soundMenu) {
		this.soundMenu = soundMenu;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}
}