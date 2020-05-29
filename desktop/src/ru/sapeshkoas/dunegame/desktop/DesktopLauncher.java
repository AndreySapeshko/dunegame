package ru.sapeshkoas.dunegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.sapeshkoas.dunegame.DuneGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 720;
		config.forceExit = false;
		config.x = 100;
		config.y = 50;
		new LwjglApplication(new DuneGame(), config);
	}
}
