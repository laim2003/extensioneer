package extensioneer.files

import extensioneer.notNull
import java.io.File
import java.io.InputStream

fun InputStream.toFile(dest: String): File {
    val file = File(dest)
    file.parentFile.notNull {
        mkdirs()
    }
    file.createNewFile()
    file.writeBytes(readBytes())
    return file
}