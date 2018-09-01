import java.util.ArrayList;
import java.io.*;
public class Scanner {
    private int index;
    private String input;
    private ArrayList<String> token;
    private ArrayList<String> type;
    public Scanner(String I){
        index = 0;
        this.token = new ArrayList<String>();
        this.type = new ArrayList<String>();
        this.input = I;
    }
    private boolean isSpecialSymbol(char c){
        if(c=='+' || c=='-'|| c=='*'|| c=='/'|| c=='='|| c=='<'|| c=='>'|| c=='('|| c==')'|| c==';'||c==':' )
            return true;
        return false;
    }

    private boolean isLetter(char c){
        if(((c>=(char)65)&&(c<=(char)90))||((c>=(char)97)&&(c<=(char)122)))
            return true;
        return false;
    }

    private boolean isDigit(char c){

        int ic = c - '0';
        if((ic>=0)&&(ic<=9))
            return true;
        return false;
    }

    private boolean isReservedWord(String s){
        int n = s.length();
        if(n>6)return false;
        if(s.equals("if")||s.equals("else")||s.equals("end")||s.equals("then")||s.equals("until")||s.equals("repeat")||s.equals("read")||s.equals("write"))
            return true;
        return false;
    }
    public void Scan(){
        String currentToken = "";
        String currentType = "";
        int n = input.length();
        int i =0;
        while(i < n){
            char c = input.charAt(i);

            if(isLetter(c)){
                if(currentType.equals("identifier")){currentToken+=c;}
                else if(currentType.equals("number")){
                    token.add(currentToken);
                    type.add(currentType);
                    currentToken="";
                    currentToken+=c;
                    currentType="identifier";
                }
                else{
                    currentToken="";
                    currentToken+=c;
                    currentType="identifier";
                }
            }//
            else if(isDigit(c)){
                if(currentType.equals("number")){currentToken+=c;}
                else if(currentType.equals("identifier")){
                    if(isReservedWord(currentToken)){currentType = "reserved word";}
                    token.add(currentToken);
                    type.add(currentType);
                    currentToken="";
                    currentToken+=c;
                    currentType="number";
                }
                else{
                    currentToken="";
                    currentToken+=c;
                    currentType="number";
                }
            }//
            else if(c=='=' && currentType.equals("special symbol")){
                currentToken += c;
                token.add(currentToken); type.add(currentType);
                currentToken = "";currentType = "";}
//
            else if(isSpecialSymbol(c)){
                if(currentType.equals("identifier")||currentType.equals("number")){
                    if(isReservedWord(currentToken)){currentType = "reserved word";}
                    token.add(currentToken);type.add(currentType);}
                currentToken="";
                currentToken+=c;currentType="special symbol";
                if(c!=':'){
                    token.add(currentToken);type.add(currentType);
                    currentToken = "";currentType="";}
            }//
            else if(c==' ' || input.charAt(i)=='\n'){//White Space or new Line
                if(currentType.equals("identifier")||currentType.equals("number")){
                    if(isReservedWord(currentToken)){currentType = "reserved word";}
                    token.add(currentToken);type.add(currentType);}
                currentToken = "";currentType = "";
            }//
            else if(c=='{'){//COMMENT
                if(currentType.equals("identifier")||currentType.equals("number")){
                    if(isReservedWord(currentToken)){currentType = "reserved word";}
                    token.add(currentToken);type.add(currentType);}
                currentToken = "";currentType = "";
                char tmp=c;
                while(tmp != '}' && i<(n-1)){
                    i++;
                    tmp=input.charAt(i);
                }
            }
            i++;
        }

    }
    public String[] getToken(){

        String value[] = new String[2];
        value[0] = "";
        value[1] = "";
        if(index<token.size()) {
            value[0] = token.get(index);
            value[1] = type.get(index);
            index++;
        }
        return value;
    }

    public String toString(){
        String s="";
        int n = token.size();
        for(int i = 0;i<n;i++){
            s+=(token.get(i)+" : "+type.get(i)+"\n");
        }
        return s;
    }

}



