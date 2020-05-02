package extensioneer.files


import extensioneer.isNullWithResult
import extensioneer.notNullWithResult
import java.io.File
import java.text.CharacterIterator
import java.text.StringCharacterIterator


fun File.size(): FileSize {
    return when {
        isDirectory -> {
            listFiles().notNullWithResult {
                var finalSize = Long.MIN_VALUE
                forEach {
                    finalSize += it.length()
                }
                FileSize(finalSize)
            }.isNullWithResult {
                FileSize(Long.MIN_VALUE)
            }!!
        }
        isFile -> {
            FileSize(length())
        }
        else -> {
            FileSize(Long.MIN_VALUE)
        }
    }
}


class FileSize internal constructor(length: Long) {
    var fileSize = length
    fun asUnit(unit: Unit): String {
        return byteCountByUnit(unit)
    }

    override fun toString(): String {
        return humanReadableByteCountSI(fileSize)
    }

    private fun humanReadableByteCountSI(bytes: Long): String {
        if (-1000 < fileSize && fileSize < 1000) {
            return "$bytes B"
        }
        val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
        while (fileSize <= -999950 || fileSize >= 999950) {
            fileSize /= 1000
            ci.next()
        }
        return String.format("%.1f %cB", fileSize / 1000.0, ci.current())
    }

    private fun byteCountByUnit(unit: Unit): String {
        val bytes = fileSize.toString()
        when (unit) {
            Unit.B -> {
                return "$bytes B"
            }
            Unit.KB -> {
                return "${fileSize / 1000} KB"
            }
            Unit.MB -> {
                return "${fileSize / 1000000} MB"
            }
            Unit.GB -> {
                return "${fileSize / 1000000000} GB"
            }
            Unit.TB -> {
                return "${fileSize / 1000000000000} TB"
            }
            Unit.PB -> {
                return "${fileSize / 1000000000000000} PB"
            }
            else -> {
                return "$bytes B"
            }
        }
    }

    enum class Unit {
        /**
         * Unit size of bytes
         */
        B,


        /**
         * Unit size of kilobytes
         */
        KB,


        /**
         * Unit size of megabytes
         */
        MB,


        /**
         * Unit size of gigabytes
         */
        GB,


        /**
         * Unit size of terrabytes
         */
        TB,


        /**
         * Unit size of petabytes
         */
        PB,

        /**
         * automatic unit size
         */
        AUTO
    }
}