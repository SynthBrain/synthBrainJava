package core.Neurons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.BulletObjects.BaseNeuron;

public class PatternNeuron extends BaseNeuron {

    public PatternNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                         short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld) {

        super(constructors, flag, filter, mainStage3D, dynamicsWorld);
        super.createBody("coneSoma");
    }


    @Override
    public void drawSynapse(ShapeRenderer shape, Color color){
        int i = 0;
        setSynapseCount(i);

        if (!getActivAverage().equals(new Vector3())){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), getActivAverage());
            i++;
            setSynapseCount(i);
        }

        // для визуализации связей, иначе неуспеваем увидеть
        try {
            Thread.sleep((long) 0.00000000000001);
            getActivAverage().set(0,0,0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //System.out.println(getDendrite().size());

        //System.out.println(getActivDendrite().size());

        /*
        for (Vector3 vec : getDendrite()){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), vec);
            i++;
            setSynapseCount(i);
            //System.out.println(vec);
        }*/
    }
}
