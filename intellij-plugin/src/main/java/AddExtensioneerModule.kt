import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class AddExtensioneerModule : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        print("ADding new module")
    }
}