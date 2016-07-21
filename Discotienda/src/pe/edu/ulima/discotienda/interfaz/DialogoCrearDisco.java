package pe.edu.ulima.discotienda.interfaz;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class DialogoCrearDisco extends JDialog
{
    private InterfazDiscotienda principal;

    private PanelCrearDisco panelDatos;

    private PanelBotonesDisco panelBotones;

    public DialogoCrearDisco( InterfazDiscotienda id )
    {
        super( id, true );
        principal = id;

        panelDatos = new PanelCrearDisco( );
        panelBotones = new PanelBotonesDisco( this );

        getContentPane( ).add( panelDatos, BorderLayout.CENTER );
        getContentPane( ).add( panelBotones, BorderLayout.SOUTH );

        setTitle( "Crear Disco" );
        pack( );

    }

    public void crearDisco( )
    {
        boolean parametersOk = true;
        String artista = panelDatos.darArtista( );
        String titulo = panelDatos.darTitulo( );
        String genero = panelDatos.darGenero( );
        String imagen = panelDatos.darImagen( );

        if( ( artista.equals( "" ) || titulo.equals( "" ) ) || ( genero.equals( "" ) || imagen.equals( "" ) ) )
        {
            parametersOk = false;
            JOptionPane.showMessageDialog( this, "Todos los campos deben ser llenados para crear el disco" );
        }
        if( parametersOk )
        {
            boolean ok = principal.crearDisco( titulo, artista, genero, imagen );
            if( ok )
                dispose( );
        }
    }
}
