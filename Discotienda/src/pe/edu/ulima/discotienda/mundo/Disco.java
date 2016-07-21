package pe.edu.ulima.discotienda.mundo;

import java.io.*;
import java.util.*;

public class Disco implements Serializable
{
    private static final long serialVersionUID = 100L;

    private ArrayList canciones;

    private String nombreDisco;

    private String artista;

    private String genero;

    private String imagen;

    private double precioTotal;

    public Disco( String nombreDiscoD, String artistaD, String generoD, String imagenD )
    {
        canciones = new ArrayList( );
        nombreDisco = nombreDiscoD;
        artista = artistaD;
        genero = generoD;
        imagen = imagenD;
        precioTotal = 0;

        verificarInvariante( );
    }

    public Cancion darCancion( String nombreC )
    {
        for( int i = 0; i < canciones.size( ); i++ )
        {
            Cancion c = ( Cancion )canciones.get( i );
            if( c.equals( nombreC ) )
                return c;
        }
        return null;
    }

    public void agregarCancion( Cancion c ) throws ElementoExisteException
    {
        if( darCancion( c.darNombre( ) ) != null )
            throw new ElementoExisteException( "La canción " + c.darNombre( ) + " ya existe en el disco" );

        canciones.add( c );
        precioTotal += c.darPrecio( );

        verificarInvariante( );
    }

    public String darArtista( )
    {
        return artista;
    }

    public ArrayList darNombresCanciones( )
    {
        ArrayList nombresCanciones = new ArrayList( );
        for( int i = 0; i < canciones.size( ); i++ )
        {
            Cancion c = ( Cancion )canciones.get( i );
            nombresCanciones.add( c.darNombre( ) );
        }
        return nombresCanciones;
    }

    public String darGenero( )
    {
        return genero;
    }

    public String darNombreDisco( )
    {
        return nombreDisco;
    }

    public String darImagen( )
    {
        return imagen;
    }

    public double darPrecioDisco( )
    {
        return precioTotal;
    }

    public boolean equals( String nombre )
    {
        return nombreDisco.equalsIgnoreCase( nombre );
    }

    private void verificarInvariante( )
    {
        assert canciones != null : "La lista de canciones es nula";
        assert nombreDisco != null && !nombreDisco.equals( "" ) : "El nombre del disco es inv�lido";
        assert artista != null && !artista.equals( "" ) : "El nombre del artista es inv�lido";
        assert genero != null && !genero.equals( "" ) : "El nombre del género es inv�lido";
        assert imagen != null && !imagen.equals( "" ) : "El nombre del archivo con la imagen es inv�lido";

        assert !buscarCancionesConElMismoNombre( ) : "Hay dos canciones con el mismo nombre";
        assert precioTotal == recalcularPrecioDisco( ) : "Hay un error en el cálculo del precio total del disco";
    }

    private double recalcularPrecioDisco( )
    {
        double acumPrecioTotal = 0;
        for( int i = 0; i < canciones.size( ); i++ )
        {
            Cancion c = ( Cancion )canciones.get( i );
            acumPrecioTotal = acumPrecioTotal + c.darPrecio( );
        }
        return acumPrecioTotal;
    }

    private boolean buscarCancionesConElMismoNombre( )
    {
        for( int i = 0; i < canciones.size( ); i++ )
        {
            Cancion c1 = ( Cancion )canciones.get( i );
            for( int j = i + 1; j < canciones.size( ); j++ )
            {
                Cancion c2 = ( Cancion )canciones.get( j );
                if( c1.equals( c2.darNombre( ) ) )
                    return true;
            }
        }
        return false;
    }
}
