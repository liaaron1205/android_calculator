import java.util.*

class Calculator {
    private var display = Vector<String>()
    private var index = 0
    private var variables = mutableMapOf<String, Double>()
    fun moveRight(){
        index = display.size.coerceAtMost(index + 1)
    }
    fun moveLeft(){
        index = 0.coerceAtLeast(index - 1)
    }
    fun type(c : String){
        display.add(index, c)
        index++
    }
    fun del(){
        if (index == 0) return
        index--
        display.removeAt(index)
    }
    fun clear(){
        display.clear()
        index = 0
    }
    fun eval(key: String = "ans"){
        var str = ""
        display.forEach{
            str+=it
        }
        try{
            val ans = Parser(Scanner(str).scan()).parse()!!.evaluate(variables)
            store(key, ans)
            clear()
            val stringVer = if (Math.floor(ans)==ans) ans.toInt().toString() else ans.toBigDecimal().toPlainString()
            stringVer.forEach {
                type(it.toString())
            }
        }
        catch (e: Exception){
            throw e;
        }
    }
    fun getDisplay(b : Boolean): String{
        var ret = ""
        for (i in 0..index-1) ret+= display[i]
        ret += if (b) "|" else " "
        for (i in index..display.size-1) ret +=display[i]
        return ret
    }
    fun store(key : String, value : Double){
        variables[key] = value
    }



}