package core.Objects;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

public class ModelSphere implements Disposable{

    private Model d3Model;
    private ModelInstance model;
    private ModelBuilder modelBuilder;
    private Material material;

    public ModelSphere(){
        modelBuilder = new ModelBuilder();
        material = new Material();

        SelectParam(Color.LIGHT_GRAY, 0.07f);
    }

    public void SelectParam(Color color, float size){

        material.set(ColorAttribute.createDiffuse(color));
        d3Model = modelBuilder.createSphere(size, size, size, 5, 5, material ,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        model = new ModelInstance(d3Model);
    }

    public ModelInstance getModel() {
        return model;
    }

    public Model getD3Model() {
        return d3Model;
    }

    @Override
    public void dispose() {
        getD3Model().dispose();
    }
}
