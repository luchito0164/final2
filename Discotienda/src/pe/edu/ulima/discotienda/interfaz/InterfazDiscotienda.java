package pe.edu.ulima.discotienda.interfaz;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import pe.edu.ulima.discotienda.mundo.ArchivoVentaException;
import pe.edu.ulima.discotienda.mundo.Cancion;
import pe.edu.ulima.discotienda.mundo.Disco;
import pe.edu.ulima.discotienda.mundo.Discotienda;
import pe.edu.ulima.discotienda.mundo.ElementoExisteException;
import pe.edu.ulima.discotienda.mundo.PersistenciaException;

public class InterfazDiscotienda extends JFrame
{
    private static final String RUTA_FACTURAS = "./data/facturas";
    private Discotienda discotienda;
    private Disco discoSeleccionado;
    private PanelExtension panelExtension;
    private PanelDiscos panelDiscos;
    private PanelDatosCanciones panelDatosCanciones;
    private PanelImagen panelImagen;
    private PanelPedido panelPedido;
    public InterfazDiscotienda( Discotienda d )
    {
        discotienda = d;

        panelImagen = new PanelImagen( );
        add( panelImagen, BorderLayout.NORTH );

        JPanel panelCentral = new JPanel( new BorderLayout( ) );
        add( panelCentral, BorderLayout.CENTER );

        panelDiscos = new PanelDiscos( this, discotienda.darDiscos( ) );
        panelCentral.add( panelDiscos, BorderLayout.CENTER );

        panelDatosCanciones = new PanelDatosCanciones( this );
        panelCentral.add( panelDatosCanciones, BorderLayout.EAST );

        ArrayList discos = discotienda.darDiscos( );
        if( discos.size( ) > 0 )
        {
            cambiarDiscoSeleccionado( ( ( String )discos.get( 0 ) ) );
        }

        panelPedido = new PanelPedido( this );
        panelCentral.add( panelPedido, BorderLayout.SOUTH );

        panelExtension = new PanelExtension( this );
        add( panelExtension, BorderLayout.SOUTH );

        setTitle( "miDiscoTienda" );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        pack( );
    }

    public void cambiarDiscoSeleccionado( String nombreDisco )
    {
        discoSeleccionado = discotienda.darDisco( nombreDisco );
        panelDiscos.cambiarDisco( discoSeleccionado );
        panelDatosCanciones.cambiarDisco( discoSeleccionado );
    }

    public void mostrarDialogoAgregarDisco( )
    {
        DialogoCrearDisco dialogo = new DialogoCrearDisco( this );
        dialogo.setLocationRelativeTo( this );
        dialogo.setVisible( true );
    }

    public void mostrarDialogoAgregarCancion( )
    {
        DialogoCrearCancion dialogo = new DialogoCrearCancion( this );
        dialogo.setLocationRelativeTo( this );
        dialogo.setVisible( true );
    }

    public boolean crearDisco( String nombreDisco, String artista, String genero, String imagen )
    {
        boolean ok = false;
        try
        {
            discotienda.agregarDisco( nombreDisco, artista, genero, imagen );
            panelDiscos.refrescarDiscos( discotienda.darDiscos( ) );
            ok = true;
        }
        catch( ElementoExisteException e )
        {
            JOptionPane.showMessageDialog( this, e.getMessage( ) );
        }

        return ok;
    }

    public boolean crearCancion( String nombre, int minutos, int segundos, double precio, double tamano, int calidad )
    {
        boolean ok = false;

        if( discoSeleccionado != null )
        {
            try
            {
                discotienda.agregarCancionADisco( discoSeleccionado.darNombreDisco( ), nombre, minutos, segundos, precio, tamano, calidad );
                discoSeleccionado = discotienda.darDisco( discoSeleccionado.darNombreDisco( ) );
                panelDiscos.cambiarDisco( discoSeleccionado );
                ok = true;
            }
            catch( ElementoExisteException e )
            {
                JOptionPane.showMessageDialog( this, e.getMessage( ) );
            }
        }

        return ok;
    }

    public void venderCancion( Disco disco, Cancion cancion )
    {
        String email = JOptionPane.showInputDialog( this, "Indique el email del comprador", "Email", JOptionPane.QUESTION_MESSAGE );
        if( email != null )
        {
            if( discotienda.validarEmail( email ) )
            {
                try
                {
                    String archivoFactura = discotienda.venderCancion( disco, cancion, email, RUTA_FACTURAS );
                    JOptionPane.showMessageDialog( this, "La factura se guard� en el archivo: " + archivoFactura, "Factura Guardada", JOptionPane.INFORMATION_MESSAGE );
                }
                catch( IOException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presenta un problema guardando el archivo de la factura:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
            else
            {
                JOptionPane.showMessageDialog( this, "El email indicado no es valido", "Error", JOptionPane.ERROR_MESSAGE );
            }
        }
    }

    public void cargarPedido( )
    {
        JFileChooser fc = new JFileChooser( "./data" );
        fc.setDialogTitle( "Pedido" );
        int resultado = fc.showOpenDialog( this );
        if( resultado == JFileChooser.APPROVE_OPTION )
        {
            File archivo = fc.getSelectedFile( );
            if( archivo != null )
            {
                try
                {
                    String archivoFactura = discotienda.venderListaCanciones( archivo, RUTA_FACTURAS );
                    JOptionPane.showMessageDialog( this, "La factura se guardó en el archivo: " + archivoFactura, "Factura Guardada", JOptionPane.INFORMATION_MESSAGE );
                }
                catch( FileNotFoundException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presentó un problema leyendo el archivo:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
                catch( IOException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presentó un problema leyendo el archivo:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
                catch( ArchivoVentaException e )
                {
                    JOptionPane.showMessageDialog( this, "Se presentó un problema debido al formato del archivo:\n" + e.getMessage( ), "Error", JOptionPane.ERROR_MESSAGE );
                }
            }
        }
    }

    public void dispose( )
    {
        try
        {
            discotienda.salvarDiscotienda( );
            super.dispose( );
        }
        catch( Exception e )
        {
            setVisible( true );
            int respuesta = JOptionPane.showConfirmDialog( this, "Problemas salvando la informaci�n de la discotienda:\n" + e.getMessage( ) + "\n�Quiere cerrar el programa sin salvar?", "Error", JOptionPane.YES_NO_OPTION );
            if( respuesta == JOptionPane.YES_OPTION )
            {
                super.dispose( );
            }
        }
    }

    public void reqFuncOpcion1( )
    {
        String resultado = discotienda.metodo1( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    public void reqFuncOpcion2( )
    {
        String resultado = discotienda.metodo2( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    public void reqFuncOpcion3( )
    {
        String resultado = discotienda.metodo3( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    public void reqFuncOpcion4( )
    {
        String resultado = discotienda.metodo4( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    public void reqFuncOpcion5( )
    {
        String resultado = discotienda.metodo5( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    public void reqFuncOpcion6( )
    {
        String resultado = discotienda.metodo6( );
        JOptionPane.showMessageDialog( this, resultado, "Respuesta", JOptionPane.INFORMATION_MESSAGE );
    }

    public static void main( String[] args )
    {
        Discotienda discotienda = null;
        try
        {
            discotienda = new Discotienda( "./data/discotienda.discos" );
        }
        catch( PersistenciaException e )
        {
            e.printStackTrace( );
            System.exit( 1 );
        }
        InterfazDiscotienda id = new InterfazDiscotienda( discotienda );
        id.setVisible( true );
    }
}
