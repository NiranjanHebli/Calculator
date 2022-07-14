import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

//for factorial: uses BigInteger and NumberFormat
import java.math.*;
import java.text.*;

public class Calculator extends JFrame implements ActionListener{
   
    JPanel[] row = new JPanel[8]; 

    /*button[0]=="acos", button[length-1]=="="*/
    String[] buttonString = {   "acos", "asin", "atan", "Del", "Clr",
                                "cos", "sin", "tan", "e^x", "ln",
                                "y^x", "xroot", "10^x", "log", "mod",
                                "7", "8", "9", "x!", "1/x",
                                "4", "5", "6", "*", "/",
                                "1", "2", "3", "+", "-",
                                "0", ".", "+/-", "pi", "="  };

    JButton[] button = new JButton[buttonString.length];

    int[] dimW = {300,60}; /*widths for buttons and textarea*/
    int[] dimH = {35, 40}; /*heights for buttons and textarea*/
    Dimension displayDimension = new Dimension(dimW[0], dimH[0]);
    Dimension regularDimension = new Dimension(dimW[1], dimH[1]);

    String function = ""; /*add,sub,mul,div */
    double[] temporary = {0, 0};
    /*holds operands for operators ex. 1 + 1*/
    JTextArea display = new JTextArea(1,20); /*displays input/output*/
    Font font = new Font("Times new Roman", Font.BOLD, 14);

    Calculator() {
        super("GUI Calculator");
        setDesign(); /*need to call this to avoid error*/
        setSize(320, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridLayout grid = new GridLayout(8,5); /*8 rows, 5 col*/
        setLayout(grid);

        FlowLayout f1 = new FlowLayout(FlowLayout.CENTER);
        FlowLayout f2 = new FlowLayout(FlowLayout.CENTER,1,1);

        for( int i=0; i < row.length; ++i ){
            row[i] = new JPanel();
        }
        row[0].setLayout(f1);       /*text area in 0th JPanel*/
        for(int i = 1; i < row.length; i++) /*buttons in 1-4 JPanel*/
            row[i].setLayout(f2);

        /*create each new button obj in a loop*/
        for(int i = 0; i < button.length; ++i){
            button[i] = new JButton();              
            button[i].setText(buttonString[i]);
            button[i].setFont(font);                
            button[i].addActionListener(this);      
            /*need this to make buttons listen to click events*/
        }

        display.setFont(font);
        display.setEditable(false);
        display.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        display.setPreferredSize(displayDimension);

        /*set sizes for various buttons*/
        for(int i=0; i < button.length; ++i){
            button[i].setPreferredSize(regularDimension);
        }

        /*add buttons to each JPanel (row) obj*/
        row[0].add(display);
        add(row[0]);

        for(int i = 0; i < 5; ++i)
            row[1].add(button[i]);
        add(row[1]);

        for(int i = 5; i < 10; ++i)
            row[2].add(button[i]);
        add(row[2]);

        for(int i =10; i < 15; ++i)
            row[3].add(button[i]);
        add(row[3]);

        for(int i =15; i < 20; ++i)
            row[4].add(button[i]);
        add(row[4]);

        for(int i =20; i < 25; ++i)
            row[5].add(button[i]);
        add(row[5]);

        for(int i =25; i < 30; ++i)
            row[6].add(button[i]);
        add(row[6]);

        for(int i =30; i < 35; ++i)
            row[7].add(button[i]);
        add(row[7]);

        setVisible(true);
    }
    /* end of all the GUI layout code */

    public final void setDesign(){
        try{
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
        }
    }


    /*functions buttons (add, sub, mul, etc)*/
    public void clear(){
        try{
            display.setText(""); /*clears display*/
            function = null;
            for( int i=0; i < temporary.length; ++i ){
                temporary[i] = 0;    /*sets operand vars to 0*/
            }
        } catch(NullPointerException e) {
            clear();
            display.setText("Error");
        }
    }

    public void del(){
        try{
            String val = display.getText(); //just need length
            int len = val.length();
            display.replaceRange("", len-1, len);
            temporary[0] = Double.parseDouble(display.getText());
        } catch (Exception e) {
            clear();
            display.setText("Error");
        }
    }

    public BigInteger getFactorial(int n){
        BigInteger ans = new BigInteger("1");
        if( n <= 1 ) return ans; //only defined for pos nums
        for( ; n > 1; --n ){
            ans = ans.multiply(new BigInteger(n + ""));
            //BigInt constructor takes Strings, not ints
        }
        return ans;
    }

    public void getResult() {
        double result = 0;
        temporary[1] = Double.parseDouble(display.getText());

        String temp0 = Double.toString(temporary[0]);
        String temp1 = Double.toString(temporary[1]);
        try {
            if(temp0.contains("-")) {
                String[] temp00 = temp0.split("-", 2);
                temporary[0] = (Double.parseDouble(temp00[1]) * -1);
            }
            if(temp1.contains("-")) {
                String[] temp11 = temp1.split("-", 2);
                temporary[1] = (Double.parseDouble(temp11[1]) * -1);
            }
        } catch( ArrayIndexOutOfBoundsException e ){
            clear();
            display.setText("Error");
        }

        /*order is Parenthsis, Exponent, Mul, Div, Add, Sub*/
        try{
            if( function.equals("y^x") )
                result = Math.pow(temporary[0], temporary[1]);
            else if( function.equals("xroot") )
                result = Math.pow(temporary[0], 1.0/temporary[1]);
            else if( function.equals("10^x") )
                result = Math.pow(10.0,temporary[0]);
            else if( function.equals("log") )
                result = Math.log10(temporary[0]);
            else if( function.equals("*") )
                result = temporary[0] * temporary[1];
            else if( function.equals("/") )
                result = temporary[0] / temporary[1];
            else if( function.equals("%") )
                result = temporary[0] % temporary[1];
            else if( function.equals("+") )
                result = temporary[0] + temporary[1];
            else if( function.equals("-") )
                result = temporary[0] - temporary[1];

            display.setText(Double.toString(result));
        } catch( Exception e ){
            clear();
            display.setText("Error");
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae){
        if(ae.getSource() == button[0]){ //acos unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.acos(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[1]){ //asin unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.asin(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[2]){ //atan unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.atan(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[3]){ //del unary operator
            del();
        }
        if(ae.getSource() == button[4]){ //clr unary operator
            clear();
        }
        if(ae.getSource() == button[5]){ //cos unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.cos(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[6]){ //sin unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.sin(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[7]){ //tan unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.tan(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[8]){ //e^x binary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.pow(Math.E,temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[9]){ //ln unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.log(temporary[0]);
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[10]){ //power binary operator
            temporary[0] = Double.parseDouble(display.getText());
            function = "y^x";
            display.setText("");
        }
        if(ae.getSource() == button[11]){ //xth root binary operator
            temporary[0] = Double.parseDouble(display.getText());
            function = "xroot";
            display.setText("");
        }
        if(ae.getSource() == button[12]){ //exponent unary operator
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.pow(10.0,temporary[0]);
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[13]){ //log10 unary op
            temporary[0] = Double.parseDouble(display.getText());
            double val = Math.log10(temporary[0]);
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[14]){ //modulo binary op
            temporary[0] = Double.parseDouble(display.getText());
            function = "%";
            display.setText("");
        }
        if(ae.getSource() == button[15]){
            display.append("7");
        }
        if(ae.getSource() == button[16]){
            display.append("8");
        }
        if(ae.getSource() == button[17]){
            display.append("9");
        }
        if(ae.getSource() == button[18]){ //factorial unary op
            temporary[0] = Integer.parseInt(display.getText());
            BigInteger val = getFactorial((int)temporary[0]);
            BigInteger max = new BigInteger(Integer.MAX_VALUE + "");

            if( val.compareTo(max) > 0 ){
                NumberFormat formatter = new DecimalFormat("0.##########E0");
                display.setText(formatter.format(val));
            } else {
                display.setText(String.valueOf(val));
            }
            
        }
        if(ae.getSource() == button[19]){ //reciprocal unary op
            temporary[0] = Double.parseDouble(display.getText());
            double val = 1.0/temporary[0];
            clear();
            display.setText(String.valueOf(val));
        }
        if(ae.getSource() == button[20]){
            display.append("4");
        }
        if(ae.getSource() == button[21]){
            display.append("5");
        }
        if(ae.getSource() == button[22]){
            display.append("6");
        }
        if(ae.getSource() == button[23]){ //mul binary op
            temporary[0] = Double.parseDouble(display.getText());
            function = "*";
            display.setText("");
        }
        if(ae.getSource() == button[24]){ //div binary op
            temporary[0] = Double.parseDouble(display.getText());
            function = "/";
            display.setText("");
        }
        if(ae.getSource() == button[25]){
            display.append("1");
        }
        if(ae.getSource() == button[26]){
            display.append("2");
        }
        if(ae.getSource() == button[27]){
            display.append("3");
        }
        if(ae.getSource() == button[28]){ //add binary op
            temporary[0] = Double.parseDouble(display.getText());
            function = "+";
            display.setText("");
        }
        if(ae.getSource() == button[29]){ //sub binary op
            temporary[0] = Double.parseDouble(display.getText());
            function = "-";
            display.setText("");
        }
        if(ae.getSource() == button[30]){
            display.append("0");
        }
        if(ae.getSource() == button[31]){ //decimal point
            display.append("0.");
        }
        if(ae.getSource() == button[32]){ //negation unary op
            try {
                temporary[0] = Double.parseDouble(display.getText());
                if(temporary[0] != 0) {
                    temporary[0] *= -1.0;
                    display.setText(String.valueOf(temporary[0]));
                }
            } catch(NumberFormatException e) {
                clear();
                display.setText("Error");
            }
        }
        if(ae.getSource() == button[33]){
            display.append(String.valueOf(Math.PI));
        }
        if(ae.getSource() == button[34]){
            getResult();
        }
    }



    //start calculator
    public static void main(String args[]){
        Calculator c = new Calculator();
    }
}