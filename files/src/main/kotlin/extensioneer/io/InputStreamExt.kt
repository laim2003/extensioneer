package extensioneer.io

import extensioneer.notNull
import java.io.File
import java.io.InputStream

/**
 * write an [InputStream]s data to a file.
 * @param dest file to be written to
 */
fun InputStream.toFile(dest: String): File {
    val file = File(dest)
    file.parentFile.notNull {
        mkdirs()
    }
    file.createNewFile()
    file.writeBytes(readBytes())
    return file
}