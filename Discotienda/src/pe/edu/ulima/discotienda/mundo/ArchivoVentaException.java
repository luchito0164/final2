package pe.edu.ulima.discotienda.mundo;

public class ArchivoVentaException extends Exception
{
    private int cancionesVendidas;

    public ArchivoVentaException( String causa, int ventas )
    {
        super( causa );
        cancionesVendidas = ventas;
    }

    public int darCancionesVendidas( )
    {
        return cancionesVendidas;
    }
}
