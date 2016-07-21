package pe.edu.ulima.discotienda.interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PanelBotonesCancion extends JPanel implements ActionListener
{
    private static final String CREAR_CANCION = "CrearCancion";
    private static final String CANCELAR = "Cancelar";
    private DialogoCrearCancion ventana;
    private JButton botonAgregarCancion;
    private JButton botonCancelar;

    public PanelBotonesCancion( DialogoCrearCancion dcc )
    {

        ventana = dcc;

        botonAgregarCancion = new JButton( "Crear" );
        botonAgregarCancion.setActionCommand( CREAR_CANCION );
        botonAgregarCancion.addActionListener( this );
        add( botonAgregarCancion );

        botonCancelar = new JButton( "Cancelar" );
        botonCancelar.setActionCommand( CANCELAR );
        botonCancelar.addActionListener( this );
        add( botonCancelar );

    }
    public void actionPerformed( ActionEvent evento )
    {
        String comando = evento.getActionCommand( );

        if( CREAR_CANCION.equals( comando ) )
        {
            ventana.crearCancion( );
        }
        else if( CANCELAR.equals( comando ) )
        {
            ventana.dispose( );
        }

    }

}
