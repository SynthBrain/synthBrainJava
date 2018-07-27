package core.BulletObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;

public class Background extends BaseNeuron{
    public BaseActor3D actor3D;

    public Background(ArrayMap<String, BaseActor3D.Constructor> constructors,short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        super(constructors, flag, filter, mainStage3D, dynamicsWorld);
        super.createBodyBack("ground");
    }
}
