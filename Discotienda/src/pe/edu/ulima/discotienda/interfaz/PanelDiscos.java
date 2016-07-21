package pe.edu.ulima.discotienda.interfaz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import pe.edu.ulima.discotienda.mundo.Disco;

public class PanelDiscos extends JPanel implements ActionListener
{
    private static final String CAMBIAR_DISCO = "CambiarDisco";

    private static final String AGREGAR_DISCO = "AgregarDisco";

    private InterfazDiscotienda principal;

    private Disco disco;

    private JComboBox comboDiscos;

    private JLabel etiquetaTituloArtista;

    private JTextField txtArtista;

    private JLabel etiquetaTituloGenero;

    private JTextField txtGenero;

    private JLabel etiquetaTituloPrecio;

    private JTextField txtPrecio;

    private JButton botonAgregarDisco;

    private JLabel etiquetaImagen;

    public PanelDiscos( InterfazDiscotienda ventanaPrincipal, ArrayList discos )
    {
        principal = ventanaPrincipal;

        setLayout( new BorderLayout( ) );

        comboDiscos = new JComboBox( discos.toArray( ) );
        comboDiscos.setEditable( false );
        comboDiscos.addActionListener( this );
        comboDiscos.setActionCommand( CAMBIAR_DISCO );
        add( comboDiscos, BorderLayout.NORTH );

        JPanel panelDatosDisco = new JPanel( );
        add( panelDatosDisco, BorderLayout.CENTER );

        panelDatosDisco.setLayout( new GridLayout( 6, 1, 0, 5 ) );

        etiquetaTituloArtista = new JLabel( "Artista: " );
        txtArtista = new JTextField( 10 );
        txtArtista.setEditable( false );
        txtArtista.setFont( txtArtista.getFont( ).deriveFont( Font.PLAIN ) );
        panelDatosDisco.add( etiquetaTituloArtista );
        panelDatosDisco.add( txtArtista );

        etiquetaTituloGenero = new JLabel( "G�nero: " );
        txtGenero = new JTextField( 10 );
        txtGenero.setEditable( false );
        txtGenero.setFont( txtGenero.getFont( ).deriveFont( Font.PLAIN ) );
        panelDatosDisco.add( etiquetaTituloGenero );
        panelDatosDisco.add( txtGenero );

        etiquetaTituloPrecio = new JLabel( "Precio: " );
        txtPrecio = new JTextField( 10 );
        txtPrecio.setEditable( false );
        txtPrecio.setFont( txtPrecio.getFont( ).deriveFont( Font.PLAIN ) );
        panelDatosDisco.add( etiquetaTituloPrecio );
        panelDatosDisco.add( txtPrecio );

        panelDatosDisco.setBorder( new EmptyBorder( 5, 5, 5, 5 ) );

        etiquetaImagen = new JLabel( );
        etiquetaImagen.setPreferredSize( new Dimension( 200, 200 ) );
        etiquetaImagen.setBorder( new CompoundBorder( new EmptyBorder( 5, 5, 5, 5 ), new TitledBorder( "" ) ) );
        add( etiquetaImagen, BorderLayout.EAST );

        botonAgregarDisco = new JButton( "Agregar Disco" );
        botonAgregarDisco.setActionCommand( AGREGAR_DISCO );
        botonAgregarDisco.addActionListener( this );
        add( botonAgregarDisco, BorderLayout.SOUTH );

        setBorder( new CompoundBorder( new EmptyBorder( 5, 5, 5, 5 ), new TitledBorder( "Discos" ) ) );

    }

    public void cambiarDisco( Disco d )
    {
        disco = d;
        if( disco != null )
        {
            etiquetaImagen.setIcon( new ImageIcon( disco.darImagen( ) ) );

            txtArtista.setText( disco.darArtista( ) );
            txtGenero.setText( disco.darGenero( ) );
            txtPrecio.setText( Double.toString( disco.darPrecioDisco( ) ) );
        }
        else
        {
            setBorder( new CompoundBorder( new EmptyBorder( 5, 5, 5, 5 ), new TitledBorder( "Detalles del Disco" ) ) );
        }
    }

    public void refrescarDiscos( ArrayList discos )
    {
        comboDiscos.removeAllItems( );

        for( int i = 0; i < discos.size( ); i++ )
        {
            comboDiscos.addItem( discos.get( i ) );
        }
    }

    public void actionPerformed( ActionEvent evento )
    {
        String comando = evento.getActionCommand( );

        if( AGREGAR_DISCO.equals( comando ) )
        {
            principal.mostrarDialogoAgregarDisco( );
        }
        else if( CAMBIAR_DISCO.equals( comando ) )
        {
            String nombreDisco = ( String )comboDiscos.getSelectedItem( );
            principal.cambiarDiscoSeleccionado( nombreDisco );
        }
    }

}
