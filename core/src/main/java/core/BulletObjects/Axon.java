package core.BulletObjects;

import com.badlogic.gdx.math.Vector3;


public class Axon{

    private float stamina;
    private Vector3 synapseVector;
    private float chargeW;

    public Axon(){
        chargeW = 0; // специализация 
        synapseVector = new Vector3(); //запоминаем именно кому передаём,
        stamina = 100; // выносливость данного направления
        // формирование направления -- аксон должен иметь зависимость от ближайших нейроколонок
    }

    public Vector3 getSynapseVector() {
        return synapseVector;
    }

    public void setSynapseVector(Vector3 synapseVector) {
        this.synapseVector = synapseVector;
    }

    public float getStamina() {
        return stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public float getChargeW() {
        return chargeW;
    }

    public void setChargeW(float chargeW) {
        this.chargeW = chargeW;
    }
}
