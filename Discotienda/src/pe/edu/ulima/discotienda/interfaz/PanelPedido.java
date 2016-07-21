package pe.edu.ulima.discotienda.interfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class PanelPedido extends JPanel implements ActionListener
{
    private static final String CARGAR_PEDIDO = "CargarPedido";

    private InterfazDiscotienda principal;

    private JButton btnPedido;

    public PanelPedido( InterfazDiscotienda ventanaPrincipal )
    {
        principal = ventanaPrincipal;
        setBorder( new TitledBorder( "Pedidos" ) );

        btnPedido = new JButton( "Cargar Pedido" );
        btnPedido.setActionCommand( CARGAR_PEDIDO );
        btnPedido.addActionListener( this );
        add( btnPedido );
    }

    public void actionPerformed( ActionEvent evento )
    {
        String comando = evento.getActionCommand( );

        if( CARGAR_PEDIDO.equals( comando ) )
        {
            principal.cargarPedido( );
        }
    }
}
