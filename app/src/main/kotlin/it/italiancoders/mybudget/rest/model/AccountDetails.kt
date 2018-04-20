package it.italiancoders.mybudget.rest.model

import com.fasterxml.jackson.annotation.JsonProperty


class AccountDetails : Account() {

    @JsonProperty("incomingCategoriesAvailable")
    val incomingCategoriesAvailable: List<Category>? = null
    @JsonProperty("expenseCategoriesAvailable")
    val expenseCategoriesAvailable: List<Category>? = null

    @JsonProperty("totalMonthlyIncoming")
    val totalMonthlyIncoming: Double? = null
    @JsonProperty("totalMonthlyExpense")
    val totalMonthlyExpense: Double? = null

    @JsonProperty("incomingOverviewMovement")
    var incomingOverviewMovement: Map<String, Double>? = null
    @JsonProperty("expenseOverviewMovement")
    val expenseOverviewMovement: Map<String, Double>? = null

    @JsonProperty("lastMovements")
    val lastMovements: List<Movement>? = null

    @JsonProperty("members")
    val members: List<User>? = null

    @JsonProperty("administrators")
    val administrators: List<String>? = null

    @JsonProperty("numberOfPendingAccountInvites")
    val numberOfPendingAccountInvites: Int? = null
}