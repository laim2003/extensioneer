package extensioneer.org.json

import org.json.JSONObject

inline fun JSONObject.forEach(action: (Pair<String, Any>) -> Unit): Unit {
    for (index in 0 until length())
        action(
            Pair(names().getString(index),
                get(names().getString(index)))
        )
}