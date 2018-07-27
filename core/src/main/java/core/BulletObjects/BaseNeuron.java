package core.BulletObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class BaseNeuron {
    private ArrayMap<String, BaseActor3D.Constructor> constructors;
    private short flag, filter;
    protected Stage3D mainStage3D;
    private btDiscreteDynamicsWorld dynamicsWorld;
    protected BaseActor3D actor3D;
    //****************************************
    private int synapseCount;

    private HashMap<Vector3, Float> vecCharge; // специализация
    private HashMap<Vector3, Float> temp; //временный принимает что б сравнить со специализацией

    private ArrayList<Vector3> dendrite;
    private ArrayList<Vector3> axon;

    private float synapseWeight;
    protected boolean active;
    public float charge;
    private float stimulus;
    protected ArrayList<Float> sortList;

    private Vector3 activStimul;
    private Vector3 activAverage;


    public BaseNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                      short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        this.constructors = constructors;
        this.flag = flag;
        this.filter = filter;
        this.mainStage3D = mainStage3D;
        this.dynamicsWorld = dynamicsWorld;

        synapseCount = 0;

        synapseWeight = 0;
        charge = 0;
        stimulus = 0; // для раздражителей - хранит значение цвета

        activAverage = new Vector3();
        activStimul = new Vector3(); // хранит активный в момент времени вектор, в котором находиться цвет
        dendrite = new ArrayList<>(); // хранит все раздражители с которыми есть связь
        axon = new ArrayList<>(); // хранит все вектора представителей паттернов в которых участвует

        sortList = new ArrayList<>();
        vecCharge = new HashMap<>();
        temp = new HashMap<>();

        active = false;

        mainStage3D.addNeuron(this);
    }


    public void createBody(String id){
        actor3D = constructors.get(id).construct(0,0,0,mainStage3D);
        actor3D.setColor(Color.CYAN);

        actor3D.body.proceedToTransform(actor3D.transform);
        actor3D.body.setUserValue(mainStage3D.getNeuronList().size());
        //actor3D.body.setUserValue(mainStage3D.getNeuronList().size());
        actor3D.body.setCollisionFlags(actor3D.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

        //mainStage3D.addActor(actor3D);
        dynamicsWorld.addRigidBody(actor3D.body);

        actor3D.body.setContactCallbackFlag(flag);
        actor3D.body.setContactCallbackFilter(filter);
    }

    public void createBodyPanorama(String id){
        actor3D = constructors.get(id).construct(0,0,0,mainStage3D);

        actor3D.loadTexture("InputVision.jpg");
        actor3D.setPosition(0,0,-500f);
        actor3D.turnByX(90);
        actor3D.turnBy(90);

        actor3D.body.proceedToTransform(actor3D.transform);
        actor3D.body.setUserValue(mainStage3D.getNeuronList().size());
        actor3D.body.setCollisionFlags(actor3D.body.getCollisionFlags() |
                btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);

        //mainStage3D.addActor(actor3D);
        dynamicsWorld.addRigidBody(actor3D.body);

        actor3D.body.setContactCallbackFlag(flag);
        actor3D.body.setContactCallbackFilter(filter);
    }

    public void createBodyBack(String id){
        actor3D = constructors.get(id).construct(0,0,0,mainStage3D);
        actor3D.setColor(Color.WHITE);
        actor3D.loadTexture("water.jpg");
        actor3D.body.setCollisionFlags(actor3D.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);

        //mainStage3D.addActor(actor3D);
        dynamicsWorld.addRigidBody(actor3D.body);

        actor3D.body.setContactCallbackFlag(flag);
        actor3D.body.setContactCallbackFilter(filter);
        actor3D.body.setActivationState(Collision.DISABLE_DEACTIVATION);
    }

    public void updPosition(Vector3 position){
        actor3D.setPosition(position);
    }

    public boolean upd(){
        if (getTemp().size() < 1) return false;


        sortList.addAll(getTemp().values());
        sortList.sort(Float::compareTo);

        float maxCharge = sortList.get(sortList.size() - 1);
        //System.out.println(maxCharge);

        /*
        for (Vector3 vec : getTemp().keySet()){
            if (getTemp().get(vec) < maxCharge){ // запоминаем без погрешности, после добавить разброс
                getTemp().remove(vec);
            }
        }*/

        if (getVecCharge().isEmpty()){
            for (Vector3 vec : getTemp().keySet()){
                getVecCharge().put(vec, getTemp().get(vec)); // получаем специализацию самых сильных направлений
                this.charge = maxCharge;
                //System.out.println("Last Nn");
            }
        }
        sortList.clear();

        sortList.addAll(getTemp().values());
        if (charge != 0 && sortList.get(0) >= charge - ((charge / 100) * 10) && sortList.get(0) <= charge + ((charge / 100) * 10) ){
        //if (sortList.get(0) >= charge - 3 && sortList.get(0) <= charge + 3){
            getTemp().clear();
            sortList.clear();
            return true;
        }

        /*
        if (getVecCharge().equals(getTemp())) {
            getTemp().clear();
            return true;
        }*/

        sortList.clear();
        getTemp().clear();
        return false;
    }

    public boolean activeBn(){
        sortList.addAll(getTemp().values());
        if (charge != 0 && sortList.get(0) >= charge - ((charge / 100) * 10) && sortList.get(0) <= charge + ((charge / 100) * 10) ){
        //if (charge != 0 && sortList.get(0) >= charge -  3 && sortList.get(0) <= charge + 3 ){
            sortList.clear();
            return true;
        }

        sortList.clear();
        //if (getVecCharge().equals(getTemp()))return true;
        return false;
    }

    public Vector3 createVector(Vector3 vecTemp){
        Vector3 vecCharge = new Vector3();

        vecCharge.x = getActor3D().getPosition().x - vecTemp.x;
        vecCharge.y = getActor3D().getPosition().y - vecTemp.y;
        vecCharge.z = getActor3D().getPosition().z - vecTemp.z;

        return vecCharge;
    }


    public void drawSynapse(ShapeRenderer shape, Color color){
        synapseCount = 0;

        if (!getActivStimul().equals(new Vector3())){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), getActivStimul());
            synapseCount++;
        }

        if (!getActivAverage().equals(new Vector3())){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), getActivAverage());
            synapseCount++;
        }


        /*
        for (Vector3 vec : dendrite){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), vec);
            synapseCount++;
            //System.out.println(vec);
        }*/

        /*
        for (Vector3 vec : childrenVecNeurons){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), vec);
            synapseCount++;
        }*/

        /*
        for (Vector3 vec : temp.keySet()){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), vec);
            synapseCount++;
        }*/

        /*
        for (Vector3 vec : vecCharge.keySet()){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), vec);
            synapseCount++;
        }*/
    }

    public BaseActor3D getActor3D() {
        return actor3D;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSynapseCount() {
        return synapseCount;
    }

    public HashMap<Vector3, Float> getVecCharge() {
        return vecCharge;
    }


    public HashMap<Vector3, Float> getTemp() {
        return temp;
    }

    public float getStimulus() {
        return stimulus;
    }

    public void setStimulus(float stimulus) {
        this.stimulus = stimulus;
    }

    public Vector3 getActivStimul() {
        return activStimul;
    }

    public void setActivStimul(Vector3 activStimul) {
        this.activStimul = activStimul;
    }

    public ArrayList<Vector3> getDendrite() {
        return dendrite;
    }

    public void setDendrite(ArrayList<Vector3> dendrite) {
        this.dendrite = dendrite;
    }

    public ArrayList<Vector3> getAxon() {
        return axon;
    }

    public void setAxon(ArrayList<Vector3> axon) {
        this.axon = axon;
    }

    public void setSynapseCount(int synapseCount) {
        this.synapseCount = synapseCount;
    }

    public Vector3 getActivAverage() {
        return activAverage;
    }

    public void setActivAverage(Vector3 activAverage) {
        this.activAverage = activAverage;
    }
}
