package core.Objects;

import core.Base.Stage3D;
import core.Objects.ObjModel;

public class Turtle extends ObjModel
{
    public Turtle(float x, float y, float z, Stage3D s)
    {
        super(x,y,z,s);
        loadObjModel("turtle.obj");
        //setBasePolygon();
    }
}