package ntr.ui.jswing.frames;

public class TextField extends javax.swing.JFrame {
	
	private static final long serialVersionUID = -3913189895323099756L;
	private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField jTextField1;
    
    public TextField() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel1.setText("Test");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(60, 40, 70, 14);

        jTextField1.setText("jTextField1");
        jTextField1.setOpaque(false);
        getContentPane().add(jTextField1);
        jTextField1.setBounds(50, 30, 90, 40);

        pack();
    }
}