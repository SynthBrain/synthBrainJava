package core.Bullet;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import core.Base.Stage3D;

public class MyContactListener extends ContactListener {
    private Stage3D stage3D;
    public MyContactListener(Stage3D stage3D){
        this.stage3D = stage3D;
    }

    @Override
    public boolean onContactAdded (int userValue0, int partId0, int index0, boolean match0, int userValue1, int partId1,
                                   int index1, boolean match1) {
        if (match0){
            //((ColorAttribute)stage3D.getActors().get(userValue0).getModelData().materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.YELLOW);
            //stage3D.getNeuronList().get(userValue0).getActor3D().setColor(Color.GOLD);
            //stage3D.getNeuronList().get(userValue0-1).getActor3D().collisionVectorObj.set(stage3D.getActors().get(userValue1).getPosition());

            //stage3D.getActors().get(userValue0).collisionVectorObj.set(stage3D.getActors().get(userValue1).getPosition());

            //stage3D.getActors().get(userValue0).setPosition(stage3D.getActors().get(userValue0).getPosition());
            /*
            if (stage3D.getElementsList().get(3).soma.actor3D.body.getUserValue() == stage3D.getActors().get(userValue0)){
                stage3D.getElementsList().get(userValue0).updPositionNeuron(stage3D.getElementsList().get(userValue0).getSoma().getPositionSoma());
            }*/
        }
        if (match1){
            //((ColorAttribute)stage3D.getActors().get(userValue1).getModelData().materials.get(0).get(ColorAttribute.Diffuse)).color.set(Color.WHITE);
            //stage3D.getNeuronList().get(userValue1).getActor3D().setColor(Color.RED);
        }

        return true;
    }
}
