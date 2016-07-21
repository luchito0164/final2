package pe.edu.ulima.discotienda.mundo;

public class ElementoExisteException extends Exception
{
    public ElementoExisteException( String nomElem )
    {
        super( nomElem );
    }
}
