package pe.edu.ulima.discotienda.mundo;

import java.io.*;
import java.text.*;
import java.util.*;

public class Discotienda
{
    private static final String LOG_FILE = "./data/error.log";

    private ArrayList discos;

    private String archivoDiscotienda;

    public Discotienda( String nombreArchivoDiscotienda ) throws PersistenciaException
    {
        archivoDiscotienda = nombreArchivoDiscotienda;
        File archivo = new File( archivoDiscotienda );
        if( archivo.exists( ) )
        {
            try
            {
                ObjectInputStream ois = new ObjectInputStream( new FileInputStream( archivo ) );
                discos = ( ArrayList )ois.readObject( );
                ois.close( );
            }
            catch( Exception e )
            {
                registrarError( e );
                throw new PersistenciaException( "Error fatal: imposible restaurar el estado del programa (" + e.getMessage( ) + ")" );
            }
        }
        else
        {
            discos = new ArrayList( );
        }
        verificarInvariante( );
    }
    public Disco darDisco( String nombreDisco )
    {
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco d = ( Disco )discos.get( i );
            if( d.equals( nombreDisco ) )
                return d;
        }
        return null;
    }

    private Disco darDisco( String nombreDisco, String nombreArtista, String nombreCancion )
    {
        Disco discoBuscado = darDisco( nombreDisco );
        if( discoBuscado != null && discoBuscado.darArtista( ).equalsIgnoreCase( nombreArtista ) )
            return ( discoBuscado.darCancion( nombreCancion ) != null ) ? discoBuscado : null;
        else
            return null;
    }

    public void agregarDisco( String nombreDiscoD, String artistaD, String generoD, String imagenD ) throws ElementoExisteException
    {
        if( darDisco( nombreDiscoD ) != null )
            throw new ElementoExisteException( "El disco " + nombreDiscoD + " ya existe en miDiscoTienda" );

        discos.add( new Disco( nombreDiscoD, artistaD, generoD, imagenD ) );
        verificarInvariante( );
    }

    public void agregarCancionADisco( String nombreDisco, String nombreC, int minutosC, int segundosC, double precioC, double tamanoC, int calidadC ) throws ElementoExisteException
    {
        Disco d = darDisco( nombreDisco );
        d.agregarCancion( new Cancion( nombreC, minutosC, segundosC, precioC, tamanoC, calidadC, 0 ) );
        verificarInvariante( );
    }

    public String venderCancion( Disco disco, Cancion cancion, String email, String rutaFactura ) throws IOException
    {
        cancion.vender( );

        int posArroba1 = email.indexOf( "@" );
        String login = email.substring( 0, posArroba1 );
        String strTiempo = Long.toString( System.currentTimeMillis( ) );
        String nombreArchivo = login + "_" + strTiempo + ".fac";

        File directorioFacturas = new File( rutaFactura );
        if( !directorioFacturas.exists( ) )
            directorioFacturas.mkdirs( );
        File archivoFactura = new File( directorioFacturas, nombreArchivo );
        PrintWriter out = new PrintWriter( archivoFactura );

        Date fecha = new Date( );
        out.println( "miDiscoTienda - FACTURA" );
        out.println( "Fecha:            " + fecha.toString( ) );
        out.println( "Email:            " + email );

        out.println( "Canción:          " + cancion.darNombre( ) + " - " + disco.darArtista( ) );
        out.println( "                  " + disco.darNombreDisco( ) );
        out.println( "No de Canciones:  1" );
        DecimalFormat df = new DecimalFormat( "$0.00" );
        out.println( "Valor Total:      " + df.format( cancion.darPrecio( ) ) );
        out.close( );

        return nombreArchivo;
    }

    public ArrayList darDiscos( )
    {
        ArrayList nombresDiscos = new ArrayList( );
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco d = ( Disco )discos.get( i );
            nombresDiscos.add( d.darNombreDisco( ) );
        }
        return nombresDiscos;
    }

    public String venderListaCanciones( File archivoPedido, String rutaFactura ) throws FileNotFoundException, IOException, ArchivoVentaException
    {
        BufferedReader lector = new BufferedReader( new FileReader( archivoPedido ) );

        String email = null;
        try
        {
            email = lector.readLine( );
        }
        catch( IOException e )
        {
            throw new ArchivoVentaException( e.getMessage( ), 0 );
        }

        // Hace las verificaciones iniciales.
        if( email == null )
            throw new ArchivoVentaException( "El archivo está vacío", 0 );
        if( !validarEmail( email ) )
            throw new ArchivoVentaException( "El email indicado no es válido", 0 );

        String pedido = null;
        try
        {
            pedido = lector.readLine( );
        }
        catch( IOException e )
        {
            throw new ArchivoVentaException( e.getMessage( ), 0 );
        }

        if( pedido == null )
            throw new ArchivoVentaException( "Debe haber por lo menos una canci�n en el pedido", 0 );

        ArrayList discosFactura = new ArrayList( );
        ArrayList cancionesFactura = new ArrayList( );
        ArrayList cancionesNoEncontradas = new ArrayList( );
        int cancionesVendidas = 0;

        while( pedido != null )
        {
            int p1 = pedido.indexOf( "#" );
            if( p1 != -1 )
            {
                String resto1 = pedido.substring( p1 + 1 );
                int p2 = resto1.indexOf( "#" );
                if( p2 != -1 )
                {
                    String nombreDisco = pedido.substring( 0, p1 );
                    String nombreArtista = resto1.substring( 0, p2 );
                    String nombreCancion = resto1.substring( p2 + 1 );

                    Disco discoBuscado = darDisco( nombreDisco, nombreArtista, nombreCancion );
                    if( discoBuscado != null )
                    {
                        Cancion cancionPedida = discoBuscado.darCancion( nombreCancion );
                        cancionPedida.vender( );
                        discosFactura.add( discoBuscado );
                        cancionesFactura.add( cancionPedida );
                        cancionesVendidas++;
                    }
                    else
                        // La canci�n no existe en la discotienda
                        cancionesNoEncontradas.add( pedido );
                }
                else
                    // El formato es inv�lido: no aparece el primer separador
                    cancionesNoEncontradas.add( pedido );
            }
            else
                cancionesNoEncontradas.add( pedido );

            try
            {
                pedido = lector.readLine( );
            }
            catch( IOException e )
            {
                generarFactura( discosFactura, cancionesFactura, cancionesNoEncontradas, email, rutaFactura );
                throw new ArchivoVentaException( e.getMessage( ), cancionesVendidas );
            }
        }

        lector.close( );

        return generarFactura( discosFactura, cancionesFactura, cancionesNoEncontradas, email, rutaFactura );
    }

    private String generarFactura( ArrayList discos, ArrayList canciones, ArrayList noEncontradas, String email, String rutaFactura ) throws IOException
    {
        int posArroba1 = email.indexOf( "@" );
        String login = email.substring( 0, posArroba1 );
        String strTiempo = Long.toString( System.currentTimeMillis( ) );
        String nombreArchivo = login + "_" + strTiempo + ".fac";

        File directorioFacturas = new File( rutaFactura );
        if( !directorioFacturas.exists( ) )
            directorioFacturas.mkdirs( );

        File archivoFactura = new File( directorioFacturas, nombreArchivo );
        PrintWriter out = new PrintWriter( archivoFactura );
        Date fecha = new Date( );
        out.println( "miDiscoTienda - FACTURA" );
        out.println( "Fecha:            " + fecha.toString( ) );
        out.println( "Email:            " + email );

        double valorTotal = 0;
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco disco = ( Disco )discos.get( i );
            Cancion cancion = ( Cancion )canciones.get( i );
            out.println( "Canci�n:          " + cancion.darNombre( ) + " - " + disco.darArtista( ) );
            out.println( "                  " + disco.darNombreDisco( ) );
            valorTotal += cancion.darPrecio( );
        }
        DecimalFormat df = new DecimalFormat( "$0.00" );
        out.println( "No de Canciones:  " + canciones.size( ) );
        out.println( "Valor Total:      " + df.format( valorTotal ) );

        // Incluye en la factura las canciones que no se encontraron
        if( noEncontradas.size( ) > 0 )
        {
            out.println( "\nCanciones no encontradas:" );
            for( int i = 0; i < noEncontradas.size( ); i++ )
            {
                out.println( noEncontradas.get( i ) );
            }
        }
        out.close( );

        return nombreArchivo;
    }

    public boolean validarEmail( String email )
    {
        boolean resultado = true;
        int posArroba1 = email.indexOf( "@" );
        if( posArroba1 == -1 )
        {
            resultado = false;
        }
        else
        {
            String dominio = email.substring( posArroba1 + 1 );
            resultado = dominio.indexOf( "." ) != -1 && ! ( dominio.substring( dominio.indexOf( "." ) + 1 ).equals( "" ) );
        }
        return resultado;
    }

    public void salvarDiscotienda( ) throws PersistenciaException
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream( archivoDiscotienda ) );
            oos.writeObject( discos );
            oos.close( );
        }
        catch( IOException e )
        {
            registrarError( e );
            throw new PersistenciaException( "Error al salvar: " + e.getMessage( ) );
        }
    }

    public void registrarError( Exception excepcion )
    {
        try
        {
            FileWriter out = new FileWriter( LOG_FILE, true );
            PrintWriter log = new PrintWriter( out );
            log.println( "---------------------------------------" );
            log.println( "Discotienda.java :" + new Date( ).toString( ) );
            log.println( "---------------------------------------" );
            excepcion.printStackTrace( log );
            log.close( );
            out.close( );
        }
        catch( IOException e )
        {
            excepcion.printStackTrace( );
            e.printStackTrace( );
        }
    }

    private void verificarInvariante( )
    {
        assert discos != null : "La lista de discos es null";
        assert !buscarDiscosConElMismoNombre( ) : "Hay dos discos con el mismo nombre";
    }

    private boolean buscarDiscosConElMismoNombre( )
    {
        for( int i = 0; i < discos.size( ); i++ )
        {
            Disco d1 = ( Disco )discos.get( i );
            for( int j = i + 1; j < discos.size( ); j++ )
            {
                Disco d2 = ( Disco )discos.get( j );
                if( d1.equals( d2.darNombreDisco( ) ) )
                    return true;
            }
        }
        return false;
    }

    public String metodo1( )
    {
        return "respuesta 1";
    }

    public String metodo2( )
    {
        return "respuesta 2";
    }

    public String metodo3( )
    {
        return "respuesta 3";
    }

    public String metodo4( )
    {
        return "respuesta 4";
    }

    public String metodo5( )
    {
        return "respuesta 5";
    }

    public String metodo6( )
    {
        return "respuesta 6";
    }
}
