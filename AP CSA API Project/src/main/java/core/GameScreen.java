package core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import helper.Constants;
import helper.TileMapHelper;
import objects.player.Player;
import org.lwjgl.opengl.XRandR;

import static helper.Constants.*;
//import com.mygdx.game.helper.Constants;

public class GameScreen extends ScreenAdapter implements ContactListener{
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2dDebugRenderer;

    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private TileMapHelper tileMapHelper;

    //game objects
    private Player player;

    private boolean isDead;
    private boolean won;

    private int attemptCounter;

    public GameScreen(OrthographicCamera camera){
        this.camera = camera;
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0,-25f), true);
        this.world.setContactListener(this);
        this.box2dDebugRenderer = new Box2DDebugRenderer();

        this.tileMapHelper = new TileMapHelper(this);
        this.orthogonalTiledMapRenderer = tileMapHelper.setUpMap();

        this.attemptCounter = 2;
    }

    public void update(){
        world.step(1.0f/60.0f,6,2);
        cameraUpdate();

        batch.setProjectionMatrix(camera.combined);
        orthogonalTiledMapRenderer.setView(camera);
        player.update();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }

        if(won){
            Gdx.app.exit();
            won=false;
        }
    }

    private void cameraUpdate(){
        Vector3 position = camera.position;
        position.x = Math.round(player.getBody().getPosition().x * PPM * 10) / 10f;
        position.y = Math.round(player.getBody().getPosition().y * PPM * 10) / 10f;
        camera.position.set(position);
        camera.update();
    }

    @Override
    public void render(float delta){
        this.update();
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        orthogonalTiledMapRenderer.render();
        //System.out.println(player.getBody().getPosition().y);
        if(player.getBody().getPosition().y < -10){
            System.out.println("You died. Moving on to Attempt Number: "+attemptCounter);
            player.getBody().setTransform(PLAYER_X/PPM,PLAYER_Y/PPM,0);
            attemptCounter++;
        }

        if(isDead){
            player.getBody().setTransform(PLAYER_X/PPM,PLAYER_Y/PPM,0);
            attemptCounter++;
            isDead=false;
        }


        //if(MyContactListener.checkIfDie())

        batch.begin();
        //render objects

        player.render(batch);

        batch.end();
        //box2dDebugRenderer.render(world, camera.combined.scl(PPM));
    }


    public World getWorld() {
        return world;
    }

    public void setPlayer(Player player){
        this.player = player;
    }

    @Override
    public void beginContact(Contact contact) {
        // Get the two colliding bodies
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        // Check if one of the bodies has a density of 900, which is spike density
        if (fixtureB.getDensity()==900 || fixtureA.getDensity()==900){
            System.out.println("You died. Moving on to Attempt Number: "+attemptCounter);
            isDead = true;
        }

        if(fixtureB.getDensity()==800 || fixtureA.getDensity()==800){
            System.out.println("You won!");
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
