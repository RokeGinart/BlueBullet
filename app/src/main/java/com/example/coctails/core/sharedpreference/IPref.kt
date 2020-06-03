package com.example.coctails.core.sharedpreference

interface IPref {

    fun isFirstInit():Boolean
    fun setFirstInit(value: Boolean)

    fun isFirstEnter():Boolean
    fun setFirstEnter(firstEnter: Boolean)

    fun isAdult():Boolean
    fun setAdult(adult: Boolean)

    fun isAdultSecondCheck():Boolean
    fun setAdultSecondCheck(adultSecond: Boolean)

    fun getVersion(): Float
    fun setVersion(version : Float)
}