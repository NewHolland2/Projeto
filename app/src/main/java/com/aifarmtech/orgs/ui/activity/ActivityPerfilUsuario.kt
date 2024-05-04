package com.aifarmtech.orgs.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.aifarmtech.orgs.databinding.ActivityListaProdutoPerfilUsuarioBinding
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ActivityPerfilUsuario : UsuarioBaseActivity() {
    private val binding by lazy {
        ActivityListaProdutoPerfilUsuarioBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        preencheCampos()
        configuraBotaoSairApp()
    }

    private fun preencheCampos() {
        lifecycleScope.launch {
            usuario.filterNotNull().collect{usuarioLogado ->
                binding.activityPerfilUsuarioId.text = usuarioLogado.id
                binding.activitiyPerfilUsuarioNome.text = usuarioLogado.id
            }
        }
    }

    private fun configuraBotaoSairApp() {
        binding.activityPerfilUsuarioBotaoSairDoApp.setOnClickListener{
            lifecycleScope.launch {
                deslogaUsuario()
            }
        }
    }
}