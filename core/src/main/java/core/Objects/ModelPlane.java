package core.Objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

public class ModelPlane implements Disposable{
    // creating a ground model using box shape
     private ModelBuilder modelBuilder;
     private ModelInstance model;
     private Model d3Model;
     private Material material;


    public ModelPlane(){
        // creating a ground model using box shape
        modelBuilder = new ModelBuilder();
        material = new Material();

        SelectParam(Color.WHITE, 40);
    }

    public void SelectParam(Color color, int groundWidth){
        modelBuilder.begin();
        material.set(ColorAttribute.createDiffuse(color));
        MeshPartBuilder mpb = modelBuilder.part("parts", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked,
                material);
        //mpb.setColor(1f, 1f, 1f, 1f);
        mpb.box(0, 0, 0, groundWidth, 0, groundWidth);
        d3Model = modelBuilder.end();

        model = new ModelInstance(d3Model);
    }


    public ModelInstance getModel() {
        return model;
    }

    @Override
    public void dispose() {
        d3Model.dispose();
    }
}
