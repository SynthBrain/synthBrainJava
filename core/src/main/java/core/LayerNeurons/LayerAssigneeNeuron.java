package core.LayerNeurons;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.Neurons.AssigneeNeuron;

public class LayerAssigneeNeuron {

    private ArrayMap<String, BaseActor3D.Constructor> constructors;
    private short flag,filter;
    private Stage3D mainStage3D;
    private btDiscreteDynamicsWorld dynamicsWorld;


    AssigneeNeuron[][] assigneeNeurons;
    private Vector3 position;
    private int sizeLayer;


    public LayerAssigneeNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                               short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        sizeLayer = 1;
        position = new Vector3();
        assigneeNeurons = new AssigneeNeuron[sizeLayer][sizeLayer];

        this.constructors = constructors;
        this.flag = flag;
        this.filter = filter;
        this.mainStage3D = mainStage3D;
        this.dynamicsWorld = dynamicsWorld;
    }

    public void updatePositionLayer(int positionX, int positionZ){
        for (int i = 0; i < assigneeNeurons.length; i++){
            for (int j = 0; j < assigneeNeurons[i].length; j++){
                assigneeNeurons[i][j] = new AssigneeNeuron(this.constructors,flag,filter,mainStage3D,dynamicsWorld);

                position.x = (float) 0.5; // background 15*15
                position.x += positionX;

                position.y = -1f;

                position.z =(float) 0.5;
                position.z += positionZ;

                assigneeNeurons[i][j].updPosition(position);
            }
        }
    }

    public void updateColumn(){
        for (int i =0; i < assigneeNeurons.length; i++){
            for (int j=0; j < assigneeNeurons[i].length;j++){
                //assigneeNeurons[i][j].upd();
            }
        }
    }

    public AssigneeNeuron[][] getAssigneeNeurons() {
        return assigneeNeurons;
    }
}
