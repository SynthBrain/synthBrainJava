package core.Buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import core.Base.LevelScreen;
import core.Base.Stage3D;
import core.Base.SynthAI;
import core.Controller.ControllerVision;
import core.Controller.ControllerVisionCortex;

public class ButtonsUI {
    // Сделать авто-рестарт интерфейса
    private Button exitButton;
    private Button restartButton;
    private Button drawButton;
    private Button camButton;
    private Button startCamButton;


    public void exitButton(Stage uiStage){
        ButtonStyle buttonStyle = new ButtonStyle();
        //exitButton = new TextButton( "Quit", BaseGame.textButtonStyle );

        //Skin skin = new Skin();
        //skin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin.atlas") ));

        //buttonStyle.up = skin.getDrawable("button-blue");
        //buttonStyle.down = skin.getDrawable("button-red");


        Texture buttonTex = new Texture( Gdx.files.internal("power18dp.png"));//("undo.png") );
        TextureRegion buttonRegion =  new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        exitButton = new Button( buttonStyle );
        //exitButton.setBounds(10,Gdx. graphics.getHeight()-170, 200,100);

        //exitButton.setColor( Color.CYAN );
        //exitButton.setPosition(10,Gdx. graphics.getHeight()-70);
        //uiStage.addActor(exitButton);


        /*
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //super.clicked(event, x, y);
                System.out.println("Press Button");
            }
        });*/


        exitButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(Type.touchDown) )
                        return false;

                    System.out.println("Push Exit Button");
                    Gdx.app.exit();
                    return true;
                }
        );
    }

    public void restartButton(Stage uiStage){
        ButtonStyle buttonStyle = new ButtonStyle();

        Texture buttonTex = new Texture( Gdx.files.internal("replay18dp.png") );
        //TextButton quitButton = new TextButton( "Quit", BaseGame.textButtonStyle );
        TextureRegion buttonRegion =  new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        restartButton = new Button( buttonStyle );
        //restartButton.setColor( Color.CYAN );
        restartButton.setPosition(10,exitButton.getY()-50);
        //uiStage.addActor(restartButton);


        restartButton.addListener(
                (Event e) ->
                {
                    InputEvent ie = (InputEvent)e;
                    if ( ie.getType().equals(Type.touchDown)){ // touchDown
                        System.out.println("Push Restart Button");
                        SynthAI.setActiveScreen( new LevelScreen() );
                    }
                    return false;
                }
        );
    }

    public void drawButton(Stage3D stage){
        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture( Gdx.files.internal("play18dp.png") );
        TextureRegion buttonRegion =  new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        ButtonStyle buttonStyleOff = new ButtonStyle();
        Texture buttonTexOff = new Texture( Gdx.files.internal("pause18dp.png") );
        TextureRegion buttonRegionOff =  new TextureRegion(buttonTexOff);
        buttonStyleOff.up = new TextureRegionDrawable( buttonRegionOff );

        drawButton = new Button( buttonStyle );

        drawButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(Type.touchDown) )
                        return false;

                    if (!stage.isDrawObj()){
                        System.out.println("Draw isActive");
                        drawButton.setStyle(buttonStyleOff);
                        stage.setDrawObj(true);
                        return  true;
                    }
                    System.out.println("Draw Not Active");
                    drawButton.setStyle(buttonStyle);
                    stage.setDrawObj(false);
                    return true;
                }
        );
    }

    public void cameraButton(Stage3D stage){
        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture( Gdx.files.internal("videocam18dp.png") );
        TextureRegion buttonRegion =  new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        camButton = new Button( buttonStyle );

        camButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(Type.touchDown) )
                        return false;

                    System.out.println("Push Camera Button");
                    if (stage.camera.position.x != 0 && stage.camera.position.y != 0 && stage.camera.position.z != 0){
                        stage.setCameraPosition(0,0,0);
                        stage.setCameraDirection(0,0,-500f);
                        return true;
                    }else {
                        stage.setCameraPosition(17,17,17);
                        stage.setCameraDirection(0,0,0);
                        return true;
                    }

                }
        );
    }

    public void startCamButton(ControllerVision controller){
        ButtonStyle buttonStyle = new ButtonStyle();
        Texture buttonTex = new Texture( Gdx.files.internal("vis18dp.png") );
        TextureRegion buttonRegion =  new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable( buttonRegion );

        startCamButton = new Button( buttonStyle );

        startCamButton.addListener(
                (Event e) ->
                {
                    if ( !(e instanceof InputEvent) )
                        return false;

                    if ( !((InputEvent)e).getType().equals(Type.touchDown) )
                        return false;

                    if (!controller.isActivVision()){
                        controller.setActivVision(true);
                        return  true;
                    }else {
                        System.out.println("Cam Not Active");
                        controller.setActivVision(false);
                        return true;
                    }
                }
        );
    }



    public Button getExitButton() {
        return exitButton;
    }

    public Button getRestartButton() {
        return restartButton;
    }

    public Button getDrawButton() {
        return drawButton;
    }

    public Button getCamButton() {
        return camButton;
    }

    public Button getStartCamButton() {
        return startCamButton;
    }
}
