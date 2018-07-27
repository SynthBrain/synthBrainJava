package core.Base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import core.Bullet.MyContactListener;
import core.BulletObjects.Background;
import core.BulletObjects.BaseNeuron;
import core.BulletObjects.Panorama;
import core.Buttons.ButtonsUI;
import core.Controller.ControllerVision;
import core.Controller.ControllerVisionCortex;
import core.Neurons.AssigneeNeuron;


public class LevelScreen extends BaseScreen {

    /**
     * Обьекты
     */
    private Panorama panorama;
    //private ControllerVisionCortex controllerVisionCortex;
    private ControllerVision controllerVision;
    /**
     * Графический Интерфейс
     */
    //private Label neuronsLabel;
    private Label allElemLabel;
    private Label massage;
    private Label fpsLabel;
    private ButtonsUI buttonsUI;
    //****************************

    /**
     * Регистратор контактов
     */
    private core.Bullet.MyContactListener myContactListener;
    private float spawnTimer;

    public void initialize() {
        myContactListener = new MyContactListener(mainStage3D);
        /**
         * Создание обьекта
         */
        //background = new Background(constructors, GROUND_FLAG,(short)0, mainStage3D, dynamicsWorld);
        panorama = new Panorama(constructors,OBJECT_FLAG,ALL_FLAG,mainStage3D,dynamicsWorld);

        //controllerVisionCortex = new ControllerVisionCortex(constructors,OBJECT_FLAG,ALL_FLAG,mainStage3D,dynamicsWorld);
        controllerVision = new ControllerVision(constructors,OBJECT_FLAG,ALL_FLAG,mainStage3D,dynamicsWorld);

        /**
         * GUI*****************************************************
         */
        //neuronsLabel = new Label("Neurons: ", BaseGame.labelStyle);
        //neuronsLabel.setColor( Color.CYAN );
        allElemLabel = new Label("All Elements: ", BaseGame.labelStyle);

        fpsLabel = new Label("FPS: ", BaseGame.labelStyle );
        massage = new Label("", BaseGame.labelStyle);
        massage.setVisible(false);

        buttonsUI = new ButtonsUI();
        GUI();

    }

    /**
     * Создание рандомного обьекта
     */
    public void spawn () {
        //BaseActor3D obj = constructors.values[2 + MathUtils.random(constructors.size - 3)].construct(0,0,0,mainStage3D);
        BaseActor3D obj = constructors.get("cylinder").construct(0, 0,0,mainStage3D);
        obj.transform.setFromEulerAngles(MathUtils.random(360f), MathUtils.random(360f), MathUtils.random(360f));

        obj.setPosition(MathUtils.random(2.5f, 2.5f), 50f, MathUtils.random(2.5f, 2.5f));
        //obj.body.translate(obj.getPosition());

        obj.body.applyCentralImpulse(new Vector3(obj.getPosition().x,-obj.getPosition().y*50, obj.getPosition().z));

        obj.body.proceedToTransform(obj.transform);
        obj.body.setUserValue(mainStage3D.getNeuronList().size());
        //obj.body.setUserValue(mainStage3D.getNeuronList().size());
        obj.body.setCollisionFlags(obj.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

        //mainStage3D.addActor(obj);
        dynamicsWorld.addRigidBody(obj.body);

        obj.body.setContactCallbackFlag(OBJECT_FLAG);
        obj.body.setContactCallbackFilter(ALL_FLAG); //на кого реагировать при колизии

        //obj.act(1);
    }


    @Override
    public void update(float dt) {
        super.update(dt);
        //controllerVisionCortex.upd();
        controllerVision.upd();
        panorama.getActor3D().loadTexture("InputVision.jpg");


        /*
        if (Gdx.input.isKeyPressed(Input.Keys.W)){
            mainStage3D.getActors().get(1).moveForward(dt);
            //mainStage3D.getActors().get(1).act(dt);
            //mainStage3D.getNeuronList().get(1).getActor3D().body.setMassProps(1,new Vector3(0,0,0));
            //mainStage3D.getNeuronList().get(1).getActor3D().body.translate(mainStage3D.getNeuronList().get(1).getActor3D().getPosition());
        }*/

        /*
        if ((spawnTimer -= dt) < 0) {
            spawn();
            spawnTimer = 3.9f;
        }*/

        //int starfishCount = BaseActor3D.count(mainStage3D, "core.Objects.Starfish");
        //starfishLabel.setText( "Starfish left: " + starfishCount );
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + "  ");
        allElemLabel.setText("All Elements: " + mainStage3D.getNeuronList().size() + "  " +
                "VisibleElements: "+ mainStage3D.visibleCount+ " Synapse: "+ mainStage3D.synapseCount);
        //neuronsLabel.setText("Neurons: " + BaseActor3D.count(mainStage3D, "core.BulletObjects.Soma"));

        //if (starfishCount == 0)
            //messageLabel.setVisible(true);
    }



    @Override
    public void dispose(){
        super.dispose();

        for (BaseNeuron actor3D : mainStage3D.getNeuronList())
            actor3D.getActor3D().dispose();
        mainStage3D.getNeuronList().clear();

        for (BaseActor3D.Constructor ctor : constructors.values())
            ctor.dispose();
        constructors.clear();

        myContactListener.dispose();
    }


    public void GUI(){

        buttonsUI.exitButton(uiStage);
        buttonsUI.restartButton(uiStage);
        buttonsUI.drawButton(mainStage3D);
        buttonsUI.cameraButton(mainStage3D);
        buttonsUI.startCamButton(controllerVision);

        uiTable.pad(1); // отступ от края
        uiTable.add(fpsLabel);
        uiTable.add(allElemLabel);
        //uiTable.add(neuronsLabel);

        uiTable.add(buttonsUI.getStartCamButton());
        uiTable.add(buttonsUI.getDrawButton());
        uiTable.add(buttonsUI.getCamButton());
        uiTable.add(buttonsUI.getRestartButton());
        uiTable.add(buttonsUI.getExitButton());
        uiTable.row();
        uiTable.add(massage).expandY();
    }

    /**
     * модуль для резайза графического интерфейса << Пока не работает >>
     */
    public void guiUpd(){

        if (Gdx.graphics.getWidth() != screenWH.x && Gdx.graphics.getHeight() != screenWH.y){
            this.uiStage = new Stage();
            uiTable.clear();

            buttonsUI.exitButton(uiStage);
            buttonsUI.restartButton(uiStage);
            buttonsUI.drawButton(mainStage3D);

            uiTable.pad(1); // отступ от края
            uiTable.add(fpsLabel);
            uiTable.add(allElemLabel);
            //uiTable.add(neuronsLabel);

            uiTable.add(buttonsUI.getDrawButton());
            uiTable.add(buttonsUI.getRestartButton());
            uiTable.add(buttonsUI.getExitButton());
            uiTable.row();
            uiTable.add(massage).expandY();

            this.uiStage.addActor(uiTable);
            screenWH.set(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        }
    }
}