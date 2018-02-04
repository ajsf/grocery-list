package com.doublea.views

import kotlinx.html.*
import kotlinx.html.stream.createHTML

fun index(header: String): String {

    return createHTML(true).html {
        head {
            title = "Does this thing work?"
        }
        body {
            h4 { +header }
            p { +"If you can see this, it worked" }
        }
    }
}