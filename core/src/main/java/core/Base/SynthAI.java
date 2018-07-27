package core.Base;

public class SynthAI extends BaseGame
{
    public void create() 
    {  
        super.create();
        setActiveScreen( new LevelScreen() );
    }
}
