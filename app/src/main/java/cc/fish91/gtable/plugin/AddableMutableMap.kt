package cc.fish91.gtable.plugin

class AddableMutableMap<K> : LinkedHashMap<K, ChangableInt>() {
    fun add(key: K, value: Int): Int? {
        if (key in keys) {
            get(key)!!.value += value
        } else {
            put(key, ChangableInt(value))
        }
        return getV(key)
    }

    fun replace(key: K, value: Int) {
        if (key in keys)
            get(key)!!.value = value
        else
            add(key, value)
    }

    fun getV(key: K) = super.get(key)?.value
}