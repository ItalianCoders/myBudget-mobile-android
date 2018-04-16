package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class Page<T> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("contents")
    var content: List<T>? = null

    var isFirst: Boolean = false

    var isLast: Boolean = false

    var number: Int = 0

    var totalPages: Int = 0

    var size: Int = 0

    var totalElements: Long = 0
}