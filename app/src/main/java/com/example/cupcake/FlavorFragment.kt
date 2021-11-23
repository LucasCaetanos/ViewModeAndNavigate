/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.cupcake

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentFlavorBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [FlavorFragment] permite que o usuario selecione o sabor do cupcake para o pedido.
 */
class FlavorFragment : Fragment() {

    /*
    Objeto binding, criado como um objeto que pode ser nulo para
    poder ser acessado nos métodos onCreateView(), onViewCreated e
    no onDestroyView().
     */
    private var binding: FragmentFlavorBinding? = null

    /*
    Esse sharedViewModel é do tipo OrderViewModel e utilizamos o
    activityViewModels() para deixá-lo no escopo da Activity principal
    e podermos ter acesso aos métodos e atributos de forma mais
    direta.
     */
    private val sharedViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Foi criado uma instância do Binding, inflando o layout
        //do fragment
        val fragmentBinding = FragmentFlavorBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {

            // Especifica o fragment como o lifecycle owner
            lifecycleOwner = viewLifecycleOwner

            // Define o view model para uma propriedade no binding
            viewModel = sharedViewModel

            // Define o fragment
            flavorFragment = this@FlavorFragment
        }
    }

    /**
     * Vai para a próxima tela para escolher a data de entrega.
     */
    fun goToNextScreen() {
        findNavController().navigate(R.id.action_flavorFragment_to_pickupFragment)
    }

    /**
     *
     * Cancela p pedido e reinicia usando o resetOrder()
     */
    fun cancelOrder() {
        // Reseta o pedido no viewModel
        sharedViewModel.resetOrder()

        // Navega de volta para o inicio
        findNavController().navigate(R.id.action_flavorFragment_to_startFragment)
    }

    /**
     * Esse método do ciclo de vida é chamado quando a hierarquia de views
     * associada ao fragment é removida. Como resultado, o objeto
     * binding volta a ficar nulo
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}