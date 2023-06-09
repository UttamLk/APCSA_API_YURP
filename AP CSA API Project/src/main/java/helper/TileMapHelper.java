package helper;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import core.GameScreen;
import objects.player.Player;

import static helper.Constants.*;

public class TileMapHelper {
    private TiledMap tiledMap;
    private GameScreen gameScreen;
   
    public TileMapHelper(GameScreen gameScreen){
        this.gameScreen = gameScreen;
    }

    public OrthogonalTiledMapRenderer setUpMap(){
        tiledMap = new TmxMapLoader().load("Maps/map2.tmx");
        parseMapObject(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    public OrthogonalTiledMapRenderer setUpMap(String mapName){
        tiledMap = new TmxMapLoader().load("Maps/"+mapName);
        parseMapObject(tiledMap.getLayers().get("objects").getObjects());
        return new OrthogonalTiledMapRenderer(tiledMap);
    }

    /*
    *Used to iterate through every MapObject and assign properties based on name
    *@param mapObjects an array containing mapObjects.
    */
    private void parseMapObject(MapObjects mapObjects){
        for(MapObject mapObject : mapObjects){
            if(mapObject instanceof PolygonMapObject){
                PolygonMapObject mO = ((PolygonMapObject) mapObject);
                String name = mO.getName();
                if(name.equals("spikes")){
                    createStaticBody((PolygonMapObject) mapObject,900);
                }
                else if(name.equals("flag")){
                    createStaticBody((PolygonMapObject) mapObject,800);
                }
                else{
                    createStaticBody((PolygonMapObject) mapObject);
                }
            }

            if(mapObject instanceof RectangleMapObject){
                Rectangle rectangle = ((RectangleMapObject) mapObject).getRectangle();
                String rectangleName = mapObject.getName();

                if(rectangleName.equals("player")){
                    Body body = BodyHelperService.createBody(
                            rectangle.getX() + rectangle.getWidth() / 2,
                            rectangle.getY() + rectangle.getHeight() / 2,
                            rectangle.getWidth(),
                            rectangle.getHeight(),
                            false,
                            gameScreen.getWorld()
                    );
                    gameScreen.setPlayer(new Player(rectangle.getWidth(), rectangle.getHeight(), body));
                }
            }
        }
    }

    /*
    *Create a StaticBody
    *@param polygonMapObject a MapObject of type Polygon.
    */
    private void createStaticBody(PolygonMapObject polygonMapObject){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        String name = polygonMapObject.getName();
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, 1000);
        shape.dispose();
    }

    /*
    *In the case of a special PolygonMapObject such as a flag or spike
    *@param polygonMapObject A Polygon in the Tilemap
    *@param density The density which you you want to assign special objects.
    */
    private void createStaticBody(PolygonMapObject polygonMapObject, int density){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        String name = polygonMapObject.getName();
        Shape shape = createPolygonShape(polygonMapObject);
        body.createFixture(shape, density);
        shape.dispose();
    }
    
    /*
    *Used to create PolygonShapes
    *@param polygonMapObject A Polygon in the Tilemap
    */
    private Shape createPolygonShape(PolygonMapObject polygonMapObject) {
        float[] vertices = polygonMapObject.getPolygon().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for(int i=0; i<vertices.length/2; i++){
            Vector2 current = new Vector2(vertices[i*2]/PPM, vertices[i*2+1]/PPM);
            worldVertices[i] = current;
        }
        PolygonShape shape = new PolygonShape();
        shape.set(worldVertices);

        return shape;
    }
}
