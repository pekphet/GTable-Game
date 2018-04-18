package cc.fish91.gtable.plugin

class AddableMutableMap<K> : LinkedHashMap<K, ChangableInt>() {
    fun put(key: K, value: Int): ChangableInt? {
        if (key in keys) {
            get(key)!!.value += value
        } else {
            put(key, ChangableInt(value))
        }
        return get(key)
    }
}