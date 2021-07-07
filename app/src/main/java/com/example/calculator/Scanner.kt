import java.util.*

class Scanner (private var expr: String){
    fun scan(): Vector<Token> {
        val tokenList = Vector<Token>()
        val dfa = DFA()
        var value = ""
        expr.forEach {
            while(!dfa.transition(it)){
                if (dfa.accept()){
                    tokenList.add(Token(value))
                    value = ""
                    dfa.reset()
                }
                else throw Exception("Invalid Expression!")
            }
            value += it
        }
        if (dfa.accept()){
            tokenList.add(Token(value))
            value = ""
            dfa.reset()
        }
        else throw Exception("Invalid Expression!")
        return tokenList
    }

    private class DFA{
        var curState = DFAState.START
        enum class DFAState{
            START, INT, DOT, NUM, PLUS, MINUS, STAR, SLASH, PCT, LPAREN, RPAREN, ANSA, ANSN, ANSS, VAR
        }
        fun reset(){
            curState = DFAState.START
        }
        fun accept(): Boolean {
            return curState!=DFAState.START && curState!=DFAState.ANSA && curState!=DFAState.ANSN
        }
        fun transition(input: Char) : Boolean{
            when(curState){
                DFAState.START -> when{
                    input.isDigit() -> curState = DFAState.INT
                    "ABXYZ".contains(input) -> curState = DFAState.VAR
                    else -> curState = when(input){
                        '.' -> DFAState.DOT
                        '+' -> DFAState.PLUS
                        '-' -> DFAState.MINUS
                        '*' -> DFAState.STAR
                        '/' -> DFAState.SLASH
                        '%' -> DFAState.PCT
                        '(' -> DFAState.LPAREN
                        ')' -> DFAState.RPAREN
                        'a' -> DFAState.ANSA
                        else-> return false
                    }
                }
                DFAState.INT -> curState = when{
                    input.isDigit() -> DFAState.INT
                    input=='.' -> DFAState.DOT
                    else -> return false
                }
                DFAState.DOT -> if (input.isDigit()) curState = DFAState.NUM else return false
                DFAState.NUM -> if (input.isDigit()) curState = DFAState.NUM else return false
                DFAState.ANSA -> if (input=='n') curState = DFAState.ANSN else return false
                DFAState.ANSN -> if (input=='s') curState = DFAState.ANSS else return false
                else -> return false
            }
            return true
        }
    }

    data class Token (val tokenVal: String){
        val type = when(tokenVal){
            "+" -> TokenType.PLUS
            "-" -> TokenType.MINUS
            "/" -> TokenType.SLASH
            "*" -> TokenType.STAR
            "%" -> TokenType.PCT
            "(" -> TokenType.LPAREN
            ")" -> TokenType.RPAREN
            "ans" -> TokenType.VAR
            "A" -> TokenType.VAR
            "B" -> TokenType.VAR
            "X" -> TokenType.VAR
            "Y" -> TokenType.VAR
            "Z" -> TokenType.VAR
            else -> TokenType.NUM
        }
        enum class TokenType{
            NUM, PLUS, MINUS, STAR, SLASH, PCT, VAR, LPAREN, RPAREN
        }
    }
}