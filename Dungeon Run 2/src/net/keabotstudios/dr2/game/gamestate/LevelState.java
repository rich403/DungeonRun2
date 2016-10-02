package net.keabotstudios.dr2.game.gamestate;

import net.keabotstudios.dr2.game.GameInfo;
import net.keabotstudios.dr2.game.gui.Gui;
import net.keabotstudios.dr2.game.gui.Gui.GuiColor;
import net.keabotstudios.dr2.game.level.Level;
import net.keabotstudios.dr2.gfx.Bitmap;
import net.keabotstudios.dr2.gfx.Bitmap3D;
import net.keabotstudios.superin.Input;

public class LevelState extends GameState {
	
	private Level level;
	private Bitmap3D bitmap3d;

	public LevelState(GameStateManager gsm, Level level) {
		super(gsm);
		this.level = level;
		this.bitmap3d = new Bitmap3D(GameInfo.GAME_WIDTH, GameInfo.GAME_HEIGHT);
	}

	public void render(Bitmap bitmap) {
		bitmap3d.renderLevel(level);
		bitmap3d.renderDistanceLimiter(level.getRenderDistance());
		bitmap.render(bitmap3d, 0, 0);
		int guiX = -6;
		int guiY = GameInfo.GAME_HEIGHT - (16 * 3) - 10;
		Gui.renderGuiBar(bitmap, "+", guiX, guiY, 1, (int) Math.round(Math.sin(GameInfo.TIME / 60.0) * 10.0) + 10, 20, GuiColor.ORANGE, GuiColor.RED);
		Gui.renderGuiBar(bitmap, "$", guiX, guiY + 20, 1, (int) -Math.round(Math.sin(GameInfo.TIME / 60.0) * 10.0) + 10, 20, GuiColor.ORANGE, GuiColor.GREEN);
		String[] text = new String[] {
				"" + ((int) Math.round(Math.sin(GameInfo.TIME / 60.0) * 5.0) + 5), "/", "10"
		};
		Gui.renderGuiText(bitmap, "=", guiX, guiY + 40, 1, text, GuiColor.ORANGE);
	}

	public void update(Input input) {
		level.update(input);
		bitmap3d.setOffsets(level.getPlayer());
	}

}
