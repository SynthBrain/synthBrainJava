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

public class BulletSphere{

    public BaseActor3D actor3D;

    public BulletSphere(ArrayMap<String, BaseActor3D.Constructor> constructors, short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        actor3D = constructors.get("sphere").construct(0,0,0,mainStage3D);
        actor3D.setColor(Color.CYAN);
        actor3D.body.setCollisionFlags(actor3D.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);

        //mainStage3D.addActor(actor3D);
        dynamicsWorld.addRigidBody(actor3D.body);

        actor3D.body.setContactCallbackFlag(flag);
        actor3D.body.setContactCallbackFilter(filter);
        actor3D.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }
}
