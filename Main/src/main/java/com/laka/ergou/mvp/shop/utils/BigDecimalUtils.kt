package com.laka.ergou.mvp.shop.utils

import android.text.TextUtils
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

/**
 * @Author:summer
 * @Date:2019/1/9
 * @Description: 大整型计算，防止失真
 */
class BigDecimalUtils {
    companion object {
        val mRoundDigit = 2

        /**
         * 乘法
         * */
        fun multi(vararg args: Double): BigDecimal {
            var strArgs: Array<String> = Array(args.size, { i -> args[i].toString() })
            return multi(*strArgs)
        }

        fun multi(vararg args: String): BigDecimal {
            return try {
                var sum: BigDecimal = BigDecimal.ONE
                for (arg in args) {
                    var bigArg = if (TextUtils.isEmpty(arg) || arg == "null" || arg == "NULL") {
                        //乘除运算返回 1，加减运算返回 0
                        BigDecimal("1")
                    } else {
                        BigDecimal(arg)
                    }
                    sum = sum.multiply(bigArg)
                }
                sum
            } catch (e: Exception) {
                e.printStackTrace()
                BigDecimal("0")
            }
        }


        /**
         * 除法
         * */
        fun divi(args1: Double, args2: Double): BigDecimal {
            return divi(args1.toString(), args2.toString())
        }

        fun divi(args1: String, args2: String): BigDecimal {
            return try {
                val result = BigDecimal(args1).divide(BigDecimal(args2), 5, RoundingMode.HALF_DOWN)
                println(result.toString())
                BigDecimal(args1).divide(BigDecimal(args2), 5, RoundingMode.HALF_DOWN)
            } catch (e: Exception) {
                e.printStackTrace()
                BigDecimal("0")
            }
        }

        /**
         * 加法
         * */
        fun add(vararg args: Double): BigDecimal {
            var strArgs: Array<String> = Array(args.size, { i -> args[i].toString() })
            return add(*strArgs)
        }

        fun add(vararg args: String): BigDecimal {
            return try {
                var sum = BigDecimal("0")
                for (arg in args) {
                    var bigArg: BigDecimal = if (TextUtils.isEmpty(arg) || arg == "null" || arg == "NULL") {
                        BigDecimal("0")
                    } else {
                        BigDecimal(arg)
                    }
                    sum = sum.add(bigArg)
                }
                sum
            } catch (e: Exception) {
                e.printStackTrace()
                BigDecimal("0")
            }
        }

        /**
         * 减法
         * */
        fun sub(vararg args: Double): BigDecimal {
            var strArgs: Array<String> = Array(args.size, { i -> args[i].toString() })
            return sub(*strArgs)
        }

        fun sub(vararg args: String): BigDecimal {
            return try {
                var sum: BigDecimal = BigDecimal.ZERO
                for (i in 0 until args?.size) {
                    sum = if (i == 0) {
                        BigDecimal(args[0])
                    } else {
                        sum.subtract(BigDecimal(args[i]))
                    }
                }
                sum
            } catch (e: Exception) {
                e.printStackTrace()
                BigDecimal("0")
            }
        }

        /**
         * 根据传入的舍如模式进行舍入
         * RoundingMode.HALF_UP  ：四舍五入
         * RoundingMode.DOWN   ：向下取整
         */
        private fun roundMode(arg: BigDecimal, mode: RoundingMode): String {
            arg?.setScale(mRoundDigit, mode)
            return numberFormat(arg)
            // return arg.toDouble().toString()
        }

        /**
         * 四舍五入，保留两位小数点
         * */
        fun roundMode(arg: BigDecimal): String {
            return roundMode(arg, RoundingMode.HALF_UP)
        }

        fun roundMode(arg: String): String {
            try {
                return roundMode(BigDecimal(arg), RoundingMode.HALF_UP)
            } catch (e: Exception) {
                return return roundMode(BigDecimal("0"), RoundingMode.HALF_UP)
            }
        }

        /**
         * 数字格式化，将尾部的 0 全部去掉
         * */
        private fun numberFormat(arg: BigDecimal): String {
            return try {
                val numberFormat = NumberFormat.getNumberInstance()
                numberFormat.maximumFractionDigits = 2
                numberFormat.isGroupingUsed = false
                numberFormat.format(arg)
            } catch (e: Exception) {
                "0"
            }
        }

        /**
         * 数字格式化，将尾部的 0 全部去掉
         * */
        fun numberFormat(arg: String): String {
            return try {
                val numberFormat = NumberFormat.getNumberInstance()
                val bigDecimal = BigDecimal(arg)
                numberFormat.maximumFractionDigits = 2
                numberFormat.isGroupingUsed = false
                numberFormat.format(bigDecimal)
            } catch (e: Exception) {
                "0"
            }
        }

        fun numberFormat(arg: Float): String {
            return numberFormat(arg.toString())
        }

        fun numberFormat(arg: Double): String {
            return numberFormat(arg.toString())
        }

    }
}