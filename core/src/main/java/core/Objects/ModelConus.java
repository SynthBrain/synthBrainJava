package core.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

public class ModelConus implements Disposable {
    private Model d3Model;
    private ModelInstance model;
    private ModelBuilder modelBuilder;
    private Material material;


    public ModelConus(){
        modelBuilder = new ModelBuilder();
        material = new Material();

        SelectParam(Color.WHITE, 0.01f,0.015f );
    }

    public void SelectParam(Color color, float sizeW, float sizeH ){
        material.set(ColorAttribute.createDiffuse(color));
        d3Model = modelBuilder.createCone(sizeW, sizeH, sizeW, 3,
                material,
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
