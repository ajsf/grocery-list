package com.doublea

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "grocery_list")
data class GroceryList(
        val title: String,

        @Column(name = "create_date")
        val createDate: Date,

        @Column(name = "modified_date")
        val modifiedDate: Date,

        @ElementCollection(fetch = FetchType.EAGER)
        val groceries: List<GroceryListEntry>,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long? = null
)

@Embeddable
data class GroceryListEntry(
        val quantity: Int,

        @Column(name = "quantity_unit")
        val quantityUnit: QuantityUnit,

        @ManyToOne(fetch = FetchType.EAGER)
        val groceryItem: GroceryItem
)

@Entity
@Table(name = "grocery_item")
data class GroceryItem(
        @Id
        val name: String
)

enum class QuantityUnit {
    BLANK, LARGE, SMALL, MEDIUM, BOX, JAR, CAN,
    BOTTLE, CASE
}