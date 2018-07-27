package core.Base;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ArrayMap;
import core.Bullet.BulletWorld;

public abstract class BaseScreen extends BulletWorld implements Screen, InputProcessor {
    public  ArrayMap<String, BaseActor3D.Constructor> constructors;
    private Model model;

    protected Stage3D mainStage3D;
    protected Stage uiStage;
    protected Table uiTable;

    protected Vector2 screenWH;

    public BaseScreen() {
        super.init();
        mainStage3D = new Stage3D();
        uiStage   = new Stage();

        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);

        constructor();
        initialize();

        screenWH = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public abstract void initialize();

    @Override
    public void update(float dt) {
        super.update(dt);
    }

    public void constructor(){
        int usageCode = VertexAttributes.Usage.Position + VertexAttributes.Usage.ColorPacked +
                VertexAttributes.Usage.Normal + VertexAttributes.Usage.TextureCoordinates;
        ModelBuilder mb = new ModelBuilder();
        mb.begin();
        mb.node().id = "ground";
        mb.part("ground", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.RED)))
                .box(5000f, 0.001f, 5000f);
        mb.node().id = "panorama";
        /*
        mb.part("panorama", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position |
                VertexAttributes.Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)))
                .box(640f,0.001f,480f);*/
        mb.part("panorama", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.LIGHT_GRAY)))
                .box(480f,0.001f,640f);
        mb.node().id = "sphere";
        mb.part("sphere", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .sphere(0.07f, 0.07f, 0.07f, 5, 5);
        mb.node().id = "box";
        mb.part("box", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.BLUE)))
                .box(0.7f, 0.7f, 0.7f);
        mb.node().id = "cone";
        mb.part("cone", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .cone(1f, 2f, 1f, 10);
        mb.node().id = "coneSoma";
        mb.part("coneSoma", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .cone(0.07f, 0.07f, 0.07f, 3);
        mb.node().id = "coneFlatSoma";
        mb.part("coneFlatSoma", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .cone(0.07f, 0.07f, 0.07f, 2);
        mb.node().id = "coneSynapse";
        mb.part("coneSynapse", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.YELLOW)))
                .cone(0.01f, 0.05f, 0.01f, 3);
        mb.node().id = "capsule";
        mb.part("capsule", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.CYAN)))
                .capsule(0.005f, 0.02f, 10);
        mb.node().id = "cylinder";
        mb.part("cylinder", GL20.GL_TRIANGLES, usageCode, new Material(ColorAttribute.createDiffuse(Color.MAGENTA)))
                .cylinder(1f, 2f, 1f, 10);
        model = mb.end();

        constructors = new ArrayMap<String, BaseActor3D.Constructor>(String.class, BaseActor3D.Constructor.class);
        constructors.put("ground", new BaseActor3D.Constructor(
                model, "ground", new btBoxShape(new Vector3(2500f, 0.0005f, 2500f)), 0f));
        constructors.put("panorama", new BaseActor3D.Constructor(
                model, "panorama", new btBoxShape(new Vector3(240f,0.0005f,320f)), 0f));
        constructors.put("sphere", new BaseActor3D.Constructor(
                model, "sphere", new btSphereShape(0.035f), 0f));
        constructors.put("box", new BaseActor3D.Constructor(
                model, "box", new btBoxShape(new Vector3(0.35f, 0.35f, 0.35f)), 0f));
        constructors.put("cone", new BaseActor3D.Constructor(
                model, "cone", new btConeShape(0.5f, 2f), 1f));
        constructors.put("coneSoma", new BaseActor3D.Constructor(
                model, "coneSoma", new btConeShape(0.035f, 0.035f), 0f));
        constructors.put("coneFlatSoma", new BaseActor3D.Constructor(
                model, "coneFlatSoma", new btConeShape(0.035f, 0.035f), 0f));
        constructors.put("coneSynapse", new BaseActor3D.Constructor(
                model, "coneSynapse", new btConeShape(0.005f, 0.025f), 0f));
        constructors.put("capsule", new BaseActor3D.Constructor(
                model, "capsule", new btCapsuleShape(0.0025f, 0.02f), 1f));
        constructors.put("cylinder", new BaseActor3D.Constructor(
                model, "cylinder", new btCylinderShape(new Vector3(.5f, 1f, .5f)),100f));
    }

     /*
        ShapeRenderer shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.line(columns[0][0].getUniNeuron()[1][1].getAxonList().get(0).getPositionAxon(),
						   columns[0][0].getUniNeuron()[0][4].getDendriteList().get(0).getPositionDendrite());
		//shapeRenderer.line(0,0,100,100);
		shapeRenderer.setColor(Color.WHITE);

		shapeRenderer.end();

		shapeRenderer.begin(ShapeType.Line);
 shapeRenderer.setColor(1, 1, 0, 1);
 shapeRenderer.line(x, y, x2, y2);
 shapeRenderer.rect(x, y, width, height);
 shapeRenderer.circle(x, y, radius);
 shapeRenderer.end();

 shapeRenderer.begin(ShapeType.Filled);
 shapeRenderer.setColor(0, 1, 0, 1);
 shapeRenderer.rect(x, y, width, height);
 shapeRenderer.circle(x, y, radius);
 shapeRenderer.end();

 shapeRenderer.begin(ShapeType.Line);
 shapeRenderer.identity();
 shapeRenderer.translate(20, 12, 2);
 shapeRenderer.rotate(0, 0, 1, 90);
 shapeRenderer.rect(-width / 2, -height / 2, width, height);
 shapeRenderer.end();*/

    // this is the gameloop. update, then render.
    public void render(float dt) 
    {
         // limit amount of time that can pass while window is being dragged
        dt = Math.min(dt, 1/30f);
        
        // act methods
        uiStage.act(dt);
        mainStage3D.act(dt);

        // defined by user
        update(dt);
        
        // render
        //Gdx.gl.glClearColor(0.5f,0.5f,0.5f,1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT + GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0,0.5f,0.7f,1);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        
        // draw the graphics
        mainStage3D.draw();
        uiStage.draw();
    }

    // methods required by Screen interface
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    public void pause()   {  }

    public void resume()  {  }

    public void dispose() {
        super.dispose();
    }

     /**
     *  Called when this becomes the active screen in a Game.
     *  Set up InputMultiplexer here, in case screen is reactivated at a later time.
     */
    public void show()    
    {  
        InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
        //im.addProcessor(this);
        im.addProcessor(uiStage);
        im.addProcessor(mainStage3D.getController());

    }

    /**
     *  Called when this is no longer the active screen in a Game.
     *  Screen class and Stages no longer process input.
     *  Other InputProcessors must be removed manually.
     */
    public void hide()    
    {   
        InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
        //im.removeProcessor(this);
        im.removeProcessor(uiStage);
        im.removeProcessor(mainStage3D.getController());

    }
    
    // methods required by InputProcessor interface
    public boolean keyDown(int keycode)
    {  return false;  }

    public boolean keyUp(int keycode)
    {  return false;  }

    public boolean keyTyped(char c) 
    {  return false;  }

    public boolean mouseMoved(int screenX, int screenY)
    {  return false;  }

    public boolean scrolled(int amount) 
    {  return false;  }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) 
    {  return false;  }

    public boolean touchDragged(int screenX, int screenY, int pointer) 
    {  return false;  }

    public boolean touchUp(int screenX, int screenY, int pointer, int button) 
    {  return false;  }
}