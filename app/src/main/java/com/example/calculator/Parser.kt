import java.util.*

/*
expr -> term
expr -> term PLUS expr
expr -> term MINUS expr
term -> factor
term -> factor STAR term
term -> factor SLASH term
term -> factor PCT term
factor -> VAR
factor -> NUM
factor -> LPAREN expr RPAREN
 */

class Parser (private var tokenList : Vector<Scanner.Token>) {
    private var curToken : Scanner.Token? = if (!tokenList.isEmpty()) tokenList[0] else null
    private var curIdx : Int = 0

    private fun advance(){
        curToken = if (curIdx+1 < tokenList.size) tokenList[++curIdx] else null
    }

    fun parse() : TreeNode? {
        if (curToken == null) return null
        val ret = expr()
        if (curToken != null) throw Exception("Invalid Expression!")
        return ret?: throw Exception("Invalid Expression!")
    }

    private fun expr() : TreeNode? {
        if (curToken == null) throw Exception("Invalid Expression!")
        var ret = term()
        while (curToken!=null && (curToken!!.type == Scanner.Token.TokenType.PLUS  || curToken!!.type == Scanner.Token.TokenType.MINUS)){
            ret = when(curToken!!.type){
                Scanner.Token.TokenType.PLUS -> {
                    advance()
                    TreeNode(TreeNode.NodeType.ADD, "", ret, term())
                }
                Scanner.Token.TokenType.MINUS -> {
                    advance()
                    TreeNode(TreeNode.NodeType.SUB, "", ret, term())
                }
                else -> throw Exception("Invalid Expression!")
            }
        }
        return ret
    }

    private fun term() : TreeNode? {
        if (curToken == null) throw Exception("Invalid Expression!")
        var ret = factor()
        while (curToken!=null && (curToken!!.type == Scanner.Token.TokenType.STAR  || curToken!!.type == Scanner.Token.TokenType.SLASH || curToken!!.type == Scanner.Token.TokenType.PCT)){
            ret = when(curToken!!.type){
                Scanner.Token.TokenType.STAR -> {
                    advance()
                    TreeNode(TreeNode.NodeType.MULT, "", ret, factor())
                }
                Scanner.Token.TokenType.SLASH -> {
                    advance()
                    TreeNode(TreeNode.NodeType.DIV, "", ret, factor())
                }
                Scanner.Token.TokenType.PCT -> {
                    advance()
                    TreeNode(TreeNode.NodeType.MOD, "", ret, factor())
                }
                else -> throw Exception("Invalid Expression!")
            }
        }
        return ret
    }

    private fun factor() : TreeNode? {
        if (curToken == null) throw Exception("Invalid Expression!")
        when(curToken!!.type){
            Scanner.Token.TokenType.NUM -> {
                val ret = TreeNode(TreeNode.NodeType.NUM, curToken!!.tokenVal)
                advance()
                return ret
            }
            Scanner.Token.TokenType.VAR -> {
                val ret = TreeNode(TreeNode.NodeType.VAR, curToken!!.tokenVal)
                advance()
                return ret
            }
            Scanner.Token.TokenType.LPAREN -> {
                advance()
                val ret = expr()
                if (curToken==null || curToken!!.type != Scanner.Token.TokenType.RPAREN) throw Exception("Invalid Expression!")
                advance()
                return ret
            }
            Scanner.Token.TokenType.MINUS -> {
                advance()
                var ret = factor()
                ret = TreeNode(TreeNode.NodeType.SUB, "", TreeNode(TreeNode.NodeType.NUM, "0"), ret)
                return ret
            }
            else -> throw Exception("Invalid Expression!")
        }
    }


    class TreeNode(private var type : NodeType, private var value : String, private var leftChild : TreeNode? = null, private var rightChild : TreeNode? = null){
        fun evaluate(variables : Map<String, Double>? = null) : Double {
            when (type){
                NodeType.ADD -> {
                    return leftChild!!.evaluate(variables) + rightChild!!.evaluate(variables)
                }
                NodeType.SUB -> {
                    return leftChild!!.evaluate(variables) - rightChild!!.evaluate(variables)
                }
                NodeType.MULT -> {
                    return leftChild!!.evaluate(variables) * rightChild!!.evaluate(variables)
                }
                NodeType.DIV -> {
                    return leftChild!!.evaluate(variables) / rightChild!!.evaluate(variables)
                }
                NodeType.MOD -> {
                    return leftChild!!.evaluate(variables) % rightChild!!.evaluate(variables)
                }
                NodeType.VAR -> {
                    if (!variables!!.containsKey(value)) throw Exception("Undefined variable!")
                    return variables[value]!!
                }
                NodeType.NUM -> {
                    return value.toDouble()
                }
            }
        }

        override fun toString(): String {
            when (type){
                NodeType.ADD -> {
                    return "( " + leftChild!!.toString() + " + " + rightChild.toString() + " )"
                }
                NodeType.SUB -> {
                    return "( " + leftChild!!.toString() + " - " + rightChild.toString() + " )"
                }
                NodeType.MULT -> {
                    return "( " + leftChild!!.toString() + " * " + rightChild.toString() + " )"
                }
                NodeType.DIV -> {
                    return "( " + leftChild!!.toString() + " / " + rightChild.toString() + " )"
                }
                NodeType.MOD -> {
                    return "( " + leftChild!!.toString() + " % " + rightChild.toString() + " )"
                }
                NodeType.VAR -> {
                    return value
                }
                NodeType.NUM -> {
                    return value
                }
            }
        }

        enum class NodeType{
            ADD, SUB, MULT, DIV, MOD, VAR, NUM
        }
    }
}