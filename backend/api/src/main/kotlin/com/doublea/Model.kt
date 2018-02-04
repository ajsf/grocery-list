package com.doublea

import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.Size

data class GroceryListDTO(
        @get:Size(min = 2) val title: String,
        val groceries: List<GroceryListEntryDTO>,
        val createDate: Date = Date(),
        val modifiedDate: Date = Date(),
        val id: Long? = null
) : Validatable()

@JsonInclude(JsonInclude.Include.NON_NULL)
open class Validatable(
        var fieldErrors: List<FieldErrorDTO>? = null,
        var genericError: String? = null
)

data class FieldErrorDTO(val field: String, val message: String)

data class GroceryListEntryDTO(
        @get:Min(1)
        val quantity: Int,
        val quantityUnit: QuantityUnit,
        @get:Size(min = 2)
        val grocery: String
)

fun GroceryListDTO.toGroceryList() = GroceryList(
        title,
        createDate,
        modifiedDate,
        groceries.toListOfGroceryListEntry(),
        id
)

private fun List<GroceryListEntryDTO>.toListOfGroceryListEntry() = this
        .map { it.toGroceryListEntry() }.toList()

private fun GroceryListEntryDTO.toGroceryListEntry() =
        GroceryListEntry(quantity, quantityUnit, GroceryItem(grocery))

fun GroceryList.toDTO() = GroceryListDTO(title, groceries.toDTO(), createDate, modifiedDate, id)

private fun List<GroceryListEntry>.toDTO() = this
        .map { it.toDTO() }.toList()

private fun GroceryListEntry.toDTO() =
        GroceryListEntryDTO(quantity, quantityUnit, groceryItem.name)

fun GroceryItem.toDTO() = this.name