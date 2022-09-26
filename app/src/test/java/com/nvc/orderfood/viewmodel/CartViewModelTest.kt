package com.nvc.orderfood.viewmodel

import com.nvc.orderfood.model.Cart
import com.nvc.orderfood.repository.CartRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CartViewModelTest {

    @MockK
    private lateinit var viewModel: CartViewModel
    @BeforeEach
    fun setUp() {
        viewModel = mockk()
        every { viewModel.listCart.value } returns listOf<Cart>()
        every { viewModel.getDataRemote() } returns Unit
    }

    @Test
    fun getListCart() {
        println(viewModel.listCart.value)
        Assertions.assertEquals(viewModel.listCart.value,listOf<Cart>())
    }

    @Test
    fun getDataRemote() {
        viewModel.getDataRemote()
    }

    @Test
    fun getTotal() {
    }

    @Test
    fun plusCart() {
    }

    @Test
    fun minusCart() {
    }

    @Test
    fun insertCart() {
    }

    @Test
    fun deleteCart() {
    }

    @Test
    fun addToCart() {
    }
}