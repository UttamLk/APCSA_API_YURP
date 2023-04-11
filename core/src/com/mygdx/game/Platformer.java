package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Platformer extends Game {

	public static Platformer INSTANCE;
	private int widthScreen,heightScreen;
	private OrthographicCamera orthographicCamera;

	public Platformer(){
		INSTANCE = this;
	}

	@Override
	public void create(){
		this.widthScreen=Gdx.graphics.getWidth();
		this.heightScreen=Gdx.graphics.getHeight();
		this.orthographicCamera=new OrthographicCamera();
		this.orthographicCamera.setToOrtho(false, widthScreen, heightScreen);
	}
}
