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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cupcake.databinding.FragmentSummaryBinding
import com.example.cupcake.model.OrderViewModel

/**
 * [SummaryFragment] contém o resumo do pedido com um botão para enviar os detalhes a outro app
 */
class SummaryFragment : Fragment() {

    /*
    Objeto binding, criado como um objeto que pode ser nulo para
    poder ser acessado nos métodos onCreateView(), onViewCreated e
    no onDestroyView().
     */
    private var binding: FragmentSummaryBinding? = null

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
        val fragmentBinding = FragmentSummaryBinding.inflate(inflater, container, false)
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
            summaryFragment = this@SummaryFragment
        }
    }

    /**
     * Submit the order by sharing out the order details to another app via an implicit intent.
     */
    /**
     * Manda o pedido para outro aplicativo por meio de uma intent implicita
     */
    fun sendOrder() {
        // Define o resumo do pedido com as informações do viewModel
        val numberOfCupcakes = sharedViewModel.quantity.value ?: 0
        val orderSummary = getString(
            R.string.order_details,
            resources.getQuantityString(R.plurals.cupcakes, numberOfCupcakes, numberOfCupcakes),
            sharedViewModel.flavor.value.toString(),
            sharedViewModel.date.value.toString(),
            sharedViewModel.price.value.toString()
        )

        // Cria uma intent implicita ACTION_SEND com o resumo do pedido nos extras da intent
        val intent = Intent(Intent.ACTION_SEND)
            .setType("text/plain")
            .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.new_cupcake_order))
            .putExtra(Intent.EXTRA_TEXT, orderSummary)

        // Checa se existe um aplicativo que possa lidar com a intent antes de iniciá-la
        if (activity?.packageManager?.resolveActivity(intent, 0) != null) {
            // Start a new activity with the given intent (this may open the share dialog on a
            // Inicia um novo activity com o intent (desse jeito, se tiver mais de um
            // aplicativo que lide com a intent, ele poderá ser escolhido)
            startActivity(intent)
        }
    }

    /**
     * Cancela o pedido e reinicia usando o resetOrder()
     */
    fun cancelOrder() {
        // Reseta o pedido no viewModel
        sharedViewModel.resetOrder()

        // Navega de volta para o inicio
        findNavController().navigate(R.id.action_summaryFragment_to_startFragment)
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