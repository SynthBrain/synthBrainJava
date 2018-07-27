package core.LayerNeurons.CorticalColumn;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.LayerNeurons.*;

public class CorticalColumn {

    private LayerAssigneeNeuron layerAssigneeNeuron;
    private LayerUniNeuron layerUniNeuron;
    private LayerStellateNeuron layerStellateNeuron;


    public CorticalColumn(ArrayMap<String, BaseActor3D.Constructor> constructors,
                          short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){

        layerAssigneeNeuron = new LayerAssigneeNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld);
        layerUniNeuron = new LayerUniNeuron(constructors, flag, filter, mainStage3D, dynamicsWorld);
        layerStellateNeuron = new LayerStellateNeuron(constructors, flag, filter, mainStage3D, dynamicsWorld);
    }

    public void updateCorticalColumn(){
        layerAssigneeNeuron.updateColumn();
        layerUniNeuron.updateColumn();
        layerStellateNeuron.updateColumn();
    }

    public void updPositionColumn(int sizeX, int sizeZ) {
        layerAssigneeNeuron.updatePositionLayer(sizeX,sizeZ);
        layerUniNeuron.updatePositionLayer(sizeX,sizeZ);
        layerStellateNeuron.updatePositionColumn(sizeX,sizeZ);
    }

    public LayerAssigneeNeuron getLayerAssigneeNeuron() {
        return layerAssigneeNeuron;
    }

    public LayerUniNeuron getLayerUniNeuron() {
        return layerUniNeuron;
    }

    public LayerStellateNeuron getLayerStellateNeuron() {
        return layerStellateNeuron;
    }
}
