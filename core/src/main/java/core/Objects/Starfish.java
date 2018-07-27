package core.Objects;


import core.Base.Stage3D;
import core.Objects.ObjModel;

public class Starfish extends ObjModel
{
    public Starfish(float x, float y, float z, Stage3D s)
    {
        super(x,y,z,s);
        loadObjModel("star.obj");
        //setScale(3,1,3);
        //setBasePolygon();
    }
    
    public void act(float dt)
    {
        //super.act(dt);
        //turnBy( 90 * dt );
    }
}