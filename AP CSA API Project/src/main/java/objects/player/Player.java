package objects.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static helper.Constants.*;

public class Player extends GameEntity{

    private int jumpCounter;

    private Sprite playerIdle = new Sprite(new Texture("Sprites/playerIdle.png"));
    private Sprite playerRun = new Sprite(new Texture("Sprites/playerRun.png"));

    public Player(float width, float height, Body body) {
        super(width, height, body);
        this.speed = 10f;
        this.jumpCounter=0;
    }

    @Override
    public void update() {
        x = body.getPosition().x * PPM;
        y = body.getPosition().y * PPM;
        checkUserInput();
    }

    @Override
    public void render(SpriteBatch batch) {
        // First we position and rotate the sprite correctly
        float posX = body.getPosition().x * PPM;
        float posY = body.getPosition().y * PPM;
        float rotation = (float) Math.toDegrees(body.getAngle());

        if(velX==0 && velY==0){
            playerIdle.setPosition(x-32, y-32);
            playerIdle.setRotation(rotation);
            playerIdle.draw(batch);

        }
        else if(velX<0){
            playerRun.setScale(-1.0f,1.0f);
            playerRun.setPosition(x-32,y-32);
            playerRun.draw(batch);
        }
        else if(velX>0){
            playerRun.setScale(1.0f,1.0f);
            playerRun.setPosition(x-32,y-32);
            playerRun.setRotation(rotation);
            playerRun.draw(batch);
        }
    }

    private void checkUserInput(){
        velX=0;
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            velX=1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            velX=-1;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && jumpCounter<2){
            float force = body.getMass()*8.5f;
            body.applyLinearImpulse(new Vector2(0,force),body.getPosition(),true);
            jumpCounter++;
        }

        //reset jumpCounter
        if(body.getLinearVelocity().y==0){
            jumpCounter=0;
        }

        body.setLinearVelocity(velX*speed,body.getLinearVelocity().y);
    }

}
