package extensioneer.io


import java.io.File
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import kotlin.math.abs

/**
 * get the [File]s size in a human readable format
 * Works with both files and directories.
 * @return returns a [FileSize] calculated in the SI format
 */
fun File.size(): FileSize {
    return when {
        isDirectory -> {
            FileSize(directorySize(this))
        }
        isFile -> {
            FileSize(length())
        }
        else -> {
            FileSize(0.toLong())
        }
    }
}

/**
 * get the [File]s size in a human readable format
 * Works with both files and directories.
 * @param unitSystem calculate the filesize in this format
 * @return returns a [FileSize] calculated in the specified format
 */
fun File.size(unitSystem: UnitSystem): FileSize {
    return when {
        isDirectory -> {
            FileSize(directorySize(this), unitSystem)
        }
        isFile -> {
            FileSize(length(), unitSystem)
        }
        else -> {
            FileSize(0.toLong(), unitSystem)
        }
    }
}

private fun directorySize(dir: File): Long {
    if (dir.listFiles() != null) {
        var finalSize: Long = 0
        dir.listFiles()!!.forEach {
            finalSize += if (it.isDirectory) {
                directorySize(it)
            } else {
                it.length()
            }
        }
        return FileSize(finalSize).fileSize
    } else {
        return FileSize(0.toLong()).fileSize
    }
}

class FileSize {
    var fileSize: Long
    var unitSystem = UnitSystem.SI

    internal constructor(length: Long) {
        fileSize = length
    }

    internal constructor(length: Long, unitSystem: UnitSystem) {
        fileSize = length
        this.unitSystem = unitSystem
    }

    fun asUnit(unit: Unit): String {
        return byteCountByUnit(unit)
    }

    override fun toString(): String {
        return if (unitSystem == UnitSystem.SI) humanReadableByteCountSI(fileSize) else humanReadableByteCountBin(
            fileSize
        )
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

    private fun humanReadableByteCountBin(bytes: Long): String {
        val absB = if (bytes == Long.MIN_VALUE) Long.MAX_VALUE else abs(bytes)
        if (absB < 1024) {
            return "$bytes B"
        }
        var value = absB
        val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
        var i = 40
        while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
            value = value shr 10
            ci.next()
            i -= 10
        }
        value *= java.lang.Long.signum(bytes).toLong()
        return String.format("%.1f %ciB", value / 1024.0, ci.current())
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

enum class UnitSystem {
    SI,
    BINARY
}