package core.BulletObjects;

import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;

public class Panorama extends BaseNeuron {

    public Panorama(ArrayMap<String, BaseActor3D.Constructor> constructors,
                    short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        super(constructors, flag, filter, mainStage3D, dynamicsWorld);
        super.createBodyPanorama("panorama");
    }
}
