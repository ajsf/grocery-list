package com.doublea.handler

import com.doublea.*
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import javax.validation.Validator

class ApiHandler(private val validator: Validator,
                 private val groceryListService: GroceryListService) {

    fun getGroceryLists(req: ServerRequest) = ok().body(
            ReactiveSecurityContextHolder
                    .getContext()
                    .map {
                        groceryListService.fetchGroceryListsByUser(it.authentication.name)
                    }.map { it.map { it.toDTO() } }
                    .toMono())

    fun getGroceryList(req: ServerRequest): Mono<ServerResponse> {
        val id = req.pathVariable("id").toLong()
        val groceryListDTO: GroceryListDTO? = groceryListService
                .fetchGroceryList(id)?.toDTO()

        return if (groceryListDTO != null) {
            ok().body(groceryListDTO.toMono())
        } else {
            notFound().build()
        }
    }

    fun saveGroceryList(req: ServerRequest): Mono<ServerResponse> = req.preProcess()
            .zipWith(ReactiveSecurityContextHolder.getContext())
            .flatMap {
                it.t1.save(it.t2.authentication)
            }

    fun updateGroceryList(req: ServerRequest): Mono<ServerResponse> = req.preProcess()
            .flatMap { it.update() }

    fun deleteGroceryList(req: ServerRequest): Mono<ServerResponse> = req.preProcess()
            .flatMap { it.delete() }

    fun getGroceryItems(req: ServerRequest) = ServerResponse.ok().body(
            Mono.just(groceryListService.fetchAllGroceryItems().map { it.toDTO() })
    )

    private fun ServerRequest.preProcess() = this.toMono().clean().validate()

    private fun ServerRequest.toMono() = this.bodyToMono<GroceryListDTO>()

    private fun Mono<GroceryListDTO>.validate() = this.map { groceryList ->
        val violations = validator.validate(groceryList)
        if (violations.isNotEmpty()) {
            groceryList.fieldErrors = violations.map {
                FieldErrorDTO(it.propertyPath.toString(), it.message)
            }
        }
        groceryList
    }

    private fun Mono<GroceryListDTO>.clean() = this.map {
        it.copy(title = it.title.trim(), groceries = it.groceries.map { groceries ->
            groceries.copy(grocery = groceries.grocery.trim().toLowerCase())
        })
    }

    private fun GroceryListDTO.save(auth: Authentication): Mono<ServerResponse> = when {
        fieldErrors != null -> unprocessableEntity().body(this.toMono())
        id != null -> {
            genericError = "List already has an ID. Use update operation."
            unprocessableEntity().body(Mono.just(this))
        }
        else -> {
            ok().body(groceryListService.saveGroceryList(this.toGroceryList(auth.name)).toDTO().toMono())
        }
    }

    private fun GroceryListDTO.update(): Mono<ServerResponse> = when {
        fieldErrors != null -> unprocessableEntity().body(this.toMono())
        id == null -> {
            genericError = "List does not have an ID. Use save operation."
            unprocessableEntity().body(Mono.just(this))
        }
        else -> groceryListService
                .updateGroceryList(toGroceryList(""))
                .map { ok().body(Mono.just(it)) }
                .orElseGet {
                    genericError = "Unable to update list. ID invalid."
                    ServerResponse.badRequest().body(Mono.just(this))
                }
    }

    private fun GroceryListDTO.delete(): Mono<ServerResponse> = when {
        fieldErrors != null -> unprocessableEntity().body(Mono.just(this))
        else -> groceryListService
                .deleteGroceryList(this.toGroceryList(""))
                .map { ok().body(it.toMono()) }
                .orElseGet {
                    this.genericError = "Unable to delete item."
                    badRequest().body(this.toMono())
                }
    }
}