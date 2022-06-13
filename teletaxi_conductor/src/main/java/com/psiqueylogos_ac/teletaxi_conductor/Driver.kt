package com.psiqueylogos_ac.teletaxi_conductor

import com.psiqueylogos_ac.teletaxi_lib.MappeableData

class Driver : MappeableData() {
    @Excluding
    var userId = ""
    var messageToken = ""
}