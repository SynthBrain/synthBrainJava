package core.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import core.BulletObjects.BaseNeuron;

import java.util.ArrayList;
import java.util.TreeMap;

public class Stage3D {
    private Environment environment;
    public PerspectiveCamera camera;
    private CameraInputController controller;   // new
    private final ModelBatch modelBatch;
    private SpriteBatch batchGUI;               // new
    private ShapeRenderer shapeRenderer;         // new
    private BitmapFont font;                    // new
    //private ArrayList<BaseActor3D> actorList;
    private ArrayList<BaseNeuron> neuronList;

    private boolean drawObj;
    public int visibleCount;
    public int synapseCount;
    private Vector3 vector3;

    public Stage3D() {

        drawObj = true;
        visibleCount = 0; // счётчик видимых обьектов
        synapseCount = 0;

        environment = new Environment();
        environment.set( new ColorAttribute(ColorAttribute.AmbientLight, 0.7f, 0.7f, 0.7f, 1f) );

        DirectionalLight dLight = new DirectionalLight();
        Color     lightColor = new Color(0.9f, 0.9f, 0.9f, 1);
        Vector3  lightVector = new Vector3(-1.0f, -0.75f, -0.25f);
        dLight.set( lightColor, lightVector );
        environment.add( dLight ) ;

        camera = new PerspectiveCamera(67,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());// угол обзора камеры
        //camera.position.set(5,5,5); // откуда смотрит камера
        camera.position.set(0,0,0); // откуда смотрит камера
        camera.lookAt(0,0,-500);       // куда смотрит камера
        camera.near = (float) 0.01;   // как близко видит камера
        camera.far = 1000; // как далеко видит камера
        camera.update();

        vector3 = new Vector3(camera.position);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        controller = new CameraInputController(camera);
        controller.autoUpdate = true;

        modelBatch = new ModelBatch();
        batchGUI = new SpriteBatch();
        font = new BitmapFont();

        neuronList = new ArrayList<BaseNeuron>();
    }


    public void act(float dt) {
        camera.update();
        for (BaseNeuron ba : neuronList){
            ba.getActor3D().act(dt);
        }
    }

    /**
     * Попытка каждый 3D обьект рисовать в отдельном потоке но в OpenGl для графики доступен лиш один поток
     * @param ba 3D обьект из общего списка всех обьектов
     */
    public void drawModelInThread(BaseNeuron ba){
        Thread myThready = new Thread(() -> {
            ba.getActor3D().draw(modelBatch, environment);
        });
        myThready.start();
    }

    public void draw() {
        camera.update();

        visibleCount = 0;
        synapseCount = 0;

        modelBatch.begin(camera);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        if (drawObj){
            for (BaseNeuron bn : neuronList){
                if (bn.getActor3D().isVisible(camera)){
                    //drawModelInThread(ba); // невышло
                    bn.getActor3D().draw(modelBatch, environment);
                }
            }
        }
        shapeRenderer.end();
        modelBatch.end();

        batchGUI.begin();
        font.draw(batchGUI, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, Gdx.graphics.getHeight()-5);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //shapeRenderer.translate(0,0,0);
        drawShapeCoordSystem(Color.WHITE,0 , 0, 0, 50, 0, 0);
        drawShapeCoordSystem(Color.WHITE,0 , 0, 0, -50, 0, 0);
        drawShapeCoordSystem(Color.WHITE,0 , 0, 0, 0, 50, 0);
        //drawShapeCoordSystem(Color.WHITE,0 , 0, 0, 0, -50, 0);
        drawShapeCoordSystem(Color.WHITE,0 , 0, 0, 0, 0, 50);
        drawShapeCoordSystem(Color.WHITE,0 , 0, 0, 0, 0, -50);


        if (drawObj){
            for (BaseNeuron bn : neuronList){
                if (bn.getActor3D().isVisible(camera)){
                    bn.drawSynapse(shapeRenderer, Color.GRAY);
                    synapseCount += bn.getSynapseCount();
                    visibleCount++;
                }
            }
        }

        shapeRenderer.end();
        batchGUI.end();
    }

    public void drawShapeCoordSystem(Color color, float x, float y, float z, float x1, float y1, float z1){
        shapeRenderer.setColor(color);
        shapeRenderer.line(x,y,z, x1,y1,z1);
    }


    public void addNeuron(BaseNeuron object){
        neuronList.add( object );
        //System.out.println("Список пополнился" + neuronList);
        //System.out.println(neuronList.size());
    }

    public void setCameraPosition(float x, float y, float z)
    {  camera.position.set(x,y,z);  }

    public void setCameraPosition(Vector3 v)
    {  camera.position.set(v);  }

    public void moveCamera(float x, float y, float z)
    {  camera.position.add(x,y,z);  }

    public void moveCamera(Vector3 v)
    {  camera.position.add(v);  }

    public void moveCameraForward(float dist)
    {  
        Vector3 forward = new Vector3(camera.direction.x, 0, camera.direction.z).nor();
        moveCamera( forward.scl( dist ) );  
    }

    public void moveCameraRight(float dist)
    {  
        Vector3 right = new Vector3( camera.direction.z, 0, -camera.direction.x).nor();
        moveCamera( right.scl( dist ) );  
    }

    public void moveCameraUp(float dist)
    {  moveCamera( 0,dist,0 );  }

    public void setCameraDirection(Vector3 v)
    {
        camera.lookAt(v);
        camera.up.set(0,1,0);
    }

    public void setCameraDirection(float x, float y, float z)
    {   setCameraDirection( new Vector3(x,y,z) );   }

    public void turnCamera(float angle)
    {  camera.rotate( Vector3.Y, -angle );  }

    public void tiltCamera(float angle)
    {
        Vector3 side = new Vector3(camera.direction.z, 0, -camera.direction.x);
        camera.direction.rotate(side, angle);
    }

    public CameraInputController getController() {
        return controller;
    }

    public boolean isDrawObj() {
        return drawObj;
    }

    public void setDrawObj(boolean drawObj) {
        this.drawObj = drawObj;
    }

    public ArrayList<BaseNeuron> getNeuronList() {
        return neuronList;
    }

    public void setNeuronList(ArrayList neuronList) {
        this.neuronList = neuronList;
    }
}