package core.Neurons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.BulletObjects.BaseNeuron;

public class StimulusNeuron extends BaseNeuron {

    public StimulusNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                          short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld) {

        super(constructors, flag, filter, mainStage3D, dynamicsWorld);
        super.createBody("coneSoma");
    }


    @Override
    public void drawSynapse(ShapeRenderer shape, Color color){

    }
}
