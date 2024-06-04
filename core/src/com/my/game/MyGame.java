package com.my.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.math.Rectangle;
import java.util.Iterator;

public class MyGame extends ApplicationAdapter {
	OrthographicCamera camera;
	SpriteBatch batch;
	Texture nasImage;
	Texture bekImage;
	Music nasMusic;
	Rectangle  bek;
	Vector3 touchPos;
	Array<Rectangle> nasDrop;
	long lastNasTime;
	
	@Override
	public void create () {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);
		batch = new SpriteBatch();
		Vector3 touchPos = new Vector3();
		nasImage = new Texture("nas.png");
		bekImage = new Texture("bek.png");


		nasMusic = Gdx.audio.newMusic(Gdx.files.internal("Nas.mp3"));

		nasMusic.setLooping(true);
		nasMusic.play();


		bek = new Rectangle();
		bek.x = 800 / 2 - 64/ 2;
		bek.y = 20;
		bek.width = 64;
		bek.height = 64;

		nasDrop = new Array<Rectangle>();
		spawnNasDrop();




	}

	private void spawnNasDrop() {
		Rectangle nasDrops = new Rectangle();
		nasDrops.x = MathUtils.random(0, 800 - 64);
		nasDrops.y = 480;
		nasDrops.width = 64;
		nasDrops.height = 64;
		nasDrop.add(nasDrops); // Добавляем объект в массив nasDrop
		lastNasTime = TimeUtils.nanoTime();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bekImage, bek.x, bek.y);
		for (Rectangle nasDrops : nasDrop){
			batch.draw(nasImage, nasDrops.x, nasDrops.y);
		}
		batch.end();


		if(Gdx.input.isTouched()){

			touchPos.set(Gdx.input.getX(),Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			bek.x = (int) (touchPos.x - 64 / 2);

		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) bek.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) bek.x += 200 * Gdx.graphics.getDeltaTime();

		if (bek.x < 0) bek.x = 0;
		if (bek.x > 800 - 64) bek.x = 800 - 64;

		if(TimeUtils.nanoTime() - lastNasTime > 1000000000) spawnNasDrop();

		Iterator<Rectangle> iter = nasDrop.iterator();
		while (iter.hasNext()){
			Rectangle nasDrops = iter.next();
			nasDrops.y -= 200 * Gdx.graphics.getDeltaTime();
			if (nasDrops.y + 64 < 0) iter.remove();
			if (nasDrops.overlaps(bek)){
				iter.remove();


			}
		}
	}
	
	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		bekImage.dispose();
		nasImage.dispose();
		nasMusic.dispose();

	}
}
