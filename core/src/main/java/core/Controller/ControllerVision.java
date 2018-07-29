package core.Controller;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.utils.ArrayMap;
import core.Base.BaseActor3D;
import core.Base.Stage3D;
import core.Base.VisionCortex;
import core.BulletObjects.BaseNeuron;
import core.Neurons.AssigneeNeuron;
import core.Neurons.AssociationNeuron;
import core.Neurons.PatternNeuron;
import core.Neurons.StimulusNeuron;

import java.util.ArrayList;
import java.util.HashMap;

public class ControllerVision extends VisionCortex {

    private ArrayMap<String, BaseActor3D.Constructor> constructors;
    private short flag, filter;
    private Stage3D mainStage3D;
    private btDiscreteDynamicsWorld dynamicsWorld;

    private int focusX;
    private int focusY;
    private int count;
    private int sizeFocus;
    private boolean activVision;

    private ArrayList<Vector3> activityList; // будет хранить активные елементы

    private AssigneeNeuron[][] layerAssign; // слой предствителей
    private AssociationNeuron[][] layerVision;
    private HashMap<Vector3, BaseNeuron> layerKeyVision;

    private HashMap<Vector3, BaseNeuron> cloudOfStimulus;   // облако представителей стимулов
    private HashMap<Float, Vector3> reverseCloudOfStimulus; // по значению получить вектор в облаке
    private HashMap<Vector3, BaseNeuron> layer1; // слой первичных паттернов
    private HashMap<Vector3, BaseNeuron> layer2;
    //private HashMap<Vector3, BaseNeuron> layer3;

    private Vector3 tempVector;

    //private HashMap<Vector3, BaseNeuron> myVectorKeyNeuron; // Список всех нейронов в виде ключ-значение

    public ControllerVision(ArrayMap<String, BaseActor3D.Constructor> constructors,
                            short flag, short filter, Stage3D mainStage3D, btDiscreteDynamicsWorld dynamicsWorld) {
        super();
        this.constructors = constructors;
        this.flag = flag;
        this.filter = filter;
        this.mainStage3D = mainStage3D;
        this.dynamicsWorld = dynamicsWorld;

        activityList = new ArrayList<>();
        tempVector = new Vector3();

        count = 0;
        sizeFocus = 20;
        activVision = false;

        // размер площади vision for SynthBrain
        layerAssign = new AssigneeNeuron[sizeFocus][sizeFocus];
        layerVision = new AssociationNeuron[sizeFocus][sizeFocus];
        layerKeyVision = new HashMap<>();

        cloudOfStimulus = new HashMap<>();
        reverseCloudOfStimulus = new HashMap<>();
        layer1 = new HashMap<>();
        layer2 = new HashMap<>();

        createLayerAssign();
        updPositionAssign();

        createNeuronStimulusAtOnce(cloudOfStimulus, 0, 20, 0, 5);
        createSynapseForStimulus();


        //layer3 = new HashMap<>();

        //myVectorKeyNeuron = new HashMap<>();
        //myVectorKeyNeuron = new TreeMap<>();

        /**
         * Стартовое положение фокуса - центр изображения
         */
        focusX = (width / 2) - (layerAssign.length / 2);
        focusY = (height / 2) - (layerAssign.length / 2);

        //downloadVectorKeyMap();
    }

    private void logic(){
        setColorAssignee(); // представители меняют цвет в цвет пикселя -- украшательство
        setActivStimulInLayer1(); // присвоили каждому флаг активности

        /**
         * После того как выставили флаги активности и записали в списки паттернов, запускаем метод проверки и создания паттерна
         */
        createPattern();
        createPatternLevel2();


    }

    public void createPattern(){
        for (Vector3 vec : cloudOfStimulus.keySet()){
            /**
             * Готовый лист паттерна, в конце его нужно очистить
             * если лист больше определенного размера то выполняем для него логику
             */
            if (cloudOfStimulus.get(vec).getAxon().size() >= 3){
                /**
                 * Получили среднюю для паттерна
                 */
                Vector3 average = new Vector3();
                average.set(createTotalAverage(cloudOfStimulus.get(vec).getAxon()));
                /**
                 * Проверяем на уровне 1 - есть ли в этих координатах уже кто то, если да то сравниваем
                 * совпало активируем того кто уже там есть
                 * не совпало то создаём тело, заносим в его дендриты список векторов,
                 * а тем векторам заносим в аксон координаты этого тела и активируем его
                 */

                if (layer1.get(average) == null){
                    createBodyPattern(average, layer1, cloudOfStimulus.get(vec).getAxon());
                    for (Vector3 vector : cloudOfStimulus.get(vec).getAxon()){
                        layerKeyVision.get(vector).getAxon().add(vec);
                    }

                    for (Vector3 vector : cloudOfStimulus.get(vec).getAxon() ){
                        layerKeyVision.get(vector).setActivAverage(average);
                    }

                    /**
                     * Тут бы нам сравнивать не на 100% совпадение, а приблизительно
                      */
                }else if (layer1.get(average).getDendrite().equals(cloudOfStimulus.get(vec).getAxon())){
                    for (Vector3 vector : cloudOfStimulus.get(vec).getAxon() ){
                        layerKeyVision.get(vector).setActivAverage(average);
                    }
                }
                /**
                 * Ложить координаты представителей паттернов в список и создавать для них общий паттерн
                 * для следующего слоя
                 */
                activityList.add(average);
            }
            cloudOfStimulus.get(vec).getAxon().clear();
        }
    }

    /**
     * группа что сформировалась под одной интенсивностью (группа цельная без разрывов),
     * столконеться с ближайшими и будет взаимное торможение
     * группы сформируют между собой связь, сначала ближайшие
     * потом удалённые, но эта связь будет содержать в себе то влияние
     * что произвели друг на друга группы
     *
     * а что если запоминать не представителей группы, а представителей влияния этих групп
     * друг на друга
     *
     * если предположить что любая входящая информация оставляет в мозгу просто шрамы (рубцевание), слепок входящей инфы
     * чем выше констраст между группами тем больше рубцевание, сильнее запоминание
     * слабые шрамы рассасываються со временем, в памяти остаються только сильные (абстрактные образы)
     *
     * сделать кратковременную память и долговременную
     * в кратковременной рубцевание образуеться мгновенно и исчезает через короткий промежуток
     * в долговременной рубцевание образуеться по тому же клище но после большого количетсва повторений, исчезает через очень длительный период неактивности
     * (к примеру раз в 24 часа запускать цикл который проверит все даты рубцов и удалит те что превысили срок неактивности)
     *
     * в долговременной создаёться рубцевание, так как обучение идёт медленно будут создаваться достаточно абстрактные представители образов,
     * как и очень точные но находиться в пространстве должны рядом (похожее рядом)
     *
     */

    private void createPatternLevel2(){
        Vector3 average = new Vector3(createTotalAverage(activityList));

        if (layer2.get(average) == null){
            createBodyPattern(average, layer2, activityList);
        }else if (layer2.get(average).getDendrite().equals(activityList)){
            count++;
            System.out.println("Фокус опознан "+ count);
        }


        // для визуализации связей
        for (Vector3 vec : activityList){
            layer1.get(vec).getActivAverage().set(average);
        }
        activityList.clear();
    }







    //******************************************************************
    public void upd(){
        long startTime = System.currentTimeMillis();
        logic();
        if (activVision){
            super.webCam(); // Работа камеры вкл/выкл
        }
        super.inputImage("InputVision.jpg");
        super.image_in_charge();
        long timeSpent = System.currentTimeMillis() - startTime;
        //System.out.println("программа выполнялась " + timeSpent + " миллисекунд");
    }



    /**
     *Придумать как их выставлять, не на плоскости ,а пространственно ,от характеристик что несёт в себе паттерн
     * что б схожие формировались рядом с друг другом
     *
     * как вариант сделать , чем кучнее координаты без разрыва интенсивности тем больший приоритет у них
     * и средняя ближе к ним
     */
    private Vector3 createTotalAverage(ArrayList<Vector3> list){
        Vector3 average = new Vector3();
        for (Vector3 vec : list){
            average.x += vec.x;
            average.y += vec.y;
            average.z += vec.z;
        }
        average.x /= list.size();
        average.y /= list.size();
        average.z /= list.size();

        average.y += 3;
        //average.y += MathUtils.random(average.y + 1.5f, 3f);

        return average;
    }

    private void setActivStimulInLayer1(){
        for (int i = 0; i < layerVision.length; i++){
            for (int j = 0; j < layerVision[i].length; j++){
                float tempStimul = layer_0[i + focusX][j + focusY];
                layerVision[i][j].setActivStimul(reverseCloudOfStimulus.get(tempStimul)); // активный стимул в момент времени
                layerAssign[i][j].setActivStimul(reverseCloudOfStimulus.get(tempStimul)); // активный стимул в момент времени

                // незабыть очищать этот паттерн
                /**
                 * Тут мы ложим елементы с одинаковой активностью в список
                 * так формируються группа по интенсивности,
                 * минус того что мы не учитываем близость растояния между ними
                 * думаю нужно складывать элементы пока между ними нет разрывов
                 *
                 * так же принебрегать очень маленькой разницей в интенсивности
                 * к примеру разницу в 5 считать одной группой
                 *
                 */

                //ДЕЛАТЬ ПРОВЕРКУ, ЕСТЬ ЛИ РЯДОМ ЕЩЕ ХОТЯ БЫ ПАРУ ТАКИХ ЖЕ ПО ИНТЕНСИВНОСТИ
                //                 * ТАК СКАЖЕМ ГРУППА СОСЕДЕЙ, тогда заносим в паттерн
                if (similarStimul(layer_0, i + focusX, j + focusY)){
                    cloudOfStimulus.get(reverseCloudOfStimulus.get(tempStimul)).getAxon().add(layerVision[i][j].getActor3D().getPosition());
                }

                //System.out.println(layerVision[i][j].getActivStimul());
            }
        }

    }

    // проверка на схожих соседей
    private boolean similarStimul(float[][] imprint, int i, int j){
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

        int result = 0;
        for (int k = 0; k < memCell.length; k++){
            if (memCell[4] == memCell[k]){
                result++;
            }
        }

        // чем больше будет цифра результата, тем большие пятна будут добавляться в паттерн
        if (result >= 3)return true;

        return false;
    }

    // запускаем для создания нейрона-представителя паттерна
    private void createBodyPattern(Vector3 vec, HashMap<Vector3, BaseNeuron> layer, ArrayList<Vector3> synapse){
        layer.put(vec, new PatternNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld));
        layer.get(vec).getActor3D().setPosition(vec);

        layer.get(vec).getDendrite().addAll(synapse);
    }

    // запускаем каждый калр
    private void setColorAssignee(){
        for (int i = 0; i < layerAssign.length; i++){
            for (int j = 0; j < layerAssign[i].length; j++){
                layerAssign[i][j].getActor3D().setColor(layer_RGB[i + focusX][j + focusY]);// тут просто для красоты цвет нейрона в цвет пикселя
            }
        }
    }

    // запускаем один раз
    private void createSynapseForStimulus(){
        for (int i = 0; i < layerVision.length; i++){
            for (int j = 0; j < layerVision[i].length; j++){
                layerVision[i][j].getDendrite().addAll(cloudOfStimulus.keySet());
                //System.out.println(layerVision[i][j].getStimulusVecNeurons().size());
            }
        }
    }

    // запускаем один ращ
    private void createNeuronStimulusAtOnce(HashMap<Vector3, BaseNeuron> layer, float min, float max, float minY, float maxY){
        Vector3 position;

        for (int i = 0; i <= 100; i ++){
            position = new Vector3();

            position.x =  MathUtils.random(min, max);
            position.y = MathUtils.random(minY, maxY);
            position.z =  MathUtils.random(min, max);

            layer.put(position, new StimulusNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld));// добавляем в список
            layer.get(position).setStimulus(i);
            reverseCloudOfStimulus.put((float)i, position);
            layer.get(position).getActor3D().setPosition(position);  // присваиваем координаты для данного нейрона, и выставляем его для визуализации
            //System.out.println(position);
            //System.out.println();
        }
    }

    // запускаем один ращ
    private void createLayerAssign(){
        for (int i = 0; i < layerAssign.length; i++){
            for (int j = 0; j < layerAssign[i].length; j++){
                layerAssign[i][j] = new AssigneeNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld);
                layerVision[i][j] = new AssociationNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld);
            }
        }
    }

    // запускаем один ращ
    private void updPositionAssign(){
        Vector3 position = new Vector3();
        Vector3 posTemp;
        for (int i = 0; i < layerAssign.length; i++){
            for (int j = 0; j < layerAssign[i].length; j++){
                position.x = (float) 0.5;
                position.x += i;//positionX;

                position.y = -3f;

                position.z =(float) 0.5;
                position.z += j; //positionZ;

                layerAssign[i][j].updPosition(position);

                position.y = 7;
                layerVision[i][j].updPosition(position);

                posTemp = new Vector3(position);
                layerKeyVision.put(posTemp, layerVision[i][j]);

            }
        }
    }

    public boolean isActivVision() {
        return activVision;
    }

    public void setActivVision(boolean activVision) {
        this.activVision = activVision;
    }
}



/*

    private void updAssignee(){
        for (int i = 0; i < layerAssign.length; i++){
            for (int j = 0; j < layerAssign[i].length; j++){

                tempVector = layerAssign[i][j].getActor3D().getPosition();
                layerAssign[i][j].getTemp().put(tempVector, layer_0[i + focusX][j + focusY]);

                if (layerAssign[i][j].upd()){ //активный или нет
                    //layerAssign[i][j].charge = layer_0[i + focusX][j + focusY];
                    activityList.add(layerAssign[i][j]); // попадаем в список активных слоя
                    layerAssign[i][j].getActor3D().setColor(layer_RGB[i + focusX][j + focusY]);// тут просто для красоты цвет нейрона в цвет пикселя
                }
            }
        }
        updActivityList();
    }

    private void updActivityList(){
        //System.out.println(activityList.size());
        for (BaseNeuron bn : activityList) {
            if (bn.getChildrenVecNeurons().size() > 0){
                for(Vector3 vec : bn.getChildrenVecNeurons()){ // раздаём заряд всем своим детям

                    layer1.get(vec).getTemp().put(bn.getActor3D().getPosition(), bn.charge);
                    if (layer1.get(vec).activeBn()){
                        count++;
                    }
                    //System.out.println(bn.charge);
                    //System.out.println(0);
                }
            }
            if (count == 0){
                createNeuron(bn, layer1, -0.25f, 0.25f, 0.25f, 2f);
            }
            count = 0;
        }

        activityList.clear();
    }

    private void updFirstLayer(){
        for (BaseNeuron bn : layer1.values()){
            if (bn.upd()) activityList.add(bn);
        }
        System.out.println(activityList.size());


        for (BaseNeuron bn : activityList) {
            bn.getActor3D().setColor(Color.WHITE);
            if (bn.getChildrenVecNeurons().size() > 0){
                for(Vector3 vec : bn.getChildrenVecNeurons()){ // раздаём заряд всем своим детям
                    layer2.get(vec).getTemp().put(bn.getActor3D().getPosition(), bn.charge);
                    //System.out.println(bn.charge);
                    //System.out.println(1);
                }


            }else {
                //createNeuron(bn, layer2, -0.50f, 0.50f, 3.25f, 5);
            }
            //bn.getActor3D().setColor(Color.BLACK);
            count = 0;
        }

        activityList.clear();
    }

    private void updSecondLayer() {
        for (BaseNeuron bn : layer2.values()){
            if (bn.upd()) activityList.add(bn);
        }
        //System.out.println(activityList.size());


        for (BaseNeuron bn : activityList) {
            if (bn.getChildrenVecNeurons().size() > 0){
                for(Vector3 vec : bn.getChildrenVecNeurons()){ // раздаём заряд всем своим детям
                    layer3.get(vec).getTemp().put(bn.getActor3D().getPosition(), bn.charge);
                    //myVectorKeyNeuron.get(vec).getTemp().put(bn.getActor3D().getPosition(), bn.charge);
                    //System.out.println(2);
                }
            }
            bn.getActor3D().setColor(Color.BLACK);
        }
        activityList.clear();
    }

    //*************************************************************************************************************


    private Vector3 vecDirectionCos(BaseNeuron bn){
        float module = (float) (Math.pow(bn.getActor3D().getPosition().x, 2) +
                Math.pow(bn.getActor3D().getPosition().y, 2) + Math.pow(bn.getActor3D().getPosition().z, 2));
        module = (float) Math.sqrt(module);

        Vector3 vectorDirectionCosines = new Vector3(bn.getActor3D().getPosition().x / module,
                bn.getActor3D().getPosition().y / module, bn.getActor3D().getPosition().z / module);

        return vectorDirectionCosines;
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

    private void downloadVectorKeyMap(){
        for (BaseNeuron bn :mainStage3D.getNeuronList()){
            //myVectorKeyNeuron.put(bn.getActor3D().getPosition(), bn);
        }
    }





    public void createNeuron(BaseNeuron bn, HashMap<Vector3, BaseNeuron> layer, float min, float max, float minY, float maxY){
        Vector3 position = new Vector3(bn.getActor3D().getPosition());

        position.x +=  MathUtils.random(min, max);
        position.y = MathUtils.random(minY, maxY);
        position.z +=  MathUtils.random(min, max);

        bn.getChildrenVecNeurons().add(position); // добавляем в список своих детей
        layer.put(position, new AssociationNeuron(constructors,flag,filter,mainStage3D,dynamicsWorld));// добавляем в список
        layer.get(position).getActor3D().setPosition(position);  // присваиваем координаты для данного нейрона, и выставляем его для визуализации

        layer.get(position).getTemp().put(bn.getActor3D().getPosition(), bn.charge); //поставляем временный набор параметров

        //myVectorKeyNeuron.put(layer.get(position).getActor3D().getPosition(), layer.get(position)); // добавляем в общий список ключей-значений
    }
}
*/