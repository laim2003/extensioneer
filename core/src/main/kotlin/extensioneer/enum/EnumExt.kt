package extensioneer.enum

/**
 * Converts the give enum to it's ordinal
 */
@Suppress("NOTHING_TO_INLINE")
inline fun <T : Enum<T>> T.toInt(): Int = this.ordinal

/**
 * Converts the number generated by [toInt] back to the enum that was converted.
 * THIS METHOD ONLY SHOULD BE USED WITH NUMBERS THAT DEFINITELY REPRESENT THE ORIGINAL ENUM.
 *
 * Example:
 * Using androidx.room type converters to save an enum value into a database:
 * ```
 * class MyRoomConverter {
 *    @TypeConverter fun myEnumToTnt(value: MyEnum?) = value?.toInt()
 *    @TypeConverter fun intToMyEnum(value: Int?) = value?.toEnum<MyEnum>()
 * }
 * ```
 */
inline fun <reified T : Enum<T>> Int.toEnum(): T = enumValues<T>()[this]