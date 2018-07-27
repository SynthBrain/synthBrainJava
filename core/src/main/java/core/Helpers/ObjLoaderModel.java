package core.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;

public class ObjLoaderModel {

    public ModelInstance model;
    private ObjLoader objLoader;

    public ObjLoaderModel(String path){
        objLoader = new ObjLoader();
        model = new ModelInstance(objLoader.loadModel(Gdx.files.internal(path)));
    }
}
