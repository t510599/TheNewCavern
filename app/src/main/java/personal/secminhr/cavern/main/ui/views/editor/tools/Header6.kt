package personal.secminhr.cavern.main.ui.views.editor.tools

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Looks6
import androidx.compose.runtime.State
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

class Header6(override val text: State<TextFieldValue>) : Tool {
    override val icon = Icons.Default.Looks6
    override val description = "Header 6"
    override val action = {
        val length = oldContent.length
        val beforeCursorContent = oldContent.substring(0 until cursorPos)
        val afterCursorContent = oldContent.substring(cursorPos until length)
        var sharps = "###### "
        val frontNewline = beforeCursorContent.isNotEmpty() && beforeCursorContent.last() != '\n'
        val endNewline = afterCursorContent.isNotEmpty() && afterCursorContent.first() != '\n'
        if(frontNewline) {
            sharps = "\n$sharps"
        }
        if(endNewline) {
            sharps += "\n"
        }
        val offset = sharps.trimEnd('\n').length
        TextFieldValue(text = "$beforeCursorContent$sharps$afterCursorContent",
                TextRange(beforeCursorContent.length + offset)
        )
    }
}