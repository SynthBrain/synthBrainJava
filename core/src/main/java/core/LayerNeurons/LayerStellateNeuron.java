package core.LayerNeurons;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.Neurons.AssociationNeuron;

public class LayerStellateNeuron {

    private ArrayMap<String, BaseActor3D.Constructor> constructors;
    private short flag,filter;
    private Stage3D mainStage3D;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private AssociationNeuron[][] associationNeurons;
    private Vector3 position;

    public LayerStellateNeuron(ArrayMap<String, BaseActor3D.Constructor> constructors,
                               short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        position = new Vector3();

        associationNeurons = new AssociationNeuron[3][3];//77

        this.constructors = constructors;
        this.flag = flag;
        this.filter = filter;
        this.mainStage3D = mainStage3D;
        this.dynamicsWorld = dynamicsWorld;
    }

    public void updatePositionColumn(int positionX, int positionZ){
        for (int i =0; i < associationNeurons.length; i++){
            for (int j =0; j < associationNeurons[i].length; j++){
                associationNeurons[i][j] = new AssociationNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld);

                position.x = MathUtils.random((float) 0.0, (float) 1); // background 15*15
                position.x += positionX;

                position.y = MathUtils.random((float) 2.25, (float) 4);

                position.z = MathUtils.random((float) 0.0, (float) 1);
                position.z += positionZ;

                associationNeurons[i][j].updPosition(position);
            }
        }
    }

    public void updateColumn(){
        for (int i =0; i < associationNeurons.length; i++){
            for (int j=0; j < associationNeurons[i].length;j++){
                //associationNeurons[i][j].upd();
            }
        }
    }
}
