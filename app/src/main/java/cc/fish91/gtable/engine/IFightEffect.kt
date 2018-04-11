package cc.fish91.gtable.engine

interface IFightEffect<P> {
    fun onFightStart(p: P)
    fun onFight(p: P)
    fun onFightEnd(p: P)
}