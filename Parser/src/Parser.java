
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Parser extends JPanel {
    private class OpNode{
        private String value;
        private String type;
        private OpNode leftChild;
        private OpNode rightChild;
        public OpNode (String val,String type){
            this.value = val;
            this.type = type;
        }
        public OpNode(){

        }
        public void setValue(String value) {
            this.value = value;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setLeftChild(OpNode leftChild) {
            this.leftChild = leftChild;
        }

        public void setRightChild(OpNode rightChild) {
            this.rightChild = rightChild;
        }

        public String getValue() {
            return value;
        }

        public String getType() {
            return type;
        }

        public OpNode getLeftChild() {
            return leftChild;
        }

        public OpNode getRightChild() {
            return rightChild;
        }
        public void draw(Graphics g, int x, int y){
            String drawnType = "";
            if(this.type.equals("op")){
                drawnType = "op";
            }
            else if(this.type.equals("identifier")){
                drawnType = "id";
            }
            else if(this.type.equals("number")){
                drawnType = "const";
            }
            g.drawOval(x, y, 45, 39);
            g.drawString(drawnType,x+12,y+12);
            g.drawString("("+this.value+")",x+12,y+25);
            if(this.rightChild != null){
                g.drawLine((x+13),(y+39),(x+13-20),(y+39+10));
                rightChild.draw(g,x+2*13,y+39+10);}
            if(this.leftChild != null){
                g.drawLine((x+2*13),(y+39),(x+2*13+20),(y+39+10));
                leftChild.draw(g,x+13-40,y+39+10);
            }


            // left child x = x+2*13+20;
            // left / right child y = y+39+20;
            // right child x = x+13-60;
        }
    }
    private class Stmt{
        private String Type;
        public Stmt(){

        }

        public void setType(String type) {
            Type = type;
        }

        public String getType() {
            return Type;
        }
        public void draw(Graphics g, int x, int y){}
    }
    private class StmtSeq{
        private ArrayList<Stmt> Stmts;
        public StmtSeq(Stmt stmt){
            Stmts = new ArrayList<Stmt>();
            Stmts.add(stmt);
        }
        public void addStmt(Stmt stmt){
            Stmts.add(stmt);
        }

        public ArrayList<Stmt> getStmts() {
            return Stmts;
        }
        public void draw(Graphics g, int x, int y){
            int n = this.Stmts.size();
            Stmt stmt = this.Stmts.get(0);
            stmt.draw(g,x,y);
            g.drawLine(x+60,y+30,x+135,y+30);
            boolean repeat = false;
            for(int i=1;i<n;i++){
                repeat = false;
                if(i<n-1){
                    if(this.Stmts.get(i).getType().equals("repeat")){repeat=true;}
                g.drawLine(x+60,y+30,x+135,y+30);
                    if(repeat){
                        g.drawLine(x+135,y+30,x+135+80,y+30);
                        x=x+80;
                }


                }
                x = x+135;
                stmt = this.Stmts.get(i);
                stmt.draw(g,x,y);

            }


        }
    }
    private class Program{
        private StmtSeq stmtSeq;
        public Program(){
        }
        public void setStmtSeq(StmtSeq stmtSeq) {
            this.stmtSeq = stmtSeq;
        }

        public StmtSeq getStmtSeq() {
            return stmtSeq;
        }
        public void draw(Graphics g, int x, int y){
            stmtSeq.draw(g,x,y);
        }
    }
    private class IfStmt extends Stmt{
        private OpNode testChild;
        private StmtSeq thenChild;
        private StmtSeq elseChild;
        public IfStmt(){
            this.setType("If");
        }

        public void setTestChild(OpNode testChild) {
            this.testChild = testChild;
        }

        public void setThenChild(StmtSeq thenChild) {
            this.thenChild = thenChild;
        }

        public void setElseChild(StmtSeq elseChild) {
            this.elseChild = elseChild;
        }

        public OpNode getTestChild() {
            return testChild;
        }

        public StmtSeq getThenChild() {
            return thenChild;
        }

        public StmtSeq getElseChild() {
            return elseChild;
        }
        public void draw(Graphics g, int x, int y){
            //if stmt

            g.drawRect( x,  y, 60, 40);
            g.drawString("if",x+25,y+20);
            g.drawLine(x,y+40,x-100,y+80);//test
            g.drawLine(x+30,y+40,x+30,y+80);//then
        //optional else part
            //conditionX x-120, boydX x, elseX x+125, Y y+80;
            testChild.draw(g,x-120,y+80);
            thenChild.draw(g, x, y+80);
            if(elseChild != null){
                g.drawLine(x+60,y+40,x+135+20,y+80);
                elseChild.draw(g,x+125, y+80);
            }

        }
    }
    private class RepeatStmt extends Stmt{
        private StmtSeq bodyChild;
        private OpNode testChild;
        public RepeatStmt(){
            this.setType("repeat");
        }

        public void setBodyChild(StmtSeq bodyChild) {
            this.bodyChild = bodyChild;
        }

        public void setTestChild(OpNode testChild) {
            this.testChild = testChild;
        }

        public StmtSeq getBodyChild() {
            return bodyChild;
        }

        public OpNode getTestChild() {
            return testChild;
        }
        public void draw(Graphics g, int x, int y){
            g.drawRect( x,  y, 60, 40);
            g.drawString("repeat",x+10,y+15);
            g.drawLine(x+20,y+40,x-120,y+60);//Body left
            g.drawLine(x+40,y+40,x+80,y+60);//test right
            bodyChild.draw(g, x-150,y+60);
            testChild.draw(g,x+60,y+60);

        }

    }
    private class AssignStmt extends Stmt{
        private String identifierName;
        private OpNode assignedValue;
        public AssignStmt(){
            this.setType("assign");
        }

        public void setIdentifierName(String identifierName) {
            this.identifierName = identifierName;
        }

        public void setAssignedValue(OpNode assignedValue) {
            this.assignedValue = assignedValue;
        }

        public String getIdentifierName() {
            return identifierName;
        }

        public OpNode getAssignedValue() {
            return assignedValue;
        }
        public void draw(Graphics g, int x, int y){
        g.drawRect( x,  y, 60, 40);
        g.drawString("assign",x+10,y+15);
        g.drawString("("+this.identifierName+")",x+10,y+30);
        g.drawLine(x+30,y+40,x+30,y+60);
        this.assignedValue.draw(g,x+10,y+60);
        }
    }
    private class ReadStmt extends Stmt{
        private String identifierName;
        public ReadStmt(){
            this.setType("read");
        }

        public void setIdentifierName(String identifierName) {
            this.identifierName = identifierName;
        }

        public String getIdentifierName() {
            return identifierName;
        }
        public void draw(Graphics g, int x, int y){
            g.drawRect( x,  y, 60, 40);
            g.drawString("read",x+10,y+15);
            g.drawString("("+this.identifierName+")", x+10,y+25);
        }
    }
    private class WriteStmt extends Stmt{
        private OpNode value;
        public WriteStmt(){
            this.setType("write");
        }

        public void setValue(OpNode value) {
            this.value = value;
        }

        public OpNode getValue() {
            return value;
        }
        public void draw(Graphics g, int x, int y){
            g.drawRect( x,  y, 60, 40);
            g.drawString("write",x+10,y+15);
            g.drawLine(x+30,y+40,x+30,y+60);
            this.value.draw(g,x+10,y+60);
        }
    }
    private Program program;
    private Scanner scanner;
    private String token;
    private String tokenType;
    private String token_type[] = new String[2];
    private String output;
    public Parser(Scanner scanner){
        this.scanner = scanner;
        this.output = "";
        token_type = new String[2];
        token_type = scanner.getToken();
        token = token_type[0];
        tokenType = token_type[1];
        program = new Program();
        program.setStmtSeq(stmtSeq());
        output += "Program found\n";
    }

    private StmtSeq stmtSeq(){
        StmtSeq temp = new StmtSeq(stmt());
        while(token.equals(";")){
            matchToken(";");
            temp.addStmt(stmt());
        }
        output+="Statement_Sequence Found\n";
        return temp;
    }
    private Stmt stmt(){
        Stmt temp = new Stmt();
        if(token.equals("if")){
            temp = ifStmt();
        }
        else if(token.equals("repeat")){
            temp = repeatStmt();
        }
        else if(tokenType.equals("identifier")){
            temp = assignStmt();
        }
        else if(token.equals("read")){
            temp = readStmt();
        }
        else if(token.equals("write")){
            temp = writeStmt();
        }
        output+="Statement Found\n";
        return temp;
    }
    private Stmt ifStmt(){
        matchToken("if");
        IfStmt temp = new IfStmt();
        temp.setTestChild(exp());
        matchToken("then");
        temp.setThenChild(stmtSeq());
        if(token.equals("else")){
            matchToken("else");
            temp.setElseChild(stmtSeq());
        }
        matchToken("end");
        output += "IF_Statement Found\n";
        return temp;

    }
    private Stmt repeatStmt(){
        RepeatStmt temp = new RepeatStmt();
        matchToken("repeat");
        temp.setBodyChild(stmtSeq());
        matchToken("until");
        temp.setTestChild(exp());
        output += "Repeat_Statement Found\n";
        return temp;
    }
    private Stmt assignStmt(){
        AssignStmt temp = new AssignStmt();
        temp.setIdentifierName(token);
        matchTokenType("identifier");
        matchToken(":=");
        temp.setAssignedValue(exp());
        output += "Assignment_Statement Found\n";
        return temp;
    }
    private Stmt readStmt(){
        matchToken("read");
        ReadStmt temp = new ReadStmt();
        temp.setIdentifierName(token);
        matchTokenType("identifier");
        output += "Read_Statement Found\n";
        return temp;
    }
    private Stmt writeStmt(){
        matchToken("write");
        WriteStmt temp = new WriteStmt();
        temp.setValue(exp());
        output += "Write_Statement Found\n";
        return temp;
    }
    private OpNode exp(){
        OpNode temp = simpleExp();
        if(token.equals(">")||token.equals("<")||token.equals("=")){
            OpNode newTemp = new OpNode(token,"op");
            matchToken(token);
            output+="Comparator_Operator Found\n";
            newTemp.setLeftChild(temp);
            newTemp.setRightChild(simpleExp());
            temp=newTemp;
        }
        output+="Expression Found\n";
        return temp;
    }
    private OpNode simpleExp(){
        OpNode temp = term();
        while(token.equals("+")|| token.equals("-")){
            OpNode newTemp = new OpNode(token,"op");
            matchToken(token);
            output+="Add_Operator Found\n";
            newTemp.setLeftChild(temp);
            newTemp.setRightChild(term());
            temp = newTemp;
        }
        output+="Simple_Expression Found\n";
        return temp;
    }
    private OpNode term(){
        OpNode temp = factor();
        while(token.equals("*")|| token.equals("/")){
            OpNode newTemp = new OpNode(token,"op");
            matchToken(token);
            output+="Mul_Operator Found\n";
            newTemp.setLeftChild(temp);
            newTemp.setRightChild(factor());
            temp = newTemp;
        }
        output+="Term Found\n";
        return temp;
    }

    private OpNode factor(){
        OpNode temp = new OpNode();
        if(token.equals("(")){
          matchToken("(");
            temp = exp();
           matchToken(")");
        }
        else if(tokenType.equals("number")){
            temp.setValue(token);
            temp.setType(tokenType);
            matchTokenType("number");

        }
        else if(tokenType.equals("identifier")){
            temp.setValue(token);
            temp.setType(tokenType);
            matchTokenType("identifier");

        }
        output+="Factor Found\n";
        return  temp;
    }
    private void matchToken(String expectedToken){
        if(token.equals(expectedToken)){
            token_type = scanner.getToken();
            token = token_type[0];
            tokenType = token_type[1];
        }
        else{
            System.out.println(token+"!="+expectedToken);
            System.out.println("Syntax Error");
        }
    }
    private void matchTokenType(String expectedTokenType){
        if(tokenType.equals(expectedTokenType)){
            token_type = scanner.getToken();
            token = token_type[0];
            tokenType = token_type[1];
        }
        else{
            System.out.println(tokenType+"!="+expectedTokenType);
            System.out.println("Syntax Error");
        }
    }
    public String toString(){
        return output;
    }
    public void paintComponent(Graphics g) {
        // Draw Tree Here
        int x=300,y=150;
        this.program.draw(g,x,y);
    }


}
