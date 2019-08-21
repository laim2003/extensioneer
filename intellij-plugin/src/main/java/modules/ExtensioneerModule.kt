package modules

import com.intellij.icons.AllIcons
import com.intellij.ide.util.projectWizard.ModuleBuilder
import com.intellij.ide.util.projectWizard.ModuleWizardStep
import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.openapi.Disposable
import com.intellij.openapi.module.ModuleType
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel

open class ExtensioneerModule : ModuleType<ExtensioneerModuleBuilder>("gradle") {
    override fun getName(): String {
        return "Extensioneer/Gradle"
    }

    override fun getDescription(): String {
        return "Set up new gradle module with extensioneer"
    }

    override fun getNodeIcon(isOpened: Boolean): Icon {
        return AllIcons.General.CreateNewProject
    }

    override fun createModuleBuilder(): ExtensioneerModuleBuilder {
        return ExtensioneerModuleBuilder()
    }
}

class ExtensioneerModuleBuilder : ModuleBuilder() {
    override fun getModuleType(): ModuleType<*> {
        return ExtensioneerModule()
    }

    override fun getCustomOptionsStep(context: WizardContext?, parentDisposable: Disposable?): ModuleWizardStep? {
        return ExtensioonerWizardStep()
    }

    class ExtensioonerWizardStep : ModuleWizardStep() {
        override fun updateDataModel() {

        }

        override fun getComponent(): JComponent {
            return JLabel("Work in progress. Not available in current version!")
        }

    }
}