package module.handlebar

import module.pallet.Pallet
import module.settings.Settings
import java.awt.BorderLayout
import java.awt.Font
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.system.exitProcess

class HandleBar(private val settings: Settings, private val pallet: Pallet) : JPanel() {
    private var initialClick: Point? = null
    val title: JLabel = JLabel(settings.getSettingOfString("handleBar.title.default"))
    private val exitButton: JButton = JButton(settings.getSettingOfString("handleBar.exitButton.text"))

    init {
        layout = BorderLayout()
        title.horizontalAlignment = JLabel.CENTER
        add(title, BorderLayout.CENTER)
        title.font = Font(settings.getSettingOfString("handleBar.title.font"), Font.BOLD, settings.getSettingOfInt("handleBar.title.fontSize"))
        preferredSize = java.awt.Dimension(0, settings.getSettingOfInt("handleBar.height"))
        add(exitButton, BorderLayout.WEST)
        exitButton.font = Font(settings.getSettingOfString("handleBar.exitButton.font"), Font.BOLD, settings.getSettingOfInt("handleBar.exitButton.fontSize"))
        exitButton.preferredSize = java.awt.Dimension(settings.getSettingOfInt("handleBar.exitButton.width"), settings.getSettingOfInt("handleBar.exitButton.height"))
        exitButton.addActionListener {
            exitProcess(0)
        }
        setPallet()
    }

    private fun setPallet(){
        background = pallet.getPallet("handleBar.background")
        foreground = pallet.getPallet("handleBar.foreground")
        title.background = pallet.getPallet("handleBar.title.background")
        title.foreground = pallet.getPallet("handleBar.title.foreground")
        exitButton.background = pallet.getPallet("handleBar.exitButton.background")
        exitButton.foreground = pallet.getPallet("handleBar.exitButton.foreground")
    }

    fun addMovingListener(target: JFrame){
        addMouseListener(object : MouseAdapter() {
            override fun mousePressed(e: MouseEvent) {
                super.mousePressed(e)
                initialClick = e.point
            }

        })
        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseDragged(e: MouseEvent) {
                super.mouseDragged(e)
                initialClick?.let {
                    val current = e.point
                    val jf = target.location
                    val xMoved = current.x - it.x
                    val yMoved = current.y - it.y
                    val x = jf.x + xMoved
                    val y = jf.y + yMoved
                    target.location = Point(x, y)
                }
            }
        })
    }
}