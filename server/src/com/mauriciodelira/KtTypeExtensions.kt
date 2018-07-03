package com.mauriciodelira

object KtTypeExtensions {
  /** retorna a lista caso ela seja preenchida, ou caso esteja vazia,
   * aplica uma função que retorne uma outra lista do mesmo tipo **/
  fun <E: Any, T : Collection<E>> T.getOrElse(f: () -> T): T {
    return if(this.isEmpty())
      f()
    else
      this
  }
}