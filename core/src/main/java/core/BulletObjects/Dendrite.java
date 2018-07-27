package core.BulletObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.BulletObjects.BulletSphere;
import core.Objects.ModelConus;

public class Dendrite{

    private boolean active;
    private float neuroplasticity;

    private Vector3 synapseVector;
    private float charge;
    private float difference;


    public Dendrite(){
        active = false;
        neuroplasticity = 100;

        synapseVector = new Vector3(); // запоминаем направление откуда по отношению к себе, 1 из 26 векторов
        charge = 0; // сила заряда что пришла
        difference = 0; // разница с соседями
        // спициализация нейрона - чем чаще вектор и значение повторяються ,тем уменьшаеться диапазон на который реагируем
        //запоминаем разницу в поляризации с соседями, вектор откуда пришёл заряд и силу заряда

    }

    public float getDifference() {
        return difference;
    }

    public void setDifference(float difference) {
        this.difference = difference;
    }

    public float getCharge() {
        return charge;
    }

    public void setCharge(float charge) {
        this.charge = charge;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getNeuroplasticity() {
        return neuroplasticity;
    }

    public void setNeuroplasticity(float neuroplasticity) {
        this.neuroplasticity = neuroplasticity;
    }

    public Vector3 getSynapseVector() {
        return synapseVector;
    }

    public void setSynapseVector(Vector3 synapseVector) {
        this.synapseVector = synapseVector;
    }


}