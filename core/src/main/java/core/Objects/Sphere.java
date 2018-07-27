package core.Objects;

import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import core.Base.BaseActor3D;
import core.Base.Stage3D;


//public class Sphere extends BaseActor3D {
public class Sphere {
    btDiscreteDynamicsWorld dynamicsWorld;

    Model m;
    String n;
    btRigidBody.btRigidBodyConstructionInfo c;

    public Sphere(float x, float y, float z, Stage3D s) {
        //super(x,y,z,s);
        this.dynamicsWorld = dynamicsWorld;

        ModelBuilder modelBuilder = new ModelBuilder();
        Material mat = new Material();

        int usageCode = Usage.Position + Usage.ColorPacked + Usage.Normal + Usage.TextureCoordinates;
        int radius = 1;
        //model = modelBuilder.createSphere(radius,radius,radius, 32,32, mat, usageCode);

        Vector3 pos = new Vector3(0,0,0);
        //setModelInstance( new ModelInstance(model, pos) );
        /*
        motionState.transform = modelData.transform;

        shape = new btSphereShape(radius / 2f);


        if (mass > 0f)
            shape.calculateLocalInertia(mass, localInertia);
        else
            localInertia.set(0, 0, 0);

        constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
        body = new btRigidBody(constructionInfo);
        body.setMotionState(motionState);

        body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
        this.dynamicsWorld.addRigidBody(body);

        body.setContactCallbackFlag(OBJECT_FLAG);
        body.setContactCallbackFilter(ALL_FLAG);*/


        //setBasePolygon();
    }

    public void act(float dt)
    {
        /*
        super.act(dt);
        if (!collision) return;
        //System.out.println("Collision name " + this.getClass().getName());*/
    }
}
