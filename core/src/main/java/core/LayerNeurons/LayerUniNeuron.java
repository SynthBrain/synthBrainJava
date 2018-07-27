package core.LayerNeurons;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.Neurons.AssociationNeuron;

public class LayerUniNeuron {

    private ArrayMap<String, BaseActor3D.Constructor> constructors;
    private short flag,filter;
    private Stage3D mainStage3D;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private AssociationNeuron[][] uniNeuron;
    private Vector3 position;

    public LayerUniNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                          short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        position = new Vector3();

        uniNeuron = new AssociationNeuron[3][3];//44
        //updatePositionColumn();

        this.constructors = constructors;
        this.flag = flag;
        this.filter = filter;
        this.mainStage3D = mainStage3D;
        this.dynamicsWorld = dynamicsWorld;
    }

    public void updatePositionLayer0(int positionX, int positionZ){
        for (int i =0; i < uniNeuron.length; i++){
            for (int j =0; j < uniNeuron[i].length; j++){
                uniNeuron[i][j] = new AssociationNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld);

                position.x = MathUtils.random((float) 0.25, (float) 0.75); // background 15*15
                position.x += positionX;

                position.y = MathUtils.random((float) 0.25, (float) 2);

                position.z = MathUtils.random((float) 0.25, (float) 0.75);
                position.z += positionZ;

                uniNeuron[i][j].updPosition(position);
            }
        }
    }

    public void updatePositionLayer(int positionX, int positionZ){
        float temp = 0.25f;
        for (int i =0; i < uniNeuron.length; i++){
            for (int j =0; j < uniNeuron[i].length; j++){
                uniNeuron[i][j] = new AssociationNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld);

                position.x =  ((temp * (i+1) == 0) ? temp : temp * (i+1));
                position.x += positionX;

                position.y = 1;

                position.z =  ((temp * (j+1) == 0) ? temp : temp * (j+1));
                position.z += positionZ;

                uniNeuron[i][j].updPosition(position);
            }
        }
    }




    public void updateColumn(){
        for (int i =0; i < uniNeuron.length; i++){
            for (int j=0; j < uniNeuron[i].length;j++){
                //uniNeuron[i][j].upd();
            }
        }
    }

    /*
    public void renderColumn(ModelBatch batch){
        for (int i =0; i < uniNeuron.length; i++){
            for (int j=0; j < uniNeuron[i].length;j++){
                uniNeuron[i][j].renderNeuron(batch);
            }
        }
    }*/

    public AssociationNeuron[][] getUniNeuron() {
        return uniNeuron;
    }

    public void setUniNeuron(AssociationNeuron[][] uniNeuron) {
        this.uniNeuron = uniNeuron;
    }
}
