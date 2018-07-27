package core.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.UBJsonReader;

public class G3LoaderModel {

    //private JsonReader jsonReader; // for .g3dj files
    private UBJsonReader jsonReader; // for .g3db files
    private G3dModelLoader modelLoader;
    public ModelInstance model;

    public G3LoaderModel(String path){
        jsonReader = new UBJsonReader();
        modelLoader = new G3dModelLoader(jsonReader);
        model = new ModelInstance(modelLoader.loadModel(Gdx.files.internal(path)));
    }
}
