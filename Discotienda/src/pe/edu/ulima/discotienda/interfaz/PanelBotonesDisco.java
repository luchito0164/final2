package pe.edu.ulima.discotienda.interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelBotonesDisco extends JPanel implements ActionListener
{
    private static final String CREAR_DISCO = "CrearDisco";

    private static final String CANCELAR = "Cancelar";

    private DialogoCrearDisco ventana;

    private JButton botonAgregarDisco;

    private JButton botonCancelar;

    public PanelBotonesDisco( DialogoCrearDisco dcd )
    {

        ventana = dcd;

        botonAgregarDisco = new JButton( "Crear" );
        botonAgregarDisco.setActionCommand( CREAR_DISCO );
        botonAgregarDisco.addActionListener( this );
        add( botonAgregarDisco );

        botonCancelar = new JButton( "Cancelar" );
        botonCancelar.setActionCommand( CANCELAR );
        botonCancelar.addActionListener( this );
        add( botonCancelar );

    }
    public void actionPerformed( ActionEvent evento )
    {
        String comando = evento.getActionCommand( );

        if( CREAR_DISCO.equals( comando ) )
        {
            ventana.crearDisco( );
        }
        else if( CANCELAR.equals( comando ) )
        {
            ventana.dispose( );
        }

    }
}
