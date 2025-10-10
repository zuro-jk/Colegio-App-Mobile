package com.jeyix.schooljeyix.domain.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Para resultados exitosos. Contiene los datos que no pueden ser nulos.
     */
    class Success<T>(data: T) : Resource<T>(data = data)

    /**
     * Para fallos. Contiene un mensaje de error que no puede ser nulo.
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data = data, message = message)

    /**
     * Para representar el estado de carga, por ejemplo, mientras la llamada a la red está en proceso.
     */
    class Loading<T>(data: T? = null) : Resource<T>(data = data)
}