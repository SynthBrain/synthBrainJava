package core.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class Background {

    //private ObjLoaderModel objLoaderModel;
    private ModelPlane modelPlane;

    public Background(){

        modelPlane = new ModelPlane();
        modelPlane.SelectParam(Color.DARK_GRAY, 50);

        //objLoaderModel = new ObjLoaderModel("plane.obj");
        //objLoaderModel.model.transform.scl(10);
    }

    public void renderBackground(ModelBatch batch) {
        //batch.render(objLoaderModel.model);
        batch.render(modelPlane.getModel());
    }
}
