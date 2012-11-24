package de.hshannover.operation_muehle.gui;

import java.awt.Color;
import java.awt.Font;

public class Theme {
	public static final String WHITE_STONE_TEXTURE
		= "white-marble.jpg";
	public static final String BLACK_STONE_TEXTURE
		= "dark-wood.jpg";
	public static final String BOARD_TEXTURE
		= "light-wood.jpg";
	public static final Color  SPOT_HINT
		= new Color(0xEE222222, true);
	public static final Color  SHADE_COLOR
		= new Color(0xDD222222, true);
	public static final Color  BACKGROUND_COLOR
		= new Color(11, 106, 11);
	public static final Color  BOARD_COLOR
		= Color.BLACK;
	public static final Color  INFO_BOX_BACKGROUND_COLOR
		= new Color(0x88000000, true);
	public static final Color  INFO_BOX_TEXT_COLOR
		= Color.WHITE;
	public static final Color  PLAYER_INFO_WHITE_ACTIVE_BG_COLOR
		= new Color(0xEEFFFFFF, true);
	public static final Color  PLAYER_INFO_WHITE_INACTIVE_BG_COLOR
		= new Color(0xAAFFFFFF, true);
	public static final Color  PLAYER_INFO_BLACK_ACTIVE_BG_COLOR
		= new Color(0xEE000000, true);
	public static final Color  PLAYER_INFO_BLACK_INACTIVE_BG_COLOR
		= new Color(0xAA000000, true);
	public static final Color  PLAYER_INFO_WHITE_TEXT_COLOR
		= Color.BLACK;
	public static final Color  PLAYER_INFO_BLACK_TEXT_COLOR
		= Color.WHITE;
	public static final Color  MESSAGE_BOX_BACKGROUND_COLOR
		= new Color(0x88000000, true);
	public static final Color  MESSAGE_BOX_TEXT_COLOR
		= Color.WHITE;
	
	public static Font getInfoBoxFont(Font base) {
		return base.deriveFont(Font.BOLD).deriveFont(16.0f);
	}
	
	public static Font getPlayerInfoHeader(Font base, boolean active) {
		base = base.deriveFont(14.0f);
		if (active) {
			return base.deriveFont(Font.BOLD);
		} else {
			return base.deriveFont(Font.ITALIC);
		}
	}
	
	public static Font getMessageBoxFont(Font base) {
		return base;
	}
}
