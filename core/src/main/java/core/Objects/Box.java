package core.Objects;

import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import core.Base.BaseActor3D;
import core.Base.Stage3D;

//public class Box extends BaseActor3D
public class Box
{
    public Box(float x, float y, float z, Stage3D s)
    {
        //super(x,y,z,s);
        ModelBuilder modelBuilder = new ModelBuilder();
        Material boxMaterial = new Material();
        
        int usageCode = Usage.Position + Usage.ColorPacked + Usage.Normal + Usage.TextureCoordinates;

        Model boxModel = modelBuilder.createBox(500, 0.1f, 500, boxMaterial, usageCode);

        Vector3 position = new Vector3(0,0,0);
        //setModelInstance( new ModelInstance(boxModel, position) );
    }

    public void act(float dt)
    {
       // super.act(dt);
       // if (!collision) return;
        //System.out.println("Collision name " + this.getClass().getName());
    }
}