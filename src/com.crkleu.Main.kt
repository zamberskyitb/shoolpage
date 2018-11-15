import Pages.*
import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.js.*
import org.khronos.webgl.Float32Array
import org.khronos.webgl.WebGLProgram
import org.khronos.webgl.WebGLRenderingContext
import org.w3c.dom.Attr
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.get
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.*

fun main(vararg args:String){
    window.setTimeout({
        document.getElementById("main")!!.block {
            textContent = ""
            val page = getAttribute("page")
            append {
                    //100*100
                    div {
                        id = "all"
                        div {
                            id = "header"
                            header {
                                h1 {
                                    run {
                                        onClickFunction = {
                                            window.location.href = "index.html"
                                        }
                                        id = "title"
                                    }
                                    +"Počítačová Grafika"
                                }
                            }
                            nav {
                                a {
                                    run {
                                        classes += "navA"
                                        href = "index.html"
                                    }
                                    +"Úvodní stráka"
                                }
                                for (i in 2..5)
                                    a {
                                        run {
                                            classes += "navA"
                                            href = if (i == 5) {
                                                "https://github.com/zamberskyitb/shoolpage"
                                            } else {
                                                "page$i.html"
                                            }
                                        }
                                        +when (i) {
                                            2 -> "Rasterizace"
                                            3 -> "Raytracing"
                                            4 -> "Galerie"
                                            5 -> "Github"
                                            else -> "Stránka $i"
                                        }
                                    }
                            }
                        }
                        div {
                            id = "rest"
                            when (valueOf(page!!)) {

                                INDEX -> genIndexHtml()
                                PAGE2 -> genPage2Html()
                                PAGE3 -> genPage3Html(this@block)
                                PAGE4 -> genPage4Html()
                            }
                        }
                        footer {
                            p { text("Vytvořil: Vojtěch Žamberský") }
                            br { }
                            p {
                                text("Kontakt: ")
                                a {
                                    href = "mailto:zamberskyv.02@spst.eu"
                                    text("zamberskyv.02@spst.eu")
                                }
                            }

                        }
                    }
                    canvas {
                        onClickFunction = {
                            window.location.href = "index.html"
                        }
                        id = "logo"
                        height = "100px"
                        width = "100px"
                        attributes["comment"] = "Ano, toto teoreticky porušuje zadání, jelikož tam bylo napsáno \"formát png\", ale není to takto lepší?"
                    }
                    style {

                        text(""" body{
                        background-color: #9999ff
                        font-family: sans-serif
                    } .galeryColumn{
                        display: table-cell
                        width: 30%
                    } img{
                        width:100%
                        border-radius:30px
                    } canvas{
                        cursor: pointer
                        position:fixed
                        margin:0
                        left:5px
                        top:5px
                    } #all{
                        margin: 0
                        left: 0
                        right: 0
                        width: 100%
                        position: absolute
                        top: -10px
                    } #header{
                        margin: 0
                        left: 0
                        right: 0
                        width: 100%
                        border-radius: 10px
                        background-color: #cccccc
                    } footer{
                        position: fixed
                        border-radius: 10px
                        padding-top: 5px
                        padding-bottom: 10px
                        width: 100%
                        bottom: -10px
                        text-align: center
                        background-color: #777777
                    } #rest {
                        border-radius: 10px
                        margin-bottom: 6%
                        margin-top: 3%
                        padding: 2%
                        padding-bottom: 3%
                        margin-left: 7%
                        margin-right: 7%
                        background-color: #ffffff11
                    } footer p {
                        display: inline
                    } header {
                        text-align: center
                        font-size: 20px
                        background-color: #777777
                        padding-top: 10px
                        border-radius: 10px
                    } nav {
                        width: 100%
                    } #warning {
                        background-color: #ff0000
                        left:0
                        top:0
                        cursor:pointer
                        color:#ffffff
                        width:100%
                        height:100%
                        position: fixed
                    } #warning p {
                        text-align: center
                        font-weight:900
                    } a {
                        margin-top: 5px
                        margin-bottom: 5px
                        color: #111111
                        text-decoration: none
                        padding: 3px
                        border-radius: 20px
                        display: inline-block
                        text-align: center
                        width: 17%
                    } #title{
                        margin: 0
                        cursor: pointer
                    } h1 {
                        color: #111111
                        padding: 1%
                        text-align: center;
                    } """.trimMargin().replace("""
                    """, """;
                    """))
                    }
                    /*div {
                        id = "warning"
                        onClickFunction = {
                            (it.currentTarget as HTMLElement).style.display = "none"
                        }
                        h1 {
                            for (i in 0..20)
                                +"Varování! "
                        }
                        p {
                            +"tato stránka je wip, prázdnota této stránky vám může způsobit psychiockou ujmu, vstupte jen na vlastní nebezpečí"
                        }
                    }*/
                }
        window.setTimeout({
                    val c: dynamic = document.getElementById("logo")
                    val a = document.getElementsByClassName("navA")
                    for (aa in 0 until a.length) {
                        var isMouseOverNow = false
                        var aaa = (a.get(aa) as HTMLElement)
                        var alpha = 0
                        aaa.onmouseenter = {
                            isMouseOverNow = true
                            Unit
                        }
                        aaa.onmouseleave = {
                            isMouseOverNow = false
                            Unit
                        }
                        window.setInterval({
                            alpha = max(min((alpha + if (isMouseOverNow) {
                                1
                            } else {
                                -1
                            }), 0x33), 0)
                            aaa.style.backgroundColor = "#000000" + if (alpha < 16) {
                                "0"
                            } else {
                                ""
                            } + alpha.toString(16)
                        }, 5)
                    }
                    var isMouseOverNow = false
                    c.onmouseenter = { isMouseOverNow = true }
                    c.onmouseleave = { isMouseOverNow = false }

                    (c.getContext("webgl2").unsafeCast<WebGLRenderingContext>()).run {
                        viewport(0, 0, (c as HTMLElement).clientWidth, c.clientHeight)
                        clearColor(0f, 0f, 0f, 0f)
                        enable(WebGLRenderingContext.DEPTH_TEST)
                        val shaderBuilder: WebGLRenderingContext.(WebGLProgram, Int, String) -> Unit = { program, shaderType, shaderSource ->
                            val sha = createShader(shaderType)
                            shaderSource(sha, shaderSource)
                            compileShader(sha)
                            console.log(getShaderInfoLog(sha))
                            attachShader(program, sha)
                            deleteShader(sha)
                        }
                        val prog = createProgram()!!
                        shaderBuilder(prog, WebGLRenderingContext.VERTEX_SHADER, """#version 300 es
                    layout(location=0) in vec3 pos;
                    uniform mat4 translate;
                    uniform mat4 rotateDefault;
                    uniform mat4 rotateInstanced[4];
                    uniform mat4 rotateHover;
                    layout(location=1) in vec3 col;
                    out vec3 color;
                    float calcBrightness(){
                        switch(gl_InstanceID){
                            case 0:
                                return 0.5;
                            case 2:
                                return 1.0;
                        }
                        return 0.75;
                    }
                    void main(){
                        float brightness = calcBrightness();
                        color = vec3(col.x*brightness,col.y*brightness,col.z*brightness);
                        gl_Position = translate*rotateHover*rotateDefault*rotateInstanced[gl_InstanceID]*vec4(pos,1.0);
                    }
                    """.trimMargin())
                        shaderBuilder(prog, WebGLRenderingContext.FRAGMENT_SHADER, """#version 300 es
                    precision mediump float;
                    in vec3 color;
                    out vec4 fcolor;
                    void main(){
                        fcolor = vec4(color,1.0);
                    }
                    """.trimMargin())
                        linkProgram(prog)
                        console.log(getProgramInfoLog(prog))
                        useProgram(prog)

                        val vertBuffer = createBuffer()
                        val floorheightf = -0.3f
                        val towerheightf = 0.5f
                        val towerheightfull = 0.65f
                        val gateheightfull = 0.2125f
                        val gateheightf = 0.1325f
                        val doorheightf = -0.05f
                        val towerfarf = 0.4f
                        val towernearf = 0.2f
                        val gatenearf = 0.05f
                        val gatedepthnearf = 0.25f
                        val gatedepthfarf = 0.35f

                        val towershade = 0.6f
                        val towershadebottom = 0.5f
                        val doorinnershade = 0.4f
                        val vertecies = Float32Array(arrayOf(-0.5f, -0.65f, 0.5f, 0f, 0.85f, 0f,
                                0.5f, floorheightf, 0.5f, 0f, 0.85f, 0f,
                                0.5f, -0.65f, 0.5f, 0f, 0.85f, 0f,
                                -0.5f, -0.65f, 0.5f, 0f, 0.85f, 0f,
                                -0.5f, floorheightf, 0.5f, 0f, 0.85f, 0f,
                                0.5f, floorheightf, 0.5f, 0f, 0.85f, 0f,
                                -0.5f, floorheightf, 0.5f, 0f, 0.95f, 0f,
                                0.5f, floorheightf, 0.5f, 0f, 0.95f, 0f,
                                0f, floorheightf, 0f, 0f, 0.95f, 0f,

                                -towerfarf, floorheightf, towerfarf, towershade, towershade, towershade,
                                -towernearf, towerheightf, towerfarf, towershade, towershade, towershade,
                                -towernearf, floorheightf, towerfarf, towershade, towershade, towershade,
                                -towerfarf, floorheightf, towerfarf, towershade, towershade, towershade,
                                -towerfarf, towerheightf, towerfarf, towershade, towershade, towershade,
                                -towernearf, towerheightf, towerfarf, towershade, towershade, towershade,

                                towerfarf, floorheightf, towerfarf, towershade, towershade, towershade,
                                towernearf, towerheightf, towerfarf, towershade, towershade, towershade,
                                towernearf, floorheightf, towerfarf, towershade, towershade, towershade,
                                towerfarf, floorheightf, towerfarf, towershade, towershade, towershade,
                                towerfarf, towerheightf, towerfarf, towershade, towershade, towershade,
                                towernearf, towerheightf, towerfarf, towershade, towershade, towershade,

                                -towerfarf, floorheightf, -towernearf, towershade, towershade, towershade,
                                -towernearf, towerheightf, -towernearf, towershade, towershade, towershade,
                                -towernearf, floorheightf, -towernearf, towershade, towershade, towershade,
                                -towerfarf, floorheightf, -towernearf, towershade, towershade, towershade,
                                -towerfarf, towerheightf, -towernearf, towershade, towershade, towershade,
                                -towernearf, towerheightf, -towernearf, towershade, towershade, towershade,

                                towerfarf, floorheightf, -towernearf, towershade, towershade, towershade,
                                towernearf, towerheightf, -towernearf, towershade, towershade, towershade,
                                towernearf, floorheightf, -towernearf, towershade, towershade, towershade,
                                towerfarf, floorheightf, -towernearf, towershade, towershade, towershade,
                                towerfarf, towerheightf, -towernearf, towershade, towershade, towershade,
                                towernearf, towerheightf, -towernearf, towershade, towershade, towershade,

                                0.5f, towerheightf, 0.5f, towershade, towershade, towershade,
                                0.5f, towerheightfull, 0.5f, towershade, towershade, towershade,
                                0.1f, towerheightf, 0.5f, towershade, towershade, towershade,
                                0.5f, towerheightf, 0.5f, towershade, towershade, towershade,
                                0.1f, towerheightfull, 0.5f, towershade, towershade, towershade,
                                0.1f, towerheightf, 0.5f, towershade, towershade, towershade,
                                0.5f, towerheightf, 0.5f, towershade, towershade, towershade,
                                0.3f, towerheightfull, 0.5f, towershade, towershade, towershade,
                                0.1f, towerheightf, 0.5f, towershade, towershade, towershade,
                                0.5f, towerheightf, 0.5f, towershadebottom, towershadebottom, towershadebottom,
                                0.3f, towerheightf, 0.3f, towershadebottom, towershadebottom, towershadebottom,
                                0.1f, towerheightf, 0.5f, towershadebottom, towershadebottom, towershadebottom,

                                -0.5f, towerheightf, 0.5f, towershade, towershade, towershade,
                                -0.5f, towerheightfull, 0.5f, towershade, towershade, towershade,
                                -0.1f, towerheightf, 0.5f, towershade, towershade, towershade,
                                -0.5f, towerheightf, 0.5f, towershade, towershade, towershade,
                                -0.1f, towerheightfull, 0.5f, towershade, towershade, towershade,
                                -0.1f, towerheightf, 0.5f, towershade, towershade, towershade,
                                -0.5f, towerheightf, 0.5f, towershade, towershade, towershade,
                                -0.3f, towerheightfull, 0.5f, towershade, towershade, towershade,
                                -0.1f, towerheightf, 0.5f, towershade, towershade, towershade,
                                -0.5f, towerheightf, 0.5f, towershadebottom, towershadebottom, towershadebottom,
                                -0.3f, towerheightf, 0.3f, towershadebottom, towershadebottom, towershadebottom,
                                -0.1f, towerheightf, 0.5f, towershadebottom, towershadebottom, towershadebottom,

                                0.5f, towerheightf, -0.1f, towershade, towershade, towershade,
                                0.5f, towerheightfull, -0.1f, towershade, towershade, towershade,
                                0.1f, towerheightf, -0.1f, towershade, towershade, towershade,
                                0.5f, towerheightf, -0.1f, towershade, towershade, towershade,
                                0.1f, towerheightfull, -0.1f, towershade, towershade, towershade,
                                0.1f, towerheightf, -0.1f, towershade, towershade, towershade,
                                0.5f, towerheightf, -0.1f, towershade, towershade, towershade,
                                0.3f, towerheightfull, -0.1f, towershade, towershade, towershade,
                                0.1f, towerheightf, -0.1f, towershade, towershade, towershade,
                                0.5f, towerheightf, -0.1f, towershadebottom, towershadebottom, towershadebottom,
                                0.3f, towerheightf, -0.3f, towershadebottom, towershadebottom, towershadebottom,
                                0.1f, towerheightf, -0.1f, towershadebottom, towershadebottom, towershadebottom,

                                -0.5f, towerheightf, -0.1f, towershade, towershade, towershade,
                                -0.5f, towerheightfull, -0.1f, towershade, towershade, towershade,
                                -0.1f, towerheightf, -0.1f, towershade, towershade, towershade,
                                -0.5f, towerheightf, -0.1f, towershade, towershade, towershade,
                                -0.1f, towerheightfull, -0.1f, towershade, towershade, towershade,
                                -0.1f, towerheightf, -0.1f, towershade, towershade, towershade,
                                -0.5f, towerheightf, -0.1f, towershade, towershade, towershade,
                                -0.3f, towerheightfull, -0.1f, towershade, towershade, towershade,
                                -0.1f, towerheightf, -0.1f, towershade, towershade, towershade,
                                -0.5f, towerheightf, -0.1f, towershadebottom, towershadebottom, towershadebottom,
                                -0.3f, towerheightf, -0.3f, towershadebottom, towershadebottom, towershadebottom,
                                -0.1f, towerheightf, -0.1f, towershadebottom, towershadebottom, towershadebottom,


                                -gatenearf, floorheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf, floorheightf, gatedepthfarf, towershade, towershade, towershade,
                                -gatenearf, floorheightf, gatedepthfarf, towershade, towershade, towershade,
                                -gatenearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                gatenearf, floorheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf, floorheightf, gatedepthfarf, towershade, towershade, towershade,
                                gatenearf, floorheightf, gatedepthfarf, towershade, towershade, towershade,
                                gatenearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                -gatenearf, floorheightf, -gatedepthnearf, towershade, towershade, towershade,
                                -towernearf, gateheightf, -gatedepthnearf, towershade, towershade, towershade,
                                -towernearf, floorheightf, -gatedepthnearf, towershade, towershade, towershade,
                                -gatenearf, floorheightf, -gatedepthnearf, towershade, towershade, towershade,
                                -gatenearf, gateheightf, -gatedepthnearf, towershade, towershade, towershade,
                                -towernearf, gateheightf, -gatedepthnearf, towershade, towershade, towershade,

                                gatenearf, floorheightf, -gatedepthnearf, towershade, towershade, towershade,
                                towernearf, gateheightf, -gatedepthnearf, towershade, towershade, towershade,
                                towernearf, floorheightf, -gatedepthnearf, towershade, towershade, towershade,
                                gatenearf, floorheightf, -gatedepthnearf, towershade, towershade, towershade,
                                gatenearf, gateheightf, -gatedepthnearf, towershade, towershade, towershade,
                                towernearf, gateheightf, -gatedepthnearf, towershade, towershade, towershade,

                                gatenearf, doorheightf, gatedepthfarf, towershade, towershade, towershade,
                                -gatenearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -gatenearf, doorheightf, gatedepthfarf, towershade, towershade, towershade,
                                gatenearf, doorheightf, gatedepthfarf, towershade, towershade, towershade,
                                gatenearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -gatenearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                gatenearf, floorheightf, gatedepthnearf, doorinnershade, doorinnershade, doorinnershade,
                                gatenearf, floorheightf, gatedepthfarf, doorinnershade, doorinnershade, doorinnershade,
                                gatenearf, doorheightf, gatedepthnearf, doorinnershade, doorinnershade, doorinnershade,
                                gatenearf, floorheightf, gatedepthfarf, doorinnershade, doorinnershade, doorinnershade,
                                gatenearf, doorheightf, gatedepthfarf, doorinnershade, doorinnershade, doorinnershade,
                                gatenearf, doorheightf, gatedepthnearf, doorinnershade, doorinnershade, doorinnershade,

                                -gatenearf, floorheightf, gatedepthnearf, doorinnershade, doorinnershade, doorinnershade,
                                -gatenearf, floorheightf, gatedepthfarf, doorinnershade, doorinnershade, doorinnershade,
                                -gatenearf, doorheightf, gatedepthnearf, doorinnershade, doorinnershade, doorinnershade,
                                -gatenearf, floorheightf, gatedepthfarf, doorinnershade, doorinnershade, doorinnershade,
                                -gatenearf, doorheightf, gatedepthfarf, doorinnershade, doorinnershade, doorinnershade,
                                -gatenearf, doorheightf, gatedepthnearf, doorinnershade, doorinnershade, doorinnershade,

                                towernearf, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                towernearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf * 0.75f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                towernearf * 0.5f, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                towernearf * 0.5f, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf * 0.75f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                towernearf * 0.5f, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                towernearf * 0.5f, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf * 0.25f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                0f, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                0f, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                towernearf * 0.25f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                -towernearf, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf * 0.75f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                -towernearf * 0.5f, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf * 0.5f, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf * 0.75f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                -towernearf * 0.5f, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf * 0.5f, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf * 0.25f, gateheightf, gatedepthfarf, towershade, towershade, towershade,

                                0f, gateheightfull, gatedepthfarf, towershade, towershade, towershade,
                                0f, gateheightf, gatedepthfarf, towershade, towershade, towershade,
                                -towernearf * 0.25f, gateheightf, gatedepthfarf, towershade, towershade, towershade
                        ))
                        bindBuffer(WebGLRenderingContext.ARRAY_BUFFER, vertBuffer)
                        bufferData(WebGLRenderingContext.ARRAY_BUFFER, vertecies, WebGLRenderingContext.STATIC_DRAW)
                        val x90deg = toRadians(90f)
                        val x30deg = toRadians(30f)
                        for (i in 0..3)
                            uniformMatrix4fv(getUniformLocation(prog, "rotateInstanced[$i]"), false, yRotate(x90deg * i + x30deg))
                        uniformMatrix4fv(getUniformLocation(prog, "translate"), false, translate(0f, 0f, .3f))
                        enableVertexAttribArray(0)
                        enableVertexAttribArray(1)
                        vertexAttribPointer(0, 3, WebGLRenderingContext.FLOAT, false, 24, 0)
                        vertexAttribPointer(1, 3, WebGLRenderingContext.FLOAT, false, 24, 12)
                        var angle = .1f
                        var angle2 = 0f
                        val gl: dynamic = this
                        window.setInterval({
                            document.getElementById("thatiframe")?.run {
                                attributes.get("width")?.value = "${(this@block.clientWidth * 0.6 * 0.84).toInt()}px"
                                attributes.get("height")?.value = "${(this@block.clientWidth * 0.45 * 0.84).toInt()}px"
                            }
                            clear(WebGLRenderingContext.COLOR_BUFFER_BIT or WebGLRenderingContext.DEPTH_BUFFER_BIT)
                            uniformMatrix4fv(getUniformLocation(prog, "rotateDefault"), false, yRotate(angle - angle2))
                            uniformMatrix4fv(getUniformLocation(prog, "rotateHover"), false, zRotate(-angle2))
                            if (isMouseOverNow) {
                                angle2 += 0.0025f
                            } else {
                                angle += 0.0025f
                                if (angle2 > 0)
                                    angle2 -= 0.15f
                                while (angle2 < 0)
                                    angle2 += 0.0025f
                            }
                            gl.drawArraysInstanced(WebGLRenderingContext.TRIANGLES, 0, vertecies.length / 6, 4)
                        }, 10)
                    }

                }, 5)
        }

    },1)

}

private fun <R> Element.block(function: Element.() -> R): R = run(function)



private fun DIV.genPage4Html() {
    h1{
        +"GALERIE"
    }
    br{}
    div{
        br {  }
        classes+="galeryColumn"
        img {
            src="castle.png"
        }
    }
    div{
        br {  }
        classes+="galeryColumn"

        img{
            src="aa.png"
        }
    }
    div{
        br {  }
        classes+="galeryColumn"

        img{
            src="anistropic.jpg"
        }
    }
}

private fun DIV.genPage3Html(page: Element) {
    h1{
        +"RAYTRACING"
    }
    br{}
    p{
        +"jedná se o velmi realistickou vykreslovací metodu"
    }
    br{}
    h2{
        +"The Compleat Angler"
    }
    p{
        +"je raytracing demo z roku 1978 (některé zdroje uvádí 1979), čas potřebný pro vyrenderování jediného snímku byl 45 minut, originální rozlišení je 512x512 pixelů"
    }
    iframe {
        style = "align-content: center"
        id="thatiframe"
        width = "${(page.clientWidth*0.6*0.84).toInt()}px"
        height = "${(page.clientWidth*0.45*0.84).toInt()}px"
        src = "https://www.youtube.com/embed/WV4qXzM641o"
        attributes["frameborder"] = "0"
        attributes["allow"]="autoplay; encrypted-media; picture-in-picture"
        attributes["allowfullscreen"]
    }
    br{}
    p{
        +"technologie raytracing se ve filmovém průmyslu používá desítky let (výše uvedené demo všemu předcházelo) a "
        strong {
            +"pomalu"
        }
        +" se dostává i do moderních her"
    }

    br{}
    p{
        +"pro příklady se podívejte na téměř libovolný moderní film"
    }
}

private fun DIV.genPage2Html() {
    h1{
        +"RASTERIZACE"
    }
    br{}
    p{
        +"je to matematická konverze vektorové grafiky na rastrovou, používa se zcela všude, prakticky si nelze sednout k počítači, aniž byste sáhli na prvek vykreslený pomocí rasterizace"
    }
    br{}
    h2{
        +"Akcelerace"
    }
    p{
        +"Obecně se jedná o zpracování programu za použití optimalizovaného hardware, v tomto případě se jedná o vykreslování pomocí grafické karty. Využívá se hlavně je hrách. Ve hrách se používá i mnoho triků pro zlepšení kvality"
    }
    br {  }
    h2{
        +"nějaké triky"
    }
    br {  }
    h3 {
        +"Mipmapy"
    }
    br {  }
    p {
        +"Jsou menší verze originálních textur, které vypadají lépe, když jsou promítnuty na vzdálený objekt (ve srovnáním s původní texturou)"
    }

    br {  }
    h3 {
        +"Anistropic filtering"
    }
    br {  }
    p {
        +"podobné mipmapám, ale vypadají lépe při promítnutí na nakloněnou rovinu podle osy x nebo y"
    }
    img{
        src="anistropic.jpg"
    }

    br {  }
    h3 {
        +"Occulsion culling"
    }
    br {  }
    p {
        +"efektivně nalezne a následně vyjme zakryté objekty z konečného vykreslení pro zvýšení výkonu"
    }
    br {  }
    br {  }
    p {
        +"jinými triky lze také dosáhnout odrazu \"světla\", osvětlení objektů, nebo stínů"
    }

}

private fun DIV.genIndexHtml() {
    h1 {
      +"CGI"
    }
    br {  }
    div{
        +"Existuje 45 let a bez této technologie bychom si dnešní počítače nedokázali představit, je úplně všude (výsledky nalezneme i na obalu od čoko-tyčinky, kterou snědlo hladové dítě v africe)"
        br {  }
        br {  }
        strong {
            a {
                run {
                    style = "width:48%"
                    classes += "navA"
                    href = "page2.html"

                }
                +"Rasterizace"
            }
            +" | "
            a {
                run {
                    style = "width:48%"
                    classes += "navA"
                    href = "page3.html"

                }
                +"Raytracing"
            }
        }
        br {  }
        h2{
            +"Další post-processing technologie:"
        }
        br {  }
        h3 {
            +"Antialiasing"
        }
        br {  }
        p {
            +"Přeloženo jako \"vyhlazování\". Je to technologie, která ostré pixelovité hrany doplní průhlednými pixely a hrana pak vypadá rovnější"
        }
        img{
            src="aa.png"
        }
        br{}
        p{
            +"víc jich neznám..."
        }
    }
}

enum class Pages {
    INDEX,
    PAGE2,
    PAGE3,
    PAGE4
}

fun translate(x: Float, y: Float, z: Float): Array<Float> = arrayOf(1f,0f,0f,x,
        0f,1f,0f,y,
        0f,0f,1f,z,
        0f,0f,0f,1f)

fun toRadians(i: Float): Float = i* PI.toFloat()/180f

fun yRotate(angle:Float):Array<Float> = arrayOf(cos(angle),0f,-sin(angle),0f,
        0f,1f,0f,0f,
        sin(angle),0f,cos(angle),0f,
        0f,0f,0f,1f)

fun zRotate(angle:Float):Array<Float> = arrayOf(cos(angle),-sin(angle),0f,0f,
        sin(angle),cos(angle),0f,0f,
        0f,0f,1f,0f,
        0f,0f,0f,1f)