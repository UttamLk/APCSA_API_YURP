package helper;

import com.badlogic.gdx.physics.box2d.*;

import static helper.Constants.PPM;

public class BodyHelperService {
    /*
    *Creates a body with the specified parameters
    *@param x,y Positional coordinates
    *@param width,height Width and Height of the Body
    *@param isStatic If the body will be a StaticBody or DynamicBody
    *@param world The world in which the body will be in.
    */
    public static Body createBody(float x, float y, float width, float height, boolean isStatic, World world){
        BodyDef bodyDef = new BodyDef();
        //bodyDef.type = isStatic ? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        if(isStatic==true){
            bodyDef.type = BodyDef.BodyType.StaticBody;
        }
        else{
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        bodyDef.position.set(x/PPM , y/PPM);
        bodyDef.fixedRotation = true;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2/PPM,height/2/PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
        shape.dispose();

        return body;
    }
}
