package core.Neurons;

import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.BulletObjects.BaseNeuron;
import core.Base.BaseActor3D;
import core.Base.Stage3D;


public class AssociationNeuron extends BaseNeuron {

    public AssociationNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                             short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld) {

        super(constructors, flag, filter, mainStage3D, dynamicsWorld);
        super.createBody("coneSoma");
    }

}
