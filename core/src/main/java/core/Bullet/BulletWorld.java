package core.Bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.physics.bullet.dynamics.*;
import core.AbstractClass.MyBulletInterface;
import core.Base.BaseActor3D;

public class BulletWorld implements MyBulletInterface {
    protected final static short GROUND_FLAG = 1 << 8;
    protected final static short OBJECT_FLAG = 1 << 9;
    protected final static short ALL_FLAG = -1;

    protected btCollisionConfiguration collisionConfig;
    protected btDispatcher dispatcher;
    //MyContactListener contactListener;
    protected btBroadphaseInterface broadphase;
    //protected btDynamicsWorld dynamicsWorld;
    protected btDiscreteDynamicsWorld dynamicsWorld;
    protected btConstraintSolver constraintSolver;

    protected BulletWorld(){

    }
    @Override
    public void init() {
        Bullet.init();

        collisionConfig = new btDefaultCollisionConfiguration();
        dispatcher = new btCollisionDispatcher(collisionConfig);
        broadphase = new btDbvtBroadphase();
        constraintSolver = new btSequentialImpulseConstraintSolver();
        dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, constraintSolver, collisionConfig);
        //dynamicsWorld.setGravity(new Vector3(0, -10f, 0));
        dynamicsWorld.setGravity(new Vector3(0f, 0f, 0f));
        //contactListener = new MyContactListener();

    }

    @Override
    public void update(float delta) {
        dynamicsWorld.stepSimulation(delta, 5, 1f / 60f);
    }

    @Override
    public void remove(btRigidBody body) {
        dynamicsWorld.removeRigidBody(body);
        ((BaseActor3D)body.userData).dispose();

    }

    @Override
    public btDiscreteDynamicsWorld getWorld() {
        return dynamicsWorld ;
    }

    @Override
    public void dispose() {
        dynamicsWorld.dispose();
        constraintSolver.dispose();
        broadphase.dispose();
        dispatcher.dispose();
        collisionConfig.dispose();

        //contactListener.dispose();
    }
}
