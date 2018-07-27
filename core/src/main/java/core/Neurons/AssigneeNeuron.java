package core.Neurons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.BulletObjects.BaseNeuron;

import java.util.Comparator;


public class AssigneeNeuron extends BaseNeuron {


    public AssigneeNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                          short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld) {

        super(constructors, flag, filter, mainStage3D, dynamicsWorld);
        super.createBody("box");
    }

    @Override
    public boolean upd(){
        if (getTemp().size() < 1) return false;

        sortList.addAll(getTemp().values());
        sortList.sort(Float::compareTo);

        float maxCharge = sortList.get(sortList.size() - 1);
        //System.out.println(maxCharge);

        for (Vector3 vec : getTemp().keySet()){
            getVecCharge().put(vec, getTemp().get(vec)); // получаем специализацию самых сильных направлений
            this.charge = maxCharge;
            //System.out.println("New Assignee");
        }
        sortList.clear();

        if (getVecCharge().equals(getTemp())){
            getTemp().clear();
            return true;
        }

        getTemp().clear();
        return false;
    }

    /*
    @Override
    public void drawSynapse(ShapeRenderer shape, Color color){
        setSynapseCount(0);
        int i = 0;

        for (Vector3 vec : getDendrite()){
            shape.setColor(color);
            shape.line(actor3D.getPosition(), vec);
            i++;
            setSynapseCount(i);
            //System.out.println(vec);
        }


    }*/
}