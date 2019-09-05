package com.nut2014.kotlintest.test

fun main() {
    println(">>>>>>>>>>>")
    sum(3, 4)
    println(sum1(5, 9))
    incrementX()
    testInterface(Personal())
    testInterface(Animal())

}
fun testInterface(myin: MyInterface){
    myin.run()
    myin.talk("我说话了")
}

/****************函数练习*/
fun sum(a: Int, b: Int) {
    println(a + b)
}

//带参数返回值的函数
fun sum1(a: Int, b: Int): Int {
    return a + b
}

//自动判断返回值
fun sum3(a: Int, b: Int) = a + b

/****************变量*/
//读局部变量使用关键字 val 定义。只能为其赋值一次。
val myNum: Int = 1
val myNum1 = 1 //自动推断类型

//可重新赋值的变量使用 var 关键字：
var myx = 5

fun incrementX() {
    myx += 1
    println(myx)
    //POJO 使用
    var abc: Customer = Customer("李四", "163.com")
    println(abc.name)
    //字符串可内插变量
    println(">>>>$myx")
}

//函数参数设置默认值
fun test2(a: Int = 0, b: String = "") {

}
//过滤list集合 使用 filter val positives = list.filter { it > 0 }

//检测元素是否存在于集合中
//if ("john@example.com" in emailsList) { …… }

//类型判读
/*
when (x) {
    is Foo //-> ……
    is Bar //-> ……
    else   //-> ……
}
*/
//遍历
/*
for ((k, v) in map) {
    println("$k -> $v")
}*/

//使用区间
/*
for (i in 1..100) { …… }  // 闭区间：包含 100
for (i in 1 until 100) { …… } // 半开区间：不包含 100
for (x in 2..10 step 2) { …… }
for (x in 10 downTo 1) { …… }
if (x in 1..10) { …… }*/

/******************基本数据类型*/
/*
Byte
Short
Int
Long
Float
Double
*/

val one = 1 // Int
val threeBillion = 3000000000 // Long
val oneLong = 1L // Long
val oneByte: Byte = 1

//TODO 请注意，与一些其他语言不同，Kotlin 中的数字没有隐式拓宽转换。 例如，具有 Double 参数的函数只能对 Double 值调用，而不能对 Float、 Int 或者其他数字值调用。
//toByte(): Byte
//toShort(): Short
//toInt(): Int
//toLong(): Long
//toFloat(): Float
//toDouble(): Double
//toChar(): Char