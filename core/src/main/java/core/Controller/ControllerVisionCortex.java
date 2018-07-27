package core.Controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.BulletObjects.Axon;
import core.BulletObjects.BaseNeuron;
import core.BulletObjects.Dendrite;
import core.LayerNeurons.CorticalColumn.CorticalColumn;
import core.Base.VisionCortex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class ControllerVisionCortex extends VisionCortex{

    private ArrayMap<String, BaseActor3D.Constructor> constructors;
    private short flag,filter;
    private Stage3D mainStage3D;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private int focusX;
    private int focusY;

    private int count;

    private CorticalColumn[][] corticalColumns;
    private HashMap<Vector3, BaseNeuron> myVectorKeyNeuron;
    //private TreeMap<Vector3, BaseNeuron> myVectorKeyNeuron;

    public ControllerVisionCortex(ArrayMap<String, BaseActor3D.Constructor> constructors,
                                  short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld){
        super();
        this.constructors = constructors;
        this.flag = flag;
        this.filter = filter;
        this.mainStage3D = mainStage3D;
        this.dynamicsWorld = dynamicsWorld;

        // размер площади vision for SynthBrain
        //corticalColumns = new CorticalColumn[width][height];
        corticalColumns = new CorticalColumn[20][20];
        fillColums();
        count = 0;

        myVectorKeyNeuron = new HashMap<>();
        //myVectorKeyNeuron = new TreeMap<>();

        /**
         * Стартовое положение фокуса - центр изображения
         */
        focusX = (width / 2) - (corticalColumns.length / 2);
        focusY = (height / 2) - (corticalColumns.length / 2);

        downloadVectorKeyMap();
    }

    public void downloadVectorKeyMap(){
        for (BaseNeuron bn :mainStage3D.getNeuronList()){
            //tempMap.put(bn.getActor3D().getPosition(), bn);
            myVectorKeyNeuron.put(bn.getActor3D().getPosition(), bn);
            //System.out.println(myVectorKeyNeuron.get(bn.getActor3D().getPosition()));
        }
        //myVectorKeyNeuron = new TreeMap<>(tempMap);
    }

    public void upd(){
        long startTime = System.currentTimeMillis();
        updAssignee();
        super.webCam();
        super.inputImage("InputVision.jpg");
        super.image_in_charge();
        long timeSpent = System.currentTimeMillis() - startTime;
        //System.out.println("программа выполнялась " + timeSpent + " миллисекунд");
    }


    public void updAssignee(){
        for (int i = 0; i < corticalColumns.length; i++){
            for (int j = 0; j < corticalColumns[i].length; j++){
                //corticalColumns[i][j].getLayerAssigneeNeuron().getAssigneeNeurons()[0][0].getDendriteList().get(0).setCharge(layer_0[i][j]);
                corticalColumns[i][j].getLayerAssigneeNeuron().getAssigneeNeurons()[0][0].setActive(true);
                if (corticalColumns[i][j].getLayerAssigneeNeuron().getAssigneeNeurons()[0][0].isActive()){
                    corticalColumns[i][j].getLayerAssigneeNeuron().getAssigneeNeurons()[0][0].getActor3D().setColor(layer_RGB[i + focusX][j + focusY]);
                    //updFirstLevel(corticalColumns[i][j], polarization(layer_0, i + focusX, j + focusY));
                    corticalColumns[i][j].updateCorticalColumn();
                    corticalColumns[i][j].getLayerAssigneeNeuron().getAssigneeNeurons()[0][0].setActive(false);
                }
            }
        }
    }

    public float[] polarization(float[][] imprint, int i, int j){
        float vector_0 = imprint[i - 1][j - 1];
        float vector_1 = imprint[i - 1][j];
        float vector_2 = imprint[i - 1][j + 1];

        float vector_3 = imprint[i][j - 1];
        float vector_4 = imprint[i][j];
        float vector_5 = imprint[i][j + 1];

        float vector_6 = imprint[i + 1][j - 1];
        float vector_7 = imprint[i + 1][j];
        float vector_8 = imprint[i + 1][j + 1];

        float[] memCell = {
                vector_0,
                vector_1,
                vector_2,
                vector_3,
                vector_4,
                vector_5,
                vector_6,
                vector_7,
                vector_8};

        return memCell;
    }



    public void fillColums(){
        for (int i = 0; i < corticalColumns.length; i++){
            for (int j = 0; j < corticalColumns[i].length; j++){
                corticalColumns[i][j] = new CorticalColumn(this.constructors,flag,filter,mainStage3D,dynamicsWorld);
                corticalColumns[i][j].updPositionColumn(i,j);
            }
        }
        //updPosition();
    }

    public void updPosition(){
        for (int i = 0; i < corticalColumns.length; i++){
            for (int j = 0; j < corticalColumns[i].length; j++){
                corticalColumns[i][j].updPositionColumn(i,j);
            }
        }

    }

    public int getFocusX() {
        return focusX;
    }

    public void setFocusX(int focusX) {
        this.focusX = focusX;
    }

    public int getFocusY() {
        return focusY;
    }

    public void setFocusY(int focusY) {
        this.focusY = focusY;
    }
}
