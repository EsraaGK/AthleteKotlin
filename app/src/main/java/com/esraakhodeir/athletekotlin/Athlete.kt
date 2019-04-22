package com.esraakhodeir.athletekotlin

class Athlete {

    var name:String? =null
    var brief:String? =null
    var image:String? =null

    constructor(name:String, brief:String, image:String) {
        this.name = name
        this.brief= brief
        this.image = image
    }
}
